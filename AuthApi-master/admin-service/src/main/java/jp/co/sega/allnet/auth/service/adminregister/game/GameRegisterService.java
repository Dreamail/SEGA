/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.game;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author NakanoY
 * 
 */
public interface GameRegisterService {

    String AUTH_TYPE_DELETE = "0";

    String registerGame(String val);

    /**
     * AllnetAdminからゲーム情報登録処理を行う。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<GameRegisterResult> registerGameForAdmin(String val);

}
