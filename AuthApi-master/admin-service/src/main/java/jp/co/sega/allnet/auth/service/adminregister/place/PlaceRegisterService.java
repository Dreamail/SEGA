/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister.place;

import jp.co.sega.allnet.auth.service.adminregister.RegisterServiceResult;

/**
 * 店舗情報の登録処理を行う。
 * 
 * @author NakanoY
 * 
 */
public interface PlaceRegisterService {

    int REGION_LEVEL_0 = 0;

    int REGION_LEVEL_1 = 1;

    int REGION_LEVEL_2 = 2;

    int REGION_LEVEL_3 = 3;

    String DELIMITER_NICKNAME = "#";

    int MAX_BYTES_NICKNAME_SEP_EUC_JP = 14;

    /**
     * 店舗情報の登録処理を行う。
     * 
     * @param val
     * @return
     */
    String registerPlace(String val);

    /**
     * AllnetAdminから店舗情報登録処理を行う。
     * 
     * @param val
     * @param allowAddA0
     * @return
     */
    RegisterServiceResult<PlaceRegisterResult> registerPlaceForAdmin(
            String val, boolean allowAddA0);

}
