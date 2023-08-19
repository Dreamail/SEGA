/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.view;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

/**
 * @author TsuboiY
 * 
 */
@Entity
@SqlResultSetMappings({ @SqlResultSetMapping(name = "logViewMapping", entities = @EntityResult(entityClass = jp.co.sega.allnet.auth.common.entity.view.LogView.class)) })
public class LogView implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String ELEMENT_DELIMITER_DEBUG_INFO = ",";

    private static final String ELEMENT_DELIMITER_RESPONSE = "&";

    private static final String KEY_VALUE_DELIMITER = "=";

    private static final String ANCHOR_TEMPLATE_IP = "<a href=\"viewRouter.do?cmd=queryRouterFromIp&val=%1$s\">%1$s</a>";

    private static final String ANCHOR_TEMPLATE_PLACE_ID = "<a href=\"viewRouter.do?cmd=queryRouterFromPlaceId&val=%1$s\">%1$s</a>";

    private static final String ANCHOR_TEMPLATE_ALLNET_ID = "<a href=\"viewMachine.do?cmd=queryMachineFromAllnetId&val=%1$s\">%1$s</a>";

    private static final Map<String, String> REPLACE_MAP = initReplaceMap();

    @Id
    private int dummyId;

    private String serial;

    private Date accessDate;

    private String debugInfo;

    private String debugInfoPlace;

    private String response;

    /**
     * &lt;a&gt;タグ置換処理を行ったdebugInfoを返します。
     * 
     * @return
     */
    public String getLinkedDebugInfo() {
        return modifyLogElement(debugInfo, REPLACE_MAP,
                ELEMENT_DELIMITER_DEBUG_INFO, KEY_VALUE_DELIMITER);
    }

    /**
     * &lt;a&gt;タグ置換処理を行ったresponseを返します。
     * 
     * @return
     */
    public String getLinkedResponse() {
        return modifyLogElement(response, REPLACE_MAP,
                ELEMENT_DELIMITER_RESPONSE, KEY_VALUE_DELIMITER);
    }

    /**
     * ログテキストに対する置換処理を行います。
     * 
     * @param log
     * @param replaceMap
     * @param elementDelimiter
     * @param keyValueDelimiter
     * @return
     */
    private String modifyLogElement(String log, Map<String, String> replaceMap,
            String elementDelimiter, String keyValueDelimiter) {
        boolean exist = false;
        for (Entry<String, String> entry : replaceMap.entrySet()) {
            if (log.matches(".*" + entry.getKey() + keyValueDelimiter + ".*")) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            return log;
        }

        StringBuilder sb = new StringBuilder();
        String[] split = log.split(elementDelimiter);
        for (String s : split) {
            if (sb.length() > 0) {
                sb.append(elementDelimiter);
            }
            String[] kv = s.split(keyValueDelimiter);
            sb.append(kv[0]);
            sb.append(keyValueDelimiter);
            if (kv.length > 1) {
                boolean match = false;
                for (Entry<String, String> entry : replaceMap.entrySet()) {
                    if (kv[0].equals(entry.getKey())) {
                        sb.append(String.format(entry.getValue(), kv[1]));
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    sb.append(kv[1]);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 置換マップを初期化します。
     * 
     * @return
     */
    private static Map<String, String> initReplaceMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ip", ANCHOR_TEMPLATE_IP);
        map.put("place_id", ANCHOR_TEMPLATE_PLACE_ID);
        map.put("allnet_id", ANCHOR_TEMPLATE_ALLNET_ID);
        return map;
    }

    // -------------------------------------------------------------- accessors

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
     * @return the accessDate
     */
    public Date getAccessDate() {
        return accessDate;
    }

    /**
     * @param accessDate
     *            the accessDate to set
     */
    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }

    /**
     * @return the debugInfo
     */
    public String getDebugInfo() {
        return debugInfo;
    }

    /**
     * @param debugInfo
     *            the debugInfo to set
     */
    public void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }

    /**
     * @return the debugInfoPlace
     */
    public String getDebugInfoPlace() {
        return debugInfoPlace;
    }

    /**
     * @param debugInfoPlace
     *            the debugInfoPlace to set
     */
    public void setDebugInfoPlace(String debugInfoPlace) {
        this.debugInfoPlace = debugInfoPlace;
    }

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response
     *            the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

}
