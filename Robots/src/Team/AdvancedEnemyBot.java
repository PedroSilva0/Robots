package team;

import java.io.Serializable;
import robocode.*;
import java.text.NumberFormat;

public class AdvancedEnemyBot extends EnemyBot implements Serializable {

    private double x;
    private double y;
	// constructor
	public AdvancedEnemyBot() {
		reset();
	}

	public void reset() {
		// tell parent to reset all his stuff
		super.reset();
		// now update our stuff
		x = 0.0;
		y = 0.0;
	}

	public void update(ScannedRobotEvent e, Robot robot) {
		// tell parent to update his stuff
		super.update(e);

		// now update our stuff

		// (convenience variable)
		double absBearingDeg = (robot.getHeading() + e.getBearing());
		if (absBearingDeg < 0) absBearingDeg += 360;

		// yes, you use the _sine_ to get the X value because 0 deg is North
		x = robot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * e.getDistance();

		// likewise, you use the _cosine_ to get the Y value for the same reason
		y = robot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * e.getDistance();
	}
        
        public void update(double x,double y){
            this.x=x;
            this.y=y;
            
        }

	// accessor methods
	public double getX() { return x; }
	public double getY() { return y; }

	public double getFutureX(long when) {
		/*
		double sin = Math.sin(Math.toRadians(getHeading()));
		double futureX = x + sin * getVelocity() * when;
		return futureX;
		*/
		return x + Math.sin(Math.toRadians(getHeading())) * getVelocity() * when;
	}

	public double getFutureY(long when) {
		/*
		double cos = Math.cos(Math.toRadians(getHeading()));
		double futureY = y + cos * getVelocity() * when;
		return futureY;
		*/
		return y + Math.cos(Math.toRadians(getHeading())) * getVelocity() * when;
	}

	
}