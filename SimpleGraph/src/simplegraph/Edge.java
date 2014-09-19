package simplegraph;

/**
 *
 * @author sikdar
 */
public class Edge implements Comparable<Edge> {

    private final int v1, v2;

    public Edge(int a, int b) {
        if (a == b) {
            throw new IllegalArgumentException("It's a simple graph!");
        }
        v1 = Math.min(a, b);
        v2 = Math.max(a, b);
    }

    public int getV1() {
        return v1;
    }

    public int getV2() {
        return v2;
    }

    public int getOther(int v) {
        if (v1 != v && v2 != v) {
            throw new IllegalArgumentException("Not this edge.");
        }
        return v == v1 ? v2 : v1;
    }

    @Override
    public int hashCode() {
        return 10000541 * v1 + v2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Edge) {
            Edge e = (Edge) obj;
            return (e.v1 == v1 && e.v2 == v2);
        }
        return false;
    }

    public String toString() {
        return "(" + v1 + "," + v2 + ")";

    }

    public int compareTo(Edge e) {
        if (this.v1 != e.v1) {
            return v1 - e.v1;
        }
        return v2 - e.v2;
    }
}
