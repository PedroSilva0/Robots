
import static java.lang.Math.sqrt;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Utilizador
 */

public class Odometer {
    
    double init_x;
    double init_y;
    double total_dist;
    
    private static double all_round_distance;

    Odometer(double x, double y) {
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
        double mov_dist=sqrt(y_var*y_var+x_var*x_var);
        total_dist += mov_dist;
        all_round_distance+=mov_dist;
        init_x=x;
        init_y=y;
    }
    
    public double get_total_dist() {
        return total_dist;
    }
    
    public double get_all_round_distance(){
        return all_round_distance;
    }
    
    
    
    
    
}
