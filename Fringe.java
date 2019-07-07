public class Fringe {
    public Node n;
    public Node previous;
    public double g;
    public double f;

    public Fringe (Node node, Node p, double g, double f) {
        n = node;
        previous = p;
        this.g = g;
        this.f = f;
    }
}
