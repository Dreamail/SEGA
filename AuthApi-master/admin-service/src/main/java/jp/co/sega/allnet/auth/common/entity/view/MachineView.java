/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author NakanoY
 * 
 */
@Entity
@SqlResultSetMapping(name = "machineViewMapping", entities = @EntityResult(entityClass = MachineView.class))
@NamedNativeQueries({
        @NamedNativeQuery(name = "findMachineViewBySerial", query = "SELECT p.place_id AS placeId, p.name AS placeName, m.serial AS serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle,m.group_index AS groupIndex,m.setting AS setting,md.uri AS uri,ms.place_ip AS placeIp,ms.last_access AS lastAccess,ms.last_auth AS lastAuth,m.register_timestamp AS updateDate,ms.hops AS hops "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN machine_download_orders md ON m.serial = md.serial LEFT OUTER JOIN places p ON m.allnet_id = p.allnet_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id "
                + "WHERE m.serial = :serial", resultSetMapping = "machineViewMapping"),
        @NamedNativeQuery(name = "findMachineViewBySerialAndGameIds", query = "SELECT p.place_id AS placeId, p.name AS placeName, m.serial AS serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle,m.group_index AS groupIndex,m.setting AS setting,md.uri AS uri,ms.place_ip AS placeIp,ms.last_access AS lastAccess,ms.last_auth AS lastAuth,m.register_timestamp AS updateDate,ms.hops AS hops "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN machine_download_orders md ON m.serial = md.serial LEFT OUTER JOIN places p ON m.allnet_id = p.allnet_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id "
                + "WHERE m.serial = :serial AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds))", resultSetMapping = "machineViewMapping"),

        @NamedNativeQuery(name = "findMachineViewByAllnetId", query = "SELECT p.place_id AS placeId, p.name AS placeName, m.serial AS serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle,m.group_index AS groupIndex,m.setting AS setting,md.uri AS uri,ms.place_ip AS placeIp,ms.last_access AS lastAccess,ms.last_auth AS lastAuth,m.register_timestamp AS updateDate,ms.hops AS hops "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN machine_download_orders md ON m.serial = md.serial INNER JOIN places p ON m.allnet_id = p.allnet_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id "
                + "WHERE m.allnet_id = :allnetId ORDER BY m.game_id, m.serial", resultSetMapping = "machineViewMapping"),
        @NamedNativeQuery(name = "findMachineViewByAllnetIdAndGameIds", query = "SELECT p.place_id AS placeId, p.name AS placeName, m.serial AS serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle,m.group_index AS groupIndex,m.setting AS setting,md.uri AS uri,ms.place_ip AS placeIp,ms.last_access AS lastAccess,ms.last_auth AS lastAuth,m.register_timestamp AS updateDate,ms.hops AS hops "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN machine_download_orders md ON m.serial = md.serial INNER JOIN places p ON m.allnet_id = p.allnet_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id "
                + "WHERE m.allnet_id = :allnetId AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY m.game_id, m.serial", resultSetMapping = "machineViewMapping"),

        @NamedNativeQuery(name = "findMachineViewByAllnetIdAndGameIdsAccessed", query = "SELECT p.place_id AS placeId, p.name AS placeName, m.serial AS serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle,m.group_index AS groupIndex,m.setting AS setting,md.uri AS uri,ms.place_ip AS placeIp,ms.last_access AS lastAccess,ms.last_auth AS lastAuth,m.register_timestamp AS updateDate,ms.hops AS hops "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN machine_download_orders md ON m.serial = md.serial INNER JOIN places p ON m.allnet_id = p.allnet_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id "
                + "WHERE m.allnet_id = :allnetId AND ms.last_access > CURRENT_TIMESTAMP - :hour/24 AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY m.game_id, m.serial", resultSetMapping = "machineViewMapping"),
        @NamedNativeQuery(name = "findMachineViewByAllnetIdAccessed", query = "SELECT p.place_id AS placeId, p.name AS placeName, m.serial AS serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle,m.group_index AS groupIndex,m.setting AS setting,md.uri AS uri,ms.place_ip AS placeIp,ms.last_access AS lastAccess,ms.last_auth AS lastAuth,m.register_timestamp AS updateDate,ms.hops AS hops "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN machine_download_orders md ON m.serial = md.serial INNER JOIN places p ON m.allnet_id = p.allnet_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id "
                + "WHERE m.allnet_id = :allnetId AND ms.last_access > CURRENT_TIMESTAMP - :hour/24 ORDER BY m.game_id, m.serial", resultSetMapping = "machineViewMapping"),

        @NamedNativeQuery(name = "findMachineViewByAllnetIdAndGameIdsNotAccessed", query = "SELECT p.place_id AS placeId, p.name AS placeName, m.serial AS serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle,m.group_index AS groupIndex,m.setting AS setting,md.uri AS uri,ms.place_ip AS placeIp,ms.last_access AS lastAccess,ms.last_auth AS lastAuth,m.register_timestamp AS updateDate,ms.hops AS hops "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN machine_download_orders md ON m.serial = md.serial INNER JOIN places p ON m.allnet_id = p.allnet_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id "
                + "WHERE m.allnet_id = :allnetId AND ms.last_access < CURRENT_TIMESTAMP - :hour/24 AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY m.game_id, m.serial", resultSetMapping = "machineViewMapping"),
        @NamedNativeQuery(name = "findMachineViewByAllnetIdNotAccessed", query = "SELECT p.place_id AS placeId, p.name AS placeName, m.serial AS serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle,m.group_index AS groupIndex,m.setting AS setting,md.uri AS uri,ms.place_ip AS placeIp,ms.last_access AS lastAccess,ms.last_auth AS lastAuth,m.register_timestamp AS updateDate,ms.hops AS hops "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN machine_download_orders md ON m.serial = md.serial INNER JOIN places p ON m.allnet_id = p.allnet_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id "
                + "WHERE m.allnet_id = :allnetId AND ms.last_access < CURRENT_TIMESTAMP - :hour/24 ORDER BY m.game_id, m.serial", resultSetMapping = "machineViewMapping"),

        @NamedNativeQuery(name = "findMachineViewByUpdateDateAndGameIds", query = "SELECT p.place_id AS placeId, p.name AS placeName, m.serial AS serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle,m.group_index AS groupIndex,m.setting AS setting,md.uri AS uri,ms.place_ip AS placeIp,ms.last_access AS lastAccess,ms.last_auth AS lastAuth,m.register_timestamp AS updateDate,ms.hops AS hops "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN machine_download_orders md ON m.serial = md.serial LEFT OUTER JOIN places p ON m.allnet_id = p.allnet_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id "
                + "WHERE (m.game_id = :gameId OR m.reserved_game_id = :gameId) AND (m.register_timestamp >= :startDate AND m.register_timestamp < :endDate) ORDER BY m.register_timestamp DESC", resultSetMapping = "machineViewMapping"),

        @NamedNativeQuery(name = "findMachineViewByRouterId", query = "SELECT p.place_id AS placeId, p.name AS placeName, m.serial AS serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle,m.group_index AS groupIndex,m.setting AS setting,md.uri AS uri,ms.place_ip AS placeIp,ms.last_access AS lastAccess,ms.last_auth AS lastAuth,m.register_timestamp AS updateDate,ms.hops AS hops "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN machine_download_orders md ON m.serial = md.serial INNER JOIN places p ON m.allnet_id = p.allnet_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id INNER JOIN routers r ON m.allnet_id = r.allnet_id AND ms.place_ip = r.place_ip "
                + "WHERE r.router_id = :routerId ORDER BY m.game_id, m.serial", resultSetMapping = "machineViewMapping"),
        @NamedNativeQuery(name = "findMachineViewByRouterIdAndGameIds", query = "SELECT p.place_id AS placeId, p.name AS placeName, m.serial AS serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle,m.group_index AS groupIndex,m.setting AS setting,md.uri AS uri,ms.place_ip AS placeIp,ms.last_access AS lastAccess,ms.last_auth AS lastAuth,m.register_timestamp AS updateDate,ms.hops AS hops "
                + "FROM machines m LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial LEFT OUTER JOIN machine_download_orders md ON m.serial = md.serial INNER JOIN places p ON m.allnet_id = p.allnet_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id INNER JOIN routers r ON m.allnet_id = r.allnet_id AND ms.place_ip = r.place_ip "
                + "WHERE r.router_id = :routerId AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY m.game_id, m.serial", resultSetMapping = "machineViewMapping") })
