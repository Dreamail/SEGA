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
     * @param $machineId 基板クライアントID
     * @param $titleId ゲームタイトルID
     */
    public static function mergeByMachineId($machineId, $titleId)
    {
        $query =
            'insert into'
            . '  unregistered_machines (serial, game_id, create_user_id, update_user_id)'
            . '    values ($1, $2, $3, $4)'
            . '  on conflict (serial) do update'
            . '    set game_id = $2, last_access = current_timestamp, update_date = current_timestamp, update_user_id = $4';
        $params = array($machineId, $titleId, DB_CREATE_USER_ID, DB_UPDATE_USER_ID);
        LogUtil::sqlLog("[未登録基板情報:登録・更新]"
            . " query=" . $query
            . " param=(" . $machineId . ", " . $titleId . ", " . DB_CREATE_USER_ID . ", " . DB_UPDATE_USER_ID . ")");
        $queryResult = pg_query_params($query, $params);

        // 結果セットを開放する
        pg_free_result($queryResult);
    }

    /**
     * 未登録基板情報を削除します
     * @param $machineId 基板クライアントID
     */
    public static function deleteByMachineId($machineId)
    {
        $query =
            'delete from'
            . '  unregistered_machines'
            . '  where serial = $1';
        $params = array($machineId);
        LogUtil::sqlLog("[未登録基板情報:削除]"
            . " query=" . $query
            . " param=(" . $machineId . ")");
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
