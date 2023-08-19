package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The persistent class for the APP_DELIVER_REPORTS database table.
 * 
 */
@Entity
@Table(name = "APP_DELIVER_REPORT_LOGS")
public class AppDeliverReportLog extends AbstractDeliverReport implements
        Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AppDeliverReportLogPK pk;

    @Column(name = "AP_VER_WORKING", nullable = false)
    private String apVerWorking;

    @Column(name = "OS_VER_WORKING")
    private String osVerWorking;

    public AppDeliverReportLog() {
    }

    @Override
    public String toString() {
        return "AppDeliverReportLog [pk=" + pk + ", filesReleased="
                + getFilesReleased() + ", filesWorking=" + getFilesWorking()
                + ", segsTotal=" + getSegsTotal() + ", segsDownloaded="
                + getSegsDownloaded() + ", authTime=" + getAuthTime()
                + ", orderTime=" + getOrderTime() + ", releaseTime="
                + getReleaseTime() + ", authState=" + getAuthState()
                + ", downloadState=" + getDownloadState() + ", description="
                + getDescription() + ", apVerReleased=" + getApVerReleased()
                + ", apVerWorking=" + apVerWorking + ", osVerReleased="
                + getOsVerReleased() + ", osVerWorking=" + osVerWorking
                + ", createUserId="
                + getCreateUserId() + "]";
    }

    /**
     * @return the pk
     */
    public AppDeliverReportLogPK getPk() {
        return pk;
    }

    /**
     * @param pk
     *            the pk to set
     */
    public void setPk(AppDeliverReportLogPK pk) {
        this.pk = pk;
    }

    /**
     * @return the apVerWorking
     */
    public String getApVerWorking() {
        return apVerWorking;
    }

    /**
     * @param apVerWorking
     *            the apVerWorking to set
     */
    public void setApVerWorking(String apVerWorking) {
        this.apVerWorking = apVerWorking;
    }

    /**
     * @return the osVerWorking
     */
    public String getOsVerWorking() {
        return osVerWorking;
    }

    /**
     * @param osVerWorking
     *            the osVerWorking to set
     */
    public void setOsVerWorking(String osVerWorking) {
        this.osVerWorking = osVerWorking;
    }

}