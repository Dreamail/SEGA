<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

/**
 * ゲーム属性
 * Class GameAttribute
 * @package app\models
 */
class GameAttribute
{
    /**
     * ゲームタイトルIDとゲームタイトルVer.（PK）でゲーム属性情報を取得します。
     * @param $gameId ゲームタイトルID
     * @param $ver ゲームタイトルVer.
     * @param $countryCode 国コード.
     * @return ゲーム属性情報の配列（0件の場合空の配列）
     */
    public static function findByPk($gameId, $ver, $countryCode)
    {
        // 戻り値
        $result = array();

        // ゲームID、ゲームVer.、国コードが空の場合は取得しない
        if (empty($gameId) || empty($ver) || empty($countryCode)) {
            return $result;
        }

        $query =
            'select'
            . '    game_id, game_ver, uri, host, auth'
            . '  from game_attributes'
            . '  where game_id = $1 and game_ver = $2 and country_code = $3';
        $params = array($gameId, $ver, $countryCode);
        LogUtil::sqlLog("[ゲーム属性情報:検索]"
            . " query=" . $query
            . " param=(" . $gameId . ", " . $ver . ", " . $countryCode . ")");
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