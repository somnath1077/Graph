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
import java.util.HashMap;

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
     * computes the smallest odd cycle transversal of the graph.
     *
     * @return a collection of vertices that is a smallest odd cycle
     * transversal, null if the graph is bipartite or empty.
     *
     */
    public Collection<Integer> minOddCycleTransversal() {
        if (graph.size() == 0) {
            System.out.println("The graph is empty!");
            return new HashSet<>();
        }

        if (graph.isTwoColorable()) {
            System.out.println("The graph is bipartite!");
            return new HashSet<>();
        }

        int k = 1;
        while (minOCT(k) == null) {
            ++k;
        }

        return minOCT(k);
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
        Collection<Integer> currVertexSet = new HashSet<>();
        // First create a graph with k + 1 vertices
        // This graph trivially has an OCT of size k + 1
        // and will be the the initial parameters for the 
        // compression algorithm. 
        Iterator<Integer> iter = graph.getVertexSet().iterator();
        while (iter.hasNext() && currVertexSet.size() != k) {
            currVertexSet.add(iter.next());
        }

        Collection<Integer> solution = new HashSet<>(currVertexSet);
        while (iter.hasNext()) {
            int vert = iter.next();
            // add the next vertex from the graph to the current vertex set
            // add it to the solution too, so that the solution is an OCT 
            // for the new graph too.
            currVertexSet.add(vert);
            solution.add(vert);

            System.out.println("About to call createOCT");
            OddCycleTransversal oct = createOCT(currVertexSet);
            System.out.println("This is before the compression routine is called!");
            solution = oct.compressionOCT(solution, k);
            System.out.println("After compression routine");
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
        SimpleGraph g = this.graph.getInducedSubgraph(currVertexSet);
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
        System.out.println("The value of k is: " + k);
        System.out.println("creating bipartite graph...");
        // First partition the vertices of the graph \ solution vertices 
        // into sets A and B such that A and B are independent.
        // To do this first copy the graph into bipartite and remove 
        // all solution vertices from it. 
        SimpleGraph bipartite = this.graph.copy();
        for (Integer u : solution) {
            bipartite.deleteVertex(u);
        }
        
        System.out.println("finished creating bipartite graph...");

        // bipartite is a bipartite graph and is hence two-colorable
        Map<Integer, SimpleGraph.VertexColor> partition = bipartite.partitionTwoColors();
        Collection<Integer> setA = new HashSet<>();
        Collection<Integer> setB = new HashSet<>();
        for (Integer u : partition.keySet()) {
            if (partition.get(u) == SimpleGraph.VertexColor.RED) {
                setA.add(u);
            } else {
                setB.add(u);
            }
        }
        
        System.out.println("finished partitioning bipartite graph...");
        // Consider all possible partitions of solution into sets L, R, and T
        // such that |T| <= k. At this point, solution has size k + 1
        // and 2 <= k + 1 <= graph.size() 
        // The strategy used is to count from 0 till 3^{k + 1} - 2;
        // for each value of count, we convert count to ternary and 
        // use this representation for the partition into L, R and T.
        // If a bit is 0, then it is in L; if it 1 then it is in R;
        // otherwise it is in T.
        // We count till 3^{k + 1} - 2, because the all 2-vector does
        // not represent a valid partition (|T| <= k and the all 2-vector
        // represents a partition that places everything in the set T).
        long maxCount = 3;
        for (int i = 1; i <= k; ++i) {
            maxCount *= 3; // not checking for overflows!
        }
        // maxCount = 3^{k+1}
        maxCount = maxCount - 2;
        int[] vertArr = new int[solution.size()];
        Iterator<Integer> iter = solution.iterator();
        Collection<Integer> setL = new HashSet<>();
        Collection<Integer> setR = new HashSet<>();
        Collection<Integer> setT = new HashSet<>();

        for (int i = 0; i < solution.size(); ++i) {
            vertArr[i] = iter.next();
        }
        
        System.out.println("finished placing the solution vertices in array...");

        // Here begins the long loop that examines all possible 
        // partitions of the solution set
        for (long i = 0; i <= maxCount; ++i) {
            // convert i into ternary
            ArrayList<Integer> ternary = getTernary(i);
            System.out.println("calculated ternary representation of " + i);
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
            
            System.out.println("finished partitioning into setL, setR and setT...");
            
            // check whether G[L] and G[R] are independent
            // if not, we can move on to the next partition
            if (!this.graph.isIndependent((HashSet<Integer>) setL)
                    || !this.graph.isIndependent((HashSet<Integer>) setR)) {
                continue;
            }
            
            System.out.println("checked whether setL and setR are independent...");
            // For each partition of the solution set into L, R, and T find out 
            // A_L and A_R; B_L and B_R
            // Recall that A_L is the set of neighbors of L in the set A
            Collection<Integer> setAl = this.graph.findNeighbors(setL, setA);
            Collection<Integer> setAr = this.graph.findNeighbors(setR, setA);
            Collection<Integer> setBl = this.graph.findNeighbors(setL, setB);
            Collection<Integer> setBr = this.graph.findNeighbors(setR, setB);

            // Construct an auxilliary graph from A_L, A_R, B_L, B_R and s and t;
            // by connecting s to A_L and B_R; and t to A_R and B_L.
            Pair<SimpleGraph, Pair<Integer, Integer>> aux = constructAuxilliary(setAl, setAr, setBl, setBr);
            System.out.println("created auxilliary graph...");

            // Check if there exists an s-t separator S' of size at most k - |T|. 
            // If yes, S' union T is the desired solution.
            Integer source = aux.getSecond().getFirst();
            Integer sink = aux.getSecond().getSecond();

            // Next create the Map for arc capacities so that the EdmondsKarp 
            // implementation of max flow can be used.
            Map<Pair<Integer, Integer>, Integer> arcCapacities = new HashMap<>();
            for (int v : this.graph.getVertexSet()) {
                for (int u : this.graph.getNeighborhood(v)) {
                    arcCapacities.put(new Pair(u, v), 1);
                    arcCapacities.put(new Pair(v, u), 1);
                }
            }
            
            System.out.println("created arc capacities...");
            EdmondsKarpMaxFlow flowRoutine = new EdmondsKarpMaxFlow(aux.getFirst(), arcCapacities,
                    source, sink);
            if (flowRoutine.computeFlow() > k - setT.size()) {
                continue;
            }
            
            System.out.println("max flow within bounds!!");
            
            ArrayList<Edge> cut = flowRoutine.getCut();
            Collection<Integer> separator = new HashSet<>();
            for (Edge e : cut) {
                separator.add(e.getV1());
                separator.add(e.getV2());
            }
            separator.addAll(setT);
            return separator;
        } // Here ends the loop which examines all possible partitions of the solution set

        // If for all partitions into L, R and T there is no s-t separator 
        // of the desired size, return null (there is no solution!)
        return null;
    }

    private ArrayList<Integer> getTernary(long num) {
        ArrayList<Integer> ternary = new ArrayList<>();
        long quotient = num;
        if (quotient == 0) {
            ternary.add(0);
            return ternary;
        }

       
        while (quotient != 0) {
            long temp = quotient / 3;
            int remainder = (int) (quotient % 3);
            quotient = temp;
            ternary.add(remainder);
        }

        return ternary;

    }

    private Pair<SimpleGraph, Pair<Integer, Integer>>
            constructAuxilliary(Collection<Integer> setAl, Collection<Integer> setAr,
                    Collection<Integer> setBl, Collection<Integer> setBr) {
        
        Collection<Integer> vertices = new HashSet<>();

        // First construct the union of these sets
        vertices.addAll((HashSet<Integer>) setAl);
        vertices.addAll((HashSet<Integer>) setAr);
        vertices.addAll((HashSet<Integer>) setBl);
        vertices.addAll((HashSet<Integer>) setAr);

        SimpleGraph auxilliary = this.graph.getInducedSubgraph(vertices);

        // We still need to add the source and sink vertices.
        // First find unique representations for source and sink
        int maxLabel = this.graph.size() + 2; // we are guaranteed two empty indices 
        Integer source = null;
        Integer sink = null;
        for (int i = 0; i < maxLabel; ++i) {
            if (this.graph.isVertex(i)) {
                continue;
            }
            if (source == null) {
                source = i;
            } else if (sink == null) {
                sink = i;
                break;
            }
        }

        // Add the source and sink
        auxilliary.addVertex(source);
        auxilliary.addVertex(sink);

        // Add edges from the source to Al and Br
        Collection<Integer> sourceConnections = new HashSet<>();
        sourceConnections.addAll((HashSet<Integer>) setAl);
        sourceConnections.addAll((HashSet<Integer>) setBr);
        for (int v : sourceConnections) {
            auxilliary.addEdge(source, v);
        }
        // Add edges from sink to Ar and Bl
        Collection<Integer> sinkConnections = new HashSet<>();
        sourceConnections.addAll((HashSet<Integer>) setAr);
        sourceConnections.addAll((HashSet<Integer>) setBl);
        for (int v : sinkConnections) {
            auxilliary.addEdge(sink, v);
        }

        return new Pair<>(auxilliary, new Pair<>(source, sink));
    }
}
