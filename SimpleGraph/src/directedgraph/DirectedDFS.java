package directedgraph;

import java.util.*;

public class DirectedDFS {

    private final Digraph graph;
    private Collection<Integer> reachable;

    public DirectedDFS(Digraph digraph) {
        this.graph = digraph;
        reachable = new HashSet<Integer>(digraph.size());
    }

    public void fromSource(int s) {
        dfs(s);
    }

    public void fromSources(Collection<Integer> sources) {
        for (Integer v : sources) {
            if (!reachable.contains(v)) {
                dfs(v);
            }
        }
    }

    public boolean isStronglyConnected() {
        Collection<Integer> vertices = graph.getVertexSet();
        reachable.clear();

        for (Integer v : vertices) {
            dfs(v);
            if (reachable.size() != graph.size()) {
                return false;
            }
            reachable.clear();
        }
        return true;
    }

    private void dfs(int s) {
        reachable.add(s);
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(s);

        while (!queue.isEmpty()) {
            Integer next = queue.remove();

            for (Integer u : graph.getOutNeighbors(next)) {
                if (!reachable.contains(u)) {
                    reachable.add(u);
                    queue.add(u);
                }
            }
        }
    }

    public static void main(String[] args) {
        Digraph d = new Digraph();
        d.addVertex(1);
        d.addVertex(2);
        d.addVertex(3);
        d.addVertex(4);
        d.addVertex(5);
        d.addArc(1, 2);
        d.addArc(2, 3);
        d.addArc(3, 4);
        d.addArc(4, 1);
        d.addArc(1, 5);
        d.addArc(5, 2);

        DirectedDFS reach = new DirectedDFS(d);
        System.out.println(reach.isStronglyConnected());
    }
}
