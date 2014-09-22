package directedgraph;

import java.util.*;
import simplegraph.Edge;

import simplegraph.Graph;

class Neighborhood {

    private final Collection<Arc> inArcs;
    private final Collection<Arc> outArcs;

    public Neighborhood() {
        inArcs = new HashSet<Arc>();
        outArcs = new HashSet<Arc>();
    }

    public void addInArc(Arc a) {
        inArcs.add(a);
    }

    public void addOutArc(Arc a) {
        outArcs.add(a);
    }

    public void deleteInArc(Arc a) {
        inArcs.remove(a);
    }

    public void deleteOutArc(Arc a) {
        outArcs.remove(a);
    }

    public Collection<Arc> getInArcs() {
        return inArcs;
    }

    public Collection<Arc> getOutArcs() {
        return outArcs;
    }

    public boolean isInArc(Arc a) {
        return inArcs.contains(a);
    }

    public boolean isOutArc(Arc a) {
        return outArcs.contains(a);
    }

    public int size() {
        return inArcs.size() + outArcs.size();
    }

    public int numInArcs() {
        return inArcs.size();
    }

    public int numOutArcs() {
        return outArcs.size();
    }

    public Collection<Integer> getInNeighbors() {
        Collection<Integer> inNeighbors = new HashSet<Integer>();
        for (Arc a : inArcs) {
            inNeighbors.add(a.tail());
        }

        return inNeighbors;
    }

    public Collection<Integer> getOutNeighbors() {
        Collection<Integer> outNeighbors = new HashSet<Integer>();
        for (Arc a : outArcs) {
            outNeighbors.add(a.head());
        }

        return outNeighbors;
    }
}

public class Digraph implements Graph {

    private final Map<Integer, Neighborhood> adjacencyList;

    public Digraph() {
        adjacencyList = new HashMap<Integer, Neighborhood>();
    }

