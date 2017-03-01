
import robocode.*;
import java.awt.Color;


public class Pacifist extends AdvancedRobot {
	
        private Kilometer_Counter distance;
        //private int turn=0;
        /**
	 * run: MyFirstRobot's default behavior
	 */
	public void run() {
            distance = new Kilometer_Counter(getX(),getY());
            //distance.starting_position(getX(),getY());
            addCustomEvent(new Condition("TurnStartedEvent", 99) {
                @Override
                public boolean test() {
                   return true;
                }
            });
           
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		setColors(Color.red,Color.red,Color.red); // body,gun,radar
                /*ahead(100);
                turnGunRight(360);
                back(100);
                turnGunRight(360);
                ahead(100);
                turnGunRight(360);
                back(100);
                turnGunRight(360);*/

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			//ahead(100);
                        
			ahead(100);
                        turnGunRight(360);
                        back(100);
                        turnGunRight(360);
                        
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		//fire(0.1);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(100);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	
        
        public void onRoundEnded(RoundEndedEvent e){
            distance.moved(getX(),getY());
            System.out.println("This round I moved "+ distance.get_total_dist() + " pixels!" );
        }
        
        @Override
        public void onCustomEvent(CustomEvent e) { 

            if (e.getCondition().getName().equals("TurnStartedEvent")) { 
                
                distance.moved(getX(),getY());
                //System.out.println("This turn"+ turn +" I moved "+ distance.get_total_dist() + " pixels!" );
                //turn++;
                //execute();
            } 
        }
}
