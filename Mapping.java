import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.lang.Math;

/**
 * Mapping class that extends the GUI.
 * Overrides the GUI methods, including mouse events, drawing, on Load etc;
 */


public class Mapping extends GUI {
    public static Graph graph;
    public static int scale;
    public static Location center;
    public static Node currentN;
    public static Node secondN;
    public static Segement currentS;
    public static ArrayList<Segement> highLightS;
    public static ArrayList<Node> aPoints;
    public static Trie trie;
    public static QuadTree qTree;
    public static boolean pathFound = false;


    @Override
    protected void redraw(Graphics g) {
        for(Segement s: graph.segments) {
            if(s.equals(currentS) || highLightS.contains(s)) {//If selected Road then draw red
                s.draw(g,center,scale,Color.red);
            } else {//Else draw blue
                s.draw(g,center,scale,Color.black);
            }
        }
        if(qTree != null) {//Reset the QuadTree
            qTree.clear();
        }
        for(Node n : graph.nodes.values()) {//If selected Node then draw red
            if(n.equals(currentN) || n.equals(secondN)) {
                n.draw(g,center,scale,Color.red);
            } else if(aPoints.contains(n)) {
                n.draw(g,center,scale,Color.green);
                System.out.println(aPoints.size());
            } else {// Else draw blue
                n.draw(g,center,scale, Color.blue);
            }
            //qTree.add(n.point.x,n.point.y,n);//Add to QuadTree
        }

        JTextArea jT = getTextOutputArea();//Get text output
        jT.setText(null);

        if(currentS != null) {//If selected Road then output information about it
            Road r = currentS.road;
            jT.insert("Road ID: " + r.id+ ". ", 0);
            jT.append(r.roadName + ", " + r.city + ". ");
            if(r.direction) {
                jT.append("One Way. ");
            }
            jT.append(r.speedLimit + " ");
            jT.append(r.roadClass + " ");
        }  else if(!highLightS.isEmpty()) {
            int i = 0;
            while(i<highLightS.size()) {
                Road r = highLightS.get(i).road;
                double distance = highLightS.get(i).length;
                boolean write = false;
                while(i<highLightS.size()-1 && !write) {
                    Road next = highLightS.get(i+1).road;
                    if(r.id.equals(next.id)) {
                        distance = distance + highLightS.get(i).length;
                    } else {
                        write = true;
                    }
                    i++;
                }
                jT.insert("Road ID: " + r.id + ". ", 0);
                jT.append(r.roadName + ", " + r.city + ". ");
                if (r.direction) {
                    jT.append("One Way. ");
                }
                jT.append(r.speedLimit + " ");
                jT.append(r.roadClass + " ");
                jT.append(distance + "km ");
                jT.append("\n");
                i++;
            }
        } else if(currentN !=null) {//If selected Node then output information about it
            jT.insert("Node ID: " + currentN.id + ". ", 0);
            if(currentN.inSegements.size() != 0) {
                jT.append("Incoming Streets: ");
                for(Segement s: currentN.inSegements) {
                    jT.append(s.road.roadName + " ");
                }
                jT.append(". ");
            }
            if(currentN.outSegements.size() != 0) {
                jT.append("Outgoing Streets: ");
                for(Segement s: currentN.outSegements) {
                    jT.append(s.road.roadName + " ");
                }
            }
        }
        if(secondN != null && currentN != null) {
            if(!pathFound) {
                jT.append("Not Path Found");
            }
        }
    }

    @Override
    protected void onClick(MouseEvent e) {
//        To find current node by search through all of them. Left just in case needed
        for(Node n : graph.nodes.values()) {
            if(n.contains(e.getX(),e.getY())) {
                currentS = null;
                highLightS.clear();
                if(currentN != null) {
                    secondN = n;
                    ArrayList<Node> path = aSearch(currentN,secondN, currentN.location.distance(secondN.location));
                    if(path == null) {
                        pathFound = false;
                        return;
                    }
                    pathFound = true;
                    for(int i =0; i<path.size()-1;i++) {
                        Node node1 = path.get(i);
                        Node node2 = path.get(i+1);
                        for(Segement s : graph.segments) {
                            if(node1 == s.outNode && node2 == s.inNode) {
                                highLightS.add(s);
                                break;
                            } else if(node1 == s.inNode && node2 == s.outNode) {
                                highLightS.add(s);
                                break;
                            }
                        }
                    }
                } else {
                    currentN = n;
                    secondN = null;
                }
                return;
            }
        }
        //Search for node using QuadTree to find one that the mouse has clicked on
//        currentN = qTree.search(qTree,e.getX(),e.getY());
//        if(currentN != null) {
//            currentS = null;
//            highLightS.clear();
//            return;
//        }
        //Else search for segement clicked on
        for(Segement s : graph.segments) {
            if(s.contains(e.getX(),e.getY())) {
                currentN = null;
                secondN = null;
                currentS = s;
                highLightS.clear();
                return;
            }
        }

    }

