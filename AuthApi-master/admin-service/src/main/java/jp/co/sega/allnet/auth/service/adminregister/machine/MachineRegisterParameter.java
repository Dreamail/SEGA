/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.machine;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;

/**
 * @author TsuboiY
 * 
 */
public class MachineRegisterParameter extends AbstractRegisterParameter
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 7;

    @NotNull
    @Pattern(regexp = "\\-?[0-9]{1,10}")
    private String allnetId;

    @NotNull
    @Pattern(regexp = "(A|C)[0-9A-Z]{0,10}")
    private String serial;

    @NotNull
    @Pattern(regexp = "[0-9A-Z]{1,5}")
    private String gameId;

    @NotNull
    @Pattern(regexp = "\\-?[0-9]{1,10}")
    private String groupIndex;

    @NotNull
    @Pattern(regexp = "\\-?[0-9]{1,10}")
    private String setting;

    @Pattern(regexp = "|\\-?[0-9]{1,10}")
    private String deletionReasonNo;

    private boolean updateReservedGameId;

    public MachineRegisterParameter(String[] csv, boolean updateReservedGameId) {
        super(csv, CSV_COLUMN_SIZE);
        this.updateReservedGameId = updateReservedGameId;
    }

    @Override
    protected void assign(String[] inputCsv) {
        allnetId = inputCsv[1];
        serial = inputCsv[2];
        gameId = inputCsv[3];
        groupIndex = inputCsv[4];
        setting = inputCsv[5];
        deletionReasonNo = inputCsv[6];
    }

    /**
     * @return the allnetId
     */
    public String getAllnetId() {
        return allnetId;
    }

    /**
     * @param allnetId
     *            the allnetId to set
     */
    public void setAllnetId(String allnetId) {
        this.allnetId = allnetId;
    }

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
     * @return the groupIndex
     */
    public String getGroupIndex() {
        return groupIndex;
    }

    /**
     * @param groupIndex
     *            the groupIndex to set
     */
    public void setGroupIndex(String groupIndex) {
        this.groupIndex = groupIndex;
    }

    /**
     * @return the setting
     */
    public String getSetting() {
        return setting;
    }

    /**
     * @param setting
     *            the setting to set
     */
    public void setSetting(String setting) {
        this.setting = setting;
    }

    /**
     * @return the deletionReasonNo
     */
    public String getDeletionReasonNo() {
        return deletionReasonNo;
    }

    /**
     * @param deletionReasonNo
     *            the deletionReasonNo to set
     */
    public void setDeletionReasonNo(String deletionReasonNo) {
        this.deletionReasonNo = deletionReasonNo;
    }

    /**
     * @return the updateReservedGameId
     */
    public boolean isUpdateReservedGameId() {
        return updateReservedGameId;
    }

    /**
     * @param updateReservedGameId
     *            the updateReservedGameId to set
     */
    public void setUpdateReservedGameId(boolean updateReservedGameId) {
        this.updateReservedGameId = updateReservedGameId;
    }

}
