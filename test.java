import java.sql.Connection;
import java.sql.DriverManager;

public class test {
    public static void main(String[] args) throws Exception {

        Class.forName("org.sqlite.JDBC");  

        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");

        System.out.println("Connected");
    }
}
