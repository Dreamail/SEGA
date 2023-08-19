<?php
namespace app\api;

require_once "config/app_config.php";
require_once "app/constant.php";
require_once "app/forms/ReportApiForm.php";
require_once "app/utils/DbUtil.php";
require_once "app/models/AppDeliverReports.php";
require_once "app/models/AppDeliverReportsHistory.php";

use app\forms\ReportApiForm;
use app\models\AppDeliverReports;
use app\models\AppDeliverReportsHistory;
use app\utils\DbUtil;
use app\utils\LogUtil;
use Exception;

/**
 * 配信ステータスAPI(アプリケーション)
 * Class OptImageReportApi
 * @package app\api
 */
class AppImageReportApi
{
    /** リクエストフォーム */
    protected $form;

    /**
     * OptImageReportApi constructor.
     */
    public function __construct()
    {
        date_default_timezone_set(TIMEZONE);
    }

    /**
     * 配信ステータスAPI
     */
    public function doApi($appDeliver)
    {
        // 開始ログ
        $start = microtime(true);
        LogUtil::init('AUTH_'.md5(uniqid()));
        LogUtil::debugLog("配信ステータスAPI(APP) START");

        $this->form = new ReportApiForm();

        $resultBody = RESULT_BODY_NG;
        try
        {
            // DBコネクションの取得
            DbUtil::getConnect();
            DbUtil::trace();

            // リクエスト処理
            $this->form->validateRequest($appDeliver, IMAGE_TYPE_APP);

            if($this->form->errorflag) {
                // 登録処理(アプリ配信レポート)
                AppDeliverReports::save($this->form);

                // 登録処理(アプリ配信レポート履歴)
                AppDeliverReportsHistory::save($this->form);
                $resultBody = RESULT_BODY_OK;
            }
        } catch (Exception $e) {
            LogUtil::applicationApiLog("配信ステータスAPI(APP)でエラーが発生しました。 ".$e->getMessage(), "ERROR");
            LogUtil::errorLog($e);
            // ロールバック
            DbUtil::rollback();
        } finally {
            // DBコネクションクローズ
            DbUtil::untrace();
            DbUtil::closeConnect();

            // 終了ログ
            LogUtil::debugLog("配信ステータスAPI(APP) END [TIME]".((microtime(true) - $start) * 1000)."(msec)");

            return $resultBody;
        }
    }
}