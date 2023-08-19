/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.exception;

/**
 * アプリケーション共通例外
 * 
 * @author TsuboiY
 * 
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * msgからApplicationExceptionを構築します。
     * 
     * @param msg
     */
    public ApplicationException(String msg) {
        super(msg);
    }

    /**
     * causeからApplicationExceptionを構築します。
     * 
     * @param cause
     */
    public ApplicationException(Throwable cause) {
        super(cause);
    }

    /**
     * msgとcauseからApplicationExceptionを構築します。
     * 
     * @param msg
     * @param cause
     */
    public ApplicationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
