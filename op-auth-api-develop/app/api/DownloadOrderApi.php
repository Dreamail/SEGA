<?php
namespace app\api;

require_once "app/api/AbstractApi.php";
require_once "app/forms/DownloadOrderApiForm.php";
require_once "app/services/DownloadOrderApiService.php";
require_once "app/utils/SignatureVerify.php";

use app\api\AbstractApi;
use app\forms\DownloadOrderApiForm;
use app\services\DownloadOrderApiService;
use app\utils\LogUtil;
use app\utils\CipherUtil;
use app\utils\DbUtil;
use app\utils\SignatureVerify;
use Exception;

/**
 * 指示書URI取得API
 * Class DownloadOrderApi
 * @package app\api
 */
class DownloadOrderApi extends AbstractApi
{
    /** InitializeApiService */
    private $downloadOrderApiService;

    /**
     * @var SignatureVerify
     */
    private SignatureVerify $signatureVerify;

    /**
     * DownloadOrderApi constructor.
     */
    public function __construct()
    {
        parent::__construct();
        $this->downloadOrderApiService = new DownloadOrderApiService();
        $this->signatureVerify = new SignatureVerify();
    }

    /**
     * 指示書URI取得API
     */
    public function doApi()
    {
        // 開始ログ
        $start = microtime(true);
        LogUtil::init('AUTH_'.md5(uniqid()));
        LogUtil::debugLog("指示書URI取得API START");

        $this->form = new DownloadOrderApiForm();
        try
        {
            // DBコネクションの取得
            DbUtil::getConnect();
            DbUtil::trace();

            // リクエスト処理
            $this->form->init($_SERVER['REQUEST_METHOD'], $_SERVER['HTTP_USER_AGENT'],
                file_get_contents('php://input'), $_SERVER['REMOTE_ADDR']);
            $this->form->validateRequest();

            // 指示書URI取得実行
            $this->doDownloadOrder();

        } catch (Exception $e) {
            LogUtil::downloadOrderApiLog("指示書URI取得APIでエラーが発生しました。 ".$e->getMessage(), "ERROR");
            LogUtil::errorLog($e);
            // ロールバック
            DbUtil::rollback();

            // レスポンス
            $this->response = $this->getErrorResponse();
        } finally {
            // DBコネクションクローズ
            DbUtil::untrace();
            DbUtil::closeConnect();

            // レスポンス
            header('Content-Type: text/html; charset=utf-8');

            // レスポンス
            header('Content-Type: text/html; charset=' . $this->form->getEncode());
            header('Pragma:' . STR_ENCODED);

            if(empty($this->response)) {
                // レスポンス
                $this->response = $this->getErrorResponse();
            }

            echo base64_encode(zlib_encode($this->response, ZLIB_ENCODING_DEFLATE))."\r\n";

            // 終了ログ
            LogUtil::debugLog("指示書URI取得API END [TIME]".(round((microtime(true) - $start) * 1000, 3))."(msec)");
        }
    }

    /**
     * 配信指示書URI取得実行
     * @throws Exception
     */
    public function doDownloadOrder()
    {
        LogUtil::downloadOrderApiLog("Start finding download order process ["
            .$this->form->getQueryString()."]");

        if(!$this->signatureVerify->verify($this->form->getAuthdata(), $this->form->getSerial())) {
            LogUtil::downloadOrderApiLog("署名検証NG");
            return;
        }

        /* アプリケーションイメージの配信指示書情報を取得 */
        $this->downloadOrderApiService->setAppDownloadOrder(
            $this->form->getSerial(), $this->form->getGameId(), $this->form->getVer());

        /* オプションイメージの配信指示書情報を取得 */
        $this->downloadOrderApiService->setOptDownloadOrder(
            $this->form->getSerial(), $this->form->getGameId(), $this->form->getVer());

        /* レスポンスの作成 */
        $this->downloadOrderApiService->createResponse();
        $this->response = $this->downloadOrderApiService->getResponse();

        /* ログ出力 */
        LogUtil::downloadOrderApiLog("Finding download order was successful [".$this->response."]");
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
        $this->downloadOrderApiService->createErrorResponse();
        return $this->downloadOrderApiService->getResponse();
    }
}