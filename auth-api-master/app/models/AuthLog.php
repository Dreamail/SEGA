<?php
namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use DateTime;
use DateTimeZone;
use Exception;

/**
 * 認証ログ
 * Class AuthLog
 * @package app\models
 */
class AuthLog
{
    /**
     * 認証リクエスト情報を認証ログテーブル登録します
     * @param $authSeq
     * @param $keychipId
     * @param $ivKeychipChallenge
     * @param $authKeychipChallenge
     * @param $request_1
     * @param $response_1
     */
    public static function authRequest($authSeq, $keychipId, $continueCheck, $ivKeychipChallenge, $authKeychipChallenge, $request_1, $response_1)
    {

        $request_1 = pg_escape_string($request_1);
        $response_1 = pg_escape_string($response_1);

        $query =
            'insert into'
            .'  auth_logs'
            .'    (auth_seq, keychip_id, continue, iv_keychip_challenge, auth_keychip_challenge, request_1, response_1)'
            .'  values'
            .'    ($1, $2, $3, $4, $5, $6, $7)';
        $params = array($authSeq, $keychipId, $continueCheck, $ivKeychipChallenge, $authKeychipChallenge, $request_1, $response_1);
        LogUtil::sqlLog("[認証ログ:登録]"
            ." query=".$query
            ." param=(".$authSeq.", ".$keychipId.", ".$continueCheck.", ".$ivKeychipChallenge.", ".$authKeychipChallenge.", ".$request_1.", ".$response_1.")");
        try {
            $queryResult = pg_query_params($query, $params);
            if (!$queryResult) {
                throw new Exception("認証ログテーブルの登録に失敗しました。"
                    ." query=".$query
                    ." param=(".$authSeq.", ".$keychipId.", ".$continueCheck.", ".$ivKeychipChallenge.", ".$authKeychipChallenge.", ".$request_1.", ".$response_1.")");
            }
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }
    }
    /**
     * 認証結果情報を認証ログテーブルに登録します
     * @param $authResult
     * @param $request_2
     * @param $expires
     * @param $response_2
     * @param $authSeq
     */
    public static function authResult($authResult, $cause, $request_2, $expires, $response_2, $authSeq)
    {

        $request_2 = pg_escape_string($request_2);
        $response_2 = pg_escape_string($response_2);

        $query =
            'update auth_logs'
            .'  set'
            .'    auth_result = $1, cause = $2, request_2 = $3, expires = $4, response_2 = $5'
            .'  where auth_seq = $6';
        $params = array($authResult, $cause, $request_2, $expires, $response_2, $authSeq);
        LogUtil::sqlLog("[認証ログ:更新]"
            ." query=".$query
            ." param=(".$authResult.", ".$cause.", ".$request_2.", ".$expires.", ".$response_2.", ".$authSeq.")");
        try {
            $queryResult = pg_query_params($query, $params);
            if (!$queryResult) {
                throw new Exception("認証ログテーブルの更新に失敗しました。"
                    ." query=".$query
                    ." param=(".$authResult.", ".$cause.", ".$request_2.", ".$expires.", ".$response_2.", ".$authSeq.")");
            }
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }
    }

    public static function continueCheck($keychip_id){

        $result = 'false';
        $query =
            'select'
            .'  max(expires) as max_expires, count(expires) as cnt_expires'
            .'  from auth_logs'
            .'  where keychip_id = $1 and auth_result = 0'
        ;
        $params = array($keychip_id);
        LogUtil::sqlLog("[認証ログ:検索]"
            ." query=".$query
            ." param=(".$keychip_id.")");
        try {
            $queryResult = pg_query_params($query, $params);
            $resultQuery = pg_fetch_assoc($queryResult, 0);
            if ($resultQuery['cnt_expires'] > 0)
            {
                $expire = new DateTime($resultQuery['max_expires']);

                $now = new DateTime("now",new DateTimeZone(LOCAL_TIMEZONE));
                if ( strtotime($now->format('Y-m-d H:i:s')) < strtotime($expire->format('Y-m-d H:i:s')) ) {
                    $result = 'true';
                }
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