<?php
namespace app\batch;

ini_set('include_path', get_include_path() . PATH_SEPARATOR . '.' . PATH_SEPARATOR . '/var/www/OpAuthorization');

require_once "app/utils/LogUtil.php";
require_once "app/utils/DbUtil.php";
require_once "app/models/KeychipProductInfo.php";

use app\models\KeychipProductInfo;
use app\utils\DbUtil;
use app\utils\LogUtil;

function getSerialKey($date) {

    $hostname = 'testkeycrafts01-v';
    $filePath = '/var/keycraft-server/product_logs/';
    $fileName = 'pd_keychip_'.(empty($date) ? date("Ymd", strtotime("-1 day")) : $date).".csv";
    $sageFilePath = '/var/www/OpAuthorization/tmp/';
    $username = 'allnet';

    $commond = "scp ".$username."@".$hostname.":".$filePath.$fileName." ".$sageFilePath;

    LogUtil::batchLog("キーチップ生産情報取込コマンド：" .$commond);
    exec($commond);

    // ファイルが存在しているかチェックする
    if (file_exists($sageFilePath.$fileName) && ($handle = fopen($sageFilePath.$fileName, "r")) !== FALSE) {

        LogUtil::batchLog("キーチップ生産情報取込処理：開始");
        // DB接続
        DbUtil::getConnect();
        DbUtil::trace();

        try {
            // 1行ずつfgetcsv()関数を使って読み込む
            while (($data = fgetcsv($handle))) {
                LogUtil::batchLog("キーチップ生産情報取込内容(keychipId=".$data[0]." keychipServer=".$data[1]." keyKeychip=".$data[2].")");
                $keychip_product_info = KeychipProductInfo::getKeychipProductInfo($data[0]);
                if ( count($keychip_product_info) == 0 ) {
                    KeychipProductInfo::insert($data[0], $data[1], $data[2], $data[3]);
                }
            }
            fclose($handle);
        } catch ( Exception $e ) {
            http_response_code(500);
            LogUtil::batchLog("エラーが発生：".$e->getMessage());
        } finally {
            DbUtil::untrace();
            DbUtil::closeConnect();

            LogUtil::batchLog("キーチップ生産情報取込処理：終了");
        }
    }
}

$date = null;

if( $argc == 2 ){
    $date = $argv[1];
}
getSerialKey($date);