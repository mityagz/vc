package ru.hypernet.net.gmac;

/**
 * Created by mitya on 6/7/16.
 */

import org.postgresql.Driver;
import org.snmp4j.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import ru.hypernet.SNMP.*;

public class GaterMac {
    public static void main(String [] args){
        Sw sw = new Sw();
        sw.getSwDb();

        LinkedList<SwType> s = new LinkedList<SwType>();
        s = sw.getSw();

        Iterator<SwType> isw = s.iterator();
        while(isw.hasNext()) {
            SwType swt = isw.next();
            //System.out.println(swt.getIp() + ":" + swt.getType() + ":" + swt.getId());
            //}
            String ip = swt.getIp();
            Integer sw_id = swt.getId();
            //if(ip.equals("10.254.255.115") || ip.equals("10.254.255.116") || ip.equals("10.254.255.117") || ip.equals("10.254.255.119")|| ip.equals("10.2.1.29")|| ip.equals("10.254.255.229")) {
                Thread th = new Thread(new SwWorker(ip, sw_id));
                th.setName("SwWorker_" + ip);
                th.start();
        }
    }
}
