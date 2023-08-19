/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.nsds.biz.common.security.exception;

/**
 * @author TsuboiY
 * 
 */
@SuppressWarnings("serial")
public class SecurityException extends Exception {

    public SecurityException() {
        super();
    }

    public SecurityException(String msg) {
        super(msg);
    }

    public SecurityException(Throwable e) {
        super(e);
    }

    public SecurityException(String msg, Throwable e) {
        super(msg, e);
    }

}
