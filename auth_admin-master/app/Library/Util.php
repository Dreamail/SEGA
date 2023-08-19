<?php

namespace App\Library;

use App\Models\ClientGroups;
use App\Models\Places;
use App\Models\Region0;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

class Util
{
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
     * 桁数チェック
     *
     * @param $data
     * @param $digits
     * @return bool
     */
    public static function digitsCheck($data, $digits) {

        if(!self::isEmpty($data)) {
            return mb_strlen($data) <= intval($digits);
        }
        return true;
    }

    /**
     * 項目数チェック
     *
     * @param $data -- 比較対象
     * @param $size -- 比較項目数
     * @return bool
     */
    public static function itemsCheck($data, $size)
    {
        if (count($data) !== intval($size)) {
            return false;
        }
        return true;
    }

    /**
     * ゲームIDフォーマットチェック
     * @param $gameId
     * @return false|int
     */
    public static function gameIdCheck($gameId)
    {
        if (!self::isEmpty($gameId)) {
            return preg_match("/^[A-Z0-9]{3,5}$/", $gameId);
        }
        return false;
    }

    /**
     * ゲームバージョンフォーマットチェック
     *
     * @param $titleVer
     * @return bool|false|int
     */
    public static function titleVerCheck($titleVer)
    {
        if (!self::isEmpty($titleVer)) {
            if (!is_numeric($titleVer)) return false;
            if (doubleval($titleVer) < 0 || doubleval($titleVer) >= 65.54) return false;
            return true;
        }
        return false;
    }

    /**
     * ゲームタイトルフォーマットチェック
     *
     * @param $title
     * @return bool|false|int
     */
    public static function titleCheck($title)
    {
        if (!self::isEmpty($title)) {
            if (mb_strlen($title, 'EUC-JP') > 32) return false;
            return true;
        }
        return false;
    }

    /**
     * URIフォーマットチェック
     *
     * @param $uri
     * @return bool|false|int
     */
    public static function gameUriCheck($uri)
    {
        if (!self::isEmpty($uri)) {
            if (mb_strlen($uri) > 128) return false;
            return true;
        }
        return false;
    }

    /**
     * HOSTフォーマットチェック
     *
     * @param $host
     * @return bool|false|int
     */
    public static function hostCheck($host)
    {
        if (!self::isEmpty($host)) {
            if (mb_strlen($host) > 128) return false;
            return true;
        }
        return false;
    }

    /**
     * 認証方式フォーマットチェック
     *
     * @param $auth
     * @return bool|false|int
     */
    public static function authCheck($auth)
    {
        if (!self::isEmpty($auth)) {
            if (!is_numeric($auth)) return false;
            if (intval($auth) < -1 || intval($auth) > 1) return false;
            return true;
        }
        return false;
    }

    /**
     * シリアルフォーマットチェック
     * @param $serial
     * @return false|int
     */
    public static function serialCheck($serial)
    {
        if (!self::isEmpty($serial)) {
            return preg_match("/^[A-Z0-9]{11}$/", $serial);
        }
        return false;
    }

    /**
     * イメージタイプチェック
     *
     * @param $type
     * @return bool
     */
    public static function imageTypeCheck($type)
    {
        if (!self::isEmpty($type)) {
            if (Config::get('const.IMAGE_TYPE_NUM')['0'] !== $type && Config::get('const.IMAGE_TYPE_NUM')['1'] !== $type)
                return false;
            return true;
        }
        return false;
    }

    /**
     * URIフォーマットチェック(空の場合は有効とする)
     *
     * @param $uri
     * @return bool
     */
    public static function uriCheck($uri)
    {
        if (!self::isEmpty(trim($uri))) {
            return preg_match('@^https?+://@i', $uri);
        }
        return true;
    }

    /**
     * 店舗名のフォーマットチェック
     *
     * @param name
     * @return bool
     */
    public static function nameCheck($name)
    {
        if (!self::isEmpty($name)) {
            return mb_ereg("[ｱ-ﾝ]", $name);
        }
        return false;
    }

