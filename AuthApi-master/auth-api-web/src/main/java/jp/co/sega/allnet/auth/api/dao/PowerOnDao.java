/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.api.dao;

import java.math.BigDecimal;

import jp.co.sega.allnet.auth.api.domain.AuthAllowedComp;
import jp.co.sega.allnet.auth.api.domain.AuthAllowedPlace;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnData.Router;
import jp.co.sega.allnet.auth.api.service.poweron.PowerOnParameter;

/**
 * 基板認証に関するデータアクセスクラス。
 * 
 * @author TsuboiY
 * 
 */
public interface PowerOnDao {

    String USER_ID = "PowenOn";

    /**
     * IPからルータに紐づく店舗および地域情報、ゲームID・ゲームバージョン・国コードからゲーム属性情報、
     * 基板シリアルから基板情報を取得してまとめて返却する。
     * 
     * @param gameId
     * @param gameVer
     * @param serial
     * @param ip
     * @return
     */
    PowerOnData find(String gameId, String gameVer, String serial, String ip);

    /**
     * ゲームID・包括先コード・請求先コードから自動認証不許可かどうかを判別する。
     * 
     * @param gameId
     * @param compCode
     * @param billCode
     * @return
     */
    boolean checkAuthDenied(String gameId, String compCode, String billCode);

    /**
     * ゲームID・ゲームVER・包括先コード・ルータに紐づく請求先コード・基板に紐づく請求先コードから自動移設不許可かどうか判別する。
     * 
     * @param gameId
     * @param game_ver
     * @param compCode
     * @param routerBillCode
     * @param machineBillCode
     * @return
     */
    boolean checkMoveDenied(String gameId, String gameVer, String compCode,
            String routerBillCode, String machineBillCode);

    /**
     * ゲームID・包括先コードから包括先認証許可情報を取得する。
     * 
     * @param gameId
     * @param compCode
     * @return
     */
    AuthAllowedComp findAuthAllowedComp(String gameId, String compCode);

    /**
     * ゲームID・ALL.NetIDから認証許可店舗情報を取得する。
     * 
     * @param gameId
     * @param allnetId
     * @return
     */
    AuthAllowedPlace findAuthAllowedPlace(String gameId, BigDecimal allnetId);

    /**
     * 基板情報のゲームID、予約ゲームIDを交換する。
     * 
     * @param data
     */
    void exchangeMachineGameId(PowerOnData data);

    /**
     * 配信PCの稼働状況ログを削除する。
     * 
     * @param data
     */
    void deleteLoaderStateLogs(PowerOnData data);

    /**
     * 基板ステータス情報を更新する。
     * 
     * @param flag
     * @param data
     */
    void updateMachineStatus(PowerOnData data, PowerOnParameter param);

    /**
     * 自動認証による基板情報の登録する。
     * 
     * @param data
     */
    void insertMachine(PowerOnData data);

    /**
     * 自動移設による基板情報の更新する。
     * 
     * @param data
     */
    void updateMachine(PowerOnData data);

    /**
     * 未登録基板情報の登録・更新する。
     * 
     * @param param
     */
    void updateUnRegisteredMachine(PowerOnParameter param);

    /**
     * 未登録基板情報の削除する。
     * 
     * @param param
     */
    void deleteUnRegisteredMachine(PowerOnParameter param);

    /**
     * 認証ログの書き込みを行う。
     * 
     * @param param
     * @param stat
     * @param cause
     * @param res
     * @param debugInfo
     * @param debugInfoPlace
     */
    void insertLog(PowerOnParameter param, int stat, int cause, Router router,
            String res, String debugInfo, String debugInfoPlace);

    /**
     * 配信レポートを削除する。
     * 
     * @param data
     */
    void deleteDeliverReport(PowerOnData data);

    /**
     * キーチップ生産情報をチェックするゲームIDかどうか判別する。
     * 
     * @param gameId
     * @return
     */
    boolean checkPrdCheckGame(String gameId);

    /**
     * キーチップ生産情報のステータスを取得する。
     * 
     * @param serial
     * @return
     */
    Integer findKeychipStat(String serial, String gameId);

}
