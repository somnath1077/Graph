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
    private Collection<Integer> maxDegTwoIS() throws CloneNotSupportedException {
        if (this.graph.size() == 0) {
            return null;
        }

        SimpleGraph localGraph;
        try {
            localGraph = this.graph.clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneNotSupportedException("Clone not supported");
        }

        IndependentSet localCopy = new IndependentSet(localGraph);
        Collection<Integer> localSol = new HashSet<>();

        while (localCopy.graph.size() != 0) {
            Integer v = localCopy.graph.getLowestDegreeVertex();
            localSol.add(v);
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
        Collection<Integer> solution;
        try{
        solution = maxIS(null, false);
        } catch (CloneNotSupportedException e) {
            return null;
        }
        
        return solution;
    }

    /*
     * The maxIS() function takes in a vertex and a boolean status flag 
     * and returns a largest independent set containing the vertex (if status == true)
     * or a largest independent set not containing the vertex (if status == false). 
     * The function makes a local copy of the graph and checks if the maximum 
     * degree is at most two, in whihc case, calls the maxDegTwoIS() method
     * to solve the problem in polynomial time. Otherwise, it branches on a 
     * vertex of largest degree. 
    */
    private Collection<Integer> maxIS(Integer v, boolean status) throws CloneNotSupportedException {

        // first create a local copy of the graph
        // which can be modified safely
        SimpleGraph localGraph;
        try {
            localGraph = this.graph.clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneNotSupportedException("Clone not supported");
        }

        IndependentSet localCopy = new IndependentSet(localGraph);
        Collection<Integer> localSol = new HashSet<Integer>();

        // First modify local copy depending on whether the passed-in 
        // vertex is in the independent set
        if (v != null) {
            if (status == true) {   // vertex v is in the independent set
                localSol.add(v);
                Collection<Integer> nbr = localCopy.graph.getNeighborhood(v);
                localCopy.graph.deleteVertex(v);
                for (Integer u : nbr) {
                    localCopy.graph.deleteVertex(u);
                }
            } else {  // v is not in the independent set
                localCopy.graph.deleteVertex(v);
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
            localSol.addAll(localCopy.maxDegTwoIS());
            return localSol;
        }

        // Otherwise, the graph has a vertex of degree at least three
        // Find the vertex of highest degree.
        Integer maxDegV = localCopy.graph.getHighestDegreeVertex();
        // First consider the case when the maxDegV is in the solution 
        Collection<Integer> partialSolIn = localCopy.maxIS(maxDegV, true);

        // Next consider the case when maxDegV is not in the solution
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