    @Override
    protected void onSearch() {
        String searchBox = getSearchBox().getText();
        highLightS.clear();
        //If exact match then add the list to highlight
        for(Segement s : graph.segments) {
            if(s.road.roadName.equals(searchBox)){
                highLightS.add(s);
            }
        }
        if (!highLightS.isEmpty()) {
            currentS = null;
            currentN = null;
        }

        List<Character> searchList = new ArrayList<>();
        for(char c : searchBox.toCharArray()) {
            searchList.add(c);
        }
        ArrayList<Segement> search = get(searchList); //Use Trie to get segments that match the searched text
        if(search !=null) {
            for (Segement s : search) {
                if (s != null) {
                    highLightS.add(s);
                }
            }
        }


    }

    @Override
    protected void onMove(Move m) {
        //Move in each direction, plus zoom in and out
        if(m.toString().equals("NORTH")) {
            center = center.moveBy(0, -2);
        } else if(m.toString().equals("SOUTH")) {
            center = center.moveBy(0, 2);
        } else if(m.toString().equals("EAST")) {
            center = center.moveBy(-2, 0);
        } else if(m.toString().equals("WEST")) {
            center = center.moveBy(2, 0);
        } else if(m.toString().equals("ZOOM_IN")) {
            scale = scale + 5;
        } else if(m.toString().equals("ZOOM_OUT")) {
            scale = scale - 5;
        }
    }

    @Override
    protected void onLoad(File nodes, File roads, File segments, File polygons) {
        //Reset variables each time it loads
        graph = new Graph();
        currentN = null;
        currentS = null;
        highLightS = new ArrayList<>();
        trie = new Trie(new ArrayList<>(), new HashMap<>());
        graph.initialise(roads,nodes,segments,polygons);
        Dimension d = getDrawingAreaDimension();
        qTree = new QuadTree(1,new Boundary(0,0,d.width,d.height));
        scale = (int) (d.getWidth() / (graph.maxL - graph.minL));
        center = center.moveBy(graph.minL * 1.75,graph.maxL);
        //Add all segement names to Trie
        for(Segement s : graph.segments) {
            ArrayList<Character> cList = new ArrayList<>();
            for(char c : s.road.roadName.toCharArray()) {
                cList.add(c);
            }
            add(cList,s);
        }
        iterAp();
    }

    /**
     * Add character name to Trie. If any of the characters aren't already in there then add a new node to the Trie
     * @param characters
     * @param s
     */
    public void add(ArrayList<Character> characters, Segement s) {
        Trie node = trie;
        for(Character c : characters) {
            if(!(node.children.keySet().contains(c))) {
                Trie newN = new Trie(new ArrayList<>(), new HashMap<>());
                node.children.put(c,newN);
            }
            node = node.children.get(c);
        }
        node.segements.add(s);
    }

    /**
     * Get list of segments that match name
     * @param name
     * @return
     */
    public ArrayList<Segement> get(List<Character> name) {
        Trie node = trie;
        for(Character c: name) {
            if(!(node.children.keySet().contains(c))) {
                return null;
            }
            node = node.children.get(c);
        }
        if(!(node.segements.isEmpty())) {
            return node.segements;
        }
        //Use a Queue to loop through the items that match the name and return the ones that match
        Queue<Trie> search = new ArrayDeque<>();
        ArrayList<Segement> returning = new ArrayList<>();
        search.add(node);
        while(!(search.isEmpty())) {
            node = search.poll();
            search.addAll(node.children.values());
            for(Segement s : node.segements) {
                returning.add(s);
            }
        }
        return returning;
    }

