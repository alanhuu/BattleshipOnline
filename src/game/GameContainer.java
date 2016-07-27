package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alanhu
 */
public class GameContainer extends JFrame {

    private static final String GAME_TITLE = "Battleship Client v.12";
    private static JFileChooser fileOpen = new JFileChooser();
    public static File file;

    private JTextArea playerMsg = new JTextArea("Please open the PLAYER.txt file!", 2, 10);
    private JTextArea cpuMsg = new JTextArea("Please open the CPU.txt file!", 2, 10);
    private JPanel menuPanel = new JPanel(new GridLayout(1, 0));
    private JButton menuButton = new JButton("Enter");
    
    private static String[] shipNames = {"Carrier", "Battleship", "Submarine", "Destroyer", "Patrol Boat"};
    private static String ships = "CBSDP", index = "ABCDEFGHIJ";

    private static JPanel panel = new JPanel(new BorderLayout()), bigPanel = new JPanel();
    private static JTabbedPane tabbedPane = new JTabbedPane();
    public static Socket sock;
    public static BufferedReader input;
    private static PrintWriter output;
    
    private String userIGN = "Alan";
    
    private String enemyIGN = "CPU";
    
    private static JTextField userNameField = new JTextField(6);
    private static JTextField passwordField = new JTextField(10);

    private static boolean ready = false;

    private final String IMAGE_PATH = "./src/images/";
    private PlayerBoard userBoard;
    private EnemyBoard enemyBoard;
    private Player user;
    private Player enemy;

    public GameContainer() throws IOException {
        super(GAME_TITLE);
        user = new Player();
        enemy = new Player();
        userBoard = new PlayerBoard(this, user, enemy);
        enemyBoard = new EnemyBoard(this, user, enemy, userBoard);

    }
    
    public String getImagePath() {
        return IMAGE_PATH;
    }

    public String getShipFullName(int index) {
        return shipNames[index];
    }

    public JTextArea getPlayerMsgArea() {
        return playerMsg;
    }

    public JTextArea getEnemyMsgArea() {
        return cpuMsg;
    }

    public String getUserIGN() {
        return userIGN;
    }

    public void changeTabToGame() {
        tabbedPane.setSelectedIndex(1);
    }

    public void setUserName(String newValue) {
        userIGN = newValue;
    }

    public String getValueFromNameField() {
        return userNameField.getText();
    }

    public void updateUserMessageBox(String message) {
        playerMsg.setText(message);
    }

    public void updateEnemyMessageBox(String message) {
        cpuMsg.setText(message);
    }
    // Initializes the window looks. Must be called first after the constructor!

