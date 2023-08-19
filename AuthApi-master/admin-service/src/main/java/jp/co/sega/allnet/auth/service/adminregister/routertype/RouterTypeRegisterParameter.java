/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.routertype;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.annotation.StringByteLength;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;

/**
 * @author NakanoY
 * 
 */
public class RouterTypeRegisterParameter extends AbstractRegisterParameter
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 3;

    @NotNull
    @Pattern(regexp = "\\-?[0-9]{1,5}")
    private String routerTypeId;

    @NotNull
    @StringByteLength(max = 60, min = 1)
    private String routerTypeName;

    private String deleteFlag;

    /**
     * コンストラクタ
     * 
     * @param csv
     */
    public RouterTypeRegisterParameter(String[] csv) {
        super(csv, CSV_COLUMN_SIZE);
    }

    @Override
    protected void assign(String[] inputCsv) {
        routerTypeId = inputCsv[0];
        routerTypeName = inputCsv[1];
        deleteFlag = inputCsv[2];
    }

    /**
     * @return the routerTypeId
     */
    public String getRouterTypeId() {
        return routerTypeId;
    }

    /**
     * @param routerTypeId
     *            the routerTypeId to set
     */
    public void setRouterTypeId(String routerTypeId) {
        this.routerTypeId = routerTypeId;
    }

    /**
     * @return the routerTypeName
     */
    public String getRouterTypeName() {
        return routerTypeName;
    }

    /**
     * @param routerTypeName
     *            the routerTypeName to set
     */
    public void setRouterTypeName(String routerTypeName) {
        this.routerTypeName = routerTypeName;
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
