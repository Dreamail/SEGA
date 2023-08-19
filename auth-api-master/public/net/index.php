<?php

require_once "app/api/KeychipResponsePacketReceiver.php";
require_once "app/api/ServerChallengePacketReceiver.php";
require_once "app/api/KeyShare.php";
require_once "app/utils/LogUtil.php";

use app\api\ServerChallengePacketReceiver;
use app\api\KeychipResponsePacketReceiver;
use app\api\KeyShare;
use app\utils\LogUtil;

session_start();

LogUtil::init('SID_'.session_id());

set_error_handler(
    function ($errno, $errstr, $errfile, $errline) {
        // エラーが発生した場合、ErrorExceptionを発生させる
        throw new ErrorException(
            $errstr, 0, $errno, $errfile, $errline
        );
    }
);

preg_match('|' . dirname($_SERVER['SCRIPT_NAME']) . '/([\w\-%/]*)|', $_SERVER['REQUEST_URI'], $matches);
$paths = explode('/', $matches[1]);
$certify_result_flg = isset($_SESSION['iv_keychip_challenge']) && isset($_SESSION['auth_keychip_challenge'] ) ? "2" : "";

switch (strtolower($_SERVER['REQUEST_METHOD']) . ':' . $paths[0] . $certify_result_flg) {

    case 'post:certify':
        $api = new ServerChallengePacketReceiver();
        $api->doApi();
        break;
    case 'post:certify2':
        $api = new KeychipResponsePacketReceiver();
        $api->doApi();
        break;
    case 'get:certify':
        LogUtil::warnLog("認証APIへのGETリクエスト");
        $api = new ServerChallengePacketReceiver();
        $api->doApi();
        break;
    case 'get:certify2':
        LogUtil::warnLog("認証APIへのGETリクエスト");
        $api = new KeychipResponsePacketReceiver();
        $api->doApi();
        break;
    case 'get:key_share':
        $api = new KeyShare();
        $api->doApi();
        break;
    default:
        // 不正アクセス
        LogUtil::warnLog("存在しないURIへのアクセス ".strtolower($_SERVER['REQUEST_METHOD']) . ':' . $paths[0]);
        http_response_code(403);
        header('Content-Type: text/html; charset=utf-8');
        break;
}
