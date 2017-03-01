/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import robocode.*;
import java.awt.Color;
//import robocode.control.events.TurnStartedEvent;
//import robocode.control.snapshot.RobotState;



public class Conta_Quilometros extends AdvancedRobot
{
	
        
        Kilometer_Counter distance;
        
        public void run() {
            /*distance= new Kilometer_Counter();
            distance.starting_position(getX(),getY());
            distance.field_size(getBattleFieldHeight(),getBattleFieldWidth());
            */
            
            setColors(Color.blue,Color.blue,Color.blue);
	
            //Robot main loop
            while(true) {
			// Replace the next 4 lines with any behavior you would like
                        //distance.moved2(getX(), getY(), getHeading(),100);
			ahead(100);
                        //distance.moved(getX(),getY());
                        
			
                        
		}
	}
        
        /*private void distance(double x,double y){
            double y_var=y-init_y;
            double x_var=x-init_x;
            total_dist += sqrt(y_var*y_var+x_var*x_var);
            init_x=x;
            init_y=y;
        }*/
        
        //Experimentar com getDistanceRemaining
        public void onDeath(DeathEvent e){
            //distance.moved(getX(),getY());
            //System.out.println("This round I moved "+ distance.get_total_dist() + " pixels!" );
        }
        
        /*public void onTurnStarted(TurnStartedEvent event){
            distance.moved(getX(),getY());
            execute();
        } */

	
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	
}

