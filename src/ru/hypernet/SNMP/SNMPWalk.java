package ru.hypernet.SNMP;

/**
 * Created by mitya on 6/8/16.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeUtils;
import org.snmp4j.util.TreeEvent;

public class SNMPWalk {
  private String targetAddr;
  private String oidStr;
  private String commStr;
  private int snmpVersion;
  private String portNum;
  private String usage;

    public SNMPWalk() throws IOException {
        // Set default value.
        targetAddr = null;
        oidStr = null;
        commStr = "public";
        snmpVersion = SnmpConstants.version2c;
        portNum = "161";
    }

    public ArrayList<String> doSnmpwalk(String ip_addr, String commStr, String Oid) throws IOException {
        ArrayList<String> res = new ArrayList<String>();
        this.targetAddr = ip_addr;
        this.commStr = commStr;
        this.oidStr = Oid;
        Address targetAddress = GenericAddress.parse("udp:" + targetAddr + "/" + portNum);
        TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();

        // setting up target
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(commStr));
        target.setAddress(targetAddress);
        target.setRetries(3);
        target.setTimeout(1000 * 3);
        target.setVersion(snmpVersion);
        OID oid = new OID(oidStr);

        // Get MIB data.
        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(target, oid);
        if (events == null || events.size() == 0) {
            System.out.println("No result returned.");
            System.exit(1);
        }

        // Handle the snmpwalk result.
        for (TreeEvent event : events) {
            if (event == null) {
                continue;
            }
            if (event.isError()) {
                System.err.println("oid [" + oid + "] " + event.getErrorMessage());
                continue;
            }

            VariableBinding[] varBindings = event.getVariableBindings();
            if (varBindings == null || varBindings.length == 0) {
                continue;
            }
            for (VariableBinding varBinding : varBindings) {
                if (varBinding == null) {
                    continue;
                }
                /*
                System.out.println(
                        varBinding.getOid() +
                                " : " +
                                varBinding.getVariable().getSyntaxString() +
                                " : " +
                                varBinding.getVariable());
                                */
                res.add(varBinding.getOid() + " : " + varBinding.getVariable().getSyntaxString() + " : " + varBinding.getVariable());
            }
        }
        snmp.close();
        return res;
    }
/*
    public static void main(String[] args){
    try{
      Snmpwalk snmpwalk = new Snmpwalk();
      snmpwalk.checkAndSetArgs(args);
      snmpwalk.doSnmpwalk();
    }
    catch(Exception e){
      System.err.println("----- An Exception happend as follows. Please confirm the usage etc. -----");
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }
*/
}
