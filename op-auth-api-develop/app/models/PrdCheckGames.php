<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\CommonUtil;
use app\utils\LogUtil;
use Exception;

/**
 * キーチップ生産チェック対象GAME情報
 * Class PrdCheckGames
 * @package app\models
 */
class PrdCheckGames
{
    public static function checkPrdCheckGame($gameId)
    {
        $returnVal = false;
        // 戻り値
        $result = array();
        $query = 'SELECT COUNT(*) FROM prd_check_games WHERE game_id = $1';

        $params = array($gameId);
        LogUtil::sqlLog("[キーチップ生産チェック対象GAME情報取得]"
            . " query=" . $query
            . " param=(" . $gameId . ")");
        try {
            $queryResult = pg_query_params($query, $params);
            if (0 < count(pg_fetch_all_columns($queryResult))) {
                $result = pg_fetch_assoc($queryResult, 0);
                if(array_key_exists('count', $result) && $result['count'] > 0) {
                    $returnVal = true;
                }
            }
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }

        return $returnVal;
    }
}