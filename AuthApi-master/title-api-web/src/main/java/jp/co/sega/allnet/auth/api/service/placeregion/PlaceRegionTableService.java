/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.service.placeregion;

/**
 * @author TsuboiY
 * 
 */
public interface PlaceRegionTableService {

    String COUNTRY_CODE_ALL = "all";

    String COUNTRY_CODE_DEFAULT = "JPN";

    String DUMMY_BILL_CODE_STR = "DUMMY";

    String DUMMY_PLACE_IP = "0.0.0.0";

    /**
     * 店舗地域情報取得処理を行う。
     * 
     * @param gameId
     * @param password
     * @param countryCode
     * @param allFlag
     * @return
     */
    String placeRegionTable(String gameId, String password, String countryCode,
            boolean all);

    /**
     * 店舗地域情報国際化対応版取得処理を行う。
     * 
     * @param gameId
     * @param password
     * @param countryCode
     * @param allFlag
     * @return
     */
    String placeRegionTableAll(String gameId, String password,
            String countryCode, boolean all);

}
