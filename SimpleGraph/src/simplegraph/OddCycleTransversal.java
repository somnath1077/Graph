/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplegraph;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author sikdar
 */
public class OddCycleTransversal {

    private final SimpleGraph graph;

    public OddCycleTransversal(SimpleGraph graph) {
        this.graph = graph;
    }

    /**
     * The minOddCycleTransversal method takes a reference to a SimpleGraph and
     * computes the smallest odd cycle transversal of the graph. It returns null
     * either when the graph is bipartite or when it is empty.
     *
     * @return a collection of vertices that is a smallest odd cycle
     * transversal, null if the graph is bipartite or empty.
     *
     */
    public Collection<Integer> minOddCycleTransversal() {
        if (graph.size() == 0) {
            System.out.println("The graph is empty!");
            return null;
        }

        if (graph.isTwoColorable()) {
            System.out.println("The graph is bipartite!");
            return null;
        }

        int numVertices = graph.size();
        // the parameter k varies from 1 to the number of vertices 
        for (int k = 1; k <= numVertices; ++k) {

            if (minOCT(k) != null) {
                return minOCT(k);
            }
        }

        // This line is NEVER reached! In the worst case, k is the size 
        // of the vertex set in which case, the solution is the vertex set itself! 
        // Added it because the IDE is complaining.
        return null;
    }

    /**
     * The minOCT takes a reference to a simple graph and a parameter k and
     * checks whether the graph has an odd cycle transversal of size k.
     *
     * @param k
     * @return an odd cycle transversal of size k, if one exists; it returns
     * null if no such solution exists.
     */
    private Collection<Integer> minOCT(int k) {
        if (k == graph.size()) {
            return graph.getVertexSet();
        }

        Collection<Integer> currVertexSet = null;
        // First create a graph with k + 1 vertices
        // This graph trivially has an OCT of size k + 1
        // and will be the the initial parameters for the 
        // compression algorithm
        Iterator<Integer> iter = graph.getVertexSet().iterator();
        Integer vert = null;
        while (iter.hasNext() && currVertexSet.size() != k + 1) {
            vert = iter.next();
            currVertexSet.add(vert);
        }

        // Create a simple graph that is the graph induced 
        // by the k + 1 vertices in the currVertexSet
        OddCycleTransversal oct = createOCT(currVertexSet);
        Collection<Integer> solution = oct.compressionOCT(currVertexSet, k);
        if (solution == null) {
            return null;
        }

        while (oct.graph.size() != this.graph.size()) {
            if (iter.hasNext()) {
                vert = iter.next();
                // add the next vertex from the graph to the current vertex set
                // add it to the solution too, so that the solution is an OCT 
                // for the new graph too.
                currVertexSet.add(vert);
                solution.add(vert);
            }

            oct = createOCT(currVertexSet);
            solution = oct.compressionOCT(solution, k);
            if (solution == null) {
                return null;
            }
        }

        return solution;
    }

    /**
     * This method creates an object of type OddCycleTransversal from a set of
     * vertices. It first creates a SimpleGraph induced on this set of vertices
     * and then initializes an OCT object from it. This is purely a helper
     * method.
     *
     * @param currVertexSet
     * @return
     */
    private OddCycleTransversal createOCT(Collection<Integer> currVertexSet) {
        SimpleGraph g = new SimpleGraph();
        for (Integer u : currVertexSet) {
            g.addVertex(u);

            for (Integer w : this.graph.getNeighborhood(u)) {
                if (currVertexSet.contains(w)) {
                    g.addVertex(w);
                    g.addEdge(u, w);
                }
            }
        }

        return new OddCycleTransversal(g);
    }

    /**
     * The compression OCT method takes a reference to a SimpleGraph, a vertex
     * set which is an odd cycle transversal for the simple graph and a
     * parameter k. The size of the solution sol is at most k + 1. It checks
     * whether the graph has an OCT of size at most k. If so, it returns the
     * solution; else, it returns null.
     *
     * @param vertices
     * @param sol
     * @param k
     * @return a solution of size at most k, if it exists; else null.
     */
    private Collection<Integer> compressionOCT(Collection<Integer> sol, int k) {

    }

}
