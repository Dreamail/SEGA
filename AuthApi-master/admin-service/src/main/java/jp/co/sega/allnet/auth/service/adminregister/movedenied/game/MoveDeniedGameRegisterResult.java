/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.game;

/**
 * 
 * @author TsuboiY
 * 
 */
public class MoveDeniedGameRegisterResult {

    private int status;

    private MoveDeniedGameRegisterParameter param;

    private String message;

    public MoveDeniedGameRegisterResult(MoveDeniedGameRegisterParameter param) {
        this.param = param;
    }

    public MoveDeniedGameRegisterResult(int status,
            MoveDeniedGameRegisterParameter param, String message) {
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
    public MoveDeniedGameRegisterParameter getParam() {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(MoveDeniedGameRegisterParameter param) {
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
