<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\CommonUtil;
use app\utils\LogUtil;
use Exception;

/**
 * 認証許可包括先
 * Class AuthAllowedComps
 * @package app\models
 */
class AuthAllowedComps
{
    public static function findAuthAllowedComp($gameId, $compCode)
    {
        // 戻り値
        $result = array();
        $query =
            'SELECT game_id, comp_code FROM auth_allowed_comps  '
            . 'WHERE game_id = $1 AND comp_code = $2 ';

        $params = array($gameId,$compCode);
        LogUtil::sqlLog("[認証許可包括先情報取得]"
            . " query=" . $query
            . " param=(" . $gameId . ", " . $compCode . ")");
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