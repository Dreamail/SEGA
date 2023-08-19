<?php
namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

/**
 * 配信指示書
 * Class DownloadOrder
 * @package app\models
 */
class DownloadOrder
{
    /**
     * アプリケーションイメージの配信指示書URIを取得します
     * @param $gameId ゲームタイトルID
     * @param $ver ゲームタイトルVer.
     * @return アプリケーションイメージの配信指示書URI
     * @throws Exception
     */
    public static function findAppDownloadOrderByPk($gameId, $ver)
    {
        $downloadOrder = DownloadOrder::findDownloadOrderByPk($gameId, $ver, DOWNLOAD_ORDER_IMAGE_TYPE_APP);
        if (count($downloadOrder) == 0)
        {
            return null;
        }
        return $downloadOrder['uri'];
    }

    /**
     * オプションイメージの配信指示書URIを取得します
     * @param $gameId ゲームタイトルID
     * @param $ver ゲームタイトルVer.
     * @return オプションイメージの配信指示書URI
     * @throws Exception
     */
    public static function findOptDownloadOrderByPk($gameId, $ver)
    {
        $downloadOrder = DownloadOrder::findDownloadOrderByPk($gameId, $ver, DOWNLOAD_ORDER_IMAGE_TYPE_OPT);
        if (count($downloadOrder) == 0)
        {
            return null;
        }
        return $downloadOrder['uri'];
    }

    /**
     * 配信指示書情報を取得します。
     * @param $gameId ゲームタイトルID
     * @param $ver ゲームタイトルVer.
     * @param $type 配信種別
     * @return 配信指示書情報
     * @throws Exception
     */
    public static function findDownloadOrderByPk($gameId, $ver, $type)
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
            .'  from download_orders'
            .'  where game_id = $1 and game_ver = $2 and type = $3';
        $params = array($gameId, $ver, $type);
        LogUtil::sqlLog("[配信指示書:検索]"
            ." query=".$query
            ." param=(".$gameId.", ".$ver.", ".$type.")");
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