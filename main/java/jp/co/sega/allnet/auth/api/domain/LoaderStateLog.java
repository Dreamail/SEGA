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
public class LoaderStateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serial;

    private Timestamp logDate;

    private BigDecimal dvd;

    private BigDecimal net;

    private BigDecimal work;

    private BigDecimal oldNet;

    private BigDecimal deliver;

    private BigDecimal filesToDownload;

    private BigDecimal filesDownloaded;

    private Timestamp lastAuth;

    private BigDecimal lastAuthState;

    private BigDecimal downloadState;

    private Timestamp receiptDate;

    private Timestamp createDate;

    private String createUserId;

    private Timestamp updateDate;

    private String updateUserId;

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
     * @return the logDate
     */
    public Timestamp getLogDate() {
        return logDate;
    }

    /**
     * @param logDate
     *            the logDate to set
     */
    public void setLogDate(Timestamp logDate) {
        this.logDate = logDate;
    }

    /**
     * @return the dvd
     */
    public BigDecimal getDvd() {
        return dvd;
    }

    /**
     * @param dvd
     *            the dvd to set
     */
    public void setDvd(BigDecimal dvd) {
        this.dvd = dvd;
    }

    /**
     * @return the net
     */
    public BigDecimal getNet() {
        return net;
    }

    /**
     * @param net
     *            the net to set
     */
    public void setNet(BigDecimal net) {
        this.net = net;
    }

    /**
     * @return the work
     */
    public BigDecimal getWork() {
        return work;
    }

    /**
     * @param work
     *            the work to set
     */
    public void setWork(BigDecimal work) {
        this.work = work;
    }

    /**
     * @return the oldNet
     */
    public BigDecimal getOldNet() {
        return oldNet;
    }

    /**
     * @param oldNet
     *            the oldNet to set
     */
    public void setOldNet(BigDecimal oldNet) {
        this.oldNet = oldNet;
    }

    /**
     * @return the deliver
     */
    public BigDecimal getDeliver() {
        return deliver;
    }

    /**
     * @param deliver
     *            the deliver to set
     */
    public void setDeliver(BigDecimal deliver) {
        this.deliver = deliver;
    }

    /**
     * @return the filesToDownload
     */
    public BigDecimal getFilesToDownload() {
        return filesToDownload;
    }

    /**
     * @param filesToDownload
     *            the filesToDownload to set
     */
    public void setFilesToDownload(BigDecimal filesToDownload) {
        this.filesToDownload = filesToDownload;
    }

    /**
     * @return the filesDownloaded
     */
    public BigDecimal getFilesDownloaded() {
        return filesDownloaded;
    }

    /**
     * @param filesDownloaded
     *            the filesDownloaded to set
     */
    public void setFilesDownloaded(BigDecimal filesDownloaded) {
        this.filesDownloaded = filesDownloaded;
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

    /**
     * @return the lastAuthState
     */
    public BigDecimal getLastAuthState() {
        return lastAuthState;
    }

    /**
     * @param lastAuthState
     *            the lastAuthState to set
     */
    public void setLastAuthState(BigDecimal lastAuthState) {
        this.lastAuthState = lastAuthState;
    }

    /**
     * @return the downloadState
     */
    public BigDecimal getDownloadState() {
        return downloadState;
    }

    /**
     * @param downloadState
     *            the downloadState to set
     */
    public void setDownloadState(BigDecimal downloadState) {
        this.downloadState = downloadState;
    }

    /**
     * @return the receiptDate
     */
    public Timestamp getReceiptDate() {
        return receiptDate;
    }

    /**
     * @param receiptDate
     *            the receiptDate to set
     */
    public void setReceiptDate(Timestamp receiptDate) {
        this.receiptDate = receiptDate;
    }

    /**
     * @return the createDate
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the createUserId
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * @param createUserId
     *            the createUserId to set
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * @return the updateDate
     */
    public Timestamp getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            the updateDate to set
     */
    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the updateUserId
     */
    public String getUpdateUserId() {
        return updateUserId;
    }

    /**
     * @param updateUserId
     *            the updateUserId to set
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

}
