<?php
ini_set('include_path', get_include_path() . PATH_SEPARATOR . '.' . PATH_SEPARATOR . '/var/www/AllnetRouterLess/AuthApi');
require_once "app/utils/CommonUtil.php";
use app\utils\CommonUtil;

/**
 * app\utils\CommonUtil#versionCompareToのテスト
 */
{
    $start = microtime(true);
    $test = array(
        // 上限バージョン, 比較対象バージョン, 期待値
        array('0.00', '0.00', 0),

        array('0.10', '0.01', 1),
        array('0.10', '0.09', 1),
        array('0.10', '0.11', -1),

        array('0.01', '0.10', -1),
        array('0.09', '0.10', -1),
        array('0.11', '0.10', 1),

        array('0.10', '0.10', 0),

        array('1.00', '0.00', 1),
        array('1.00', '0.01', 1),
        array('1.00', '0.10', 1),
        array('1.00', '0.99', 1),
        array('1.00', '1.01', -1),
        array('1.00', '1.10', -1),

        array('0.00', '1.00', -1),
        array('0.01', '1.00', -1),
        array('0.10', '1.00', -1),
        array('0.99', '1.00', -1),
        array('1.01', '1.00', 1),
        array('1.10', '1.00', 1),
    );
    $err = false;
    foreach ($test as $val) {
        $actual = CommonUtil::versionCompareTo($val[0], $val[1]);
        $expect = $val[2];
        if ($expect !== $actual) {
            echo("test error. version=".$val[0]." compVersion=".$val[1]." actual=".$actual." expect=".$expect."\n");
            $err = true;
        }
    }
    echo $err ? "test NG." : "test OK.";
}
