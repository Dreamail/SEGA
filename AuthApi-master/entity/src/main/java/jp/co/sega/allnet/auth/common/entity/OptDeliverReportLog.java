package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The persistent class for the OPT_DELIVER_REPORTS database table.
 * 
 */
@Entity
@Table(name = "OPT_DELIVER_REPORT_LOGS")
public class OptDeliverReportLog extends AbstractDeliverReport implements
        Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private OptDeliverReportLogPK pk;

    public OptDeliverReportLog() {
    }

    @Override
    public String toString() {
        return "OptDeliverReportLog [pk=" + pk + ", filesReleased="
                + getFilesReleased() + ", filesWorking=" + getFilesWorking()
                + ", segsTotal=" + getSegsTotal() + ", segsDownloaded="
                + getSegsDownloaded() + ", authTime=" + getAuthTime()
                + ", orderTime=" + getOrderTime() + ", releaseTime="
                + getReleaseTime() + ", authState=" + getAuthState()
                + ", downloadState=" + getDownloadState() + ", description="
                + getDescription() + ", apVerReleased=" + getApVerReleased()
                + ", osVerReleased=" + getOsVerReleased() + ", createUserId="
                + getCreateUserId() + "]";
    }

    /**
     * @return the pk
     */
    public OptDeliverReportLogPK getPk() {
        return pk;
    }

    /**
     * @param pk
     *            the pk to set
     */
    public void setPk(OptDeliverReportLogPK pk) {
        this.pk = pk;
    }

}