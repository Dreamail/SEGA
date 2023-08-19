/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authdenied.bill;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author TsuboiY
 * 
 */
public interface AuthDeniedBillRegisterService {

    /**
     * 自動認証不許可請求先情報の登録を行います。
     * 
     * @param val
     * @return
     */
    String registerAuthDeniedBill(String val);

    /**
     * AllnetAdminから自動認証不許可請求先情報の登録を行います。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<AuthDeniedBillRegisterResult> registerAuthDeniedBillForAdmin(
            String val);

}
