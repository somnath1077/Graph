/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import simplegraph.IndependentSet;

/**
 *
 * @author sikdar
 */
public class SimpleGraphController {

    private final SimpleGraphView view;
    private final DrawableGraph graph;

    private long lastClickTime;
    private static final long DOUBLE_CLICK_THRESHOLD = 200;
    private Integer selectedVertex = null;

    public SimpleGraphController() {
        view = new SimpleGraphView();
        graph = new DrawableGraph();

        lastClickTime = System.currentTimeMillis();

        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });

        view.addTwoColorListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleTwoColor();
            }
        });

        view.addIndSetListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleIndSet();
            }
        });
    }

    /**
     * This function colors the graph into two colors, if it is bipartite, in
     * which case it returns true; else returns false;
     *
     * @return true if graph is bipartite together with a two coloring of the
     * graph; false, otherwise.
     */
    private void restoreDefaults(DrawableGraph graph) {
        for (Integer u : graph.getVertexSet()) {
            graph.setColor(u, Color.BLUE);
        }
        view.invalidate();
        view.repaint();
    }

    private boolean handleTwoColor() {
        Collection<Integer> vertices = graph.getVertexSet();

        // A vertex is visited if it and all its neighbors
        // are in the queue
        Collection<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        Iterator<Integer> iter = vertices.iterator();
        if (iter.hasNext()) {
            Integer v = iter.next();
            visited.add(v);
            queue.add(v);
            graph.setColor(v, Color.RED);
            view.invalidate();
            view.repaint();
        } else {             // the graph is empty
            return true;     // and hence return
        }

        while (!queue.isEmpty()) {
            Integer v = queue.remove();
            Collection<Integer> nbrV = graph.getNeighborhood(v);
            if (nbrV.isEmpty()) {
                graph.setColor(v, Color.GREEN);
            } else {
                for (Integer u : nbrV) {
                    if (graph.getColor(u) == graph.getColor(v)) {
                        restoreDefaults(graph);
                        return false;
                    }
                    if (!visited.contains(u)) {
                        visited.add(u);
                        queue.add(u);
                        if (graph.getColor(v) == Color.RED) {
                            graph.setColor(u, Color.GREEN);

                        } else {
                            graph.setColor(u, Color.RED);
                        }
                    }
                }
            }
            view.invalidate();
            view.repaint();
            // check if all vertices have been visited
            // this needs to be checked if the graph is disconnected
            if (queue.isEmpty() && visited.size() != graph.size()) {
                for (Integer u : vertices) {
                    if (!visited.contains(u)) {
                        queue.add(u);
                        visited.add(u);
                        break;
                    }
                }
            }
        }

        return true;
    }

    /**
     * This function invokes the Independent Set algorithm on the DrawableGraph
     * and sets the color of the independent set vertices to BLUE and the
     * remaining to BLACK.
     */
    private void handleIndSet() {
        IndependentSet indSet = new IndependentSet(this.graph);
        Collection<Integer> solution = indSet.maxIS();
        for (Integer u : graph.getVertexSet()) {
            if (solution.contains(u)) {
                graph.setColor(u, Color.BLUE);
            } else {
                graph.setColor(u, Color.BLACK);
            }
        }
        view.invalidate();
        view.repaint();
    }

    private void handleMouseClick(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON3) {
            rightClick();
            return;
        }

        long currentClickTime = System.currentTimeMillis();
        int x = e.getX();
        int y = e.getY();

        Coordinate click = new Coordinate(x, y);
        long diff = currentClickTime - lastClickTime;
        lastClickTime = currentClickTime;

        if (diff > DOUBLE_CLICK_THRESHOLD) {
            singleClick(click);
        } else {
            doubleClick(click);
        }
    }

    private void addNewVertex(Coordinate c) {
        // add vertex to graph, add coordinate 
        int u = graph.addNewVertex(c);
        System.out.println("Added vertex to graph: " + u + " at " + c);
        view.updateGraph(graph);
    }

    private void deleteVertex(Integer u) {
        // delete vertex u and all edges adjacenct to it
        if (u == null) {
            return;
        }

        graph.deleteVertex(u);
        System.out.println("Removed vertex: " + u);
        System.out.println("\t" + graph.getVertexSet());
        view.updateGraph(graph);
    }

    private void highlightVertex(Integer v) {
        view.setHighlightedVertex(v);
    }

    private Integer getClosestVertex(Coordinate c, int radius) {
        double closestDist = radius + 1;
        Integer closestVertex = null;

        for (Integer v : graph.getVertexSet()) {
            Coordinate coordV = graph.getCoordinate(v);

            double dist = c.findDistance(coordV);
            if (dist <= radius && dist < closestDist) {
                closestDist = dist;
                closestVertex = v;
            }
        }

        return closestVertex;
    }

    private void doSpringLayout() {
        SpringLayout spLay = new SpringLayout(graph);
        spLay.iterate(1);
        view.updateGraph(graph);
    }

    private void toggleEdge(int v, int u) {
        if (v == u) {
            return;
        }
        graph.toggleEdge(v, u);
        view.updateGraph(graph);
    }

    /**
     * With each right click, we recompute the graph layout according to our
     * spring layout model.
     */
    public void rightClick() {
        doSpringLayout();
    }

    /**
     * In response to a single click, this function either inserts a new vertex,
     * if there was none at the vicinity of the click; if there is one (current
     * vertex) and the selectedVertex field was empty, it selects the current
     * vertex; else, it draws an edge between the last selected vertex and the
     * current vertex.
     *
     * @param click coordinates of the click
     */
    public void singleClick(Coordinate click) {
        System.out.println("single click at " + click);
        Integer closest = getClosestVertex(click, 15);

        if (closest == null) {
            System.out.println("\tAdded new vertex at " + click);
            addNewVertex(click);
        } else if (selectedVertex == null) {
            // TODO make GUI highlight selected vertex!
            System.out.println("\tSelected vertex " + closest);
            selectedVertex = closest;
            highlightVertex(closest);
        } else {

            System.out.println("\tToggling vertex " + selectedVertex + " -- " + closest);
            toggleEdge(selectedVertex, closest);
            selectedVertex = null;
            highlightVertex(null);
        }

    }

    /**
     * This function takes the coordinates of a click and checks if there is a
     * vertex close by. If so, it deletes that vertex and all edges adjacent to
     * it. It resets the fields lastClickTime. If the vertex found is the one
     * that was selected before, it resets the selectedVertex field.
     *
     * @param click : the coordinates of the current click
     */
    public void doubleClick(Coordinate click) {
        System.out.println("double click at " + click);
        Integer closest = getClosestVertex(click, 15);
        if (selectedVertex == closest) {
            selectedVertex = null;
        }
        deleteVertex(closest);
    }

    public static void main(String[] args) {
        new SimpleGraphController();
    }

}
