
import java.awt.Color;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import robocode.AdvancedRobot;
import robocode.DeathEvent;
import robocode.HitRobotEvent;
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Obstacle_course_runner extends AdvancedRobot {

    private LinkedHashMap<String, EnemyBot3> obstacles;
    private ArrayList<EnemyBot3> points_of_path;
    private double goal_x;
    private double goal_y;
    private DistanceThread counter;
    private Random r;
    private double perimeter;

    public void run() {

        //setup
        perimeter = 0;
        r = new Random();
        goal_x = 20;
        goal_y = 20;
        setAdjustRadarForRobotTurn(true);

        obstacles = new LinkedHashMap<>();
        points_of_path = new ArrayList<>();
        //Ir  para posição inicial
        skip(100);
        to_place(goal_x, goal_y);
        this.counter = new DistanceThread(this);
        this.counter.start();
        turnRadarRight(360);
        obstacles.put("Goal", new EnemyBot3((int) goal_x, (int) goal_y, "Goal"));
        calc_path();
        main_turn_action();
    }

    private void main_turn_action() {
        while (true) {
            //Do stuff here
            if (counter != null) {
                double distance = counter.getDistance();
                System.out.println("Distance: " + distance + " pixels");
                double dif = distance - perimeter;
                double ratio = perimeter / distance;
                System.out.println("Diferença: " + dif);
                System.out.println("Rácio: " + ratio);
                counter.kill();
                counter = null;
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
            obstacles.put(e.getName(), new EnemyBot3((int) Math.round(enemyX), (int) Math.round(enemyY), e.getName()));
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

    private void to_place2(double x, double y) {
        boolean in_place = false;
        while (!in_place) {
            goTo(x, y);
            execute();
            if (Math.abs(x - getX()) < 0.001 && Math.abs(y - getY()) < 0.001) {
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

    public void onHitRobot(HitRobotEvent e) {
        if (e.getBearing() > -90 && e.getBearing() <= 90) {
            back(10);
        } else {
            ahead(10);
        }
        System.out.println("Bati");
    }

    private void skip(int turns) {
        for (int i = 0; i < turns; i++) {
            doNothing();
        }
    }

    private void calc_path() {
        ArrayList<EnemyBot3> aux = new ArrayList<>(obstacles.values());
        aux.sort(new ClockWiseComparator((int) goal_x, (int) goal_y));
        EnemyBot3 ant;
        EnemyBot3 e;
        //Calcular perímetro
        for (int i = 0; i < aux.size(); i++) {
            e = aux.get(i);
            if (i == 0) {
                perimeter += sqrt(((e.getX() - 20) * (e.getX() - 20)) + ((e.getY() - 20) * (e.getY() - 20)));
            }
            if (i == 1) {
                ant = aux.get(i - 1);
                perimeter += sqrt(((e.getX() - ant.getX()) * (e.getX() - ant.getX())) + ((e.getY() - ant.getY()) * (e.getY() - ant.getY())));
            }
            if (i == 2) {
                ant = aux.get(i - 1);
                perimeter += sqrt(((e.getX() - ant.getX()) * (e.getX() - ant.getX())) + ((e.getY() - ant.getY()) * (e.getY() - ant.getY())));
            }
            System.out.println(e.getX() + "    " + e.getY());
        }
        e = aux.get(2);
        perimeter += sqrt(((20 - e.getX()) * (20 - e.getX())) + ((20 - e.getY()) * (20 - e.getY())));
        System.out.println("Perímetro: " + perimeter);

        for (int i = 0; i < obstacles.size() - 1; i++) {
            EnemyBot3 next = aux.get(i);
            EnemyBot3 after_next = aux.get(i + 1);
            int diag = calc_which_diagonal(next, after_next);
            switch (diag) {
                case 1:
                    to_place2(next.getX() - 37, next.getY() + 37);
                    do_side_of_robot(90);
                    if (after_next.getY() < next.getY() && after_next.getX() < getX()) {
                        adjustHeading(180);
                    }
                    break;
                case 2:
                    to_place2(next.getX() - 37, next.getY() + 37);
                    break;
                case 3:
                    to_place2(next.getX() + 37, next.getY() - 37);
                    to_place2(next.getX() - 37, next.getY() - 37);
                    break;
                case 4:
                    if (getY() - 38 < next.getY()) {
                        to_place2(next.getX() - 37, next.getY() + 37);
                        do_side_of_robot(90);
                        do_side_of_robot(180);
                    } else {
                        to_place2(next.getX() + 37, next.getY() + 37);
                        do_side_of_robot(180);
                    }
                    break;
            }
        }
        adjustHeading(270);
        to_place(goal_x, goal_y);
    }

    //Returns the diagonal to wich you need to go. Diagonals are numbered acording to the quadrant they belong
    private int calc_which_diagonal(EnemyBot3 next, EnemyBot3 after_next) {

        if (after_next.getX() < next.getX() && after_next.getY() >= next.getY()) {
            return 3;
        }
        if (after_next.getX() < next.getX()) {
            return 4;
        }
        if (next.getY() <= after_next.getY()) {
            return 2;
        } else {
            return 1;
        }

    }

    public void onRoundEnded(RoundEndedEvent event) {
    }

    public void onDeath(DeathEvent event) {
        double distance = counter.getDistance();
        System.out.println("Distance: " + distance + " pixels");
        counter.kill();
    }

    private void do_side_of_robot(int direction_heading) {
        boolean my_head = false;
        while (!my_head) {
            if (getHeading() > direction_heading) {
                turnLeft(getHeading() - direction_heading);
            } else {
                turnRight(direction_heading - getHeading());
            }
            if (getHeading() == direction_heading) {
                my_head = true;
            }
        }
        ahead(74);
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
