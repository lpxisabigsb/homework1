
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class OpenstackClient {
    Client client;
    String urlBase = "http://192.168.47.150/";

    public String ah = "X-Auth-Token";
    public String av = "";

    public static void print(String msg) {
        System.out.println(msg);
    }

    public String readFile(String fn) throws Exception {
        File file = new File(fn);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public String get(String cmd, String pn[], String pv[]) throws Exception {
        WebTarget t = client.target(urlBase + cmd);
        if (pn != null) {
            for (int i = 0; i < pn.length; i++) {
                t = t.queryParam(pn[i], pv[i]);
            }
        }
        Builder b = t.request();
        b = b.header(ah, av);
        Response r = b.get();
        return r.readEntity(String.class);
    }

    public String get(String cmd) throws Exception {
        return get(cmd, null, null);
    }

    public String post(String cmd, String json) throws Exception {
        WebTarget t = client.target(urlBase + cmd);
        Builder b = t.request();
        b = b.header(ah, av);
        Entity<String> enty = Entity.entity(json, MediaType.APPLICATION_JSON);
        Response r = b.post(enty);
        String rjson = r.readEntity(String.class);
        return rjson;
    }

    public String post(String cmd, String json, String headField) throws Exception {
        WebTarget t = client.target(urlBase + cmd);
        Builder b = t.request();
        Entity<String> enty = Entity.entity(json, MediaType.APPLICATION_JSON);
        Response r = b.post(enty);
        r.readEntity(String.class);
        String hf = r.getHeaderString(headField);
        return hf;
    }

    public void del(String cmd) throws Exception {
        WebTarget t = client.target(urlBase + cmd);
        Builder b = t.request();
        b = b.header(ah, av);
        Response r = b.delete();
        String rjson = r.readEntity(String.class);
        // print(rjson);
    }

    public void open() throws Exception {
        print("Login...");

        client = ClientBuilder.newClient();
        String ui = readFile("user.txt");
        String tk = post("identity/v3/auth/tokens", ui, "X-Subject-Token");
        av = tk;

        print("token: " + tk);
    }

    public void close() throws Exception {
        client.close();
    }

    public void listImgs() throws Exception {
        print("List images...");

        String jstr = get("image/v2/images");
        // print(jstr);

        JsonReader jreader = Json.createReader(new StringReader(jstr));
        JsonObject jobj = jreader.readObject();
        JsonArray imgs = jobj.getJsonArray("images");
        for (int i = 0; i < imgs.size(); i++) {
            JsonObject img = imgs.getJsonObject(i);
            String imgName = img.getString("name");
            print(imgName);
        }
    }

    public void createVM() throws Exception {
        print("Create virtual machine...");

        String si = readFile("s1.txt");
        String rt = post("compute/v2.1/servers", si);
        // print(rt);
    }

    public void listVms() throws Exception {
        print("List virtual machines...");

        String jstr = get("compute/v2.1/servers");
        // print(jstr);

        JsonReader jreader = Json.createReader(new StringReader(jstr));
        JsonObject jobj = jreader.readObject();
        JsonArray vms = jobj.getJsonArray("servers");
        for (int i = 0; i < vms.size(); i++) {
            JsonObject vm = vms.getJsonObject(i);
            String vmName = vm.getString("name");
            print(vmName);
        }
    }

    public void delVMs() throws Exception {
        print("Delete virtual machines...");

        String jstr = get("compute/v2.1/servers");
        // print(jstr);

        JsonReader jreader = Json.createReader(new StringReader(jstr));
        JsonObject jobj = jreader.readObject();
        JsonArray vms = jobj.getJsonArray("servers");
        for (int i = 0; i < vms.size(); i++) {
            JsonObject vm = vms.getJsonObject(i);
            String id = vm.getString("id");
            del("compute/v2.1/servers/" + id);
            print("delete virtual machine, id=" + id);
        }
    }

    public int readCmd() throws Exception {
        String notes[] = { "0: exit", "1: create server", "2: list server", "3: delete server","4: list images" };
        for (String note : notes) {
            print(note);
        }
        int cmd=0;
        while(true) {
            cmd = System.in.read();
            if(cmd=='\n' || cmd =='\r') {
                continue;
            }
            else {
                break;
            }
        }
        return cmd - '0';
    }

    public static void main(String[] args) {
        print("Begin");
        try {
            OpenstackClient oc = new OpenstackClient();
            oc.open();
            int cmd;
            while ((cmd = oc.readCmd()) != 0) {
                print(""+cmd);
                switch (cmd) {
                    case 1:
                        oc.createVM();
                        break;
                    case 2:
                        oc.listVms();
                        break;
                    case 3:
                        oc.delVMs();
                        break;
                    case 4:
                        oc.listImgs();
                        break;
                    default:
                        print("Unknown command");
                }
            }
            oc.close();

        } catch (Exception ec) {
            ec.printStackTrace();
        }
        print("End");

    }

}
