package datastruct;

//import edu.princeton.cs.algs4.*;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mitya on 4/11/20.
 */
public class NPaths {
    private NDirectedEdge[] edgeTo;
    private double [] distTo;
    private IndexMinPQ<Double> pq;
    private NNode [] nodes;

    NGraph G;
    private int end;
    private int s;
    private int d;
    private int count = 0;

    public NPaths(NGraph G, int s, int d) {
        this.G = G;
        this.s = s;
        this.d = d;
        this.end = d;
    }

    public List<String> dfs() {
        return dfs(s, "", 0);
    }

    private List<String> dfs(int current, String path, double weight) {
        LinkedList<String> paths = new LinkedList<String>();
        LinkedList<LinkedList<NDirectedEdge>> nPaths = new LinkedList<LinkedList<NDirectedEdge>>();

        count++;
        StdOut.println(count + " : " + path);
        if (path.contains((current) + " ")) {
            return paths;
        } else {
            if(path.isEmpty()) {
                 path = Integer.toString(current) + " ; 0";
            } else {
                int index = path.indexOf(";");
                String temp = path.substring(0, index);
                String sum = path.substring(index + 1);
                weight += Double.valueOf(sum);
                path = temp + " -> " + current + "; " + weight;
            }

            if(current == end) {
                paths.add(path);
                return paths;
            }

            for(NDirectedEdge e : G.adj(current)) {
                List<String> result = dfs(e.to(), path, e.weight());

                if(result.size() != 0)
                    paths.addAll(result);
            }
        }
        return paths;
    }

    /**

     public static void main(String[] args) {
        In in = new In(args[0]);
        NGraph G = new NGraph(in);
        int s = Integer.parseInt(args[1]);
        int d = Integer.parseInt(args[2]);

        StdOut.println(G);
        StdOut.println("------------------------");

        NPaths p = new NPaths(G, s, d);

        List<String> pts =  p.dfs();
        StdOut.println("------------------------");
        for(String itp : pts)
            StdOut.println(itp);
    }

     */
}
