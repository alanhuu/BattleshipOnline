//client 
//CopyRight Alan Productions Summer 2014

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.util.Random;
import javax.swing.*;

public class BattleshipGUI {

    private static JFileChooser fileOpen = new JFileChooser();
    public static File file;
    private static JLabel[][] pbLabels = new JLabel[10][11], cbLabels = new JLabel[10][11];
    private static JTextArea playerMsg = new JTextArea("Please open the PLAYER.txt file!", 2, 10), cpuMsg = new JTextArea("Please open the CPU.txt file!", 2, 10);
    private static JPanel menuPanel = new JPanel(new GridLayout(1, 0)), compBoardPanel = new JPanel(new GridLayout(10, 11)), playerBoard = new JPanel(new GridLayout(10, 11));
    private static JButton[][] buttonC = new JButton[10][10];
    private static JButton menuButton = new JButton("Enter");
    private static int cpuHits = 0, playerHits = 0, level = 0, wins = 0;
    private static String[] shipNames = {"Carrier", "Battleship", "Submarine", "Destroyer", "Patrol Boat"};
    private static int[] cpuHitPoints = {5, 4, 3, 3, 2}, playerHitPoints = {5, 4, 3, 3, 2};
    private static String ships = "CBSDP", index = "ABCDEFGHIJ";
    private static JFrame frame = new JFrame("Battleship Client v.12");
    private static JPanel panel = new JPanel(new BorderLayout()), bigPanel = new JPanel();
    private static JTabbedPane tabbedPane = new JTabbedPane();
    public static Socket sock;
    public static BufferedReader input;
    private static PrintWriter output;
    private static String user, enemy = "";
    private static JTextField userNameField = new JTextField(6);
    private static JTextField passwordField = new JTextField(10);
    private static int enemyLevel, enemyWins = 0;
    private static boolean ready = false;

