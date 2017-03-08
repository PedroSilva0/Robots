
import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Obstacle_course_runner extends AdvancedRobot {

    //private boolean in_place;
    //private EnemyBot enemys;
    //private ArrayList<EnemyBot> obstacles;
    //private EnemyBot obstacle;
    private LinkedHashMap<String, EnemyBot> obstacles;
    private ArrayList<EnemyBot> points_of_path;
    private double goal_x;
    private double goal_y;

    public void run() {

        //setup
        goal_x=20;
        goal_y=20;
        setAdjustRadarForRobotTurn(true);

        obstacles = new LinkedHashMap<>();
        points_of_path= new ArrayList<>();
        //Ir  para posição inicial
        to_place(goal_x, goal_y);
        skip(50);
        //main_turn_action();
        turnRadarRight(360);
            obstacles.put("Goal", new EnemyBot((int) goal_x, (int) goal_y, "Goal"));
            calc_path();
            for (EnemyBot o : obstacles.values()) {
            //to_place(o.getX(), o.getY());
            System.out.println("X: "+o.getX()+" Y: "+o.getY());
        }
            for (EnemyBot o : points_of_path) {
            //to_place(o.getX(), o.getY());
            //System.out.println("X: "+o.getX()+" Y: "+o.getY());
        }
            run_course();
            main_turn_action();
    }

    private void main_turn_action() {
        while (true) {
            //Do stuff here
            /*turnRadarRight(360);
            obstacles.put("Goal", new EnemyBot((int) goal_x, (int) goal_y, "Goal"));
            calc_path();
            for (EnemyBot o : obstacles.values()) {
            //to_place(o.getX(), o.getY());
            System.out.println("X: "+o.getX()+" Y: "+o.getY());
        }
            for (EnemyBot o : points_of_path) {
            //to_place(o.getX(), o.getY());
            //System.out.println("X: "+o.getX()+" Y: "+o.getY());
        }
            run_course();*/
            turnRight(360);

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

        if (e.getVelocity() == 0) {
            double angleToEnemy = e.getBearing();

            // Calculate the angle to the scanned robot
            double angle = Math.toRadians(getHeading() + angleToEnemy % 360);

            // Calculate the coordinates of the robot
            double enemyX = (getX() + Math.sin(angle) * e.getDistance());
            double enemyY = (getY() + Math.cos(angle) * e.getDistance());
            
            //Add to obstacle list
            obstacles.put(e.getName(), new EnemyBot((int) Math.round(enemyX),(int) Math.round(enemyY), e.getName()));
            //System.out.println("Nome:" + e.getName() + "   X: " + enemyX+ " Y :"+ enemyY);
        }
    }

    private void to_place(double x, double y) {
        boolean in_place = false;
        while (!in_place) {
            goTo(x, y);
            execute();
            //System.out.println("X: " + getX()+ "         Y :"+ getY());
            if (getX() == x && getY() == y) {
                in_place = true;
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
        if (targetAngle == turnAngle) {
            setAhead(distance);
        } else {
            setBack(distance);
        }
    }

    private void run_course() {
        //System.out.println(obstacles.size());
        for (EnemyBot o : points_of_path) {
            to_place(o.getX(), o.getY());
            //System.out.println(o.getX());
        }

        //obstacle.next_obstacle();
        /*turnRight(90-getHeading());
            ahead(100);*/
    }

    public void onHitRobot(HitRobotEvent e) {
        System.out.println("Bati");
    }

    private void skip(int turns) {
        for(int i=0;i<turns;i++){
            doNothing();
        }
    }

    private void calc_path() {
        ArrayList<EnemyBot> aux= new ArrayList<>(obstacles.values());
        aux.sort(new ClockWiseComparator((int) goal_x, (int) goal_y));
        for(int i=0;i<obstacles.size()-1;i++){
            //System.out.println("");
            EnemyBot next=aux.get(i);
            EnemyBot after_next=aux.get(i+1);
            int diag=calc_which_diagonal(next,after_next);
            System.out.println(diag);
            switch(diag){
                case 1:
                    //to_place(next.getX() - 38, next.getY() + 38);
                    //to_place(next.getX() + 38, next.getY() + 38);
                    points_of_path.add(new EnemyBot(next.getX() - 38, next.getY() + 38,"Point"));
                    points_of_path.add(new EnemyBot(next.getX() + 38, next.getY() + 38,"Point"));
                    break;
                case 2:
                    //to_place(next.getX() - 38, next.getY() + 38);
                    points_of_path.add(new EnemyBot(next.getX() - 38, next.getY() + 38,"Point"));
                    break;
                case 3:
                    //to_place(next.getX() + 38, next.getY() - 38);
                    //to_place(next.getX() - 38, next.getY() - 38);
                    points_of_path.add(new EnemyBot(next.getX() + 38, next.getY() - 38,"Point"));
                    points_of_path.add(new EnemyBot(next.getX() - 38, next.getY() - 38,"Point"));
                    break;
                case 4:
                    //to_place(next.getX() + 38, next.getY() - 38);
                    points_of_path.add(new EnemyBot(next.getX() + 38, next.getY() - 38,"Point"));
                    break;
            }
        }
        points_of_path.add(new EnemyBot((int)goal_x, (int) goal_y,"Point"));
    }

    //Returns the diagonal to wich you need to go. Diagonals are numbered acording to the quadrant they belong
    private int calc_which_diagonal(EnemyBot next, EnemyBot after_next) {
        
        if(after_next.getX()<next.getX()&& after_next.getY()<= next.getY()){
            return 3;
        }
        if(after_next.getX()<next.getX()){
            return 4;
        }
        if(next.getY()<=after_next.getY()){
            return 2;
        }else{
            return 1;
        }
        
    }

}
