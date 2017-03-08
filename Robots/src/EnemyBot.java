/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Utilizador
 */
public class EnemyBot {
    
    private double x;
    private double y;
    private String name;

    public EnemyBot(double x, double y,String name) {
        this.x = x;
        this.y = y;
        this.name=name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    

    public void getOutsideEdge(double x,double y){
        
    }

   

    public void next_obstacle(double enemyX, double enemyY, String name) {
        if(!name.equals(this.name)){
            x=enemyX;
            y=enemyY;
        }
    }
}
