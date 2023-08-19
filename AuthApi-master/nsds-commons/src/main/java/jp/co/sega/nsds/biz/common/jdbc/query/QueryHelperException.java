/**
 * Copyright (C) 2013 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.nsds.biz.common.jdbc.query;

/**
 * @author TsuboiY
 * 
 */
@SuppressWarnings("serial")
public class QueryHelperException extends Exception {

    public QueryHelperException(String msg) {
        super(msg);
    }

    public QueryHelperException(Throwable e) {
        super(e);
    }

}
