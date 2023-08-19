/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author NakanoY
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportImage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serial;

    private String[] dfl = new String[0];

    private String[] wfl = new String[0];

    private Integer tsc;

    private Integer tdsc;

    private long at;

    private long ot;

    private long rt;

    private Integer as;

    @JsonProperty("rf_state")
    private Integer rfState;

    private String gd;

    private String dav;

    private String wdav;

    private String dov;

    private String wdov;

    public String getString() {

        StringBuilder dflSb = new StringBuilder();
        for (String s : dfl) {
            if (dflSb.length() != 0) {
                dflSb.append(",");
            }

            dflSb.append(s);
        }

        StringBuilder wflSb = new StringBuilder();
        for (String s : wfl) {
            if (wflSb.length() != 0) {
                wflSb.append(",");
            }

            wflSb.append(s);
        }

        return String
                .format("serial=%s&dfl={%s}&wfl={%s}&tsc=%s&tdsc=%s&at=%s&ot=%s&rt=%s&as=%s&rfState=%s&gd=%s&dav=%s&wdav=%s&dov=%s&wdov=%s",
                        serial, dflSb.toString(), wflSb.toString(), tsc, tdsc,
                        at, ot, rt, as, rfState, gd, dav, wdav, dov, wdov);
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
     * @return the dfl
     */
    public String[] getDfl() {
        return dfl;
    }

    /**
     * @param dfl
     *            the dfl to set
     */
    public void setDfl(String[] dfl) {
        this.dfl = dfl;
    }

    /**
     * @return the wfl
     */
    public String[] getWfl() {
        return wfl;
    }

    /**
     * @param wfl
     *            the wfl to set
     */
    public void setWfl(String[] wfl) {
        this.wfl = wfl;
    }

    /**
     * @return the tsc
     */
    public Integer getTsc() {
        return tsc;
    }

    /**
     * @param tsc
     *            the tsc to set
     */
    public void setTsc(Integer tsc) {
        this.tsc = tsc;
    }

    /**
     * @return the tdsc
     */
    public Integer getTdsc() {
        return tdsc;
    }

    /**
     * @param tdsc
     *            the tdsc to set
     */
    public void setTdsc(Integer tdsc) {
        this.tdsc = tdsc;
    }

    /**
     * @return the at
     */
    public long getAt() {
        return at;
    }

    /**
     * @param at
     *            the at to set
     */
    public void setAt(long at) {
        this.at = at;
    }

    /**
     * @return the ot
     */
    public long getOt() {
        return ot;
    }

    /**
     * @param ot
     *            the ot to set
     */
    public void setOt(long ot) {
        this.ot = ot;
    }

    /**
     * @return the rt
     */
    public long getRt() {
        return rt;
    }

    /**
     * @param rt
     *            the rt to set
     */
    public void setRt(long rt) {
        this.rt = rt;
    }

    /**
     * @return the as
     */
    public Integer getAs() {
        return as;
    }

    /**
     * @param as
     *            the as to set
     */
    public void setAs(Integer as) {
        this.as = as;
    }

    /**
     * @return the rfState
     */
    public Integer getRfState() {
        return rfState;
    }

    /**
     * @param rfState
     *            the rfState to set
     */
    public void setRfState(Integer rfState) {
        this.rfState = rfState;
    }

    /**
     * @return the gd
     */
    public String getGd() {
        return gd;
    }

    /**
     * @param gd
     *            the gd to set
     */
    public void setGd(String gd) {
        this.gd = gd;
    }

    /**
     * @return the dav
     */
    public String getDav() {
        return dav;
    }

    /**
     * @param dav
     *            the dav to set
     */
    public void setDav(String dav) {
        this.dav = dav;
    }

    /**
     * @return the wdav
     */
    public String getWdav() {
        return wdav;
    }

    /**
     * @param wdav
     *            the wdav to set
     */
    public void setWdav(String wdav) {
        this.wdav = wdav;
    }

    /**
     * @return the dov
     */
    public String getDov() {
        return dov;
    }

    /**
     * @param dov
     *            the dov to set
     */
    public void setDov(String dov) {
        this.dov = dov;
    }

    /**
     * @return the wdov
     */
    public String getWdov() {
        return wdov;
    }

    /**
     * @param wdov
     *            the wdov to set
     */
    public void setWdov(String wdov) {
        this.wdov = wdov;
    }

}
