/**
 * Created with IntelliJ IDEA.
 * User: ecm
 * Date: 15/11/13
 * Time: 21:48
 * To change this template use File | Settings | File Templates.
 */

public class Edge {
    private Vertex target;

    private int distance;

    public Edge(Vertex target, int distance) {
        this.target = target;
        this.distance = distance;
    }

    public Vertex getTarget() {
        return target;
    }

    public int getDistance() {
        return distance;
    }
}
