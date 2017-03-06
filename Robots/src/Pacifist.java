
import robocode.*;
import java.awt.Color;


public class Pacifist extends AdvancedRobot {
	
        private Odometer odometer;
        //private static double total_distance;
        //private int turn=0;
        /**
	 * run: MyFirstRobot's default behavior
	 */
	public void run() {
            //total_distance=0;
            odometer = new Odometer(getX(),getY());
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
            odometer.moved(getX(),getY());
            System.out.println("This round I moved "+ odometer.get_total_dist() + " pixels!" );
            //total_distance+=odometer.get_total_dist();
            //System.out.println("So far I moved "+ odometer.get_all_round_distance()+" pixels!");
        }
        
        @Override
        public void onCustomEvent(CustomEvent e) { 

            if (e.getCondition().getName().equals("TurnStartedEvent")) { 
                
                odometer.moved(getX(),getY());
                //System.out.println("This turn"+ turn +" I moved "+ distance.get_total_dist() + " pixels!" );
                //turn++;
                //execute();
            } 
        }
        
        public void onBattleEnded(BattleEndedEvent e){
            System.out.println("In this match I moved "+odometer.get_all_round_distance()+" pixels!");
        }
}
