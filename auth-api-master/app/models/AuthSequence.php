<?php
namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

class AuthSequence
{

    public static function getAuthSequence(){

        $query =
            "select"
            ."    nextval('auth_sequence') as auth_seq"
        ;
        $params = array();

        LogUtil::sqlLog("[受付シーケンス番号:検索]"
            ." query=".$query);

        try {
            $queryResult = pg_query_params($query, $params);
            if (0 < count(pg_fetch_all_columns($queryResult)))
            {
                $result = pg_fetch_assoc($queryResult, 0);
            }
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }

        return $result['auth_seq'];


    }

}