/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.comp;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author NakanoY
 * 
 */
public interface AuthDeniedCompRegisterService {

    /**
     * 自動認証不許可包括先情報の登録を行います。
     * 
     * @param val
     * @return
     */
    String registerAuthDeniedComp(String val);

    /**
     * AllnetAdminから自動認証不許可包括先情報の登録を行います。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<AuthDeniedCompRegisterResult> registerAuthDeniedCompForAdmin(
            String val);

}
