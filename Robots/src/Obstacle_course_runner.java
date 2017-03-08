
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Utilizador
 */
public class Obstacle_course_runner extends AdvancedRobot{
    
    //private boolean in_place;
    //private EnemyBot enemys;
    //private ArrayList<EnemyBot> obstacles;
    //private EnemyBot obstacle;
    private LinkedHashMap<String,EnemyBot> obstacles;
    private ArrayList<EnemyBot> points_of_path;
    
    public void run(){
        //in_place=false;
        //setup
        setAdjustRadarForRobotTurn(true);
        
        //obstacles= new ArrayList<>();
        
        obstacles=new LinkedHashMap<>();
        //Ir  para posição inicial
        to_place(20,20);
        skip(50);
        
        //goTo(20,20);
        //execute();
        main_turn_action();
        
    }
    
    private void main_turn_action() {
        while(true){
            //Do stuff here
            turnRadarRight(360);
            obstacles.put("Goal",new EnemyBot(getX(),getY(),"Goal"));
            calc_path();
            run_course();
            //run_course();
            
        }
    }
    
    /*public void doMove() {

	// switch directions if we've stopped
	if (getVelocity() == 0)
		moveDirection *= -1;

	// circle our enemy
	setTurnRight(enemy.getBearing() + 90);
	setAhead(1000 * moveDirection);
}*/
    
    public void onScannedRobot(ScannedRobotEvent e) {
            
            double angleToEnemy = e.getBearing();

            // Calculate the angle to the scanned robot
            double angle = Math.toRadians(getHeading() + angleToEnemy % 360);

            // Calculate the coordinates of the robot
            double enemyX = (getX() + Math.sin(angle) * e.getDistance());
            double enemyY = (getY() + Math.cos(angle) * e.getDistance());
            //System.out.println("Rock X: "+enemyX+"      Rock Y: " + enemyY);
            obstacles.put(e.getName(),new EnemyBot(enemyX,enemyY,e.getName()));
            //obstacle= new EnemyBot(enemyX,enemyY,e.getName());
            //obstacle.next_obstacle(enemyX,enemyY,e.getName());
            //run_course();
            System.out.println("Nome:"+ e.getName()+"   Tamanho: "+obstacles.size());
    }
    
    
    
    private void to_place(double x,double y){
        boolean in_place=false;
        while(!in_place){
            goTo(x,y);
            execute();
            //System.out.println("X: " + getX()+ "         Y :"+ getY());
            if(getX()== x && getY()==y){
                in_place=true;
            }
        }
    }
    
    private void goTo(double x, double y) {
	/* Transform our coordinates into a vector */
	x -= getX();
	y -= getY();
 
	/* Calculate the angle to the target position */
	double angleToTarget = Math.atan2(x, y);
 
	/* Calculate the turn required get there */
	double targetAngle = Utils.normalRelativeAngle(angleToTarget - getHeadingRadians());
 
	/* 
	 * The Java Hypot method is a quick way of getting the length
	 * of a vector. Which in this case is also the distance between
	 * our robot and the target location.
	 */
	double distance = Math.hypot(x, y);
 
	/* This is a simple method of performing set front as back */
	double turnAngle = Math.atan(Math.tan(targetAngle));
	setTurnRightRadians(turnAngle);
	if(targetAngle == turnAngle) {
		setAhead(distance);
	} else {
		setBack(distance);
	}
}

    private void run_course() {
        System.out.println(obstacles.size());
        for(EnemyBot o:obstacles.values()){
            to_place(o.getX()-36,o.getY()+36);
            //System.out.println(o.getX());
        }
        
        //obstacle.next_obstacle();
            /*turnRight(90-getHeading());
            ahead(100);*/
        
    }
    
    public void onHitRobot(HitRobotEvent e){
        System.out.println("Bati");
    }

    private void skip(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void calc_path() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
