<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\CommonUtil;
use app\utils\LogUtil;
use Exception;

/**
 * ルータ情報
 * Class Routers
 * @package app\models
 */
class Routers
{
    public static function findRouter($ip)
    {
        // 戻り値
        $result = array();

        // IPが空の場合は取得しない
        if (empty($ip)) {
            return $result;
        }

        $query =
            'SELECT r.router_id, p.allnet_id, p.place_id, p.name, p.bill_code, p.nickname, p.country_code, p.region0_id, r0.name as r0_name, r1.name as r1_name, r2.name as r2_name, r3.name as r3_name, p.timezone '
            . 'FROM routers r LEFT OUTER JOIN places p ON r.allnet_id = p.allnet_id '
            . 'LEFT OUTER JOIN region0 r0 ON p.country_code = r0.country_code AND p.region0_id = r0.region_id '
            . 'LEFT OUTER JOIN region1 r1 ON p.country_code = r1.country_code AND p.region1_id = r1.region_id '
            . 'LEFT OUTER JOIN region2 r2 ON p.country_code = r2.country_code AND p.region2_id = r2.region_id '
            . 'lEFT OUTER JOIN region3 r3 ON p.country_code = r3.country_code AND p.region3_id = r3.region_id '
            . 'WHERE r.place_ip = $1 OR r.place_ip = $2';

        $convertIp = CommonUtil::convertRouterIp($ip);
        $params = array($ip, $convertIp);
        LogUtil::sqlLog("[ルータ情報:検索]"
            . " query=" . $query
            . " param=(" . $ip . ", " . $convertIp . ")");
        try {
            $queryResult = pg_query_params($query, $params);
            if (0 < count(pg_fetch_all_columns($queryResult))) {
                $result = pg_fetch_assoc($queryResult, 0);
            }
        } catch (Exception $e) {
            LogUtil::debugLog("1");
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }

        return $result;
    }
}