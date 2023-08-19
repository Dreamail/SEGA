/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

/**
 * @author NakanoY
 * 
 */
@Entity
@SqlResultSetMapping(name = "machineDeletionHistoryViewMapping", entities = @EntityResult(entityClass = jp.co.sega.allnet.auth.common.entity.view.MachineDeletionHistoryView.class))
@NamedNativeQueries({
        @NamedNativeQuery(name = "findMachineDeletionHistoryView", query = "SELECT ROW_NUMBER() OVER(ORDER BY m.create_date, m.serial) AS dummyId, m.create_date AS createDate, m.serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle, m.allnet_id AS allnetId, m.place_name AS placeName, m.reason_id AS reasonId, mr.description FROM machine_deletion_histories m LEFT OUTER JOIN machine_deletion_reasons mr ON m.reason_id = mr.reason_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id WHERE m.create_date >= :startDate AND m.create_date < :endDate ORDER BY m.create_date DESC", resultSetMapping = "machineDeletionHistoryViewMapping"),
        @NamedNativeQuery(name = "findMachineDeletionHistoryViewByReason", query = "SELECT ROW_NUMBER() OVER(ORDER BY m.create_date, m.serial) AS dummyId, m.create_date AS createDate, m.serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle, m.allnet_id AS allnetId, m.place_name AS placeName, m.reason_id AS reasonId, mr.description FROM machine_deletion_histories m LEFT OUTER JOIN machine_deletion_reasons mr ON m.reason_id = mr.reason_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id WHERE m.reason_id = :reasonId AND (m.create_date >= :startDate AND m.create_date < :endDate) ORDER BY m.create_date DESC", resultSetMapping = "machineDeletionHistoryViewMapping"),
        @NamedNativeQuery(name = "findMachineDeletionHistoryViewBySerial", query = "SELECT ROW_NUMBER() OVER(ORDER BY m.create_date, m.serial) AS dummyId, m.create_date AS createDate, m.serial, m.game_id AS gameId, g1.title AS gameTitle, m.reserved_game_id AS reservedGameId, g2.title AS reservedGameTitle, m.allnet_id AS allnetId, m.place_name AS placeName, m.reason_id AS reasonId, mr.description FROM machine_deletion_histories m LEFT OUTER JOIN machine_deletion_reasons mr ON m.reason_id = mr.reason_id LEFT OUTER JOIN games g1 ON m.game_id = g1.game_id LEFT OUTER JOIN games g2 ON m.reserved_game_id = g2.game_id WHERE m.serial = :serial ORDER BY m.create_date DESC", resultSetMapping = "machineDeletionHistoryViewMapping") })
public class MachineDeletionHistoryView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int dummyId;

    private Date createDate;

    private String serial;

    private String gameId;

    private String gameTitle;

    private String reservedGameId;

    private String reservedGameTitle;

    private BigDecimal allnetId;

    private String placeName;

    private BigDecimal reasonId;

    private String description;

    public int getDummyId() {
        return dummyId;
    }

    public void setDummyId(int dummyId) {
        this.dummyId = dummyId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public BigDecimal getAllnetId() {
        return allnetId;
    }

    public void setAllnetId(BigDecimal allnetId) {
        this.allnetId = allnetId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public BigDecimal getReasonId() {
        return reasonId;
    }

    public void setReasonId(BigDecimal reasonId) {
        this.reasonId = reasonId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
