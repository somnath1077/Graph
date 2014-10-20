package simplegraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author sikdar
 */
public class SimpleGraph implements Graph, Cloneable {

    /**
     * A SimpleGraph object has a single field: --the adjacency list implemented
     * as a HashTable of heaps
     */
    private final Map<Integer, Collection<Integer>> adjacencyList;

    public SimpleGraph() {
        adjacencyList = new HashMap<>();
    }

    @Override
    public Collection<Edge> getEdges() {
        Collection<Edge> edges = new HashSet<Edge>();
        for (Integer u : adjacencyList.keySet()) {
            for (Integer v : adjacencyList.get(u)) {
                Edge e = new UndirectedEdge(u, v);
                edges.add(e);
            }
        }
        return edges;
    }

    @Override
    public SimpleGraph getInducedSubgraph(Collection<Integer> vertices) {
        SimpleGraph graph = new SimpleGraph();
        for (Integer v : vertices) {
            if (!isVertex(v)) {
                throw new IllegalArgumentException("Not a vertex: " + v);
            }

            graph.addVertex(v);
            Collection<Integer> nbrV = getNeighborhood(v);
            for (Integer u : nbrV) {
                if (vertices.contains(u)) {
                    graph.addEdge(v, u);
                }
            }
        }
        return graph;
    }

    @Override
    public SimpleGraph getSubgraph(Collection<Integer> V, Collection<Edge> E) {
        SimpleGraph graph = new SimpleGraph();
        for (Integer v : V) {
            graph.addVertex(v);
        }

        for (Edge e : E) {
            int v1 = e.getV1();
            int v2 = e.getV2();
            graph.addEdge(v1, v2);
        }
        return graph;
    }

    @Override
    public boolean isEdge(int v1, int v2) {
        if (!isVertex(v1) || !isVertex(v2)) {
            throw new IllegalArgumentException("One endpoint is not a vertex.");
        }
        return adjacencyList.get(v1).contains(v2);
    }

    @Override
    public boolean isVertex(int v) {
        return adjacencyList.containsKey(v);
    }

    @Override
    public int getDegree(int v) {
        return adjacencyList.get(v).size();
    }

    /**
     * getMaxDegree() returns the maximum degree in the graph. If empty, then it
     * returns -1.
     *
     * @return maximum degree or -1 if graph is empty
     */
    public int getMaxDegree() {
        Integer v = null;
        Iterator<Integer> iter = this.adjacencyList.keySet().iterator();
        if (iter.hasNext()) {
            v = iter.next();
        } else {
            return -1;
        }
         
        int max = this.getDegree(v);
        for (Integer u : this.getVertexSet()) {
            if (this.getDegree(u) > max) {
                max = this.getDegree(u);
            }
        }
        return max;
    }

    /**
     *
     * @return minimum degree or -1 if graph is empty
     */
    public int getMinDegree() {

        Integer v = null;
        Iterator<Integer> iter = this.adjacencyList.keySet().iterator();
        if (iter.hasNext()) {
            v = iter.next();
        } else {
            return -1;
        }

        // if size() != 0, then min degree >= 0
        int min = this.getDegree(v);
        for (Integer u : this.getVertexSet()) {
            if (this.getDegree(u) < min) {
                min = this.getDegree(u);
            }
        }

        return min;
    }

    @Override
    public Collection<Integer> getNeighborhood(int v) {
        if (!isVertex(v)) {
            throw new IllegalArgumentException();
        }
        return adjacencyList.get(v);
    }

    /**
     * Returns the vertex set of the graph
     *
     * @return the set of integers that form the vertices of the graph
     */
    @Override
    public Collection<Integer> getVertexSet() {
        return adjacencyList.keySet();
    }

    public Integer getLowestDegreeVertex() {
        if (this.size() == 0) {
            return null;
        }

        int minDeg = this.getMinDegree();

        for (Integer u : this.getVertexSet()) {
            if (getDegree(u) == minDeg) {
                return u;
            }
        }

        return null;
    }

    public Integer getHighestDegreeVertex() {
        if (this.size() == 0) {
            return null;
        }

        int maxDeg = this.getMaxDegree();
        for (Integer u : this.getVertexSet()) {
            if (getDegree(u) == maxDeg) {
                return u;
            }
        }
        return null;
    }

    @Override
    public boolean addVertex(int v) {
        if (!adjacencyList.containsKey(v)) {
            adjacencyList.put(v, new HashSet<Integer>());
            return true;
        }
        return false;
    }

    @Override
    public UndirectedEdge addEdge(int v1, int v2) {
        if (isVertex(v1) && isVertex(v2)) {
            adjacencyList.get(v1).add(v2);
            adjacencyList.get(v2).add(v1);
            UndirectedEdge e = new UndirectedEdge(v1, v2);
            return e;
        }
        throw new IllegalArgumentException("Cannot add edge.");
    }

    @Override
    public void deleteVertex(int v) {
        if (!isVertex(v)) {
            throw new IllegalArgumentException("Vertex not present.");
        }
        Collection<Integer> neighborhood = getNeighborhood(v);
        adjacencyList.remove(v);
        for (Integer neighbor : neighborhood) {
            adjacencyList.get(neighbor).remove(v);
        }
    }

