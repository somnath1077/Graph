/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JPanel;
import simplegraph.Edge;

/**
 *
 * @author sikdar
 */
public class View extends JPanel {

    private final DrawableGraph graph;
    private Integer highlightedVertex;

    public View() {
        graph = new DrawableGraph();
        highlightedVertex = null;

        setBackground(Color.white);
    }

    @Override
    public void paint(Graphics g) {

        System.out.println("paint!");

        g.setColor(Color.white);
        g.fillRect(0, 0, 1000, 1000);
        g.setColor(Color.black);
        Collection<Edge> edges = graph.getEdges();
        for (Edge e : edges) {
            int v1 = e.getV1();
            int v2 = e.getV2();
            Coordinate c1 = graph.getCoordinate(v1);
            Coordinate c2 = graph.getCoordinate(v2);
            g.drawLine(c1.getX() + 5, c1.getY() + 5, c2.getX() + 5, c2.getY() + 5);
        }

        for (Integer v : graph.getVertexSet()) {
            Coordinate c = graph.getCoordinate(v);

            if (v != highlightedVertex) {
                g.setColor(graph.getColor(v));
            } else {
                g.setColor(Color.CYAN);
            }

            g.fillOval(c.getX(), c.getY(), graph.getSize(v), graph.getSize(v));

            // labels magic, fix at some point, to write string! 
            g.setColor(Color.blue);
            char[] data = ("" + v).toCharArray();
            g.drawChars(data, 0, data.length, c.getX() + 5, c.getY() - 5);
        }
        String info = "Graph is " + (graph.isConnected(true) ? "connected" : "disconnected");
        info += " with n=" + graph.size();
        info += " and with m=" + graph.getNumberOfEdges();
        //info += " and avg degree = " + graph.getAverageDegree();

        g.setColor(Color.white);
        g.fillRect(0, 0, 700, 100);

        g.setColor(Color.black);
        char[] data = info.toCharArray();
        g.drawChars(data, 0, data.length, 10, 10);
    }

    public void addNewVertex(Coordinate c) {
        // add vertex to graph, add coordinate 
        int u = graph.addNewVertex(c);
        System.out.println("Added vertex to graph: " + u + " at " + c);
        repaint();
        invalidate();
    }

    public void deleteVertex(Integer u) {
        // delete vertex u and all edges adjacenct to it
        if (u == null) {
            return;
        }
        graph.deleteVertex(u);
        System.out.println("Removed vertex: " + u);
        repaint();
        invalidate();
    }

    public void highlightVertex(Integer v) {
        highlightedVertex = v;
        repaint();
        invalidate();
    }

    public Integer getClosestVertex(Coordinate c, int radius) {
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

    public void doSpringLayout() {
        SpringLayout spLay = new SpringLayout(graph);
        spLay.iterate(1);
        repaint();
        invalidate();
    }

    public void toggleEdge(int v, int u) {
        if (v == u) {
            return;
        }
        graph.toggleEdge(v, u);
        repaint();
        invalidate();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        final View gui = new View();
        frame.add(gui);
        frame.setMinimumSize(new Dimension(1000, 700));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add mouse listener
        GraphViewListener mouseListener = new GraphViewListener(gui);
        gui.addMouseListener(mouseListener);

        System.out.println("good bye");
    }
}

class GraphViewListener extends MouseAdapter {

    private final View gui;
    private Integer selectedVertex = null;
    private long lastClickTime = System.currentTimeMillis();

    private static final long DOUBLE_CLICK_THRESHOLD = 200;

    public GraphViewListener(View gui) {
        this.gui = gui;
    }

    /**
     * With each right click, we recompute the graph layout according to our
     * spring layout model.
     */
    public void rightClick() {
        gui.doSpringLayout();
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
        Integer closest = gui.getClosestVertex(click, 15);

        if (closest == null) {
            System.out.println("\tAdded new vertex at " + click);
            gui.addNewVertex(click);
        } else if (selectedVertex == null) {
            // TODO make GUI highlight selected vertex!
            System.out.println("\tSelected vertex " + closest);
            selectedVertex = closest;
            gui.highlightVertex(closest);
        } else {

            System.out.println("\tToggling vertex " + selectedVertex + " -- " + closest);
            gui.toggleEdge(selectedVertex, closest);
            selectedVertex = null;
            gui.highlightVertex(null);
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
        Integer closest = gui.getClosestVertex(click, 15);
        if (selectedVertex == closest) {
            selectedVertex = null;
        }
        gui.deleteVertex(closest);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
}
