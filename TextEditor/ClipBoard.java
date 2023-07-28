package TextEditor;

import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import LoginSystem.DataBaseController;
import Constants.ConstantsGUI;


public class ClipBoard extends JFrame{





    private JFrame frame = new JFrame("Clipboard");

    private DataBaseController dataBaseController;
    private JLabel[] allMessagesLabels;
    public ClipBoard(String username) throws ClassNotFoundException, SQLException {

        dataBaseController = new DataBaseController();

        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setSize(ConstantsGUI.CLIPBOARD_FRAME_WIDTH, ConstantsGUI.CLIPBOARD_FRAME_HEIGHT);
        //this.setLayout(new FlowLayout());
        //this.setLocationRelativeTo(null);

        dataBaseController = new DataBaseController();
        ArrayList<String> allMessages = new ArrayList<String>();
        allMessages = dataBaseController.displayMessage();
        this.allMessagesLabels = new JLabel[allMessages.size()];

        for (int i = 0; i < allMessages.size(); i++) {
            this.allMessagesLabels[i] = new JLabel();
            this.allMessagesLabels[i].setBounds(ConstantsGUI.CLIPBOARD_MESSAGE_X, ConstantsGUI.CLIPBOARD_MESSAGE_Y + i * ConstantsGUI.CLIPBOARD_MESSAGE_DISPLASE_VALUE, 
            ConstantsGUI.CLIPBOARD_MESSAGE_WIDTH, ConstantsGUI.CLIPBOARD_MESSAGE_HEIGHT);

            this.allMessagesLabels[i].setFont(new Font(null, Font.ITALIC, ConstantsGUI.FONT_SIZE));
            this.allMessagesLabels[i].setText(allMessages.get(i));
            this.frame.add(this.allMessagesLabels[i]);
        }

        
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(ConstantsGUI.CLIPBOARD_FRAME_WIDTH, ConstantsGUI.CLIPBOARD_FRAME_HEIGHT);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
