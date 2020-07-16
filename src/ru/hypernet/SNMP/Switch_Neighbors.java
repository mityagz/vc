package ru.hypernet.SNMP;

public class Switch_Neighbors {
		public int id;
		public int id_sw;
		public String mac_address;
		public String ip_address;
		public String system_name;
		public String system_location;
		public int port;
		public Switch_Neighbors(int id_sw_p, int port_p,  String ip_address_p){
			///int id = id_p; 
			id_sw = id_sw_p; 
			//String mac_address = mac_address_p; 
			ip_address = ip_address_p;
			//String system_name = system_name_p;
			//String system_location = system_location_p; 
			//int port = port_p;
			port = port_p;
		}
}
