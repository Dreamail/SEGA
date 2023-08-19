<?php
namespace app\models;

require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

class KeychipProductInfo
{
    public static function getKeychipProductInfo($keychip_id){
        // 戻り値
        $result = array();

        // キーチップIDが空の場合は取得しない
        if (empty($keychip_id))
        {
            return $result;
        }

        $query =
            'select'
            .'    keychip_id, key_server, key_keychip'
            .'  from keychip_product_info'
            .'  where keychip_id = $1'
        ;
        $params = array($keychip_id);
        LogUtil::sqlLog("[キーチップ生産情報:検索]"
            ." query=".$query
            ." param=(".$keychip_id.")");
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

    /**
     * キーチップ生産情報を登録します
     */
    public static function insert($keychipId, $keychipServer, $keyKeychip, $createDate)
    {
        $query =
            'insert into'
            .'  keychip_product_info'
            .'    (keychip_id, key_server, key_keychip, create_date)'
            .'  values'
            .'    ($1, $2, $3, $4)';
        $params = array($keychipId, $keychipServer, $keyKeychip, $createDate);
        LogUtil::sqlLog("[キーチップ生産情報:登録]"
            ." query=".$query
            ." param=(".$keychipId.", ".$keychipServer.", ".$keyKeychip.", ".$createDate.")");
        try {
            $queryResult = pg_query_params($query, $params);
            if (!$queryResult) {
                throw new Exception("キーチップ生産情報の登録に失敗しました。"
                    ." query=".$query
                    ." param=(".$keychipId.", ".$keychipServer.", ".$keyKeychip.", ".$createDate.")");
            }
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }
    }
}