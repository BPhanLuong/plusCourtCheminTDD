import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ecm
 * Date: 15/11/13
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class GraphTest {
    private Vertex lille = new Vertex("Lille");
    private Vertex paris = new Vertex("Paris");
    private Vertex reims = new Vertex("Reims");
    private Vertex nancy = new Vertex("Nancy");
    private Vertex lyon = new Vertex("Lyon");
    private Vertex marseille = new Vertex("Marseille");
    private Vertex lemans = new Vertex("Le Mans");
    private Vertex nantes = new Vertex("Nantes");
    private Vertex bordeaux = new Vertex("Bordeaux");
    private Vertex toulouse = new Vertex("Toulouse");
    private Vertex clermont = new Vertex("Clermont Ferrant");
    private Vertex montpellier = new Vertex("Montpellier");

    @Before
    public void setup() {
        lille.connectTo(reims, 206);
        lille.connectTo(paris, 222);
        lille.connectTo(nancy, 418);

        reims.connectTo(paris, 144);
        reims.connectTo(nancy, 245);
        reims.connectTo(lyon, 489);

        paris.connectTo(lyon, 465);
        paris.connectTo(lemans, 208);
        paris.connectTo(clermont, 423);

        lyon.connectTo(clermont, 166);
        lyon.connectTo(marseille, 313);
        lyon.connectTo(montpellier, 304);

        lemans.connectTo(nantes, 189);
        lemans.connectTo(bordeaux, 443);

        nantes.connectTo(bordeaux, 347);

        bordeaux.connectTo(toulouse, 243);

        toulouse.connectTo(montpellier, 245);

        montpellier.connectTo(marseille, 169);
        montpellier.connectTo(toulouse, 245);

        marseille.connectTo(montpellier, 169);

        clermont.connectTo(lyon, 166);
        clermont.connectTo(montpellier, 333);
        clermont.connectTo(marseille, 474);
    }

    @Test
    public void getDistanceForTwoAdjacentVertices() {
        Graph graph = new Graph(paris, lyon);

        assertEquals(graph.getDistance("Paris", "Lyon"), 465);
    }

    @Test
    public void checkConnectionVertices() {
        assertTrue("the two vertices are not connected",paris.isConnectedTo(lyon));

    }

    @Test
    public void symmetricConnection(){
        assertTrue("not symmetrically Connected",nancy.symmetricallyConnectTo(reims));
    }


    @Test
    public void belongToGraph(){
        Graph graph = new Graph(paris);
        assertTrue("the vertex isn't in the graph",graph.containsVertex(paris));
    }

    @Test
    public void wasVisited(){
        Graph circuit = new Graph(paris,lyon,lille);
        circuit.visit(paris);
        assertTrue("vertex wasn't visited",paris.isVisited());
    }

    @Test
    public void getVisitValueOfVertex(){
        Graph graph = new Graph(paris);
        paris.setVisitValue(0);
        assertEquals(paris.getVisitValue(), 0);

        graph.addVertex(lyon);
        assertTrue(graph.containsVertex(lyon));

        lyon.setVisitValue(paris.getVisitValue()+graph.getDistance(paris.getName(),lyon.getName()));
        assertEquals(lyon.getVisitValue(),465);
    }

    @Test
    public void compareVisitValuesOfVertex(){
        Graph circuit = new Graph(paris,lyon,clermont);
        lyon.setVisitValue(465);
        int distanceParisClermont = circuit.getDistance(paris.getName(),clermont.getName()); // 423
        circuit.visit(clermont);
        clermont.setVisitValue(distanceParisClermont);
        assertTrue(clermont.oldInferiorToNewVisitValue(circuit,lyon));
    }

    @Test
    public void currentVertex(){
        Graph graph = new Graph(lille);
        assertEquals(graph.currentVertex().getName(), "Lille");

        graph.addVertex(paris);
        assertEquals(graph.currentVertex().getName(),"Paris");
    }

    @Test
    public void exploreNeighborNotInGraph(){
        Graph circuitFr = new Graph(lille,paris,reims,nancy,lyon,marseille,lemans,nantes,bordeaux,toulouse,clermont,montpellier);
        Graph graph = new Graph(lyon,clermont);

        assertEquals(lyon.areNeighboursNotInGraph(circuitFr, graph).get(0).getName(),"Paris");
        assertEquals(lyon.areNeighboursNotInGraph(circuitFr, graph).get(1).getName(),"Reims");
    }

    @Test
    public void bestNeighbour(){
        Graph circuitFr = new Graph(lille,paris,reims,nancy,lyon,marseille,lemans,nantes,bordeaux,toulouse,clermont,montpellier);
        circuitFr.symmetricallyConnect();
        Graph graph = new Graph(lyon,clermont);
        lyon.setVisitValue(0);

        Vertex bestNeighbour = lyon.bestNeighbour(circuitFr,graph);
        assertEquals(bestNeighbour.getVisitValue(),304);
        assertEquals(graph.currentVertex(),montpellier);
    }

    @Test
    public void distanceGraph(){
        Graph circuitFr = new Graph(lille,paris,reims,nancy,lyon,marseille,lemans,nantes,bordeaux,toulouse,clermont,montpellier);
        Graph graph = new Graph(lyon,clermont);
        lyon.setVisitValue(0);
        lyon.bestNeighbour(circuitFr,graph);
        graph.setDistanceParcours();
        assertEquals(graph.getDistanceParcours(),304);
    }

    @Test
    public void whichOriginYieldedVisitValue(){
        Graph graph = new Graph(lille,reims,paris);
        lille.setVisitValue(0);

        paris.oldInferiorToNewVisitValue(graph,lille);
        reims.oldInferiorToNewVisitValue(graph,lille);

        assertEquals(paris.originYieldedVisitValue(),lille);
        assertEquals(reims.originYieldedVisitValue(),lille);

        paris.oldInferiorToNewVisitValue(graph,reims);
        assertEquals(paris.originYieldedVisitValue(),lille);
    }

    @Test
    public void graphOriginTarget(){
        Graph circuitFr = new Graph(lille,paris,reims,nancy,lyon,marseille,lemans,nantes,bordeaux,toulouse,clermont,montpellier);
        circuitFr.symmetricallyConnect();

        Graph graph = new Graph();
        Vertex origin = lille;
        Vertex target = marseille;

        graph.algoDijkstra(circuitFr,origin,target);
        assertEquals(graph.getDistanceParcours(),1000);
    }

    @Test
    public void deleteIntermedVertex(){
        Graph graph = new Graph(lille,reims,paris);
        graph.symmetricallyConnect();
        paris.setOriginVertexVisitValue(lille);
        reims.setOriginVertexVisitValue(lille);
        graph.deleteIntermedVertex();
        Graph graphTest = new Graph(lille,paris);
        assertEquals(graph.toString(), graphTest.toString());

    }

    @Test
    public void plusCourtCheminDijkstra(){
        Graph circuitFr = new Graph(lille,paris,reims,nancy,lyon,marseille,lemans,nantes,bordeaux,toulouse,clermont,montpellier);
        circuitFr.symmetricallyConnect();
        Graph graph = new Graph();

        Vertex origin = lille;
        Vertex target = marseille;

        graph.algoDijkstra(circuitFr,origin,target);
        graph.deleteIntermedVertex();

        Graph graphTest = new Graph(lille,paris,lyon,marseille);

        assertEquals(graph.toString(), graphTest.toString());
        assertEquals(graph.getDistanceParcours(),1000);
    }

    @Test
    public void graphVide(){
        Graph circuitFr = new Graph();
        assertTrue(circuitFr.isEmpty());
    }

    @Test
    public void villeNotExists(){
        Graph circuitFr = new Graph(paris,lyon);
        assertTrue(!marseille.existsInCircuit(circuitFr));
    }

    @Test
    public void dijkstraOptim(){
        Graph circuitFr = new Graph(lille,paris,reims,nancy,lyon,marseille,lemans,nantes,bordeaux,toulouse,clermont,montpellier);
        Graph graph = new Graph();

        Vertex origin = lille;
        Vertex target = marseille;

        graph.dijkstraOptim(circuitFr, origin, target);
        Graph graphTest = new Graph(lille,paris,lyon,marseille);

        assertEquals(graph.toString(), graphTest.toString());
        assertEquals(graph.getDistanceParcours(),1000);
    }
}