/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.gamecomp;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author NakanoY
 * 
 */
public interface AuthDeniedGameCompRegisterService {

    /**
     * 自動認証不許可ゲーム・包括先情報の登録を行う。
     * 
     * @param val
     * @return
     */
    String registerAuthDeniedGameComp(String val);

    /**
     * AllnetAdminから自動認証不許可ゲーム・包括先情報の登録を行う。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<AuthDeniedGameCompRegisterResult> registerAuthDeniedGameCompForAdmin(
            String val);

}
