package datastruct;

import edu.princeton.cs.algs4.*;

/**
 * Created by mitya on 4/11/20.
 */
public class NGraph {
    private static final String NEWLINE = System.getProperty("line.separator");
    private  int V;
    private int E;
    private Bag<NDirectedEdge>[] adj;
    private int[] indegree;

    public NGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be non-negative");
        this.V = V;
        this.E = 0;
        adj = (Bag<NDirectedEdge> []) new Bag[V];
        indegree = new int[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<NDirectedEdge>();
        }
    }

    public NGraph(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be non-negative");
        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            double weight = 0.01 * StdRandom.uniform(100);
            NDirectedEdge e = new NDirectedEdge(v, w, weight);
            addEdge(e);
        }
    }

    public NGraph(NGraph G) {
        this(G.V());
        this.E = G.E();
        for(int v = 0; v < G.V(); v++) {
            indegree[v] = G.indegree(v);
        }

        Stack<NDirectedEdge> reverse = new Stack<NDirectedEdge>();
        for(int v = 0; v < V; v++) {
            for(NDirectedEdge e : G.adj(v))
                reverse.push(e);
            for(NDirectedEdge e : G.adj(v))
                adj[v].add(e);
        }
    }

    public NGraph(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be non-negative");
        for(int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            validateVertex(v);
            validateVertex(w);
            double weight = in.readDouble();
            String ifOut = in.readString();
            String ifIn = in.readString();
            NDirectedEdge e = new NDirectedEdge(v, w, weight, ifOut, ifIn);
            addEdge(e);
        }

    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(NDirectedEdge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[e.from()].add(e);
        indegree[w]++;
        E++;
    }

    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }

    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public Iterable<NDirectedEdge> adj(int v) {
        return adj[v];
    }

    public Iterable<NDirectedEdge> edges() {
        Bag<NDirectedEdge> edges = new Bag<NDirectedEdge>();
        for(int v = 0; v < V; v++) {
            for(NDirectedEdge e : adj[v])
                edges.add(e);
        }
        return edges;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }


    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (NDirectedEdge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        NGraph G = new NGraph(in);
        StdOut.println(G);
    }
}
