/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.dlorder;

import java.io.Serializable;
import java.util.List;

/**
 * @author NakanoY
 * 
 */
public class DownloadOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serial;

    private int allnetId;

    private String countryCode;

    private int groupIndex;

    private int setting;

    private String uri;

    private List<String> groupSerials;

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
     * @return the allnetId
     */
    public int getAllnetId() {
        return allnetId;
    }

    /**
     * @param allnetId
     *            the allnetId to set
     */
    public void setAllnetId(int allnetId) {
        this.allnetId = allnetId;
    }

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
     * @return the setting
     */
    public int getSetting() {
        return setting;
    }

    /**
     * @param setting
     *            the setting to set
     */
    public void setSetting(int setting) {
        this.setting = setting;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     *            the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the groupSerials
     */
    public List<String> getGroupSerials() {
        return groupSerials;
    }

    /**
     * @param groupSerials
     *            the groupSerials to set
     */
    public void setGroupSerials(List<String> groupSerials) {
        this.groupSerials = groupSerials;
    }

}
