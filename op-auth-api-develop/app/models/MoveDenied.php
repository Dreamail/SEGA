<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\CommonUtil;
use app\utils\LogUtil;
use Exception;

/**
 * 自動移設情報
 * Class MoveDenied
 * @package app\models
 */
class MoveDenied
{
    public static function checkMoveDenied($gameId, $gameVer, $compCode, $routerBillCode, $machineBillCode)
    {
        $returnVal = false;
        // 戻り値
        $result = array();
        $query =
            'SELECT COUNT(*) FROM( '
            . 'SELECT COUNT(*) AS cnt FROM move_denied_games g WHERE game_id = $1 '
            . 'UNION ALL  '
            . 'SELECT COUNT(*) AS cnt FROM move_denied_gamevers gv WHERE game_id = $1 AND game_ver = $2 '
            . 'UNION ALL  '
            . 'SELECT COUNT(*) AS cnt FROM move_denied_comps c WHERE comp_code = $3 '
            . 'UNION ALL  '
            . 'SELECT COUNT(*) AS cnt FROM move_denied_game_comps gc WHERE comp_code = $3 AND game_id = $1  '
            . 'UNION ALL  '
            . 'SELECT COUNT(*) AS cnt FROM move_denied_bills b WHERE bill_code = $4 OR bill_code = $5  '
            . 'UNION ALL  '
            . 'SELECT COUNT(*) AS cnt FROM move_denied_game_bills gb WHERE (bill_code = $4 OR bill_code = $5) AND game_id = $1) t '
            . 'WHERE cnt > 0';

        $params = array($gameId, $gameVer, $compCode, $routerBillCode, $machineBillCode);
        LogUtil::sqlLog("[自動認証情報取得]"
            . " query=" . $query
            . " param=(" . $gameId . ", " . $gameVer . ", ". $compCode . ", ". $routerBillCode. ", ". $machineBillCode . ")");
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