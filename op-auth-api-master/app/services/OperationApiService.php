<?php

namespace app\services;

require_once "app/constant.php";
require_once "app/services/AuthenticateService.php";
require_once "app/models/GameAttribute.php";
require_once "app/models/Machine.php";
require_once "app/models/MachineStatus.php";
require_once "app/models/UnregisteredMachine.php";
require_once "app/models/AuthLog.php";
require_once "app/utils/CommonUtil.php";
require_once "app/utils/LogUtil.php";

use app\forms\AbstractForm;
use app\forms\OperationApiForm;
use app\services\AuthenticateService;
use app\models\GameAttribute;
use app\models\Machine;
use app\models\MachineStatus;
use app\models\UnregisteredMachine;
use app\models\AuthLog;
use app\utils\CommonUtil;
use app\utils\LogUtil;

/**
 * 基板認証APIサービス
 * Class InitializeApiService
 * @package app\services
 */
class OperationApiService
{
    /** ステータス */
    private $status = array();

    /** ゲーム属性情報 */
    private $gameAttribute = array();

    /** 基板情報 */
    private $machine = array();

    /** レスポンス文字列 */
    private $response;

    /**
     * InitializeApiService constructor.
     */
    public function __construct()
    {
        // ステータスに初期値をセット
        $this->status = array(
            'result' => STAT_RESULT_SUCCESS,
            'cause' => STAT_CAUSE_SUCCESS,
        );
    }

    /**
     * DBから認証に使用するデータを取得します。
     * @param $titleId リクエストパラメータのゲームタイトルID
     * @param $titleVer リクエストパラメータのゲームタイトルVer.
     * @param $clientId リクエストパラメータの基板クライアントID
     */
    public function setActivateApiData($paramTitleId, $paramTitleVer, $paramMachineId)
    {
        /* 基板情報取得 */
        $this->machine = Machine::findByMachineId($paramMachineId);

        // 基板情報のステータス有無をセット（基板情報が取得出来て基板ステータスの基板シリアルがある場合、true）
        if (count($this->machine) != 0) {
            /* ゲーム属性情報取得 */
            $this->gameAttribute = GameAttribute::findByPk($paramTitleId, $paramTitleVer,$this->machine['country_code']);

            $this->machine['exist_stat_serial'] = !empty($this->machine['stat_serial']);
            LogUtil::debugLog("基板ステータスの基板シリアル有無 exist_stat_serial=" . $this->machine['exist_stat_serial']);
        }
    }

    /**
     * 認証処理
     * @param $paramTitleId リクエストパラメータのゲームタイトルID
     * @param $paramTitleVer リクエストパラメータのゲームタイトルVer
     */
    public function authenticate($paramTitleId, $paramTitleVer)
    {
        /* 基板情報なし */
        if (count($this->machine) == 0) {
            LogUtil::debugLog("[認証処理]基板情報なし");
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_MACHINE;
            $this->status['cause'] = STAT_CAUSE_FAIL_MACHINE;
            // 認証処理なし
            return;
        }

        /* 基板情報の店舗IPなし */
        if (!isset($this->machine['place_ip'])) {
            LogUtil::debugLog("[認証処理]ルータ情報の店舗IPなし");
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_LOC;
            $this->status['cause'] = STAT_CAUSE_FAIL_PLACE_IP;
            // 認証処理なし
            return;
        }

        /* 基板情報のALL.NetIDがない */
        if (empty($this->machine['allnet_id'])) {
            LogUtil::debugLog("[認証処理]基板情報のALL.NetIDなし。 allnet_id=" . $this->machine['allnet_id']);
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_LOC;
            $this->status['cause'] = STAT_CAUSE_FAIL_PLACE;
            // 認証処理なし
            return;
        }

        /* ゲームタイトル情報なし */
        if (count($this->gameAttribute) == 0) {
            LogUtil::debugLog("[認証処理]ゲームタイトル情報なし");
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_GAME;
            $this->status['cause'] = STAT_CAUSE_FAIL_GAME;
            // 認証処理なし
            return;
        }

        /** 通常認証処理 */
        $authenticateService = new AuthenticateService($this->status, $this->gameAttribute, $this->machine);
        $authenticateService->authenticateNormal($paramTitleId);

        // 認証後の情報をセット
        $this->status = $authenticateService->getStatus();
        $this->gameAttribute = $authenticateService->getGameAttribute();
        $this->machine = $authenticateService->getMachine();
    }

