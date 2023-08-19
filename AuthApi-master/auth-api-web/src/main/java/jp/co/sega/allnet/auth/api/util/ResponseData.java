/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.util;

import java.io.Serializable;

/**
 * @author NakanoY
 * 
 */
public class ResponseData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;
    private String value;

    public ResponseData(String key, Object value) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("keyにnullもしくはブランク文字列が指定されました");
        }
        this.key = key;
        if (value != null) {
            this.value = value.toString();
        }

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