    public boolean addVertex(int u) {
        if (adjacencyList.containsKey(u)) {
            return false;
        }
        Neighborhood nh = new Neighborhood();
        adjacencyList.put(u, nh);
        return true;
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

    public boolean isVertex(int u) {
        return adjacencyList.containsKey(u);
    }

    public Arc addArc(int u, int v) {
        return addArc(u, v, 1);
    }

    public Arc addArc(int u, int v, double weight) {

        if (!isVertex(u)) {
            throw new IllegalArgumentException("Tail is not vertex: " + u);
        }
        if (!isVertex(v)) {
            throw new IllegalArgumentException("Head is not vertex: " + v);
        }

        Arc e = new Arc(u, v, weight);
        adjacencyList.get(u).addOutArc(e);      // add e to the out-arcs set of u
        adjacencyList.get(v).addInArc(e);       // add e to the in-arcs set of v 
        return e;
    }

    @Override
    public Edge addEdge(int v1, int v2) {
        return addArc(v1, v2);
    }

    public boolean isArc(Arc a) {
        int aHead = a.head();
        return adjacencyList.get(aHead).isInArc(a);
    }

    // The isEdge() method is from the Graph interface
    @Override
    public boolean isEdge(int v1, int v2) {
        Arc a = new Arc(v1, v2);
        return isArc(a);
    }

    public void deleteVertex(int v) {
        if (!isVertex(v)) {
            throw new IllegalArgumentException("Not a vertex.");
        }

        Neighborhood nbrHood = adjacencyList.get(v);
        Collection<Arc> inArcs = nbrHood.getInArcs();
        Collection<Arc> outArcs = nbrHood.getOutArcs();

        adjacencyList.remove(v);

        for (Arc e : inArcs) {
            int tail = e.tail();
            Neighborhood tailNbrHood = adjacencyList.get(tail);
            tailNbrHood.deleteOutArc(e);
        }

        for (Arc e : outArcs) {
            int head = e.head();
            Neighborhood headNbrHood = adjacencyList.get(head);
            headNbrHood.deleteInArc(e);
        }
    }

    public void deleteArc(int u, int v) {
        if (!isVertex(u)) {
            throw new IllegalArgumentException("Tail is not vertex: " + u);
        }
        if (!isVertex(v)) {
            throw new IllegalArgumentException("Head is not vertex: " + v);
        }

        Neighborhood neighborsOfu = adjacencyList.get(u);
        Neighborhood neighborsOfv = adjacencyList.get(v);
        Collection<Arc> outArcs = neighborsOfu.getOutArcs();
        Collection<Arc> inArcs = neighborsOfv.getInArcs();

        boolean flag = false;
        for (Arc e : outArcs) {
            if (e.head() == v) {
                outArcs.remove(e);
                flag = true;
            }
        }

        for (Arc e : inArcs) {
            if (e.tail() == u) {
                inArcs.remove(e);
                flag = true;
            }
        }

        if (flag == false) {
            throw new IllegalArgumentException("Not an arc:" + u + " -> " + v);
        }

    }

    public void deleteEdge(Edge e) {
        deleteArc(e.getV1(), e.getV2());
    }

    @Override
    public void deleteEdge(int v1, int v2) {
        deleteArc(v1, v2);
    }

    @Override
    public int size() {
        return adjacencyList.size();
    }

    @Override
    public int getNumberOfEdges() {
        int count = 0;
        for (Integer v : adjacencyList.keySet()) {
            count += adjacencyList.get(v).numOutArcs();
        }
        return count;
    }

    public Collection<Integer> getVertexSet() {
        Collection<Integer> vertexSet = new HashSet<Integer>(this.size());
        for (Integer v : adjacencyList.keySet()) {
            vertexSet.add(v);
        }

        return vertexSet;
    }

    @Override
    public Collection<Integer> getNeighborhood(int v) {
        return getOutNeighbors(v);
    }

    public Collection<Integer> getOutNeighbors(int u) {
        if (!isVertex(u)) {
            throw new IllegalArgumentException("Not a vertex: " + u);
        }

        return adjacencyList.get(u).getOutNeighbors();
    }

    public Collection<Integer> getInNeighbors(int u) {
        if (!isVertex(u)) {
            throw new IllegalArgumentException("Not a vertex: " + u);
        }

        return adjacencyList.get(u).getInNeighbors();
    }

    @Override
    public int getDegree(int v) {
        if (!isVertex(v)) {
            throw new IllegalArgumentException("Not a vertex: " + v);
        }
        return adjacencyList.get(v).numOutArcs();
    }

    @Override
    public Graph getInducedSubgraph(Collection<Integer> vertexSet) {
        Digraph d = new Digraph();

        for (Integer v : vertexSet) {
            if (!isVertex(v)) {
                throw new IllegalArgumentException("Not a vertex: " + v);
            }

            Neighborhood nbrGraph = adjacencyList.get(v);
            Neighborhood nbrInduced = new Neighborhood();
            d.adjacencyList.put(v, nbrInduced);

            for (Integer u : nbrGraph.getInNeighbors()) {
                if (vertexSet.contains(u)) {
                    Arc e = new Arc(u, v);
                    d.adjacencyList.get(v).addInArc(e);
                }
            }

            for (Integer w : nbrGraph.getOutNeighbors()) {
                if (vertexSet.contains(w)) {
                    Arc e = new Arc(v, w);
                    d.adjacencyList.get(v).addOutArc(e);
                }
            }

        }

        return d;
    }

    @Override
    public Graph getSubgraph(Collection<Integer> vertexSet, Collection<Edge> edgeSet) {
        Digraph subDigraph = new Digraph();

        for (Integer v : vertexSet) {
            if (!isVertex(v)) {
                throw new IllegalArgumentException("Not a vertex: " + v);
            }

            Neighborhood nbr = new Neighborhood();
            subDigraph.adjacencyList.put(v, nbr);
        }

        for (Edge e : edgeSet) {
            if (!isArc((Arc) e)) {
                throw new IllegalArgumentException("Not an arc: " + e);
            }

            int tail = ((Arc) e).tail();
            int head = ((Arc) e).head();
            subDigraph.adjacencyList.get(tail).addOutArc((Arc) e);
            subDigraph.adjacencyList.get(head).addInArc((Arc) e);
        }

        return subDigraph;
    }

    /**
     * This function checks whether the given digraph is connected. If strongly
     * == true, then it checks for strong connectivity, else it checks for weak
     * connectivity.
     *
     * @param strongly
     * @return
     */
    @Override
    public boolean isConnected(boolean strongly) {
        if (strongly) {
            DirectedDFS reach = new DirectedDFS(this);
            return reach.isStronglyConnected();
        }

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

            Neighborhood incidentArcs = adjacencyList.get(next);
            Collection<Integer> outNbrs = incidentArcs.getOutNeighbors();
            Collection<Integer> inNbrs = incidentArcs.getInNeighbors();

            for (Integer nbr : outNbrs) {
                if (!visited.contains(nbr)) {
                    queue.add(nbr);
                    visited.add(nbr);
                }
            }

            for (Integer nbr : inNbrs) {
                if (!visited.contains(nbr)) {
                    queue.add(nbr);
                    visited.add(nbr);
                }
            }
        }
        return visited.size() == adjacencyList.size();

    }

    public String toString() {
        String graph = "";
        for (Integer v : adjacencyList.keySet()) {
            graph += "Vertex: " + v + "\n";
            Neighborhood nbr = adjacencyList.get(v);
            graph += "In arcs: \n";
            for (Arc e : nbr.getInArcs()) {
                graph += e.toString() + ", ";
            }

            graph += "\n" + "Out Arcs: \n";
            for (Arc e : nbr.getOutArcs()) {
                graph += e.toString() + ", ";
            }

            graph += "\n";
        }

        return graph;
    }

    public static void main(String[] args) {
        Digraph d = new Digraph();
        d.addVertex(1);
        d.addVertex(2);
        d.addArc(1, 2);
        d.addVertex(3);
        d.addArc(2, 3);
        d.addVertex(4);
        d.addArc(3, 4, 5);
        d.addArc(4, 1);
        d.addArc(1, 3);
        d.addVertex(5);
        d.addArc(3, 5);
        //d.addArc(5, 2);
        //System.out.println(d.toString());
        //d.deleteArc(3, 4);

        Collection<Integer> vSet = new HashSet<Integer>(3);
        vSet.add(2);
        vSet.add(3);
        vSet.add(5);
        System.out.println(d.isConnected(false));
        System.out.println(d.isConnected(true));

        //Digraph d1 = (Digraph) d.getInducedSubgraph(vSet);
        //System.out.println(d1.toString());
    }

}
