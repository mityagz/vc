package ru.hypernet.SNMP;

import ru.hypernet.SNMP.SNMPHelper;
import ru.hypernet.SNMP.Helper;
import org.snmp4j.smi.*;

@SuppressWarnings("unused")
public class Port {
	SNMPHelper hp =  new SNMPHelper();
	
	int set_port_speed(String host, int n_port, float tx_speed, float rx_speed) {
	/*
	#set port speed by tariff set_port_speed(ip_switch, n_port, tx_speed, rx_speed);
	my $hostname = shift @_;
	my $n_port = shift @_;
	my $tx_speed = shift @_;
	my $rx_speed = shift @_;
	my $result;
	my @oidlist;
	my $oidlist;
	my $community = "private";
	my $swL2QOSBandwidthTxRate = "1.3.6.1.4.1.171.11.64.1.2.6.1.1.2";
	my $swL2QOSBandwidthRxRate = "1.3.6.1.4.1.171.11.64.1.2.6.1.1.3";
	my ($session, $error) = Net::SNMP->session(-hostname      => $hostname,
											   -community     => $community);
		if(!defined($session)){
			printf("ERROR: %s.\n", $error);
			exit 1;
		}
		@oidlist = ();

	            $swL2QOSBandwidthTxRate = $swL2QOSBandwidthTxRate.".".$n_port;
	            $oidlist = [($swL2QOSBandwidthTxRate, 2, $tx_speed)];

		$result = $session->set_request(                                                                        
	                          -varbindlist      => $oidlist                                              
	                      );                                                                            
		@oidlist = ();
	            $swL2QOSBandwidthRxRate = $swL2QOSBandwidthRxRate.".".$n_port;
	            $oidlist = [($swL2QOSBandwidthRxRate, 2, $rx_speed)];

		$result = $session->set_request(                                                                        
	                          -varbindlist      => $oidlist                                              
	                      );                                                                            

	   if(!defined($result)){                                                                                 
	    printf("ERROR: %s.\n", $session->error);                                                       
	    $session->close;                                                                               
	    exit 1;                                                                                        
	   }                                                                                                       

	   $session->close;                                                                                        
	   return($result->{$swL2QOSBandwidthRxRate});
	   */
		return 0;
	}

	int  set_port_description(String host, int n_port, String n_description) {
	/*
	#snmpset -v2c -c private 192.168.0.1  1.3.6.1.4.1.171.11.64.1.2.4.2.1.9.1 s test
	#set port description	 set_port_description(ip_switch, n_port, n_description);
	my $hostname = shift @_;
	my $n_port = shift @_;
	my $n_description = shift @_;
	my $result;
	my @oidlist;
	my $oidlist;
	my $community = "private";
	my $swL2PortCtrlDescription = "1.3.6.1.2.1.31.1.1.1.18";
	my ($session, $error) = Net::SNMP->session(-hostname      => $hostname,
	                                           -community     => $community);
		if(!defined($session)){
	        printf("ERROR: %s.\n", $error);
	        exit 1;
	    }
	    @oidlist = ();

	            $swL2PortCtrlDescription = $swL2PortCtrlDescription.".".$n_port;
	            $oidlist = [($swL2PortCtrlDescription, 4, $n_description)];

	    $result = $session->set_request(
	                          -varbindlist      => $oidlist
	                      );

	   if(!defined($result)){
	    printf("ERROR: %s.\n", $session->error);
	    $session->close;
	    exit 1;
	   }

	   $session->close;
	   return($result->{$swL2PortCtrlDescription});
	   */
		return 0;
	}

	int set_port_state(String host, int n_port, int n_portstate) {
	/*
	#set port state			 set_port_state(ip_switch, n_port, n_portstate); n_portstate = 0||1
	my $hostname = shift @_;
	my $n_port = shift @_;
	my $n_portstate = shift @_;

		my $swL2PortCtrlAdminState = "1.3.6.1.4.1.171.11.64.1.2.4.2.1.3";
		my $disable = 2;
		my $enable = 3;
	 	my $result;
		my @oidlist;
		my $oidlist;
		my $community = "private";
		my ($session, $error) = Net::SNMP->session(-hostname      => $hostname,
												   -community     => $community);
		if (!defined($session)) {
	             printf("ERROR: %s.\n", $error);
	             exit 1;
	    }

		@oidlist = ();

			if($n_portstate){
				$swL2PortCtrlAdminState = $swL2PortCtrlAdminState.".".$n_port;
				$oidlist = [($swL2PortCtrlAdminState, 2, $enable)];
			}else{
				$swL2PortCtrlAdminState = $swL2PortCtrlAdminState.".".$n_port;
				$oidlist = [($swL2PortCtrlAdminState, 2, $disable)];
			}

		$result = $session->set_request(
	                                 -varbindlist      => $oidlist
	                              );
		if(!defined($result)) {
	             printf("ERROR: %s.\n", $session->error);
	             $session->close;
	             exit 1;
	    }
		$session->close;
		return($result->{$swL2PortCtrlAdminState});
		*/
		return 0;
	}
	
