/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.authallowed.place;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * 店舗認証情報の登録処理を行う。
 * 
 * @author TsuboiY
 * 
 */
public interface AuthAllowedPlaceRegisterService {

    String MODE_FLAG_REGISTER = "1";

    /**
     * 店舗認証情報の登録処理を行う。
     * 
     * @param val
     * @return
     */
    String registerAuthAllowedPlace(String val);

    /**
     * AllnetAdminから店舗認証情報の登録処理を行う。
     * 
     * @param val
     * @return
     */
    RegisterServiceResult<AuthAllowedPlaceRegisterResult> registerAuthAllowedPlaceForAdmin(
            String val);

}
