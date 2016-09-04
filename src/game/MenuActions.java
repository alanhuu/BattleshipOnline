package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alanhu
 */
public class MenuActions implements ActionListener {

    //the counter that ensures both files are opened
    private static int numberOfFilesOpened;
    private JFileChooser fileOpen = new JFileChooser();
    private JLabel[][] userLabels;
    private JLabel[][] enemyLabels;
    private EnemyBoard enemyBoard;
    private PlayerBoard playerBoard;
    private GameContainer game;

    public MenuActions(GameContainer game, PlayerBoard userBoard, EnemyBoard enemyBoard) {
        this.userLabels = userBoard.getBoardLabels();
        this.enemyLabels = enemyBoard.getBoardLabels();
        this.enemyBoard = enemyBoard;
        this.game = game;
        this.playerBoard = userBoard;
        numberOfFilesOpened = 0;
    }

    public static void resetNumbersOfFilesOpend() {
        numberOfFilesOpened = 0;
    }

    public void actionPerformed(ActionEvent e) {
        File file = new File("");
        //File opener
        if (e.getActionCommand().equals("0")) {
            int fileAction = fileOpen.showOpenDialog(null);
            if (fileAction == JFileChooser.APPROVE_OPTION) {
                file = fileOpen.getSelectedFile();
            }
            try {
                if (file.getName().equals("PLAYER.txt")) { //if Player.txt is opened
                    enemyBoard.initializePanel();
                    loadFile(userLabels, file, false); //load the file
                    game.getPlayerMsgArea().setText("File Loaded!"); //update the message
                    numberOfFilesOpened++;
                } else if (numberOfFilesOpened == 1 && file.getName().equals("CPU.txt")) { //same thing but for the CPU.txt
                    enemyBoard.startGame();
                    loadFile(enemyLabels, file, true);
                    game.getEnemyMsgArea().setText("File Loaded!");
                } else {
                    JOptionPane.showMessageDialog(game, "Please open PLAYER.txt first!");
                }
            } catch (Exception ex) {
                System.out.println("gg");
            }
        } else if (e.getActionCommand().equals("1")) {
            game.save("PLAYER.txt", userLabels);
            game.save("CPU.txt", enemyLabels);
        } else if (e.getActionCommand().equals("2")) {
            playerBoard.generate();
            enemyBoard.generate();
            enemyBoard.startGame();
            //for game restart
        } else if (e.getActionCommand().equals("3")) {
            enemyBoard.initializePanel();
            playerBoard.initializePanel();
            //code for exit
        } else if (e.getActionCommand().equals("4")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("START")) {
        } else if (e.getActionCommand().equals("ENTER")) {
            if (game.getValueFromNameField().equals("")) {
                JOptionPane.showMessageDialog(game, "Please set a name!");
            } else {
                game.setUserName(game.getValueFromNameField());
                System.out.println(game.getUserIGN());
                game.changeTabToGame();
                // Removing the login screen.
                game.getTabbedPane().remove(0);
//            if (!ready) {
//                playerMsg.setText("Welcome " + user + "! \nWaiting for an opponent to connect...");
//            } else {
//                playerMsg.setText("Welcome " + user + "! \nAn opponent is ready to play!");
//
//            }
//            try {
//                output.println("USER" + user + " " + passwordField.getText());
//                //output.println("PASS" + passwordField.getText());
//            } finally {
//            }
            }
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
}
