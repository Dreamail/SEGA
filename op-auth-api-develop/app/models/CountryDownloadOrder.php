<?php
namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

/**
 * 国別配信指示書
 * Class CountryDownloadOrder
 * @package app\models
 */
class CountryDownloadOrder
{
    /**
     * 国別配信指示書URIを取得します
     * @param $gameId ゲームタイトルID
     * @param $ver ゲームタイトルVer.
     * @param $countryCode 国コード.
     * @param $type イメージタイプ.
     * @throws Exception
     */
    public static function findUriByCountry($gameId, $ver, $countryCode, $type)
    {
        // 戻り値
        $result = array();

        // ゲームタイトルID、ゲームタイトルVer.が空の場合は取得しない
        if (empty($gameId) || empty($ver))
        {
            return $result;
        }

        $query =
            'select uri'
            .'  from country_download_orders'
            .'  where game_id = $1 and game_ver = $2 and country_code = $3 and type = $4';
        $params = array($gameId, $ver, $countryCode, $type);
        LogUtil::sqlLog("[国別配信指示書:検索]"
            ." query=".$query
            ." param=(".$gameId.", ".$ver.", ".$countryCode.", ".$type. ")");
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

        return $result;
    }
}