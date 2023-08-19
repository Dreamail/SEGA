/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author NakanoY
 * 
 */
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    private String countryCode;

    private int regionId;

    private String name;

    private BigDecimal parentRegionId;

    private int level;

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode
     *            the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return the regionId
     */
    public int getRegionId() {
        return regionId;
    }

    /**
     * @param regionId
     *            the regionId to set
     */
    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the parentRegionId
     */
    public BigDecimal getParentRegionId() {
        return parentRegionId;
    }

    /**
     * @param parentRegionId
     *            the parentRegionId to set
     */
    public void setParentRegionId(BigDecimal parentRegionId) {
        this.parentRegionId = parentRegionId;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

}
