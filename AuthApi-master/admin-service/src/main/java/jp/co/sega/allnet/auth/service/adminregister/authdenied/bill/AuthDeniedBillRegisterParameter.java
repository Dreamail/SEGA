/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.bill;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.service.adminregister.AbstractRegisterParameter;
import jp.co.sega.allnet.auth.service.adminregister.AdminRegisterConstants;

/**
 * @author TsuboiY
 * 
 */
public class AuthDeniedBillRegisterParameter extends AbstractRegisterParameter
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int CSV_COLUMN_SIZE = 3;

    @NotNull
    @Pattern(regexp = AdminRegisterConstants.REGISTER_FLAG_DEL + "|"
            + AdminRegisterConstants.REGISTER_FLAG_REG)
    private String registerFlag;

    @NotNull
    @Pattern(regexp = "[0-9A-Z]{9}")
    private String billCode;

    /**
     * コンストラクタ
     * 
     * @param csv
     */
    public AuthDeniedBillRegisterParameter(String[] csv) {
        super(csv, CSV_COLUMN_SIZE);
    }

    @Override
    protected void assign(String[] inputCsv) {
        registerFlag = inputCsv[0];
        billCode = inputCsv[2];
        if (billCode != null) {
            billCode = billCode.toUpperCase();
        }
    }

    /**
     * @return the registerFlag
     */
    public String getRegisterFlag() {
        return registerFlag;
    }

    /**
     * @param registerFlag
     *            the registerFlag to set
     */
    public void setRegisterFlag(String registerFlag) {
        this.registerFlag = registerFlag;
    }

    /**
     * @return the billCode
     */
    public String getBillCode() {
        return billCode;
    }

    /**
     * @param billCode
     *            the billCode to set
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

}
