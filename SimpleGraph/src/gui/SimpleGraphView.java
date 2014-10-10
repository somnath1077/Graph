/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import simplegraph.Edge;

/**
 *
 * @author sikdar
 */
public class SimpleGraphView extends JFrame {

    private final JPanel pane;
    private final JPanel buttonPanel;
    private final JTextField label;
    private final JButton buttTwoColor;
    private final JButton buttIndSet;
    private DrawableGraph graph;
    private Integer highlightedVertex;

    public SimpleGraphView() {
        super("Simple Graph View");

        pane = new JPanel();
        buttonPanel = new JPanel();
        label = new JTextField("My colorful graph");
        label.setEditable(rootPaneCheckingEnabled);
        buttTwoColor = new JButton("Two Color Graph");
        buttIndSet = new JButton("Maximum Independent Set");
        // buttonDeleteVertex = new JButton("Delete Vertex");
        // buttonAddEdge = new JButton("Add Edge");

        setBounds(0, 0, 1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container cont = this.getContentPane();
        cont.add(label, BorderLayout.NORTH);
        cont.add(pane, BorderLayout.CENTER);
        buttonPanel.add(buttTwoColor);
        buttonPanel.add(buttIndSet);
        cont.add(buttonPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    public void addTwoColorListener(ActionListener act) {
        buttTwoColor.addActionListener(act);
    }

    public void addAddVertexListener(ActionListener act) {
        buttIndSet.addActionListener(act);
    }

    /*
    public void addDeleteVertexListener(ActionListener act) {
        buttonDeleteVertex.addActionListener(act);
    }

    public void addAddEdgeListener(ActionListener act) {
        buttonAddEdge.addActionListener(act);
    }

    */
    public void addMouseListener(MouseListener ml) {
        pane.addMouseListener(ml);
    }

    public void updateGraph(DrawableGraph g) {
        this.graph = g;
        String info = "Graph is " + (graph.isConnected(true) ? "connected" : "disconnected");
        info += " with n = " + graph.size();
        info += " and with m = " + graph.getNumberOfEdges();
        System.out.print("setting label text to " + info);
        label.setText(info);
        System.out.println(". done: " + label.getText());

        label.repaint();
        label.invalidate();

        repaint();
        invalidate();
    }

    public void setHighlightedVertex(Integer u) {
        highlightedVertex = u;
        repaint();
        invalidate();
    }

    @Override
    public void paint(Graphics g) {
        if (graph == null || g == null) {
            return;
        }

        // we want to draw on pane's graphics object
        g = pane.getGraphics();

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
        /*String info = "Graph is " + (graph.isConnected(true) ? "connected" : "disconnected");
         info += " with n=" + graph.size();
         info += " and with m=" + graph.getNumberOfEdges();
         //info += " and avg degree = " + graph.getAverageDegree();

         g.setColor(Color.black);
         char[] data = info.toCharArray();
         g.drawChars(data, 0, data.length, 10, 10);
         */
    }

}
