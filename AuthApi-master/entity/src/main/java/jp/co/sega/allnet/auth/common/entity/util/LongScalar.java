/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.util;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author TsuboiY
 * 
 */
@Entity
public class LongScalar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private long value;

    /**
     * @return the value
     */
    public long getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(long value) {
        this.value = value;
    }

    public int intValue() {
        return (int) value;
    }

}
