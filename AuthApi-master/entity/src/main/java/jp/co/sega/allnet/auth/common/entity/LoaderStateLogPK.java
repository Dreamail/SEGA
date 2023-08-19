package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the LOADER_STATE_LOGS database table.
 * 
 */
@Embeddable
public class LoaderStateLogPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "SERIAL", nullable = false)
    private String serial;

    @Column(name = "LOG_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date logDate;

    public LoaderStateLogPK() {
    }

    public LoaderStateLogPK(String serial, Date logDate) {
        this.serial = serial;
        this.logDate = logDate;
    }

    @Override
    public String toString() {
        return "LoaderStateLogPK [serial=" + serial + ", logDate=" + logDate
                + "]";
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getLogDate() {
        return this.logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LoaderStateLogPK)) {
            return false;
        }
        LoaderStateLogPK castOther = (LoaderStateLogPK) other;
        return this.serial.equals(castOther.serial)
                && this.logDate.equals(castOther.logDate);

    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.serial.hashCode();
        hash = hash * prime + this.logDate.hashCode();

        return hash;
    }
}