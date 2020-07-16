package ru.hypernet.net.gmac;

import java.sql.*;
import java.util.LinkedList;

/**
 * Created by mitya on 6/7/16.
 */
public class Sw {

    LinkedList<SwType> sw;

    public Sw() {
        sw = new LinkedList<SwType>();
    }

    public  void getSwDb(){
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/randid";
            String name = "randid";
            String password = "";

            try (Connection con = DriverManager.getConnection(url,name,password)){
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery( "SELECT id, ip_address, id_device  FROM sw " +
                                                "WHERE id_device  IN " +
                                                "(SELECT id FROM device " +
                                                "WHERE sw_enabled=1 " +
                                                "AND (device_type LIKE '%DES%' OR device_type LIKE '%DGS%'))");
                while(rs.next()){
                    sw.add(new SwType(rs.getInt(1),  rs.getString(2), rs.getInt(3)));
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (ClassNotFoundException e){

        }
    }

    public LinkedList<SwType> getSw(){
        return sw;
    }
}
