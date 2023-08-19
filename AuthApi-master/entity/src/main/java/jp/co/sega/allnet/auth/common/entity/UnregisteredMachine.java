package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import jp.co.sega.allnet.auth.common.entity.util.LongScalar;

/**
 * The persistent class for the UNREGISTERED_MACHINES database table.
 * 
 */
@Entity
@Table(name = "UNREGISTERED_MACHINES")
@NamedQueries({ @NamedQuery(name = "findUnRegisteredMachineByGameIds", query = "SELECT u FROM UnregisteredMachine u WHERE u.gameId IN (:gameIds) ORDER BY u.serial") })
@NamedNativeQueries({ @NamedNativeQuery(name = "countUnRegisteredMachineByGameIds", query = "SELECT COUNT(1) as value FROM unregistered_machines WHERE game_id IN (:gameIds)", resultClass = LongScalar.class) })
public class UnregisteredMachine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SERIAL", nullable = false)
    private String serial;

    @Column(name = "PLACE_IP")
    private String placeIp;

    @Column(name = "GAME_ID")
    private String gameId;

    @Column(name = "LAST_ACCESS", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccess;

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

    public UnregisteredMachine() {
    }

    @Override
    public String toString() {
        return "UnregisteredMachine [serial=" + serial + ", placeIp=" + placeIp
                + ", gameId=" + gameId + ", lastAccess=" + lastAccess
                + ", createDate=" + createDate + ", createUserId="
                + createUserId + ", updateDate=" + updateDate
                + ", updateUserId=" + updateUserId + "]";
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPlaceIp() {
        return placeIp;
    }

    public void setPlaceIp(String placeIp) {
        this.placeIp = placeIp;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
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