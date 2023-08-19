/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

/**
 * タイトルサーバからの店舗情報取得に関するデータアクセスクラス。
 * 
 * @author NakanoY
 * 
 */
public interface ApiAccountDao {

    static final String GET_PLACE_LIST_ALL_TRUE = "1";

    /**
     * ゲームアカウントが登録されているかチェックする。
     * 
     * @param gameId
     * @param password
     * @return
     */
    boolean checkApiAccount(String gameId, String password);

    /**
     * ゲームアカウントが登録されているかチェックする。 (すべての店舗を取得するかも同時にチェックする)
     * 
     * @param gameId
     * @param password
     * @return
     */
    ApiAccountStatus checkApiAccountAndPlaceAll(String gameId, String password);

    enum ApiAccountStatus {
        NG, OK, OK_PLACE_ALL;
    }
}
