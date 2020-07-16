package ru.hypernet.SNMP;

//import p1.SNMPHelper;


public class Helper {
private static final String Bin = null;
SNMPHelper hs =  new SNMPHelper();
	
	String get_sysdescr(String host, String community) {
		String strRes;
		String SysDescr = ".1.3.6.1.2.1.1.1.0";
		strRes = hs.snmpGet(host, community, SysDescr);
		return(strRes);
	}

	/*
	 *  methods for bit, byte, hex manipulations
	 */
	
	 static Long LogicAnd(Long l0, Long l1){	 
		 return l0 & l1;
	 }
	 
	 static Long LogicOr(Long l0, Long l1){
		 return l0 | l1;
	 }
	 
	 static Long LogicNot(Long l0){
		 return ~l0;
	 }
	 
	 // for formation OctetString
	 public static byte [] HexToBinary(String Hex) {
		 String  HexArr[] = Hex.split(":");
		 byte [] bb = new byte [Hex.split(":").length];
		 String Bin = "";
		 String ResBin = "";
		 int j = 0;
		 	for(String i : HexArr){
		 		Long H = Long.parseLong(i, 16);
		    	bb[j] = H.byteValue();
		    	ResBin += Bin;
		    	j++;
		 	}
		 	return bb;
	 }

	 
	 public static String hex2Octetstring(String hex){
	        StringBuilder strRes = new StringBuilder();
	        for(int i = 0; i < hex.length(); i += 2){
	            String str = hex.substring(i, i + 2);
	            strRes.append(Integer.parseInt(str, 16));
	        }
	        return strRes.toString();
	 }
	 
	 public static String split2str(String str){
		 String strRes = "";
		 String []res = str.split(":");
		 for(String s : res){
			 strRes += s;
		 }
		 
		 return strRes;
	 }
	 public static boolean IsPortVlan (String port_map, int n_port, int n_vlan){
		 String hex_portmap = port2Hex(port_map, n_port);	 
		 String is_port_hex_portmap = HexMap(port_map, hex_portmap, "and");
		 int dec_portmap = Hex2port(is_port_hex_portmap);
		 if(dec_portmap != 0)
			 return true;
		 	return false;
	 }
	 
	 public static int Hex2port(String hex_port){
		 int len_hex_port = hex_port.split(":").length;
		 String [] hpms= hex_port.split(":");
		 Long[] long_sub0 = new Long[len_hex_port];	 
		 
		 for(int i = 0; i < len_hex_port; i++){
  			long_sub0[i] = Long.parseLong(hpms[i], 16);
  			if(long_sub0[i] != 0){
  				return 1;
  			}
  		 }
		 return 0;
	 }
	 
	 
public static String port2Hex(String hex_portmap, int n_port){
    	 String bin = "";
    	 int byte_len = 8;
    	 String bin_part = "";
    	 int n_byte = (hex_portmap.split(":").length);
    	 String [] byte_sub = new String[n_byte];
    	 String [] hex_sub = new String[n_byte];   	
    	 String hex_out = "";
    	 int len_mask = (hex_portmap.split(":").length)*8;
    	 
         for(int i = 1; i <= len_mask; i++){
                 if(i == n_port){
                         bin = bin + 1;
                 }else{
                         bin = bin + 0;
                 }
         }        
        for(int i = 0, j = 0; i < len_mask; i += byte_len, j++ ){
        			 bin_part = bin.substring(i, i + byte_len);
        			 byte_sub[j] = bin_part;
        			 hex_sub[j] = Long.toHexString(Long.parseLong(byte_sub[j], 2));
        }
        
         for(int i = 0; i < n_byte; i++){
  			if(hex_sub[i].equals("0")){
					hex_sub[i] += "0";
  			}else if(hex_sub[i].length() == 1){
 				hex_sub[i] = "0" + hex_sub[i];
 			}
  			if(i < n_byte - 1){
  				hex_out += hex_sub[i] + ":";
  			}else{
  				hex_out += hex_sub[i];
  			}
  			
   		}
   		
         return hex_out;
     }
     
