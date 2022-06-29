import java.sql.*;

public class TestDB {
    public static void main(String[] args) throws Exception {
        String url="jdbc:sqlserver://localhost:1434;database=db_2007310456;user=sa;password=root";
        Connection con=null;
        Statement stmt=null;
        ResultSet rs=null;

        System.out.println("connection!");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        con= DriverManager.getConnection(url);
        System.out.println("连上了");

        String sql="DELETE FROM STUDENT WHERE Sno='200215126'";
        stmt=con.createStatement();
        stmt.executeUpdate(sql);

        sql="INSERT INTO student(Sno,Sname,Ssex,Sage,Sdept) VALUES ('200215126','lpx','bo',21,'MA')";
        stmt.executeUpdate(sql);

        sql="SELECT  * FROM student";
        rs=stmt.executeQuery(sql);
        System.out.println("学号\t姓名\t性别\t年龄\t所在系\t");
        while (rs.next()){
            System.out.println(rs.getString("Sno")+rs.getString("Sname")+rs.getString("Ssex")+rs.getString("Sage")+rs.getString("Sdept"));
        }
    }
}
