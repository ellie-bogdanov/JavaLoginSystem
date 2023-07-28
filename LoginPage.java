package LoginSystem;

 import TextEditor.TextEditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Constants.ConstantsDB;
import Constants.ConstantsGUI;


public class LoginPage implements ActionListener{

    /*static final int MAX_USERNAME_LENGTH = 20;
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
 
    static final int FONT_SIZE = 25;*/


    
    private JFrame frame = new JFrame();
    private JButton loginButton = new JButton("Login");
    private JButton registerButton = new JButton("Register");
    
    private JTextField userIDField = new JTextField();
    private JPasswordField userPasswordField = new JPasswordField();
    private JLabel userIDLabel = new JLabel("user ID:");
    private JLabel userPasswordLabel = new JLabel("Passowrd:");
    JLabel messagLabel = new JLabel();


    DataBaseController dataBaseController;

    public LoginPage() throws ClassNotFoundException, SQLException {
        this.dataBaseController = new DataBaseController();

        this.userIDLabel.setBounds(ConstantsGUI.USER_LABEL_X, ConstantsGUI.USER_LABEL_Y, ConstantsGUI.USER_LABEL_WIDTH, ConstantsGUI.USER_LABEL_HEIGHT);
        this.userPasswordLabel.setBounds(ConstantsGUI.USER_LABEL_X, ConstantsGUI.USER_LABEL_Y + 50, ConstantsGUI.USER_LABEL_WIDTH, ConstantsGUI.USER_LABEL_HEIGHT);

        this.messagLabel.setBounds(ConstantsGUI.MESSAGE_LABEL_X, ConstantsGUI.MESSAGE_LABEL_Y, ConstantsGUI.MESSAGE_LABEL_WIDTH, ConstantsGUI.MESSAGE_LABEL_HEIGHT);
        this.messagLabel.setFont(new Font(null, Font.ITALIC, ConstantsGUI.FONT_SIZE));

        this.userIDField.setBounds(ConstantsGUI.USER_FIELD_X, ConstantsGUI.USER_FIELD_Y, ConstantsGUI.USER_FIELD_WIDTH, ConstantsGUI.USER_FIELD_HEIGHT);

        this.userPasswordField.setBounds(ConstantsGUI.USER_FIELD_X, ConstantsGUI.USER_FIELD_Y + 50, ConstantsGUI.USER_FIELD_WIDTH, ConstantsGUI.USER_FIELD_HEIGHT);

        this.loginButton.setBounds(ConstantsGUI.BUTTON_X, ConstantsGUI.BUTTON_Y, ConstantsGUI.BUTTON_WIDTH, ConstantsGUI.BUTTON_HEIGHT);
        this.loginButton.setFocusable(false);
        this.loginButton.addActionListener(this);

        this.registerButton.setBounds(ConstantsGUI.BUTTON_X + 100, ConstantsGUI.BUTTON_Y, ConstantsGUI.BUTTON_WIDTH, ConstantsGUI.BUTTON_HEIGHT);
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
        this.frame.setSize(ConstantsGUI.FRAME_WIDTH, ConstantsGUI.FRAME_HEIGHT);
        frame.setLayout(null);
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //register logic
        if(e.getSource().equals(this.registerButton)) {
            //this.userIDField.setText("");
            //this.userPasswordField.setText("");
            String userID = this.userIDField.getText();
            String userPassword = String.valueOf(userPasswordField.getPassword());
            int registerState;
            try {
                registerState = this.dataBaseController.register(userID, userPassword);

                switch(registerState) {
                    case ConstantsDB.WEAK_PASSWORD_RETURN_VAL:
                        this.userIDField.setText("");
                        this.userPasswordField.setText("");
                        this.messagLabel.setForeground(Color.RED);
                        this.messagLabel.setText("Weak Password");  
                        break;
                    
                    case ConstantsDB.WEAK_USERNAME_RETURN_VAL:
                        this.userIDField.setText("");
                        this.userPasswordField.setText("");
                        this.messagLabel.setForeground(Color.RED);
                        this.messagLabel.setText("Weak Password");
                        break;

                    case ConstantsDB.USER_ALREADY_EXISTS_RETURN_VAL:
                        this.userIDField.setText("");
                        this.userPasswordField.setText("");
                        this.messagLabel.setForeground(Color.RED);
                        this.messagLabel.setText("User Exists");
                        break;
                    
                    default:
                        this.userIDField.setText("");
                        this.userPasswordField.setText("");
                        this.messagLabel.setForeground(Color.GREEN);
                        this.messagLabel.setText("Registered Successfully");
                        break;
                }
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InvalidKeySpecException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InvalidUsernameStringException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }

        if(e.getSource().equals(this.loginButton)) {
            //login logic
            String userID = this.userIDField.getText();
            String userPassword = String.valueOf(userPasswordField.getPassword());

            try {
                boolean flag = this.dataBaseController.authorizeUser(userID, userPassword); 
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
