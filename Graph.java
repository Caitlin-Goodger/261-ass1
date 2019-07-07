import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Graph of Auckland.
 * Contains information about all the roads, nodes and road segments.
 * Initialise Graph by reading in the all the files and inputting all the information into the classes.
 */

public class Graph {
    public HashMap<String,Road> roads;
    public HashMap<String,Node> nodes;
    public HashSet<Segement> segments;
    public double minL;
    public double maxL;
    public double minWidthL;

    public Graph() {
        roads = new HashMap<>();
        nodes = new HashMap<>();
        segments = new HashSet<>();

    }

    /**
     * Read all files into the classes
     * @param roadFile
     * @param nodeFile
     * @param segFile
     * @param polygons
     */
    public void initialise(File roadFile,File nodeFile, File segFile, File polygons) {
        //Read Road Information into the Road Map
        try {
            if(!roadFile.exists()) {
                return;
            }
            BufferedReader input = new BufferedReader(new FileReader(roadFile));

            String line;
            input.readLine();
            while ((line = input.readLine()) != null) {
                String[] splitParts = line.split("\t");
                String id = splitParts[0];
                int type = Integer.parseInt(splitParts[1]);
                String roadName = splitParts[2];
                String city = splitParts[3];
                int direction = Integer.parseInt(splitParts[4]);
                int speedLimit = Integer.parseInt(splitParts[5]);
                int roadClass = Integer.parseInt(splitParts[6]);
                int carUse = Integer.parseInt(splitParts[7]);
                int pedUse = Integer.parseInt(splitParts[8]);
                int bikeUse =Integer.parseInt(splitParts[9]);
                Road r = new Road(id,type,roadName,city,speedLimit,direction,roadClass,carUse,pedUse,bikeUse);
                roads.put(id,r); // Load data read into roads map
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e ) {
            e.printStackTrace();
        }
        //Read Node Information into the Node Map
        try {
            if(!nodeFile.exists()) {
                return;
            }
            BufferedReader input = new BufferedReader(new FileReader(nodeFile));

            String line;
            input.readLine();
            while ((line = input.readLine()) != null) {
                String[] splitParts = line.split("\t");
                String id = splitParts[0];
                Double lat = Double.parseDouble(splitParts[1]);
                Double lon = Double.parseDouble(splitParts[2]);
                Location l = Location.newFromLatLon(lat,lon);
                if(l.x<minL) {
                    minL = l.x;
                }
                if(l.x > maxL) {
                    maxL = l.x;
                }
                if(l.y < minWidthL) {
                    minWidthL = l.y;
                }
                Node n = new Node(id,l);
                nodes.put(id,n);//Put into node map
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e ) {
            e.printStackTrace();
        }

        //Read Segement information into the file.
        try {
            if(!segFile.exists()) {
                return;
            }
            BufferedReader input = new BufferedReader(new FileReader(segFile));

            String line;
            input.readLine();
            while ((line = input.readLine()) != null) {
                String[] splitParts = line.split("\t");
                String roadId = splitParts[0];
                double length = Double.parseDouble(splitParts[1]);
                String node1ID = splitParts[2];
                String node2ID = splitParts[3];
                Road r = roads.get(roadId);
                Node node1 = nodes.get(node1ID);
                Node node2 = nodes.get(node2ID);
                ArrayList<Location> locations = new ArrayList<>();
                for(int i =4;i<splitParts.length-1;i=i+2) {
                    Double lat = Double.parseDouble(splitParts[i]);
                    Double lon = Double.parseDouble(splitParts[i+1]);
                    Location l = Location.newFromLatLon(lat,lon);
                    locations.add(l);
                }
                Segement s = new Segement(r,node1,node2,length,locations);
                segments.add(s);

                int roadType = r.type;
                if(roadType ==0) { // If road is two way then add incoming and outgoing roads in both directions
                    node1.addInSegement(s);
                    node1.addOutSegement(s);
                    node2.addInSegement(s);
                    node2.addOutSegement(s);
                } else {//Else only add them one way. Unsure which way it is going
                    if(node1 != null && node2 != null) {
//                        node2.addInSegement(s);
//                        node1.addOutSegement(s);
                        node1.addInSegement(s);
                        node1.addOutSegement(s);
                        node2.addInSegement(s);
                        node2.addOutSegement(s);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

}