    /**
     * 店舗IPのフォーマットチェック
     * @param string $placeIp
     * @return mixed
     */
    public static function placeIpCheck($placeIp)
    {
        if (!self::isEmpty($placeIp)){
            return preg_match('/^(([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])[.]){3}([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/', $placeIp);
        }
        return false ;
    }


    /**
     * 電話番号のフォーマットチェック
     *
     * @param $tel
     * @return bool
     */
    public static function telCheck($tel)
    {
        if (!self::isEmpty($tel)) {
            return preg_match("/^[0-9]{0,16}$/", $tel);
        }
        return true;
    }

    /**
     * 住所のフォーマットチェック
     *
     * @param $address
     * @return bool
     */
    public static function addressCheck($address)
    {
        if (!self::isEmpty($address)) {
            return mb_strlen($address) <= 200;
        }
        return false;
    }

    /**
     * アクセス情報のフォーマットチェック
     *
     * @param $station
     * @return bool
     */
    public static function stationCheck($station)
    {
        if (!self::isEmpty($station)) {
            return mb_strlen($station) <= 160;
        }
        return false;
    }

    /**
     * PR文のフォーマットチェック
     *
     * @param $specialinfo
     * @return bool
     */
    public static function specialInfoCheck($specialinfo)
    {
        if (!self::isEmpty($specialinfo)) {
            return mb_strlen($specialinfo) <= 256;
        }
        return false;

    }

