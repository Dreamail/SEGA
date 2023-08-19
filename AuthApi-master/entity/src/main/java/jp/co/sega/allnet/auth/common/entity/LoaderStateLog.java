package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the LOADER_STATE_LOGS database table.
 * 
 */
@Entity
@Table(name = "LOADER_STATE_LOGS")
@NamedQueries({
        @NamedQuery(name = "deleteLoaderStateLogs", query = "DELETE FROM LoaderStateLog l WHERE l.pk.serial = :serial") })
public class LoaderStateLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private LoaderStateLogPK pk;

    @Column(name = "DVD", nullable = false)
    private BigDecimal dvd;

    @Column(name = "NET", nullable = false)
    private BigDecimal net;

    @Column(name = "WORK", nullable = false)
    private BigDecimal work;

    @Column(name = "OLD_NET", nullable = false)
    private BigDecimal oldNet;

    @Column(name = "DELIVER", nullable = false)
    private BigDecimal deliver;

    @Column(name = "FILES_TO_DOWNLOAD", nullable = false)
    private BigDecimal filesToDownload;

    @Column(name = "FILES_DOWNLOADED", nullable = false)
    private BigDecimal filesDownloaded;

    @Column(name = "LAST_AUTH", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAuth;

    @Column(name = "LAST_AUTH_STATE", nullable = false)
    private BigDecimal lastAuthState;

    @Column(name = "DOWNLOAD_STATE", nullable = false)
    private BigDecimal downloadState;

    @Column(name = "RECEIPT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiptDate;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    @Column(name = "UPDATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Version
    private Date updateDate;

    @Column(name = "UPDATE_USER_ID", nullable = false)
    private String updateUserId;

    public LoaderStateLog() {
    }

    @Override
    public String toString() {
        return "LoaderStateLog [pk=" + pk + ", dvd=" + dvd + ", net=" + net
                + ", work=" + work + ", oldNet=" + oldNet + ", deliver="
                + deliver + ", filesToDownload=" + filesToDownload
                + ", filesDownloaded=" + filesDownloaded + ", lastAuth="
                + lastAuth + ", lastAuthState=" + lastAuthState
                + ", downloadState=" + downloadState + ", receiptDate="
                + receiptDate + ", createDate=" + createDate + ", createUserId="
                + createUserId + ", updateDate=" + updateDate
                + ", updateUserId=" + updateUserId + "]";
    }

    public LoaderStateLogPK getPk() {
        return pk;
    }

    public void setPk(LoaderStateLogPK pk) {
        this.pk = pk;
    }

    public BigDecimal getDvd() {
        return dvd;
    }

    public void setDvd(BigDecimal dvd) {
        this.dvd = dvd;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getWork() {
        return work;
    }

    public void setWork(BigDecimal work) {
        this.work = work;
    }

    public BigDecimal getOldNet() {
        return oldNet;
    }

    public void setOldNet(BigDecimal oldNet) {
        this.oldNet = oldNet;
    }

    public BigDecimal getDeliver() {
        return deliver;
    }

    public void setDeliver(BigDecimal deliver) {
        this.deliver = deliver;
    }

    public BigDecimal getFilesToDownload() {
        return filesToDownload;
    }

    public void setFilesToDownload(BigDecimal filesToDownload) {
        this.filesToDownload = filesToDownload;
    }

    public BigDecimal getFilesDownloaded() {
        return filesDownloaded;
    }

    public void setFilesDownloaded(BigDecimal filesDownloaded) {
        this.filesDownloaded = filesDownloaded;
    }

    public Date getLastAuth() {
        return lastAuth;
    }

    public void setLastAuth(Date lastAuth) {
        this.lastAuth = lastAuth;
    }

    public BigDecimal getLastAuthState() {
        return lastAuthState;
    }

    public void setLastAuthState(BigDecimal lastAuthState) {
        this.lastAuthState = lastAuthState;
    }

    public BigDecimal getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(BigDecimal downloadState) {
        this.downloadState = downloadState;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

}