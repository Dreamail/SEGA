/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.movedenied.gamever;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author TsuboiY
 * 
 */
public interface MoveDeniedGameVerRegisterService {

    /**
     * 同一包括先内自動移設不許可ゲームバージョン情報の登録を行います。
     * 
     * @param val
     * @return
     */
    String registerMoveDeniedGameVer(String val);

    /**
     * AllnetAdminから同一包括先内自動移設不許可ゲームバージョン情報の登録を行います。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<MoveDeniedGameVerRegisterResult> registerMoveDeniedGameVerForAdmin(
            String val);

}
