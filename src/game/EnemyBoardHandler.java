package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alanhu
 */
public class EnemyBoardHandler implements ActionListener {

    private JLabel[][] userLabels;
    private JLabel[][] enemyLabels;
    private int[] playerHitPoints;
    private int[] cpuHitPoints;
    private EnemyBoard enemyBoard;
    private GameContainer game;
    private String index;
    private String ships;
    private int playerHits;
    private int cpuHits;
    private JButton[][] buttonC;

    public EnemyBoardHandler(GameContainer game, PlayerBoard userBoard, EnemyBoard enemyBoard) {
        this.userLabels = userBoard.getBoardLabels();
        this.enemyLabels = enemyBoard.getBoardLabels();
        this.enemyBoard = enemyBoard;
        this.game = game;
        this.index = userBoard.getIndex();
        this.ships = userBoard.getShips();
        this.playerHitPoints = userBoard.getHitPoints();
        this.playerHits = userBoard.getShipPoints();
        this.cpuHits = enemyBoard.getShipPoints();
        this.cpuHitPoints = enemyBoard.getHitPoints();
        this.buttonC = enemyBoard.getButtons();
    }

    public void actionPerformed(ActionEvent e) {

        //while (true) {
        //Player move
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                //checks the actioncommand for each button in the 2D array
                if (e.getActionCommand().equals("" + row + col)) {
                    //action for a miss
                    if (makeValidAttack(enemyLabels, row, col + 1).equals("M")) {
                        enemyBoard.getButtons()[row][col].setIcon(new ImageIcon(".\\src\\images\\M.jpg"));
                        game.getPlayerMsgArea().setText(game.getUserIGN() + "! \nYou have missed!");
                        enemyLabels[row][col + 1].setText("M");
//                        try {
//                            //disable();
//                            network("M", row, col);
//
//                            System.out.println("DISABLED!");
//
//                            //able = "false";
//                            //action for a hit
//                        } catch (IOException ex) {
//                        }
                    } else if (!makeValidAttack(enemyLabels, row, col + 1).equals("M") && !makeValidAttack(enemyLabels, row, col + 1).equals("N")) {
                        buttonC[row][col].setIcon(new ImageIcon("./src/images/H.jpg"));
                        cpuHitPoints[ships.indexOf(makeValidAttack(enemyLabels, row, col + 1))]--;
                        game.getPlayerMsgArea().setText("Direct hit! Good job! " + game.getUserIGN());
//                        try {
//                            //disable();
//                            network("H", row, col);
//
//                            //able = "false";
//                            //action for a hit
//                        } catch (IOException ex) {
//                        }
                        if (cpuHitPoints[ships.indexOf(makeValidAttack(enemyLabels, row, col + 1))] == 0) {
                            game.getPlayerMsgArea().setText("Direct hit! Nice shot, Sir!\nYou have sunk the enemy's " + game.getShipFullName(ships.indexOf(makeValidAttack(enemyLabels, row, col + 1))));
                        }
                        enemyLabels[row][col + 1].setText("H");
                        playerHits++;
                        //action for an invalid attack
                    } else if (makeValidAttack(enemyLabels, row, col + 1).equals("N")) {
                        JOptionPane.showMessageDialog(game, "Invalid Move!");
                        return; //skips cpu's turn if the player messes up
                    }
                    if (playerHits == 17) {
                        //output.println("WIN");
                        JOptionPane.showMessageDialog(game, "You have won the game!");
                        endGame();
                        playerHits = 0;
                    } else if (cpuHits == 17) {
                        JOptionPane.showMessageDialog(game, "The enemy has won the game!");
                        endGame();
                        cpuHits = 0;
                    }

                }

            }
        }
        makeMoveCPU();

    }



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

    private void endGame() {
        for (int row = 0; row < 10; row++) {
            userLabels[row][0] = new JLabel(new ImageIcon("./src/images/" + index.charAt(row) + ".png"));
            enemyLabels[row][0] = new JLabel(new ImageIcon("./src/images/" + index.charAt(row) + ".png"));
            for (int col = 0; col < 10; col++) {
                userLabels[row][col + 1].disable();
                userLabels[row][col + 1].updateUI();
                buttonC[row][col].setEnabled(false);
                buttonC[row][col].updateUI();
            }
        }
        //output.println("ENDGAME");
    }
    
    private void makeMoveCPU() {
        //CPU move
        java.util.Random rand = new java.util.Random();
        int rowC = 0, colC = 0;
        //generates a random row and column that is valid
        do {
            rowC = rand.nextInt(10);
            colC = rand.nextInt(10);
        } while (makeValidAttack(userLabels, rowC, colC + 1).equals("N"));
        //action for a miss
        if (makeValidAttack(userLabels, rowC, colC + 1).equals("M")) {
            userLabels[rowC][colC + 1].setIcon(new ImageIcon("./src/images/M.jpg"));
            game.getEnemyMsgArea().setText("The computer has attacked " + index.charAt(rowC) + colC + " and missed!");
            userLabels[rowC][colC + 1].setText("M");
            //action for a hit
        } else if (!makeValidAttack(userLabels, rowC, colC + 1).equals("M") && !makeValidAttack(userLabels, rowC, colC + 1).equals("N")) {
            String shipName = game.getShipFullName(ships.indexOf(makeValidAttack(userLabels, rowC, colC + 1)));
            userLabels[rowC][colC + 1].setIcon(new ImageIcon("./src/images/H.jpg"));
            playerHitPoints[ships.indexOf(makeValidAttack(userLabels, rowC, colC + 1))]--;
            game.getEnemyMsgArea().setText("The computer has attacked " + index.charAt(rowC) + colC + " and hit your " + shipName);
            if (playerHitPoints[ships.indexOf(makeValidAttack(userLabels, rowC, colC + 1))] == 0) {
                game.getEnemyMsgArea().setText("The computer has attacked " + index.charAt(rowC) + colC + " and hit your " + shipName + "\n" + "The computer has sunk your " + shipName);
            }
            userLabels[rowC][colC + 1].setText("H");
            cpuHits++;

            //checks for the winner
            if (playerHits == 17) {
                JOptionPane.showMessageDialog(game, "The player has won the game!");
                endGame();

            } else if (cpuHits == 17) {
                JOptionPane.showMessageDialog(game, "The CPU has won the game!");
                endGame();

            }
        }
    }
}
    // }

