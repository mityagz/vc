package db;

import datastruct.Adj;
import datastruct.SymGraph;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.*;

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

    public RDB(Adj adj) {
        this.adj = adj;
        this.sg = sg;
        connDB(db, user_db, password_db);
        //IsExistAdj(1, 1, 1, 1, 1, "x");
    }

    private int CheckTables() {
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
            rs = st.executeQuery("select a.id, a.name, n0.hostname, n0.ip, i0.name, i1.name, n1.hostname, n1.ip from adjs a " +
                    "join node n0 on a.node_id = n0.id " +
                    "join node n1 on a.adj_node_id = n1.id " +
                    "join interface i0 on a.interface_id_oif=i0.id " +
                    "join interface i1 on a.interface_id_iif=i1.id;");

            if(rs.next()) {
                retId = rs.getInt(1);
                if(retId != 0) {
                    System.out.println("Node id found: " + retId);
                }

            } else {
                System.out.println("Node id isn't found in adj table");
                return retId;
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
            //create.select().from(ADJS).fetch();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
