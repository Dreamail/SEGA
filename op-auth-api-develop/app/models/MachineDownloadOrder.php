<?php
namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

/**
 * 基板配信指示書
 * Class MachineDownloadOrder
 * @package app\models
 */
class MachineDownloadOrder
{
    /**
     * アプリケーションイメージの基板配信指示書情報を取得します
     * @param $clientId 基板クライアントID
     * @return アプリケーションイメージの基板配信指示書情報
     * @throws Exception
     */
    public static function findAppDownloadOrderByClientId($clientId)
    {
        return MachineDownloadOrder::findDownloadOrderByClientId($clientId, DOWNLOAD_ORDER_IMAGE_TYPE_APP);
    }

    /**
     * オプションイメージの基板配信指示書情報を取得します
     * @param $clientId 基板クライアントID
     * @return オプションイメージの基板配信指示書情報
     * @throws Exception
     */
    public static function findOptDownloadOrderByClientId($clientId)
    {
        return MachineDownloadOrder::findDownloadOrderByClientId($clientId, DOWNLOAD_ORDER_IMAGE_TYPE_OPT);
    }

    /**
     * 基板配信指示書情報を取得します
     * @param $serial 基板シリアル
     * @param $type 配信種別
     * @return 基板配信指示書情報
     * @throws Exception
     */
    public static function findDownloadOrderByClientId($serial, $type)
    {
        // 戻り値
        $result = array();

        // 基板クライアントIDが空の場合は取得しない
        if (empty($serial))
        {
            return $result;
        }

        $query =
            'select'
            .'    machines.serial, machines.setting, machines.group_index,'
            .'    places.allnet_id, places.country_code, machine_download_orders.uri'
            .'  from machines'
            .'  inner join places on machines.allnet_id = places.allnet_id'
            .'  left outer join machine_download_orders on machines.serial = machine_download_orders.serial'
            .'    and machine_download_orders.type = $1'
            .'  where machines.serial = $2'
        ;
        $params = array($type, $serial);
        LogUtil::sqlLog("[基板配信指示書:検索]"
            ." query=".$query
            ." param=(".$type.", ".$serial.")");
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