/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.dlorder;

import java.io.Serializable;

import jp.co.sega.allnet.auth.api.util.RequestUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author NakanoY
 * 
 */
public class DownloadOrderParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String gameId;

    private String ver;

    private String serial;

    private String ip;

    private String encode;

    public DownloadOrderParameter(String gameId, String ver, String serial,
            String ip, String encode) {
        this.gameId = gameId;
        this.ver = ver;
        this.serial = serial;
        this.ip = ip;
        this.encode = encode;
    }

    /**
     * クエリ文字列を構築する。
     * 
     * @return
     */
    public String buildQueryString() {
        return String.format("gameId=%s&ver=%s&serial=%s&ip=%s&encode=%s",
                gameId, ver, serial, ip, encode);
    }

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
     * @return the ver
     */
    public String getVer() {
        return ver;
    }

    /**
     * @param ver
     *            the ver to set
     */
    public void setVer(String ver) {
        this.ver = ver;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial
     *            the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the encode
     */
    public String getEncode() {
        if (StringUtils.isEmpty(encode)) {
            return RequestUtils.DEFAULT_CHARACTER_ENCODING;
        }
        return encode;
    }

    /**
     * @param encode
     *            the encode to set
     */
    public void setEncode(String encode) {
        this.encode = encode;
    }

}
