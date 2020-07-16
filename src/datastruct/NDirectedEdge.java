package datastruct;

import edu.princeton.cs.algs4.*;

/**
 * Created by mitya on 1/7/20.
 */
public class NDirectedEdge {
    private final int v;
    private final int w;
    private final double weight;

    private String ifOut;   // Outgoing interface vertex v
    private String ifIn;    // Incoming interface vertex w

    public NDirectedEdge(int v, int w, double weight) {
        if (v < 0) throw new IllegalArgumentException("Vertex names must be non-negative integers");
        if (w < 0) throw new IllegalArgumentException("Vertex names must be non-negative integers");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public NDirectedEdge(int v, int w, double weight, String ifOut, String ifIn) {
        if (v < 0) throw new IllegalArgumentException("Vertex names must be non-negative integers");
        if (w < 0) throw new IllegalArgumentException("Vertex names must be non-negative integers");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weight = weight;
        this.ifOut = ifOut;
        this.ifIn = ifIn;
    }

    public double weight() {
        return weight;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public String ifOut () {
        return ifOut;
    }

    public String ifIn() {
        return ifIn;
    }

    public String toString() {
        return String.format("%d->%d %.2f %s %s", v, w, weight, ifOut, ifIn);
    }

    public static void main(String[] args) {
        NDirectedEdge e = new NDirectedEdge(12, 34, 5.67);
        StdOut.println(e);
    }
}
