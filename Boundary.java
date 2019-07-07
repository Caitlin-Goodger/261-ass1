/**
 * Boundary class that defines a square.
 * Used for the QuadTree to know if a point is in it's area
 */
public class Boundary {
    double xMax;
    double xMin;
    double yMax;
    double yMin;

    public Boundary(double xMin,double yMin, double xMax, double yMax) {
        this.xMax = xMax;
        this.xMin = xMin;
        this.yMax = yMax;
        this.yMin = yMin;
    }

    //Check to see if x and y are in this box
    public Boolean contains(int x, int y) {
        if(x>=xMin && x<=xMax && y>=yMin && y<=yMax) {
            return true;
        }
        return false;
    }
}
