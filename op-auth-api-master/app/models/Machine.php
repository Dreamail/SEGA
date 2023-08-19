<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

/**
 * 基板
 * Class Machine
 * @package app\models
 */
class Machine
{
    /**
     * 基板クライアントID（PK）で基板情報を取得します。
     * また、基板に紐づく、基板ステータス、店舗、
     * 店舗に紐づく地域を取得します。
     * @param $machineId 基板クライアントID
     * @return 基板とそれに紐づく情報
     */
    public static function findByMachineId($machineId)
    {
        // 戻り値
        $result = array();

        // 基板クライアントIDが空の場合は取得しない
        if (empty($machineId)) {
            return $result;
        }

        $query =
            'select'
            . '    machines.serial, machines.game_id, machines.reserved_game_id, machines.setting, machines.allnet_id, machines.place_id,'
            . '    machine_statuses.serial as stat_serial,'
            . '    places.name, places.bill_code, places.nickname, places.country_code, places.region0_id, places.timezone,'
            . '    routers.place_ip,'
            . '    region0.name AS r0_name,'
            . '    region1.name AS r1_name,'
            . '    region2.name AS r2_name,'
            . '    region3.name AS r3_name,'
            . '    routers.router_id'
            . '  from machines'
            . '  left outer join machine_statuses   on machines.serial = machine_statuses.serial'
            . '  left outer join places             on machines.allnet_id = places.allnet_id'
            . '  left outer join routers            on machines.allnet_id = routers.allnet_id'
            . '      and routers.router_type_id in (select router_type_id from router_types_attributes where router_types_attributes.attributes_code = $1)'
            . '  left outer join region0            on places.region0_id = region0.region_id'
            . '  left outer join region1            on places.country_code = region1.country_code and places.region1_id = region1.region_id'
            . '  left outer join region2            on places.country_code = region2.country_code and places.region2_id = region2.region_id'
            . '  left outer join region3            on places.country_code = region3.country_code and places.region3_id = region3.region_id'
            . '  where machines.serial = $2';
        $params = array(SLIM_ROUTER_TYPE_ATTR, $machineId);
        LogUtil::sqlLog("[基板情報:検索]"
            . " query=" . $query
            . " param=(" . SLIM_ROUTER_TYPE_ATTR . ", " . $machineId . ")");
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

    /**
     * 基板クライアントIDに紐づく各店舗での最終アクセス日時を取得
     * @param $clientId 基板クライアントID
     * @return 基板とそれに紐づく情報
     */
    public static function findLastAccess($clientId)
    {

        // 戻り値
        $result = array();

        // 基板クライアントIDが空の場合は取得しない
        if (empty($clientId)) {
            return $result;
        }

        $query =
            'select'
            . '    s1.client_id, s1.place_id,'
            . '    s2.title_id, s2.title_ver, s2.place_ip, s2.firm_ver, s2.boot_ver, s2.format_ver, s2.user_agent,'
            . '    TO_CHAR(s2.last_access, \'yyyy-mm-dd hh24:mi:ss\') last_access, TO_CHAR(s2.last_auth, \'yyyy-mm-dd hh24:mi:ss\') last_auth'
            . '  from machines s1'
            . '  inner join machine_statuses s2 on s1.client_id = s2.client_id'
            . '  where s1.client_id = $1';
        $params = array($clientId);
        LogUtil::sqlLog("[基板情報:検索]"
            . " query=" . $query
            . " param=(" . $clientId . ")");
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

    public static function exchangeMachineGameId($machine)
    {
        $query = null;
        $query =
            'update machines'
            . '  set'
            . '    game_id = $1, reserved_game_id = $2,'
            . '    update_date = current_timestamp, update_user_id = $3'
            . '  where serial = $4';

        $params = array($machine['reserved_game_id'], $machine['game_id'], DB_UPDATE_USER_ID, $machine['serial']);
        LogUtil::sqlLog("[基板情報:更新]"
            . " query=" . $query
            . " param=(" . $machine['reserved_game_id'] . ", " . $machine['game_id'] . ", " . DB_UPDATE_USER_ID . ", " . $machine['serial'] . ")");
        try {
            $queryResult = pg_query_params($query, $params);
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }
    }
}