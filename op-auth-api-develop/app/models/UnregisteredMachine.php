<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

/**
 * 未登録基板情報
 * Class UnregisteredMachine
 * @package app\models
 */
class UnregisteredMachine
{
    /**
     * 未登録基板情報を登録・更新します
     * @param $serial 基板クライアントID
     * @param $gameId ゲームタイトルID
     * @param $placeIp 店舗IP
     */
    public static function mergeByMachineId($serial, $gameId, $placeIp)
    {
        $query =
            'insert into'
            . '  unregistered_machines (serial, place_ip, game_id, last_access, create_user_id, update_user_id)'
            . '    values ($1, $2, $3, current_timestamp, $4, $5)'
            . '  on conflict (serial) do update'
            . '    set place_ip = $2, game_id = $3, last_access = current_timestamp, update_date = current_timestamp, update_user_id = $5';
        $params = array($serial, $placeIp, $gameId, DB_CREATE_USER_ID, DB_UPDATE_USER_ID);
        LogUtil::sqlLog("[未登録基板情報:登録・更新]"
            . " query=" . $query
            . " param=(" . $serial . ", " . $placeIp . ", " . $gameId . ", " . DB_CREATE_USER_ID . ", " . DB_UPDATE_USER_ID . ")");
        $queryResult = pg_query_params($query, $params);

        // 結果セットを開放する
        pg_free_result($queryResult);
    }

    /**
     * 未登録基板情報を削除します
     * @param $serial 基板シリアル
     */
    public static function deleteByMachineId($serial)
    {
        $query =
            'delete from'
            . '  unregistered_machines'
            . '  where serial = $1';
        $params = array($serial);
        LogUtil::sqlLog("[未登録基板情報:削除]"
            . " query=" . $query
            . " param=(" . $serial . ")");
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
