<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

/**
 * キーチップ情報
 * Class Vkeychips
 * @package app\models
 */
class Vkeychips
{
    public static function findKeychipStat($serial, $gameId)
    {
        // 戻り値
        $result = array();

        // ゲームIDが空の場合は取得しない
        if (empty($gameId)) {
            return $result;
        }

        $query =
            'SELECT keychip_stat FROM v_keychips WHERE keychip_sn = $1 AND game_id = $2';
        $params = array($serial, $gameId);
        LogUtil::sqlLog("[キーチップ情報取得]"
            . " query=" . $query
            . " param=(" . $serial . ", " . $gameId . ")");
        try {
            $queryResult = pg_query_params($query, $params);
            if (0 < count(pg_fetch_all_columns($queryResult))) {
                $result = pg_fetch_assoc($queryResult, 0);
            }
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }

        return $result;
    }
}