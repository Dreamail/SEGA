/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import java.util.List;

import jp.co.sega.allnet.auth.api.service.dlorder.DownloadOrder;

/**
 * 配信指示書URI取得に関するデータアクセスクラス。
 * 
 * @author TsuboiY
 * 
 */
public interface DownloadOrderDao {

    /**
     * 基板配信指示書情報を取得
     * 
     * @param serial
     * @return
     */
    DownloadOrder findMachineDownloadOrder(String serial, int type);

    /**
     * グループ内のほかの基板シリアルを取得
     * 
     * @param serial
     * @param allnetId
     * @param groupIndex
     * @param gameId
     * @return
     */
    List<String> findGroupSerials(String serial, int allnetId, int groupIndex,
            String gameId);

    /**
     * 国別の配信指示書URIを取得
     * 
     * @param gameId
     * @param gameVer
     * @param countryCode
     * @return
     */
    String findUriByCountry(String gameId, String gameVer, String countryCode, int type);

    /**
     * ゲームID、ゲームバージョン別の配信指示書URIを取得
     * 
     * @param gameId
     * @param gameVer
     * @return
     */
    String findUriByGame(String gameId, String gameVer, int type);

}
