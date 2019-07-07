import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
import java.awt.Point;

public class APoints {
    public double depth;
    public Node node;
    public Node parent;
    public ArrayList<Node> children = new ArrayList<>();


    public APoints(Node n, double depth, Node parent) {
        node = n;
        this.depth = depth;
        this.parent = parent;
        //n.depth = depth;
    }
}
