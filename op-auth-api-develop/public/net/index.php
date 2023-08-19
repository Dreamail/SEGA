<?php
ini_set('include_path', get_include_path() . PATH_SEPARATOR . '.' . PATH_SEPARATOR . 'D:/git/allnetauth2.0/op-auth-api/');

require_once("app/api/OperationApi.php");
require_once("app/api/DownloadOrderApi.php");
require_once("app/api/AppImageReportApi.php");
require_once("app/api/OptImageReportApi.php");
require_once "app/utils/SignatureVerify.php";
require_once("app/utils/LogUtil.php");

use app\api\OperationApi;
use app\api\DownloadOrderApi;
use app\api\AppImageReportApi;
use app\api\OptImageReportApi;
use app\utils\SignatureVerify;
use app\utils\LogUtil;

preg_match('|' . dirname($_SERVER['SCRIPT_NAME']) . '/([\w/-/%/]*)|', $_SERVER['REQUEST_URI'], $matches);
$paths = explode('/', $matches[1]);

switch (strtolower($_SERVER['REQUEST_METHOD']) . ':' . $paths[0]) {
    case 'post:operation':
        $api = new OperationApi();
        $api->doApi();
        break;
    case 'post:downloadorder':
        $api = new DownloadOrderApi();
        $api->doApi();
        break;
    case 'post:report':
        $json_string = file_get_contents('php://input');

        $json = mb_convert_encoding($json_string, 'UTF8', 'ASCII,JIS,UTF-8,EUC-JP,SJIS-WIN');

        $arr = json_decode($json,true);

        $resultBody = RESULT_BODY_NG;

        $signatureVerify = new SignatureVerify();
        if(isset($arr['auth_data']) && $signatureVerify->verify($arr['auth_data'], 'report')) {
            if(isset($arr[IMAGE_TYPE_APP])) {
                $app = new AppImageReportApi();
                $resultBody = $app->doApi($arr[IMAGE_TYPE_APP]);
            } elseif(isset($arr[IMAGE_TYPE_OPT])) {
                $opt = new OptImageReportApi();
                $resultBody = $opt->doApi($arr[IMAGE_TYPE_OPT]);
            } else {
                LogUtil::applicationApiLog("不正なイメージキーが設定されています。 ", "ERROR");
            }
        } else {
            LogUtil::applicationApiLog("署名検証NG ", "ERROR");
        }

        LogUtil::debugLog($resultBody);
        // レスポンス
        header('Content-Type: text/html; charset=utf-8');
        http_response_code(200);
        echo $resultBody;
    default:
        // 不正アクセス
        LogUtil::warnLog("POSTメソッド以外、または存在  しないURIへのアクセス " . strtolower($_SERVER['REQUEST_METHOD']) . ':' . $paths[0]);
        http_response_code(403);
        header('Content-Type: text/html; charset=utf-8');
        break;
}
