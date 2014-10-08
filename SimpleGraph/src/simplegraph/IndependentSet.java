/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplegraph;

import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author sikdar
 */
public class IndependentSet {

    private final SimpleGraph graph;

    public IndependentSet(SimpleGraph graph) {
        this.graph = graph;
    }

    // the max degree two IS can be solved in poly time
    // start by repeatedly picking an arbitrary vertex of 
    // lowest degree in the solution and deleting all 
    // its neighbors. 
    private Collection<Integer> maxDegTwoIS() {
        if (this.graph.size() == 0) {
            return null;
        }

        SimpleGraph localGraph;
        try {
            localGraph = this.graph.clone();
            System.out.println("In maxTwoDeg() cloned successfully. cloned graph: " + localGraph.toString());
        } catch (CloneNotSupportedException e) {
            return null;
        }

        IndependentSet localCopy = new IndependentSet(localGraph);
        Collection<Integer> localSol = new HashSet<>();

        while (localCopy.graph.size() != 0) {
            System.out.println("In maxTwoIS: the local copy looks like: " + localCopy.graph.toString());
            Integer v = localCopy.graph.getLowestDegreeVertex();
            localSol.add(v);

            System.out.println("In maxTwoIS added vertex: " + v);
            Collection<Integer> nbr = localCopy.graph.getNeighborhood(v);

            localCopy.graph.deleteVertex(v);
            if (nbr != null) {
                for (Integer u : nbr) {
                    localCopy.graph.deleteVertex(u);
                }
            }
        }
        return localSol;
    }

    public Collection<Integer> maxIS() {
        return maxIS(null, false);
    }

    public Collection<Integer> maxIS(Integer v, boolean status) {

        // first create a local copy of the graph
        // which can be modified safely
        SimpleGraph localGraph;
        try {
            localGraph = this.graph.clone();
            System.out.println("Cloning in maxIS() successful. ");
        } catch (CloneNotSupportedException e) {
            return null;
        }

        IndependentSet localCopy = new IndependentSet(localGraph);

        Collection<Integer> localSol = new HashSet<Integer>();
        System.out.println("In maxIS(): cloned local graph: " + localCopy.graph.toString());

        // First modify local copy depending on whether the passed-in 
        // vertex is in the independent set
        if (v != null) {
            if (status == true) {   // vertex v is in the independent set
                localSol.add(v);
                System.out.println("added vertex: " + v);
                Collection<Integer> nbr = localCopy.graph.getNeighborhood(v);
                localCopy.graph.deleteVertex(v);
                for (Integer u : nbr) {
                    localCopy.graph.deleteVertex(u);
                }
                System.out.println("Vertex " + v + " in solution. Modified graph: " + localCopy.graph.toString());
            } else {  // v is not in the independent set
                localCopy.graph.deleteVertex(v);
                System.out.println("Vertex " + v + " not in solution. Modifed graph: " + localCopy.graph.toString());
            }
        }

        // Check if graph is empty
        if (localCopy.graph.size() == 0) {
            return localSol;
        }

        // Collect isolated vertices in the solution
        for (Integer u : localCopy.graph.getVertexSet()) {
            if (localCopy.graph.getDegree(u) == 0) {
                localSol.add(u);
                localCopy.graph.deleteVertex(u);
            }
        }

        // if the graph becomes empty in the process
        if (localCopy.graph.size() == 0) {
            return localSol;
        }

        // next check if the graph has max degree two
        // this is poly-time solvable;
        if (localCopy.graph.getMaxDegree() <= 2) {
            System.out.println("calling maxDegTwoIs: max degree = " + localCopy.graph.getMaxDegree());
            System.out.println("the graph at this point is: " + localCopy.graph.toString());
            localSol.addAll(localCopy.maxDegTwoIS());
            return localSol;
        }

        // Otherwise, the graph has a vertex of degree at least three
        // Find the vertex of highest degree.
        Integer maxDegV = localCopy.graph.getHighestDegreeVertex();
        // First consider the case when the maxDegV is in the solution 
        System.out.println("calling maxIS() with " + maxDegV + " as part of solution");
        Collection<Integer> partialSolIn = localCopy.maxIS(maxDegV, true);

        // Next consider the case when maxDegV is not in the solution
        System.out.println("calling maxIS() with " + maxDegV + " not in solution");
        Collection<Integer> partialSolOut = localCopy.maxIS(maxDegV, false);

        if (partialSolIn.size() > partialSolOut.size()) {
            localSol.addAll(partialSolIn);
        } else {
            localSol.addAll(partialSolOut);
        }

        // modify 
        return localSol;
    }

    public static void main(String[] args) {
        SimpleGraph graph = new SimpleGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
        graph.addEdge(2, 5);

        IndependentSet is = new IndependentSet(graph);
        Collection<Integer> sol = is.maxIS();
        System.out.println("The independent set consists of the following vertices: ");
        for (Integer u : sol) {
            System.out.println(u);
        }
    }
}