 public static String HexMap(String hex_portmap, String hex_port, String op){
    	 	String hex_out;
     		int len_hex_portmap = hex_portmap.split(":").length;
     		String [] hpms= hex_portmap.split(":");
 			String [] hps = hex_port.split(":");
 			
     		
     		Long[] long_sub0 = new Long[len_hex_portmap];
     		String [] hex_sub0 = new String[len_hex_portmap];
     		Long[] long_sub1 = new Long[len_hex_portmap];
     		
     		Long [] long_band = new Long[len_hex_portmap];
     		
     		for(int i = 0; i < len_hex_portmap; i++){
     			long_sub0[i] = Long.parseLong(hpms[i], 16);
     			long_sub1[i] = Long.parseLong(hps[i], 16);
     		}
    
     		for(int i = 0; i < len_hex_portmap; i++){
     			if(op.equals("and_not")){
     				//long_band[i] = long_sub0[i] & ~long_sub1[i];
     				long_band[i] = LogicAnd(long_sub0[i], LogicNot(long_sub1[i]));
     			}else if(op.equals("or")){
     				//long_band[i] = long_sub0[i] | long_sub1[i];
     				long_band[i] = LogicOr(long_sub0[i], long_sub1[i]);
     			}else if(op.equals("and")){
     				//long_band[i] = long_sub0[i] | long_sub1[i];
     				long_band[i] = LogicAnd(long_sub0[i], long_sub1[i]);
     			}
     		}
     		
     		hex_out = "";
     		for(int i = 0; i < len_hex_portmap; i++){
     			hex_sub0[i] = Long.toHexString(long_band[i]);
     			if(hex_sub0[i].equals("0")){
 					hex_sub0[i] += "0";
     			}else if(hex_sub0[i].length() == 1){
     				hex_sub0[i] = "0" + hex_sub0[i];
     			}
     			if(i < len_hex_portmap - 1){
     				hex_out += hex_sub0[i] + ":";
     			}else{
     				hex_out += hex_sub0[i];
     			}
      		} 
     			System.out.println("hex_portmap HexMap: " + hex_portmap);
      			System.out.println("hex_port HexMap:    " + hex_port);
     		return hex_out;
     }
 
 public static String getIpAddress(byte[] rawBytes){
     int i = 4;
     String ipAddress = "";
     for (byte raw : rawBytes){
         ipAddress += (raw & 0xFF);
         if (--i > 0){
             ipAddress += ".";
         }
     }
     return ipAddress;
 }




    public static String dec2hex(Integer decimal){
		String hex = "";
		if(decimal == 0){
			return "00";
		}
		while (decimal != 0) {
			int hexValue = decimal % 16;
			char hexDigit = (hexValue <= 9 && hexValue >= 0) ?
					(char) (hexValue + '0') : (char) (hexValue - 10 + 'A');

			hex = hexDigit + hex;
			decimal = decimal / 16;
		}
		if(hex.length() == 1) hex = 0 + hex;
		return hex;
	}




     public static void main(String args[]){
 	 	//String hex_portmap =  "b0:80:80:c0:ff:dd:fe:cc:80:80:80:c0:ff:dd:ff:ff";
	 	//String hex_port = "00:00:00:c0";
	 	
         //System.out.println("HexMap	and_not     " +HexMap(hex_portmap, port2Hex(hex_portmap,128), "and_not") + "\n");
         //System.out.println("HexMap or :	     " +HexMap(hex_portmap, port2Hex(hex_portmap,120), "or"));
         //System.out.println("HexMap and:	     " +HexMap(hex_portmap, port2Hex(hex_portmap,120), "and"));
         //System.out.println("HexMap:	     " +port2Hex(hex_portmap, 1));
         //System.out.println(IsPortVlan(hex_portmap, 128, 216));
        //Hex2port("00:00:00:00:00:00:00:00:00:00:00:00:00:00:08:00");
 	    //Hex2port("00:00:00:10");
 	 	int port = 119;
 	 	int vlan = 2222;
 	    if(IsPortVlan("00:80:00:00:00:00:00:00:00:00:00:00:00:00:00:00" ,91, 2222)){
 	    	System.out.println("Port " + port + " is  in vlan " + vlan);
 	    }else{
 	    	System.out.println("Port " + port + " isn't in vlan " + vlan);
 	    }
     }
}
