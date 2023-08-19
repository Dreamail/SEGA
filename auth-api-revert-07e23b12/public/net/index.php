<?php

require_once "app/api/KeychipResponsePacketProc.php";
require_once "app/api/ServerChallengePacketProc.php";
require_once "app/api/KeyShare.php";
require_once "app/utils/LogUtil.php";

use app\api\ServerChallengePacketProc;
use app\api\KeychipResponsePacketProc;
use app\api\KeyShare;
use app\utils\LogUtil;

session_start();

preg_match('|' . dirname($_SERVER['SCRIPT_NAME']) . '/([\w\-%/]*)|', $_SERVER['REQUEST_URI'], $matches);
$paths = explode('/', $matches[1]);

switch (strtolower($_SERVER['REQUEST_METHOD']) . ':' . $paths[0]) {
    case 'post:certify':
        if ( ! isset($_SESSION['iv_keychip_challenge']) && ! isset($_SESSION['auth_keychip_challenge'] ) ) {
            $message = "認証サーバチャレンジパケット";
            //iv_keychip_challenge、auth_keychip_challengeがセッションに設定されていない場合、認証サーバチャレンジパケットと判定
            $api = new ServerChallengePacketProc();

        } else {
            $message = "認証サーバチャレンジパケット";
            //iv_keychip_challenge、auth_keychip_challengeがセッションに設定されている場合、キーチップレスポンスパケットと判定
            $api = new KeychipResponsePacketProc();
        }
        LogUtil::certifyApiLog($message."処理開始");
        $api->doApi();
        LogUtil::certifyApiLog($message."処理終了");
        break;
    case 'get:key_share':
        $api = new KeyShare();
        $api->doApi();
        break;
    default:
        // 不正アクセス
        LogUtil::warnLog("POSTメソッド以外、または存在しないURIへのアクセス ".strtolower($_SERVER['REQUEST_METHOD']) . ':' . $paths[0]);
        http_response_code(403);
        header('Content-Type: text/html; charset=utf-8');
        break;
}
