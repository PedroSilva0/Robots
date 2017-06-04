/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team;

import robocode.*;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 *
 * @author Utilizador
 */
public class Shooter extends TeamRobot implements Droid {

    private AdvancedEnemyBot enemy = new AdvancedEnemyBot();
    private HashMap<String, Mate> team = new HashMap<>();
    private byte radarDirection = 1;
    private byte moveDirection = 1;
    private PAD_Space emotions = new PAD_Space();

    public void run() {
        Mate myself = new Mate(getName(), getX(), getY());
        try {
            broadcastMessage(myself);
        } catch (IOException ex) {
            Logger.getLogger(Spoter.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    @Override
    public void onDeath(DeathEvent event) {
        try {
            broadcastMessage("DIED");
        } catch (IOException ex) {
            Logger.getLogger(Spoter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doMove() {

        // turn slightly toward our enemy
        setTurnRight(normalizeBearing(enemy.getBearing() + 90 - (15 * moveDirection)));

        // strafe toward him
        if (getTime() % 20 == 0) {
            moveDirection *= -1;
            setAhead(150 * moveDirection);
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

        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
            setFire(firePower);
        }
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

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Point3D) {
            Point3D p = (Point3D) e.getMessage();
            // Calculate x and y to target
            double dx = p.getX() - this.getX();
            double dy = p.getY() - this.getY();
            // Calculate angle to target
            double theta = Math.toDegrees(Math.atan2(dx, dy));

            // Turn gun to target
            turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
            //System.out.println(!in_line_of_fire(p.getX(),p.getY(), p.getZ()));
            if (!in_line_of_fire(p.getX(), p.getY(), p.getZ())) {
                // Fire hard!
                fire(1.5);
            }
        }
        if (e.getMessage() instanceof Mate) {
            Mate mate = (Mate) e.getMessage();
            team.put(mate.getName(), mate);
        }
        if (e.getMessage() instanceof String) {
            team.remove(e.getSender());
        }
    }

    public boolean in_line_of_fire(double x, double y, double when) {
        for (Mate m : team.values()) {
            if (Line2D.linesIntersect(m.getX() - 40, m.getY() - 40, m.getX() + 40, m.getY() + 40, getFutureX(when), getFutureY(when), x, y)) {
                return true;
            }
        }
        return false;
    }

    public double getFutureX(double when) {
        /*
		double sin = Math.sin(Math.toRadians(getHeading()));
		double futureX = x + sin * getVelocity() * when;
		return futureX;
         */
        return getY() + Math.sin(Math.toRadians(getHeading())) * getVelocity() * when;
    }

    public double getFutureY(double when) {
        /*
		double cos = Math.cos(Math.toRadians(getHeading()));
		double futureY = y + cos * getVelocity() * when;
		return futureY;
         */
        return getX() + Math.cos(Math.toRadians(getHeading())) * getVelocity() * when;
    }
}
