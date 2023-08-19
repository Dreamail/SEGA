/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever;

/**
 * 
 * @author TsuboiY
 * 
 */
public class MoveDeniedGameVerRegisterResult {

    private int status;

    private MoveDeniedGameVerRegisterParameter param;

    private String message;

    public MoveDeniedGameVerRegisterResult(
            MoveDeniedGameVerRegisterParameter param) {
        this.param = param;
    }

    public MoveDeniedGameVerRegisterResult(int status,
            MoveDeniedGameVerRegisterParameter param, String message) {
        this.status = status;
        this.param = param;
        this.message = message;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the param
     */
    public MoveDeniedGameVerRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(MoveDeniedGameVerRegisterParameter param) {
        this.param = param;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
