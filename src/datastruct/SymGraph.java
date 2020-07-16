package datastruct;

import Models.JuniperMX;
import edu.princeton.cs.algs4.*;

import java.util.Map;

/**
 * Created by mitya on 4/13/20.
 */
public class SymGraph {
    private ST<String, Integer> st;
    private String[] keys;
    private NGraph G;
    private JuniperMX root;

    public SymGraph(Adj adj) {
        st = new ST<String, Integer>();
        root = adj.getJ();
        int i = 0;
        for(String h : root.getRnp().keySet()) {
            //System.out.println(h + ":");
            if(!st.contains(h)) {
                st.put(h, i++);
            }
        }

        keys = new String[st.size()];
        for (String ip : st.keys()) {
            keys[st.get(ip)] = ip;
        }

        for(String hh : st.keys()) {
            System.out.println("st : " + hh + " : " + keys[st.get(hh)] + " : " + st.get(hh));
        }

        G = new NGraph(st.size());


        for(String h : root.getRnp().keySet()) {
            Map<String, NodeParse0> npp = root.getRnp().get(h);
            for (String nnp : npp.keySet()) {
                System.out.println(npp.get(nnp));
                System.out.println(nnp);
                if((h != null || h != "0.0.0.0") && nnp != null) {
                    //System.out.println("addEdge: " + h + " : " + st.get(h) + " : "  + st.get(npp.get(nnp).getLo0()));
                    G.addEdge(new NDirectedEdge(st.get(h), st.get(npp.get(nnp).getLo0()), 1, npp.get(nnp).getIfI(), npp.get(nnp).getIfO()));
                }
            }
        }


        StdOut.println(G);
        StdOut.println("------------------------");

        NEPaths p = new NEPaths(G, 8, 7);

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
}

/*

    st : 10.229.0.0 : 10.229.0.0     : 9  : core0
    st : 10.229.128.0 : 10.229.128.0 : 4  : core1
    st : 10.229.132.0 : 10.229.132.0 : 12 : pe1
    st : 10.229.134.0 : 10.229.134.0 : 2 : pe3
    st : 10.229.136.0 : 10.229.136.0 : 3 ; sw-noc
    st : 10.229.138.0 : 10.229.138.0 : 5 : srx-noc
    st : 10.229.140.0 : 10.229.140.0 : 10 : bras0
    st : 10.229.142.0 : 10.229.142.0 : 11 : access0
    st : 10.229.4.0 : 10.229.4.0 :      6 : pe0
    st : 10.229.6.0 : 10.229.6.0 :      1 : pe2
    st : 10.240.0.65 : 10.240.0.65 :    8 : ce0
    st : 10.240.0.66 : 10.240.0.66 :    7 : ce2
    st : 10.248.0.4 : 10.248.0.4 :     13 : ce1
    st : 10.248.0.66 : 10.248.0.66 :    0 : ce3

    0:ifOut:ge-0/0/3 5:ifIn:ge-0/0/2 5:ifOut:ge-0/0/0 3:ifIn:xe-0/0/2 3:ifOut:xe-0/0/0 4:ifIn:ge-0/0/5 4:ifOut:ge-0/0/4 2:ifIn:ge-0/0/1 2:ifOut:ge-0/0/4 13:ifIn:ge-0/0/2 13:ifOut:ge-0/0/0 12:ifIn:ge-0/0/3 12:ifOut:ge-0/0/0 9:ifIn:ge-0/0/3 9:ifOut:ge-0/0/4 1:ifIn:ge-0/0/1 1:ifOut:ge-0/0/3 8:ifIn:ge-0/0/1 8:ifOut:ge-0/0/0 6:ifIn:ge-0/0/3
 */