    /**
     * 基板ステータスの更新、未登録基板情報の登録・更新・削除を行います。
     * @param $paramTitleId リクエストパラメータのゲームタイトルID
     * @param $paramTitleVer リクエストパラメータのゲームタイトルVer.
     * @param $paramMachineId リクエストパラメータの基板クライアントID
     * @param $userAgent ユーザエージェント
     * @param $paramFirmVer リクエストパラメータのファームウェアバージョン
     * @param $paramBootVer　リクエストパラメータのブートROMバージョン
     * @param $paramFormatVer　リクエストパラメータのフォーマットバージョン
     */
    public function updateMachineStatus($paramTitleId, $paramTitleVer, $paramMachineId, $userAgent, $paramFirmVer, $paramBootVer, $paramFormatVer)
    {
        /* 基板情報なし */
        if (count($this->machine) == 0) {
            LogUtil::debugLog("[基板ステータスの更新、未登録基板情報の登録・更新・削除]基板情報なし。 paramMachineId=" . $paramMachineId);
            // リクエストパラメータの基板シリアルがある場合は未登録基板情報の登録・更新を行う
            if (!empty($paramMachineId)) {
                UnregisteredMachine::mergeByMachineId($paramMachineId, $paramTitleId);
            }
            return;
        }

        /* 基板情報あり */

        LogUtil::debugLog(
            "[基板ステータスの更新、未登録基板情報の登録・更新・削除]基板ステータスの基板シリアル有無。 exist_stat_serial="
            . $this->machine['exist_stat_serial']);
        // 基板ステータスの基板シリアルあり(基板ステータス更新)
        if ($this->machine['exist_stat_serial']) {
            MachineStatus::updateByMachineId($this->status['result'] == STAT_RESULT_SUCCESS,
                $paramMachineId, $paramTitleId, $paramTitleVer, $userAgent, $paramFirmVer, $paramBootVer, $paramFormatVer, $this->machine['place_ip']);
        } // 基板ステータスの基板シリアルなし(基板ステータス登録)
        else {
            MachineStatus::save($this->status['result'] == STAT_RESULT_SUCCESS,
                $paramMachineId, $paramTitleId, $paramTitleVer, $userAgent, $paramFirmVer, $paramBootVer, $paramFormatVer, $this->machine['place_ip']);
        }

        // 未登録基板情報の削除
        UnregisteredMachine::deleteByMachineId($paramMachineId);
    }

