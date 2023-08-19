/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.lctype;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.annotation.StringByteLength;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;

/**
 * @author NakanoY
 * 
 */
public class LcTypeRegisterParameter extends AbstractRegisterParameter
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 3;

    @NotNull
    @Pattern(regexp = "\\-?[0-9]{1,5}")
    private String lcTypeId;

    @NotNull
    @StringByteLength(max = 60, min = 1)
    private String lcTypeName;

    private String deleteFlag;

    public LcTypeRegisterParameter(String[] csv) {
        super(csv, CSV_COLUMN_SIZE);
    }

    @Override
    protected void assign(String[] inputCsv) {
        lcTypeId = inputCsv[0];
        lcTypeName = inputCsv[1];
        deleteFlag = inputCsv[2];
    }

    /**
     * @return the lcTypeId
     */
    public String getLcTypeId() {
        return lcTypeId;
    }

    /**
     * @param lcTypeId
     *            the lcTypeId to set
     */
    public void setLcTypeId(String lcTypeId) {
        this.lcTypeId = lcTypeId;
    }

    /**
     * @return the lcTypeName
     */
    public String getLcTypeName() {
        return lcTypeName;
    }

    /**
     * @param lcTypeName
     *            the lcTypeName to set
     */
    public void setLcTypeName(String lcTypeName) {
        this.lcTypeName = lcTypeName;
    }

    /**
     * @return the deleteFlag
     */
    public String getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * @param deleteFlag
     *            the deleteFlag to set
     */
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

}
