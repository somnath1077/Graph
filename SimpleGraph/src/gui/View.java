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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import simplegraph.Edge;
import simplegraph.SimpleGraph;

/**
 *
 * @author sikdar
 */
public class View extends JPanel {

    private final SimpleGraph graph;
    private final Map<Integer, Coordinate> pos;
    private Integer highlightedVertex;

    public View() {
        graph = new SimpleGraph();
        pos = new HashMap<>();
        highlightedVertex = null;

        setBackground(Color.white);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, 1000,1000);
        g.setColor(Color.black);
        Collection<Edge> edges = graph.getEdges();
        for (Edge e : edges) {
            int v1 = e.getV1();
            int v2 = e.getV2();
            Coordinate c1 = pos.get(v1);
            Coordinate c2 = pos.get(v2);
            g.drawLine(c1.getX() + 5, c1.getY() + 5, c2.getX() + 5, c2.getY() + 5);
        }

        for (Integer v : pos.keySet()) {
            Coordinate c = pos.get(v);

            if (v != highlightedVertex) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.CYAN);
            }

            g.fillOval(c.getX(), c.getY(), 10, 10);

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
        int u = graph.addNewVertex();
        pos.put(u, c);
        System.out.println("Added vertex to graph: " + u + " at " + c);
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

    public void toggleEdge(int v, int u) {
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

    public GraphViewListener(View gui) {
        this.gui = gui;

    }

    /*
    
     @Override
     public void mousePressed(MouseEvent e) {
     if (e.getButton() == MouseEvent.BUTTON1) {
     System.out.println("left mouse pressed");
     }
     }

     @Override
     public void mouseReleased(MouseEvent e) {
     System.out.println("release");
     }
    
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Coordinate click = new Coordinate(x, y);
        System.out.println("User clicked at " + click);

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
}
