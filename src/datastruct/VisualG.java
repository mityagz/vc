package datastruct;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.util.Map;

/**
 * Created by mitya on 4/26/20.
 */
//public class VisualG implements ViewerListener {
public class VisualG {
    protected boolean loop = true;

    int edge_count = 0;
    Graph graph = new SingleGraph("VC");
    Map<String, Map<String, NodeParse0>> rnp;
    private String styleSheet =
			"graph {"+
			"	canvas-color: white;"+
			"		fill-mode: gradient-radial;"+
			"		fill-color: white, #EEEEEE;"+
			"		padding: 60px;"+
			"	}"+
			"node {"+
			"	size-mode: dyn-size;"+
			"	shape: circle;"+
			"	size: 20px;"+
			"	fill-mode: plain;"+
			"	fill-color: #CCC;"+
			"	stroke-mode: plain;"+
			"	stroke-color: black;"+
			"	stroke-width: 1px;"+
			"}";

    VisualG(Adj adj) {
        System.setProperty("org.graphstream.ui", "swing");

        rnp = adj.getJ().getRnp();

        graph.addAttribute("ui.stylesheet", styleSheet);

        for (String h : rnp.keySet()) {
            Map<String, NodeParse0> npp = rnp.get(h);
            Node n = graph.addNode(h);
            n.addAttribute("ui.label", h);
        }

        for (String h : rnp.keySet()) {
            Map<String, NodeParse0> npp = rnp.get(h);
            for (String nnp : npp.keySet()) {
                System.out.println(edge_count + "A: " + h + " B: " + npp.get(nnp).getLo0());
                if (graph.getEdge(npp.get(nnp).getLo0() + " " + h) == null && graph.getEdge(h + " " + npp.get(nnp).getLo0()) == null && h != "0.0.0.0" && npp.get(nnp).getLo0() != "0.0.0.0") {
                    graph.addEdge(npp.get(nnp).getLo0() + " " + h, h, npp.get(nnp).getLo0());
                    Node n = graph.getNode(npp.get(nnp).getLo0());
                    n.addAttribute("ui.label", npp.get(nnp).getLo0() + ":" + npp.get(nnp).getSystemName());
                }
                edge_count++;
            }
        }

        Viewer viewer = graph.display();
    }
        /*
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);


        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(graph);


        while(loop) {
            fromViewer.pump();
        }
    }

    public void viewClosed(String id) {
        loop = false;
    }

    public void buttonPushed(String id) {
        System.out.println("Button pushed on node " + id);
    }

    public void buttonReleased(String id) {
        System.out.println("Button released on node " + id);
    }

    public void mouseOver(String id) {
        System.out.println("Need the Mouse Options to be activated");
    }

    public void mouseLeft(String id) {
        System.out.println("Need the Mouse Options to be activated");
    }
    */
}
