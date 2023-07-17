package LoginSystem;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;



//data base consists of columns [ID, username, password]
public class DataBaseController {

    
    private static final String ROOT_NAME = "root";       
    private static final String DB_PASSWORD = "root";
    private static final String DB_NAME = "mydb";

    public DataBaseController() throws SQLException {

    }

    public static boolean authorizeUser(String username, String password) throws Exception {
        
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB_NAME, ROOT_NAME,DB_PASSWORD);
            Statement stmt = con.createStatement();  
            String query = "SELECT * FROM users WHERE username = " + "'" + username + "'" + " AND password =  " + "'" + password + "'";
            ResultSet rs = stmt.executeQuery(query);
            boolean userExists = rs.next();
            if(rs.next()) {
                throw new Exception("Duplicate username in database!");
            }
            rs.previous();
            boolean correctLogin = false;
            if(userExists) {
                correctLogin = rs.getString(2).equals(username) && rs.getString(3).equals(password); 
            }

            return correctLogin;

    }

    public static boolean checkForExistingUser(String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB_NAME + "?verifyServerCertificate=false&useSSL=true", ROOT_NAME, DB_PASSWORD);
        Statement stmt = con.createStatement();
        String query = "SELECT * FROM users WHERE username = " + "'" + username + "'";
        ResultSet rs = stmt.executeQuery(query);
        boolean doesUserNotExist = rs.next() == false;
        
        
        if(doesUserNotExist) {
            String numberOfUsersQuery = "SELECT COUNT(*) FROM users";
            
            ResultSet userIDRs = stmt.executeQuery(numberOfUsersQuery);
            String addUserSql = "";

            userIDRs.next();
            int newID = 1 + userIDRs.getInt(1);
            addUserSql = "INSERT INTO users VALUES(" + newID +", "  + "'" + username + "'" + ", " + "'" + password + "');";

            stmt.executeUpdate(addUserSql);


        }
        return doesUserNotExist;
}

    
}
