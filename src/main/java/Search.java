import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Search {
    public static void main(String[] args) {
        // 使用默认浏览器打开网页
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported()) {
            try {
                Integer i=6;
                while(true){
                    i--;
                    if(i<=0) break;
                    else{
                        desktop.browse(new URI("http://jwxt2018.gxu.edu.cn/jwglxt/pwdmgr/retake/index.zf"));
                    }
                }
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
