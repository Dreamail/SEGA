/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.gameauth;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * @author NakanoY
 * 
 */
public interface GameAuthRegisterService {

    String AUTH_TYPE_DELETE = "0";

    /**
     * AllnetAdminからゲーム認証方式の更新処理を行う。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<GameAuthRegisterResult> registerGameAuthForAdmin(
            String val);

}
