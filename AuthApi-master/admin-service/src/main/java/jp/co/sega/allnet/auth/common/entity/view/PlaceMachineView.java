/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;

import jp.co.sega.allnet.auth.common.entity.util.CsvWritable;
import jp.co.sega.allnet.auth.csv.CsvUtils;

/**
 * @author TsuboiY
 * 
 */
public class PlaceMachineView implements Serializable, CsvWritable {

    private static final long serialVersionUID = 1L;

    private String placeName;

    private BigDecimal allnetId;

    private String serial;

    private String gameId;

    private BigDecimal groupIndex;

    private BigDecimal setting;

    @Override
    public Writer writeCsvLine(Writer writer) throws IOException {
        writer.write(CsvUtils.escapeForCsv(placeName));
        writer.write(",");
        if (allnetId != null) {
            writer.write(allnetId.toPlainString());
        }
        writer.write(",");
        writer.write(CsvUtils.escapeForCsv(serial));
        writer.write(",");
        writer.write(CsvUtils.escapeForCsv(gameId));
        writer.write(",");
        if (groupIndex != null) {
            writer.write(groupIndex.toPlainString());
        }
        writer.write(",");
        if (setting != null) {
            writer.write(setting.toPlainString());
        }
        writer.write("\n");

        return writer;
    }

    /**
     * @return the placeName
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * @param placeName
     *            the placeName to set
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    /**
     * @return the allnetId
     */
    public BigDecimal getAllnetId() {
        return allnetId;
    }

    /**
     * @param allnetId
     *            the allnetId to set
     */
    public void setAllnetId(BigDecimal allnetId) {
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

}
