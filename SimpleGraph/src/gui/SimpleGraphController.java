/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import simplegraph.SimpleGraph;

/**
 *
 * @author sikdar
 */
public class SimpleGraphController {

    private final SimpleGraphView view;
    private final SimpleGraph graph;
    private final Map<Integer, Coordinate> pos;
    private long lastClickTime;
    private static final long DOUBLE_CLICK_THRESHOLD = 200;
    private Integer selectedVertex = null;

    public SimpleGraphController() {
        view = new SimpleGraphView();
        graph = new SimpleGraph();
        pos = new HashMap<>();
        lastClickTime = System.currentTimeMillis();

        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
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
        int u = graph.addNewVertex();
        pos.put(u, c);
        System.out.println("Added vertex to graph: " + u + " at " + c);
        view.updateGraph(graph);
        view.updateMap(pos);
    }

    private void deleteVertex(Integer u) {
        // delete vertex u and all edges adjacenct to it
        if (u == null) {
            return;
        }

        graph.deleteVertex(u);
        pos.remove(u);
        System.out.println("Removed vertex: " + u);

        view.updateGraph(graph);
        view.updateMap(pos);
    }

    private void highlightVertex(Integer v) {
        view.setHighlightedVertex(v);
    }

    private Integer getClosestVertex(Coordinate c, int radius) {
        double closestDist = radius + 1;
        Integer closestVertex = null;

        for (Integer v : pos.keySet()) {
            Coordinate coordV = pos.get(v);

            double dist = c.findDistance(coordV);
            if (dist <= radius && dist < closestDist) {
                closestDist = dist;
                closestVertex = v;
            }
        }

        return closestVertex;
    }

    private void doSpringLayout() {
        SpringLayout spLay = new SpringLayout(graph, pos);
        spLay.iterate(1);
        view.updateGraph(graph);
        view.updateMap(pos);
    }

    private void toggleEdge(int v, int u) {
        if (v == u) {
            return;
        }
        graph.toggleEdge(v, u);
        view.updateGraph(graph);
        view.updateMap(pos);
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
