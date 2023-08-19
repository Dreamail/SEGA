<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\CommonUtil;
use app\utils\LogUtil;
use Exception;

/**
 * 自動認証情報
 * Class AuthDenied
 * @package app\models
 */
class AuthDenied
{
    public static function checkAuthDenied($gameId, $compCode, $billCode)
    {
        $returnVal = false;
        // 戻り値
        $result = array();
        $query =
            'SELECT COUNT(*) FROM( '
            . 'SELECT COUNT(*) AS cnt FROM auth_denied_comps c WHERE comp_code = $1 '
            . 'UNION ALL  '
            . 'SELECT COUNT(*) AS cnt FROM auth_denied_game_comps gc WHERE comp_code = $1 AND game_id = $2 '
            . 'UNION ALL  '
            . 'SELECT COUNT(*) AS cnt FROM auth_denied_bills b WHERE bill_code = $3 '
            . 'UNION ALL  '
            . 'SELECT COUNT(*) AS cnt FROM auth_denied_game_bills gb WHERE bill_code = $3 AND game_id = $2) t  '
            . 'WHERE cnt > 0';

        $params = array($compCode, $gameId, $billCode);
        LogUtil::sqlLog("[自動認証情報取得]"
            . " query=" . $query
            . " param=(" . $compCode . ", " . $gameId . ", ". $billCode . ")");
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