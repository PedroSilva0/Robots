
import java.awt.Color;
import static java.lang.Math.sqrt;
import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import robocode.AdvancedRobot;
import robocode.DeathEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Obstacle_course_runner extends AdvancedRobot {
    
    private LinkedHashMap<String, EnemyBot> obstacles;
    private ArrayList<EnemyBot> points_of_path;
    private double goal_x;
    private double goal_y;
    private DistanceThread counter;
    private Random r;
    private double perimeter;

    public void run() {

        //setup
        perimeter=0;
        r= new Random();
        goal_x=20;
        goal_y=20;
        setAdjustRadarForRobotTurn(true);
        

        obstacles = new LinkedHashMap<>();
        points_of_path= new ArrayList<>();
        //Ir  para posição inicial
        skip(100);
        to_place(goal_x, goal_y);
        this.counter = new DistanceThread(this);
        this.counter.start();
        //skip(100);
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
            if(counter!=null){
            double distance = counter.getDistance();
            System.out.println("Distance: " + distance + " pixels");
            double dif=distance-perimeter;
            double ratio=perimeter/distance;
            System.out.println("Diferença: " + dif);
            System.out.println("Rácio: "+ ratio);
            counter.kill();
            counter=null;
            }
            turnRight(1);
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
            //System.out.println(getX()+"      "+getY() );
            if (Math.abs(x-getX()) < 0.001 && Math.abs(y-getY()) < 0.001) {
                //System.out.println("entrei aqui 2");
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

    private void run_course() {
        for (EnemyBot o : points_of_path) {
            to_place(o.getX(), o.getY());
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        if (e.getBearing() > -90 && e.getBearing() <= 90) {
           back(10);
       } else {
           ahead(10);
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
        //double perimeter=0;
        EnemyBot ant;
        EnemyBot e;
        for(int i=0;i<aux.size(); i++){
            e=aux.get(i);
            if(i==0){
                perimeter+=sqrt(((e.getX()-20)*(e.getX()-20))+((e.getY()-20)*(e.getY()-20)));
            }
            if(i==1){
                ant=aux.get(i-1);
                perimeter+=sqrt(((e.getX()-ant.getX())*(e.getX()-ant.getX()))+((e.getY()-ant.getY())*(e.getY()-ant.getY())));
            }
            if(i==2){
                ant=aux.get(i-1);
                perimeter+=sqrt(((e.getX()-ant.getX())*(e.getX()-ant.getX()))+((e.getY()-ant.getY())*(e.getY()-ant.getY())));
            }
            System.out.println(e.getX()+"    " + e.getY());
        }
        e=aux.get(2);
        perimeter+=sqrt(((20-e.getX())*(20-e.getX()))+((20-e.getY())*(20-e.getY())));
        System.out.println("Perímetro: "+perimeter);
        
        for(int i=0;i<obstacles.size()-1;i++){
            int prev_diag=0;
            EnemyBot next=aux.get(i);
            EnemyBot after_next=aux.get(i+1);
            int diag=calc_which_diagonal(next,after_next);
            //System.out.println(diag);
            switch(diag){
                case 1:
                    to_place2(next.getX() - 37, next.getY() + 37);
                    do_side_of_robot(90);
                    if(after_next.getY()<next.getY() && after_next.getX()<getX()){
                        //System.out.println("entrei aqui 2");
                        adjustHeading(180);
                    }
                    prev_diag=1;
                    break;
                case 2:
                    to_place2(next.getX() - 37, next.getY() + 37);
                    prev_diag=2;
                    break;
                case 3:
                    to_place2(next.getX() + 37, next.getY() - 37);
                    to_place2(next.getX() - 37, next.getY() - 37);
                    prev_diag=3;
                    break;
                case 4:
                    //System.out.println(getY()+"    "+next.getY());
                    if(getY()-38 < next.getY()){
                        //System.out.println("entrei aqui");
                        to_place2(next.getX()-37,next.getY()+37);
                        do_side_of_robot(90);
                        do_side_of_robot(180);
                        //stop();
                    }else{
                        //adjustHeading(180);
                        to_place2(next.getX()+37,next.getY()+ 37);
                        //adjustHeading(180);
                        do_side_of_robot(180);
                    }
                    break;
            }
        }
        adjustHeading(270);
        to_place(goal_x,goal_y);
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
    
    private void goTo3(int x, int y) {
    double a;
    setTurnRightRadians(Math.tan(
        a = Math.atan2(x -= (int) getX(), y -= (int) getY()) 
              - getHeadingRadians()));
    setAhead(Math.hypot(x, y) * Math.cos(a));
}

    private void do_side_of_robot(int direction_heading) {
        boolean my_head=false;
        while(!my_head){
            if(getHeading()>direction_heading){
                turnLeft(getHeading()-direction_heading);
            }else{
                turnRight(direction_heading-getHeading());
            }
            if(getHeading()==direction_heading){
                my_head=true;
            }
        }
        ahead(74);
    }
    
    private void adjustHeading(int new_heading){
        boolean my_head=false;
        while(!my_head){
            if(getHeading()>new_heading){
                turnLeft(getHeading()-new_heading);
            }else{
                turnRight(new_heading-getHeading());
            }
            
            if(getHeading()==new_heading){
                my_head=true;
            }
        }

    }
    
    

}