	int del_port_vlan_untag(String host, String community, int n_port, int n_vlan) {
		//String swL2TagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.2.";
		String swL2UnTagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.4.";
		String strRes;
		String tosetRes = "";
		int res;	
		//Getting current untagged port bitmap
		 swL2UnTagVlan = swL2UnTagVlan + n_vlan;
		 strRes = hp.snmpGet(host, community, swL2UnTagVlan); 
		 System.out.println(strRes);
		 System.out.println(strRes.split(":").length);
		 if(Helper.IsPortVlan(strRes, n_port, n_vlan)){
			 tosetRes = Helper.HexMap(strRes, Helper.port2Hex(strRes,n_port), "and_not");
			 System.out.println("Port " + n_port + " is in Vlan " + n_vlan);
			 System.out.println("Helper.HexMap: " + tosetRes);swL2UnTagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.4.";                                                                                                           
			 swL2UnTagVlan = swL2UnTagVlan + n_vlan; 
			 hp.snmpSet(host, community, swL2UnTagVlan, Helper.HexToBinary(tosetRes));
		 }else{
			 System.out.println("Port " + n_port + " isn't in Vlan " + n_vlan + " Can't delete any");
			 return 1;
		 }	 
			return 0;
		}
	
	int del_port_vlan_tag(String host, String community, int n_port, int n_vlan) {
		String swL2TagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.2.";
		//String swL2UnTagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.4.";
		String strRes;
		String tosetRes = "";
		int res;	
		//Getting current tagged port bitmap
		 swL2TagVlan = swL2TagVlan + n_vlan;
		 strRes = hp.snmpGet(host, community, swL2TagVlan); 
		 System.out.println(strRes);
		 System.out.println(strRes.split(":").length);
		 if(Helper.IsPortVlan(strRes, n_port, n_vlan)){
			 tosetRes = Helper.HexMap(strRes, Helper.port2Hex(strRes,n_port), "and_not");
			 System.out.println("Port " + n_port + " is in Vlan " + n_vlan);
			 System.out.println("Helper.HexMap: " + tosetRes);swL2TagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.2.";                                                                                                           
			 swL2TagVlan = swL2TagVlan + n_vlan; 
			 hp.snmpSet(host, community, swL2TagVlan, Helper.HexToBinary(tosetRes));
		 }else{
			 System.out.println("Port " + n_port + " isn't in Vlan " + n_vlan + " Can't delete any");
			 return 1;
		 }
		return 0;
	}

	
	int del_port_vlan(String host, String community, int n_port, int n_vlan){
		del_port_vlan_untag(host, community, n_port, n_vlan);
		del_port_vlan_tag(host, community, n_port, n_vlan);
		return 0;
	}
	
	int set_port_vlan_untag(String host, String community, int n_port, int n_vlan) {
	
	String swL2UnTagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.4.";                                                                               
	String strRes;
	String tosetRes = "";
	int res;
	
	//Getting current untagged port bitmap
	 swL2UnTagVlan = swL2UnTagVlan + n_vlan;
	 strRes = hp.snmpGet(host, community, swL2UnTagVlan);
	 System.out.println(strRes);
	 if(Helper.IsPortVlan(strRes, n_port, n_vlan)){
		 System.out.println("Port " + n_port + " is in vlan " + n_vlan);
		 return 0;
	 }
	 // !!!! This is important my $res = SNMP_Handle_sw::del_port_vlan($hostname,$n_port, SNMP_Handle_sw::get_port_vlan($hostname,$n_port));	  
	 // Getting current tagged port bitmap
	 tosetRes = Helper.HexMap(strRes, Helper.port2Hex(strRes,n_port), "or");
	 System.out.println("Port " + n_port + " is in Vlan " + n_vlan);
	 System.out.println("Helper.HexMap: " + tosetRes);swL2UnTagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.4."; 
	 swL2UnTagVlan = swL2UnTagVlan + n_vlan;
	 hp.snmpSet(host, community, swL2UnTagVlan, Helper.HexToBinary(tosetRes));	 
	
	 return 0;
	}
	
	int set_port_vlan_tag(String host, String community, int n_port, int n_vlan) {
		
		String swL2TagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.2.";                                                                               
		String strRes;
		String tosetRes = "";
		int res;
		
		//Getting current tagged port bitmap
		 swL2TagVlan = swL2TagVlan + n_vlan;
		 strRes = hp.snmpGet(host, community, swL2TagVlan);
		 System.out.println(strRes);
		 del_port_vlan_untag(host, community, n_port, n_vlan);
		 if(Helper.IsPortVlan(strRes, n_port, n_vlan)){	 
			 System.out.println("Port " + n_port + " is in vlan " + n_vlan);
			 return 0;
		 }
		 // !!!! This is important my $res = SNMP_Handle_sw::del_port_vlan($hostname,$n_port, SNMP_Handle_sw::get_port_vlan($hostname,$n_port));	  
		 // Getting current tagged port bitmap
		 tosetRes = Helper.HexMap(strRes, Helper.port2Hex(strRes,n_port), "or");
		 System.out.println("Port " + n_port + " is in Vlan " + n_vlan);
		 System.out.println("Helper.HexMap: " + tosetRes); swL2TagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.2.";
		 swL2TagVlan = swL2TagVlan + n_vlan;
		 hp.snmpSet(host, community, swL2TagVlan, Helper.HexToBinary(tosetRes));	 
		
		 return 0;
		}


