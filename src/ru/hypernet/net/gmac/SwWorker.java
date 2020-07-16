package ru.hypernet.net.gmac;

import ru.hypernet.SNMP.Helper;
import ru.hypernet.SNMP.SNMPHelper;
import ru.hypernet.SNMP.SNMPWalk;

import javax.xml.stream.events.StartDocument;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.sql.*;

/**
 * Created by mitya on 6/8/16.
 */
public class SwWorker implements Runnable {
    private  String ip_addr;
    private ArrayList<String> mac_port;
    private ArrayList<String> untagPort;
    private ArrayList<String> tagPort;
    HashMap<Integer, ArrayList<Integer>> tagPortVlan;
    HashMap<Integer, ArrayList<Integer>> untagPortVlan;
    HashMap<Integer, Integer> portType;
    private Connection con;
    private int sw_id;

    SwWorker(String ip_addr, Integer sw_id){ this.ip_addr = ip_addr;this.sw_id = sw_id; }
    @Override
    public void run() {
        try {
            Thread.sleep(1000*60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // get system type
        String Oid = ".1.3.6.1.2.1.1.1.0";
        SNMPHelper snmpHelper = new SNMPHelper();
        //System.out.println(snmpHelper.snmpGet(ip_addr, "public", Oid));
        //System.out.println(this.getClass().getCanonicalName());


        // get port vlan
        try {
            SNMPWalk snmpWalk0 = new SNMPWalk();
            tagPort = snmpWalk0.doSnmpwalk(ip_addr, "public", ".1.3.6.1.2.1.17.7.1.4.3.1.2");
            untagPort = snmpWalk0.doSnmpwalk(ip_addr, "public", ".1.3.6.1.2.1.17.7.1.4.3.1.4");
        } catch (IOException e){

        }

        // get fdb
        try {
            SNMPWalk snmpWalk = new SNMPWalk();
            mac_port = snmpWalk.doSnmpwalk(ip_addr, "public", ".1.3.6.1.2.1.17.7.1.2.2.1.2");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<String> i_mac_port = mac_port.iterator();
        /* while(i_mac_port.hasNext()){
            System.out.println(i_mac_port.next());
        }
        */

        synchronized (System.out) {
            connectionDb(); // get db hasndler
            swPortCreate(); // port create
            swPortVlan(); // get vlan port
            swPortData(); // add fdb to db
        }
    }

    //
    private void connectionDb(){
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/randid";
            String name = "randid";
            String password = "";

            try {
                con = DriverManager.getConnection(url,name,password);
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (ClassNotFoundException e){
                System.out.println(e);
        }
    }

    //
    public void swPortCreate(){
        Integer port_create, nport, bport, eport, insertPort = 0;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT sw.port_created, device.nports, device.bport, device.eport " +
                                            "FROM sw, device " +
                                            "WHERE device.id = sw.id_device " +
                                            "AND sw.id = " + sw_id);
            if(rs.next()) {
                port_create = rs.getInt(1);
                nport = rs.getInt(2);
                bport = rs.getInt(3);
                eport = rs.getInt(4);

                if (port_create == 0) {
                    for(int p = bport; p <= nport; p++){
                        String port =   "INSERT INTO " +
                                        "sw_ports (id_sw, ifindex, nport, service_id, state, monitor) " +
                                        "VALUES (" + sw_id + ", " + p + " , " +  p + " ,  5, 0, 0)";
                        insertPort += st.executeUpdate(port);
                    }
                    if(insertPort == nport){
                        String port_created =   "UPDATE sw set port_created = 1 " +
                                                "WHERE id = " + sw_id;
                        st.executeUpdate(port_created);
                        System.out.println("Ports " + insertPort + " is created");
                    }else {
                        System.out.println("insertPort don't equal nport: " + insertPort + " != " + nport);
                    }
                } else {
                    //System.out.println("Ports already created");
                }
            }

        } catch (SQLException e){
            System.out.println(e);
        }
    }

    //
    private static void mapPortVlan(Integer vlan, String portmap, HashMap<Integer, ArrayList<Integer>> portVlanMap){
            int lenPortMap = portmap.split(":").length;
            String [] portmapStr = portmap.split(":");
            String [] portmapStrBin = new String[lenPortMap];
            int padding = 0;

            for(int i = 0; i < lenPortMap; i++) {
                portmapStrBin[i] = Long.toBinaryString(Long.parseLong(portmapStr[i], 16));
                 if((padding = ( 8 - portmapStrBin[i].length())) > 0){
                    for(int j = 0; j < padding; j++){
                        portmapStrBin[i] = "0" + portmapStrBin[i];
                    }
                }
            }

            int indexPort = 1;
            for(int i = 0; i < portmapStrBin.length; i++){
                for(int j = 0; j < portmapStrBin[i].length(); j++){
                    if(Integer.parseInt(portmapStrBin[i].substring(j, j + 1)) == 1){
                        if(portVlanMap.get(indexPort) == null){
                            portVlanMap.put(indexPort, new ArrayList<Integer>());
                            portVlanMap.get(indexPort).add(vlan);
                        }else{
                            portVlanMap.get(indexPort).add(vlan);
                        }
                    }
                    indexPort++;
                }
            }
    }

    // 0 - unknown
    // 1 - trunk
    // 2 - tag
    // 3 - access
    private void swPortType(){
        portType = new HashMap<Integer, Integer>();
        for(Map.Entry p : tagPortVlan.entrySet()) {
            Integer pr = (Integer) p.getKey();
            if (tagPortVlan.get(pr).size() > 1) {     // Its trunk port
                portType.put(pr, 1);
            } else if (((tagPortVlan.get(pr) != null) && (untagPortVlan.get(pr) != null)) && (tagPortVlan.get(pr).size() == 1 && untagPortVlan.get(pr).size() == 1) && (tagPortVlan.get(pr).get(0).equals(untagPortVlan.get(pr).get(0)))) {
                portType.put(pr, 3);
            } else {
                portType.put(pr, 2);
            }
        }

        try {
            Statement st = con.createStatement();
            Integer port_id = 0;

                    for (Map.Entry p : portType.entrySet()) {
                        String port_id_sql = "SELECT id " +
                                "FROM sw_ports  " +
                                "WHERE id_sw = " + sw_id +
                                "AND service_id=5 " +
                                "AND  nport = " + p.getKey();
                        ResultSet rs = st.executeQuery(port_id_sql);
                        if (rs.next()) {
                            port_id = rs.getInt(1);
                            String sw_ports_type =  "UPDATE  sw_ports " +
                                                    "SET port_type=" + p.getValue() +
                                                    "WHERE id=" + port_id;
                            st.executeUpdate(sw_ports_type);
                        }
                    }
            }catch(SQLException e){
                System.out.println(e);
            }
    }

    //
    public void swPortVlan(){
        HashMap<Integer, String []> vlanTagBitPort = new HashMap<>();
        HashMap<Integer, String []> vlanUnTagBitPort = new HashMap<>();
        tagPortVlan = new HashMap<>();
        untagPortVlan = new HashMap<>();

        System.out.println("IP sw: " + ip_addr);

        // <vlanid, hexbitmap_ports>
        //1.3.6.1.2.1.17.7.1.4.3.1.2.1 : OCTET STRING : 00:00:00:00

        String bimapHexPortStr;
        String vlanOid [];
        Integer vlan;
        for(String s : tagPort) {
            //System.out.println(s);
            vlanOid = (s.split(" : ")[0]).split("\\.");
            vlan = Integer.parseInt(vlanOid[vlanOid.length - 1]);
            bimapHexPortStr = s.split(" : ")[2];
            //System.out.println("Vlan : bitmapHexPort " + vlan + " | " + bimapHexPortStr);
            mapPortVlan(vlan, bimapHexPortStr, tagPortVlan);
        }

        for(String s : untagPort) {
            //System.out.println(s);
            vlanOid = (s.split(" : ")[0]).split("\\.");
            vlan = Integer.parseInt(vlanOid[vlanOid.length - 1]);
            bimapHexPortStr = s.split(" : ")[2];
            //System.out.println("Vlan : bitmapHexPort " + vlan + " | " + bimapHexPortStr);
            mapPortVlan(vlan, bimapHexPortStr, untagPortVlan);
        }

        /*
        for(Map.Entry p : tagPortVlan.entrySet()){
            Integer pr = (Integer) p.getKey();
            Iterator ivlan = tagPortVlan.get(pr).iterator();
            System.out.println("TagPort " + pr + " vlan:");
                while (ivlan.hasNext()){
                    System.out.print(ivlan.next() + ",");
                }
            System.out.println();
        }

        for(Map.Entry p : untagPortVlan.entrySet()){
            Integer pr = (Integer) p.getKey();
            Iterator ivlan = untagPortVlan.get(pr).iterator();
            System.out.println("UntagPort " + pr + " vlan:");
                while (ivlan.hasNext()){
                    System.out.print(ivlan.next() + ",");
                }
            System.out.println();
        }
        */
        swPortType();

        for(Map.Entry p : portType.entrySet()){
            //System.out.println("Port: " + p.getKey() + " PortType:  " + p.getValue());
        }

    }

    //
    public  void swPortData() {
        HashMap<String, HashMap<Integer, ArrayList<String>>> swPort = null;
        // ip -> port -> vid -> mac
        // ip -> port -> { mac } , this working struct
        // ip -> port -> { mac:vlan }
        // VlanMac vm = new VlanMac();
        Integer port = 0;
        Integer vid = 0;
        String mac = "";
        //ip                port                    vid                 mac
        //HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>> swPort = new HashMap<>();
        //swPort.get(ip_addr).put((new HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>>()).put(port, (new HashMap<Integer, ArrayList<String>>()).put(vid, (new ArrayList<String>()))));
        //(new HashMap<>()).put(port, (new HashMap<>()).put(vid, (new ArrayList<String>()).add(mac)));
        //(new HashMap<>()).put(port, (new HashMap<Integer, ArrayList<String>>()).put(vid, (new ArrayList<String>()).add(mac)));
        //HashMap<String, HashMap<Integer, HashMap<Integer, ArrayList<String>>>> swPort = new HashMap<>();

        swPort = new HashMap<>();
        Iterator<String> i_mac_port = mac_port.iterator();
        while (i_mac_port.hasNext()) {
            String snmpRes = i_mac_port.next();
            String snmpResP0;
            String[] asnmpResP0;

            snmpResP0 = (snmpRes.split(":")[0]).replaceAll(" ", "");
            asnmpResP0 = snmpResP0.split("\\.");
            Integer[] macDec = {Integer.parseInt(asnmpResP0[14]), Integer.parseInt(asnmpResP0[15]), Integer.parseInt(asnmpResP0[16]), Integer.parseInt(asnmpResP0[17]), Integer.parseInt(asnmpResP0[18]), Integer.parseInt(asnmpResP0[19])};
            vid = Integer.parseInt(asnmpResP0[13]);
            port = Integer.parseInt((snmpRes.split(":")[2]).replaceAll(" ", ""));

            mac = "";
            for (int j = 0; j < macDec.length; j++) {
                mac += (Helper.dec2hex(macDec[j]));
                if (j != macDec.length - 1) mac += "-";
            }

            //System.out.println("port vlan mac: " + port + ":" + vid + ":" + mac);

            if (swPort.get(ip_addr) == null) {
                HashMap<Integer, ArrayList<String>> rport = new HashMap<>();
                swPort.put(ip_addr, rport);
            }
            if (swPort.get(ip_addr).get(port) == null) {
                ArrayList<String> rmac = new ArrayList<String>();
                rmac.add(mac);
                swPort.get(ip_addr).put(port, rmac);
            }
            if (!swPort.get(ip_addr).get(port).contains(mac)) {
                swPort.get(ip_addr).get(port).add(mac);
            }
        }

        try {
            Statement st = con.createStatement();
            Integer port_id = 0;
            for (Map.Entry eip : swPort.entrySet()) {
                String ip = (String) eip.getKey();
                System.out.println("IP sw: " + ip);
                for (Map.Entry eport : swPort.get(ip).entrySet()) {
                    //System.out.println("Sw port: " + eport.getKey());
                    Integer p = (Integer) eport.getKey();
                    for (String emac : swPort.get(ip).get(p)) {
                        String port_id_sql = "SELECT id " +
                                "FROM sw_ports  " +
                                "WHERE id_sw = " + sw_id +
                                "AND  nport = " + p;
                        ResultSet rs = st.executeQuery(port_id_sql);
                         if(rs.next()) {
                             port_id = rs.getInt(1);
                             //System.out.println("Mac: " + emac + " : Port " + p + " PortId: " + port_id);
                             String sw_mac = "INSERT INTO sw_mac(sw_ports_id, mac_addr)" +
                                             "VALUES(" + port_id + ", '" + emac + "')";
                             st.executeUpdate(sw_mac);
                         }
                    }
                    //System.out.println("---------------------");
                }
                //System.out.println();
                //System.out.println("---------------------");
            }
            }catch(SQLException e){
                System.out.println(e);
            }


    }

    //
    private class VlanMac {
        private ArrayList<String> fdb;
        private Integer vid;

        VlanMac(Integer vid) {
            fdb = new ArrayList<String>();
            this.vid = vid;
        }

        public void addMac(String mac){
            fdb.add(mac);
        }
    }
}
