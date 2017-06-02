/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team;
import robocode.*;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import robocode.util.Utils;
import static robocode.util.Utils.normalRelativeAngleDegrees;


/**
 *
 * @author Utilizador
 */
public class Bodyguard extends TeamRobot implements Droid{
    


	private AdvancedEnemyBot enemy = new AdvancedEnemyBot();
	private byte radarDirection = 1;
	private byte moveDirection = 1;
        private int myNumber;
        private int turn=0;
        
        

	public void run() {
            myNumber = getBotNumber(this.getName());
            System.out.println(getName());
		setColors(Color.white, Color.black, Color.magenta);
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);

		
	}

	

	public void onRobotDeath(RobotDeathEvent e) {
		// see if the robot we were tracking died
		if (e.getName().equals(enemy.getName())) {
			enemy.reset();
		}
	}   

	

	
	void doGun() {

		if (enemy.none())
			return;

		double firePower = Math.min(400 / enemy.getDistance(), 3);
		double bulletSpeed = 20 - firePower * 3;
		long time = (long)(enemy.getDistance() / bulletSpeed);
		double futureX = enemy.getFutureX(time);
		double futureY = enemy.getFutureY(time);
		double absDeg = absoluteBearing(getX(), getY(), futureX, futureY);
		setTurnGunRight(normalizeBearing(absDeg - getGunHeading()));

		if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
			setFire(firePower);
		}
	}

	// computes the absolute bearing between two points
	double absoluteBearing(double x1, double y1, double x2, double y2) {
		double xo = x2-x1;
		double yo = y2-y1;
		double hyp = Point2D.distance(x1, y1, x2, y2);
		double arcSin = Math.toDegrees(Math.asin(xo / hyp));
		double bearing = 0;

		if (xo > 0 && yo > 0) { // both pos: lower-Left
			bearing = arcSin;
		} else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
			bearing = 360 + arcSin; // arcsin is negative here, actually 360 - ang
		} else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
			bearing = 180 - arcSin;
		} else if (xo < 0 && yo < 0) { // both neg: upper-right
			bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
		}

		return bearing;
	}

	// normalizes a bearing to between +180 and -180
	double normalizeBearing(double angle) {
		while (angle >  180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}
        
        public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof Point2D.Double) {
                        //System.out.println("recebi mensagem");
			Point2D.Double p = (Point2D.Double) e.getMessage();
			// Calculate x and y to target
			double dx = p.getX() - this.getX();
			double dy = p.getY() - this.getY();
			// Calculate angle to target
			double theta = Math.toDegrees(Math.atan2(dx, dy));

			// Turn gun to target
			turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
			// Fire hard!
			fire(3);
		}
                if(e.getMessage() instanceof Point2D.Float){
                    Point2D.Float p = (Point2D.Float) e.getMessage();
                    go_to_start_position(p.getX(),p.getY());
                    adjustHeading(90);
                    try{
                        sendMessage("team.Vip*", "READY");
                    } catch (IOException ex) {
                        Logger.getLogger(Bodyguard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(e.getMessage() instanceof Point){
                    //x é o turn_right e y e o value
                    Point p= (Point) e.getMessage();
                    doMove(p.getX(),p.getY());
                    execute();
                    /*try{
                        sendMessage("team.Vip*", "READY");
                    } catch (IOException ex) {
                        Logger.getLogger(Bodyguard.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
                }
                
                if(e.getMessage() instanceof String){
                    turn_arround();
                }
	}
        
    
        private void to_place(double x, double y) {
        boolean in_place = false;
        while (!in_place) {
            goTo(x, y);
            execute();

            if (getX() == x && getY() == y) {
                in_place = true;
                stop();
            }
        }
    }
        
        private void turn_arround() {
        //update state
        stop();
        turnRight(180);
        ahead(200);
        
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
        //turnRight(turnAngle);
        if (targetAngle == turnAngle) {
            setAhead(distance);
            //ahead(distance);
        } else {
            setBack(distance);
            //back(distance);
        }
    }
        
    static public int getBotNumber(String name) {
        String n = "0";
        int low = name.indexOf("(") + 1;
        int hi = name.lastIndexOf(")");
        if (low >= 0 && hi >= 0) {
            n = name.substring(low, hi);
        }
        return Integer.parseInt(n);
    }

    private void go_to_start_position(double x, double y) {
        to_place(x+70,y);
        /*switch(myNumber){
            case 1:
                to_place(x+25,y);
        }*/
    }
    
    public void doMove(double right,double value) {
        
        if(turn==1){
            if(right==1){
                turnRight(value);
                turn=0;
            }else{
                turnRight(-value);
                turn=0;
            }
        }else{
            turn=1;
            ahead(value);
        }
        
        
    }
    
    private void adjustHeading(int new_heading) {
        boolean my_head = false;
        while (!my_head) {
            if (getHeading() > new_heading) {
                turnLeft(getHeading() - new_heading);
            } else {
                turnRight(new_heading - getHeading());
            }

            if (getHeading() == new_heading) {
                my_head = true;
            }
        }
    }
}

    