    public static void main(String[] args) throws IOException {
        playerMsg.setEditable(false); //so people can't edit the player msg board
        cpuMsg.setEditable(false); //so people can't edit the cpu msg board

        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        panel.setPreferredSize(new Dimension(1200, 600));
        //network
        //String serverAddress = JOptionPane.showInputDialog(
        // "Enter IP Address of a machine that is\n"
        // + "running the service on port 9090:");
        Socket s = new Socket("localhost", 9090);
        sock = s;
        input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        output = new PrintWriter(sock.getOutputStream(), true);
        String answer = input.readLine();
        //answer += input.readLine();
        JOptionPane.showMessageDialog(null, answer);
        //panel.remove(menuPanel);
        //menu bar
        JMenuBar menuBar = new JMenuBar();
        //put the menubar on the frame
        frame.setJMenuBar(menuBar);
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
            menuOptions[i].addActionListener(new menuActions());
            menuOptions[i].setActionCommand(Integer.toString(i));
            menu.add(menuOptions[i]);
        }
        //created a border around the exit menuitem
        menuOptions[2].setBorder(BorderFactory.createTitledBorder(""));
        //Label Panel(columns)
        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        JLabel playerBoardCol = new JLabel();
        playerBoardCol.setIcon(new ImageIcon(".\\src\\images\\cols.png"));
        labelPanel.add(playerBoardCol);
        panel.add(labelPanel, BorderLayout.PAGE_START); //puts the column labels at the very top of the window (frame)
        //Player board panel
        playerBoard.setPreferredSize(new Dimension(400, 300));
        setDefault(pbLabels, playerBoard);
        playerBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(playerBoard, BorderLayout.LINE_START);
        //Comp Board panel
        setDefault(cbLabels, compBoardPanel); //we chose to use labels instead of buttons to have a more solid effect
        playerBoard.setPreferredSize(new Dimension(600, 500));
        compBoardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(compBoardPanel, BorderLayout.CENTER);
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
        setDefault(cbLabels, compBoardPanel);
        setDefault(cbLabels, compBoardPanel);
        load(pbLabels);
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
        menuButton.addActionListener(new menuActions());
        menuButton.setActionCommand("ENTER");
        frame.setContentPane(bigPanel);
        bigPanel.add(tabbedPane);
        frame.pack();
        while (true) {
            if (input.readLine().equals("READY")) {
                load(cbLabels);
                startGame();
                playerMsg.setText("An opponent has connected!");
                System.out.println("An opponent has connected!");
                playerMsg.setText("Welcome " + user + "! \nAn opponent is ready to play!");
                break;
                //able = input.readLine();
                //System.out.println(able);
            }

        }
        while (true) {
            int row1 = 0, col1 = 0;
            try {
                String answer1 = input.readLine();
                //if(!answer1.equals(null)) {

                if (!answer1.equals("") && answer1.length() == 2) {
                    System.out.println("Answer" + answer1);
                    row1 = Character.getNumericValue(answer1.charAt(0));
                    col1 = Character.getNumericValue(answer1.charAt(1));
                    enable();
                    System.out.println("ENABLED!");
                    answer1 = "";
                    if (makeValidAttack(pbLabels, row1, col1).equals("M")) {
                        pbLabels[row1][col1 + 1].setIcon(new ImageIcon(".\\src\\images\\M.jpg"));
                        cpuMsg.setText(enemy + " has attacked " + index.charAt(row1) + col1 + " and missed!");
                        pbLabels[row1][col1 + 1].setText("M");
                        //action for a hit
                    } else if (!makeValidAttack(pbLabels, row1, col1 + 1).equals("M") && !makeValidAttack(pbLabels, row1, col1 + 1).equals("N")) {
                        pbLabels[row1][col1 + 1].setIcon(new ImageIcon(".\\src\\images\\H.jpg"));
                        playerHitPoints[ships.indexOf(makeValidAttack(pbLabels, row1, col1 + 1))]--;
                        cpuMsg.setText(enemy + " has attacked " + index.charAt(row1) + col1 + " and hit your " + shipNames[ships.indexOf(makeValidAttack(pbLabels, row1, col1 + 1))]);
                        if (playerHitPoints[ships.indexOf(makeValidAttack(pbLabels, row1, col1 + 1))] == 0) {
                            cpuMsg.setText(enemy + " has attacked " + index.charAt(row1) + col1 + " and hit your " + shipNames[ships.indexOf(makeValidAttack(pbLabels, row1, col1 + 1))] + "\n" + "The computer has sunk your " + shipNames[ships.indexOf(makeValidAttack(pbLabels, row1, col1 + 1))]);
                        }
                        pbLabels[row1][col1 + 1].setText("H");
                        cpuHits++;
                    }
                } else if (answer1.equals("PLAYERDC")) {
                    playerMsg.setText(enemy + " has disconnected!");
                    JOptionPane.showMessageDialog(frame, enemy + " has disconnected!");
                    endGame();
                    System.exit(0);
                } else if (answer1.equals("ENDGAME")) {
                    JOptionPane.showMessageDialog(frame, "You have been defeated!");
                    playerMsg.setText("You have been defeated!");
                    endGame();
                } else if (answer1.startsWith("USER")) {
                    enemy = answer1.substring(4);
                } else if (answer1.startsWith("LEVEL")) {
                    level = Integer.parseInt(answer1.substring(5));
                    System.out.println(answer1);
                    borderP.setTitle("Player Message (Level " + level + ")");
                } else if (answer1.startsWith("WINS")) {
                    wins = Integer.parseInt(answer1.substring(4));
                    borderP.setTitle("Player Message (Level " + level + ", " + wins + " wins)");
                } else if (answer1.startsWith("INCREASEDWINS")) {
                    wins = Integer.parseInt(answer1.substring(13));
                    borderP.setTitle("Player Message (Level " + level + ", " + wins + " wins)");
                } else if (answer1.startsWith("CREATE")) {
                    JOptionPane.showMessageDialog(frame, "The account, " + user + " has been created!");
                } else if (answer1.startsWith("UNABLETOLOGIN")) {
                    JOptionPane.showMessageDialog(frame, "Unable to load, " + user + "! Please wait until another player to connect!");
                } else if (answer1.startsWith("OLDUSER")) {
                    JOptionPane.showMessageDialog(frame, "Welcome back " + user + "! Your account has been loaded!");
                } else if (answer1.startsWith("ENEMYLEVEL")) {
                    enemyLevel = Integer.parseInt(answer1.substring(10));
                    borderC.setTitle("Enemy Message (Level " + enemyLevel + ")");
                } else if (answer1.startsWith("ENEMYWINS")) {
                    enemyWins = Integer.parseInt(answer1.substring(9));
                    borderC.setTitle("Enemy Message (Level " + enemyLevel + ", " + enemyWins + " wins)");
                } else if(!answer1.equals("")) {
                    System.out.println(answer1);
                
                } else if (answer1.startsWith("INVALIDPASS")) {
                    JOptionPane.showMessageDialog(frame, "Invalid password ! ");
                }
                //}
            } catch (IOException o) {
            }
        }
        //}

    }
    //resets the whole game: including the boards, and stats. 

    private static void setDefault(JLabel labels[][], JPanel panel) {
        panel.removeAll();
        for (int row = 0; row < 10; row++) {
            labels[row][0] = new JLabel(new ImageIcon(".\\src\\images\\" + index.charAt(row) + ".png"));
            panel.add(labels[row][0]);
            for (int col = 1; col < 11; col++) {
                labels[row][col] = new JLabel("");
                labels[row][col].setForeground(Color.BLUE);
                labels[row][col].setBorder(BorderFactory.createEtchedBorder());
                panel.add(labels[row][col]);

            }
        }
        //resets all the variables, so the new game can be started
        playerHits = 0;
        cpuHits = 0;
        menuActions.n = 0;
        int cpuHitPoints[] = {5, 4, 3, 3, 2};
        int playerHitPoints[] = {5, 4, 3, 3, 2};
        BattleshipGUI.cpuHitPoints = cpuHitPoints;
        BattleshipGUI.playerHitPoints = playerHitPoints;
        //playerMsg.setText("Please open the PLAYER.txt file!");
        cpuMsg.setText("Waiting to make move...");
    }
    //adds buttons to the cpu panel -> sets up the actionlisteners and action commands etc.

    private static void startGame() {
        compBoardPanel.removeAll();
        for (int row = 0; row < 10; row++) {
            compBoardPanel.add(cbLabels[row][0]); //adds the row labels. ex: 1234567
            for (int col = 0; col < 10; col++) {
                buttonC[row][col] = new JButton("*");
                buttonC[row][col].setForeground(Color.red);
                buttonC[row][col].setBorder(BorderFactory.createEtchedBorder());
                buttonC[row][col].addActionListener(new compBoard());
                buttonC[row][col].setActionCommand("" + row + col);
                buttonC[row][col].setBackground(new Color(238, 238, 238));//sets the background colour to gray
                compBoardPanel.add(buttonC[row][col]);
            }
        }
    }

    private static void disable() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                buttonC[row][col].setEnabled(false);
                buttonC[row][col].updateUI();
            }
        }
    }

    private static void enable() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                buttonC[row][col].setEnabled(true);
                buttonC[row][col].updateUI();
            }
        }
    }

    private static void load(JLabel[][] board) throws IOException {
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
    //ActionListener for the menu

    private static class menuActions implements ActionListener {
        //the counter that ensures both files are opened

        public static int n = 0;

        public void actionPerformed(ActionEvent e) {
            //File opener
            if (e.getActionCommand().equals("0")) {
                int fileAction = fileOpen.showOpenDialog(null);
                if (fileAction == JFileChooser.APPROVE_OPTION) {
                    file = fileOpen.getSelectedFile();
                }
                try {
                    if (file.getName().equals("PLAYER.txt")) { //if Player.txt is opened
                        setDefault(cbLabels, compBoardPanel);
                        loadFile(pbLabels, file, false); //load the file
                        playerMsg.setText("File Loaded!"); //update the message
                        n++;
                    } else if (n == 1 && file.getName().equals("CPU.txt")) { //same thing but for the CPU.txt
                        startGame();
                        loadFile(cbLabels, file, true);
                        cpuMsg.setText("File Loaded!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Please open PLAYER.txt first!");
                    }
                } catch (Exception ex) {
                    System.out.println("gg");
                }
            } else if (e.getActionCommand().equals("1")) {
                save("PLAYER.txt", pbLabels);
                save("CPU.txt", cbLabels);
            } else if (e.getActionCommand().equals("2")) {
                generate(pbLabels);
                generate(cbLabels);
                startGame();
                //for game restart
            } else if (e.getActionCommand().equals("3")) {
                setDefault(cbLabels, compBoardPanel);
                setDefault(pbLabels, playerBoard);
                //code for exit
            } else if (e.getActionCommand().equals("4")) {
                System.exit(0);
            } else if (e.getActionCommand().equals("START")) {
            } else if (e.getActionCommand().equals("ENTER")) {
                user = userNameField.getText();
                System.out.println(user);
                tabbedPane.setSelectedIndex(1);
                if(!ready) {
                    playerMsg.setText("Welcome " + user + "! \nWaiting for an opponent to connect...");
                } else {
                    playerMsg.setText("Welcome " + user + "! \nAn opponent is ready to play!");
                
                }
                try {
                    output.println("USER" + user + " " + passwordField.getText());
                    //output.println("PASS" + passwordField.getText());
                } finally {
                }
            }
        }

        //copies the content on the files onto the JLabel arrays in order to be used by the boards
        private static void loadFile(JLabel[][] board, File fileName, boolean isCpu) throws IOException {
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
                                    buttonC[row][col].setIcon(new ImageIcon(".\\src\\images\\" + temp + ".jpg"));
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
    }

    private static void generate(JLabel[][] board) {
        Random rand = new Random();
        int i = 0;
        int input = 0;
        for (int row = 0; row < 10; row++) {
            for (int col = 1; col < 11; col++) {
                input = rand.nextInt(rand.nextInt(20) + 1);
                if (row == 9) {
                    input = 1;
                }
                if (i < 5 && input == 1 && (col + playerHitPoints[i] - 1) < 11) {//if it's 1 then output a ship
                    for (int a = 0; a < playerHitPoints[i]; a++) {
                        board[row][col].setText("" + ships.charAt(i));
                        col++;
                        if (a == playerHitPoints[i] - 1) {
                            col--;
                        }

                    }
                    i++;
                } else {
                    board[row][col].setText("*");
                }
            }
        }

    }

    private static void save(String fileName, JLabel[][] board) {
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

    public static void network(String move, int row, int col) throws IOException {
        try {
            //System.out.println("SENDING " + row + col)
            BufferedReader input;
            input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            output.println(move + row + col);
        } catch (IOException p) {
        }
    }
    //code for the Jbuttons on the cpu board

    private static class compBoard implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            //while (true) {
            //Player move
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    //checks the actioncommand for each button in the 2D array
                    if (e.getActionCommand().equals("" + row + col)) {
                        //action for a miss
                        if (makeValidAttack(cbLabels, row, col + 1).equals("M")) {
                            buttonC[row][col].setIcon(new ImageIcon(".\\src\\images\\M.jpg"));
                            playerMsg.setText(user + "! \nYou have missed!");
                            cbLabels[row][col + 1].setText("M");
                            try {    
                                //disable();
                                network("M", row, col);
                                
                                System.out.println("DISABLED!");
                                
                                //able = "false";
                                //action for a hit
                            } catch (IOException ex) {
                            }
                        } else if (!makeValidAttack(cbLabels, row, col + 1).equals("M") && !makeValidAttack(cbLabels, row, col + 1).equals("N")) {
                            buttonC[row][col].setIcon(new ImageIcon(".\\src\\images\\H.jpg"));
                            cpuHitPoints[ships.indexOf(makeValidAttack(cbLabels, row, col + 1))]--;
                            playerMsg.setText("Direct hit! Good job! " + user);
                            try {
                                //disable();
                                network("H", row, col);
                                
                                //able = "false";
                                //action for a hit
                            } catch (IOException ex) {
                            }
                            if (cpuHitPoints[ships.indexOf(makeValidAttack(cbLabels, row, col + 1))] == 0) {
                                playerMsg.setText("Direct hit! Nice shot, Sir!\nYou have sunk the enemy's " + shipNames[ships.indexOf(makeValidAttack(cbLabels, row, col + 1))]);
                            }
                            cbLabels[row][col + 1].setText("H");
                            playerHits++;
                            //action for an invalid attack
                        } else if (makeValidAttack(cbLabels, row, col + 1).equals("N")) {
                            JOptionPane.showMessageDialog(frame, "Invalid Move!");
                            return; //skips cpu's turn if the player messes up
                        }
                        if (playerHits == 17) {
                            //output.println("WIN");
                            JOptionPane.showMessageDialog(frame, "You have won the game!");
                            endGame();
                            playerHits = 0;
                        } else if (cpuHits == 17) {
                            JOptionPane.showMessageDialog(frame, "The enemy has won the game!");
                            endGame();
                            cpuHits = 0;
                        }

                    }

                }
            }

        }
        // }
    }
    //CPU move
         /*   java.util.Random rand = new java.util.Random();
     int rowC = 0, colC = 0;
     //generates a random row and column that is valid
     do {
     rowC = rand.nextInt(10);
     colC = rand.nextInt(10);
     } while (makeValidAttack(pbLabels, rowC, colC + 1).equals("N"));
     //action for a miss
     if (makeValidAttack(pbLabels, rowC, colC + 1).equals("M")) {
     pbLabels[rowC][colC + 1].setIcon(new ImageIcon(".\\src\\images\\M.jpg"));
     cpuMsg.setText("The computer has attacked " + index.charAt(rowC) + colC + " and missed!");
     pbLabels[rowC][colC + 1].setText("M");
     //action for a hit
     } else if (!makeValidAttack(pbLabels, rowC, colC + 1).equals("M") && !makeValidAttack(pbLabels, rowC, colC + 1).equals("N")) {
     pbLabels[rowC][colC + 1].setIcon(new ImageIcon(".\\src\\images\\H.jpg"));
     playerHitPoints[ships.indexOf(makeValidAttack(pbLabels, rowC, colC + 1))]--;
     cpuMsg.setText("The computer has attacked " + index.charAt(rowC) + colC + " and hit your " + shipNames[ships.indexOf(makeValidAttack(pbLabels, rowC, colC + 1))]);
     if (playerHitPoints[ships.indexOf(makeValidAttack(pbLabels, rowC, colC + 1))] == 0) {
     cpuMsg.setText("The computer has attacked " + index.charAt(rowC) + colC + " and hit your " + shipNames[ships.indexOf(makeValidAttack(pbLabels, rowC, colC + 1))] + "\n" + "The computer has sunk your " + shipNames[ships.indexOf(makeValidAttack(pbLabels, rowC, colC + 1))]);
     }
     pbLabels[rowC][colC + 1].setText("H");
     cpuHits++;
         
    
     //checks for the winner
     if (playerHits == 17) {
     JOptionPane.showMessageDialog(frame, "The player has won the game!");
     endGame();

     } else if (cpuHits == 17) {
     JOptionPane.showMessageDialog(frame, "The CPU has won the game!");
     endGame();
         
     }
     }
     }*/

    public static String makeValidAttack(JLabel[][] board, int row, int col) { //checks if what type of move it is : hit, miss, or invalid

        try {

            if (board[row][col].getText().equals("H") || board[row][col].getText().equals("M")) {
                return "N";
            }
        } catch (Exception e) {
            return "N";
        }
        if (board[row][col].getText().equals("*")) {
            return "M";
        } else {
            return board[row][col].getText();
        }
    }
    //end game disable buttons

    private static void endGame() {
        for (int row = 0; row < 10; row++) {
            pbLabels[row][0] = new JLabel(new ImageIcon(".\\src\\images\\" + index.charAt(row) + ".png"));
            cbLabels[row][0] = new JLabel(new ImageIcon(".\\src\\images\\" + index.charAt(row) + ".png"));
            for (int col = 0; col < 10; col++) {
                pbLabels[row][col + 1].disable();
                pbLabels[row][col + 1].updateUI();
                buttonC[row][col].setEnabled(false);
                buttonC[row][col].updateUI();
            }
        }
        output.println("ENDGAME");
    }
}
