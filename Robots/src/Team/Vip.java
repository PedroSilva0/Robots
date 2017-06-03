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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import robocode.util.Utils;
import java.util.concurrent.ThreadLocalRandom;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Vip extends TeamRobot {

    private team.AdvancedEnemyBot enemy = new team.AdvancedEnemyBot();
    private byte radarDirection = 1;
    private byte moveDirection = 1;
    private int myNumber;
    private int start_x=500;
    private int start_y=500;
    private int state;
    private int tooCloseToWall = 0;
    private int wallMargin = 300;
    private int turn=0;
    private int ready=0;

    public void run() {
        
        myNumber = getBotNumber(this.getName());
        setColors(Color.white, Color.black, Color.magenta);
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);
        state=0;  //iniciar a um para testes
        
        to_place(start_x, start_y);
        
        Point2D.Float start_spot=new Point2D.Float(start_x,start_y);
        try {
            broadcastMessage(start_spot);
        } catch (IOException ex) {
            Logger.getLogger(Spoter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while (true) {
            switch(state){
                case 0:
                    adjustHeading(90);
                    break;
                case 1:
                    doNothing();
                    if(ready==this.getTeammates().length){
                        state=2;
                    }
                    break;
                case 2:
                    doRadar();
                    doMove();
                    doGun();
                    execute();
                    break;
                case 3:
                    doRadar();
                    turn_arround();
                    execute();
                    //state=2;
                    tooCloseToWall();
                case 4:
                    doRadar();
                    doGun();
                    execute();
                    //state=2;
                    break;
            }
           
        }
    }
    
    


    public void onScannedRobot(ScannedRobotEvent e) {
        if (!isTeammate(e.getName())) {
            if (enemy.none() || e.getDistance() < enemy.getDistance() - 70
                    || e.getName().equals(enemy.getName())) {
                // track him using the NEW update method
                stop();
                state=4;
                enemy.update(e, this);
            }
        }
    }

    public void onRobotDeath(RobotDeathEvent e) {
        // see if the robot we were tracking died
        if (e.getName().equals(enemy.getName())) {
            state=2;
            enemy.reset();
        }
    }

    void doRadar() {
        if (enemy.none()) {
            // look around if we have no enemy
            setTurnRadarRight(360);
        } else {
            // oscillate the radar
            double turn = getHeading() - getRadarHeading() + enemy.getBearing();
            turn += 30 * radarDirection;
            setTurnRadarRight(normalizeBearing(turn));
            radarDirection *= -1;
        }
    }

    public void doMove() {
        //update state
        tooCloseToWall();
        
        int right=ThreadLocalRandom.current().nextInt(0, 2);
        int value_turn=ThreadLocalRandom.current().nextInt(1, 91);
        int value_move=ThreadLocalRandom.current().nextInt(1, 91);
        int value=ThreadLocalRandom.current().nextInt(1, 91);
        Point move_ins=new Point(right,value);
        try {
            broadcastMessage(move_ins);
        } catch (IOException ex) {
            Logger.getLogger(Spoter.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    void doGun() {

        if (enemy.none()) {
            return;
        }

        double firePower = Math.min(400 / enemy.getDistance(), 3);
        double bulletSpeed = 20 - firePower * 3;
        long time = (long) (enemy.getDistance() / bulletSpeed);
        double futureX = enemy.getFutureX(time);
        double futureY = enemy.getFutureY(time);
        double absDeg = absoluteBearing(getX(), getY(), futureX, futureY);
        setTurnGunRight(normalizeBearing(absDeg - getGunHeading()));
        Point2D.Double spot=new Point2D.Double(futureX,futureY);
        try {
            broadcastMessage(spot);
        } catch (IOException ex) {
            Logger.getLogger(Spoter.class.getName()).log(Level.SEVERE, null, ex);
        }
        //setFire(1.5);
        
    }

    // computes the absolute bearing between two points
    double absoluteBearing(double x1, double y1, double x2, double y2) {
        double xo = x2 - x1;
        double yo = y2 - y1;
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
        while (angle > 180) {
            angle -= 360;
        }
        while (angle < -180) {
            angle += 360;
        }
        return angle;
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

    @Override
    public void onHitRobot(HitRobotEvent event) {
        back(100);
        adjustHeading(90);
        back(100);
        int i=ThreadLocalRandom.current().nextInt(0, 200);
        for(;i>0;i--){
            doNothing();
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
        state = 1;  // mudar para 1 após testes completos

    }
     
    private boolean tooCloseToWall() {
        boolean toclose=false;
        if(getX() <= wallMargin|| getX() >= getBattleFieldWidth() - wallMargin|| getY() <= wallMargin|| getY() >= getBattleFieldHeight() - wallMargin){
            toclose=true;
            stop();
            state=3;
        }else{
            state=2;
        }
        return toclose;
    }

    private void turn_arround() {
        //update state
        try {
            broadcastMessage("WALL");
        } catch (IOException ex) {
            Logger.getLogger(Spoter.class.getName()).log(Level.SEVERE, null, ex);
        }
        turnRight(180);
        ahead(200);
        
    }
    
    public void onMessageReceived(MessageEvent e) {
                if(e.getMessage() instanceof String){
                    ready++;
                }
	}
        
        
}

