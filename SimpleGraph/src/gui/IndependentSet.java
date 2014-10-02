/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author sikdar
 */
public class IndependentSet {
    private final DrawableGraph graph;
    
    public IndependentSet(DrawableGraph graph) {
        this.graph = graph;
    }
    
    // the max degree two IS casn be solved in poly time
    // start by picking a arbitrary vertex of lowest degree 
    // in the solution and deleet all its neighbors. 
    private Collection<Integer> maxDegTwoIS() {
        Collection<Integer> Sol = new HashSet<>();
        for (Integer u : )
        return Sol;
    }
    
    public Collection<Integer> maxIS() {
        Collection<Integer> Sol = new HashSet<>();
        
    // first collect isolated vertices in the solution
    for (Integer u : graph.getVertexSet()) {
        if (graph.getDegree(u) == 0) {
            Sol.add(u);
        } 
    }    
    
    // next check if the graph has max degree two
    // this is poly-time solvable;
    if (graph.getMaxDegree() == 2) {
        Sol.addAll(maxDegTwoIS());
        return Sol;
    }    
        return Sol;
    }
    
    
}
