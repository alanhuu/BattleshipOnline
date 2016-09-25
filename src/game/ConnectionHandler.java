/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author alanhu
 */
public class ConnectionHandler {

    private Socket sock;
    private BufferedReader input;
    private PrintWriter output;
    private GameContainer game;

    public ConnectionHandler(GameContainer game) {
        this.game = game;

    }

    public void showIPDialog() throws IOException {
        String serverAddress = JOptionPane.showInputDialog(
                "Enter IP Address of a machine that is\n"
                + "running the service on port 9090:");
        Socket s = new Socket("localhost", 9090);
        sock = s;
        input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        output = new PrintWriter(sock.getOutputStream(), true);
        String answer = input.readLine();
        //answer += input.readLine();
        JOptionPane.showMessageDialog(null, answer);
    }

    public void waitForOpponentConnection(EnemyBoard enemyBoard) throws IOException {
        while (true) {
            if (input.readLine().equals("READY")) {
                game.load(enemyBoard.getBoardLabels(), input);
                enemyBoard.startGame();
                game.getPlayerMsgArea().setText("An opponent has connected!");
                System.out.println("An opponent has connected!");
                game.getPlayerMsgArea().setText("Welcome " + game.getUserIGN() + "! \nAn opponent is ready to play!");
                break;
            }

        }
    }
    
//    public void handleGameSignals() throws IOException {
//                while (true) {
//            int row1 = 0, col1 = 0;
//            try {
//                String answer1 = input.readLine();
//                //if(!answer1.equals(null)) {
//
//                if (!answer1.equals("") && answer1.length() == 2) {
//                    System.out.println("Answer" + answer1);
//                    row1 = Character.getNumericValue(answer1.charAt(0));
//                    col1 = Character.getNumericValue(answer1.charAt(1));
//                    enable();
//                    System.out.println("ENABLED!");
//                    answer1 = "";
//                    if (makeValidAttack(pbLabels, row1, col1).equals("M")) {
//                        pbLabels[row1][col1 + 1].setIcon(new ImageIcon(".\\src\\images\\M.jpg"));
//                        cpuMsg.setText(enemy + " has attacked " + index.charAt(row1) + col1 + " and missed!");
//                        pbLabels[row1][col1 + 1].setText("M");
//                        //action for a hit
//                    } else if (!makeValidAttack(pbLabels, row1, col1 + 1).equals("M") && !makeValidAttack(pbLabels, row1, col1 + 1).equals("N")) {
//                        pbLabels[row1][col1 + 1].setIcon(new ImageIcon(".\\src\\images\\H.jpg"));
//                        playerHitPoints[ships.indexOf(makeValidAttack(pbLabels, row1, col1 + 1))]--;
//                        cpuMsg.setText(enemy + " has attacked " + index.charAt(row1) + col1 + " and hit your " + shipNames[ships.indexOf(makeValidAttack(pbLabels, row1, col1 + 1))]);
//                        if (playerHitPoints[ships.indexOf(makeValidAttack(pbLabels, row1, col1 + 1))] == 0) {
//                            cpuMsg.setText(enemy + " has attacked " + index.charAt(row1) + col1 + " and hit your " + shipNames[ships.indexOf(makeValidAttack(pbLabels, row1, col1 + 1))] + "\n" + "The computer has sunk your " + shipNames[ships.indexOf(makeValidAttack(pbLabels, row1, col1 + 1))]);
//                        }
//                        pbLabels[row1][col1 + 1].setText("H");
//                        cpuHits++;
//                    }
//                } else if (answer1.equals("PLAYERDC")) {
//                    playerMsg.setText(enemy + " has disconnected!");
//                    JOptionPane.showMessageDialog(frame, enemy + " has disconnected!");
//                    endGame();
//                    System.exit(0);
//                } else if (answer1.equals("ENDGAME")) {
//                    JOptionPane.showMessageDialog(frame, "You have been defeated!");
//                    playerMsg.setText("You have been defeated!");
//                    endGame();
//                } else if (answer1.startsWith("USER")) {
//                    enemy = answer1.substring(4);
//                } else if (answer1.startsWith("LEVEL")) {
//                    level = Integer.parseInt(answer1.substring(5));
//                    System.out.println(answer1);
//                    borderP.setTitle("Player Message (Level " + level + ")");
//                } else if (answer1.startsWith("WINS")) {
//                    wins = Integer.parseInt(answer1.substring(4));
//                    borderP.setTitle("Player Message (Level " + level + ", " + wins + " wins)");
//                } else if (answer1.startsWith("INCREASEDWINS")) {
//                    wins = Integer.parseInt(answer1.substring(13));
//                    borderP.setTitle("Player Message (Level " + level + ", " + wins + " wins)");
//                } else if (answer1.startsWith("CREATE")) {
//                    JOptionPane.showMessageDialog(frame, "The account, " + user + " has been created!");
//                } else if (answer1.startsWith("UNABLETOLOGIN")) {
//                    JOptionPane.showMessageDialog(frame, "Unable to load, " + user + "! Please wait until another player to connect!");
//                } else if (answer1.startsWith("OLDUSER")) {
//                    JOptionPane.showMessageDialog(frame, "Welcome back " + user + "! Your account has been loaded!");
//                } else if (answer1.startsWith("ENEMYLEVEL")) {
//                    enemyLevel = Integer.parseInt(answer1.substring(10));
//                    borderC.setTitle("Enemy Message (Level " + enemyLevel + ")");
//                } else if (answer1.startsWith("ENEMYWINS")) {
//                    enemyWins = Integer.parseInt(answer1.substring(9));
//                    borderC.setTitle("Enemy Message (Level " + enemyLevel + ", " + enemyWins + " wins)");
//                } else if(!answer1.equals("")) {
//                    System.out.println(answer1);
//                
//                } else if (answer1.startsWith("INVALIDPASS")) {
//                    JOptionPane.showMessageDialog(game, "Invalid password ! ");
//                }
//                //}
//            } catch (IOException o) {
//            }
//    }
//
//    private void load(JLabel[][] board) throws IOException {
//        try {
//            String content = input.readLine();
//            //while (content.equals(null)) {
//            java.util.StringTokenizer pieces = new java.util.StringTokenizer(content);
//            for (int row = 0; row < 10; row++) {
//
//                for (int col = 0; col < 10; col++) {
//                    board[row][col + 1].setText(pieces.nextToken());
//                }
//            }
//            // }
//        } catch (IOException l) {
//        }
//
//    }
//
}
