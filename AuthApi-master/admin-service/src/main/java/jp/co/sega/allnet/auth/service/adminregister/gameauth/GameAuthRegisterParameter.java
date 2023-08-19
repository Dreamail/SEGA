/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.gameauth;

import java.io.Serializable;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;

import org.apache.commons.lang3.StringUtils;

/**
 * AdminRegister?cmd=place のvalパラメータを保持するクラス。
 * 
 * @author NakanoY
 * 
 */
public class GameAuthRegisterParameter extends AbstractRegisterParameter
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 4;

    private static final String DEFAULT_AUTH_TYPE = "1";

    @NotNull
    @Pattern(regexp = "[0-9A-Z]{3,5}")
    private String gameId;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("65.54")
    private String gameVer;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("32767")
    private String auth;

    @NotNull
    @Pattern(regexp = "[A-Z]{3}")
    private String countryCode;

    /**
     * コンストラクタ
     * 
     * @param csv
     */
    public GameAuthRegisterParameter(String[] csv) {
        super(csv, CSV_COLUMN_SIZE);
    }

    @Override
    protected void assign(String[] inputCsv) {
        gameId = inputCsv[0];
        if (gameId != null) {
            gameId = gameId.toUpperCase();
        }

        gameVer = inputCsv[1];
        auth = inputCsv[2];
        if (StringUtils.isEmpty(auth)) {
            auth = DEFAULT_AUTH_TYPE;
        }

        countryCode = inputCsv[3];
        if (countryCode != null) {
            countryCode = countryCode.toUpperCase();
        }
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
     * @return the auth
     */
    public String getAuth() {
        return auth;
    }

    /**
     * @param auth
     *            the auth to set
     */
    public void setAuth(String auth) {
        this.auth = auth;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode
     *            the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
