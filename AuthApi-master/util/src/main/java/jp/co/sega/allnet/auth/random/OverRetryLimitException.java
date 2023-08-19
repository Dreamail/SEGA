/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.random;

/**
 * @author TsuboiY
 * 
 */
@SuppressWarnings("serial")
public class OverRetryLimitException extends RuntimeException {

    private int retryLimit;

    public OverRetryLimitException(int retryLimit) {
        this.retryLimit = retryLimit;
    }

    /**
     * @return the retryLimit
     */
    public int getRetryLimit() {
        return retryLimit;
    }

}
