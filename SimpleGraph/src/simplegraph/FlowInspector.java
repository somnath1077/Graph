/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplegraph;

/**
 * This code was borrowed from package no.uib.ii.algo.st8.algorithms and
 * modified to fit the needs of this application.
 *
 * @author sikdar
 */
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FlowInspector {

    /**
     * Given a graph, a source and a target vertex the function computes the
     * flow and returns the size of the flow and a set of edges demonstrating
     * the flow in the graph.
     *
     * @param graph A simple graph
     * @param source The source
     * @param target The target
     * @return The flow from source to target and a set of edges demonstrating
     * the flow
     */
    public static Pair<Integer, Collection<Edge>> findFlow(
            SimpleGraph graph, Integer source, Integer target) {
        Set<Pair<Integer, Integer>> flowEdges = new HashSet<Pair<Integer, Integer>>();

        // Compute flow
        int flow = 0;
        while (flowIncreasingPath(graph, flowEdges, source, target)) {
            ++flow;
        }

        // Retrieve edges with flow
        Set<Edge> edges = new HashSet<Edge>();
        for (Pair<Integer, Integer> p : flowEdges) {
            if (graph.isEdge(p.getFirst(), p.getSecond())) {
                Edge e = new UndirectedEdge(p.getFirst(), p.getSecond());
                edges.add(e);
            }

        }
        return new Pair<Integer, Collection<Edge>>(flow, edges);
    }

    /**
     * Finds a flow increasing path in the graph from source to target avoiding
     * the directed edges in flowEdges.
     *
     * @param graph The graph
     * @param flowEdges The directed edges that should not be used in the flow
     * increasing path
     * @param source The source
     * @param target The target
     * @return True if a flow increasing path exist, false otherwise.
     */
    private static boolean flowIncreasingPath(SimpleGraph graph,
            Set<Pair<Integer, Integer>> flowEdges, Integer source, Integer target) {
        Map<Integer, Integer> prev = new HashMap<Integer, Integer>();
        Queue<Integer> next = new LinkedList<Integer>();

        // Initialise search
        next.add(source);
        prev.put(source, null);

        // Search the graph
        while (!next.isEmpty()) {
            Integer v = next.poll();

            if (v == target) {
                break;
            }

            // Look at the neighbourhood
            for (Integer nbr : graph.getNeighborhood(v)) {
                // If the edge is not in flowEdges and the neighbour is not
                // already visited we want to search the neighbour
                if (!flowEdges.contains(new Pair<Integer, Integer>(v, nbr))
                        && !prev.containsKey(nbr)) {
                    next.add(nbr);
                    prev.put(nbr, v);
                }
            }
        }

        // No path found
        if (!prev.containsKey(target)) {
            return false;
        }

        // Updates flowEdges according to the path found
        Integer v = target;
        while (v != source) {
            flowEdges.add(new Pair<Integer, Integer>(prev.get(v), v));
            flowEdges.add(new Pair<Integer, Integer>(v, prev.get(v)));
            v = prev.get(v);
        }

        return true;
    }
}
