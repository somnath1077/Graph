/*
 * 
 */
package simplegraph;

import java.util.Collection;

/**
 * An interface for graphs. Both directed, undirected, simple and multigraphs
 * may implement this.
 *
 * @author sikdar
 */
public interface Graph {
    

    /**
     * Checks whether the two given vertices form an edge in the graph. If the
     * graph is directed, then the order matters.
     *
     * @param v1 vertex which is first endpoint
     * @param v2 vertex which is second endpoint
     *
     * @return true if and only if (v1,v2) is an edge in this graph.
     */
    boolean isEdge(int v1, int v2);
    
    /**
     *  Returns the number of vertices in the graph
     * @return size of vertex set
     */
    int size();

    /**
     *  returns the number of edges
     * @return the size of the edge set
     */
    int getNumberOfEdges();

    /**
     *  Checks if v is a vertex
     * @param v
     * @return true if v is a vertex; false otherwise
     */
    boolean isVertex(int v);

    /**
     * Undirected graphs: Returns the the degree of vertex v 
     * Directed graphs: Returns the out-degree of v
     * @param v
     * @return total degree (undirected graphs); out-degree (directed graphs) 
     */
    int getDegree(int v);

    Edge addEdge(int v1, int v2);

    void deleteVertex(int v);

    void deleteEdge(Edge e);

    void deleteEdge(int v1, int v2);
    
    /**
     * Undirected graphs: returns the neighborhood of v
     * Directed graphs: returns the out-neighborhood of v
     * @param v
     * @return 
     */
    Collection<Integer> getNeighborhood(int v);

    /**
     * Adds a vertex to the graph.
     *
     * @return the id of the newly created vertex
     */
    int addNewVertex();

    /**
     * Add v as a vertex
     *
     * @param v vertex to add
     * @return true if vertex was added, false if it was already there
     */
    boolean addVertex(int v);

    /**
     * Checks if the graph is (strongly) connected. If the graph is undirected,
     * the strongly-flag is ignored.
     *
     * @param strongly
     * @return true if either the graph is undirected and connected OR if 
     * directed and strongly connected. Returns false otherwise. 
     */
    boolean isConnected(boolean strongly);

    /**
     * Takes a collection of vertices as input and returns the graph induced on
     * these vertices.
     *
     * @param V
     * @return graph induced by the vertex set V
     */
    Graph getInducedSubgraph(Collection<Integer> V);

    /**
     * Takes a collection of edges or arcs as input and returns the graph whose
     * vertex set is the set of end-points of the edges (arcs) in E; and whose
     * set of edges (arcs) is the set E itself.
     *
     * @param V the vertex set; and E the edge (arc) set.
     * @return subgraph whose vertex set is V and edge (arc) set is E.
     */
    Graph getSubgraph(Collection<Integer> V, Collection<Edge> E);
}
