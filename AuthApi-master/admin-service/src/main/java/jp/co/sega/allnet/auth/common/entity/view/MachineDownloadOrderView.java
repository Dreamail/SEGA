/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

/**
 * @author NakanoY
 * 
 */
@Entity
@SqlResultSetMappings({ @SqlResultSetMapping(name = "machineDownloadOrderMapping", entities = { @EntityResult(entityClass = MachineDownloadOrderView.class) }) })
public class MachineDownloadOrderView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String serial;

    @Column(name = "PLACE_ID")
    private String placeId;

    @Column(name = "GAME_ID")
    private String gameId;

    @Column(name = "RESERVED_GAME_ID")
    private String reservedGameId;

    @Column(name = "PLACE_IP")
    private String placeIp;

    @Column(name = "GROUP_INDEX")
    private BigDecimal groupIndex;

    private BigDecimal setting;

    private String uri;

    @Column(name = "LAST_ACCESS")
    private Date lastAccess;

    @Column(name = "LAST_AUTH")
    private Date lastAuth;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

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
     * @return the placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * @param placeId
     *            the placeId to set
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * @return the gameId
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * @param gameId
     *            the gameId to set
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * @return the reservedGameId
     */
    public String getReservedGameId() {
        return reservedGameId;
    }

    /**
     * @param reservedGameId
     *            the reservedGameId to set
     */
    public void setReservedGameId(String reservedGameId) {
        this.reservedGameId = reservedGameId;
    }

    /**
     * @return the placeIp
     */
    public String getPlaceIp() {
        return placeIp;
    }

    /**
     * @param placeIp
     *            the placeIp to set
     */
    public void setPlaceIp(String placeIp) {
        this.placeIp = placeIp;
    }

    /**
     * @return the groupIndex
     */
    public BigDecimal getGroupIndex() {
        return groupIndex;
    }

    /**
     * @param groupIndex
     *            the groupIndex to set
     */
    public void setGroupIndex(BigDecimal groupIndex) {
        this.groupIndex = groupIndex;
    }

    /**
     * @return the setting
     */
    public BigDecimal getSetting() {
        return setting;
    }

    /**
     * @param setting
     *            the setting to set
     */
    public void setSetting(BigDecimal setting) {
        this.setting = setting;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     *            the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the lastAccess
     */
    public Date getLastAccess() {
        return lastAccess;
    }

    /**
     * @param lastAccess
     *            the lastAccess to set
     */
    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    /**
     * @return the lastAuth
     */
    public Date getLastAuth() {
        return lastAuth;
    }

    /**
     * @param lastAuth
     *            the lastAuth to set
     */
    public void setLastAuth(Date lastAuth) {
        this.lastAuth = lastAuth;
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
    

}
