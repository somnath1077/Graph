/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static java.lang.Math.log;
import static java.lang.Math.pow;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import simplegraph.SimpleGraph;

/**
 *
 * @author sikdar
 */
public class SpringLayout {

    /**
     * Iterates n times, changes in the map
     *
     * @param map
     * @param n
     */
    // constants for spring layout
    private static final double c1 = 2.0;
    private static final double c2 = 1.0;
    private static final double c3 = 10.0; /// gravity

    private final Map<Integer, Coordinate> map;
    private final SimpleGraph graph;

    public SpringLayout(SimpleGraph graph, Map<Integer, Coordinate> map) {
        this.graph = graph;
        this.map = map;
    }

    // calculates the attractive force between 
    // the edge (v1, v2); 
    private double interEdgeForce(int v1, int v2) {
        Coordinate coord1 = map.get(v1);
        Coordinate coord2 = map.get(v2);
        double dist = coord1.findDistance(coord2);
        return log(dist) * c1;
    }

    // calculates the force between two vertices 
    // v1 and v2, assuming that they are not 
    // connected by an edge. This is repulsive force.
    private double interVertexForce(int v1, int v2) {

        Coordinate coord1 = map.get(v1);
        Coordinate coord2 = map.get(v2);
        double dist = coord1.findDistance(coord2);
        return (c3 / pow(dist, 2));

    }

    private Coordinate totalOffset(int v) {
        Collection<Integer> nbr = graph.getNeighborhood(v);
        double xOffset = 0;
        double yOffset = 0;

        Coordinate coordV = map.get(v);

        for (Integer u : nbr) {
            double force = interEdgeForce(v, u);
            System.out.println("force:" + force);
            Coordinate coordU = map.get(u);

            double dist = coordU.findDistance(coordV);
            double xDiff = (coordU.getX() - coordV.getX());
            double yDiff = (coordU.getY() - coordV.getY());
            double cosine = xDiff / dist;
            double sine = yDiff / dist;
            xOffset += cosine * force;
            yOffset += sine * force;
        }

        for (Integer u : graph.getVertexSet()) {
            if (u != v && !nbr.contains(u)) {
                double force = interVertexForce(u, v);
                Coordinate coordU = map.get(u);

                double xDiff = (coordU.getX() - coordV.getX());
                double yDiff = (coordU.getY() - coordV.getY());

                double dist = coordU.findDistance(coordV);
                double cosine = xDiff / dist;
                double sine = yDiff / dist;
                xOffset += cosine * force;
                yOffset += sine * force;
            }
        }

        return new Coordinate((int) (xOffset * c3), (int) (yOffset * c3));
    }

    public void iterate(int n) {
        System.out.println("iterate " + n);
        if (map.keySet().isEmpty()) {
            return;
        }

        HashMap<Integer, Coordinate> offsetMap = new HashMap<Integer, Coordinate>();

        for (int i = 0; i < n; ++i) {
            for (Integer v : map.keySet()) {
                //double total = totalForce(v);
                //double offset = c1 * total;
                Coordinate offset = totalOffset(v);
                System.out.println("perturbing " + v + " by " + offset.toString());

                offsetMap.put(v, offset);
            }
            // updates all the coordinates
            for (Integer v : offsetMap.keySet()) {
                map.put(v, map.get(v).add(offsetMap.get(v)));
            }
        }
    }
}
