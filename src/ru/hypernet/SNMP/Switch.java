package ru.hypernet.SNMP;

import java.util.*;
import java.util.Map.*;

//import DB.*;

public class Switch {
public	  int id;
public    int id_device;
public    String mac_address;
public    String ip_address;
public    String system_name;
public    String system_location;
public    String vlant;
public    String vlanm;
public    String vlanu;
public    int id_acct;
public    int backup;
public    int icmpstate;
public    int monitor;
public    String prev_sw;
public    int prev_port;
public    String next_sw;
public    int next_port;
public 	  ArrayList<Switch_Neighbors> Neighbors;
    
    public Switch(int id_p, int id_device_p, String mac_address_p, String
    		ip_address_p,  String system_name_p,
            String system_location_p, String vlant_p,  String vlanm_p, 
            String vlanu_p, int id_acct_p,
            int backup_p, int icmpstate_p, int monitor_p,  String
            prev_sw_p, int prev_port_p,  String next_sw_p, int next_port_p, 
            ArrayList<Switch_Neighbors>Neighbors_p){
         id = id_p;
         id_device = id_device_p;
         mac_address = mac_address_p;
         ip_address = ip_address_p;
         system_name = system_name_p;
         system_location = system_location_p;
         vlant = vlant_p;
         vlanm = vlanm_p;
         vlanu = vlanu_p;
         id_acct = id_acct_p;
         backup = backup_p;
         icmpstate = icmpstate_p;
         monitor = monitor_p;
         prev_sw = prev_sw_p;
         prev_port = prev_port_p;
         next_sw = next_sw_p;
         next_port = next_port_p;
         Neighbors = Neighbors_p;
    }
    
    /*
    public static void main(String args[]) throws Exception{
		System.out.println("From main thread");
		TreeMap<String, Switch> sw = new TreeMap<String, Switch>();
		DBQuery dbq = new DBQuery(sw);
		
		try{
			dbq.t.join();
		}catch(InterruptedException e){
			System.out.println(dbq.t.getName() + " is break");
		}
		
		
		Set<Entry<String, Switch>> sw_set0  = sw.entrySet();
		int i = 0;
		for(Entry<String, Switch> s : sw_set0){
			System.out.println(s.getKey() + ":" + sw.get(s.getKey()).ip_address + ":" + 
								sw.get(s.getKey()).system_name + ":" +
								sw.get(s.getKey()).system_location + ":" +
								sw.get(s.getKey()).prev_sw + ":" + sw.get(s.getKey()).next_sw);
			i++;
		}
		System.out.println(i);
		
	}
	*/
}
