<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

/**
 * 基板ステータス
 * Class MachineStatus
 * @package app\models
 */
class MachineStatus
{
    /**
     * 基板ステータスを登録します
     * @param $statResultSuccess ステータスOKフラグ
     * @param $serial 基板クライアント
     * @param $gameId ゲームタイトルID
     * @param $gameVer ゲームタイトルVer.
     * @param $userAgent UserAgent
     * @param $firmVer ファームウェアバージョン
     * @param $bootVer ブートROMバージョン
     * @param $formatVer フォーマットバージョン
     * @param $placeIp IP
     * @param $hops Hops
     */
    public static function save($statResultSuccess, $serial, $gameId, $gameVer, $userAgent, $firmVer, $bootVer, $formatVer, $placeIp, $hops)
    {
        $query = null;
        // 認証成功の場合、最終認証成功時刻（last_auth）をセット
        if ($statResultSuccess) {
            $query =
                'insert into'
                . '  machine_statuses'
                . '    (serial, game_id, game_ver, place_ip, firm_ver, boot_ver, format_ver, user_agent, last_access, last_auth, hops, create_user_id, update_user_id)'
                . '  values'
                . '    ($1, $2, $3, $4, $5, $6, $7, $8, current_timestamp, current_timestamp, $9, $10, $11)';
        } else {
            $query =
                'insert into'
                . '  machine_statuses'
                . '    (serial, game_id, game_ver, place_ip, firm_ver, boot_ver, format_ver, user_agent, last_access, hops, create_user_id, update_user_id)'
                . '  values'
                . '    ($1, $2, $3, $4, $5, $6, $7, $8, current_timestamp, $9, $10, $11)';
        }
        $params = array($serial, $gameId, $gameVer, $placeIp, $firmVer, $bootVer, $formatVer, $userAgent, $hops, DB_CREATE_USER_ID, DB_UPDATE_USER_ID);
        LogUtil::sqlLog("[基板ステータス:登録]"
            . " query=" . $query
            . " param=(" . $serial . ", " . $gameId . ", " . $gameVer . ", " . $placeIp . ", " . $firmVer . ", " . $bootVer . ", " . $formatVer . ", " . $userAgent . ", " . $hops . ", " . DB_CREATE_USER_ID . ", " . DB_UPDATE_USER_ID . ")");
        try {
            $queryResult = pg_query_params($query, $params);
            if (!$queryResult) {
                throw new Exception("基板ステータスの登録に失敗しました。"
                    . " query=" . $query
                    . " param=(" . $serial . ", " . $gameId . ", " . $gameVer . ", " . $placeIp . ", " . $firmVer . ", " . $bootVer . ", " . $formatVer . ", " . $userAgent . ", " . $hops . ", " . DB_CREATE_USER_ID . ", " . DB_UPDATE_USER_ID . ")");
            }
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }
    }

    /**
     * 基板ステータスを更新します
     * @param $statResultSuccess ステータスOKフラグ
     * @param $serial 基板クライアントID
     * @param $gameId ゲームタイトルID
     * @param $gameVer ゲームタイトルVer.
     * @param $userAgent UserAgent
     */
    public static function updateByMachineId($statResultSuccess, $serial, $gameId, $gameVer, $userAgent, $firmVer, $bootVer, $formatVer, $placeIp, $paramHops)
    {
        $query = null;
        // 認証成功の場合、最終認証成功時刻（last_auth）をセット
        if ($statResultSuccess) {
            $query =
                'update machine_statuses'
                . '  set'
                . '    game_id = $1, game_ver = $2, place_ip = $3, firm_ver = $4, boot_ver = $5, format_ver = $6, user_agent = $7, '
                . '    last_access = current_timestamp, last_auth = current_timestamp, hops = $8, update_date = current_timestamp, update_user_id = $9 '
                . '  where serial = $10';
        } else {
            $query =
                'update machine_statuses'
                . '  set'
                . '    game_id = $1, game_ver = $2, place_ip = $3, firm_ver = $4, boot_ver = $5, format_ver = $6, user_agent = $7, '
                . '    last_access = current_timestamp, hops = $8, update_date = current_timestamp, update_user_id = $9 '
                . '  where serial = $10';
        }
        $params = array($gameId, $gameVer, $placeIp, $firmVer, $bootVer, $formatVer, $userAgent, $paramHops, DB_UPDATE_USER_ID, $serial);
        LogUtil::sqlLog("[基板ステータス:更新]"
            . " query=" . $query
            . " param=(" . $gameId . ", " . $gameVer . ", " . $placeIp . ", " . $firmVer . ", " . $bootVer . ", " . $formatVer . ", " . $userAgent . ", " . $paramHops .  ", " . DB_UPDATE_USER_ID .  ", " . $serial . ")");
        try {
            $queryResult = pg_query_params($query, $params);
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }
    }
}
