package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the MACHINE_STATUSES database table.
 * 
 */
@Entity
@Table(name = "MACHINE_STATUSES")
public class MachineStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SERIAL", nullable = false)
    private String serial;

    @Column(name = "GAME_ID")
    private String gameId;

    @Column(name = "GAME_VER")
    private String gameVer;

    @Column(name = "PLACE_IP")
    private String placeIp;

    @Column(name = "FIRM_VER")
    private BigDecimal firmVer;

    @Column(name = "BOOT_VER")
    private BigDecimal bootVer;

    @Column(name = "FORMAT_VER")
    private String formatVer;

    @Column(name = "USER_AGENT")
    private String userAgent;

    @Column(name = "LAST_ACCESS", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccess;

    @Column(name = "LAST_AUTH")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAuth;

    @Column(name = "HOPS", nullable = false)
    private BigDecimal hops;

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

    // bi-directional one-to-many association to Machine
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @ManyToOne
    @JoinColumn(name = "SERIAL", insertable = false, updatable = false)
    private Machine machine;

    public MachineStatus() {
    }

    @Override
    public String toString() {
        return "MachineStatus [serial=" + serial + ", gameId=" + gameId
                + ", gameVer=" + gameVer + ", placeIp=" + placeIp
                + ", firmVer=" + firmVer + ", bootVer=" + bootVer
                + ", formatVer=" + formatVer + ", userAgent=" + userAgent
                + ", lastAccess=" + lastAccess + ", lastAuth=" + lastAuth
                + ", hops=" + hops + ", createDate=" + createDate
                + ", createUserId=" + createUserId + ", updateDate="
                + updateDate + ", updateUserId=" + updateUserId + "]";
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameVer() {
        return gameVer;
    }

    public void setGameVer(String gameVer) {
        this.gameVer = gameVer;
    }

    public String getPlaceIp() {
        return placeIp;
    }

    public void setPlaceIp(String placeIp) {
        this.placeIp = placeIp;
    }

    public BigDecimal getFirmVer() {
        return firmVer;
    }

    public void setFirmVer(BigDecimal firmVer) {
        this.firmVer = firmVer;
    }

    public BigDecimal getBootVer() {
        return bootVer;
    }

    public void setBootVer(BigDecimal bootVer) {
        this.bootVer = bootVer;
    }

    public String getFormatVer() {
        return formatVer;
    }

    public void setFormatVer(String formatVer) {
        this.formatVer = formatVer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Date getLastAuth() {
        return lastAuth;
    }

    public void setLastAuth(Date lastAuth) {
        this.lastAuth = lastAuth;
    }

    public BigDecimal getHops() {
        return hops;
    }

    public void setHops(BigDecimal hops) {
        this.hops = hops;
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

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

}