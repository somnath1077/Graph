/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.event.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import simplegraph.SimpleGraph;

/**
 *
 * @author sikdar
 */

public class DrawableGraph extends SimpleGraph {

    private final Map<Integer, NodeAttribute> attributeMap;
    private final Collection<ActionListener> listeners;

    private static final int COORD = 1;
    private static final int SIZE = 2;
    private static final int COLOR = 3;
    private static final int LABEL = 4;

    public DrawableGraph() {
        super();
        this.attributeMap = new HashMap<>();
        listeners = new HashSet<>();
    }

    public NodeAttribute getAttribute(int u) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        return attributeMap.get(u);
    }

    public Map<Integer, NodeAttribute> getAttributeMap() {
        return attributeMap;
    }

    // ActionEvent constructor takes as parameters: 
    // ActionEvent(Object source, int id, String command);
    public void setCoordinate(int u, Coordinate c) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        attributeMap.get(u).setCoordinate(c);
        for (ActionListener listener : listeners) {
            listener.actionPerformed(new ActionEvent(this, COORD, "Coordinate change"));
        }
    }

    public void setColor(int u, Color col) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        attributeMap.get(u).setColor(col);
        for (ActionListener listener : listeners) {
            listener.actionPerformed(new ActionEvent(this, COLOR, "Color change"));
        }
    }

    public void setSize(int u, Integer sz) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        attributeMap.get(u).setSize(sz);
        for (ActionListener listener : listeners) {
            listener.actionPerformed(new ActionEvent(this, SIZE, "Size change"));
        }
    }

    public void setLabel(int u, String lab) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        attributeMap.get(u).setLabel(lab);
        for (ActionListener listener : listeners) {
            listener.actionPerformed(new ActionEvent(this, LABEL, "Label change"));
        }

    }

    public Coordinate getCoordinate(int u) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        return attributeMap.get(u).getCoordinate();
    }

    public Integer getSize(int u) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        return attributeMap.get(u).getSize();
    }

    public Color getColor(int u) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        return attributeMap.get(u).getColor();
    }

    public String getLabel(int u) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        return attributeMap.get(u).getLabel();
    }

    @Override
    public void deleteVertex(int u) {
        super.deleteVertex(u);
        attributeMap.remove(u);
    }

    public Integer addNewVertex(Coordinate c) {
        int u = super.addNewVertex();
        NodeAttribute att = new NodeAttribute();
        att.setCoordinate(c);
        attributeMap.put(u, att);
        return u;
    }

    public static void main(String[] args) {
        DrawableGraph g = new DrawableGraph();
        g.addVertex(1);
        g.addVertex(2);
        g.addEdge(1, 2);
        g.addVertex(3);
        g.addEdge(1, 3);

        NodeAttribute att1 = new NodeAttribute();
        NodeAttribute att2 = new NodeAttribute();
        NodeAttribute att3 = new NodeAttribute();
        att1.setCoordinate(new Coordinate(1, 1));
        att2.setCoordinate(new Coordinate(2, 2));
        att3.setCoordinate(new Coordinate(3, 3));

        g.attributeMap.put(1, att1);
        g.attributeMap.put(2, att2);
        g.attributeMap.put(3, att3);
        System.out.println(g.attributeMap.get(1).toString());
    }
}
