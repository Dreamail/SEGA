/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author TsuboiY
 * 
 */
public abstract class AbstractLogUtils {

    private final String id;

    protected AbstractLogUtils() {
        this.id = generateId();
    }

    /**
     * リクエストIDを生成する。
     * 
     * @return
     */
    protected String generateId() {
        StringBuilder sb = new StringBuilder();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        sb.append(df.format(new Date()));
        sb.append(":");
        sb.append(RandomStringUtils.randomAlphanumeric(16));
        return sb.toString();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
}
