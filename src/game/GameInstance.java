package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alanhu
 */
public class GameInstance {
    public static void main(String args[]) throws IOException{
        GameContainer game = new GameContainer();
        game.init();
    }
//        public static void network(String move, int row, int col) throws IOException {
//        try {
//            //System.out.println("SENDING " + row + col)
//            BufferedReader input;
//            input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//            output.println(move + row + col);
//        } catch (IOException p) {
//        }
//    }
}