    /**
     *地域名0のフォーマットチェック
     *
     * @param $region0
     * @return bool
     */
    public static function region0Check($region0)
    {
        $region = new Region0();
        $data = $region->getData(Config::get('const.COUNTY_CODE'),$region0);
        if (count($data) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 短い店舗名のフォーマットチェック
     *
     * @param $nickname
     * @return bool|false|int
     */
    public static function nicknameCheck($nickname)
    {
        if (!self::isEmpty($nickname)) {
            return mb_ereg("[ｱ-ﾝ]", $nickname);
        }
        return false;
    }

    /**
     * 店舗タイプのフォーマットチェック
     *
     * @param $locationType
     * @return bool
     */
    public static function locationTypeCheck($locationType)
    {
        return ($locationType == 1 || $locationType == -1);
    }

    /**
     * All.NetIDのフォーマットチェック
     *
     * @param $allNetId
     * @return bool
     */
    public static function allNetIdCheck($allNetId)
    {
        if (!self::isEmpty($allNetId)) {
            if (!is_numeric($allNetId)) return false;
            return true;
        }
        return false;
    }

    /**
     * 店舗IDのフォーマットチェック
     *
     * @param $placeId
     * @return bool
     */
    public static function placeIdCheck($placeId)
    {
        if(preg_match("/^[0-9]{1,4}$/", $placeId)) {
            if(intval($placeId) > 0) {
                return intval($placeId) <= Config::get('const.MAX_PLACE_ID');
            }
            return false;
        }
        return false;
    }

    /**
     * グループ分け番号のフォーマットチェック
     *
     * @param $groupIndex
     * @return bool
     */
    public static function groupIndexCheck($groupIndex)
    {
        if (!self::isEmpty($groupIndex)) {
            return preg_match("/^[0-9]{1,10}$/", $groupIndex);
        }
        return false;
    }

    /**
     * 通信セッティングのフォーマットチェック
     *
     * @param $setting
     * @return bool
     */
    public static function settingCheck($setting)
    {
        if (!self::isEmpty($setting)) {
            return preg_match("/^[1-3]$/", $setting);
        }
        return false;
    }

    /**
     * 削除理由番号のフォーマットチェック
     *
     * @param $deletionReasonNo
     * @return bool|false|int
     */
    public static function deletionReasonNoCheck($deletionReasonNo)
    {
        if (!self::isEmpty($deletionReasonNo)) {
            return preg_match("/^[0-9]$/", $deletionReasonNo);
        }
        return false;
    }

    /**
     * 英数字チェック(空の場合はfalse)
     *
     * @param $value
     * @return bool
     */
    public static function ctypeAlnum($value) {

        if (!self::isEmpty($value)) {
            return preg_match("/^[!-~]+$/", $value);
        }
        return false;
    }

    /**
     * バージョンのフォーマットチェック
     *
     * @param $version
     * @return bool|false|int
     */
    public static function versionCheck($version) {
        if (!self::isEmpty($version)) {
            return preg_match("/^[0-9]{1,10}$/", $version);
        }
        return false;
    }

    /**
     * 削除フラグチェック
     * @param $delFlg
     * @return bool
     */
    public static function delFlgCheck($delFlg) {
        if (self::isEmpty($delFlg)) {
            return true;
        }
        return $delFlg == '1';
    }

    /**
     * 店舗名のチェックを行います。
     * 第一引数の店舗名が、第二引数の店舗IDから取得した店舗名と一致するかチェックします。
     * @param $checkPlaceName チェックする店舗名
     * @param $placeId DBから店舗名を取得する店舗ID
     * @return bool 店舗名が一致した場合、true
     */
    public static function checkPlaceNameById($checkPlaceName, $placeId) {
        $models = new Places();
        $places = $models->getDataPlaceId(intval($placeId))->toArray();
        if (!empty($places) && count($places) == 1) {
            return strcmp($checkPlaceName, $places[0]->name) == 0;
        }
        return false;
    }

    /**
     * 店舗名のチェックを行います。
     * 第一引数の店舗名が、第二引数のALL.Net IDから取得した店舗名と一致するかチェックします。
     * @param $checkPlaceName チェックする店舗名
     * @param $allNetId DBから店舗名を取得するALL.Net ID
     * @return bool 店舗名が一致した場合、true
     */
    public static function checkPlaceNameByAllNetId($checkPlaceName, $allNetId) {
        $models = new Places();
        $places = $models->getDataAllNetId(intval($allNetId))->toArray();
        if (!empty($places) && count($places) == 1) {
            return strcmp($checkPlaceName, $places[0]->name) == 0;
        }
        return false;
    }

    /**
     * 日付を指定TIMEZONEに変更(CSV⇒DEFAULT)
     * @param $time
     * @param $format
     * @return string
     */
    public static function timezoneToDefault($time, $format) {

        date_default_timezone_set(Config::get('const.CSV_TIME_ZONE'));

        $boardDate = new \DateTime($time);
        $boardDate->setTimezone(new \DateTimeZone(Config::get('const.DEFAULT_TIME_ZONE')));

        return $boardDate->format($format);
    }

    /**
     * 日付を指定TIMEZONEに変更(DEFAULT⇒CSV)
     * @param $time
     * @param $format
     * @return string
     */
    public static function timezoneToCsv($time, $format) {

        date_default_timezone_set(Config::get('const.DEFAULT_TIME_ZONE'));

        $boardDate = new \DateTime($time);
        $boardDate->setTimezone(new \DateTimeZone(Config::get('const.CSV_TIME_ZONE')));

        return $boardDate->format($format);
    }

    /**
     * BOMの削除を行います。
     * <pre>
     * 引数の文字列内に存在するBOM（Byte Order Mark）を空文字に置換して返却します。
     * <pre>
     * @param $subject BOMを削除する文字列または文字列の配列
     * @return string BOMを削除した文字列または文字列の配列
     */
    public static function deleteBOM($subject) {
        if ($subject == false || $subject == null) {
            return $subject;
        }
        if (is_array($subject)) {
            if (count($subject) == 0) {
                return $subject;
            }
        } else {
            if ($subject == "") {
                return $subject;
            }
        }
        $result = preg_replace('/\xEF\xBB\xBF/', '', $subject);
        return $result;
    }
}
