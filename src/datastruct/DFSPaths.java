package datastruct;

import edu.princeton.cs.algs4.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mitya on 4/11/20.
 */
public class DFSPaths {
    private NDirectedEdge[] edgeTo;
    private double [] distTo;
    private IndexMinPQ<Double> pq;
    private NNode [] nodes;

    EdgeWeightedDigraph G;
    private int end;
    private int s;
    private int d;
    private int count = 0;

    public DFSPaths(EdgeWeightedDigraph G, int s, int d) {
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

        count++;
        StdOut.println(count + " : " + path);

        //if(path.compareTo(Integer.toString(current)) == 0) {
        //if (path.IndexOf(" " + (current.id + 1).ToString() + " ") != -1)
        //if (path.contains(" " + (current) + " ")) {
        if (path.contains((current) + " ")) {
            //StdOut.println(count + ":" + "path.compare not found");
            return paths;
        } else {
            if(path.isEmpty()) {
                //StdOut.println(count + ":" + "path empty " + path);
                path = Integer.toString(current) + " ; 0" ;
            } else {
                //StdOut.println(count + ":" + "path: " + path);
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

            for(DirectedEdge e : G.adj(current)) {
                //StdOut.println(count + " :from: " + current + " to: " + e.to() + " " + path);
                List<String> result = dfs(e.to(), path, e.weight());

                if(result.size() != 0)
                    paths.addAll(result);
            }
        }
        return paths;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        int s = Integer.parseInt(args[1]);
        int d = Integer.parseInt(args[2]);

        StdOut.println(G);
        StdOut.println("------------------------");

        DFSPaths p = new DFSPaths(G, s, d);

        List<String> pts =  p.dfs();
        StdOut.println("------------------------");
        for(String itp : pts)
            StdOut.println(itp);
    }

}