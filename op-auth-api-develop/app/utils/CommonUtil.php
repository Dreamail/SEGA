<?php

namespace app\utils;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;

/**
 * 共通Util
 * Class CommonUtil
 * @package app\utils
 */
class CommonUtil
{
    /**
     * ステータスのcauseを取得します
     * @param $auth ゲームタイトル情報の認証方式
     * @param $coefficient 係数
     * @return cause
     */
    public static function  getCause($auth, $coefficient)
    {
        return ((abs($auth) * 1000) + $coefficient) * -1;
    }

    /**
     * UTC現在時刻を取得します
     * @param $dateFormat フォーマット
     * @return UTC現在時刻
     */
    public static function getUtcTime($dateFormat, $timeFormat)
    {
        return gmdate($dateFormat) . 'T' . gmdate($timeFormat) . 'Z';
    }

    /**
     * NULL、空文字判定(NULLの場合はTrueを返却)
     *
     * @param $data
     * @return bool
     */
    public static function isEmpty($data)
    {
        if (is_null($data) || $data === "") {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 日付判定(引数は秒数)
     *
     * @param $unixTime
     * @return int
     */
    public static function convertSecondToMillis($unixTime) {

        if ($unixTime < 0 || $unixTime / CONVERT_MILLI_TO_SECOND > CALENDER_MAX_DATE) {
            return CALENDER_DEFAULT_DATE;
        } else {
            return $unixTime;
        }
    }

    /**
     * 配列内の値を1つの文字列として結合して返却
     *
     * @param $str
     * @return string
     */
    public static function createStr($str) {
        $returnData = '';

        foreach ($str as $value) {
            if (strlen($returnData) > 0) {
                $returnData = $returnData.',';
            }
            $returnData = $returnData.$value;
        }

        return $returnData;
    }

    /**
     * カンマ区切りのリストの文字列を取得します。（空の場合はnullを返却）
     *
     * @param $fileList ファイルリスト
     * @return null|string ファイルリストがない場合はnull、ある場合はカンマ区切りの文字列にして返却
     */
    public static function convertListToStr($fileList) {
        // 空の場合はnullを返却
        if (!isset($fileList)) {
            return null;
        }
        if (CommonUtil::isArrayEmpty($fileList)) {
            return null;
        }
        // リストがある場合はカンマ区切りにして返却
        return CommonUtil::createStr($fileList);
    }

    /**
     * Array値チェック
     *
     * @param $data
     * @return bool
     */
    public static function isArrayEmpty($data)
    {
        if (self::isEmpty($data)) {
            return true;
        } elseif(is_array($data)) {
            if(count($data) <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * ユーザエージェントからユーザエージェント内のゲームタイトルIDを取得します。
     * @param $userAgent ユーザエージェント
     * @return ゲームタイトルID
     */
    public static function getUaTitleId($userAgent)
    {
        // UAがない
        if (empty($userAgent)) {
            return null;
        }
        $uaArray = explode(';', $userAgent);
        // セミコロンで分割したUAがない、または、セミコロンで分割した1つ目（ゲームID）の文字長超過
        if (count($uaArray) == 0 || GAME_ID_LENGTH < strlen($uaArray[0])) {
            return null;
        }
        return $uaArray[0];
    }

    /**
     * リクエストボディをivと暗号化したリクエストパラメータに分割して配列として返却します
     * @param $requestBodyStr リクエストボディ文字列
     * @return array
     */
    public static function getRequestBodyArray($requestBodyStr)
    {
        // iv文字長
        $ivLength = openssl_cipher_iv_length(CIPHER_METHODS);

        // リクエストボディの文字列がない、または、16バイト未満の場合は空を返却
        if (empty($requestBodyStr) || strlen($requestBodyStr) < $ivLength) {
            return array();
        }

        // iv（リクエストボディの先頭16バイト）とそれ以降に分割して配列を返却
        return array(
            'iv' => substr($requestBodyStr, 0, $ivLength),
            'encRequestParams' => substr($requestBodyStr, $ivLength),
        );
    }

    /**
     * バージョンの比較を行います。
     * <pre>
     * 引数を比較して以下を返却します。
     * ・第一引数より第二引数が大きい場合、-1
     * ・第一引数と第二引数が等しい場合、0
     * ・第一引数より第二引数が小さい場合、1
     * ・第一引数、第二引数のどちらかor両方がない場合、0（比較を行わない）
     *
     * 引数のバージョンは「X.XX」の形式（小数点以下のXXは0埋めの2桁）の前提です。
     * そのまま小数に変換すると、
     * ・1.01→1.1
     * ・1.10→1.1
     * となり不具合が発生するので、小数点で分割して小数点以上と以下の数値で比較を行います。
     * ・1.01→小数点以上：1、小数点以下：1
     * ・1.10→小数点以上：1、小数点以下：10 // こっちが大きい
     * </pre>
     * @param $version バージョン
     * @param $compVersion 比較対象バージョン
     * @return int 比較結果
     */
    public static function versionCompareTo($version, $compVersion)
    {
        if (!isset($version) || $version == '' || !isset($compVersion) || $compVersion == '') {
            LogUtil::warnLog("バージョン比較の引数が存在しません。 version=" . $version . " compVersion=" . $compVersion);
        }

        // バージョンを小数点で区切って配列に格納
        $verArr = explode('.', $version);
        $compVerArr = explode('.', $compVersion);

        // 小数点以上
        $verX = (int)$verArr[0];
        $compVerX = (int)$compVerArr[0];
        if ($verX < $compVerX) {
            return -1;
        }
        if ($verX > $compVerX) {
            return 1;
        }
        // この時点で小数点以上が等しいので小数点以下の比較を行う

        // 小数点以下
        $verXX = 1 < count($verArr) ? (int)$verArr[1] : 0;
        $compVerXX = 1 < count($compVerArr) ? (int)$compVerArr[1] : 0;
        if ($verXX < $compVerXX) {
            return -1;
        }
        if ($verXX > $compVerXX) {
            return 1;
        }
        return 0;
    }

    /**
     * 基板IPが送られてきた場合は末尾が254なルータIPに変換する
     *
     * @param ip
     * @return
     */
    public static function convertRouterIp($ip) {
        if (empty($ip))
            return $ip;
        if (CommonUtil::endsWith(trim($ip),ROUTER_IP_END_STRING) || strripos($ip, '.') < 0)
            return trim($ip);
        return substr($ip,0, strripos($ip, '.') + 1). ROUTER_IP_END_STRING;
    }

    /**
     * 指定された接尾辞で終るかどうかを判定
     * @param $haystack
     * @param $needle
     * @return bool
     */
    public static function endsWith($haystack, $needle)
    {
        $length = strlen($needle);
        if ($length == 0) {
            return true;
        }
        return (substr($haystack, -$length) === $needle);
    }

    /**
     * ルータ情報から紐づく店舗と基板情報の店舗は同じか
     * @param $router
     * @param $machine
     * @return bool
     */
    public static function isSamePlace($router, $machine) {
        if (empty($router['router_id']) || empty($router['allnet_id'])) {
            if (empty($machine['allnet_id'])) {
                return true;
            }
            return false;
        }
        if ($router['allnet_id'] === $machine['allnet_id']) {
            return true;
        }
        return false;
    }

    public static function toString($str, $connect) {
        $retunVal = '';

        foreach ($str as $key => $value) {
            if(empty($retunVal)) {
                $retunVal = $retunVal. $key . "=" .$value;
            } else {
                $retunVal = $retunVal. $connect. $key . "=" .$value;
            }
        }
        return $retunVal;
    }
}
