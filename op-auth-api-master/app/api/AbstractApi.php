<?php

namespace app\api;

require_once "config/app_config.php";
require_once "app/constant.php";
require_once "app/utils/LogUtil.php";
require_once "app/utils/CipherUtil.php";
require_once "app/utils/DbUtil.php";

/**
 * Class AbstractApi
 * @package app\api
 */
abstract class AbstractApi
{
    /**
     * リクエストフォーム
     */
    protected $form;
    /**
     * レスポンス文字列
     */
    protected $response;

    /**
     * AbstractApi constructor.
     */
    public function __construct()
    {
        date_default_timezone_set(TIMEZONE);
    }

    /**
     * API実行
     * @return mixed
     */
    protected abstract function doApi();

    /**
     * レスポンス文字列取得
     * @return mixed
     */
    protected abstract function getResponse();

    /**
     * エラー時のレスポンス文字列取得
     * @return mixed
     */
    protected abstract function getErrorResponse();
}