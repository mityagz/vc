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


/*
private List<string> dfs(Vertex current, string path, int weight)
        {
            //создаем новый список путей
            List<string> pathes = new List<string>();
            //если в пути уже встречался эта вершина возвращаем пустой список
            if (path.IndexOf(" " + (current.id + 1).ToString() + " ") != -1)
                return pathes;
            else
                //в противном случае
                //если путь ещё не был начат, начинаем
                if (path == "")
                    path = (current.id + 1).ToString() + "; 0";
                else
                {
                    //если был начат, продолжаем
                    int index = path.IndexOf(";");
                    string temp = path.Substring(0, index);
                    string sum = path.Substring(index + 1);
                    //меняем вес
                    weight += Convert.ToInt32(sum);
                    //наращиваем путь
                    path = temp + " -> " + (current.id + 1).ToString() + "; " + weight.ToString();
                }
            //если дошли до последней вершины завераем построение пути
            if (current.id == end)
            {
                pathes.Add(path);
                return pathes;
            }
            //просматриваем все вершины, в которые идут ребра из данной
            foreach (KeyValuePair<Vertex, Edge> pair in current.exit_edges)
            {
                //для каждой вершины получаем список путей
                List<string> result = dfs(pair.Key, path, pair.Value.weight);
                //если есть хоть один путь до конечной вершины
                if (result.Count != 0)
                    //добавляем этот спмсок к общему списку
                    pathes.AddRange(result);
            }
            return pathes;
        }


 */