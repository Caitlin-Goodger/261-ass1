import java.util.ArrayList;

/**
 * QuadTree class to make searching for a node easier
 * Divides area into four so that they are smaller to search as it is known what one to search in
 * Not quite implemented properly
 */
public class QuadTree {
    public QuadTree northWest = null;
    public QuadTree northEast = null;
    public QuadTree southWest = null;
    public QuadTree southEast = null;
    public ArrayList<Node> nodes;
    public int max = 800;
    public int level = 0;
    public Boundary boundary;

    public QuadTree(int lvl, Boundary b) {
        level = lvl;
        nodes = new ArrayList<>();
        boundary = b;
    }

    /**
     * Divide current QuadTree's area into four and spread it across 4 new Trees
     */
    public void divide() {
        double x = boundary.xMin + (boundary.xMax-boundary.xMin)/2;
        double y = boundary.yMin + (boundary.yMax-boundary.yMin)/2;
        northWest = new QuadTree(level +1, new Boundary(boundary.xMin,boundary.yMin,x,y));
        northEast = new QuadTree(level+1, new Boundary(x,boundary.yMin,boundary.xMax,y));
        southWest = new QuadTree(level+1, new Boundary(boundary.xMin,y,x,boundary.yMax));
        southEast = new QuadTree(level +1, new Boundary(x,y,boundary.xMax,boundary.yMax));
    }

    /**
     * Add Node to tree recursively depending on where it is
     * @param x
     * @param y
     * @param n
     */
    public void add(int x, int y, Node n) {
        if(!boundary.contains(x,y)) {
            return;
        }
        if (nodes.size() < max) {
            nodes.add(n);
        } else {
            if (northWest == null) {
                divide();
            }
            if (northWest.boundary.contains(x, y)) {
                northWest.add(x, y, n);
            } else if (northEast.boundary.contains(x, y)) {
                northEast.add(x, y, n);
            } else if (southWest.boundary.contains(x, y)) {
                southWest.add(x, y, n);
            } else if (southEast.boundary.contains(x, y)) {
                southEast.add(x, y, n);
            }
        }

    }

    /**
     * Search recursively for node in tree by going down the tree that has a boundary that x and y are inside
     * @param q
     * @param x
     * @param y
     * @return
     */
    public Node search(QuadTree q, int x, int y) {
        if(q == null) {
            return null;
        }
        for(Node n : nodes) {
            if(n.contains(x,y)){
                return n;
            }
        }

        if(northWest.boundary.contains(x,y)) {
            return search(northWest,x,y);
        } else if(northEast.boundary.contains(x,y)) {
            return search(northEast,x,y);
        } else if(southWest.boundary.contains(x,y)) {
            return search(southWest,x,y);
        } else if( southEast.boundary.contains(x,y)) {
            return search(southEast,x,y);
        } else {
            return null;
        }
    }

    /**
     * Clear nodes list and reset trees
     */
    public void clear() {
        nodes.clear();
        northEast = null;
        northWest = null;
        southEast = null;
        southWest = null;
    }
}
