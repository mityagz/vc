package datastruct;

import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

/**
 * Created by mitya on 4/11/20.
 */
public class AggregateRoute {
    private LinkedList<String> paths;
    private LinkedList<String> nPaths;

    public AggregateRoute() {

    }

    public AggregateRoute(LinkedList<String> path, LinkedList<String> nPath) {
        this.paths = path;
        this.nPaths = nPath;
    }

    public void add(LinkedList<String> paths, LinkedList<String> nPaths) {
        if(paths != null) {
            if(this.paths == null)
                this.paths = paths;
            this.paths.addAll(paths);
        }
        if(nPaths != null) {
            if(this.nPaths == null)
                this.nPaths = nPaths;
            this.nPaths.addAll(nPaths);
        }
    }

    public void add(String path, String nPath) {
        this.paths.add(path);
        this.nPaths.add(nPath);
    }

    public  LinkedList<String> getPath() {
        return paths;
    }

    public LinkedList<String> getnPath() {
        return nPaths;
    }
}
