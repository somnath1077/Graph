/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;

/**
 *
 * @author sikdar
 */
public class NodeAttribute {

    private Coordinate coord;
    private Integer size;
    private Color color;
    private String label;

    public NodeAttribute() {
        coord = new Coordinate(0, 0);
        size = 10;
        color = Color.blue;
        label = "";
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

    public String toString() {
        String ret = "Coordinate: " + this.coord.toString() + "; ";
        ret += "Size: " + this.size + "; ";
        ret += "Color: " + this.color.toString() + "; ";
        ret += "Label: " + this.label + ". ";
        return ret;
    }
}

