package com.dyniamic.demo.model;

import java.math.BigDecimal;
import java.util.Date;

public class SaRegionInfo {
    private BigDecimal id;

    private Long code;

    private String name;

    private BigDecimal parentid;

    private Short regionlevel;

    private Date createdate;

    private Short state;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getParentid() {
        return parentid;
    }

    public void setParentid(BigDecimal parentid) {
        this.parentid = parentid;
    }

    public Short getRegionlevel() {
        return regionlevel;
    }

    public void setRegionlevel(Short regionlevel) {
        this.regionlevel = regionlevel;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }
}