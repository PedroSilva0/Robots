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
    
    private int x;
    private int y;
    private String name;

    public EnemyBot(int x, int y,String name) {
        this.x = x;
        this.y = y;
        this.name=name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    

    public void getOutsideEdge(double x,double y){
        
    }

   

    public void next_obstacle(int enemyX, int enemyY, String name) {
        if(!name.equals(this.name)){
            x=enemyX;
            y=enemyY;
        }
    }
}
