/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.comp;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.annotation.StringByteLength;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;

/**
 * @author NakanoY
 * 
 */
public class CompRegisterParameter extends AbstractRegisterParameter implements
        Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 3;

    @NotNull
    @Pattern(regexp = "[0-9A-Z]{6}")
    private String compCode;

    @NotNull
    @StringByteLength(min = 1, max = 100, encoding = "EUC-JP")
    private String compName;

    private String deleteFlag;

    /**
     * コンストラクタ
     * 
     * @param csv
     */
    public CompRegisterParameter(String[] csv) {
        super(csv, CSV_COLUMN_SIZE);
    }

    @Override
    protected void assign(String[] inputCsv) {
        compCode = inputCsv[0];
        if (compCode != null) {
            compCode = compCode.toUpperCase();
        }

        compName = inputCsv[1];

        deleteFlag = inputCsv[2];
    }

    /**
     * @return the compCode
     */
    public String getCompCode() {
        return compCode;
    }

    /**
     * @param compCode
     *            the compCode to set
     */
    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    /**
     * @return the compName
     */
    public String getCompName() {
        return compName;
    }

    /**
     * @param compName
     *            the compName to set
     */
    public void setCompName(String compName) {
        this.compName = compName;
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
