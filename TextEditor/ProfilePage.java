package TextEditor;

import java.awt.Color;

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


import LoginSystem.DataBaseController;

import Constants.ConstantsDB;
import Constants.ConstantsGUI;

public class ProfilePage extends JFrame implements ActionListener{
    
    JLabel username;
    JButton resetPassword;
    JPasswordField oldPassword;
    JLabel oldPasswordLabel;
    JPasswordField newPassword;
    JLabel newPasswordLabel;
    JLabel message;
    JButton reset;
    DataBaseController dataBaseController;

    public ProfilePage(String usernmae) throws ClassNotFoundException, SQLException {
        dataBaseController = new DataBaseController();

        this.setSize(ConstantsGUI.FRAME_WIDTH, ConstantsGUI.FRAME_HEIGHT);
        this.setLayout(null);
        this.setLocationRelativeTo(null);

        this.username = new JLabel(usernmae);
        this.username.setBounds(0, 0, 100, 100);

        this.resetPassword = new JButton("Reset Password");
        this.resetPassword.setBounds(200, 130, 150, 30);
        this.resetPassword.addActionListener(this);

        this.oldPassword = new JPasswordField();
        this.oldPassword.setBounds(200, 100, 100, 30);

        this.oldPasswordLabel = new JLabel("Old Password:");
        this.oldPasswordLabel.setBounds(100, 70, 100, 100);

        this.newPassword = new JPasswordField();
        this.newPassword.setBounds(200, 130, 100, 30);

        this.newPasswordLabel = new JLabel("New Password:");
        this.newPasswordLabel.setBounds(100, 100, 100, 100);

        this.reset = new JButton("Reset");
        this.reset.setBounds(200, 160, 100, 30);
        this.reset.addActionListener(this);


        this.message = new JLabel("");
        this.message.setBounds(200, 200, 300, 100);
        this.message.setBackground(Color.GREEN);
        this.message.setVisible(false);

        this.add(this.username);
        this.add(this.resetPassword);
        this.add(oldPassword);
        this.add(oldPasswordLabel);
        this.add(newPassword);
        this.add(newPasswordLabel);
        this.add(reset);
        this.add(message);

        this.oldPassword.setVisible(false);
        this.newPassword.setVisible(false);
        this.reset.setVisible(false);
        this.oldPasswordLabel.setVisible(false);
        this.newPasswordLabel.setVisible(false);
        this.setVisible(true);
        

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(this.resetPassword)) {
            this.resetPassword.setVisible(false);
            this.oldPassword.setVisible(true);
            this.newPassword.setVisible(true);
            this.oldPasswordLabel.setVisible(true);
            this.newPasswordLabel.setVisible(true);
            this.reset.setVisible(true);

        }

        if(e.getSource().equals(this.reset)) {
            String username = this.username.getText();
            char[] oldPassword = this.oldPassword.getPassword();
            char[] newPassword = this.newPassword.getPassword();
            String strOldPassword = new String(oldPassword);
            String strNewPassword = new String(newPassword);
            int passwordResetState = 0;

            try {
                passwordResetState = this.dataBaseController.resetPassword(username, strOldPassword, strNewPassword);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InvalidKeySpecException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if(passwordResetState == ConstantsDB.CORRECT_PASSWORD_RETURN_VAL){
                this.message.setText("Password Updated");
                this.message.setForeground(Color.GREEN);
                this.message.setVisible(true);
            }
            else if(passwordResetState == ConstantsDB.WEAK_PASSWORD_RETURN_VAL) {
                this.message.setText("Weak Password");
                this.message.setForeground(Color.RED);
                this.message.setVisible(true);
            }
            else if(passwordResetState == ConstantsDB.INCORRECT_PASSWORD_RETURN_VAL){
                this.message.setText("Incorrect Password");
                this.message.setForeground(Color.RED);
                this.message.setVisible(true);
            }
            else if(passwordResetState == ConstantsDB.MAX_PASSWORD_RESETS_REACHED_RETURN_VAL) {
                this.message.setText("max amount of password resets has reached");
                this.message.setForeground(Color.RED);
                this.message.setVisible(true);
            }


        }

    }
}
