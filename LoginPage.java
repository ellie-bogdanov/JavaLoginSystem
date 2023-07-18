package LoginSystem;

 import TextEditor.TextEditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.sql.SQLException;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Server.Client;

public class LoginPage implements ActionListener{

    static final int MAX_USERNAME_LENGTH = 20;
    static final int MIN_USERNAME_LENGTH = 5;

    static final int MAX_PASSWORD_LENGTH = 30;
    static final int MIN_PASSWORD_LENGTH = 7;

    static final int FRAME_WIDTH = 420;
    static final int FRAME_HEIGHT = 420;
 
    static final int USER_LABEL_X = 50;
    static final int USER_LABEL_Y = 100;
    static final int USER_LABEL_WIDTH = 75;
    static final int USER_LABEL_HEIGHT = 25;
 
    static final int USER_FIELD_X = 125;
    static final int USER_FIELD_Y = 100;
    static final int USER_FIELD_WIDTH = 200;
    static final int USER_FIELD_HEIGHT = 25;
 
    static final int MESSAGE_LABEL_X = 125;
    static final int MESSAGE_LABEL_Y = 250;
    static final int MESSAGE_LABEL_WIDTH = 250;
    static final int MESSAGE_LABEL_HEIGHT = 35;
 
    static final int BUTTON_X = 125;
    static final int BUTTON_Y = 200;
    static final int BUTTON_WIDTH = 100;
    static final int BUTTON_HEIGHT = 25;
 
    static final int FONT_SIZE = 25;


    
    private JFrame frame = new JFrame();
    private JButton loginButton = new JButton("Login");
    private JButton registerButton = new JButton("Register");
    
    private JTextField userIDField = new JTextField();
    private JPasswordField userPasswordField = new JPasswordField();
    private JLabel userIDLabel = new JLabel("user ID:");
    private JLabel userPasswordLabel = new JLabel("Passowrd:");
    JLabel messagLabel = new JLabel();



    public LoginPage() {


        this.userIDLabel.setBounds(USER_LABEL_X, USER_LABEL_Y, USER_LABEL_WIDTH, USER_LABEL_HEIGHT);
        this.userPasswordLabel.setBounds(USER_LABEL_X, USER_LABEL_Y + 50, USER_LABEL_WIDTH, USER_LABEL_HEIGHT);

        this.messagLabel.setBounds(MESSAGE_LABEL_X, MESSAGE_LABEL_Y, MESSAGE_LABEL_WIDTH, MESSAGE_LABEL_HEIGHT);
        this.messagLabel.setFont(new Font(null, Font.ITALIC, FONT_SIZE));

        this.userIDField.setBounds(USER_FIELD_X, USER_FIELD_Y, USER_FIELD_WIDTH, USER_FIELD_HEIGHT);

        this.userPasswordField.setBounds(USER_FIELD_X, USER_FIELD_Y + 50, USER_FIELD_WIDTH, USER_FIELD_HEIGHT);

        this.loginButton.setBounds(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.loginButton.setFocusable(false);
        this.loginButton.addActionListener(this);

        this.registerButton.setBounds(BUTTON_X + 100, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.registerButton.setFocusable(false);
        this.registerButton.addActionListener(this);

        this.frame.add(this.userIDLabel);
        this.frame.add(this.userPasswordLabel);
        this.frame.add(this.userIDField);
        this.frame.add(this.userPasswordField);
        this.frame.add(this.loginButton);
        this.frame.add(this.registerButton);
        this.frame.add(this.messagLabel);
 

        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private static boolean checkIfValidPassAndID(String username, String password) {
        if( username == null || username.length() > MAX_USERNAME_LENGTH || username.length() < MIN_USERNAME_LENGTH)
        {
            return false;
        }

        if(password == null || password.length() > MAX_PASSWORD_LENGTH || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }

        for(int i = 0; i < username.length(); i++) {
            if(!Character.isLetter(username.charAt(i)) && !Character.isDigit(username.charAt(i))) {
                return false;
            }
        }

        boolean symbolFlag = false;
        boolean digitFlag = false;
        boolean letterFlag = false;

        for(int i = 0; i < password.length(); i++) {
            if(!Character.isLetter(password.charAt(i)) && !Character.isDigit(password.charAt(i))) {
                symbolFlag = true;
            }

            if(Character.isLetter(password.charAt(i))) {
                letterFlag = true;
            }

            if(Character.isDigit(password.charAt(i))) {
                digitFlag = true;
            }

            if(symbolFlag && digitFlag && letterFlag) {
                return true;
            }
        }


        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //register logic
        if(e.getSource().equals(this.registerButton)) {
            //this.userIDField.setText("");
            //this.userPasswordField.setText("");
            String userID = this.userIDField.getText();
            String userPassword = String.valueOf(userPasswordField.getPassword());
            if(checkIfValidPassAndID(userID, userPassword)) {
                try {
                    if(DataBaseController.checkForExistingUser(userID, userPassword)) {
                        this.userIDField.setText("");
                        this.userPasswordField.setText("");
                        this.messagLabel.setForeground(Color.GREEN);
                        this.messagLabel.setText("Registered Successfully");
                    }
                    else {
                        this.messagLabel.setForeground(Color.RED);
                        this.messagLabel.setText("User Exists");
                    }


                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }

        if(e.getSource().equals(this.loginButton)) {
            //login logic
            String userID = this.userIDField.getText();
            String userPassword = String.valueOf(userPasswordField.getPassword());

            try {
                boolean flag = DataBaseController.authorizeUser(userID, userPassword); 
                if(flag) {
                    this.messagLabel.setForeground(Color.GREEN);
                    this.messagLabel.setText("Login Successful");
                    this.frame.dispose();
                    new TextEditor(userID);

            
                }
                else {
                    this.messagLabel.setForeground(Color.RED);
                    this.messagLabel.setText("Failed to log in");
                }
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