    @Override
    public void deleteEdge(Edge e) {
        int v1 = e.getV1();
        int v2 = e.getV2();

        if (!isVertex(v1) || !isVertex(v2)) {
            throw new IllegalArgumentException("At least one endpoint not a vertex");
        }

        if (adjacencyList.get(v1).contains(v2)) {
            adjacencyList.get(v1).remove(v2);
            adjacencyList.get(v2).remove(v1);
        }
    }

    @Override
    public void deleteEdge(int v1, int v2) {
        UndirectedEdge e = new UndirectedEdge(v1, v2);
        deleteEdge(e);
    }

    @Override
    public boolean toggleEdge(int v1, int v2) {
        if (isEdge(v1, v2)) {
            deleteEdge(v1, v2);
            return false;
        }

        addEdge(v1, v2);
        return true;
    }

    @Override
    public SimpleGraph clone() throws CloneNotSupportedException {
        SimpleGraph cloned = new SimpleGraph();
        for (Integer u : this.getVertexSet()) {
            Collection<Integer> nbr = this.getNeighborhood(u);
            Collection<Integer> cloneNbr = new HashSet<>();
            for (Integer v : nbr) {
                cloneNbr.add(v);
            }
            cloned.adjacencyList.put(u, cloneNbr);
        }
        return cloned;
    }

    @Override
    public boolean isConnected(boolean strongly) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        /*
         Add some arbitrary element to the visited and queue
         data structures. 
         */
        Iterator<Integer> iter = adjacencyList.keySet().iterator();
        if (iter.hasNext()) {
            Integer vertex = iter.next();
            queue.add(vertex);
            visited.add(vertex);
        } else {
            return true;
        }

        while (!queue.isEmpty()) {
            Integer next = queue.remove();
            Collection<Integer> nextNbHood = getNeighborhood(next);
            for (Integer nbr : nextNbHood) {
                if (!visited.contains(nbr)) {
                    queue.add(nbr);
                    visited.add(nbr);
                }
            }
        }
        return visited.size() == adjacencyList.size();
    }
    
    /**
     * Checks if the graph is two colorable. 
     * 
     * @return true if it is two colorable; else false.
     */
    public boolean isTwoColorable() {
        Collection<Integer> vertices = adjacencyList.keySet();
        Map<Integer, String> coloring = new HashMap<>();
        
        // A vertex is visited if it and all its neighbors
        // are in the queue
        Collection<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        Iterator<Integer> iter = vertices.iterator();
        if (iter.hasNext()) {
            Integer v = iter.next();
            visited.add(v);
            queue.add(v);
            coloring.put(v, "red");
            
        } else {             // the graph is empty
            return true;     // and hence return
        }

        while (!queue.isEmpty()) {
            Integer v = queue.remove();
            Collection<Integer> nbrV = adjacencyList.get(v);
            if (nbrV.isEmpty()) {
                coloring.put(v, "green");
            } else {
                for (Integer u : nbrV) {
                    if (coloring.get(u) == coloring.get(v)) {
                        return false;
                    }
                    if (!visited.contains(u)) {
                        visited.add(u);
                        queue.add(u);
                        if (coloring.get(v) == "red") {
                            coloring.put(u, "green");

                        } else {
                            coloring.put(u, "red");
                        }
                    }
                }
            }
            
            // check if all vertices have been visited
            // this needs to be checked if the graph is disconnected
            if (queue.isEmpty() && visited.size() != adjacencyList.size()) {
                for (Integer u : vertices) {
                    if (!visited.contains(u)) {
                        queue.add(u);
                        visited.add(u);
                        break;
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        String graphStr = "";
        for (Integer v : adjacencyList.keySet()) {
            graphStr += "vertex: " + Integer.toString(v) + " Neighbors: ";
            Collection<Integer> vNbr = getNeighborhood(v);
            for (Integer nbr : vNbr) {
                graphStr += Integer.toString(nbr) + " ";
            }

            graphStr += "\n";
        }
        return graphStr;
    }

    @Override
    public int addNewVertex() {
        int v = adjacencyList.size();
        for (int i = 0; i < v; i++) {
            if (!adjacencyList.containsKey(i)) {
                v = i;
                break;
            }
        }

        addVertex(v);
        return v;
    }

    @Override
    public int size() {
        return adjacencyList.size();
    }

    @Override
    public int getNumberOfEdges() {
        int numE = 0;
        for (Integer i : adjacencyList.keySet()) {
            numE += adjacencyList.get(i).size();
        }
        return numE;
    }

    public static void main(String[] args) {
        Graph g = GraphReader.readGraph("smallgraph.csv");

        System.out.println(g.size());

        System.out.println(g.getNumberOfEdges());

        System.out.println(g.isConnected(true));

        SimpleGraph g1 = new SimpleGraph();
        g1.addVertex(1);
        g1.addVertex(2);
        g1.addVertex(3);
        g1.addVertex(4);
        g1.addEdge(1, 2);
        g1.addEdge(1, 3);
        g1.addEdge(2, 4);
        System.out.println(g1.isTwoColorable());

    }

}
