/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.game;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author TsuboiY
 * 
 */
public interface MoveDeniedGameRegisterService {

    /**
     * 同一包括先内自動移設不許可ゲーム情報の登録を行います。
     * 
     * @param val
     * @return
     */
    String registerMoveDeniedGame(String val);

    /**
     * AllnetAdminから同一包括先内自動移設不許可ゲーム情報の登録を行います。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<MoveDeniedGameRegisterResult> registerMoveDeniedGameForAdmin(
            String val);

}
