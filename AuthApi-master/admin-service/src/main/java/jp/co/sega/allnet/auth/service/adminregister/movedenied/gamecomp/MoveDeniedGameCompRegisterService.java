/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.gamecomp;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author NakanoY
 * 
 */
public interface MoveDeniedGameCompRegisterService {

    /**
     * 同一包括先内自動移設不許可ゲーム・包括先情報の登録を行う。
     * 
     * @param val
     * @return
     */
    String registerMoveDeniedGameComp(String val);

    /**
     * AllnetAdminから同一包括先内自動移設不許可ゲーム・包括先情報の登録を行う。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<MoveDeniedGameCompRegisterResult> registerMoveDeniedGameCompForAdmin(
            String val);

}
