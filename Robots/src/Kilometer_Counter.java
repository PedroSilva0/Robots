
import static java.lang.Math.sqrt;
import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.CustomEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Utilizador
 */

public class Kilometer_Counter {
    
    double init_x;
    double init_y;
    double total_dist;

    Kilometer_Counter(double x, double y) {
        init_x=x;
        init_y=y;
        total_dist=0;
    }
    
    public void starting_position(double x, double y){
        total_dist=0;
        init_x=x;
        init_y=y;
    }
    
    public void moved(double x,double y){
        double y_var=y-init_y;
        double x_var=x-init_x;
        total_dist += sqrt(y_var*y_var+x_var*x_var);
        init_x=x;
        init_y=y;
    }
    
    public double get_total_dist() {
        return total_dist;
    }
    
    
    
    
    
}
