/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.place;

import jp.co.sega.allnet.auth.api.service.AuthenticationException;

/**
 * @author TsuboiY
 * 
 */
public interface GetPlaceListService {

    /**
     * 店舗情報取得処理を行う。
     * 
     * @param gameId
     * @param password
     * @param countryCode
     * @param allMachine
     * @return
     */
    String getPlaceList(String gameId, String password, String countryCode,
            boolean allMachine, int activeDays, String placeType)
            throws AuthenticationException;

}
