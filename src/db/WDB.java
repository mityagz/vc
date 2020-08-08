package db;

import datastruct.Adj;
import java.sql.*;
import java.sql.Statement;
import java.util.Map;

import datastruct.NodeParse0;
import datastruct.SymGraph;
import org.jooq.*;
import org.jooq.impl.DSL;
import scala.util.parsing.combinator.testing.Str;

/**
 * Created by mitya on 7/22/20.
 */
public class WDB {
    private String db = "jdbc:postgresql://192.168.122.19:5432/vc";
    private String user_db ="vc";
    private String password_db = "vc";
    private Connection connection_db = null;
    private Statement st = null;
    private ResultSet rs = null;
    private Adj adj;
    private SymGraph sg;

    public WDB(Adj adj, SymGraph sg) {
        this.adj = adj;
        this.sg = sg;
        connDB(db, user_db, password_db);
        //IsExistAdj(1, 1, 1, 1, 1, "x");
        checkDB();
    }


    private void connDB(String db, String user, String pass) {
        try {
            connection_db = DriverManager.getConnection(db, user_db, password_db);
            st = connection_db.createStatement();
            DSLContext create = DSL.using(connection_db, SQLDialect.POSTGRES);
            //create.select().from(ADJS).fetch();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void queryBuild(){

    }

    private void checkDB() {
        // Adj
        Map<String, Map<String, NodeParse0>> rnp;
        rnp = adj.getJ().getRnp();

        /*
        // sg: st{loopback_ip} = node_id;
        for(String h : sg.getSt().keys()) {
            System.out.println("checkDB st : h :" + h + ": st{h} :" + sg.getSt().get(h));
        }

        // keys[node_id] = loopback_ip
        String [] keys = sg.getKeys();
        for(int i = 0; i < keys.length; i++) {
            System.out.println("checkDB keys : h :" + i + ": keys[" + i + "] = :" + keys[i]);
        }

        // sg: st1{loopback_ip} = hostname;
        for(String h : sg.getSt1().keys()) {
            System.out.println("checkDB st1 : h :" + h + ": st1{h} :" + sg.getSt1().get(h));
        }

        //checkDB st : h :10.248.0.66: st{h} :0
        //checkDB keys : h :0: keys[0] = :10.248.0.66
        //checkDB st1 : h :CE3: st1{h} :10.248.0.66
        */

        for(String h : rnp.keySet()) {
            //System.out.println("checkDB: adj host : " + h);
            for (String hh : rnp.get(h).keySet()) {
                //IsExistAdj(int id, int node_id, int adj_node_id, int interface_id_oif, int interface_id_iif, String name)
                //IsExistAdj(int node_id, int adj_node_id, String interface_id_oif, String interface_id_iif, String name)

                String node_ip_lo0 = h;
                String adj_ip_node_lo0 = rnp.get(h).get(hh).getLo0();
                String node_hostname = sg.getSt2().get(h);
                String node_adj_hostname = rnp.get(h).get(hh).getSystemName();
                String interface_name_oif = rnp.get(h).get(hh).getIfO();
                String interface_name_iif = rnp.get(h).get(hh).getIfI();

                //checkParam(node_ip_lo0, adj_ip_node_lo0, node_hostname, node_adj_hostname, interface_name_oif, interface_name_iif);


                if (CheckTables(node_ip_lo0, adj_ip_node_lo0, node_hostname, node_adj_hostname, interface_name_oif, interface_name_iif, "x") == -1) {
                    if (IsExistNodeId(1, 1, node_ip_lo0, node_hostname) == -1 || IsExistNodeId(1, 2, adj_ip_node_lo0, node_adj_hostname) == -1) {
                            if (IsExistTypeId(1, "", "", "") == -1) {
                                //insertTypeId("PE", "PE", "Provider edge router");
                            }
                            if (IsExistNodeId(1, 1, node_ip_lo0, node_hostname) == -1)
                                insertNodeId(node_ip_lo0, node_hostname);
                            if (IsExistNodeId(1, 2, adj_ip_node_lo0, node_adj_hostname) == -1)
                                insertNodeId(adj_ip_node_lo0, node_adj_hostname);
                        }


                    if (IsExistInterface(1, node_ip_lo0, "", interface_name_oif, "") == -1 || IsExistInterface(1, adj_ip_node_lo0, "", interface_name_iif, "") == -1) {
                        if (IsExistInterface(1, node_ip_lo0, "", interface_name_oif, "") == -1)
                            insertInterfaceId(node_ip_lo0, interface_name_oif);
                        if (IsExistInterface(1, adj_ip_node_lo0, "", interface_name_iif, "") == -1)
                            insertInterfaceId(adj_ip_node_lo0, interface_name_iif);
                    }
                    insertAdj(node_ip_lo0, adj_ip_node_lo0, node_hostname, node_adj_hostname, interface_name_oif, interface_name_iif);
                }
            }
        }
    }

    private int CheckTables(String node_id_lo0, String adj_node_id_lo0, String node_id_host, String adj_node_id_host, String int_oif, String int_iif, String adj_name) {
        Integer retId = -1;
        /*
        System.out.println("CheckTables: " + node_id_lo0);
        System.out.println("CheckTables: " + sg.getSt2().get(node_id_lo0));
        System.out.println("CheckTables: " + adj_node_id_lo0);
        System.out.println("CheckTables: " + adj_node_id_host);
        System.out.println("CheckTables: " + int_oif);
        System.out.println("CheckTables: " + int_iif);
        System.out.println("CheckTables: " + adj_name);
        */

        try {
            st = connection_db.createStatement();
            String q = "select a.id, a.name, n0.hostname, n0.ip, i0.name, i1.name, n1.hostname, n1.ip from adjs a " +
                                 "join node n0 on a.node_id = n0.id " +
                                 "join node n1 on a.adj_node_id = n1.id " +
                                 "join interface i0 on a.interface_id_oif=i0.id " +
                                 "join interface i1 on a.interface_id_iif=i1.id " +
                                 "where n0.ip = '" + node_id_lo0 +
                                 "' and n1.ip = '" + adj_node_id_lo0 +
                                 "' and i0.name = '" + int_oif +
                                 "' and i1.name = '" + int_iif + "';";

            rs = st.executeQuery(q);

            if(rs.next()) {
                retId = rs.getInt(1);
                if(retId != 0) {
                    //System.out.println("checkTable: Adj id was found: " + retId + ":" + q);
                }

            } else {
                //System.out.println("checkTable: Adj id wasn't found in adj table: " + retId + ":" + q);
            }
            //System.out.println("checkTable2: " + node_id_lo0 + ":" + adj_node_id_lo0 + ":" + int_oif + ":" + int_iif);
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return retId;
    }

    private void insertAdj(String node_ip_lo0, String adj_ip_node_lo0, String node_hostname, String node_adj_hostname, String interface_name_oif, String interface_name_iif) {
        //System.out.println("Insert into Adj");
        Integer oif = getInterfaceId(node_ip_lo0, interface_name_oif);
        Integer iif = getInterfaceId(adj_ip_node_lo0, interface_name_iif);
        if(oif == -1 || iif == -1)
            return;
        Integer node_id = getNodeId(node_ip_lo0);
        Integer adj_node_id = getNodeId(adj_ip_node_lo0);
        if(node_id == -1 || adj_node_id == -1)
            return;
        //System.out.println("insertAdj: " + node_id + ":" + adj_node_id + ":" + oif + ":" + iif);
        try {
            int rs;
            st = connection_db.createStatement();
            rs = st.executeUpdate("insert into " +
                    "adjs(node_id, adj_node_id, interface_id_oif, interface_id_iif, name) " +
                    "values (" + node_id + ", " + adj_node_id + ", " + oif + ", " + iif + ", '"
                     + node_hostname + ":" + interface_name_oif + "<-->" + node_adj_hostname + ":" + interface_name_iif + "')");
            //System.out.println("Adj id was inserted, node " + node_id + " " + adj_node_id + " id is: x " + rs);
            st.closeOnCompletion();
            st.close();
        } catch (Exception e) {
            System.out.println("insertAdj: " + e);
        }
    }

    private void insertInterfaceId(String ip, String interface_name) {
        //System.out.println("Insert into ip:Interface: " + ip + " : " + interface_name);
        int node_id = getNodeId(ip);
        if(node_id == -1)
            return;
        try {
            int rs;
            st = connection_db.createStatement();
            rs = st.executeUpdate("insert into " +
                    "interface (node_id, name) " +
                    "values ('" + node_id + "' , '"+ interface_name + "');");
            //System.out.println("Interface id was inserted, node " + ip + " " + interface_name + " id is: x " + rs);
            st.closeOnCompletion();
            st.close();
        } catch (Exception e) {
            System.out.println("insertNodeId: " + e);
        }
    }

    private void insertNodeId(String ip, String hostname) {
        try {
            int rs;
            st = connection_db.createStatement();
            rs = st.executeUpdate("insert into " +
                    "node (type_id, hostname, ip) " +
                    "values (1 , '" + hostname + "' , '"+ ip + "');");
            //System.out.println("Node id was inserted, node " + hostname + " " + ip + " id is: x " + rs);
            st.closeOnCompletion();
            st.close();
        } catch (Exception e) {
            System.out.println("insertNodeId: " + e);
        }
    }

    private void insertTypeId(String type, String role, String name) {
        //System.out.println("Insert into type");
        String type_r =   "insert into " +
                        "type (type, role, name) " +
                        "values (" + type + ", '" + role + "' , '" +  name + "')";
        try {
            st.executeUpdate(type_r);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private int IsExistAdj(int id, int node_id, int adj_node_id, int interface_id_oif, int interface_id_iif, String name) {
        Integer retId = -1;
        try {
            st = connection_db.createStatement();
            rs = st.executeQuery("SELECT adjs.id " +
                                            " FROM adjs " +
                                            " WHERE adjs.node_id = " + node_id +
                                            " AND adjs.adj_node_id = " + adj_node_id +
                                            " AND adjs.interface_id_oif = " + interface_id_oif +
                                            " AND adjs.interface_id_oif = " + interface_id_iif +
                                            " AND adjs.name = '" + name + "'");
            if(rs.next()) {
                retId = rs.getInt(1);
                if(retId == 0) {
                    //System.out.println("Node id found");
                }
            } else {
                //System.out.println("Node id isn't found in adj table");
                return retId;
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return retId;
    }

    private int getInterfaceId(String node_lo, String interface_name) {
        Integer retId = -1;
        int node_id = getNodeId(node_lo);
        if(node_id == -1)
            return retId;
        try {
            st = connection_db.createStatement();
            rs = st.executeQuery("select i0.id from interface i0 " +
                                 " where i0.name = '" + interface_name + "'" + " and i0.node_id = " + node_id);
            if (rs.next()) {
                retId = rs.getInt(1);
                if (retId != 0) {
                    //System.out.println("getInterfaceId id was found, node " + node_lo + ":" + node_id + " id is: " + retId);
                }
            } else {
                //System.out.println("getInterfaceId id wasn't found, node " + node_lo + ":" + node_id + " id is: " + retId);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("getNodeId: " + e);
        }
        return retId;
    }

    private int getNodeId(String node_lo) {
        Integer retId = -1;
        try {
            st = connection_db.createStatement();
            rs = st.executeQuery("select n0.id, n0.hostname, n0.ip from node n0 " +
                                 " where n0.ip = '" + node_lo + "'");
            if (rs.next()) {
                retId = rs.getInt(1);
                if (retId != 0) {
                    //System.out.println("getNodeId id was found, node " + node_lo + " id is: " + retId);
                }
            } else {
                //System.out.println("getNodeId id wasn't found, node " + node_lo + " id is: " + retId);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("getNodeId: " + e);
        }
        return retId;
    }

    private int IsExistInterface(int id, String node_lo, String type, String name, String description) {
        Integer retId = -1;
        int node_id = getNodeId(node_lo);
        if(node_id == -1)
            return retId;
        try {
            st = connection_db.createStatement();
            rs = st.executeQuery("select i0.id, i0.node_id, i0.type,  i0.name, i0.description from interface i0 " +
                                 " where i0.node_id = '" + node_id + "' and i0.name = '" + name + "'");
            if (rs.next()) {
                retId = rs.getInt(1);
                if (retId != 0) {
                    //System.out.println("IsExistInterface id was found, node " + node_lo + " id is: " + name + " : " + retId);
                }
            } else {
                //System.out.println("IsExistInterface id wasn't found, node " + node_lo + " id is: " + name + " : " + retId);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("IsExistNodeId: " + e);
        }
        return retId;
    }

    private int IsExistNodeId (int id, int type_id, String ip, String hostname) {
        Integer retId = -1;
        try {
            st = connection_db.createStatement();
            rs = st.executeQuery("select n0.id, n0.hostname, n0.ip from node n0 " +
                                 " where n0.ip = '" + ip + "' and n0.hostname = '" + hostname + "'");
            if (rs.next()) {
                retId = rs.getInt(1);
                if (retId != 0) {
                    //System.out.println("Node id was found, node " + ip + " id is: " + hostname + " : " + retId);
                }
            } else {
                //System.out.println("Node id wasn't found, node " + ip + " id is: " + hostname + " : " + retId);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("IsExistNodeId: " + e);
        }
        return retId;
    }

    private int IsExistTypeId (int id, String type, String role, String name) {
        Integer retId = 1;
        return retId;
    }

    private void checkParam(String p0, String p1, String p2, String p3, String p4, String p5) {
        System.out.println("checkParam: " + p0 + ":" + ":" + p1 + ":" + p2 + ":" + p3 + ":" + p4 + ":" + p5);
    }

    public static void main(String [] args) {
        new WDB(null, null);
    }
}