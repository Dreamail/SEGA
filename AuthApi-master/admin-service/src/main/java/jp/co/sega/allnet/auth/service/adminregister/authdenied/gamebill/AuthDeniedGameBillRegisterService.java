/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.gamebill;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author NakanoY
 * 
 */
public interface AuthDeniedGameBillRegisterService {

    /**
     * 自動認証不許可ゲーム・請求先情報の登録を行う。
     * 
     * @param val
     * @return
     */
    String registerAuthDeniedGameBill(String val);

    /**
     * AllnetAdminから自動認証不許可ゲーム・請求先情報の登録を行う。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<AuthDeniedGameBillRegisterResult> registerAuthDeniedGameBillForAdmin(
            String val);

}
