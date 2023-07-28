package LoginSystem;

import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.PreparedStatement;
import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.security.spec.InvalidKeySpecException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import Constants.ConstantsDB;




//data base consists of columns [ID, username, password, numOfPasswordChanges, passwordChangeTimer, message]
public class DataBaseController {

    Connection con;

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()\u2013[{}]:;',?/*~$^+=<>]).{" + ConstantsDB.MIN_PASSWORD_LENGTH + "," + ConstantsDB.MAX_PASSWORD_LENGTH + "}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public DataBaseController() throws SQLException, ClassNotFoundException {
            Class.forName(ConstantsDB.DRIVER_NAME);
            this.con = DriverManager.getConnection(ConstantsDB.DRIVER_CONNECTION_NAME + ConstantsDB.DB_NAME, ConstantsDB.ROOT_NAME,ConstantsDB.DB_PASSWORD);
    }

    private ResultSet doesUserExists(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        PreparedStatement stmt = this.con.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public boolean authorizeUser(String username, String password) throws Exception {


            String hashedPassword = new String(passwordHash(password), "UTF-8");


            ResultSet rs = doesUserExists(username);
            
            boolean userExists = rs.next();
            boolean correctLogin = false;
            if(userExists) {
                correctLogin = rs.getString(ConstantsDB.USERNAME_INDEX).equals(username) && rs.getString(ConstantsDB.PASSWORD_INDEX).equals(hashedPassword); 
            }

            return correctLogin;

    }

    public int register(String username, String password) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, InvalidUsernameStringException {
        if(!checkIfUsernameValid(username)) {
            return ConstantsDB.WEAK_USERNAME_RETURN_VAL;
        }
        
        if(!checkIfPasswordValid(password)) {
            return ConstantsDB.WEAK_PASSWORD_RETURN_VAL;
        }

        String hashedPassword = new String(passwordHash(password), "UTF-8");

        ResultSet rs = doesUserExists(username);
        boolean doesUserExists = rs.next();
        PreparedStatement stmt;
        if(!doesUserExists) {
            Statement idCheck = con.createStatement();
            String numberOfUsersQuery = "SELECT COUNT(*) FROM users";
            ResultSet userIDRs = idCheck.executeQuery(numberOfUsersQuery);
            String addUserSql = "";

            userIDRs.next();
            int newID = 1 + userIDRs.getInt(1);

            addUserSql = "INSERT INTO users(ID, username, password) VALUES(?, ?, ?)";
            stmt = this.con.prepareStatement(addUserSql);
            stmt.setInt(1, newID);
            stmt.setString(2, username);
            stmt.setString(3, hashedPassword);
            stmt.executeUpdate();

        }
        if(doesUserExists) {
            return ConstantsDB.USER_ALREADY_EXISTS_RETURN_VAL;
        }
        return ConstantsDB.USER_NOT_EXISTS_RETURN_VAL;
    }

    public int resetPassword(String username, String oldPassword, String newPassword) throws ClassNotFoundException, SQLException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException {

        String checkUserQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement stmt = this.con.prepareStatement(checkUserQuery);
        stmt.setString(1, username);
        String oldPasswordHashed = new String(passwordHash(oldPassword), "UTF-8");
        stmt.setString(2, oldPasswordHashed);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next()) {
            return ConstantsDB.INCORRECT_PASSWORD_RETURN_VAL;
        }

        if(!checkIfPasswordValid(newPassword)) {
            return ConstantsDB.WEAK_PASSWORD_RETURN_VAL;
        }

        String newPasswordHashed = new String(passwordHash(newPassword), "UTF-8");
        int numOfChanges = rs.getInt(ConstantsDB.PASSWORD_RESET_INDEX);
        Timestamp usersResetTimer = rs.getTimestamp(ConstantsDB.RESET_TIMER_INDEX);

        if(!usersResetTimer.after(new Timestamp(System.currentTimeMillis()))) {
            numOfChanges = 0;
        }
        if(numOfChanges >= ConstantsDB.MAX_PASSWORD_RESETS) {
            return ConstantsDB.MAX_PASSWORD_RESETS_REACHED_RETURN_VAL;
        }

        
        String query = "UPDATE users SET password = ? WHERE username = ?"; 
        try {
            stmt = this.con.prepareStatement(query);
            stmt.setString(1, newPasswordHashed);
            stmt.setString(2, username);
            stmt.executeUpdate();

            numOfChanges += 1;
            query = "UPDATE users SET numOfPasswordChanges = ? WHERE username = ?";
            stmt = this.con.prepareStatement(query);
            stmt.setInt(1, numOfChanges);
            stmt.setString(2, username);
            stmt.executeUpdate();

            Timestamp newUserResetTimer = new Timestamp(System.currentTimeMillis() + ConstantsDB.RESET_TIME_TO_ADD);
            System.out.println(newUserResetTimer);
            String timeWithoutMillis = newUserResetTimer.toString().split("\\.")[0];
            System.out.println(newUserResetTimer.toString());

            query = "UPDATE users SET passwordChangeTimer = ? WHERE username = ?";
            stmt = this.con.prepareStatement(query);
            stmt.setString(1, timeWithoutMillis);
            stmt.setString(2, username);
            stmt.executeUpdate();

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ConstantsDB.CORRECT_PASSWORD_RETURN_VAL;

    }

    public boolean saveMessage(String username, String message, int messageMaxLength) throws SQLException {

        if(message.length() > messageMaxLength) {
            return ConstantsDB.MESSAGE_TOO_LONG_RETURN_VAL;
        }
        String query = "UPDATE users SET message = " + "'" + message + "'" + " WHERE username = " + "'" + username + "'";
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(query);
        return !ConstantsDB.MESSAGE_TOO_LONG_RETURN_VAL;

    }

    public ArrayList<String> displayMessage() throws SQLException {
        String query = "SELECT * FROM users";
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<String> allMessages = new ArrayList<String>();
        while(rs.next()) {
            String username = rs.getString(ConstantsDB.USERNAME_INDEX);
            if(rs.getString(ConstantsDB.MESSAGE_INDEX) != null) {
                allMessages.add(username + ": " + rs.getString(ConstantsDB.MESSAGE_INDEX));
            }

        }
        
        return allMessages;
    }


    private static boolean checkIfUsernameValid(String username) throws InvalidUsernameStringException {
        Objects.requireNonNull(username, ConstantsDB.USERNAME_NULL_MESSAGE);
        if(username.length() > ConstantsDB.MAX_USERNAME_LENGTH || username.length() < ConstantsDB.MIN_USERNAME_LENGTH)
        {
            throw new InvalidUsernameStringException(ConstantsDB.USERNAME_LENGTH_EXCEPTION);
        }

        for(int i = 0; i < username.length(); i++) {
            if(!Character.isLetter(username.charAt(i)) && !Character.isDigit(username.charAt(i))) {
                throw new InvalidUsernameStringException(ConstantsDB.USERNAME_CONTAINS_SYMBOLS_EXCEPTION);
            }
        }

        return true;

    }

    private static boolean checkIfPasswordValid(String password) {

        Matcher matcher = pattern.matcher(password);
        boolean validPassword = matcher.matches();

        return validPassword;
    }

    private static byte[] passwordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance(ConstantsDB.HASHING_ALG);
        byte[] hash = messageDigest.digest(password.getBytes("UTF-8"));
        return hash;
        
    }

}
