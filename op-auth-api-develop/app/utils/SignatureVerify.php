<?php

namespace app\utils;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;
use DateTime;
use DateTimeZone;

/**
 * Class SignatureVerify
 * @package app\utils
 */
class SignatureVerify
{
    // エラー時のケース
    public $cause = '';

    /**
     * 署名検証
     * @param $authData 認証結果
     * @param $serial キーチップ情報
     * @return bool 署名検証結果
     */
    public function verify($authData, $serial)
    {
        if (!isset($authData)) {
            LogUtil::debugLog("[署名検証]認証結果が存在しない");
            return false;
        }
        $filePath = SIGNATURE_FILE_PATH.uniqid(rand()).$serial.".pem";
        $this->createFile($authData, $filePath);

        //TODO 証明書名は仮
        $cipherKey = CERT_FILE_PATH;

        $result = $this->valueSplit($this->cmsVerify($filePath, $cipherKey));
        return $this->checkAuthdata($result);
    }

    /**
     * ファイル作成
     */
    public function createFile($text, $filePath) {
        // ファイルを書き込みモードで開く
        $file_handle = fopen( $filePath, "w");
        // ファイルへデータを書き込み
        fwrite( $file_handle, CMS_START. "\n". $text. "\n". CMS_END);
        // ファイルを閉じる
        fclose($file_handle);
    }

    /**
     * 署名検証処理(exec)
     * @param $filePath
     * @param $cipherKey
     * @return string $result
     */
    private function cmsVerify($filePath, $cipherKey) {

        return exec("openssl cms -verify -inform PEM -in ".$filePath." -certfile " . $cipherKey . " -noverify");
    }
    
    /**
     * 認証結果を分割
     * @param $value
     * @return array
     */
    private function valueSplit($value) {
        $data = array();
        if(mb_substr_count($value,'&') == count(AUTH_ITEMS) - 1){
            preg_match_all("/([^&= ]+)=([^&= ]+)/", $value, $r);
            $data = array_combine($r[1], $r[2]);
        }
        return $data;
    }

    /**
     * 認証結果チェック
     */
    private function checkAuthdata($value) {

        if(empty($value)) {
            LogUtil::debugLog("[認証結果チェック]認証結果取得不可 authData=" . CommonUtil::toString($value, CONNECT_CHAR));
            return false;
        } elseif (count(AUTH_ITEMS) == count(array_intersect(AUTH_ITEMS, $value))){
            LogUtil::debugLog("[認証結果チェック]認証結果項目数不足 authData=" . CommonUtil::toString($value, CONNECT_CHAR));
            return false;
        } elseif ($value['result'] != AUTH_SUCCESS) {
            LogUtil::debugLog("[認証結果チェック]認証失敗 authData=" . CommonUtil::toString($value, CONNECT_CHAR));
            return false;
        }

        $nowDate = new DateTime('now');
        $limit = new DateTime($value['limit']);
        $nowDate->setTimeZone(new DateTimeZone(TIMEZONE));
        $limit->setTimeZone(new DateTimeZone(TIMEZONE));

        if ($nowDate > $limit) {
            LogUtil::debugLog("[認証結果チェック]認証リミットオーバー authData=" . CommonUtil::toString($value, CONNECT_CHAR));
            return false;
        }
        return true;
    }
}