import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ecm
 * Date: 15/11/13
 * Time: 21:48
 * To change this template use File | Settings | File Templates.
 */

public class Vertex {
    private String name;

    private List<Edge> edges = new ArrayList<Edge>();

    private boolean visited = false;

    private int visitValue;

    private Vertex thisOriginYieldedVisitValue;

    public Vertex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void connectTo(Vertex target, int distance) {
        edges.add(new Edge(target, distance));
    }

    public  List<Edge> getEdges(){
        return edges;
    }

    public boolean isConnectedTo(Vertex target){
        for (Edge edge: edges){
            if (edge.getTarget() == target){
                return true;
            }
        }
        return false;
    }

    public boolean symmetricallyConnectTo(Vertex target) {
        if (this.isConnectedTo(target)){
            Graph arc = new Graph(this,target);
            int distance = arc.getDistance(this.getName(),target.getName());
            target.connectTo(this,distance);
            return true;
        }

        if ( target.isConnectedTo(this)){
            Graph arc = new Graph(target,this);
            int distance = arc.getDistance(target.getName(),this.getName());
            this.connectTo(target,distance);
            return true;
        }

        return false;
    }

    public void setVisited(Boolean visit){
        this.visited = visit;
    }

    public boolean isVisited(){
        return visited;
    }

    public void setVisitValue(int visitValue){
        this.visitValue = visitValue;
    }

    public int getVisitValue(){
        return visitValue;
    }

    public boolean oldInferiorToNewVisitValue(Graph circuit, Vertex originOfEdge) {
        if (circuit.isEmpty()){
            return false;
        }

        int newVisitValue = originOfEdge.getVisitValue() + circuit.getDistance(originOfEdge.getName(),this.getName());
        if (!this.visited){
            this.visited = true;
            this.visitValue = newVisitValue;
            this.thisOriginYieldedVisitValue = originOfEdge;
            return false;
        }

        if (this.visitValue < newVisitValue){
            return true;
        }

        this.visitValue = newVisitValue;
        this.thisOriginYieldedVisitValue = originOfEdge;
        return false;
    }

    public ArrayList<Vertex> areNeighboursNotInGraph(Graph circuit, Graph graph) {
        List<Vertex> myNeighboursToConsider = new ArrayList<Vertex>();
        if (!circuit.isEmpty()){
            for (Vertex vertex: circuit.getVertices()){
                if (this.symmetricallyConnectTo(vertex) && !graph.containsVertex(vertex)){

                    myNeighboursToConsider.add(vertex);
                }
            }
        }

        return (ArrayList<Vertex>) myNeighboursToConsider;
    }

    public Vertex bestNeighbour(Graph circuit, Graph graph) {
        List<Vertex> myNeighboursToConsider = this.areNeighboursNotInGraph(circuit, graph);

        for (Vertex neighbour: myNeighboursToConsider){
            neighbour.oldInferiorToNewVisitValue(circuit,this);
        }

        int n = myNeighboursToConsider.size();
        Vertex neighbourMinVisitValue = myNeighboursToConsider.get(0);

        for (int i=1;i<n;i++){
            if (myNeighboursToConsider.get(i-1).getVisitValue() > myNeighboursToConsider.get(i).getVisitValue()){
               neighbourMinVisitValue = myNeighboursToConsider.get(i);
            }
        }
        graph.addVertex(neighbourMinVisitValue);

        return neighbourMinVisitValue;
    }

    public Vertex originYieldedVisitValue() {

        return thisOriginYieldedVisitValue;
    }

    public void setOriginVertexVisitValue(Vertex originVertexVisitValue) {
        this.thisOriginYieldedVisitValue = originVertexVisitValue;
    }

    public boolean existsInCircuit(Graph circuit) {
        return circuit.containsVertex(this);
    }

    public Vertex getOriginY() {
        return thisOriginYieldedVisitValue;
    }
}
