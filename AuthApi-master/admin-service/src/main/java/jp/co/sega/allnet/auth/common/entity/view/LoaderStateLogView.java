/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

import jp.co.sega.allnet.auth.common.entity.util.CsvXmlExportable;
import jp.co.sega.allnet.auth.csv.CsvUtils;

/**
 * @author NakanoY
 * 
 */
@Entity
@SqlResultSetMappings({ @SqlResultSetMapping(name = "loaderStateLogMapping", entities = { @EntityResult(entityClass = LoaderStateLogView.class) }) })
public class LoaderStateLogView implements Serializable, CsvXmlExportable {

    private static final long serialVersionUID = 1L;

    @Id
    private String serial;

    @Column(name = "PLACE_ID")
    private String placeId;

    @Column(name = "NAME")
    private String placeName;

    @Column(name = "download_ratio")
    private BigDecimal downloadRatio;

    private int dvd;

    private int net;

    private int work;

    @Column(name = "OLD_NET")
    private int oldnet;

    private int deliver;

    @Column(name = "FILES_TO_DOWNLOAD")
    private int filesToDownload;

    @Column(name = "FILES_DOWNLOADED")
    private int filesDownloaded;

    @Column(name = "LAST_AUTH")
    private Date lastAuth;

    @Column(name = "LAST_AUTH_STATE")
    private int lastAuthState;

    @Column(name = "DOWNLOAD_STATE")
    private int downloadState;

    @Column(name = "RECEIPT_DATE")
    private Date receiptDate;

    @Override
    public String createCsvLine() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_YEAR_TO_MSEC1);

        StringBuilder sb = new StringBuilder();
        sb.append(CsvUtils.escapeForCsv(serial));
        sb.append(",");
        sb.append(CsvUtils.escapeForCsv(placeId));
        sb.append(",");
        sb.append(CsvUtils.escapeForCsv(placeName));
        sb.append(",");
        sb.append(downloadRatio);
        sb.append(",");
        sb.append(dvd);
        sb.append(",");
        sb.append(net);
        sb.append(",");
        sb.append(work);
        sb.append(",");
        sb.append(oldnet);
        sb.append(",");
        sb.append(deliver);
        sb.append(",");
        sb.append(filesToDownload);
        sb.append(",");
        sb.append(filesDownloaded);
        sb.append(",");
        sb.append(df.format(lastAuth));
        sb.append(",");
        sb.append(lastAuthState);
        sb.append(",");
        sb.append(downloadState);
        sb.append(",");
        if (receiptDate != null) {
            sb.append(df.format(receiptDate));
        } else {
            sb.append("-");
        }
        sb.append("\n");

        return sb.toString();
    }

    @Override
    public String createXmlLine() {
        throw new UnsupportedOperationException();
    }

    public boolean isCheckAuth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Calendar lastAuthCal = Calendar.getInstance();
        lastAuthCal.setTime(lastAuth);
        if (cal.after(lastAuthCal)) {
            return true;
        }
        return false;
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
     * @return the placeName
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * @param placeName
     *            the placeName to set
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    /**
     * @return the downloadRatio
     */
    public BigDecimal getDownloadRatio() {
        return downloadRatio;
    }

    /**
     * @param downloadRatio
     *            the downloadRatio to set
     */
    public void setDownloadRatio(BigDecimal downloadRatio) {
        this.downloadRatio = downloadRatio;
    }

    /**
     * @return the dvd
     */
    public int getDvd() {
        return dvd;
    }

    /**
     * @param dvd
     *            the dvd to set
     */
    public void setDvd(int dvd) {
        this.dvd = dvd;
    }

    /**
     * @return the net
     */
    public int getNet() {
        return net;
    }

    /**
     * @param net
     *            the net to set
     */
    public void setNet(int net) {
        this.net = net;
    }

    /**
     * @return the work
     */
    public int getWork() {
        return work;
    }

    /**
     * @param work
     *            the work to set
     */
    public void setWork(int work) {
        this.work = work;
    }

    /**
     * @return the oldnet
     */
    public int getOldnet() {
        return oldnet;
    }

    /**
     * @param oldnet
     *            the oldnet to set
     */
    public void setOldnet(int oldnet) {
        this.oldnet = oldnet;
    }

    /**
     * @return the deliver
     */
    public int getDeliver() {
        return deliver;
    }

    /**
     * @param deliver
     *            the deliver to set
     */
    public void setDeliver(int deliver) {
        this.deliver = deliver;
    }

    /**
     * @return the filesToDownload
     */
    public int getFilesToDownload() {
        return filesToDownload;
    }

    /**
     * @param filesToDownload
     *            the filesToDownload to set
     */
    public void setFilesToDownload(int filesToDownload) {
        this.filesToDownload = filesToDownload;
    }

    /**
     * @return the filesDownloaded
     */
    public int getFilesDownloaded() {
        return filesDownloaded;
    }

    /**
     * @param filesDownloaded
     *            the filesDownloaded to set
     */
    public void setFilesDownloaded(int filesDownloaded) {
        this.filesDownloaded = filesDownloaded;
    }

    /**
     * @return the lastAuth
     */
    public Date getLastAuth() {
        return lastAuth;
    }

    /**
     * @param lastAuth
     *            the lastAuth to set
     */
    public void setLastAuth(Date lastAuth) {
        this.lastAuth = lastAuth;
    }

    /**
     * @return the lastAuthState
     */
    public int getLastAuthState() {
        return lastAuthState;
    }

    /**
     * @param lastAuthState
     *            the lastAuthState to set
     */
    public void setLastAuthState(int lastAuthState) {
        this.lastAuthState = lastAuthState;
    }

    /**
     * @return the downloadState
     */
    public int getDownloadState() {
        return downloadState;
    }

    /**
     * @param downloadState
     *            the downloadState to set
     */
    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    /**
     * @return the receiptDate
     */
    public Date getReceiptDate() {
        return receiptDate;
    }

    /**
     * @param receiptDate
     *            the receiptDate to set
     */
    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

}