    public void init() throws IOException {
        System.out.println(new File(".").getCanonicalPath());
        playerMsg.setEditable(false); //so people can't edit the player msg board
        cpuMsg.setEditable(false); //so people can't edit the cpu msg board

        this.setVisible(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 800);
        panel.setPreferredSize(new Dimension(1200, 600));
        //network
        //String serverAddress = JOptionPane.showInputDialog(
        // "Enter IP Address of a machine that is\n"
        // + "running the service on port 9090:");
//        Socket s = new Socket("localhost", 9090);
//        sock = s;
//        input = new BufferedReader(new InputStreamReader(s.getInputStream()));
//        output = new PrintWriter(sock.getOutputStream(), true);
        //String answer = input.readLine();
        //answer += input.readLine();
        // JOptionPane.showMessageDialog(null, answer);
        //panel.remove(menuPanel);
        //menu bar
        JMenuBar menuBar = new JMenuBar();
        //put the menubar on the frame
        this.setJMenuBar(menuBar);
        //Stuff inside the menubar
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        //components(options)inside the menu
        JMenuItem menuOptions[] = new JMenuItem[5];
        menuOptions[0] = new JMenuItem("Open");
        menuOptions[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menu.add(menuOptions[0]);
        menuOptions[1] = new JMenuItem("Save");
        menuOptions[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menu.add(menuOptions[1]);
        menuOptions[2] = new JMenuItem("Generate");
        menuOptions[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        menu.add(menuOptions[2]);
        menuOptions[3] = new JMenuItem("Restart Game");
        menuOptions[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        menu.add(menuOptions[3]);
        menuOptions[4] = new JMenuItem("Exit");
        menuOptions[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        menu.add(menuOptions[4]);
        for (int i = 0; i < menuOptions.length; i++) { //initializing the menu options
            menuOptions[i].addActionListener(new MenuActions(this, userBoard, enemyBoard));
            menuOptions[i].setActionCommand(Integer.toString(i));
            menu.add(menuOptions[i]);
        }
        //created a border around the exit menuitem
        menuOptions[2].setBorder(BorderFactory.createTitledBorder(""));
        //Label Panel(columns)
        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        JLabel playerBoardCol = new JLabel();
        playerBoardCol.setIcon(new ImageIcon(IMAGE_PATH + "cols.png"));
        labelPanel.add(playerBoardCol);
        panel.add(labelPanel, BorderLayout.PAGE_START); //puts the column labels at the very top of the window (frame)

        //Player board panel
        userBoard.setPreferredSize(new Dimension(600, 500));
        //setDefault(pbLabels, playerBoard);
        userBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(userBoard, BorderLayout.LINE_START);

        //Comp Board panel
        //setDefault(cbLabels, compBoardPanel); //we chose to use labels instead of buttons to have a more solid effect
        enemyBoard.setPreferredSize(new Dimension(600, 500));
        enemyBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(enemyBoard, BorderLayout.CENTER);

        //msg panel
        JPanel msgPanel = new JPanel(new GridLayout(2, 0));
        javax.swing.border.TitledBorder borderP = new javax.swing.border.TitledBorder("Player Message"), borderC = new javax.swing.border.TitledBorder("CPU Message");
        borderP.setTitleColor(Color.BLUE); //setting the colour of text
        playerMsg.setBorder(borderP);
        playerMsg.setForeground(Color.BLUE);
        cpuMsg.setBorder(borderC);
        cpuMsg.setForeground(Color.RED);
        borderC.setTitleColor(Color.RED);
        msgPanel.add(playerMsg);
        msgPanel.add(cpuMsg);
        panel.add(msgPanel, BorderLayout.SOUTH);
        //setDefault(cbLabels, compBoardPanel);
        //setDefault(cbLabels, compBoardPanel);
        //load(pbLabels);
        //while (true) {
        //adding tabs
        //tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        JPanel gameMenu = new JPanel();
        tabbedPane.addTab("Menu", gameMenu);
        tabbedPane.addTab("Game", panel);
        //Menu code *******************************************************************************************
        JLabel userName = new JLabel("Username");
        gameMenu.add(userName);
        gameMenu.add(userNameField);
        JLabel password = new JLabel("Password");
        gameMenu.add(password);
        gameMenu.add(passwordField);
        gameMenu.add(menuButton); //Added the login button ****************************************************
        menuButton.addActionListener(new MenuActions(this, userBoard, enemyBoard));
        menuButton.setActionCommand("ENTER");
        this.setContentPane(bigPanel);
        bigPanel.add(tabbedPane);
        this.pack();
        
    }


    private void disableBoards() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                enemyBoard.getButtons()[row][col].setEnabled(false);
                enemyBoard.getButtons()[row][col].updateUI();
            }
        }
    }

    private void enableBoards() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                enemyBoard.getButtons()[row][col].setEnabled(true);
                enemyBoard.getButtons()[row][col].updateUI();
            }
        }
    }

    private void load(JLabel[][] board) throws IOException {
        try {
            String content = input.readLine();
            //while (content.equals(null)) {
            java.util.StringTokenizer pieces = new java.util.StringTokenizer(content);
            for (int row = 0; row < 10; row++) {

                for (int col = 0; col < 10; col++) {
                    board[row][col + 1].setText(pieces.nextToken());
                }
            }
            // }
        } catch (IOException l) {
        }

    }
    //copies the content on the files onto the JLabel arrays in order to be used by the boards

    private void loadFile(JLabel[][] board, File fileName, boolean isCpu) throws IOException {
        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(fileName));
            String lineRead = inputStream.readLine();
            while (lineRead != null) {
                for (int row = 0; row < board.length; row++) {
                    java.util.StringTokenizer pieces = new java.util.StringTokenizer(lineRead);
                    for (int col = 0; pieces.hasMoreTokens(); col++) {
                        char temp = pieces.nextToken().charAt(0);
                        if (temp == 'H' || temp == 'M') {
                            if (isCpu == true) {
                                enemyBoard.getButtons()[row][col].setIcon(new ImageIcon(".\\src\\images\\" + temp + ".jpg"));
                                board[row][col + 1].setText("" + temp);
                            } else {
                                board[row][col + 1].setIcon(new ImageIcon(".\\src\\images\\" + temp + ".jpg"));
                            }
                        } else {
                            if (isCpu == true) {
                                //buttonC[row][col].setText("" + temp);
                            }
                            board[row][col + 1].setText("" + temp);
                        }
                    }

                    lineRead = inputStream.readLine();
                }
            }
        } catch (FileNotFoundException exception) {
            System.out.println("Error opening file");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public void save(String fileName, JLabel[][] board) {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        } catch (IOException exception) {
            System.out.println("Problem creating " + fileName);
        }
        for (int row = 0; row < 10; row++) {
            for (int col = 1; col < 11; col++) {
                System.out.println(board[row][col].getText());
                outputStream.print(board[row][col].getText() + " ");
            }
            outputStream.println("");
        }
        outputStream.close();

    }
}
