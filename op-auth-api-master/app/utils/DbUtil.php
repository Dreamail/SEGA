<?php

namespace app\utils;

require_once "app/constant.php";
require_once "config/db_config.php";
require_once "config/log_config.php";

use Exception;

/**
 * DB Util
 * Class DbUtil
 * @package app\utils
 */
class DbUtil
{
    /**
     * コネクションを取得します
     * @return bool
     */
    public static function getConnect()
    {
        pg_connect(
            'host=' . PG_HOST .
            ' port=' . PG_PORT .
            ' dbname=' . PG_DATABASE .
            ' user=' . PG_USER .
            ' password=' . PG_PASSWORD
        );
        pg_query("BEGIN");
    }

    /**
     * コネクションをクローズします
     */
    public static function closeConnect()
    {
        pg_query("COMMIT");
        pg_close();
    }

    /**
     * ロールバック
     */
    public static function rollback()
    {
        pg_query("ROLLBACK");
    }

    /**
     * トレースログ出力
     */
    public static function trace()
    {
        if (DEBUG) {
            pg_trace(APP_LOG_DIR . PG_TRAGE_LOG . '_' . date('Ymd') . '.log', 'w');
        }
    }

    /**
     * トレースログ終了
     */
    public static function untrace()
    {
        if (DEBUG) {
            pg_untrace();
        }
    }
}
