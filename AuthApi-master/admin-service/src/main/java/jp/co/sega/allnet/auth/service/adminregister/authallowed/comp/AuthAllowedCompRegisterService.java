/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authallowed.comp;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * 包括先認証情報の登録処理を行う。
 * 
 * @author NakanoY
 * 
 */
public interface AuthAllowedCompRegisterService {

    /**
     * 包括先認証情報の登録処理を行う。
     * 
     * @param val
     * @return
     */
    String registerAuthAllowedComp(String val);

    /**
     * AllnetAdminから包括先認証情報の登録処理を行う。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<AuthAllowedCompRegisterResult> registerAuthAllowedCompForAdmin(
            String val);

}
