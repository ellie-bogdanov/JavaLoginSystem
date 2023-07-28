package Constants;

public class ConstantsDB {
    public static final int MAX_USERNAME_LENGTH = 20;
    public static final int MIN_USERNAME_LENGTH = 5;
    public static final int MAX_PASSWORD_LENGTH = 30;
    public static final int MIN_PASSWORD_LENGTH = 7;
    public static final int CORRECT_PASSWORD_RETURN_VAL  = 1;
    public static final int INCORRECT_PASSWORD_RETURN_VAL = 2;
    public static final int MAX_PASSWORD_RESETS_REACHED_RETURN_VAL = 3;
    public static final int WEAK_PASSWORD_RETURN_VAL = 4;
    public static final int WEAK_USERNAME_RETURN_VAL = 5;
    public static final int USER_ALREADY_EXISTS_RETURN_VAL = 6;
    public static final int USER_NOT_EXISTS_RETURN_VAL = 7;
    public static final boolean MESSAGE_TOO_LONG_RETURN_VAL = true;    
    
    public static final int USERNAME_INDEX = 2;
    public static final int PASSWORD_INDEX = 3;
    public static final int PASSWORD_RESET_INDEX = 4;
    public static final int RESET_TIMER_INDEX = 5;
    public static final int MESSAGE_INDEX = 6;
    public static final int MAX_PASSWORD_RESETS = 3;
    public static final long RESET_TIME_TO_ADD = 20000L;
    public static final String USERNAME_NULL_MESSAGE = "username can not be null";
    public static final String USERNAME_LENGTH_EXCEPTION = "username must be between " + MIN_USERNAME_LENGTH + " and " + MAX_USERNAME_LENGTH + " characters";
    public static final String USERNAME_CONTAINS_SYMBOLS_EXCEPTION = "username must contain only letters and digits";
    public static final String HASHING_ALG = "SHA-256";
    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static final String DRIVER_CONNECTION_NAME = "jdbc:mysql://localhost:3306/";

    public static final String ROOT_NAME = "root";       
    public static final String DB_PASSWORD = "samgy2010";
    public static final String DB_NAME = "mydb";

    

}
