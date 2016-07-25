package game;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alanhu
 */
public class Player {
    private int[] shipPoints;
    private int hitPoints;
    
    public Player() {
        this.shipPoints = new int[] {5, 4, 3, 3, 2};
        this.hitPoints = 0;
    }
    
    public void setShipPoints(int[] newValues) {
        this.shipPoints = newValues;
    }
    public void setHitPoints(int newValue) {
        this.hitPoints = newValue;
    }
    public int getHitPoints() {
        return hitPoints;
    }
    public int[] getShipPoints() {
        return shipPoints;
    }
}
