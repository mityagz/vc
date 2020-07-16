package datastruct;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Map;

/**
 * Created by mitya on 4/26/20.
 */
public class VisualG {
    int edge_count = 0;
    Graph graph = new SingleGraph("VC");
    Map<String, Map<String, NodeParse0>> rnp;

    VisualG(Adj adj) {

        rnp = adj.getJ().getRnp();

        for (String h : rnp.keySet()) {
                Map<String, NodeParse0> npp = rnp.get(h);
                Node n = graph.addNode(h);
                n.addAttribute("ui.label", h);
        }

        for (String h : rnp.keySet()) {
            Map<String, NodeParse0> npp = rnp.get(h);
                for (String nnp : npp.keySet()) {
                    System.out.println(edge_count + "A: " + h + " B: " + npp.get(nnp).getLo0());
                    if(graph.getEdge(npp.get(nnp).getLo0() + " " + h) == null && graph.getEdge(h + " " + npp.get(nnp).getLo0()) == null && h != "0.0.0.0" && npp.get(nnp).getLo0() != "0.0.0.0") {
                        graph.addEdge(npp.get(nnp).getLo0() + " " + h, h, npp.get(nnp).getLo0());
                        Node n = graph.getNode(npp.get(nnp).getLo0());
                        n.addAttribute("ui.label", npp.get(nnp).getLo0() + ":" + npp.get(nnp).getSystemName());
                    }
                    edge_count++;
                }
        }

        graph.display();
    }
}
