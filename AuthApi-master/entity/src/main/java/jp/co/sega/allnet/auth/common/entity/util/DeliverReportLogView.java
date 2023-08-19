/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.util;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * @author NakanoY
 * 
 */
@Entity
@SqlResultSetMappings({ @SqlResultSetMapping(name = "deliverReportLogMapping", entities = { @EntityResult(entityClass = DeliverReportLogView.class) }) })
@NamedNativeQueries({
        @NamedNativeQuery(name = "findAppDeliverReportLogs", query = "SELECT r.create_date, r.segs_total, r.segs_downloaded, r.auth_time, r.auth_state, r.download_state,CASE WHEN r.segs_downloaded = 0 THEN 0 ELSE ROUND(r.segs_downloaded / r.segs_total * 100) END AS download_ratio "
                + "FROM app_deliver_report_logs r WHERE r.serial = :serial ORDER BY r.create_date DESC", resultSetMapping = "deliverReportLogMapping"),
        @NamedNativeQuery(name = "countAppDeliverReportLogs", query = "SELECT COUNT(1) AS value FROM app_deliver_report_logs r WHERE r.serial = :serial", resultClass = LongScalar.class),
        @NamedNativeQuery(name = "findOptDeliverReportLogs", query = "SELECT r.create_date, r.segs_total, r.segs_downloaded, r.auth_time, r.auth_state, r.download_state,CASE WHEN r.segs_downloaded = 0 THEN 0 ELSE ROUND(r.segs_downloaded / r.segs_total * 100) END AS download_ratio "
                + "FROM opt_deliver_report_logs r WHERE r.serial = :serial ORDER BY r.create_date DESC", resultSetMapping = "deliverReportLogMapping"),
        @NamedNativeQuery(name = "countOptDeliverReportLogs", query = "SELECT COUNT(1) AS value FROM opt_deliver_report_logs r WHERE r.serial = :serial", resultClass = LongScalar.class) })
public class DeliverReportLogView {

    @Id
    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "SEGS_TOTAL", nullable = false)
    private int segsTotal;

    @Column(name = "SEGS_DOWNLOADED", nullable = false)
    private int segsDownloaded;

    @Column(name = "AUTH_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date authTime;

    @Column(name = "AUTH_STATE", nullable = false)
    private int authState;

    @Column(name = "DOWNLOAD_STATE", nullable = false)
    private int downloadState;

    @Column(name = "download_ratio")
    private BigDecimal downloadRatio;

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the segsTotal
     */
    public int getSegsTotal() {
        return segsTotal;
    }

    /**
     * @param segsTotal
     *            the segsTotal to set
     */
    public void setSegsTotal(int segsTotal) {
        this.segsTotal = segsTotal;
    }

    /**
     * @return the segsDownloaded
     */
    public int getSegsDownloaded() {
        return segsDownloaded;
    }

    /**
     * @param segsDownloaded
     *            the segsDownloaded to set
     */
    public void setSegsDownloaded(int segsDownloaded) {
        this.segsDownloaded = segsDownloaded;
    }

    /**
     * @return the authTime
     */
    public Date getAuthTime() {
        return authTime;
    }

    /**
     * @param authTime
     *            the authTime to set
     */
    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }

    /**
     * @return the authState
     */
    public int getAuthState() {
        return authState;
    }

    /**
     * @param authState
     *            the authState to set
     */
    public void setAuthState(int authState) {
        this.authState = authState;
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



}
