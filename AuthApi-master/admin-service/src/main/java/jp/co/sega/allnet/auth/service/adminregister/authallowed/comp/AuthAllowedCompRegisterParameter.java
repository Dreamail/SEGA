/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authallowed.comp;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;
import jp.co.sega.allnet.auth.service.adminregister.AdminRegisterConstants;

/**
 * @author NakanoY
 * 
 */
public class AuthAllowedCompRegisterParameter extends AbstractRegisterParameter
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 4;

    @NotNull
    @Pattern(regexp = AdminRegisterConstants.REGISTER_FLAG_DEL + "|"
            + AdminRegisterConstants.REGISTER_FLAG_REG)
    private String registerFlag;

    @NotNull
    @Pattern(regexp = "[0-9A-Z]{6}")
    private String compCode;

    @NotNull
    @Pattern(regexp = "[0-9A-Z]{1,5}")
    private String gameId;

    /**
     * コンストラクタ
     * 
     * @param csv
     */
    public AuthAllowedCompRegisterParameter(String[] csv) {
        super(csv, CSV_COLUMN_SIZE);
    }

    @Override
    protected void assign(String[] inputCsv) {
        registerFlag = inputCsv[0];
        compCode = inputCsv[2];
        if (compCode != null) {
            compCode = compCode.toUpperCase();
        }
        gameId = inputCsv[3];
        if (gameId != null) {
            gameId = gameId.toUpperCase();
        }
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
     * @return the compCode
     */
    public String getCompCode() {
        return compCode;
    }

    /**
     * @param compCode
     *            the compCode to set
     */
    public void setCompCode(String compCode) {
        this.compCode = compCode;
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

}
