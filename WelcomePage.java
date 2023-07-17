package LoginSystem;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class WelcomePage {
    static final int WELCOME_FRAME_WIDTH = 420;
    static final int WELCOME_FRAME_HEIGHT = 420;

    static final int WELCOME_LABEL_X = 0;
    static final int WELCOME_LABEL_Y = 0;
    static final int WELCOME_LABEL_WIDTH = 200;
    static final int WELCOME_LABEL_HEIGHT = 35;

    static final int FONT_SIZE = 25;

    
    private JFrame frame = new JFrame();
    private JLabel welcomLabel = new JLabel("");

    public WelcomePage(String userID) {
        this.welcomLabel.setText("Hello ".concat(userID));
        this. welcomLabel.setBounds(WELCOME_LABEL_X, WELCOME_LABEL_Y, WELCOME_LABEL_WIDTH, WELCOME_LABEL_HEIGHT);
        this.welcomLabel.setFont(new Font(null, Font.PLAIN, FONT_SIZE));

        frame.add(this.welcomLabel);
        this.frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WELCOME_FRAME_WIDTH, WELCOME_FRAME_HEIGHT);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
