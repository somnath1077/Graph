/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import simplegraph.SimpleGraph;

/**
 *
 * @author sikdar
 */
class NodeAttribute {

    private Coordinate coord;
    private Integer size;
    private Color color;
    private String label;

    public NodeAttribute() {
        coord = new Coordinate(0, 0);
        size = 5;
        color = Color.blue;
        label = "";
    }

    public void setAttribute(NodeAttribute att) {
        this.coord = att.coord;
        this.size = att.size;
        this.color = att.color;
        this.label = att.label;
    }

    public void setCoordinate(Coordinate c) {
        this.coord = c;
    }

    public void setSize(Integer sz) {
        this.size = sz;
    }

    public void setColor(Color col) {
        this.color = col;
    }

    public void setLabel(String lab) {
        this.label = lab;
    }

    public Coordinate getCoordinate() {
        return this.coord;
    }

    public Integer getSize() {
        return this.size;
    }

    public Color getColor() {
        return this.color;
    }

    public String getLabel() {
        return this.label;
    }

}

public class DrawableGraph extends SimpleGraph {

    private final Map<Integer, NodeAttribute> attributeMap;

    public DrawableGraph() {
        super();
        this.attributeMap = new HashMap<>();
    }

    public NodeAttribute getAttribute(Integer u) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        return attributeMap.get(u);
    }

    public void setAttribute(Integer u, NodeAttribute attr) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        attributeMap.get(u).setAttribute(attr);
    }

    public void setCoordinate(Integer u, Coordinate c) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        attributeMap.get(u).setCoordinate(c);
    }
    
    public void setColor(Integer u, Color col) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        attributeMap.get(u).setColor(col);
    }
    
    public void setSize(Integer u, Integer sz) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        attributeMap.get(u).setSize(sz);
    }
    
    public void setLabel(Integer u, String lab) {
        if (!attributeMap.containsKey(u)) {
            throw new IllegalArgumentException("Not a vertex " + u);
        }
        attributeMap.get(u).setLabel(lab);
        
    }

}
