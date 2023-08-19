/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.lctype;

/**
 * @author NakanoY
 * 
 */
public interface LcTypeRegisterService {

    /**
     * 回線種別情報の登録を行います。
     * 
     * @param val
     * @return
     */
    String registerLcType(String val);

}
