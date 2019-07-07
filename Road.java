/**
 * Road Class for storing all the information about each road
 */

public class Road {
    public String id;
    public int type;
    public String roadName;
    public String city;
    public String speedLimit;
    public String roadClass;
    public boolean direction;
    public boolean carUse;
    public boolean pedUse;
    public boolean bikeUse;

    public Road (String id, int type, String name, String city,int speed, int direction, int rClass,int cuse, int puse, int buse) {
        this.id = id;
        this.type = type;
        roadName = name;
        this.city = city;
        //Depending on the value of speed it corresponds to a different speed limit
        if(speed == 0) {
            speedLimit = "5km/h.";
        }
        else if(speed == 1) {
            speedLimit = "20km/h.";
        }
        else if(speed == 2) {
            speedLimit = "40km/h.";
        }
        else if(speed == 3) {
            speedLimit = "60km/h.";
        }
        else if(speed == 4) {
            speedLimit = "80km/h.";
        }
        else if(speed == 5) {
            speedLimit = "100km/h.";
        }
        else if(speed == 6) {
            speedLimit = "110km/h.";
        }
        else if(speed == 7) {
            speedLimit = "No Speed Limit.";
        }
        //Depending on the value of the RoadClass it corresponds to a different class of road
        if(rClass == 0) {
            roadClass = ("Residential road.");
        }
        else if(rClass == 1) {
            roadClass = ("Collector road.");
        }
        else if(rClass == 2) {
            roadClass = ("Arterial road.");
        }
        else if(rClass == 3) {
            roadClass = ("Principal HW.");
        }
        else if(rClass == 4) {
            roadClass = ("Major HW.");
        }
        if(direction == 1) {
            this.direction = true;
        } else {
            this.direction = false;
        }
        if(cuse == 1) {
            carUse = true;
        } else {
            carUse = false;
        }
        if(puse == 1) {
            pedUse = true;
        } else {
            pedUse = false;
        }
        if(buse == 1) {
            bikeUse = true;
        } else {
            bikeUse = false;
        }
    }

}
