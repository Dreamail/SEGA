<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

/**
 * ゲーム情報
 * Class Game
 * @package app\models
 */
class Game
{
    public static function findByPk($titleId)
    {
        // 戻り値
        $result = array();

        // ゲームIDが空の場合は取得しない
        if (empty($titleId)) {
            return $result;
        }

        $query =
            'select'
            . '    title_id, cipher_key'
            . '  from games'
            . '  where title_id = $1';
        $params = array($titleId);
        LogUtil::sqlLog("[ゲーム情報:検索]"
            . " query=" . $query
            . " param=(" . $titleId . ")");
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