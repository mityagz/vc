package Models;

import com.sonalake.utah.config.Config;
import com.sonalake.utah.config.ConfigLoader;
import datastruct.NNode;
import datastruct.NodeParse0;
import net.juniper.netconf.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import com.sonalake.utah.*;

/**
 * Created by mitya on 1/9/20.
 */
public class JuniperMX {
    private String configRes0 = "conf/juniper.lldp_neib.config.xml";
    private String configRes1 = "conf/juniper.lldp_neib.config1.xml";
    private String configRes2 = "conf/juniper.lldp_neib.config2.xml";
    private String lldp_out;
    private LinkedList<NNode> nNodes;
    private Map<String, NodeParse0> np00;
    private Map<String, NodeParse0> np01;

    // rnp
    // hashmap<ip_addr, hashmap<peerName(hostname), nodeparse0>>
    //  ^
    //  |
    //public NodeParse0(String systemName, String ifI, String ifO, String chassisId, String lo0) {
    //    this.ifI = ifI;
    //    this.ifO = ifO;
    //    this.chassisId = chassisId;
    //    this.systemName = systemName;
    //    this.lo0 = lo0;
    //}

    Map<String, Map<String, NodeParse0>> rnp = new HashMap<String, Map<String, NodeParse0>>();
    Map<String, Integer> LoopToNodeId = new HashMap<String, Integer>();
    Map<Integer, String> NodeIdToLoop = new HashMap<Integer, String>();
    Map<String, String> LoopToHostname = new HashMap<String, String>();
    Map<String, Boolean> marked = new HashMap<String, Boolean>();


    private int node = 0;

    public JuniperMX(String ip_addr, CommandLine a) throws IOException, SAXException {
        d(ip_addr);
        showResult(a);
    }

    public JuniperMX(ArrayList<String> a) throws IOException, SAXException {
        dd(a);
    }

    public Map<String, Map<String, NodeParse0>> getRnp() { return rnp; }

    private void showResult(CommandLine a) {
        for (String h : rnp.keySet()) {
            Map<String, NodeParse0> npp = rnp.get(h);
            if(a.hasOption("h")) {
                System.out.println(h + ":" + LoopToHostname.get(h));
            } else {
                System.out.println(h + ":");
                for (String nnp : npp.keySet()) {
                    System.out.println(" " + npp.get(nnp));
                }
            }
        }
    }

    private void dd(ArrayList<String> a) {
        // rnp
        // hashmap<ip_addr, hashmap<peerName(hostname), nodeparse0>>
        //  ^
        //  |
        //public NodeParse0(String systemName, String ifI, String ifO, String chassisId, String lo0) {
        //    this.ifI = ifI;
        //    this.ifO = ifO;
        //    this.chassisId = chassisId;
        //    this.systemName = systemName;
        //    this.lo0 = lo0;
        //}

        int i = 0;


        for (i = 0; i < a.size() - 1; i++) {
            String[] ss = a.get(i).split(":");
            if(rnp.get(ss[1]) == null)
                rnp.put(ss[1], new HashMap<String, NodeParse0>());
        }

        for(i = 0; i < a.size() - 1; i++) {
            String []ss = a.get(i).split(":");
            if(ss[4] != null || ss[4] != "null") {
                System.out.println("SS: " + ss[1] + ":" + ss[4] + ":" + ss[3] + ":" + ss[2] + " mac " + ss[5]);
                NodeParse0 nnp = new NodeParse0(ss[4], ss[3], ss[2], "mac", ss[5]);
                //!!!!!!!!!!!!!!nnp.setNode();
                rnp.get(ss[1]).put(ss[4], nnp);
            }
        }
        System.out.println("DD: cnt: " + i);


        for (String key : rnp.keySet()) {
            for (String key2 : rnp.get(key).keySet())
                System.out.println("key: " + key + " key2: "  + key2  + " : " + rnp.get(key).get(key2));

        }
    }

