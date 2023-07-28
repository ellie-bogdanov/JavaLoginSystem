package TextEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import LoginSystem.DataBaseController;
import LoginSystem.LoginPage;
import Constants.ConstantsGUI;

public class TextEditor extends JFrame implements ActionListener{
    /*static public final int WINDOW_WIDTH = 500;
    static public final int WINDOW_HEIGHT = 500;

    static private final int SCROLL_PANE_WIDTH = 450;
    static private final int SCROLL_PANE_HEIGHT = 450;
 
    static private final String FONT = "Arial";
    static private final int FONT_SIZE = 20;
 
    static private final int FONT_SIZE_SPINNER_WIDTH = 50;
    static private final int FONT_SIZE_SPINNER_HEIGHT = 25;
    static private final int FONT_SIZE_SPINNER_VALUE = 20;*/

    private String username;

    private JButton uploadMessage;
    private JButton profile;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JSpinner fontSizeSpinner;
    private JLabel fontLabel;
    private JButton fontColorButton;
    private JComboBox<String> fontBox;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;
    JMenuItem logoutItem;
    JMenuItem openClipBoardItem;

    
    DataBaseController dataBaseController;



    public TextEditor(String username) throws ClassNotFoundException, SQLException {
        this.username = username;

        this.dataBaseController = new DataBaseController();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(ConstantsGUI.FRAME_WIDTH, ConstantsGUI.FRAME_HEIGHT);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        this.textArea = new JTextArea();

        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        this.textArea.setFont(new Font(null, Font.PLAIN, ConstantsGUI.FONT_SIZE));

        this.scrollPane = new JScrollPane(this.textArea);
        this.scrollPane.setPreferredSize(new Dimension(ConstantsGUI.SCROLL_PANE_WIDTH, ConstantsGUI.SCROLL_PANE_HEIGHT));
        this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.profile = new JButton("Profile");
        this.profile.setSize(100, 100);
        this.profile.addActionListener(this);

        this.fontLabel = new JLabel("Font: ");

        this.fontSizeSpinner = new JSpinner();
        this.fontSizeSpinner.setPreferredSize(new Dimension(ConstantsGUI.FONT_SIZE_SPINNER_WIDTH, ConstantsGUI.FONT_SIZE_SPINNER_HEIGHT));
        this.fontSizeSpinner.setValue(ConstantsGUI.FONT_SIZE_SPINNER_VALUE);
        this.fontSizeSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(null, Font.PLAIN, (int) fontSizeSpinner.getValue()));
            }
            
        });

        this.uploadMessage = new JButton("upload message");
        this.uploadMessage.setSize(100, 100);
        this.uploadMessage.addActionListener(this);

        this.fontColorButton = new JButton("Color");
        this.fontColorButton.addActionListener(this);

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        this.fontBox = new JComboBox<String>(fonts);
        this.fontBox.addActionListener(this);
        this.fontBox.setSelectedItem(ConstantsGUI.FONT);
        
        // ------ menubar ------

        this.menuBar = new JMenuBar();
        this.fileMenu = new JMenu("File");
        this.openItem = new JMenuItem("Open");
        this.saveItem = new JMenuItem("Save");
        this.exitItem = new JMenuItem("Exit");
        this.logoutItem = new JMenuItem("Logout");
        this.openClipBoardItem = new JMenuItem("Clipboard");

        this.openItem.addActionListener(this);
        this.saveItem.addActionListener(this);
        this.exitItem.addActionListener(this);
        this.logoutItem.addActionListener(this);
        this.openClipBoardItem.addActionListener(this);

        this.fileMenu.add(openItem);
        this.fileMenu.add(saveItem);
        this.fileMenu.add(exitItem);
        this.fileMenu.add(logoutItem);
        this.fileMenu.add(openClipBoardItem);

        this.menuBar.add(fileMenu);

        // ------ /menubar ------

        
        

        this.setJMenuBar(this.menuBar);
        this.add(this.fontLabel);
        this.add(this.fontSizeSpinner);
        this.add(this.fontBox);
        this.add(this.fontColorButton);
        this.add(profile);
        this.add(this.uploadMessage);
        this.add(this.scrollPane);
        this.setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(fontColorButton)) {
            Color color = JColorChooser.showDialog(null, "Choose a Color", Color.BLACK);

            textArea.setForeground(color);
        }

        if(e.getSource().equals(this.fontBox)) {
            this.textArea.setFont(new Font((String) this.fontBox.getSelectedItem(), Font.PLAIN, this.textArea.getFont().getSize()));
        }

        if(e.getSource().equals(this.openItem)) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);

            if(response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;
                
                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()) {
                        while(fileIn.hasNextLine()) {
                            String line = fileIn.nextLine() + "\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException e1) {

                    e1.printStackTrace();
                }
                finally {
                    fileIn.close();
                }
            }
        }

        if(e.getSource().equals(this.saveItem)) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            int response = fileChooser.showSaveDialog(null);

            if(response == JFileChooser.APPROVE_OPTION) {
                File file;
                PrintWriter fileOut = null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(this.textArea.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                finally {
                    fileOut.close();
                }
            }
        }



        if(e.getSource().equals(this.exitItem)) {
            System.exit(0);
        }

        if(e.getSource().equals(this.logoutItem)) {
            this.dispose();
            try {
                new LoginPage();
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        if(e.getSource().equals(this.openClipBoardItem)) {
            try {
                new ClipBoard(this.username);
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        if(e.getSource().equals(this.profile)) {
            try {
                new ProfilePage(username);
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        if(e.getSource().equals(this.uploadMessage)) {
            try {
                String message = this.textArea.getText();

                boolean isMessageToLong = this.dataBaseController.saveMessage(this.username, message, ConstantsGUI.CLIPBOARD_MAX_MESSAGE_LENGTH);
                if(isMessageToLong) {
                    this.textArea.setText(ConstantsGUI.MESSAGE_TO_LONG_INFO);
                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
    
}
