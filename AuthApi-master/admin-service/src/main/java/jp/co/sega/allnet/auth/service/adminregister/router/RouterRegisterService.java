/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.router;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * ルータ情報の登録処理を行う。
 * 
 * @author NakanoY
 * 
 */
public interface RouterRegisterService {

    int DELETE_ALLNET_ID = 0;

    /**
     * ルータ情報の登録処理を行う。
     * 
     * @param val
     * @return
     */
    String registerRouter(String val);

    /**
     * AllnetAdminからルータ情報登録処理を行う。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<RouterRegisterResult> registerRouterForAdmin(
            String val);

}
