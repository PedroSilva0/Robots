/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team;

import java.io.Serializable;

/**
 *
 * @author Utilizador
 */
public class Mate implements Serializable {

    private String name;
    private double x;
    private double y;

    public Mate() {
        name = "";
        x = 0;
        y = 0;
    }

    public Mate(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

}
