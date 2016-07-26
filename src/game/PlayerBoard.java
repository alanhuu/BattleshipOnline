package game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
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
public class PlayerBoard extends JPanel{
    private static final String SHIPS = "CBSDP";
    private static final String INDEX_FILENAMES = "ABCDEFGHIJ";
    private static final int[] BOARD_LABEL_DIMENSIONS = {10, 11};
    private static final int[] PANEL_DIMENSIONS = {400, 300};
    private static final int[] FULL_SHIP_HPS = {5, 4, 3, 3, 2};
    protected GameInstance game;
    protected JLabel boardLabels[][];
    protected Player user;
    protected Player enemy;
    protected GameContainer container;
    
    
    public PlayerBoard(GameContainer container, Player user, Player enemy) {
        super(new GridLayout(10, 11));
        boardLabels = new JLabel[BOARD_LABEL_DIMENSIONS[0]][BOARD_LABEL_DIMENSIONS[1]];
        this.user = user;
        this.enemy = enemy;
        this.container = container;
        initializePanel();
    }
    
    public JLabel[][] getBoardLabels() {
        return boardLabels;
    }
    public String getIndex() {
        return INDEX_FILENAMES;
    }
    public String getShips() {
        return SHIPS;
    }
    public int[] getHitPoints() {
        return user.getShipPoints();
    }
    
    public int getShipPoints() {
        return user.getHitPoints();
    }
    
    public void initializePanel() {
        setPreferredSize(new Dimension(PANEL_DIMENSIONS[0], PANEL_DIMENSIONS[1]));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setDefault(boardLabels);
    }

    private void setDefault(JLabel labels[][]) {
        this.removeAll();
        for (int row = 0; row < 10; row++) {
            labels[row][0] = new JLabel(new ImageIcon("./src/images/" + INDEX_FILENAMES.charAt(row) + ".png"));
            this.add(labels[row][0]);
            for (int col = 1; col < 11; col++) {
                labels[row][col] = new JLabel("");
                labels[row][col].setForeground(Color.BLUE);
                labels[row][col].setBorder(BorderFactory.createEtchedBorder());
                this.add(labels[row][col]);

            }
        }
        //resets all the variables, so the new game can be started
        user.setHitPoints(0);
        enemy.setHitPoints(0);
        MenuActions.resetNumbersOfFilesOpend();
        int enemyHitPoints[] = FULL_SHIP_HPS;
        int playerHitPoints[] = FULL_SHIP_HPS;
        enemy.setShipPoints(enemyHitPoints);
        user.setShipPoints(playerHitPoints);
        //playerMsg.setText("Please open the PLAYER.txt file!");
        container.updateEnemyMessageBox("Waiting to make move...");
    }
    
        public void generate() {
        Random rand = new Random();
        int i = 0;
        int input = 0;
        for (int row = 0; row < 10; row++) {
            for (int col = 1; col < 11; col++) {
                input = rand.nextInt(rand.nextInt(20) + 1);
                if (row == 9) {
                    input = 1;
                }
                if (i < 5 && input == 1 && (col + FULL_SHIP_HPS[i] - 1) < 11) {//if it's 1 then output a ship
                    for (int a = 0; a < FULL_SHIP_HPS[i]; a++) {
                        boardLabels[row][col].setText("" + SHIPS.charAt(i));
                        col++;
                        if (a == FULL_SHIP_HPS[i] - 1) {
                            col--;
                        }

                    }
                    i++;
                } else {
                    boardLabels[row][col].setText("*");
                }
            }
        }

    }
    
   

}
