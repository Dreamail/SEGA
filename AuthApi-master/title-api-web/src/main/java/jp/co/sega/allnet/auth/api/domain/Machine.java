/**
 * Copyright (C) 2014 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author NakanoY
 * 
 */
public class Machine implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serial;

    private long allnetId;
    
    private String placeId;

    private Timestamp lastAccess;

    private Timestamp lastAuth;

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
     * @return the allnetId
     */
    public long getAllnetId() {
        return allnetId;
    }

    /**
     * @param allnetId the allnetId to set
     */
    public void setAllnetId(long allnetId) {
        this.allnetId = allnetId;
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
