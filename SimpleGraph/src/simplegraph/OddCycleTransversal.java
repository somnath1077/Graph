/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplegraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

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
     * either when the graph is bipartite or when the graph is empty.
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
        // NOTE: 2 <= k + 1 <= graph.size()
        Iterator<Integer> iter = graph.getVertexSet().iterator();
        while (iter.hasNext() && currVertexSet.size() != k + 1) {
            currVertexSet.add(iter.next());
        }

        // Create a simple graph that is the graph induced 
        // by the k + 1 vertices in the currVertexSet
        OddCycleTransversal oct = createOCT(currVertexSet);
        Collection<Integer> solution = oct.compressionOCT(currVertexSet, k);
        // if the solution is null, meaning that this induced subgraph has no 
        // k-sized OCT, then the whole graph cannot have a k-sized OCT
        if (solution == null) {
            return null;
        }

        Integer vert = null;
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
                    if (!g.isVertex(w)) {
                        g.addVertex(w);
                    }
                    g.addEdge(u, w);
                }
            }
        }

        return new OddCycleTransversal(g);
    }

    /**
     * The compression OCT method takes a reference to an object of type
     * OddCycleTransversal, a vertex set which is an odd cycle transversal for
     * the simple graph and a parameter k. The size of the solution is at most k
     * + 1. It checks whether the graph has an OCT of size at most k. If so, it
     * returns the solution; else, it returns null.
     *
     * @param vertices
     * @param sol
     * @param k
     * @return a solution of size at most k, if it exists; else null.
     */
    private Collection<Integer> compressionOCT(Collection<Integer> solution, int k) {
        if (solution.size() <= k) {
            return solution;
        }

        // First partition the vertices of the graph \ solution vertices 
        // into sets A and B such that A and B are independent.
        // To do this first copy the graph into currForest and remove 
        // all solution vertices from it. 
        SimpleGraph currForest = this.graph.copy();
        for (Integer u : solution) {
            currForest.deleteVertex(u);
        }

        // currForest is a forest and is hence two-colorable
        Map<Integer, String> partition = currForest.partitionTwoColors();
        Collection<Integer> setA = new HashSet<>();
        Collection<Integer> setB = new HashSet<>();
        for (Integer u : partition.keySet()) {
            if (partition.get(u) == "red") {
                setA.add(u);
            } else {
                setB.add(u);
            }
        }

        // Consider all possible partitions of solution into sets L, R, and T
        // such that |T| <= k. At this point, solution has size k + 1
        // and 2 <= k + 1 <= graph.size() 
        // The strategy used is to count from 0 till 3^{k + 1} - 2;
        // for each value of count, we convert count to ternary and 
        // use this representation for the partition into L, R and T.
        // If a bit is 0, then it is in L; if it 1 then it is in R;
        // otherwise it is in T.
        // We count till 3^{k + 1} - 2, because the all 2-string does
        // not represent a valid partition
        long maxCount = (long) Math.pow(3, k + 1) - 2;
        int[] vertArr = new int[solution.size()];
        Iterator<Integer> iter = solution.iterator();
        Collection<Integer> setL = new HashSet<>();
        Collection<Integer> setR = new HashSet<>();
        Collection<Integer> setT = new HashSet<>();

        for (int i = 0; i < solution.size(); ++i) {
            vertArr[i] = iter.next();
        }

        for (long i = 0; i <= maxCount; ++i) {
            // convert i into ternary
            ArrayList<Integer> ternary = getTernary(i);
            // Use the ternary representation of i to 
            // get the next partition of solution into 
            // L, R and T
            for (int j = 0; j < ternary.size(); ++j) {
                if (ternary.get(j) == 0) {
                    setL.add(vertArr[j]);
                } else if (ternary.get(j) == 1) {
                    setR.add(vertArr[j]);
                } else {
                    setT.add(vertArr[j]);
                }
            }
        }

        // For each partition of the solution set into L, R, and T find out 
        // A_L and A_R; B_L and B_R
        // Construct an auxilliary graph from A_L, A_R, B_L, B_R and s and t;
        // by connecting s to A_L and B_R; and t to A_R and B_L.
        // Check if there exists an s-t separator S' of size at most k - |T|. 
        // If yes, S' union T is the desired solution
        // If for all partitions into L, R and T there is no s-t separator 
        // of the desired size, return null (there is no solution!)
        return solution;
    }

    private ArrayList<Integer> getTernary(long num) {
        ArrayList<Integer> ternary = new ArrayList<Integer>();
        long quotient = num;
        if (quotient == 0) {
            ternary.add(0);
            return ternary;
        }

        Integer remainder = null;
        while (quotient != 0) {
            long temp = quotient / 3;
            remainder = (int) quotient % 3;
            quotient = temp;
            ternary.add(remainder);
        }

        return ternary;

    }
}
