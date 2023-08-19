/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authallowed.place;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;

/**
 * @author TsuboiY
 * 
 */
public class AuthAllowedPlaceRegisterParameter extends
        AbstractRegisterParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 4;

    @NotNull
    @Pattern(regexp = "\\-?[0-9]{1,10}")
    private String allnetId;

    @NotNull
    @Pattern(regexp = "[0-9A-Z]{1,5}")
    private String gameId;

    private String mode;

    public AuthAllowedPlaceRegisterParameter(String[] csv) {
        super(csv, CSV_COLUMN_SIZE);
    }

    @Override
    protected void assign(String[] inputCsv) {
        allnetId = inputCsv[1];
        gameId = inputCsv[2];
        if (gameId != null) {
            gameId = gameId.toUpperCase();
        }
        mode = inputCsv[3];
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
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode
     *            the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

}
