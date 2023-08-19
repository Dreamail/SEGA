<?php

namespace app\utils;

require_once "app/constant.php";
require_once "config/log_config.php";

use Exception;

/**
 * ログUtil
 * Class LogUtil
 * @package app\utils
 */
class LogUtil
{
    /** スレッド識別用ユニークID */
    private static $uid;

    /**
     * LogUtil constructor.
     */
    private function __construct()
    {
        //
    }

    /**
     * 初期化
     * @param $uid ユニークID
     */
    public static function init($uid)
    {
        LogUtil::$uid = $uid;
        if (!file_exists(APP_LOG_DIR)) mkdir(APP_LOG_DIR, 0777, true);
    }

    /**
     * ActivateApiログ出力
     * @param $message
     */
    public static function operationApiLog($message, $loglevel = 'INFO')
    {
        file_put_contents(LogUtil::getLogFile(OPERATION_API_LOG),
            LogUtil::getMessage($loglevel, debug_backtrace(), $message), FILE_APPEND);
    }

    /**
     * DownloadOrderApiログ出力
     * @param $message
     */
    public static function downloadOrderApiLog($message, $loglevel = 'INFO')
    {
        file_put_contents(LogUtil::getLogFile(DELIVERY_INSTRUCTION_API_LOG),
            LogUtil::getMessage($loglevel, debug_backtrace(), $message), FILE_APPEND);
    }

    /**
     * ApplicationApiログ出力
     * @param $message
     */
    public static function applicationApiLog($message, $loglevel = 'INFO')
    {
        file_put_contents(LogUtil::getLogFile(APP_REPORT_LOG),
            LogUtil::getMessage($loglevel, debug_backtrace(), $message), FILE_APPEND);
    }

    /**
     * OptionApiログ出力
     * @param $message
     */
    public static function optionApiLog($message, $loglevel = 'INFO')
    {
        file_put_contents(LogUtil::getLogFile(OPT_REPORT_LOG),
            LogUtil::getMessage($loglevel, debug_backtrace(), $message), FILE_APPEND);
    }

    /**
     * MaimaiApiログ出力
     * @param $message
     */
    public static function maimaiApiLog($message, $loglevel = 'INFO')
    {
        file_put_contents(LogUtil::getLogFile(MAIMAI_API_LOG),
            LogUtil::getMessage($loglevel, debug_backtrace(), $message), FILE_APPEND);
    }

    /**
     * DEBUGログ出力
     * @param $message
     */
    public static function debugLog($message)
    {
        if (DEBUG) {
            file_put_contents(LogUtil::getLogFile(DEBUG_LOG),
                LogUtil::getMessage('DEBUG', debug_backtrace(), $message), FILE_APPEND);
        }
    }

    /**
     * WARNログ出力
     * @param $message
     */
    public static function warnLog($message)
    {
        error_log(LogUtil::getMessage('WARN', debug_backtrace(), $message),
            3, LogUtil::getLogFile(WARN_LOG));
    }

    /**
     * ERRORログ出力
     * @param Exception $e
     */
    public static function errorLog(Exception $e)
    {
        error_log(LogUtil::getMessage('ERROR', debug_backtrace(), $e->getMessage(), $e->getTraceAsString()),
            3, LogUtil::getLogFile(ERROR_LOG));
    }

    /**
     * SQLログ出力（DEBUG）
     * @param $message
     */
    public static function sqlLog($message)
    {
        if (DEBUG) {
            file_put_contents(LogUtil::getLogFile(SQL_LOG),
                LogUtil::getMessage('DEBUG', debug_backtrace(), $message), FILE_APPEND);
        }
    }

    /**
     * ログ出力メッセージを取得します
     * @param $logLevel
     * @param $dbg
     * @param $message
     * @param null $trace
     * @return string
     */
    private static function getMessage($logLevel, $dbg, $message, $trace = null)
    {
        return
            "[" . date('Y-m-d H:i:s') . "][" . str_pad($logLevel, 5) . "]"
            . "[UID]" . LogUtil::$uid
            . " " . $dbg[0]['file'] . "(" . $dbg[0]['line'] . ")"
            . " " . $message . "\n" . (!empty($trace) ? $trace . "\n" : "");
    }

    /**
     * ログ出力ファイル名を取得します
     * @param $fileName
     * @return string
     */
    private static function getLogFile($fileName)
    {
        return APP_LOG_DIR . $fileName . '_' . date('Ymd') . '.log';
    }
}