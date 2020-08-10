package datastruct;

import Models.JuniperMX;
import db.RDB;
import db.WDB;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.DirectedEdge;
import lldp.DetectType;
import org.apache.commons.cli.*;
import org.snmp4j.*;
import edu.princeton.cs.*;
import datastruct.*;

import java.io.IOException;
import java.util.*;

import org.apache.commons.*;

import org.xml.sax.SAXException;
import ru.hypernet.SNMP.*;
import net.juniper.netconf.*;

/**
 * Created by mitya on 1/8/20.
 */

/*

public class adj {
    HashMap<String, Bag<DirectedEdge>> adj = new HashMap<String, Bag<DirectedEdge>>();
}

*/
public class Adj {
    private Bag<NDirectedEdge> []adj0;
    private NNode []adj1;
    private HashMap<String, NNode> adj2;
    private HashMap<String, Bag<NDirectedEdge>> adj3;
    private  String ip_addr;
    private String system_type;
    private JuniperMX d;

    //get system type
    String Oid_get_system_type = ".1.3.6.1.2.1.1.1.0";

    /* snmp discover NNode
       dfs discover
    */

    Adj(String ip_addr, CommandLine a) throws IOException, SAXException {
        //DetectType type = new DetectType(ip_addr);
        //if (type.getSystem_type() == 1) {
            d = new JuniperMX(ip_addr, a);
        //}
    }

    Adj(ArrayList<String> a) throws IOException, SAXException {
        d = new JuniperMX(a);
    }

    public JuniperMX getJ() { return d; }

    public static void main(String [] args) throws IOException, SAXException {
        Options options = new Options();
        Option hosts = new Option("h", "hosts");
        hosts.setRequired(false);
        options.addOption(hosts);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(args[0], options);
            System.exit(1);
        }

        /*
        Adj a = new Adj("10.229.0.0", cmd);
        SymGraph sg = new SymGraph(a);
        new VisualG(a);
        new WDB(a, sg);
        */


        Adj a = null;
        RDB rdb = new RDB(a);
        a = new Adj(rdb.getA());
        Map<String, Map<String, NodeParse0>> aa = a.getJ().getRnp();
        SymGraph ssg = new SymGraph(a);
        for(String sa : aa.keySet()) {
            System.out.println("SA: " + sa);
            for(String ssa : aa.get(sa).keySet()) {
                System.out.println(ssa);
                System.out.println(aa.get(sa).get(ssa).getLo0());
                System.out.println(aa.get(sa).get(ssa).getSystemName());
            }
            System.out.println();
        }
        new VisualG(a);

    }
}
