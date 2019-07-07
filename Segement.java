import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
import java.awt.Point;

/**
 * Segement class to store information about each road Segement
 */
public class Segement {
    public Road road;
    public Node inNode;
    public Node outNode;
    public double length;
    public ArrayList<Location> locations;
    public ArrayList<Point> points;

    public Segement (Road r, Node in, Node out, double length, ArrayList location) {
        road = r;
        inNode = in;
        outNode = out;
        this.length = length;
        locations = location;
    }

    //Draw segement as a line, location based on where it is compared to the origin
    public void draw(Graphics g, Location origin, double scale, Color c) {
        g.setColor(c);
        points = new ArrayList<>();
        for(Location l : locations) {
            Point p = l.asPoint(origin,scale);
            points.add(p);
        }

        for(int i=0; i<points.size()-1; i++) {
            g.drawLine((int) points.get(i).getX(),(int) points.get(i).getY(),(int) points.get(i+1).getX(),(int) points.get(i+1).getY());
        }
    }

    // Check to see if x and y are near road segement
    public boolean contains(int x, int y) {
        for(int i=0; i<points.size()-1; i++) {
            double gradient = (points.get(i+1).getY()-points.get(i).getY())/(points.get(i+1).getX()-points.get(i).getX());
            if(x > points.get(i).getX() && x<points.get(i +1).getX() || x > points.get(i+1).getX() && x < points.get(i).getX()) {
                double newY = gradient * (x - points.get(i).getX()) + points.get(i).getY();
                if(y > newY -5 && y < newY + 5) {
                    return true;
                }
            }
            if(y > points.get(i).getY() && y < points.get(i + 1).getY() || y > points.get(i+1).getY() && y < points.get(i).getY()) {

                double newX = (y - points.get(i).getY()) / gradient + points.get(i).getX();
                if(x > newX - 5 && x < newX + 5) {
                    return true;
                }

            }
        }
        return false;
    }


}
