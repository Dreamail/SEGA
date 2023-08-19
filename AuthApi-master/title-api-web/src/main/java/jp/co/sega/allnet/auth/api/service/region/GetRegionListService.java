/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.region;

import jp.co.sega.allnet.auth.api.service.AuthenticationException;

/**
 * @author TsuboiY
 * 
 */
public interface GetRegionListService {

    /**
     * 地域情報取得処理を行う。
     * 
     * @param gameId
     * @param password
     * @param countryCode
     * @return
     */
    String getRegionList(String gameId, String password, String countryCode)
            throws AuthenticationException;

}
