/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.comp;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import jp.co.sega.allnet.auth.service.adminregister.AdminRegisterConstants;

/**
 * @author NakanoY
 * 
 */
public class AdminCompRegisterParameter extends CompRegisterParameter implements
        Serializable {

    private static final long serialVersionUID = 1L;

    @Pattern(regexp = "REG|DEL")
    private String registerFlag;

    /**
     * コンストラクタ
     * 
     * @param csv
     */
    public AdminCompRegisterParameter(String[] csv) {
        super(csv);
    }

    @Override
    protected void assign(String[] inputCsv) {
        super.assign(inputCsv);
        // 削除フラグはnullとする
        setDeleteFlag(null);
        // 登録フラグにセットする
        setRegisterFlag(inputCsv[2]);
    }

    @Override
    public String getDeleteFlag() {
        if (registerFlag.equals("DEL")) {
            return AdminRegisterConstants.DELETE_FLAG_DEL;
        }

        return super.getDeleteFlag();
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

}
