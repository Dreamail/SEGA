/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.poweron;

import java.io.Serializable;

import jp.co.sega.allnet.auth.api.util.RequestUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author TsuboiY
 * 
 */
public class PowerOnParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String gameId;

    private String ver;

    private String serial;

    private String ip;

    private String globalIp;

    private String firmVer;

    private String bootVer;

    private String encode = RequestUtils.DEFAULT_CHARACTER_ENCODING;

    private String formatVer = "1.00";

    private String userAgent;

    private String hops = "-1";

    private String queryString;

    private String token;

    /**
     * コンストラクタ
     * 
     * @param gameId
     * @param ver
     * @param serial
     * @param ip
     * @param globalIp
     * @param firmVer
     * @param bootVer
     * @param encode
     * @param formatVer
     * @param userAgent
     * @param hops
     * @param queryString
     */
    public PowerOnParameter(String gameId, String ver, String serial,
            String ip, String globalIp, String firmVer, String bootVer,
            String encode, String formatVer, String userAgent, String hops,
            String queryString) {
        this(gameId, ver, serial, ip, globalIp, firmVer, bootVer, encode,
                formatVer, userAgent, hops, queryString, null);

    }

    public PowerOnParameter(String gameId, String ver, String serial,
            String ip, String globalIp, String firmVer, String bootVer,
            String encode, String formatVer, String userAgent, String hops,
            String queryString, String token) {
        this.gameId = gameId;
        this.ver = ver;
        this.serial = serial;
        this.ip = ip;
        this.globalIp = globalIp;
        this.firmVer = firmVer;
        this.bootVer = bootVer;
        if (!StringUtils.isEmpty(encode)) {
            this.encode = encode;
        }
        if (!StringUtils.isEmpty(formatVer)) {
            this.formatVer = formatVer;
        }
        this.userAgent = userAgent;
        if (!StringUtils.isEmpty(hops)) {
            this.hops = hops;
        }
        this.queryString = queryString;
        this.token = token;
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
     * @return the globalIp
     */
    public String getGlobalIp() {
        return globalIp;
    }

    /**
     * @param globalIp
     *            the globalIp to set
     */
    public void setGlobalIp(String globalIp) {
        this.globalIp = globalIp;
    }

    /**
     * @return the firmVer
     */
    public String getFirmVer() {
        return firmVer;
    }

    /**
     * @param firmVer
     *            the firmVer to set
     */
    public void setFirmVer(String firmVer) {
        this.firmVer = firmVer;
    }

    /**
     * @return the bootVer
     */
    public String getBootVer() {
        return bootVer;
    }

    /**
     * @param bootVer
     *            the bootVer to set
     */
    public void setBootVer(String bootVer) {
        this.bootVer = bootVer;
    }

    /**
     * @return the encode
     */
    public String getEncode() {
        return encode;
    }

    /**
     * @param encode
     *            the encode to set
     */
    public void setEncode(String encode) {
        this.encode = encode;
    }

    /**
     * @return the formatVer
     */
    public String getFormatVer() {
        return formatVer;
    }

    /**
     * @param formatVer
     *            the formatVer to set
     */
    public void setFormatVer(String formatVer) {
        this.formatVer = formatVer;
    }

    /**
     * @return the userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @param userAgent
     *            the userAgent to set
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * @return the hops
     */
    public String getHops() {
        return hops;
    }

    /**
     * @param hops
     *            the hops to set
     */
    public void setHops(String hops) {
        this.hops = hops;
    }

    /**
     * @return the queryString
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * @param queryString
     *            the queryString to set
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

}
