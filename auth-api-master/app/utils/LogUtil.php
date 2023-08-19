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
    private static $sid;

    /**
     * LogUtil constructor.
     */
    private function __construct()
    {
        //
    }

    /**
     * 初期化
     * @param $sid
     */
    public static function init($sid)
    {
        LogUtil::$sid = $sid;
        if(!file_exists(APP_LOG_DIR)) mkdir(APP_LOG_DIR, 0777, true);
    }

    /**
     * KeyShareApiログ出力
     * @param $message
     */
    public static function keyShareApiLog($message, $loglevel = 'INFO')
    {
        file_put_contents(LogUtil::getLogFile(KEY_SHARE_API_LOG),
            LogUtil::getMessage($loglevel, debug_backtrace(), $message), FILE_APPEND);
    }

    /**
     * ServerChallengePacketReceiverApiログ出力
     * @param $message
     */
    public static function serverChallnegePacketReceiverApiLog($message, $loglevel = 'INFO')
    {
        file_put_contents(LogUtil::getLogFile(CERTIFY_API_LOG),
            LogUtil::getMessage($loglevel, debug_backtrace(), $message), FILE_APPEND);
    }

    /**
     * KeychipResponsePacketReceiverApiログ出力
     * @param $message
     */
    public static function keychipResponsePacketReceiverApiLog($message, $loglevel = 'INFO')
    {
        file_put_contents(LogUtil::getLogFile(CERTIFY_API_LOG),
            LogUtil::getMessage($loglevel, debug_backtrace(), $message), FILE_APPEND);
    }

    /**
     * Batchログ出力
     * @param $message
     */
    public static function batchLog($message, $loglevel = 'INFO')
    {
        file_put_contents(LogUtil::getLogFile(BATCH_LOG),
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
            "[".date('Y-m-d H:i:s')."][".str_pad($logLevel, 5)."]"
            ."[SID]".LogUtil::$sid
            ." ".$dbg[0]['file']."(".$dbg[0]['line'].")"
            ." ".$message."\n".(!empty($trace) ? $trace."\n" : "");
    }

    /**
     * ログ出力ファイル名を取得します
     * @param $fileName
     * @return string
     */
    private static function getLogFile($fileName)
    {
        return APP_LOG_DIR.$fileName.'_'.date('Ymd').'.log';
    }
}