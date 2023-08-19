<?php
namespace app\services;

require_once "app/constant.php";
require_once "app/models/DownloadOrder.php";
require_once "app/models/MachineDownloadOrder.php";
require_once "app/models/CountryDownloadOrder.php";
require_once "app/utils/LogUtil.php";

use app\models\CountryDownloadOrder;
use app\models\DownloadOrder;
use app\models\Machine;
use app\models\MachineDownloadOrder;
use app\utils\LogUtil;

/**
 * 指示書URI取得APIサービス
 * Class DownloadOrderApiService
 * @package app\services
 */
class DownloadOrderApiService
{
    /** アプリケーションイメージの配信指示書情報 */
    private $appDownloadOrder = array();

    private $appSerials = array();

    /** オプションイメージの配信指示書情報 */
    private $optDownloadOrder = array();

    /** レスポンス文字列 */
    private $response;

    /**
     * アプリケーションイメージの配信指示書情報を取得しセットします
     * @param $paramSerial リクエストパラメータの基板クライアントID
     * @param $paramGameId リクエストパラメータのゲームタイトルID
     * @param $paramVer リクエストパラメータのゲームタイトルVer.
     * @throws \Exception
     */
    public function setAppDownloadOrder($paramSerial, $paramGameId, $paramVer)
    {
        /* 基板配信指示書 */
        // 基板配信指示書情報を取得
        $this->appDownloadOrder = MachineDownloadOrder::findAppDownloadOrderByClientId($paramSerial);

        // アプリケーションイメージの配信指示書情報が取得出来ない場合（基板情報なし）は処理終了
        if (count($this->appDownloadOrder) == 0)
        {
            LogUtil::debugLog("[アプリケーションイメージ]基板情報なし。 serial=".$paramSerial);
            return;
        }

        // 基板情報の通信セッティングが通信許可でない場合、終了
        if ($this->appDownloadOrder['setting'] != COMMUNICATION_SETTING_ALLOWED)
        {
            LogUtil::debugLog("[アプリケーションイメージ]基板情報の通信セッティングが通信許可でない。"
                ." serial=".$paramSerial
                ." setting=".$this->appDownloadOrder['setting']);
            return;
        }

        // 同一店舗に設置されている他の基板情報を取得
        $this->appSerials = Machine::findGroupSerials($this->appDownloadOrder['serial'],
                $this->appDownloadOrder['allnet_id'], $this->appDownloadOrder['group_index'], $paramGameId);

        // 基板配信指示書情報の配信指示書URIがある場合、基板配信指示書情報を使用する
        if (!empty($this->appDownloadOrder['uri']))
        {
            LogUtil::downloadOrderApiLog("Apply machine download order as a download order uri");
            return;
        }

        // 国別配信指示書URI取得
        $country = CountryDownloadOrder::findUriByCountry($paramGameId, $paramVer,
                        $this->appDownloadOrder['country_code'], DOWNLOAD_ORDER_IMAGE_TYPE_APP);

        if (count($country) > 0) {
            $this->appDownloadOrder['uri'] = $country['uri'];
            LogUtil::downloadOrderApiLog("Apply country download order as a download order uri");
            return;
        }

        // 配信指示書情報の配信指示書URIを取得
        $uri = DownloadOrder::findAppDownloadOrderByPk($paramGameId, $paramVer, $this->appDownloadOrder);
        if (empty($uri)) {
            // 配信指示書情報の配信指示書URIなし
            LogUtil::debugLog("[アプリケーションイメージ]配信指示書情報の配信指示書URIなし。"
                ." game_id=".$paramGameId
                ." game_ver=".$paramVer);
            return;
        }

        // 配信指示書のURIが取得できた場合、配信指示書情報のURIをセットする
        $this->appDownloadOrder['uri'] = $uri;
        LogUtil::downloadOrderApiLog("Apply basic download order as a download order uri");
    }

