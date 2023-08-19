/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.util;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

/**
 * キーとバリューのペアを格納するクラス。
 * 
 * @author TsuboiY
 * 
 */
@Entity
@SqlResultSetMapping(name = "stringKeyValuePairMapping", entities = @EntityResult(entityClass = jp.co.sega.allnet.auth.common.entity.util.StringKeyValuePair.class))
public class StringKeyValuePair implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String key;

    private String value;

    public StringKeyValuePair() {
    }

    public StringKeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
