/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.prdcheck.game;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author TsuboiY
 * 
 */
public interface PrdCheckGameRegisterService {

    /**
     * キーチップ生産情報チェック対象ゲーム情報の登録を行います。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<PrdCheckGameRegisterResult> registerPrdCheckGameForAdmin(
            String val);

}
