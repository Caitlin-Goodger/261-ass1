import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
import java.awt.Point;

/**
 * Node class to store all the node information in
 */
public class Node {
    public String id;
    public Location location;
    public ArrayList<Segement> inSegements = new ArrayList<>();
    public ArrayList<Segement> outSegements = new ArrayList<>();
    public Point point;
    public int width = 3;
    public ArrayList<Node> neighbours = new ArrayList<>();
    public double depth = Double.POSITIVE_INFINITY;
    public double reachBack = Double.POSITIVE_INFINITY;




    public Node (String nodeId,Location l) {
        id = nodeId;
        location = l;
    }

    //Add Road segement that is going into this node
    public void addInSegement(Segement s ) {
        inSegements.add(s);
    }

    //Add Road Segement that is going out of this node
    public void addOutSegement(Segement s ) {
        outSegements.add(s);
    }

    //Draw node based on how far it is from the origin
    public void draw(Graphics g, Location origin, double scale, Color c) {
        point = location.asPoint(origin,scale);
        g.setColor(c);
        g.fillOval((int) point.x,(int) point.y,width,width);
    }

    //See if x and y values are close to node
    public boolean contains(int x,int y) {
        if(x < point.x + 5 && x > point.x - 5 && (y < point.y + 5 && y > point.y - 5)) {
            return true;
        }
        return false;
    }

    public void findNeighbours(HashMap<String,Node> nodes) {
        for(int i =0;i<outSegements.size();i++) {
            Segement s = outSegements.get(i);
            if(s.outNode == this) {
                for(Node n : nodes.values()) {
                    if(n.equals(s.inNode)) {
                        neighbours.add(n);
                        break;
                    }
                }
            } else if(s.inNode == this) {
                for(Node n : nodes.values()) {
                    if(n.equals(s.outNode)) {
                        neighbours.add(n);
                        break;
                    }
                }
            }
        }
        for(int i =0;i<inSegements.size();i++) {
            Segement s = inSegements.get(i);
            if(s.outNode == this) {
                for(Node n : nodes.values()) {
                    if(n.equals(s.inNode)) {
                        if(!neighbours.contains(n)) {
                            neighbours.add(n);
                        }
                        break;
                    }
                }
            } else if(s.inNode == this) {
                for(Node n : nodes.values()) {
                    if(n.equals(s.outNode)) {
                        if(!neighbours.contains(n)) {
                            neighbours.add(n);
                        }
                        break;
                    }
                }
            }
        }
    }

    public ArrayList<Node> getNeighbours() {
        return neighbours;
    }

}
