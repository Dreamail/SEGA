<?php

namespace app\api;

require_once "app/api/AbstractApi.php";
require_once "app/forms/OperationApiForm.php";
require_once "app/services/OperationApiService.php";

use app\api\AbstractApi;
use app\forms\OperationApiForm;
use app\services\OperationApiService;
use app\utils\LogUtil;
use app\utils\CipherUtil;
use app\utils\DbUtil;
use Exception;

/**
 * 基板稼働認可API
 * Class operationApi
 * @package app\api
 */
class OperationApi extends AbstractApi
{
    /** OperationApiService */
    /**
     * @var OperationApiService
     */
    private OperationApiService $operationApiService;

    /**
     * initializeApi constructor.
     */
    public function __construct()
    {
        parent::__construct();
        $this->operationApiService = new OperationApiService();
    }

    /**
     * 基板認証API
     */
    public function doApi()
    {
        // 開始ログ
        $start = microtime(true);
        LogUtil::init('AUTH_' . md5(uniqid()));
        LogUtil::debugLog("基板認証API START");

        $this->form = new OperationApiForm();
        try {
            // DBコネクションの取得
            DbUtil::getConnect();
            DbUtil::trace();

            // リクエスト処理
            $this->form->init($_SERVER['REQUEST_METHOD'], $_SERVER['HTTP_USER_AGENT'],
                file_get_contents('php://input'),$this->getClientIp());
            $this->form->validateRequest();

            // 認証処理実行
            $this->doAuth();
            //レスポンス
            http_response_code(200);

        } catch (Exception $e) {
            LogUtil::activateApiLog("基板認証APIでエラーが発生しました。 " . $e->getMessage(), "ERROR");
            LogUtil::errorLog($e);
            // ロールバック
            DbUtil::rollback();

            // レスポンス
            $this->response = $this->getErrorResponse();
            http_response_code(403);
        } finally {
            // DBコネクションクローズ
            DbUtil::untrace();
            DbUtil::closeConnect();

            // レスポンス
            header('Content-Type: text/html; charset=' . $this->form->getEncode());
            echo $this->getResponse();

            // 終了ログ
            LogUtil::debugLog("基板認証API END [TIME]" . (round((microtime(true) - $start) * 1000, 3)) . "(msec)");
        }
    }

    /**
     * クライアントIPアドレスを取得
     * @return mixed
     */
    private function getClientIp()
    {
        $globalIp = null;
        if(isset($_SERVER["HTTP_X_FORWARDED_FOR"])) {
            $globalIp = $_SERVER['HTTP_X_FORWARDED_FOR'];
        } else if(empty($globalIp)) {
            $globalIp = $_SERVER['REMOTE_ADDR'];
        }
        return $globalIp;
    }

    /**
     * 認証実行
     * @throws Exception
     */
    public function doAuth()
    {
        LogUtil::activateApiLog("Start authentication process [" . $this->form->getQueryString() . "]");

        /** DBから認証に使用するデータを取得 */
        $this->operationApiService->setActivateApiData(
            $this->form->getTitleId(), $this->form->getTitleVer(), $this->form->getMachineId());

        /* 認証処理 */
        $this->operationApiService->authenticate($this->form->getTitleId(), $this->form->getTitleVer());

        /* 基板ステータス情報更新 */
        $this->operationApiService->updateMachineStatus(
            $this->form->getTitleId(), $this->form->getTitleVer(), $this->form->getMachineId(), $this->form->getUserAgent(), $this->form->getFirmVer(), $this->form->getBootVer(), $this->form->getFormatVer());

        /* レスポンス文字列生成 */
        $this->operationApiService->createResponse($this->form->getToken(), $this->form->getEncode());
        $this->response = $this->operationApiService->getResponse();

        /* 認証ログ書き込み（DB登録） */
        $this->operationApiService->putLog($this->form->getTitleId(), $this->form->getTitleVer(),
            $this->form->getMachineId(), $this->form->getQueryString(), $this->form->getGlobalIp(), $this->form->getEncode());

        if ($this->operationApiService->getStatus()['result'] == STAT_RESULT_SUCCESS) {
            LogUtil::activateApiLog("Authentication was successful");
        } else {
            // 認証失敗はwarnログ
            LogUtil::activateApiLog("Authentication was failed: " . $this->operationApiService->getStatus()['result'], 'WARN');
        }
    }

    /**
     * @return mixed
     */
    public function getResponse()
    {
        return $this->response;
    }

    /**
     * @return mixed
     */
    public function getErrorResponse()
    {
        $this->operationApiService->createErrorResponse(
            empty($this->form) ? null : $this->form->getToken(),
            empty($this->form) ? null : $this->form->getEncode());
        return $this->operationApiService->getResponse();
    }
}