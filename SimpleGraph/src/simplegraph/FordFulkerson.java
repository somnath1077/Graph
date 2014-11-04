/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplegraph;

/**
 *
 * @author somnath
 */
public class FordFulkerson {

    private final SimpleGraph graph;
    private final int source, sink;
    private final int cutoff;

    public FordFulkerson(SimpleGraph g, int source, int sink) {
        this(g, source, sink, g.getNumberOfEdges());
    }

    public FordFulkerson(SimpleGraph g, int source, int sink, int cutoff) {
        this.graph = g;
        this.source = source;
        this.sink = sink;
        this.cutoff = cutoff;
    }
    
    
}
