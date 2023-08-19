<?php
namespace app\api;

require_once "config/app_config.php";
require_once "app/constant.php";
require_once "app/forms/ReportApiForm.php";
require_once "app/utils/DbUtil.php";
require_once "app/models/OptDeliverReports.php";
require_once "app/models/OptDeliverReportsHistory.php";

use app\forms\ReportApiForm;
use app\models\OptDeliverReports;
use app\models\OptDeliverReportsHistory;
use app\utils\DbUtil;
use app\utils\LogUtil;
use Exception;

/**
 * 配信ステータスAPI(アプリケーション)
 * Class OptImageReportApi
 * @package app\api
 */
class OptImageReportApi
{
    /** InitializeApiService */
    private $form;

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
    public function doApi($optDeliver)
    {
        // 開始ログ
        $start = microtime(true);
        LogUtil::init('AUTH_'.md5(uniqid()));
        LogUtil::debugLog("配信ステータスAPI(OPT) START");

        $this->form = new ReportApiForm();

        $resultBody = RESULT_BODY_NG;
        try
        {
            // DBコネクションの取得
            DbUtil::getConnect();
            DbUtil::trace();

            // リクエスト処理
            $this->form->validateRequest($optDeliver, IMAGE_TYPE_OPT);

            if($this->form->errorflag) {
                // 登録処理(オプション配信レポート)
                OptDeliverReports::save($this->form);

                // 登録処理(オプション配信履歴レポート)
                OptDeliverReportsHistory::save($this->form);

                $resultBody = RESULT_BODY_OK;
            }
        } catch (Exception $e) {
            LogUtil::applicationApiLog("配信ステータスAPI(OPT)でエラーが発生しました。 ".$e->getMessage(), "ERROR");
            LogUtil::errorLog($e);
            // ロールバック
            DbUtil::rollback();
        } finally {
            // DBコネクションクローズ
            DbUtil::untrace();
            DbUtil::closeConnect();

            // 終了ログ
            LogUtil::debugLog("配信ステータスAPI(OPT) END [TIME]".((microtime(true) - $start) * 1000)."(msec)");

            return $resultBody;
        }
    }
}