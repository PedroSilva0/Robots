
import java.awt.Color;
import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import robocode.AdvancedRobot;
import robocode.DeathEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RoundEndedEvent;
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
    private DistanceThread counter;
    private Random r;

    public void run() {

        //setup
        r= new Random();
        goal_x=20;
        goal_y=20;
        setAdjustRadarForRobotTurn(true);

        obstacles = new LinkedHashMap<>();
        points_of_path= new ArrayList<>();
        //Ir  para posição inicial
        to_place(goal_x, goal_y);
        this.counter = new DistanceThread(this);
        this.counter.start();
        skip(100);
        turnRadarRight(360);
        /*for(EnemyBot e:obstacles.values()){
            System.out.println(e.getX()+"    " + e.getY());
        }*/
            obstacles.put("Goal", new EnemyBot((int) goal_x, (int) goal_y, "Goal"));
            calc_path();
            //run_course();
            main_turn_action();
    }

    private void main_turn_action() {
        while (true) {
            //Do stuff here
            //Random r= new Random();
            turnRight(360);
            setBodyColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
            setGunColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
            setRadarColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        }
    }

   
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
            
            
            if (getX() ==x && getY() == y) {
                in_place = true;
                stop();
            }
        }
    }
    
    private void to_place2(double x, double y) {
        boolean in_place = false;
        while (!in_place) {
            goTo(x, y);
            //stop();
            execute();
            
            if (Math.abs(x-getX()) < 0.001 && Math.abs(y-getY()) <0.001) {
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
        if (targetAngle == turnAngle) {
            setAhead(distance);
        } else {
            setBack(distance);
        }
    }

    private void run_course() {
        for (EnemyBot o : points_of_path) {
            to_place(o.getX(), o.getY());
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        if (e.getBearing() > -90 && e.getBearing() <= 90) {
           back(100);
       } else {
           ahead(100);
       }
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
        for(EnemyBot e:aux){
            System.out.println(e.getX()+"    " + e.getY());
        }
        for(int i=0;i<obstacles.size()-1;i++){
            EnemyBot next=aux.get(i);
            EnemyBot after_next=aux.get(i+1);
            int diag=calc_which_diagonal(next,after_next);
            System.out.println(diag);
            switch(diag){
                case 1:
                    to_place2(next.getX() - 37, next.getY() + 37);
                    to_place2(next.getX() + 37, next.getY() + 37);
                    //points_of_path.add(new EnemyBot(next.getX() - 38, next.getY() + 38,"Point"));
                    //points_of_path.add(new EnemyBot(next.getX() + 38, next.getY() + 38,"Point"));
                    break;
                case 2:
                    to_place2(next.getX() - 37, next.getY() + 37);
                    //points_of_path.add(new EnemyBot(next.getX() - 38, next.getY() + 38,"Point"));
                    break;
                case 3:
                    //to_place(next.getX()+38,next.getY()+38);
                    to_place2(next.getX() + 37, next.getY() - 37);
                    to_place2(next.getX() - 37, next.getY() - 37);
                    //points_of_path.add(new EnemyBot(next.getX() + 38, next.getY() + 38,"Point"));
                    //points_of_path.add(new EnemyBot(next.getX() + 38, next.getY() - 38,"Point"));
                    //points_of_path.add(new EnemyBot(next.getX() - 38, next.getY() - 38,"Point"));
                    break;
                case 4:
                    if(getY()-37 < next.getY()){
                        System.out.println("entrei aqui");
                        to_place2(next.getX()-37,next.getY()+37);
                    }
                    to_place2(next.getX()+37,next.getY()+ 37);
                    //stop();
                    to_place2(next.getX() + 37, next.getY() - 37);
                    //points_of_path.add(new EnemyBot(next.getX() + 38, next.getY() - 38,"Point"));
                    break;
            }
        }
        to_place(goal_x,goal_y);
        //points_of_path.add(new EnemyBot((int)goal_x, (int) goal_y,"Point"));
    }

    //Returns the diagonal to wich you need to go. Diagonals are numbered acording to the quadrant they belong
    private int calc_which_diagonal(EnemyBot next, EnemyBot after_next) {
        
        if(after_next.getX()<next.getX()&& after_next.getY()>= next.getY()){
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
    
    
    
    public void onRoundEnded(RoundEndedEvent event) {
       //double distance = counter.getDistance();
       //System.out.println("Distance: " + distance + " pixels");
       //counter.kill();
   }
    
    public void onDeath(DeathEvent event){
       double distance = counter.getDistance();
       System.out.println("Distance: " + distance + " pixels");
       counter.kill();
    }
    
    

}
