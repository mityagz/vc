package datastruct;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by mitya on 4/11/20.
 */
public class NEPaths {
    private NDirectedEdge[] edgeTo;
    private double [] distTo;
    private IndexMinPQ<Double> pq;
    private NNode [] nodes;

    NGraph G;
    private int end;
    private int s;
    private int d;
    private int count = 0;

    public NEPaths(NGraph G, int s, int d) {
        this.G = G;
        this.s = s;
        this.d = d;
        this.end = d;
    }

    //public List<String> dfs() {
    public AggregateRoute dfs() {
        return dfs(s, null, "", "", 0);
    }

    //private List<String> dfs(int v, NDirectedEdge de, String path, String nPath, double weight) {
    private AggregateRoute dfs(int v, NDirectedEdge de, String path, String nPath, double weight) {
        LinkedList<String> paths = new LinkedList<String>();
        //LinkedList<LinkedList<NDirectedEdge>> nPaths = new LinkedList<LinkedList<NDirectedEdge>>();
        LinkedList<String> nPaths = new LinkedList<String>();
        AggregateRoute aPaths = new AggregateRoute(paths, nPaths);

        count++;
        //StdOut.println(count + " : " + path);
        if (path.contains(v + " ") || path.contains(v + ":")) {
            return aPaths;
        } else {
            if(path.isEmpty()) {
                 path = Integer.toString(v) + " ; 0";
            } else {
                int index = path.indexOf(";");
                String temp = path.substring(0, index);
                String sum = path.substring(index + 1);
                weight += Double.valueOf(sum);
                path = temp + " -> " + v + "; " + weight;
                //nPath += " " + v + " from:" + de.from() + " to: " + de.to() + " ifOut:" + de.ifOut() + " ifIn:" + de.ifIn();
                //nPath += " " + de.from() + ":ifOut:" + de.ifOut() + " " + de.to() + ":ifIn:" + de.ifIn();
                nPath += " " + de.from() + ":ifOut:" + de.ifIn() + " " + de.to() + ":ifIn:" + de.ifOut();
                //StdOut.println(nPath);
            }

            if(v == end) {
                //paths.add(path);
                //nPaths.add(nPath);
                aPaths.add(path, nPath);
                return aPaths;
            }

            for(NDirectedEdge e : G.adj(v)) {
                //List<String> result = dfs(e.to(), e, path, nPath, e.weight());
               AggregateRoute aggResult = dfs(e.to(), e, path, nPath, e.weight());

                if(aggResult.getPath() != null && aggResult.getPath().size() != 0) {
                    //paths.addAll(aggResult.getPath());
                    //nPaths.addAll(aggResult.getnPath());
                    aPaths.add(aggResult.getPath(), aggResult.getnPath());
                }
            }
        }
        return aPaths;
    }

    /**
    public static void main(String[] args) {
        In in = new In(args[0]);
        NGraph G = new NGraph(in);
        int s = Integer.parseInt(args[1]);
        int d = Integer.parseInt(args[2]);

        StdOut.println(G);
        StdOut.println("------------------------");

        NEPaths p = new NEPaths(G, s, d);

        //List<String> pts =  p.dfs();
        AggregateRoute  pts =  p.dfs();
        StdOut.println("------------------------");
        for(String itp : pts.getPath()) {
            StdOut.println(itp);
        }
        for(String itp : pts.getnPath()) {
            StdOut.println(itp);
        }
    }
     */
}
