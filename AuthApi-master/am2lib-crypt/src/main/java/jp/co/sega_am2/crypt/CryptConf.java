/*
 * $Id: CryptConf.java 961 2006-03-23 08:23:31Z koi $
 * 
 * Copyright (C) 2005 SEGA Corporation. All rights reserved.
 */

package jp.co.sega_am2.crypt;

import java.io.Serializable;

import jp.co.sega.allnet.auth.am2lib.util.Log;

/**
 * 暗号設定
 */
@SuppressWarnings("serial")
public class CryptConf implements Serializable {

    private boolean ignoreParameterEncription;
    private boolean noUrlEncode;
    private boolean useAssert;

    /**
     * 初期化
     */
    public void init() {
        Log.write("CryptConf#init()");
        Crypt.setCryptConf(this);
    }

    /**
     * 開発時のデバッグ用、非暗号モードをセット(Crypt用)
     */
    public void setIgnoreParameterEncription(boolean flag) {
        ignoreParameterEncription = flag;
    }

    /**
     * 開発時のデバッグ用、非暗号モードを得る
     */
    public boolean isIgnoreParameterEncription() {
        return ignoreParameterEncription;
    }

    /**
     * @return noUrlEncode を戻します。
     */
    public boolean isNoUrlEncode() {
        return noUrlEncode;
    }

    /**
     * @param noUrlEncode
     *            設定する noUrlEncode。
     */
    public void setNoUrlEncode(boolean noUrlEncode) {
        this.noUrlEncode = noUrlEncode;
    }

    /**
     * @return useAssert を戻します。
     */
    public boolean isUseAssert() {
        return useAssert;
    }

    /**
     * @param useAssert
     *            設定する useAssert。
     */
    public void setUseAssert(boolean useAssert) {
        this.useAssert = useAssert;
    }

}