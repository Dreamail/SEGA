/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.poweron;

/**
 * @author TsuboiY
 * 
 */
public enum AuthType {

    NORMAL(1), AUTO(2), PLACE_AUTO(3), COMP_AUTO_MOVE(4), AUTO_MOVE(5), DEBUG(
            -1);

    private int value;

    private AuthType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static AuthType fromValue(int value) {
        for (AuthType t : AuthType.values()) {
            if (t.value == value) {
                return t;
            }
        }
        throw new IllegalArgumentException(String.valueOf(value));
    }

}
