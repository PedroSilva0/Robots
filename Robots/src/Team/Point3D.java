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
public class Point3D implements Serializable {
    
    private double x;
    private double y;
    private double z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setZ(double z) {
        this.z = z;
    }
    
    
    
}
