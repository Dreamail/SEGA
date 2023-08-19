/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author TsuboiY
 * 
 */
public class AuthAllowedPlace implements Serializable {

    private static final long serialVersionUID = 1L;

    private String gameId;

    private BigDecimal allnetId;

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

}