	int get_port_speed(String host, int n_port) {
	/*
	#get port speed by port 	 get_port_speed(ip_switch, n_port);
	my $hostname = shift @_;
	my $n_port = shift @_;
	my $tx_speed = shift @_;
	my $rx_speed = shift @_;
	my $result;
	my @oidlist;
	my $oidlist;
	my $community = "private";
	my $swL2QOSBandwidthTxRate = "1.3.6.1.4.1.171.11.64.1.2.6.1.1.2";
	my $swL2QOSBandwidthRxRate = "1.3.6.1.4.1.171.11.64.1.2.6.1.1.3";
	my ($session, $error) = Net::SNMP->session(-hostname      => $hostname,
	                                           -community     => $community);
	    if(!defined($session)){
	        printf("ERROR: %s.\n", $error);
	        exit 1;
	    }
	    @oidlist = ();

	            $swL2QOSBandwidthTxRate = $swL2QOSBandwidthTxRate.".".$n_port;
	            $oidlist = [($swL2QOSBandwidthTxRate)];

	    $result = $session->get_request(
	                          -varbindlist      => $oidlist
	                      );
	    @oidlist = ();
	            $swL2QOSBandwidthRxRate = $swL2QOSBandwidthRxRate.".".$n_port;
	            $oidlist = [($swL2QOSBandwidthRxRate)];

	    $result = $session->get_request(
	                          -varbindlist      => $oidlist
	                      );

	   if(!defined($result)){
	    printf("ERROR: %s.\n", $session->error);
	    $session->close;
	    exit 1;
	   }

	   $session->close;
	   return($result->{$swL2QOSBandwidthRxRate});
	   */
		return 0;
	}

	String get_port_description(String host, String community, int n_port) {	
	      
	   String swL2PortCtrlDescription = "1.3.6.1.2.1.31.1.1.1.18.";
	   swL2PortCtrlDescription = swL2PortCtrlDescription + n_port;
	   String retStr = "";
	   String ret = "";
	   if((hp.snmpGet(host, community, swL2PortCtrlDescription).length()) != 0){
			retStr = hp.snmpGet(host, community, swL2PortCtrlDescription);
			ret = retStr;
		}
		return ret;
	}

	int get_port_state (String host, String community, int n_port){
		
	//!!! This method doesn't work correctly, because swL2PortCtrlAdminState is very specific !!!
		
	String swL2PortCtrlAdminState = "1.3.6.1.4.1.171.11.64.1.2.4.2.1.3.";
			String retStr;
			int ret = 0;
	    swL2PortCtrlAdminState = swL2PortCtrlAdminState + n_port;                                                                    
		if((hp.snmpGet(host, community, swL2PortCtrlAdminState).length()) != 0){
			retStr = hp.snmpGet(host, community, swL2PortCtrlAdminState);
			ret = Integer.parseInt(retStr);
		}
		return ret;
	}
	
	int[] get_port_vlan_tag(String host, String community, int n_port, int n_vlan){
		//$swL2TagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.2"; 
		//$swL2UnTagVlan = "1.3.6.1.2.1.17.7.1.4.3.1.4";
		int ret [] = {1,2,3};
		return (ret);
	}
	
	int get_port_vlan_untag(String host, String community, int n_port, int n_vlan) {
		String swL2Untag = ".1.3.6.1.2.1.17.7.1.4.5.1.1.";        
	    swL2Untag = swL2Untag + n_port;	    
			String retStr;
			int ret = 0;
			if((hp.snmpGet(host, community, swL2Untag).length()) != 0){
				retStr = hp.snmpGet(host, community, swL2Untag);
				ret = Integer.parseInt(retStr);
			}
		return ret;
	}
	
	public static void main(String str[]){
	    Port p = new Port();
	    //System.out.println("State of port 1 is: "  + p.get_port_state("10.2.1.29", "private", 1));
	    //System.out.println("Description of port 11 is: "  + p.get_port_description("10.2.1.29", "private", 11));
	    //System.out.println(p.del_port_vlan_untag("10.2.1.29", "private", 1, 216));
	    //System.out.println(p.del_port_vlan_tag("10.2.1.29", "private", 1, 216));
	     //System.out.println(p.del_port_vlan("10.2.1.29", "private", 1, 216));
	    //System.out.println(p.set_port_vlan_untag("10.2.1.29", "private", 2, 216));
	    //System.out.println(p.set_port_vlan_tag("10.2.1.29", "private", 1, 216));
	}
}
