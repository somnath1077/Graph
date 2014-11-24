/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplegraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * An implementation of Edmonds Karp running in both O(f * (n + m)) and O(nm^2),
 * where f, n and m are the flow found, the number of vertices and the number of
 * edges respectively. It also supports finding minimum edge cuts. In addition
 * you can provide a cutoff and if the flow exceeds this threshold it will
 * return a flow being at least this threshold.
 *
 * @author Markus Dregi
 */
public class EdmondsKarpMaxFlow {

    private final SimpleGraph graph;
    private final Map<Pair<Integer, Integer>, Integer> arcCapacities;
    private final int source, sink;
    private final int cutoff;
    private Map<Pair<Integer, Integer>, Integer> arcFlows;
    private int flow;

    /**
     * Constructor with cutoff.
     *
     * @param g
     * @param arcCapacities
     * @param source
     * @param sink
     * @param cutoff
     */
    public EdmondsKarpMaxFlow(SimpleGraph g,
            Map<Pair<Integer, Integer>, Integer> arcCapacities,
            int source, int sink, int cutoff) {
        this.graph = g;
        this.source = source;
        this.sink = sink;
        this.cutoff = cutoff;
        this.arcCapacities = arcCapacities;
    }

    /**
     * Constructor.
     *
     * @param g
     * @param arcCapacities
     * @param source
     * @param sink
     */
    public EdmondsKarpMaxFlow(SimpleGraph g,
            Map<Pair<Integer, Integer>, Integer> arcCapacities,
            int source, int sink) {
        this(g, arcCapacities, source, sink, Integer.MAX_VALUE);
    }

    /**
     * Computes the flow.
     *
     * @return the flow
     */
    @SuppressWarnings("empty-statement")
    public int computeFlow() {
        arcFlows = new HashMap<>();
        for (Pair<Integer, Integer> dirEdge : arcCapacities.keySet()) {
            arcFlows.put(dirEdge, 0);
        }
        System.out.println("initialized arc flows...");

        flow = 0;
        while (augmentFlow() && flow <= cutoff);

        return flow;
    }

    // Find augmenting path from source to sink
    private boolean augmentFlow() {
        System.out.println("finding augmenting path...");
        ArrayList<Integer> path = findAugmentingPath();

        System.out.println("computing new flow along augmenting path...");
        int newFlow = Integer.MAX_VALUE;
        for (int i = 0; i + 1 < path.size(); ++i) {
            Pair<Integer, Integer> edge = new Pair(path.get(i), path.get(i + 1));
            newFlow = Math.min(newFlow, arcCapacities.get(edge) - arcFlows.get(edge));
        }
        System.out.println("found new flow. updating flow...");
        if (newFlow < Integer.MAX_VALUE) {
            updateFlow(path, newFlow);
            flow += newFlow;
        }

        return newFlow > 0;
    }

    // Finds an augmenting path
    private ArrayList<Integer> findAugmentingPath() {
        System.out.println("in the findAugmentingPath method...");
        Map<Integer, Integer> prev = new HashMap<>();
        Queue<Integer> next = new LinkedList<>();

        next.add(source);
        prev.put(source, null);

        while (!next.isEmpty()) {
            int pos = next.poll();

            for (int nbr : graph.getNeighborhood(pos)) {
                Pair<Integer, Integer> edge = new Pair(pos, nbr);
                if (prev.containsKey(nbr)
                        || arcCapacities.get(edge) - arcFlows.get(edge) <= 0) {
                    continue;
                }

                prev.put(nbr, pos);
                next.add(nbr);
            }
        }

        if (!prev.containsKey(sink)) {
            return new ArrayList<>();
        }

        ArrayList<Integer> path = new ArrayList<>();
        path.add(sink);
        int pos = sink;
        while (prev.get(pos) != null) {
            pos = prev.get(pos);
            path.add(pos);
        }

        Collections.reverse(path);
        return path;
    }

    // Update the flows
    private void updateFlow(ArrayList<Integer> path, int flowIncrease) {
        for (int i = 0; i + 1 < path.size(); ++i) {
            Pair<Integer, Integer> edge = new Pair<>(path.get(i), path.get(i + 1));
            Pair<Integer, Integer> revEdge = new Pair<>(path.get(i + 1), path.get(i));
            arcFlows.put(edge, arcFlows.get(edge) + flowIncrease);
            arcFlows.put(revEdge, arcFlows.get(revEdge) - flowIncrease);
        }
    }

    /**
     * Returns a minimum edge cut.
     *
     * @return a cut
     */
    public ArrayList<Edge> getCut() {
        if (arcFlows == null) {
            throw new IllegalStateException("Client should call computeFlow first.");
        }

        Set<Integer> visited = new HashSet<>();
        Queue<Integer> next = new LinkedList<>();

        next.add(source);
        visited.add(source);

        while (!next.isEmpty()) {
            int pos = next.poll();

            for (int nghbr : graph.getNeighborhood(pos)) {
                Pair<Integer, Integer> edge = new Pair(pos, nghbr);
                if (arcCapacities.get(edge) - arcFlows.get(edge) > 0
                        && !visited.contains(nghbr)) {
                    visited.add(nghbr);
                    next.add(nghbr);
                }
            }
        }

        ArrayList<Edge> cut = new ArrayList<>();
        for (int v : visited) {
            for (int nghbr : graph.getNeighborhood(v)) {
                if (!visited.contains(nghbr)) {
                    cut.add(new UndirectedEdge(v, nghbr));
                }
            }
        }

        return cut;
    }

    public static void main(String[] args) {
        SimpleGraph testGraph = new SimpleGraph();
        Map<Pair<Integer, Integer>, Integer> arcCap = new HashMap<>();
        int source, sink;

        // create graph
        testGraph.addVertex(0);
        testGraph.addVertex(1);
        testGraph.addVertex(2);
        testGraph.addVertex(3);
        testGraph.addVertex(4);
        testGraph.addVertex(5);

        // arc capacities
        arcCap.put(new Pair(0, 1), 2);
        arcCap.put(new Pair(0, 2), 3);
        arcCap.put(new Pair(1, 3), 3);
        arcCap.put(new Pair(1, 4), 1);
        arcCap.put(new Pair(2, 3), 1);
        arcCap.put(new Pair(2, 4), 1);
        arcCap.put(new Pair(3, 5), 2);
        arcCap.put(new Pair(4, 5), 3);

        source = 0;
        sink = 5;

        EdmondsKarpMaxFlow flow = new EdmondsKarpMaxFlow(testGraph, arcCap, source, sink);
        System.out.println("The flow is: " + flow.computeFlow());

    }
}
