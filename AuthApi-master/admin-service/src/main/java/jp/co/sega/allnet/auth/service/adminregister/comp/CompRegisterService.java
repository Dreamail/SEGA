/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.comp;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author NakanoY
 * 
 */
public interface CompRegisterService {

    /**
     * 包括先情報の登録を行います。
     * 
     * @param val
     * @return
     */
    String registerComp(String val);

    /**
     * AllnetAdminから包括先情報の登録を行います。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<CompRegisterResult> registerCompForAdmin(String val);

}
