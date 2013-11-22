import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ecm
 * Date: 15/11/13
 * Time: 21:54
 * To change this template use File | Settings | File Templates.
 */
public class Graph {
    private List<Vertex> vertices = new ArrayList<Vertex>();
    private int distanceParcours;

    public Graph(Vertex... vertices) {
        this.vertices.addAll(Arrays.asList(vertices));
    }

    public int getDistance(String from, String to) {
        Vertex origin = null;
        Vertex target = null;

        for (Vertex vertex: vertices){
            if (vertex.getName() == from){
                origin = vertex;
            }
            if (vertex.getName() == to){
                target = vertex;
            }
        }

        List<Edge> listEdges = origin.getEdges();
        for (Edge edge: listEdges){
            if (edge.getTarget() == target){
                return edge.getDistance();
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        ArrayList<String> listVertices = new ArrayList<String>();
        for (Vertex vertex: this.vertices){
            listVertices.add(vertex.getName());
        }
        return listVertices.toString();
    }

    public boolean containsVertex(Vertex vertex){
        return vertices.contains(vertex);
    }

    public void visit(Vertex vertex){
        vertex.setVisited(true);
    }

    public void addVertex(Vertex vertex) {
        this.vertices.add(vertex);
    }


    public Vertex currentVertex() {
        int n = vertices.size();
        return vertices.get(n-1);
    }


    public List<Vertex> getVertices(){
        return  this.vertices;
    }

    public void setDistanceParcours(){
        distanceParcours = this.vertices.get(this.vertices.size()-1).getVisitValue();
    }

    public int getDistanceParcours() {
        return distanceParcours;
    }

    public void algoDijkstra(Graph circuit,Vertex origin, Vertex target) {
        if (!circuit.isEmpty() && origin.existsInCircuit(circuit) && target.existsInCircuit(circuit)){
            this.addVertex(origin);
            Vertex vertexCourant = origin;

            while  (vertexCourant.getName() != target.getName()){
                vertexCourant = vertexCourant.bestNeighbour(circuit, this);
                this.setDistanceParcours();
            }
        }
    }

    public void deleteIntermedVertex() {

        int n = this.vertices.size();
        int i = n-2;
        Vertex currentVertex = this.currentVertex();
        Vertex intermedVertex = this.vertices.get(i);

        while (intermedVertex.getName() != this.vertices.get(0).getName()){
            while (intermedVertex.getName() != currentVertex.originYieldedVisitValue().getName()){
                this.vertices.remove(i);
                i -= 1;
                intermedVertex = this.vertices.get(i);
            }

            currentVertex = intermedVertex;
            if (i != 0){
                i -=1;
                intermedVertex = this.vertices.get(i);
            }
        }
    }

    public void symmetricallyConnect() {
        int n =  this.getVertices().size();
        for (int i=0;i<n-1;i++){
            Vertex origin = this.getVertices().get(i);
            Vertex target = this.getVertices().get(i+1);
            origin.symmetricallyConnectTo(target);
        }
    }

    public boolean isEmpty() {
        return this.vertices.isEmpty();
    }

    public void dijkstraOptim(Graph circuitFr, Vertex origin, Vertex target) {
        circuitFr.symmetricallyConnect();
        this.algoDijkstra(circuitFr,origin,target);
        this.deleteIntermedVertex();
    }
}
