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
public class SimpleGraph implements Graph {

    /**
     * A SimpleGraph object has two fields: --the number of edges --the
     * adjacency list implemented as a HashTable of heaps
     */
    private int numEdges;
    private final Map<Integer, Collection<Integer>> adjacencyList;

    public SimpleGraph() {
        adjacencyList = new HashMap<>();
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

    @Override
    public Collection<Integer> getNeighborhood(int v) {
        if (!isVertex(v)) {
            throw new IllegalArgumentException();
        }
        return adjacencyList.get(v);
    }

    @Override
    public boolean addVertex(int v) {
        if (!adjacencyList.containsKey(v)) {
            adjacencyList.put(v, new HashSet<>());
            return true;
        }
        return false;
    }

    @Override
    public Edge addEdge(int v1, int v2) {
        if (isVertex(v1) && isVertex(v2)) {
            adjacencyList.get(v1).add(v2);
            adjacencyList.get(v2).add(v1);
            Edge e = new Edge(v1, v2);
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
        Edge e = new Edge(v1, v2);
        deleteEdge(e);
    }

    @Override
    public boolean isConnected(boolean strongly) {
        Set<Integer> visited = new HashSet<>();

        Queue<Integer> queue = new LinkedList<>();

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

    public static void main(String[] args) {
        SimpleGraph g = new SimpleGraph();
        g.addVertex(2);
        g.addVertex(3);

        int a = g.addNewVertex();
        int b = g.addNewVertex();

        g.addEdge(a, b);
        g.addEdge(b, 3);
        g.addEdge(a, 2);

        System.out.println(g);

        System.out.println(g.isConnected(true));
    }

}
