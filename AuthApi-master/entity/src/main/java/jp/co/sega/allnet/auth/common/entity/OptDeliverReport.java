package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the OPT_DELIVER_REPORTS database table.
 * 
 */
@Entity
@Table(name = "OPT_DELIVER_REPORTS")
@NamedQueries({ @NamedQuery(name = "deleteOptDeliverReports", query = "DELETE FROM OptDeliverReport adr WHERE adr.serial = :serial") })
public class OptDeliverReport extends AbstractDeliverReport implements
        Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SERIAL", nullable = false)
    private String serial;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "UPDATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Version
    private Date updateDate;

    @Column(name = "UPDATE_USER_ID", nullable = false)
    private String updateUserId;

    public OptDeliverReport() {
    }

    @Override
    public String toString() {
        return "OptDeliverReport [serial=" + serial + ", filesReleased="
                + getFilesReleased() + ", filesWorking=" + getFilesWorking()
                + ", segsTotal=" + getSegsTotal() + ", segsDownloaded="
                + getSegsDownloaded() + ", authTime=" + getAuthTime()
                + ", orderTime=" + getOrderTime() + ", releaseTime="
                + getReleaseTime() + ", authState=" + getAuthState()
                + ", downloadState=" + getDownloadState() + ", description="
                + getDescription() + ", apVerReleased=" + getApVerReleased()
                + ", osVerReleased=" + getOsVerReleased() + ", createDate="
                + createDate + ", createUserId=" + getCreateUserId()
                + ", updateDate=" + updateDate + ", updateUserId="
                + updateUserId + "]";
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
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
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