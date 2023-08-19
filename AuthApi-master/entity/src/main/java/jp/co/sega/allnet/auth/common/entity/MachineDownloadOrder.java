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

/**
 * The persistent class for the MACHINE_DOWNLOAD_ORDERS database table.
 * 
 */
@Entity
@Table(name = "MACHINE_DOWNLOAD_ORDERS")
@NamedQueries({
        @NamedQuery(name = "deleteMachineDownloadOrder", query = "DELETE FROM MachineDownloadOrder o WHERE o.serial = :serial"),
        @NamedQuery(name = "countMachineDownloadOrderByGameId", query = "SELECT COUNT(md.serial) FROM MachineDownloadOrder md WHERE md.gameId = :gameId") })
@NamedNativeQueries({
        @NamedNativeQuery(name = "deleteMachineDownloadOrderByGameId", query = "DELETE FROM machine_download_orders o WHERE o.game_id = :gameId", resultClass = MachineDownloadOrder.class),
        @NamedNativeQuery(name = "findMachineDownloadOrderByGameId", query = "SELECT md.serial, m.place_id, md.game_id, m.reserved_game_id, ms.place_ip, m.group_index, m.setting, md.uri, ms.last_access, ms.last_auth, md.create_date, md.update_date FROM machine_download_orders md LEFT OUTER JOIN machine_statuses ms ON md.serial = ms.serial LEFT OUTER JOIN machines m ON md.serial = m.serial WHERE md.game_id = :gameId AND md.uri is not null ORDER BY md.serial", resultSetMapping = "machineDownloadOrderMapping") })
public class MachineDownloadOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SERIAL", nullable = false)
    private String serial;
    
    @Column(name = "GAME_ID", nullable = false)
    private String gameId;

    private String uri;

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

    @Override
    public String toString() {
        return "MachineDownloadOrder [serial=" + serial + ", gameId=" + gameId + ", uri=" + uri
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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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