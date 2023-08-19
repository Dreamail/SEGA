<?php

namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use Exception;

/**
 * 認証ログ
 * Class AuthLog
 * @package app\models
 */
class AuthLog
{
    /**
     * 認証ログを登録します
     * @param $titleId ゲームタイトルID
     * @param $titleVer ゲームタイトルVer.
     * @param $machineId 基板クライアントID
     * @param $globalIp グローバルIP
     * @param $result ステータスresult
     * @param $cause ステータスcause
     * @param $placeId 店舗ID
     * @param $countryCode 国コード
     * @param $allnetId ALL.NetID
     * @param $placeIp 店舗IP
     * @param $request 認証リクエスト文字列
     * @param $response レスポンス文字列
     * @param $debugInfo デバッグ情報
     * @param $placeDebugInfo 店舗デバッグ情報
     */
    public static function save($titleId, $titleVer, $machineId, $globalIp, $result, $cause, $placeId, $countryCode,
                                $allNetId, $placeIp, $request, $response, $debugInfo, $placeDebugInfo)
    {
        $query =
            'insert into'
            . '  logs'
            . '    (serial, stat, cause, game_id, game_ver, place_id, country_code, place_ip, global_ip, allnet_id,'
            . '     request, response, debug_info, debug_info_place, create_user_id)'
            . '  values'
            . '    ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15)';
        $params = array($machineId, $result, $cause, $titleId, $titleVer, $placeId, $countryCode, $placeIp, $globalIp, $allNetId,
            $request, $response, $debugInfo, $placeDebugInfo, DB_CREATE_USER_ID);
        LogUtil::sqlLog("[認証ログ:登録]"
            . " query=" . $query
            . " param=(" . $machineId . ", " . $result . ", " . $cause . ", " . $titleId . ", " . $titleVer . ", " . $placeId . ", " . $countryCode . ", " . $placeIp . ", " . $globalIp . ", " . $allNetId
            . ", " . $request . ", " . $response . ", " . $debugInfo . ", " . $placeDebugInfo . ", " . DB_CREATE_USER_ID . ")");
        try {
            $queryResult = pg_query_params($query, $params);
            if (!$queryResult) {
                throw new Exception("認証ログテーブルの登録に失敗しました。"
                    . " query=" . $query
                    . " param=(" . $machineId . ", " . $result . ", " . $cause . ", " . $titleId . ", " . $titleVer . ", " . $placeId . ", " . $countryCode . ", " . $placeIp . ", " . $globalIp . ", " . $allNetId
                    . ", " . $request . ", " . $response . ", " . $debugInfo . ", " . $placeDebugInfo . ", " . DB_CREATE_USER_ID . ")");
            }
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }
    }
}