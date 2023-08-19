/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.router;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.annotation.StringByteLength;
import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;

/**
 * @author TsuboiY
 * 
 */
public class RouterRegisterParameter extends AbstractRegisterParameter
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 6;

    @NotNull
    @Pattern(regexp = "\\-?[0-9]{1,10}")
    private String allnetId;

    @NotNull
    @Pattern(regexp = "[0-9A-Z]{1,16}")
    private String routerId;

    @NotNull
    @Pattern(regexp = "[0-9A-Z\\.:]{7,39}")
    private String placeIp;

    @NotNull
    @Pattern(regexp = "\\-?[0-9]{1,5}")
    private String routerTypeId;

    @StringByteLength(max = 60)
    private String routerTypeName;

    @Pattern(regexp = "|\\-?[0-9]{1,5}")
    private String lcTypeId;

    @StringByteLength(max = 60)
    private String lcTypeName;

    /**
     * コンストラクタ
     * 
     * @param csv
     */
    public RouterRegisterParameter(String[] csv) {
        super(csv, CSV_COLUMN_SIZE);
    }

    @Override
    protected void assign(String[] inputCsv) {
        allnetId = inputCsv[1];
        routerId = inputCsv[2];
        placeIp = inputCsv[3];
        routerTypeId = inputCsv[4];
        lcTypeId = inputCsv[5];
    }

    /**
     * @return the allnetId
     */
    public String getAllnetId() {
        return allnetId;
    }

    /**
     * @param allnetId
     *            the allnetId to set
     */
    public void setAllnetId(String allnetId) {
        this.allnetId = allnetId;
    }

    /**
     * @return the routerId
     */
    public String getRouterId() {
        return routerId;
    }

    /**
     * @param routerId
     *            the routerId to set
     */
    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return the placeIp
     */
    public String getPlaceIp() {
        return placeIp;
    }

    /**
     * @param placeIp
     *            the placeIp to set
     */
    public void setPlaceIp(String placeIp) {
        this.placeIp = placeIp;
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

}
