/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplegraph;

/**
 *
 * @author somnath
 */
public class Pair<A, B> {

    private A first;
    private B second;

    public Pair(A first, B second) {
        super();
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }

        Pair other = (Pair) obj;
        boolean firstCondition = (this.first == other.first)
                || (this.first != null && other.first != null && this.first.equals(other.first));
        boolean secondCondition = (this.second == other.second)
                || (this.second != null && other.second != null && this.second.equals(other.second));
        return firstCondition && secondCondition;
        
        /*if (this.first == other.first
                || (this.first != null && other.first != null && this.first.equals(other.first))
                && this.second == other.second
                || (this.second != null && other.second != null && this.second.equals(other.second))) {
            return true;
        }
        
        return false;
        */
    }

    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }
}
