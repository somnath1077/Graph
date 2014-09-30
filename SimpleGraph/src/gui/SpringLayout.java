/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static java.lang.Math.log;
import java.util.Collection;
import java.util.HashMap;

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
    private static final double c3 = 2.0; /// gravity

    private final DrawableGraph graph;

    public SpringLayout(DrawableGraph graph) {
        this.graph = graph;
    }

    // calculates the attractive force between 
    // the edge (v1, v2); 
    private double interEdgeForce(int v1, int v2) {
        Coordinate coord1 = graph.getCoordinate(v1);
        Coordinate coord2 = graph.getCoordinate(v2);
        double dist = coord1.findDistance(coord2);
        return log(dist) * c1;
        //return sqrt(dist);
    }

    // calculates the force between two vertices 
    // v1 and v2, assuming that they are not 
    // connected by an edge. This is repulsive force.
    private double interVertexForce(int v1, int v2) {
        Coordinate coord1 = graph.getCoordinate(v1);
        Coordinate coord2 = graph.getCoordinate(v2);
        double dist = coord1.findDistance(coord2);
        return 1000d * (c3 / dist);
    }

    private Coordinate totalOffset(int v) {
        Collection<Integer> nbr = graph.getNeighborhood(v);
        double xOffset = 0;
        double yOffset = 0;

        Coordinate coordV = graph.getCoordinate(v);

        for (Integer u : nbr) {
            double force = interEdgeForce(v, u);
            System.out.println("force:" + force);
            Coordinate coordU = graph.getCoordinate(u);

            double dist = coordU.findDistance(coordV);
            double xDiff = (coordU.getX() - coordV.getX());
            double yDiff = (coordU.getY() - coordV.getY());
            double cosine = xDiff / dist;
            double sine = yDiff / dist;
            System.out.println(v + " has force " + cosine + "," + sine + " from (nbr) " + u);
            xOffset += cosine * force;
            yOffset += sine * force;
        }

        for (Integer u : graph.getVertexSet()) {
            if (u != v) {
                Coordinate coordU = graph.getCoordinate(u);

                double force = interVertexForce(u, v);

                System.out.println(v + "," + u + " has gravity force " + force);

                double xDiff = (coordU.getX() - coordV.getX());
                double yDiff = (coordU.getY() - coordV.getY());

                double dist = coordU.findDistance(coordV);
                double cosine = xDiff / dist;
                double sine = yDiff / dist;
                xOffset += cosine * force;
                yOffset += sine * force;
            }
        }

        return new Coordinate((int) (xOffset), (int) (yOffset));
    }

    public void iterate(int numIterations) {
        System.out.println("iterate " + numIterations);
        if (graph.getVertexSet().isEmpty()) {
            return;
        }

        HashMap<Integer, Coordinate> offsetMap = new HashMap<>();
        Collection<Integer> vertices = graph.getVertexSet();
        for (int i = 0; i < numIterations; ++i) {
            for (Integer v : vertices) {
                //double total = totalForce(v);
                //double offset = c1 * total;
                Coordinate offset = totalOffset(v);
                System.out.println("perturbing " + v + " by " + offset.toString());

                offsetMap.put(v, offset);
            }
            // updates all the coordinates
            for (Integer v : offsetMap.keySet()) {
                Coordinate n = graph.getCoordinate(v).add(offsetMap.get(v));
                graph.setCoordinate(v, n);
            }
        }
    }
}
