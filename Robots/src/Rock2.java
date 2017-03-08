

import robocode.AdvancedRobot;
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
public class Rock2 extends AdvancedRobot{
    
    public void run(){
        to_place(300,500);
    }
    
    private void to_place(double x,double y){
        boolean in_place=false;
        while(!in_place){
            goTo(x,y);
            execute();
            System.out.println("X: " + getX()+ "         Y :"+ getY());
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
    
}
