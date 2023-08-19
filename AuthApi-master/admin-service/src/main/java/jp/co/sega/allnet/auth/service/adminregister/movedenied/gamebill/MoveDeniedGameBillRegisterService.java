/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.gamebill;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author NakanoY
 * 
 */
public interface MoveDeniedGameBillRegisterService {

    /**
     * 同一包括先内自動移設不許可ゲーム・請求先情報の登録を行う。
     * 
     * @param val
     * @return
     */
    String registerMoveDeniedGameBill(String val);

    /**
     * AllnetAdminから同一包括先内自動移設不許可ゲーム・請求先情報の登録を行う。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<MoveDeniedGameBillRegisterResult> registerMoveDeniedGameBillForAdmin(
            String val);

}
