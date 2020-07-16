package ru.hypernet.SNMP;

import ru.hypernet.SNMP.SNMPHelper;

public class Vlan {
	//int vlan_create(String host, String community,String vlan_name, int vlan_id)
	//int vlan_delete(String host, String community, int vlan_id)
	//int vlan_get(String host, String community,int vlan_id)
	
	SNMPHelper h =  new SNMPHelper();
		
	public int vlan_create(String host, String community,String vlan_name, int vlan_id){
        int res = 0;
        int op = 4;
        String oid0 = "1.3.6.1.2.1.17.7.1.4.3.1.1." + vlan_id;
        String oid1 = "1.3.6.1.2.1.17.7.1.4.3.1.2." + vlan_id;
        String oid2 = "1.3.6.1.2.1.17.7.1.4.3.1.4." + vlan_id;
        String oid3 = "1.3.6.1.2.1.17.7.1.4.3.1.5." + vlan_id;
        h.snmpSet(host, community, oid0, vlan_name,oid1, "00000000000000000000000000000000", oid2, "00000000000000000000000000000000", oid3, op );
        //h.snmpSet(host, community, oid1, "00000000000000000000000000000000");
        //h.snmpSet(host, community, oid2, "00000000000000000000000000000000");
        //h.snmpSet(host, community, oid3, op);
        return res;
}

	public int vlan_delete(String host, String community, int vlan_id){
        int res = 0;
        int op = 6;
        String base_oid = "1.3.6.1.2.1.17.7.1.4.3.1.5.";
        String oid = base_oid + vlan_id;
        h.snmpSet(host, community, oid, op);
        return res;
}

	public  String vlan_get(String host, String community,int vlan_id){
        String base_oid = "1.3.6.1.2.1.17.7.1.4.3.1.1.";
        String oid = base_oid + vlan_id;
        return h.snmpGet(host, community, oid);
	}

	public static void main(String str[]){
		Vlan v = new Vlan();
		Helper he = new Helper();
		Port p = new Port();
		//v.vlan_delete("10.254.95.163", "private", 222);
		//System.out.println(v.vlan_get("10.2.1.33", "private", 222));
		/*
		if(v.vlan_get("10.254.95.163", "private", 222).length() == 0){
			v.vlan_create("10.254.95.163", "private", "HN-TEST", 222);
			p.set_port_vlan_tag("10.254.95.163", "private", 5, 222);
			p.set_port_vlan_untag("10.254.95.163", "private", 5, 222);
			p.set_port_vlan_tag("10.254.95.163", "private", 6, 222);
		}
		*/
		
		//System.out.println(he.get_sysdescr("10.2.1.33", "private"));
		//
		// To do: Revised return value methods.
		//
		/* Test for switch model:
			DGS-3120+
			DES-3526+
			DES-3200-10+
			GDS-3612-
			DGS-3312-
			DES-3028-
			DES-3052-
			DES-3010G-
		*/	
	}
}
