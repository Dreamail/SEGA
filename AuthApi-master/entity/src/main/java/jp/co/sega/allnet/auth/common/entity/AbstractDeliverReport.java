package jp.co.sega.allnet.auth.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the APP_DELIVER_REPORTS database table.
 * 
 */
@MappedSuperclass
public abstract class AbstractDeliverReport {

    @Column(name = "FILES_RELEASED")
    private String filesReleased;

    @Column(name = "FILES_WORKING")
    private String filesWorking;

    @Column(name = "SEGS_TOTAL", nullable = false)
    private int segsTotal;

    @Column(name = "SEGS_DOWNLOADED", nullable = false)
    private int segsDownloaded;

    @Column(name = "AUTH_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date authTime;

    @Column(name = "ORDER_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;

    @Column(name = "RELEASE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseTime;

    @Column(name = "AUTH_STATE", nullable = false)
    private int authState;

    @Column(name = "DOWNLOAD_STATE", nullable = false)
    private int downloadState;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "AP_VER_RELEASED")
    private String apVerReleased;

    @Column(name = "OS_VER_RELEASED")
    private String osVerReleased;

    @Column(name = "CREATE_USER_ID", nullable = false)
    private String createUserId;

    public AbstractDeliverReport() {
    }


    /**
     * @return the filesReleased
     */
    public String getFilesReleased() {
        return filesReleased;
    }

    /**
     * @param filesReleased
     *            the filesReleased to set
     */
    public void setFilesReleased(String filesReleased) {
        this.filesReleased = filesReleased;
    }

    /**
     * @return the filesWorking
     */
    public String getFilesWorking() {
        return filesWorking;
    }

    /**
     * @param filesWorking
     *            the filesWorking to set
     */
    public void setFilesWorking(String filesWorking) {
        this.filesWorking = filesWorking;
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
     * @return the orderTime
     */
    public Date getOrderTime() {
        return orderTime;
    }

    /**
     * @param orderTime
     *            the orderTime to set
     */
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * @return the releaseTime
     */
    public Date getReleaseTime() {
        return releaseTime;
    }

    /**
     * @param releaseTime
     *            the releaseTime to set
     */
    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the apVerReleased
     */
    public String getApVerReleased() {
        return apVerReleased;
    }

    /**
     * @param apVerReleased
     *            the apVerReleased to set
     */
    public void setApVerReleased(String apVerReleased) {
        this.apVerReleased = apVerReleased;
    }

    /**
     * @return the osVerReleased
     */
    public String getOsVerReleased() {
        return osVerReleased;
    }

    /**
     * @param osVerReleased
     *            the osVerReleased to set
     */
    public void setOsVerReleased(String osVerReleased) {
        this.osVerReleased = osVerReleased;
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


}