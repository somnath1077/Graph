package simplegraph;

/**
 *  
 * @author sikdar
 * An edge is viewed is an ordered pair of vertices. In directed graphs, the pair 
 * is stored as (tail, head). In undirected graphs, the pair is stored as: 
 * (vertex_with_smaller_index, vertex_with_larger_index).
 */
public interface Edge {

    /**
     * Takes a vertex and returns the other endpoint/ 
     * @param v
     * @return the other endpoint of the edge
     */
    int getOther(int v);

    /**
     *  if undirected, this gives the smaller of the two endpoints;
     *  if directed, this gives tail.
     * @return 
     */
    int getV1();

    /**
     * if undirected, this gives the larger of the two endpoints 
     * if directed, this gives head
     * @return 
     */
    int getV2();
    
}
