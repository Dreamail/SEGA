/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author TsuboiY
 * 
 */
public class MachineTable implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal region0Id;

    private String placeId;

    private String name;

    private String serial;

    private int groupIndex;

    private Timestamp lastAccess;

    private Timestamp lastAuth;

    /**
     * @return the region0Id
     */
    public BigDecimal getRegion0Id() {
        return region0Id;
    }

    /**
     * @param region0Id
     *            the region0Id to set
     */
    public void setRegion0Id(BigDecimal region0Id) {
        this.region0Id = region0Id;
    }

    /**
     * @return the placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * @param placeId
     *            the placeId to set
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
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
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial
     *            the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return the groupIndex
     */
    public int getGroupIndex() {
        return groupIndex;
    }

    /**
     * @param groupIndex
     *            the groupIndex to set
     */
    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    /**
     * @return the lastAccess
     */
    public Timestamp getLastAccess() {
        return lastAccess;
    }

    /**
     * @param lastAccess
     *            the lastAccess to set
     */
    public void setLastAccess(Timestamp lastAccess) {
        this.lastAccess = lastAccess;
    }

    /**
     * @return the lastAuth
     */
    public Timestamp getLastAuth() {
        return lastAuth;
    }

    /**
     * @param lastAuth
     *            the lastAuth to set
     */
    public void setLastAuth(Timestamp lastAuth) {
        this.lastAuth = lastAuth;
    }

}