    /**
     * オプションイメージの配信指示書情報を取得しセットします
     * @param $paramSerial リクエストパラメータの基板クライアントID
     * @param $paramGameId リクエストパラメータのゲームタイトルID
     * @param $paramVer リクエストパラメータのゲームタイトルVer.
     * @throws \Exception
     */
    public function setOptDownloadOrder($paramSerial, $paramGameId, $paramVer)
    {
        // 基板配信指示書情報を取得
        $this->optDownloadOrder = MachineDownloadOrder::findOptDownloadOrderByClientId($paramSerial);

        // オプションイメージの配信指示書情報が取得出来ない場合（基板情報なし）は処理終了
        if (count($this->optDownloadOrder) == 0)
        {
            LogUtil::debugLog("[オプションイメージ]基板情報なし。 serial=".$paramSerial);
            return;
        }

        // 基板情報の通信セッティングが通信許可でない場合、終了
        if ($this->optDownloadOrder['setting'] != COMMUNICATION_SETTING_ALLOWED)
        {
            LogUtil::debugLog("[オプションイメージ]基板情報の通信セッティングが通信許可でない。"
                ." serial=".$paramSerial
                ." setting=".$this->optDownloadOrder['setting']);
            return;
        }

        // 基板配信指示書情報の配信指示書URIがある場合、基板配信指示書情報を使用する
        if (!empty($this->optDownloadOrder['uri']))
        {
            LogUtil::downloadOrderApiLog("Option machine download order as a download order uri");
            return;
        }

        // 国別配信指示書URI取得
        $country = CountryDownloadOrder::findUriByCountry($paramGameId, $paramVer,
            $this->optDownloadOrder['country_code'], DOWNLOAD_ORDER_IMAGE_TYPE_OPT);

        if (count($country) > 0) {
            $this->optDownloadOrder['uri'] = $country['uri'];
            LogUtil::downloadOrderApiLog("Option country download order as a download order uri");
            return;
        }

        // 配信指示書情報の配信指示書URIを取得
        $uri = DownloadOrder::findOptDownloadOrderByPk($paramGameId, $paramVer);
        if (empty($uri)) {
            // 配信指示書情報の配信指示書URIなし
            LogUtil::debugLog("[オプションイメージ]配信指示書情報の配信指示書URIなし。"
                ." game_id=".$paramGameId
                ." game_ver=".$paramVer);
            return;
        }

        // 配信指示書のURIが取得できた場合、配信指示書情報のURIをセットする
        $this->optDownloadOrder['uri'] = $uri;
        LogUtil::downloadOrderApiLog("Option basic download order as a download order uri");
    }

    /**
     * レスポンス文字列を生成します
     */
    public function createResponse()
    {
        $this->response = 'result='.$this->appDownloadOrder['setting'];

        if ($this->appDownloadOrder['setting'] == COMMUNICATION_SETTING_ALLOWED)
        {
            $data = '';
            foreach ($this->appSerials as $serial) {
                if(!empty($data)) {
                    $data = $data. ',';
                }
                $data = $data . $serial;
            }
            $this->response = $this->response. '&serial='.$data;
            $this->response = $this->response. '&uri='.$this->getUri();
        }
        LogUtil::debugLog("[レスポンス文字列生成]response=".$this->response);
    }

    /**
     * エラーレスポンス文字列を生成します
     */
    public function createErrorResponse()
    {
        $this->response = 'result=0'; // ステータスNGをセット
        LogUtil::debugLog("[エラーレスポンス文字列生成]response=".$this->response);
    }

    /**
     * 指示書URIを取得します
     * @return 指示書URI
     */
    private function getUri()
    {
        // アプリケーションイメージとオプションイメージの両方ない場合は、'null'の文字列を返却
        if (empty($this->appDownloadOrder['uri']) && empty($this->optDownloadOrder['uri']))
        {
            LogUtil::debugLog("[URI取得]アプリケーションイメージとオプションイメージの両方なし");
            return 'null';
        }

        // アプリケーションイメージとオプションイメージのURIを'|'でつないで返却する
        return
            (empty($this->appDownloadOrder['uri']) ? '' : $this->appDownloadOrder['uri'])
            .(empty($this->optDownloadOrder['uri']) ? '' : '|'.$this->optDownloadOrder['uri'])
            ;
    }

    /**
     * @return mixed
     */
    public function getAppDownloadOrder()
    {
        return $this->appDownloadOrder;
    }

    /**
     * @return mixed
     */
    public function getOptDownloadOrder()
    {
        return $this->optDownloadOrder;
    }

    /**
     * @return mixed
     */
    public function getResponse()
    {
        return $this->response;
    }
}