package db;

import datastruct.Adj;
import datastruct.SymGraph;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by mitya on 7/22/20.
 */
public class RDB {
    private String db = "jdbc:postgresql://192.168.122.19:5432/vc";
    private String user_db ="vc";
    private String password_db = "vc";
    private Connection connection_db = null;
    private Statement st = null;
    private ResultSet rs = null;
    private Adj adj;
    private SymGraph sg;
    private ArrayList<String> a;

    public RDB(Adj adj) {
        this.adj = adj;
        this.sg = sg;
        connDB(db, user_db, password_db);
        a = new ArrayList<String>();
        CheckTables();
    }

    public ArrayList<String> getA() {
        return a;
    }


    private int CheckTables() {
        Integer retId = -1;
        Integer adj_id;
        String connection_name;
        String node_hostname;
        String node_ip;
        String int_name_oif;
        String int_name_iif;
        String adj_node_hostname;
        String adj_node_ip;

        try {
            st = connection_db.createStatement();
            rs = st.executeQuery("select a.id, a.name, n0.hostname, n0.ip, i0.name, i1.name, n1.hostname, n1.ip from adjs a " +
                    "join node n0 on a.node_id = n0.id " +
                    "join node n1 on a.adj_node_id = n1.id " +
                    "join interface i0 on a.interface_id_oif=i0.id " +
                    "join interface i1 on a.interface_id_iif=i1.id;");
            while(rs.next()) {
                retId = rs.getInt(1);
                adj_id = rs.getInt(1);
                connection_name = rs.getString(2);
                node_hostname = rs.getString(3);
                node_ip = rs.getString(4);
                int_name_oif = rs.getString(5);
                int_name_iif = rs.getString(6);
                adj_node_hostname = rs.getString(7);
                adj_node_ip = rs.getString(8);
                //System.out.println("Get Graph from db: " + adj_id + " : " + connection_name + " : " + node_hostname + " : " +
                        //node_ip + " : " + int_name_oif + " : " + int_name_iif + " : " + adj_node_hostname + " : " + adj_node_ip);
                a.add(node_hostname + ":" + node_ip + ":" + int_name_oif + ":" + int_name_iif + ":" + adj_node_hostname + ":" + adj_node_ip);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return retId;
    }


    private void connDB(String db, String user, String pass) {
        try {
            connection_db = DriverManager.getConnection(db, user_db, password_db);
            st = connection_db.createStatement();
            DSLContext create = DSL.using(connection_db, SQLDialect.POSTGRES);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

}