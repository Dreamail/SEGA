/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.poweron;

/**
 * @author NakanoY
 * 
 */
public enum AuthStatus {

    SUCCESS(1), FAIL_GAME(-1), FAIL_MACHINE(-2), FAIL_LOC(-3);

    private int value;

    private AuthStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
