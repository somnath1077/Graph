package directedgraph;

import simplegraph.Edge;

public class Arc implements Edge {

    private final int tail;			// tail of arc
    private final int head;			// head of arc
    private final double weight;	                // weight of arc

    public Arc(int tail, int head, double weight) {
        this.tail = tail;
        this.head = head;
        this.weight = weight;
    }

    public Arc(int tail, int head) {
        this.tail = tail;
        this.head = head;
        this.weight = 1;			// the default arc weight is 1
    }

    public double weight() {
        return weight;
    }

    public int tail() {
        return tail;
    }

    public int head() {
        return head;
    }

    public Arc increaseWeight(double extra) {
        return new Arc(head, tail, weight + extra);
    }

    public Arc withWeight(double theWeight) {
        return new Arc(head, tail, theWeight);
    }

    public Arc flip() {
        return new Arc(tail, head, weight);
    }

    public int compareTo(Arc that) {
        if (that == null) {
            throw new IllegalArgumentException();
        }

        if (weight != that.weight) {
            return Double.compare(weight, that.weight);
        }

        if (head != that.head) {
            return head - that.head;
        }

        return tail - that.tail;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Arc) {
            Arc e = (Arc) obj;
            return (e.tail == tail && e.head == head && e.weight == weight);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 2147483647 * tail + head + ((Double) weight).hashCode();
    }

    public String toString() {
        return String.format("%d -> %d weight: %.2f", tail, head, weight);
    }

    public static void main(String[] args) {
        Arc a = new Arc(1, 2, 4);
        Arc b = null;
        System.out.println(a.equals(new Arc(1, 2, 4)));
        System.out.println(a.toString());
        System.out.println(a.equals(b));
    }

    @Override
    public int getOther(int v) {
        if (head == v) {
            return tail;
        }
        if (tail == v) {
            return head;
        }
        throw new IllegalArgumentException("Not a vertex: " + v);
    }

    @Override
    public int getV1() {
        return tail;
    }

    @Override
    public int getV2() {
        return head;
    }
}
