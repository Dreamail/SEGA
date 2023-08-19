/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.domain;

import java.io.Serializable;

/**
 * @author TsuboiY
 * 
 */
public class AuthAllowedComp implements Serializable {

    private static final long serialVersionUID = 1L;

    private String gameId;

    private String compCode;

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

}
