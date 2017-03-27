
import java.util.Comparator;



public class ClockWiseComparator implements Comparator<EnemyBot3>  {
    
    private int center_x;
    private int center_y;
    
    public ClockWiseComparator(int x,int y) {
        center_x = x;
        center_y=y;
    }

    public int compare(EnemyBot3 o1, EnemyBot3 o2) {
        double angle1 = Math.atan2(o1.getY() - center_y, o1.getX() - center_x);
        double angle2 = Math.atan2(o2.getY() - center_y, o2.getX() - center_x);

        //For counter-clockwise, just reverse the signs of the return values
        if(angle1 < angle2) return 1;
        else if (angle2 < angle1) return -1;
        return 0;
    }

    

}