    private void d(String ip_addr) throws IOException, SAXException {
        Device device = null;
        LoopToNodeId.put(ip_addr, node);
        NodeIdToLoop.put(node, ip_addr);
        if(ip_addr.contains("0.0.0.0")) return;
        if(marked.containsKey(ip_addr)) return;
        marked.put(ip_addr, true);

        try {
            device = new Device(ip_addr, "am", "qwerty", null);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            device.connect();;
        } catch (NetconfException ne) {
            System.out.println("Connect to: " + ip_addr);
        }
        //device.connect();
        boolean isLocked = device.lockConfig();
        if(!isLocked) {
            System.out.println("Could not lock configuration. Exit now.");
            return;
        }

        lldp_out = device.runCliCommand("show version");
        lldp_parse2(ip_addr, lldp_out);

        lldp_out = device.runCliCommand("show lldp neighbors");
        np00 = lldp_parse0(ip_addr, lldp_out);

        for (String nnp : np00.keySet()) {
            lldp_out = device.runCliCommand("show lldp neighbors interface " + np00.get(nnp).getIfI());
            lldp_parse1(ip_addr, lldp_out, np00, np00.get(nnp).getSystemName());
        }

        rnp.put(ip_addr, np00);

            for (String nnp : rnp.get(ip_addr).keySet()) {
                d(rnp.get(ip_addr).get(nnp).getLo0());
            }

        device.unlockConfig();
        device.close();
    }

    private Map<String, NodeParse0> lldp_parse1(String ip, String in_parse, Map<String, NodeParse0> np01, String systemName) {
        URL cURL = Thread.currentThread().getContextClassLoader().getResource(configRes1);
        List<Map<String, String>> lldpValues = new ArrayList<Map<String, String>>();
        /*
        Management address
        Address Type      : IPv4(1)
        Address           : 10.229.128.0
        Interface Number  : 0
        Interface Subtype : Unknown(1)
        OID               : 1.3.6.1.2.1.31.1.1.1.1.0.
         */

        try {
            Config cfg = new ConfigLoader().loadConfig(cURL);
            InputStream is = new ByteArrayInputStream(in_parse.getBytes(Charset.forName("UTF-8")));
            BufferedReader ir = new BufferedReader(new InputStreamReader(is));
            Parser parser = Parser.parse(cfg, ir);
            while (true) {
                Map<String, String> record = parser.next();
                if(record == null)
                    break;
                else if(record.get("IPv4") != null) {
                    lldpValues.add(record);
                    np01.get(systemName).setLo0(record.get("IPv4"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return np01;
    }

    private Map<String, NodeParse0> lldp_parse0(String ip, String in_parse) throws IOException {
        URL cURL = Thread.currentThread().getContextClassLoader().getResource(configRes0);
        List<Map<String, String>> lldpValues = new ArrayList<Map<String, String>>();
        Map<String, NodeParse0> np0 = new HashMap<String, NodeParse0>();
        try {
            Config cfg = new ConfigLoader().loadConfig(cURL);
            InputStream is = new ByteArrayInputStream(in_parse.getBytes(Charset.forName("UTF-8")));
            BufferedReader ir = new BufferedReader(new InputStreamReader(is));
            Parser parser = Parser.parse(cfg, ir);
            while (true) {
                Map<String, String> record = parser.next();
                if(record == null)
                    break;
                else lldpValues.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Map<String, String> s : lldpValues) {
            np0.put(s.get("peerName"), new NodeParse0(s.get("peerName"), s.get("ifI"), s.get("ifO"), s.get("mac"), "0.0.0.0"));
            np0.get(s.get("peerName")).setNode(node++);
        }

        return np0;
    }

    private void lldp_parse2(String ip, String in_parse) throws IOException {
    // show system information
    // show version
    // Hostname: PE0
    URL cURL = Thread.currentThread().getContextClassLoader().getResource(configRes2);
    List<Map<String, String>> lldpValues = new ArrayList<Map<String, String>>();

        try {
            Config cfg = new ConfigLoader().loadConfig(cURL);
            InputStream is = new ByteArrayInputStream(in_parse.getBytes(Charset.forName("UTF-8")));
            BufferedReader ir = new BufferedReader(new InputStreamReader(is));
            Parser parser = Parser.parse(cfg, ir);
            while (true) {
                Map<String, String> record = parser.next();
                if(record == null)
                    break;
                else {
                    lldpValues.add(record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Map<String, String> s : lldpValues) {
            if(s.get("Hostname") != null) {
                LoopToHostname.put(ip, s.get("Hostname"));
            }
        }
    }
}