    /**
     * レスポンス文字列を生成します。
     */
    public function createResponse($token, $encode)
    {
        if ($this->status['result'] != 1) {
            $this->response = 'result=' . $this->status['result'];
        } else {
            $this->response =
                'result=' . $this->status['result'] .
                '&allnet_id=' . (empty($this->machine['allnet_id']) ? '' : $this->machine['allnet_id']) .
                '&place_id=' . (empty($this->machine['place_id']) ? '' : $this->machine['place_id']) .
                '&title_uri=' . ($this->status['result'] != STAT_RESULT_SUCCESS || empty($this->gameAttribute['uri']) ? '' : $this->gameAttribute['uri']) .
                '&title_host=' . ($this->status['result'] != STAT_RESULT_SUCCESS || empty($this->gameAttribute['host']) ? '' : $this->gameAttribute['host']) .
                '&name=' . (empty($this->machine['name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['name'], $encode, 'UTF-8')))) .
                '&nickname=' . (empty($this->machine['nickname']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['nickname'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['nickname'], $encode, 'UTF-8')))) .
                '&setting=' . (empty($this->machine['setting']) ? '' : $this->machine['setting']) .
                '&region0=' . (empty($this->machine['region0_id']) ? '' : $this->machine['region0_id']) .
                '&region_name0=' . (empty($this->machine['r0_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['r0_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['r0_name'], $encode, 'UTF-8')))) .
                '&region_name1=' . (empty($this->machine['r1_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['r1_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['r1_name'], $encode, 'UTF-8')))) .
                '&region_name2=' . (empty($this->machine['r2_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['r2_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['r2_name'], $encode, 'UTF-8')))) .
                '&region_name3=' . (empty($this->machine['r3_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['r3_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['r3_name'], $encode, 'UTF-8')))) .
                '&country=' . (empty($this->machine['country_code']) ? '' : $this->machine['country_code']) .
                '&utc_time=' . CommonUtil::getUtcTime(RESPONSE_DATE_FORMAT, RESPONSE_TIME_FORMAT) .
                '&client_timezone=' . (empty($this->machine['timezone']) ? '' : $this->machine['timezone']) .
                '&res_ver=' . RES_VER_NU_FORMAT_VER .
                '&token=' . (empty($token) ? 'null' : $token) .
                '&location_ip=' . (empty($this->machine['place_ip']) ? '' : $this->machine['place_ip']);
            LogUtil::debugLog("[レスポンス文字列生成]response=" . $this->response);
        }
    }

    /**
     * 例外発生時のレスポンスを生成します
     * @param $token トークン
     */
    public function createErrorResponse($token, $encode)
    {
        $this->status['result'] = STAT_RESULT_FAIL_LOC;
        $this->gameAttribute = array();
        $this->machine = array();

        $this->createResponse($token, $encode);
        LogUtil::debugLog("[エラーレスポンス文字列生成]response=" . $this->response);
    }

    /**
     * 認証ログの登録を行います。
     * @param $paramTitleId リクエストパラメータのゲームID
     * @param $paramTitleVer リクエストパラメータのゲームVer
     * @param $paramMachineId リクエストパラメータの基板シリアル
     * @param $queryString 認証リクエスト文字列
     */
    public function putLog($paramTitleId, $paramTitleVer, $paramMachineId, $queryString, $globalIp)
    {
        // 認証ログテーブルに格納するデバッグ文字列の生成
        $debugInfo = $this->createDebugInfo($paramTitleId, $paramTitleVer, $paramMachineId);
        $placeDebugInfo = $this->createPlaceDebugInfo();

        // 認証ログの登録
        AuthLog::save($paramTitleId, $paramTitleVer, $paramMachineId, $globalIp, $this->status['result'], $this->status['cause'],
            empty($this->machine['place_id']) ? null : $this->machine['place_id'],
            empty($this->machine['country_code']) ? null : $this->machine['country_code'],
            empty($this->machine['allnet_id']) ? null : $this->machine['allnet_id'],
            empty($this->machine['place_ip']) ? null : $this->machine['place_ip'],
            $queryString, $this->response, $debugInfo, $placeDebugInfo);
    }

    /**
     * 認証ログテーブルに格納するデバッグ文字列を生成します。
     * @param $titleId ゲームタイトルID
     * @param $titleVer ゲームタイトルVer.
     * @param $machineId 基板クライアントID
     * @return 認証ログテーブルに格納するデバッグ文字列
     */
    private function createDebugInfo($titleId, $titleVer, $machineId)
    {
        return
            'game_id=' . $titleId
            . ',ver=' . $titleVer
            . ',serial=' . $machineId
            . ',ip=' . $this->machine['place_ip']
            . ',place_id=' . (empty($this->machine['place_id']) ? '' : $this->machine['place_id'])
            . ',allnet_id=' . $this->machine['allnet_id']
            . ',router_no=' . $this->machine['router_id']
            . ',stat=' . $this->status['result']
            . ',setting=' . (empty($this->machine['setting']) ? '' : $this->machine['setting']);
    }

    /**
     * 認証ログテーブルに格納する店舗情報デバッグ文字列を生成します。
     * @return 認証ログテーブルに格納する店舗情報デバッグ文字列
     */
    private function createPlaceDebugInfo()
    {
        return '['
            . HOPS
            . ','
            . (empty($this->machine['name']) ? '' : $this->machine['name'])
            . ','
            . (empty($this->machine['nickname']) ? '' : $this->machine['nickname'])
            . ']';
    }

    /**
     * @return array
     */
    public function getStatus()
    {
        return $this->status;
    }

    /**
     * @return array
     */
    public function getGameAttribute()
    {
        return $this->gameAttribute;
    }

    /**
     * @return array
     */
    public function getMachine()
    {
        return $this->machine;
    }

    /**
     * @return mixed
     */
    public function getResponse()
    {
        return $this->response;
    }

    /**
     * 10進を16進に変換
     * @param $placeId
     * @return string
     */
    private function placeIdDechex($placeId)
    {
        return str_pad(dechex($placeId), 4, 0, STR_PAD_LEFT);
    }
}