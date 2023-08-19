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
     * @param $machineId 基板クライアント
     * @param $titleId ゲームタイトルID
     * @param $titleVer ゲームタイトルVer.
     * @param $userAgent UserAgent
     * @param $firmVer ファームウェアバージョン
     * @param $bootVer ブートROMバージョン
     * @param $formatVer フォーマットバージョン
     */
    public static function save($statResultSuccess, $machineId, $titleId, $titleVer, $userAgent, $firmVer, $bootVer, $formatVer, $placeIp)
    {
        $query = null;
        // 認証成功の場合、最終認証成功時刻（last_auth）をセット
        if ($statResultSuccess) {
            $query =
                'insert into'
                . '  machine_statuses'
                . '    (serial, game_id, game_ver, user_agent,'
                . '    last_auth, create_user_id, update_user_id, firm_ver, boot_ver, format_ver, place_ip)'
                . '  values'
                . '    ($1, $2, $3, $4, current_timestamp, $5, $6, $7, $8, $9, $10)';
        } else {
            $query =
                'insert into'
                . '  machine_statuses'
                . '    (serial, game_id, game_ver, user_agent,'
                . '    create_user_id, update_user_id, firm_ver, boot_ver, format_ver, place_ip)'
                . '  values'
                . '    ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)';
        }
        $params = array($machineId, $titleId, $titleVer, $userAgent, DB_CREATE_USER_ID, DB_UPDATE_USER_ID, $firmVer, $bootVer, $formatVer, $placeIp);
        LogUtil::sqlLog("[基板ステータス:登録]"
            . " query=" . $query
            . " param=(" . $machineId . ", " . $titleId . ", " . $titleVer . ", " . $userAgent . ", " . DB_CREATE_USER_ID . ", " . DB_UPDATE_USER_ID . ", " . $firmVer . ", " . $bootVer . ", " . $formatVer . ", " . $placeIp . ")");
        try {
            $queryResult = pg_query_params($query, $params);
            if (!$queryResult) {
                throw new Exception("基板ステータスの登録に失敗しました。"
                    . " query=" . $query
                    . " param=(" . $machineId . ", " . $titleId . ", " . $titleVer . ", " . $userAgent . ", " . DB_CREATE_USER_ID . ", " . DB_UPDATE_USER_ID . ", " . $firmVer . ", " . $bootVer . ", " . $formatVer . ", " . $placeIp . ")");
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
     * @param $machineId 基板クライアントID
     * @param $titleId ゲームタイトルID
     * @param $titleVer ゲームタイトルVer.
     * @param $userAgent UserAgent
     */
    public static function updateByMachineId($statResultSuccess, $machineId, $titleId, $titleVer, $userAgent, $firmVer, $bootVer, $formatVer, $placeIp)
    {
        $query = null;
        // 認証成功の場合、最終認証成功時刻（last_auth）をセット
        if ($statResultSuccess) {
            $query =
                'update machine_statuses'
                . '  set'
                . '    game_id = $1, game_ver = $2, user_agent = $3,'
                . '    last_access = current_timestamp, last_auth = current_timestamp,'
                . '    update_date = current_timestamp, update_user_id = $4, firm_ver = $5, boot_ver = $6, format_ver = $7, place_ip = $8, hops = $9'
                . '  where serial = $10';
        } else {
            $query =
                'update machine_statuses'
                . '  set'
                . '    game_id = $1, game_ver = $2, user_agent = $3,'
                . '    last_access = current_timestamp, update_date = current_timestamp, update_user_id = $4, firm_ver = $5, boot_ver = $6, format_ver = $7, place_ip = $8, hops = $9'
                . '  where serial = $10';
        }
        $params = array($titleId, $titleVer, $userAgent, DB_UPDATE_USER_ID, $firmVer, $bootVer, $formatVer, $placeIp, HOPS, $machineId);
        LogUtil::sqlLog("[基板ステータス:更新]"
            . " query=" . $query
            . " param=(" . $titleId . ", " . $titleVer . ", " . $userAgent . ", " . DB_UPDATE_USER_ID . ", " . $firmVer . ", " . $bootVer . ", " . $formatVer . ", " . $placeIp .  ", " . HOPS .  ", " . $machineId . ")");
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