    public ArrayList aSearch(Node start, Node goal, double f) {
        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Fringe> fringes= new ArrayList<>();
        ArrayList<Fringe> usedfringes= new ArrayList<>();
        Fringe f1 = new Fringe(start,null,0,f);
        fringes.add(f1);
        boolean goalF = false;
        while(!fringes.isEmpty()) {
            double min =999;
            int minIndex =0;
            for(int i = 0;i<fringes.size();i++) {
                if(fringes.get(i).f < min) {
                    min = fringes.get(i).f;
                    minIndex = i;
                }
            }
            Fringe fringe = fringes.get(minIndex);
            fringes.remove(fringe);
            usedfringes.add(fringe);
            if(!visited.contains(fringe.n)) {
                if(fringe.n == goal) {
                    usedfringes.add(fringe);
                    goalF = true;
                    break;
                }
                visited.add(fringe.n);
                ArrayList out = fringe.n.outSegements;
                for(int i =0; i<out.size();i++) {
                    Segement s = (Segement) out.get(i);
                    Node n1 = null;
                    if(s.outNode == fringe.n) {
                        n1 = s.inNode;
                    } else {
                        n1 = s.outNode;
                    }
                    if(!visited.contains(n1)) {
                        Location l = n1.location;
                        double g = fringe.g + l.distance(start.location);
                        double fNew = g + l.distance(goal.location);
                        Fringe f2 = new Fringe(n1,fringe.n,g,fNew);
                        fringes.add(f2);
                    }
                }
            }
        }
        if(!goalF) {
            return null;
        }

        Node n = goal;
        ArrayList<Node> path = new ArrayList<>();
        path.add(n);
        while(n != start) {
            Fringe f3 = null;
            for(Fringe fr : usedfringes) {
                if(fr.n == n) {
                    f3 = fr;
                    break;
                }
            }
            for(Fringe fr : usedfringes) {
                    if (fr.n == f3.previous) {
                        n = fr.n;
                        path.add(n);
                        break;
                    }
            }
        }
        return path;
    }

    public void articulationPoints(Node first, double depth, Node root) {
        Stack<APoints> apStack = new Stack<>();
        apStack.push(new APoints(first,depth,root));
        ArrayList<Node> children = new ArrayList<>();
        while(!apStack.empty()) {
            APoints ap = apStack.peek();
            Node n = ap.node;
            if(n.depth == Double.POSITIVE_INFINITY) {
                n.depth = depth;
                n.reachBack = depth;
                children = n.getNeighbours();
                if(children.contains(ap.parent)) {
                    children.remove(ap.parent);
                }
            } else if(!children.isEmpty()) {
                Node child = children.remove(0);
                if(child.depth<Double.POSITIVE_INFINITY) {
                    n.reachBack = Math.min(child.depth,n.reachBack);
                } else {
                    apStack.push(new APoints(child,depth+1,n));
                }
            } else {
                if(n != first) {
                    ap.parent.reachBack = Math.min(n.reachBack,ap.parent.reachBack);
                    if(n.reachBack >= ap.parent.depth) {
                        aPoints.add(ap.parent);
                    }
                }
                apStack.remove(ap);
            }
        }
    }


    public void iterAp() {
        Node root = null;
        for(Node n : graph.nodes.values()) {
            n.depth = Double.POSITIVE_INFINITY;
            n.findNeighbours(graph.nodes);
            root = n;


        }

        aPoints.clear();
        int numSubTrees = 0;
        if(root.depth == Double.POSITIVE_INFINITY) {
            root.depth = 0;
            for (int i = 0; i < root.getNeighbours().size(); i++) {
                Node n1 = root.getNeighbours().get(i);
                if (n1.depth == Double.POSITIVE_INFINITY) {
                    articulationPoints(n1, 1, root);
                    numSubTrees++;
                }
            }
            if (numSubTrees > 1) {
                aPoints.add(root);
            }
        }

    }


    public static void main(String[] args) {
        new Mapping();
        graph = new Graph();
        currentN = null;
        currentS = null;
        highLightS = new ArrayList<>();
        aPoints = new ArrayList<>();
        center = Location.newFromLatLon(-36.847622, 174.763444);
        for(Node n : graph.nodes.values()) {
            n.findNeighbours(graph.nodes);
        }
    }
}

// code for COMP261 assignments
