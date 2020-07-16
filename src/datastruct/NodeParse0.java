package datastruct;

/**
 * Created by mitya on 2/18/20.
 */
public class NodeParse0 {
    private String ifI;
    private String ifO;
    private String chassisId;
    private String systemName;
    private String lo0;
    private int node;

    //
    public NodeParse0(String systemName, String ifI, String ifO, String chassisId, String lo0) {
        this.ifI = ifI;
        this.ifO = ifO;
        this.chassisId = chassisId;
        this.systemName = systemName;
        this.lo0 = lo0;
    }

    public int getNode() { return node; }
    public String getIfI() { return ifI; }
    public String getIfO() { return ifO; }
    public String getChassisId() { return chassisId; }
    public String getSystemName() { return systemName; }
    public String getLo0() { return lo0; }
    public String toString() { return node + " " + ifI + " " + systemName + " " + chassisId + " " + ifO + " " + lo0; }

    public void setLo0(String ip) { lo0 = ip; }
    public void setNode(int node) { this.node = node; }
}
