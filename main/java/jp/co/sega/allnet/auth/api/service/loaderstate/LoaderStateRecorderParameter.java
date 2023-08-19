/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.loaderstate;

import java.io.Serializable;

/**
 * @author TsuboiY
 * 
 */
public class LoaderStateRecorderParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serial;

    private String dvd;

    private String net;

    private String work;

    private String oldNet;

    private String deliver;

    private String nbFtd;

    private String nbDld;

    private String lastSysa;

    private String sysaSt;

    private String dldSt;

    public LoaderStateRecorderParameter(String serial, String dvd, String net,
            String work, String oldNet, String deliver, String nbFtd,
            String nbDld, String lastSysa, String sysaSt, String dldSt) {
        this.serial = serial;
        this.dvd = dvd;
        this.net = net;
        this.work = work;
        this.oldNet = oldNet;
        this.deliver = deliver;
        this.nbFtd = nbFtd;
        this.nbDld = nbDld;
        this.lastSysa = lastSysa;
        this.sysaSt = sysaSt;
        this.dldSt = dldSt;
    }

    /**
     * クエリ文字列を構築する。
     * 
     * @return
     */
    public String buildQueryString() {
        return String
                .format("serial=%s&dvd=%s&net=%s&work=%s&old_net=%s&deliver=%s&nb_ftd=%s&nb_dld=%s&last_sysa=%s&sysa_st=%s&dld_st=%s",
                        serial, dvd, net, work, oldNet, deliver, nbFtd, nbDld,
                        lastSysa, sysaSt, dldSt);
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
     * @return the dvd
     */
    public String getDvd() {
        return dvd;
    }

    /**
     * @param dvd
     *            the dvd to set
     */
    public void setDvd(String dvd) {
        this.dvd = dvd;
    }

    /**
     * @return the net
     */
    public String getNet() {
        return net;
    }

    /**
     * @param net
     *            the net to set
     */
    public void setNet(String net) {
        this.net = net;
    }

    /**
     * @return the work
     */
    public String getWork() {
        return work;
    }

    /**
     * @param work
     *            the work to set
     */
    public void setWork(String work) {
        this.work = work;
    }

    /**
     * @return the oldNet
     */
    public String getOldNet() {
        return oldNet;
    }

    /**
     * @param oldNet
     *            the oldNet to set
     */
    public void setOldNet(String oldNet) {
        this.oldNet = oldNet;
    }

    /**
     * @return the deliver
     */
    public String getDeliver() {
        return deliver;
    }

    /**
     * @param deliver
     *            the deliver to set
     */
    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    /**
     * @return the nbFtd
     */
    public String getNbFtd() {
        return nbFtd;
    }

    /**
     * @param nbFtd
     *            the nbFtd to set
     */
    public void setNbFtd(String nbFtd) {
        this.nbFtd = nbFtd;
    }

    /**
     * @return the nbDld
     */
    public String getNbDld() {
        return nbDld;
    }

    /**
     * @param nbDld
     *            the nbDld to set
     */
    public void setNbDld(String nbDld) {
        this.nbDld = nbDld;
    }

    /**
     * @return the lastSysa
     */
    public String getLastSysa() {
        return lastSysa;
    }

    /**
     * @param lastSysa
     *            the lastSysa to set
     */
    public void setLastSysa(String lastSysa) {
        this.lastSysa = lastSysa;
    }

    /**
     * @return the sysaSt
     */
    public String getSysaSt() {
        return sysaSt;
    }

    /**
     * @param sysaSt
     *            the sysaSt to set
     */
    public void setSysaSt(String sysaSt) {
        this.sysaSt = sysaSt;
    }

    /**
     * @return the dldSt
     */
    public String getDldSt() {
        return dldSt;
    }

    /**
     * @param dldSt
     *            the dldSt to set
     */
    public void setDldSt(String dldSt) {
        this.dldSt = dldSt;
    }

}
