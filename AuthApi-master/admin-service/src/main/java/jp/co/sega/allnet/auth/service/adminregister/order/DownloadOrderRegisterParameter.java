/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.order;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.annotation.StringByteLength;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;

/**
 * @author TsuboiY
 * 
 */
public class DownloadOrderRegisterParameter extends AbstractRegisterParameter
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 4;

    @NotNull
    @Pattern(regexp = "(A|C)[0-9A-Z]{0,10}")
    private String serial;

    @NotNull
    @Pattern(regexp = "[0-9A-Z]{1,5}")
    private String gameId;

    @NotNull
    @Pattern(regexp = "|[0-9\\.]{3,5}")
    private String gameVer;

    @NotNull
    @StringByteLength(max = 128)
    private String uri;

    private final boolean lengthNotEnough;

    public DownloadOrderRegisterParameter(String[] csv) {
        super(csv, CSV_COLUMN_SIZE);
        if (csv.length != CSV_COLUMN_SIZE) {
            this.lengthNotEnough = true;
        } else {
            this.lengthNotEnough = false;
        }
    }

    protected void assign(String[] inputCsv) {
        serial = inputCsv[0];
        gameId = inputCsv[1];
        gameVer = inputCsv[2];
        uri = inputCsv[3];
    }

    public static int getDefaultColumnSize() {
        return CSV_COLUMN_SIZE;
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
     * @return the gameVer
     */
    public String getGameVer() {
        return gameVer;
    }

    /**
     * @param gameVer
     *            the gameVer to set
     */
    public void setGameVer(String gameVer) {
        this.gameVer = gameVer;
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
     * @return the lengthNotEnough
     */
    public boolean isLengthNotEnough() {
        return lengthNotEnough;
    }

}
