/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.comp;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author NakanoY
 * 
 */
public interface MoveDeniedCompRegisterService {

    /**
     * 同一包括先内自動移設不許可包括先情報の登録を行います。
     * 
     * @param val
     * @return
     */
    String registerMoveDeniedComp(String val);

    /**
     * AllnetAdminから同一包括先内自動移設不許可包括先情報の登録を行います。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<MoveDeniedCompRegisterResult> registerMoveDeniedCompForAdmin(
            String val);

}
