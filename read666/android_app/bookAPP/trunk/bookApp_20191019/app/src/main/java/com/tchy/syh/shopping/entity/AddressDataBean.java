package com.tchy.syh.shopping.entity;

import java.util.List;

public class AddressDataBean {

    /**
     * id : 1
     * name : 北京市
     * areas : {}
     */

    private String id;
    private String name;

    public List<AddressDataBean> getAreas() {
        return areas;
    }

    public void setAreas(List<AddressDataBean> areas) {
        this.areas = areas;
    }

    private List<AddressDataBean> areas;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
