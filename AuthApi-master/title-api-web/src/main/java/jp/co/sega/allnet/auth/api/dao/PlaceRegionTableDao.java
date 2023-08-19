/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import java.util.List;

import jp.co.sega.allnet.auth.api.domain.Place;
import jp.co.sega.allnet.auth.api.domain.Region;

/**
 * タイトルサーバからの店舗情報取得に関するデータアクセスクラス。
 * 
 * @author NakanoY
 * 
 */
public interface PlaceRegionTableDao {

    int SETTING_COMM_OK = 1;

    int[] REGION_LEVEL_RANGE = new int[] { 0, 1, 2, 3 };

    /**
     * 該当のゲームIDの通信が許可された基板が存在する店舗情報を取得する。
     * 
     * @param gameId
     * @return
     */
    List<Place> findPlaces(String gameId, int activeDays, String placeType);

    /**
     * 該当のゲームIDの通信が許可された基板が存在する該当の国の店舗情報を取得する。
     * 
     * @param gameId
     * @param countryCode
     * @return
     */
    List<Place> findPlaces(String gameId, String countryCode, int activeDays,
            String placeType);

    /**
     * 該当のゲームIDの基板が存在する店舗情報を取得する。
     * 
     * @param gameId
     * @return
     */
    List<Place> findPlacesIncludeAuthDenied(String gameId, int activeDays,
            String placeType);

    /**
     * 該当のゲームIDの基板が存在する該当の国の店舗情報を取得する。
     * 
     * @param gameId
     * @param countryCode
     * @return
     */
    List<Place> findPlacesIncludeAuthDenied(String gameId, String countryCode,
            int activeDays, String placeType);

    /**
     * すべての店舗情報を取得する。
     * 
     * @return
     */
    List<Place> findPlacesAll();

    /**
     * 該当の国の地域情報を取得する。
     * 
     * @param countryCode
     * @param level
     * @return
     */
    List<Region> findRegions(String countryCode, int level);

    /**
     * 該当の店舗が存在するすべての国の地域情報を取得する。
     * 
     * @param places
     * @return
     */
    List<Region> findRegions(List<Place> places);

    /**
     * 請求先の公開が許可されているゲームIDかどうかをチェックする。
     * 
     * @param gameId
     * @return
     */
    boolean checkBillOpenAllowedGame(String gameId);

}
