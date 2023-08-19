package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the MACHINE_DELETION_HISTORIES database table.
 * 
 */
@Embeddable
public class MachineDeletionHistoryPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "SERIAL", nullable = false)
    private String serial;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public MachineDeletionHistoryPK() {
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MachineDeletionHistoryPK)) {
            return false;
        }
        MachineDeletionHistoryPK castOther = (MachineDeletionHistoryPK) other;
        return this.serial.equals(castOther.serial)
                && this.createDate.equals(castOther.createDate);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.serial.hashCode();
        hash = hash * prime + this.createDate.hashCode();

        return hash;
    }

    @Override
    public String toString() {
        return "MachineDeletionHistoryPK [serial=" + serial + ", createDate="
                + createDate + "]";
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}