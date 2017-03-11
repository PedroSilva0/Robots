

import robocode.Robot;
import java.awt.geom.Point2D;

/**
 ** DistanceThread - Thread that calculates distance (in pixels) made by a designated robot during its life time
 * 
 ** IMPORTANT: 
 * 
 *      - Thread must be killed in the end of each round since "robocode" API clears the robot object. If you don't do that errors will be thrown!
 *      - You must indicate the Robot's self reference (this) to the thread constructor!
 * 
 ** EXAMPLE (Class that extends Robot):
  
   private DistanceThread counter;
  
   @Override
   public void run() {
       this.counter = new DistanceThread(this);
       this.counter.start();
       
       while (true) {
           ...
       }
   }
  
   @Override
   public void onRoundEnded(RoundEndedEvent event) {
       double distance = counter.getDistance();
       System.out.println("Distance: " + distance + " pixels");
       counter.kill();
   }
 ** 
 * 
 * @author AlgoriTeam
 */
 
public class DistanceThread extends Thread{
    
    private Robot robot; // The robot
    private boolean alive; // Thread state
    
    private Point2D old_position; // Robot's last position
    private Point2D new_position; // New position
    
    private Double distance; // Accumulated distance
    
    private int rate = 1000; // Frames that are drawn per second
    private int frame_rate = 1000 / rate; // Time between frames
    
    private boolean error_show;
    
    /**
     * DistanceThread - Constructor
     *
     * @param r, Robot
     */
    public DistanceThread(Robot r) {
        this.robot = r;
        this.alive = true;
        
        this.old_position = new Point2D.Double(r.getX(), r.getY());
        this.distance = 0.0;
        
        this.error_show = true;
    }
    
    /**
     * Method called on thread ".Start()"
     */
    @Override
    public void run() {
        while(true) {
            if (this.alive) {
                try {
                    this.move();
                } catch (robocode.exception.DisabledException d) {
                    if (this.error_show) {
                        System.out.println("[DEBUG] Robot is not moving and too many requests are being made by 'DistanceThread'");
                        this.error_show = false;
                    }
                }

                // Sleep before calculating new frame
                try {
                    Thread.sleep(this.frame_rate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else break;
        }
    }
    
    /**
     * Kills current thread safelly
     */
    public void kill() {
        this.alive = false;
    }
    
    /**
     * Returns the distance performed by the robot from the moment this thread starts and until any call is made to this method
     * 
     * @return Distance
     */
    public double getDistance() {
        synchronized(this.distance) {
            return this.distance;
        }
    }
    
    // ----- OTHER METHODS ----- //
    
    /**
     * Accumulates the distance made by the robot since the last calculated frame
     */
    public void move() {
        synchronized(this.distance) {
            this.update_new_position();
            this.update_distance();
            this.update_old_position();
        }
    }
    
    public void update_new_position() {
        this.new_position = new Point2D.Double(this.robot.getX(), this.robot.getY());
    }
    
    public void update_distance() {
        double euclidian = Math.sqrt(Math.pow((this.old_position.getX() - this.new_position.getX()), 2) + Math.pow((this.old_position.getY() - this.new_position.getY()), 2));
        this.distance += euclidian;
    }
    
    public void update_old_position() {
        this.old_position = new Point2D.Double(this.new_position.getX(), this.new_position.getY());
    }
}