public class MachineView implements Serializable {
    private static final long serialVersionUID = 1L;

    private String placeId;

    private String placeName;

    @Id
    @NotNull
    @Pattern(regexp = "(A|C)[0-9A-Z]{10}")
    private String serial;

    @NotNull
    @Pattern(regexp = "[0-9A-Z]{1,5}")
    private String gameId;

    private String gameTitle;

    private String reservedGameId;

    private String reservedGameTitle;

    private Integer groupIndex;

    private Integer setting;

    private String uri;

    private String placeIp;

    private Date lastAccess;

    private Date lastAuth;

    private Date updateDate;

    private BigDecimal hops;

    @Transient
    @NotNull
    private BigDecimal allnetId;

    public boolean isCheckAuth() {
        if (lastAuth == null) {
            return true;
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Calendar lastAuthCal = Calendar.getInstance();
        lastAuthCal.setTime(lastAuth);
        if (cal.after(lastAuthCal)) {
            return true;
        }
        return false;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getReservedGameId() {
        return reservedGameId;
    }

    public void setReservedGameId(String reservedGameId) {
        this.reservedGameId = reservedGameId;
    }

    public String getReservedGameTitle() {
        return reservedGameTitle;
    }

    public void setReservedGameTitle(String reservedGameTitle) {
        this.reservedGameTitle = reservedGameTitle;
    }

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    public Integer getSetting() {
        return setting;
    }

    public void setSetting(Integer setting) {
        this.setting = setting;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPlaceIp() {
        return placeIp;
    }

    public void setPlaceIp(String placeIp) {
        this.placeIp = placeIp;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public BigDecimal getHops() {
        return hops;
    }

    public void setHops(BigDecimal hops) {
        this.hops = hops;
    }

    public BigDecimal getAllnetId() {
        return allnetId;
    }

    public void setAllnetId(BigDecimal allnetId) {
        this.allnetId = allnetId;
    }

}
