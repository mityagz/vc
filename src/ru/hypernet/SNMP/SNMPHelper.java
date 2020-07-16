package ru.hypernet.SNMP;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPHelper {
	@SuppressWarnings("rawtypes")
	public String snmpGet(String host, String community, String strOID) {
		  String strResponse="";
		  ResponseEvent response;
		  Snmp snmp;
		  try {
		    OctetString community1 = new OctetString(community);
		    host= host+"/"+"161";
		    Address tHost = new UdpAddress(host);
		    TransportMapping transport = new DefaultUdpTransportMapping();
		    transport.listen();
		    CommunityTarget comtarget = new CommunityTarget();
		    comtarget.setCommunity(community1);
		    comtarget.setVersion(SnmpConstants.version1);
		    comtarget.setAddress(tHost);
		    comtarget.setRetries(2);
		    comtarget.setTimeout(5000);
		    PDU pdu = new PDU();
		    pdu.add(new VariableBinding(new OID(strOID)));
		    pdu.setType(PDU.GET); 
		    snmp = new Snmp(transport);
		    response = snmp.get(pdu,comtarget);
		    if(response != null) {
		      if(response.getResponse().getErrorStatusText().equalsIgnoreCase("Success")) {
		        PDU pduresponse=response.getResponse();
		        strResponse=pduresponse.getVariableBindings().firstElement().toString();
		        if(strResponse.contains("=")) {
		          //int len = str.indexOf("=");
		          //strResponse=str.substring(len+1, str.length());
		        	int len = strResponse.indexOf("=");
			        strResponse=strResponse.substring(len+2, strResponse.length());
		        }
		      }
		    } else {
		      System.out.println("Looks like a TimeOut occured ");
		    }
		    snmp.close();
		  } catch(Exception e) {
			  System.out.println("Host: " + host + " no access");
		     //e.printStackTrace();
		  }
		 //System.out.println("Response = "+strResponse);
		 return strResponse;
		}
	
	@SuppressWarnings("rawtypes")
	public void snmpSet(String host, String community, String strOID, int Value) {
		  host= host+"/"+"161";
		  Address tHost = GenericAddress.parse(host);
		  Snmp snmp;
		  try {
		    TransportMapping transport = new DefaultUdpTransportMapping();
		    snmp = new Snmp(transport);
		    transport.listen();
		    CommunityTarget target = new CommunityTarget();
		    target.setCommunity(new OctetString(community));
		    target.setAddress(tHost);
		    target.setRetries(2);
		    target.setTimeout(5000);
		    target.setVersion(SnmpConstants.version1); //Set the correct SNMP version here
		    PDU pdu = new PDU();
		    //Depending on the MIB attribute type, appropriate casting can be done here
		    pdu.add(new VariableBinding(new OID(strOID), new Integer32(Value)));
		    pdu.setType(PDU.SET);
		    ResponseListener listener = new ResponseListener() {
		      public void onResponse(ResponseEvent event) {
		        PDU strResponse;
		        String result;
		        ((Snmp)event.getSource()).cancel(event.getRequest(), this);
		        strResponse = event.getResponse(); 
		        if (strResponse!= null) {
		          result = strResponse.getErrorStatusText();
		          System.out.println("Set Status is: "+result);
		        }
		      }};
		      snmp.send(pdu, target, null, listener);
		      snmp.close();
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		}
	public void snmpSet(String host, String community, String strOID, String Value) {
		  host= host+"/"+"161";
		  Address tHost = GenericAddress.parse(host);
		  Snmp snmp;
		  try {
		    TransportMapping transport = new DefaultUdpTransportMapping();
		    snmp = new Snmp(transport);
		    transport.listen();
		    CommunityTarget target = new CommunityTarget();
		    target.setCommunity(new OctetString(community));
		    target.setAddress(tHost);
		    target.setRetries(2);
		    target.setTimeout(5000);
		    target.setVersion(SnmpConstants.version1); //Set the correct SNMP version here
		    PDU pdu = new PDU();
		    //Depending on the MIB attribute type, appropriate casting can be done here
		    pdu.add(new VariableBinding(new OID(strOID), new OctetString(Value)));
		    pdu.setType(PDU.SET);
		    ResponseListener listener = new ResponseListener() {
		      public void onResponse(ResponseEvent event) {
		        PDU strResponse;
		        String result;
		        ((Snmp)event.getSource()).cancel(event.getRequest(), this);
		        strResponse = event.getResponse();
		        if (strResponse!= null) {
		          result = strResponse.getErrorStatusText();
		          System.out.println("Set Status is: "+result);
		        }
		      }};
		      snmp.send(pdu, target, null, listener);
		      snmp.close();
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		}
	
	public void snmpSet(String host, String community, String strOID, byte [] Value) {
		  host= host+"/"+"161";
		  Address tHost = GenericAddress.parse(host);
		  Snmp snmp;
		  try {
		    TransportMapping transport = new DefaultUdpTransportMapping();
		    snmp = new Snmp(transport);
		    transport.listen();
		    CommunityTarget target = new CommunityTarget();
		    target.setCommunity(new OctetString(community));
		    target.setAddress(tHost);
		    target.setRetries(2);
		    target.setTimeout(5000);
		    target.setVersion(SnmpConstants.version1); //Set the correct SNMP version here
		    PDU pdu = new PDU();
		    //Depending on the MIB attribute type, appropriate casting can be done here
		    pdu.add(new VariableBinding(new OID(strOID), new OctetString( Value)));
		    pdu.setType(PDU.SET);
		    ResponseListener listener = new ResponseListener() {
		      public void onResponse(ResponseEvent event) {
		        PDU strResponse;
		        String result;
		        ((Snmp)event.getSource()).cancel(event.getRequest(), this);
		        strResponse = event.getResponse();
		        if (strResponse!= null) {
		          result = strResponse.getErrorStatusText();
		          System.out.println("Set Status is: "+result);
		        }
		      }};
		      snmp.send(pdu, target, null, listener);
		      snmp.close();
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		}
		
	public void snmpSet(String host, String community, String strOID0, String Value0, String strOID1, String Value1, String strOID2, String Value2, String strOID3, Integer Value3 ) {
		  host= host+"/"+"161";
		  Address tHost = GenericAddress.parse(host);
		  Snmp snmp;
		  try {
		    TransportMapping transport = new DefaultUdpTransportMapping();
		    snmp = new Snmp(transport);
		    transport.listen();
		    CommunityTarget target = new CommunityTarget();
		    target.setCommunity(new OctetString(community));
		    target.setAddress(tHost);
		    target.setRetries(2);
		    target.setTimeout(5000);
		    target.setVersion(SnmpConstants.version1); //Set the correct SNMP version here
		    PDU pdu = new PDU();
		    //Depending on the MIB attribute type, appropriate casting can be done here
		    pdu.add(new VariableBinding(new OID(strOID0), new OctetString(Value0)));
		    //pdu.add(new VariableBinding(new OID(strOID1), new OctetString(Value1)));
		    //pdu.add(new VariableBinding(new OID(strOID2), new OctetString(Value2)));
		    pdu.add(new VariableBinding(new OID(strOID3), new Integer32(Value3)));
		    pdu.setType(PDU.SET);
		    ResponseListener listener = new ResponseListener() {
		      public void onResponse(ResponseEvent event) {
		        PDU strResponse;
		        String result;
		        ((Snmp)event.getSource()).cancel(event.getRequest(), this);
		        strResponse = event.getResponse();
		        if (strResponse!= null) {
		          result = strResponse.getErrorStatusText();
		          System.out.println("Set Status is: "+result);
		        }
		      }};
		      snmp.send(pdu, target, null, listener);
		      snmp.close();
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		}
	
	public void snmpSet(String host, String community, String strOID0, String Value0, String strOID1, byte [] Value1, String strOID2, Integer Value2, String strOID3, Integer Value3) {
		  host= host+"/"+"161";
		  Address tHost = GenericAddress.parse(host);
		  Snmp snmp;
		  try {
		    TransportMapping transport = new DefaultUdpTransportMapping();
		    snmp = new Snmp(transport);
		    transport.listen();
		    CommunityTarget target = new CommunityTarget();
		    target.setCommunity(new OctetString(community));
		    target.setAddress(tHost);
		    target.setRetries(2);
		    target.setTimeout(5000);
		    target.setVersion(SnmpConstants.version1); //Set the correct SNMP version here
		    PDU pdu = new PDU();
		    //Depending on the MIB attribute type, appropriate casting can be done here
		    pdu.add(new VariableBinding(new OID(strOID0), new IpAddress(Value0)));
		    pdu.add(new VariableBinding(new OID(strOID1), new OctetString(Value1)));
		    pdu.add(new VariableBinding(new OID(strOID2), new Integer32(Value2)));
		    pdu.add(new VariableBinding(new OID(strOID3), new Integer32(Value3)));
		    pdu.setType(PDU.SET);
		    ResponseListener listener = new ResponseListener() {
		      public void onResponse(ResponseEvent event) {
		        PDU strResponse;
		        String result;
		        ((Snmp)event.getSource()).cancel(event.getRequest(), this);
		        strResponse = event.getResponse();
		        if (strResponse!= null) {
		          result = strResponse.getErrorStatusText();
		          System.out.println("Set Status is: "+result);
		        }
		      }};
		      snmp.send(pdu, target, null, listener);
		      snmp.close();
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		}
		// 3526 acl
		public void snmpSet(String host, String community,  String strOID0, String Value0, String strOID2, Integer Value2, String strOID1, byte [] Value1,  String strOID3, Integer Value3) {
		  host= host+"/"+"161";
		  Address tHost = GenericAddress.parse(host);
		  Snmp snmp;
		  try {
		    TransportMapping transport = new DefaultUdpTransportMapping();
		    snmp = new Snmp(transport);
		    transport.listen();
		    CommunityTarget target = new CommunityTarget();
		    target.setCommunity(new OctetString(community));
		    target.setAddress(tHost);
		    target.setRetries(2);
		    target.setTimeout(5000);
		    target.setVersion(SnmpConstants.version1); //Set the correct SNMP version here
		    PDU pdu = new PDU();
		    //Depending on the MIB attribute type, appropriate casting can be done here
		    pdu.add(new VariableBinding(new OID(strOID0), new IpAddress(Value0)));
		    pdu.add(new VariableBinding(new OID(strOID2), new Integer32(Value2)));
		    pdu.add(new VariableBinding(new OID(strOID1), new OctetString(Value1)));
		    pdu.add(new VariableBinding(new OID(strOID3), new Integer32(Value3)));
		    pdu.setType(PDU.SET);
		    ResponseListener listener = new ResponseListener() {
		      public void onResponse(ResponseEvent event) {
		        PDU strResponse;
		        String result;
		        ((Snmp)event.getSource()).cancel(event.getRequest(), this);
		        strResponse = event.getResponse();
		        if (strResponse!= null) {
		          result = strResponse.getErrorStatusText();
		          System.out.println("Set Status is: "+result);
		        }
		      }};
		      snmp.send(pdu, target, null, listener);
		      snmp.close();
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		}
	
	
	public static void main(String args[]){
		SNMPHelper sh =  new SNMPHelper();
		//sh.snmpGet("10.2.1.29", "public", ".1.3.6.1.2.1.1.5.0");
	}
}
