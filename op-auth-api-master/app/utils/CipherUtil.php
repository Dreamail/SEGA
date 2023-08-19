<?php

namespace app\utils;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";

use app\utils\LogUtil;

/**
 * Class CipherUtil
 * @package app\utils
 */
class CipherUtil
{
    /**
     * 文字列を復号化します
     * @param $str 暗号化文字列
     * @param $cipherKey 暗号化キー
     * @param $iv iv
     * @return string 複合化後の文字列
     */
    public static function decrypt($encStr, $cipherKey, $iv)
    {
        if (!isset($encStr) || !isset($cipherKey) || !isset($iv)) {
            // 引数のいずれかがない場合、復号化しない
            LogUtil::debugLog("[復号化]引数が不正です");
            return "";
        }
        return openssl_decrypt($encStr, CIPHER_METHODS, $cipherKey, OPENSSL_RAW_DATA, $iv);
    }

    /**
     * 文字列を暗号化します
     * @param $str 暗号化対象文字列
     * @param $cipherKey 暗号キー
     * @param $iv iv
     * @return string 暗号化後の文字列
     */
    public static function encrypt($decStr, $cipherKey, $iv)
    {
        LogUtil::debugLog("[暗号化]decStr=" . $decStr . " cipherKey=" . $cipherKey);
        if (!isset($decStr) || !isset($cipherKey) || !isset($iv)) {
            // 引数のいずれかがない場合、復号化しない
            LogUtil::debugLog("[暗号化]引数が不正です");
            return null;
        }
        return openssl_encrypt($decStr, CIPHER_METHODS, $cipherKey, OPENSSL_RAW_DATA, $iv);
    }

    /**
     * IVを生成します。
     * @return IV
     */
    public static function bgenerateIv()
    {
        $ivLength = openssl_cipher_iv_length(CIPHER_METHODS);
        $iv = openssl_random_pseudo_bytes($ivLength);
        // $iv = substr(bin2hex($iv), 0, $ivLength);
        // LogUtil::debugLog("[IV生成]ivLength=" . $ivLength . " iv=" . $iv);
        return $iv;
    }
}