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
            'SELECT m.serial, ms.serial as stat_serial, m.game_id, m.reserved_game_id, m.setting, p.allnet_id, m.place_id, p.name, p.bill_code, '
            .'p.nickname, p.country_code, p.region0_id, r0.name AS r0_name, r1.name AS r1_name, r2.name AS r2_name, r3.name AS r3_name, p.timezone '
            .'FROM machines m '
            .'LEFT OUTER JOIN machine_statuses ms ON m.serial = ms.serial '
            .'LEFT OUTER JOIN places p ON m.allnet_id = p.allnet_id '
            .'LEFT OUTER JOIN region0 r0 ON p.country_code = r0.country_code AND p.region0_id = r0.region_id '
            .'LEFT OUTER JOIN region1 r1 ON p.country_code = r1.country_code AND p.region1_id = r1.region_id '
            .'LEFT OUTER JOIN region2 r2 ON p.country_code = r2.country_code AND p.region2_id = r2.region_id '
            .'LEFT OUTER JOIN region3 r3 ON p.country_code = r3.country_code AND p.region3_id = r3.region_id '
            .'WHERE m.serial = $1';
        $params = array($machineId);
        LogUtil::sqlLog("[基板情報:検索]"
            . " query=" . $query
            . " param=(" . $machineId . ")");
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

    public static function insertMachine($machine) {

        $query =
            'INSERT INTO machines'
            . ' (serial, allnet_id, game_id, place_id, create_user_id, update_user_id)'
            . ' VALUES '
            . ' ($1, $2, $3, $4, $5, $6)';

        $params = array($machine['serial'], $machine['allnet_id'], $machine['game_id']
                        , $machine['place_id'], DB_CREATE_USER_ID, DB_CREATE_USER_ID);
        LogUtil::sqlLog("[基板情報:登録]"
            . " query=" . $query
            . " param=(" . $machine['serial'] . ", " . $machine['allnet_id'] . ", "
            . $machine['game_id'] . ", " . $machine['place_id'] . ", " . DB_CREATE_USER_ID .", " . DB_CREATE_USER_ID .")");
        try {
            $queryResult = pg_query_params($query, $params);
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }
    }
    public static function updateMachine($machine, $router) {
        $query =
            'UPDATE machines'
            . ' SET'
            . '  allnet_id = $1, group_index = $2, place_id = $3, register_timestamp = current_timestamp,'
            . '  update_date = current_timestamp, update_user_id = $4'
            . ' WHERE serial = $5';

        $params = array($router['allnet_id'], DEFAULT_INDEX, $router['place_id'], DB_UPDATE_USER_ID, $machine['serial']);
        LogUtil::sqlLog("[基板情報:更新]"
            . " query=" . $query
            . " param=(" . $router['allnet_id'] . ", " . DEFAULT_INDEX . ", "
            . $router['place_id'] . ", " . DB_UPDATE_USER_ID . ", " . $machine['serial'] .")");
        try {
            $queryResult = pg_query_params($query, $params);
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }
    }

    /**
     * 同一店舗に設置されている他の基板情報を取得
     * @param $serial
     * @param $allnetId
     * @param $groupIndex
     * @param $gameId
     * @return array
     * @throws Exception
     */
    public static function findGroupSerials($serial, $allnetId, $groupIndex, $gameId)
    {
        // 戻り値
        $result = array();

        // 基板クライアントIDが空の場合は取得しない
        if (empty($serial)) {
            return $result;
        }

        $query =
            'SELECT serial FROM machines'
            . '  WHERE allnet_id = $1 AND game_id = $2 AND group_index = $3 AND serial <> $4 ';
        $params = array($allnetId, $gameId, $groupIndex, $serial);
        LogUtil::sqlLog("[基板情報:検索]"
            . " query=" . $query
            . " param=(" . $allnetId . ", " . $gameId . ", ". $groupIndex . ", ".  $serial . ")");
        try {
            $queryResult = pg_query_params($query, $params);
            for ($i = 0; $i < count(pg_fetch_all_columns($queryResult)); $i++) {
                // 実行結果のi行目の行情報を取り出す
                $row = pg_fetch_row($queryResult, $i);
                $result[$i] = $row[0];
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