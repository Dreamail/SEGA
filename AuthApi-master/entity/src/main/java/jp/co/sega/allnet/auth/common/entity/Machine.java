package jp.co.sega.allnet.auth.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the MACHINES database table.
 * 
 */
@Entity
@Table(name = "MACHINES")
@NamedQueries({
        @NamedQuery(name = "findMachineBySerialGameId", query = "SELECT m FROM Machine m WHERE m.serial = :serial AND (m.gameId = :gameId OR m.reservedGameId = :gameId)"),
        @NamedQuery(name = "findMachine", query = "SELECT m FROM Machine m WHERE (m.serial = :serial OR :serial IS NULL) AND (m.allnetId = :allnetId OR :allnetId IS NULL) AND ((m.gameId = :gameId OR m.reservedGameId = :gameId) OR :gameId IS NULL) ORDER BY m.serial"),
        @NamedQuery(name = "findMachineUsingCompetence", query = "SELECT m FROM Machine m WHERE m.gameId IN (:gameIds) AND (m.serial = :serial OR :serial IS NULL) AND (m.allnetId = :allnetId OR :allnetId IS NULL) AND ((m.gameId = :gameId OR m.reservedGameId = :gameId) OR :gameId IS NULL) ORDER BY m.serial"),
        @NamedQuery(name = "countMachine", query = "SELECT COUNT(m) FROM Machine m WHERE (m.serial = :serial OR :serial IS NULL) AND (m.allnetId = :allnetId OR :allnetId IS NULL) AND ((m.gameId = :gameId OR m.reservedGameId = :gameId) OR :gameId IS NULL)"),
        @NamedQuery(name = "countMachineUsingCompetence", query = "SELECT COUNT(m) FROM Machine m WHERE m.gameId IN (:gameIds) AND (m.serial = :serial OR :serial IS NULL) AND (m.allnetId = :allnetId OR :allnetId IS NULL) AND ((m.gameId = :gameId OR m.reservedGameId = :gameId) OR :gameId IS NULL)") })
@NamedNativeQueries({
        @NamedNativeQuery(name = "findPlaceMachineByAllnetIdGameIdOrder", query = "SELECT m.* FROM machines m INNER JOIN places p ON m.allnet_id = p.allnet_id WHERE p.allnet_id = :allnetId ORDER BY m.game_id, m.serial", resultClass = Machine.class),
        @NamedNativeQuery(name = "findMachineWithPlaceBySerial", query = "SELECT * FROM machines m LEFT OUTER JOIN places p ON m.allnet_id = p.allnet_id WHERE m.serial = :serial", resultSetMapping = "placeMachineMapping") })
@SqlResultSetMappings({ @SqlResultSetMapping(name = "placeMachineMapping", entities = {
        @EntityResult(entityClass = Machine.class),
        @EntityResult(entityClass = Place.class) }) })
public class Machine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SERIAL", nullable = false)
    private String serial;

    @Column(name = "ALLNET_ID", nullable = false)
    private BigDecimal allnetId;

    @Column(name = "GAME_ID", nullable = false)
    private String gameId;

    @Column(name = "RESERVED_GAME_ID")
    private String reservedGameId;

    @Column(name = "GROUP_INDEX", nullable = false)
    private BigDecimal groupIndex = new BigDecimal(1);

    @Column(name = "SETTING", nullable = false)
    private BigDecimal setting = new BigDecimal(1);

    @Column(name = "PLACE_ID")
    private String placeId;

    @Column(name = "REGISTER_TIMESTAMP", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerTimestamp = new Date();

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

    // bi-directional many-to-one association to MachineStatus
    // * this is workaround for hibernate can't fetch one-to-one lazily
    @OneToMany(mappedBy = "machine", cascade = CascadeType.REMOVE)
    private List<MachineStatus> machineStatuses;

    public Machine() {
    }

    @Override
    public String toString() {
        return "Machine [serial=" + serial + ", allnetId=" + allnetId
                + ", gameId=" + gameId + ", reservedGameId=" + reservedGameId
                + ", groupIndex=" + groupIndex + ", setting=" + setting
                + ", placeId=" + placeId + ", registerTimestamp="
                + registerTimestamp + ", createDate=" + createDate
                + ", createUserId=" + createUserId + ", updateDate="
                + updateDate + ", updateUserId=" + updateUserId
                + ", machineStatuses=" + machineStatuses + "]";
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public BigDecimal getAllnetId() {
        return allnetId;
    }

    public void setAllnetId(BigDecimal allnetId) {
        this.allnetId = allnetId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getReservedGameId() {
        return reservedGameId;
    }

    public void setReservedGameId(String reservedGameId) {
        this.reservedGameId = reservedGameId;
    }

    public BigDecimal getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(BigDecimal groupIndex) {
        this.groupIndex = groupIndex;
    }

    public BigDecimal getSetting() {
        return setting;
    }

    public void setSetting(BigDecimal setting) {
        this.setting = setting;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Date getRegisterTimestamp() {
        return registerTimestamp;
    }

    public void setRegisterTimestamp(Date registerTimestamp) {
        this.registerTimestamp = registerTimestamp;
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


    public List<MachineStatus> getMachineStatuses() {
        return machineStatuses;
    }

    public void setMachineStatuses(List<MachineStatus> machineStatuses) {
        this.machineStatuses = machineStatuses;
    }

}