package simplegraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author sikdar
 */
public class GraphReader {

    /**
     * Reads a graph from file
     *
     * @param fileName is the name of the file containing the graph
     * @return a simple graph
     */
    public static SimpleGraph readGraph(String fileName) {
        /* Initialize file pointers */
        FileReader fr = null;
        BufferedReader br = null;

        /* Initialize graph fields*/
        SimpleGraph graph = new SimpleGraph();

        String line = null;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                String[] vPair = line.split(" ");
                int v1 = Integer.parseInt(vPair[0]);
                int v2 = Integer.parseInt(vPair[1]);
                graph.addVertex(v1);
                graph.addVertex(v2);
                graph.addEdge(v1, v2);
            }
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        }

        return graph;

    }

}
