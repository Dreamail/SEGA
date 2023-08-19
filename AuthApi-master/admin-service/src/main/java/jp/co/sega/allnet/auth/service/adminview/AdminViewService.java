/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminview;

/**
 * 認証ログ取得処理を行う。
 * 
 * @author NakanoY
 * 
 */
public interface AdminViewService {

    String GAMEID_MATCH_PATTERN = "^[0-9A-Z]{1,5}$";

    String DATE_MATCH_PATTERN = "^|([0-9]{4}[0-1][0-9][0-3][0-9])$";

    String TIME_MATCH_PATTERN = "^|([0-2][0-9][0-5][0-9][0-5][0-9])$";

    /**
     * 認証ログ取得処理を行う。
     * 
     * @param gameId
     * @param date
     * @param time
     * @return
     */
    String adminView(String gameId, String date, String time);

}
