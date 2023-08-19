<?php
ini_set('include_path', get_include_path() . PATH_SEPARATOR . '.' . PATH_SEPARATOR . '/var/www/AllnetRouterLess/AuthApi');

require_once("app/api/OperationApi.php");
require_once("app/utils/LogUtil.php");

use app\api\OperationApi;
use app\utils\LogUtil;

preg_match('|' . dirname($_SERVER['SCRIPT_NAME']) . '/([\w/-/%/]*)|', $_SERVER['REQUEST_URI'], $matches);
$paths = explode('/', $matches[1]);

switch (strtolower($_SERVER['REQUEST_METHOD']) . ':' . $paths[0]) {
    case 'post:operation':
        $api = new OperationApi();
        $api->doApi();
        break;
    default:
        // 不正アクセス
        LogUtil::warnLog("POSTメソッド以外、または存在しないURIへのアクセス " . strtolower($_SERVER['REQUEST_METHOD']) . ':' . $paths[0]);
        http_response_code(403);
        header('Content-Type: text/html; charset=utf-8');
        break;
}
