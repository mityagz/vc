package lldp;

import ru.hypernet.SNMP.SNMPHelper;

/**
 * Created by mitya on 1/9/20.
 */
public class DetectType {
    private String system_type;
    private Integer t;

    public DetectType(String ip_addr) {

        try {
            SNMPHelper snmpHelper = new SNMPHelper();
            system_type = snmpHelper.snmpGet(ip_addr, "public", ".1.3.6.1.2.1.1.1.0");
            if (system_type.contains("vmx")) {
                t = 1;
            } else if (system_type.contains("Dlink")) {
                t = 2;
            }
        } catch (Exception e) {

        }
    }

    public Integer getSystem_type() {
        return t;
    }
}
