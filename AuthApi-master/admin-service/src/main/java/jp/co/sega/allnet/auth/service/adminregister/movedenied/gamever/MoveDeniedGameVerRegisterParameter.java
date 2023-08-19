/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;
import jp.co.sega.allnet.auth.service.adminregister.AdminRegisterConstants;

/**
 * @author TsuboiY
 * 
 */
public class MoveDeniedGameVerRegisterParameter extends
        AbstractRegisterParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 3;

    @NotNull
    @Pattern(regexp = AdminRegisterConstants.REGISTER_FLAG_DEL + "|"
            + AdminRegisterConstants.REGISTER_FLAG_REG)
    private String registerFlag;

    @NotNull
    @Pattern(regexp = "[0-9A-Z]{1,5}")
    private String gameId;

    @NotNull
    @Pattern(regexp = "[0-9\\.]{1,5}")
    private String gameVer;

    /**
     * コンストラクタ
     * 
     * @param csv
     */
    public MoveDeniedGameVerRegisterParameter(String[] csv) {
        super(csv, CSV_COLUMN_SIZE);
    }

    @Override
    protected void assign(String[] inputCsv) {
        registerFlag = inputCsv[0];
        gameId = inputCsv[1];
        if (gameId != null) {
            gameId = gameId.toUpperCase();
        }
        gameVer = inputCsv[2];
    }

    /**
     * @return the registerFlag
     */
    public String getRegisterFlag() {
        return registerFlag;
    }

    /**
     * @param registerFlag
     *            the registerFlag to set
     */
    public void setRegisterFlag(String registerFlag) {
        this.registerFlag = registerFlag;
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

}
