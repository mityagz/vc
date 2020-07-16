package ru.hypernet.net.gmac;

/**
 * Created by mitya on 6/7/16.
 */
public class SwType {
    Integer id;
    String ip_address;
    Integer type;

    SwType(Integer id, String ip_address, Integer type){
        this.id = id;
        this.ip_address = ip_address;
        this.type = type;
    }

    public String getIp(){
        return ip_address;
    }

    public Integer getType(){
        return type;
    }

    public Integer getId() { return id; }
}
