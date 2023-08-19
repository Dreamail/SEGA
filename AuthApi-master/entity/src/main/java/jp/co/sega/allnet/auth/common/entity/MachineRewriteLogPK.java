package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the MACHINE_REWRITE_LOGS database table.
 * 
 */
@Embeddable
public class MachineRewriteLogPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "SERIAL", nullable = false)
    private String serial;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public MachineRewriteLogPK() {
    }

    public MachineRewriteLogPK(String serial, Date createDate) {
        this.serial = serial;
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "MachineRewriteLogPK [serial=" + serial + ", createDate="
                + createDate + "]";
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((serial == null) ? 0 : serial.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MachineRewriteLogPK other = (MachineRewriteLogPK) obj;
        if (createDate == null) {
            if (other.createDate != null)
                return false;
        } else if (!createDate.equals(other.createDate))
            return false;
        if (serial == null) {
            if (other.serial != null)
                return false;
        } else if (!serial.equals(other.serial))
            return false;
        return true;
    }

}