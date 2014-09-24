/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static java.lang.Math.sqrt;
import static java.lang.StrictMath.pow;

/**
 *
 * @author sikdar
 */
public class Coordinate {

    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Finds and returns a closest vertex to coordinate c within a radius of
     * given radius. Returns null if no vertex is within the specified radius.
     *
     * @param c
     * @param radius
     * @return closest vertex, or null if no such within radius
     */
    public double findDistance(Coordinate other) {
        int xdiff = this.x - other.x;
        int ydiff = this.y - other.y;
        return sqrt(pow(xdiff, 2) + pow(ydiff, 2));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.x;
        hash = 17 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        final Coordinate other = (Coordinate) obj;
        if (this.x != other.x || this.y != other.y) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

}
