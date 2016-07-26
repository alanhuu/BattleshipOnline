/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 *
 * @author alanhu
 */
public class EnemyBoard extends PlayerBoard {

    private JButton[][] buttonC;
    private PlayerBoard playerBoardReference;

    public EnemyBoard(GameContainer container, Player user, Player enemy, PlayerBoard playerBoardReference) {
        super(container, user, enemy);
        buttonC = new JButton[10][10];
        this.playerBoardReference = playerBoardReference;
    }
    
    public JButton[][] getButtons() {
        return buttonC;
    }
    
    public void startGame() {
        this.removeAll();
        for (int row = 0; row < 10; row++) {
            this.add(boardLabels[row][0]); //adds the row labels. ex: 1234567
            for (int col = 0; col < 10; col++) {
                buttonC[row][col] = new JButton("*");
                buttonC[row][col].setForeground(Color.red);
                buttonC[row][col].setBorder(BorderFactory.createEtchedBorder());
                buttonC[row][col].addActionListener(new EnemyBoardHandler(container, playerBoardReference, this));
                buttonC[row][col].setActionCommand("" + row + col);
                buttonC[row][col].setBackground(new Color(238, 238, 238));//sets the background colour to gray
                this.add(buttonC[row][col]);
            }
        }
    }

    private void disableBoards() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                buttonC[row][col].setEnabled(false);
                buttonC[row][col].updateUI();
            }
        }
    }

    private void enableBoards() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                buttonC[row][col].setEnabled(true);
                buttonC[row][col].updateUI();
            }
        }
    }
}
