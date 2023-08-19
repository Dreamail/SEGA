<?php

namespace app\services;

require_once "app/constant.php";
require_once "app/services/AuthenticateNormalService.php";
require_once "app/services/AuthenticateAndMoveService.php";
require_once "app/services/AuthenticateAutoService.php";
require_once "app/models/GameAttribute.php";
require_once "app/models/Machine.php";
require_once "app/models/Routers.php";
require_once "app/models/MachineStatus.php";
require_once "app/models/UnregisteredMachine.php";
require_once "app/models/AuthLog.php";
require_once "app/utils/CommonUtil.php";
require_once "app/utils/LogUtil.php";

use app\forms\AbstractForm;
use app\forms\OperationApiForm;
use app\forms\StatusForm;
use app\models\Routers;
use app\services\AuthenticateNormalService;
use app\models\GameAttribute;
use app\models\Machine;
use app\models\MachineStatus;
use app\models\UnregisteredMachine;
use app\models\AuthLog;
use app\utils\CommonUtil;
use app\utils\LogUtil;
use Exception;

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

    /** 基板情報 */
    private $routers = array();

    /** レスポンス文字列 */
    private $response;

    private $debugInfo;
    private $placeDebugInfo;

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
     * @param $paramGameId リクエストパラメータのゲームタイトルID
     * @param $paramVer リクエストパラメータのゲームタイトルVer.
     * @param $parmSerial リクエストパラメータの基板クライアントID
     * @param $ip リクエストパラメータの店舗IP
     * @throws Exception
     */
    public function setActivateApiData($paramGameId, $paramVer, $parmSerial, $ip)
    {
        // ルータ情報取得
        $this->routers = Routers::findRouter($ip);

        // 基板情報取得
        $this->machine = Machine::findByMachineId($parmSerial);

        if (count($this->routers) != 0) {
            /* ゲーム属性情報取得 */
            $this->gameAttribute = GameAttribute::findByPk($paramGameId, $paramVer, $this->routers['country_code']);
        }
        // 基板情報のステータス有無をセット（基板情報が取得出来て基板ステータスの基板シリアルがある場合、true）
        if (count($this->machine) != 0) {
            $this->machine['exist_stat_serial'] = !empty($this->machine['stat_serial']);
            LogUtil::debugLog("基板ステータスの基板シリアル有無 exist_stat_serial=" . $this->machine['exist_stat_serial']);
        }
    }

    /**
     * 認証処理
     * @param $paramGameId リクエストパラメータのゲームタイトルID
     * @param $paramVer リクエストパラメータのゲームタイトルVer
     * @param $parmSerial リクエストパラメータのシリアル
     * @param $token リクエストパラメータのシリアル
     * @param $encode リクエストパラメータのシリアル
     */
    public function authenticate($paramGameId, $paramVer, $parmSerial, $token, $encode, $hops, $ip)
    {
        // すでにエラーの場合、処理しない
        if ($this->status['result'] != STAT_RESULT_SUCCESS) {
            LogUtil::debugLog("処理前にすでにエラー。 result=" . $this->status['result']);

            $this->createResponse($token,$encode, $paramGameId, $paramVer, $parmSerial, $hops, $ip);
            return;
        }

        /* ルータ情報なし */
        if (count($this->routers) == 0) {
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_LOC;
            $this->status['cause'] = STAT_CAUSE_FAIL_ROUTER;

            $this->createResponse($token,$encode, $paramGameId, $paramVer, $parmSerial, $hops, $ip);
            // 認証処理なし
            return;
        }

        /* ゲームタイトル情報なし */
        if (count($this->gameAttribute) == 0) {
            LogUtil::debugLog("[認証処理]ゲームタイトル情報なし");
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_GAME;
            $this->status['cause'] = STAT_CAUSE_FAIL_GAME;

            $this->createResponse($token,$encode, $paramGameId, $paramVer, $parmSerial, $hops, $ip);
            // 認証処理なし
            return;
        }

        $authenticateService = null;
        if(count($this->machine) != 0 && isset($this->machine['serial'])) {
            // 基板情報に基板シリアルが存在する
            if(!in_array($this->gameAttribute['auth'], GAME_AUTH_MOVE_ALLOWED) || $this->isSamePlace()) {
                if(isset($this->machine['allnet_id'])) {
                    /** 通常認証処理 */
                    $authenticateService = new AuthenticateNormalService($this->status, $this->gameAttribute, $this->machine, $this->routers);
                    $authenticateService->authenticateNormal($paramGameId);

                    // 認証後の情報をセット
                    $this->status = $authenticateService->getStatus();
                    $this->gameAttribute = $authenticateService->getGameAttribute();
                    $this->machine = $authenticateService->getMachine();

                } else {
                    LogUtil::debugLog("[認証処理]店舗情報に関するNG");
                    // ステータスをセット
                    $this->status['result'] = STAT_RESULT_FAIL_LOC;
                    $this->status['cause'] = STAT_CAUSE_FAIL_PLACE;
                }
                $this->createResponse($token,$encode, $paramGameId, $paramVer, $parmSerial, $hops, $ip);

            } else {
                /** 自動移設処理 */
                $authenticateService = new AuthenticateAndMoveService($this->status, $this->gameAttribute, $this->machine, $this->routers);
                $authenticateService->authenticateAndMove($paramGameId, $paramVer, $parmSerial);

                // 認証後の情報をセット
                $this->status = $authenticateService->getStatus();
                $this->gameAttribute = $authenticateService->getGameAttribute();
                $this->machine = $authenticateService->getMachine();

                $this->createResponseFromRouter($token,$encode, $paramGameId, $paramVer, $parmSerial, $hops, $ip);
            }
        } else {
            if(in_array($this->gameAttribute['auth'], GAME_AUTH_AUTO_ALLOWED)) {
                // ゲーム情報の認証方式が自動認証であるなら自動認証処理
                /** 自動認証処理 */
                $authenticateService = new AuthenticateAutoService($this->status, $this->gameAttribute, $this->machine, $this->routers);
                $authenticateService->authenticateAuto($paramGameId, $parmSerial);

                // 認証後の情報をセット
                $this->status = $authenticateService->getStatus();
                $this->gameAttribute = $authenticateService->getGameAttribute();
                $this->machine = $authenticateService->getMachine();

                $this->createResponseFromRouter($token,$encode, $paramGameId, $paramVer, $parmSerial, $hops, $ip);
            } else {
                LogUtil::debugLog("[認証処理]基板情報に関するNG");
                // ステータスをセット
                $this->status['result'] = STAT_RESULT_FAIL_SELIAL;
                $this->status['cause'] = STAT_CAUSE_FAIL_MACHINE;
                $this->createResponse($token,$encode, $paramGameId, $paramVer, $parmSerial, $hops, $ip);
            }
        }
    }

    /**
     * 基板ステータスの更新、未登録基板情報の登録・更新・削除を行います。
     * @param $paramGameId リクエストパラメータのゲームタイトルID
     * @param $paramVer リクエストパラメータのゲームタイトルVer.
     * @param $parmSerial リクエストパラメータの基板クライアントID
     * @param $userAgent ユーザエージェント
     * @param $paramFirmVer リクエストパラメータのファームウェアバージョン
     * @param $paramBootVer　リクエストパラメータのブートROMバージョン
     * @param $paramFormatVer　リクエストパラメータのフォーマットバージョン
     * @param $paramIp リクエストパラメータのIPアドレス
     * @param $paramHops リクエストパラメータのHops
     */
    public function updateMachineStatus($paramGameId, $paramVer, $parmSerial, $userAgent, $paramFirmVer, $paramBootVer, $paramFormatVer, $paramIp, $paramHops)
    {
        /* 基板情報なし */
        if (count($this->machine) != 0 && !empty($this->machine['serial'])) {
            // 基板ステータスの基板シリアルあり(基板ステータス更新)
            if (array_key_exists('stat_serial',$this->machine) && isset($this->machine['stat_serial'])) {
                /* 基板情報あり */
                LogUtil::debugLog(
                    "[基板ステータスの更新、未登録基板情報の登録・更新・削除]基板ステータスの基板シリアル有無。 stat_serial="
                    . $this->machine['stat_serial']);

                MachineStatus::updateByMachineId($this->status['result'] == STAT_RESULT_SUCCESS,
                    $parmSerial, $paramGameId, $paramVer, $userAgent, $paramFirmVer, $paramBootVer, $paramFormatVer, $paramIp, $paramHops);
            } // 基板ステータスの基板シリアルなし(基板ステータス登録)
            else {
                /* 基板情報あり */
                LogUtil::debugLog(
                    "[基板ステータスの更新、未登録基板情報の登録・更新・削除]基板ステータスの基板シリアル有無。 stat_serial=");

                MachineStatus::save($this->status['result'] == STAT_RESULT_SUCCESS,
                    $parmSerial, $paramGameId, $paramVer, $userAgent, $paramFirmVer, $paramBootVer, $paramFormatVer, $paramIp, $paramHops);
            }

            LogUtil::debugLog("[基板ステータスの更新、未登録基板情報の削除]基板情報あり。 serial=" . $this->machine['serial']);
            // 未登録基板情報の削除
            UnregisteredMachine::deleteByMachineId($parmSerial);
        } else {
            LogUtil::debugLog("[基板ステータスの更新、未登録基板情報の登録・更新・削除]基板情報なし。 serial=" . $parmSerial);
            UnregisteredMachine::mergeByMachineId($parmSerial, $paramGameId, $paramIp);
        }
    }

    /**
     * レスポンス文字列を生成します。
     */
    public function createResponse($token, $encode, $paramGameId, $paramVer, $parmSerial, $hops, $ip)
    {
        if ($this->status['result'] != STAT_RESULT_SUCCESS) {
            $this->response = 'stat=' . $this->status['result'] . "\r\n";
        } else {
            $this->response =
                'stat=' . $this->status['result'] .
                '&allnet_id=' . (empty($this->machine['allnet_id']) ? '' : $this->machine['allnet_id']) .
                '&place_id=' . (empty($this->machine['place_id']) ? '' : $this->machine['place_id']) .
                '&uri=' . ($this->status['result'] != STAT_RESULT_SUCCESS || empty($this->gameAttribute['uri']) ? '' : $this->gameAttribute['uri']) .
                '&host=' . ($this->status['result'] != STAT_RESULT_SUCCESS || empty($this->gameAttribute['host']) ? '' : $this->gameAttribute['host']) .
                '&name=' . (empty($this->machine['name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['name'], $encode, 'UTF-8')))) .
                '&nickname=' . (empty($this->machine['nickname']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['nickname'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['nickname'], $encode, 'UTF-8')))) .
                '&setting=' . (empty($this->machine['setting']) ? '' : $this->machine['setting']) .
                '&region0=' . (empty($this->machine['region0_id']) ? '0' : $this->machine['region0_id']) .
                '&region_name0=' . (empty($this->machine['r0_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['r0_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['r0_name'], $encode, 'UTF-8')))) .
                '&region_name1=' . (empty($this->machine['r1_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['r1_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['r1_name'], $encode, 'UTF-8')))) .
                '&region_name2=' . (empty($this->machine['r2_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['r2_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['r2_name'], $encode, 'UTF-8')))) .
                '&region_name3=' . (empty($this->machine['r3_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->machine['r3_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->machine['r3_name'], $encode, 'UTF-8')))) .
                '&country=' . (empty($this->machine['country_code']) ? '' : $this->machine['country_code']) .
                '&utc_time=' . CommonUtil::getUtcTime(RESPONSE_DATE_FORMAT, RESPONSE_TIME_FORMAT) .
                '&client_timezone=' . (empty($this->machine['timezone']) ? '' : $this->machine['timezone']) .
                '&res_ver=' . RES_VER_NU_FORMAT_VER .
                '&token=' . (empty($token) ? 'null' : $token) . "\r\n" ;
            LogUtil::debugLog("[レスポンス文字列生成]response=" . $this->response);
        }

        // 認証ログテーブルに格納するデバッグ文字列の生成
        $this->createDebugInfo($paramGameId, $paramVer, $parmSerial, $ip,
            (empty($this->machine['place_id']) ? '' : $this->machine['place_id']),
            (empty($this->machine['allnet_id']) ? '' : $this->machine['allnet_id']),
            (empty($this->routers['router_id']) ? '' : $this->routers['router_id']),
            (empty($this->machine['setting']) ? '' : $this->machine['setting']));

        $this->createPlaceDebugInfo($hops,
            (empty($this->machine['name']) ? '' : $this->machine['name']),
            (empty($this->machine['nickname']) ? '' : $this->machine['nickname']));
    }

    /**
     * レスポンス文字列を生成します。
     */
    public function createResponseFromRouter($token, $encode, $paramGameId, $paramVer, $parmSerial, $hops, $ip)
    {
        if ($this->status['result'] != STAT_RESULT_SUCCESS) {
            $this->response = 'stat=' . $this->status['result'] . "\r\n";
        } else {
            $this->response =
                'stat=' . $this->status['result'] .
                '&allnet_id=' . (empty($this->routers['allnet_id']) ? '' : $this->routers['allnet_id']) .
                '&place_id=' . (empty($this->routers['place_id']) ? '' : $this->routers['place_id']) .
                '&uri=' . ($this->status['result'] != STAT_RESULT_SUCCESS || empty($this->gameAttribute['uri']) ? '' : $this->gameAttribute['uri']) .
                '&host=' . ($this->status['result'] != STAT_RESULT_SUCCESS || empty($this->gameAttribute['host']) ? '' : $this->gameAttribute['host']) .
                '&name=' . (empty($this->routers['name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->routers['name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->routers['name'], $encode, 'UTF-8')))) .
                '&nickname=' . (empty($this->routers['nickname']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->routers['nickname'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->routers['nickname'], $encode, 'UTF-8')))) .
                '&setting=' . (empty($this->machine['setting']) ? '' : $this->machine['setting']) .
                '&region0=' . (empty($this->routers['region0_id']) ? '0' : $this->routers['region0_id']) .
                '&region_name0=' . (empty($this->routers['r0_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->routers['r0_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->routers['r0_name'], $encode, 'UTF-8')))) .
                '&region_name1=' . (empty($this->routers['r1_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->routers['r1_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->routers['r1_name'], $encode, 'UTF-8')))) .
                '&region_name2=' . (empty($this->routers['r2_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->routers['r2_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->routers['r2_name'], $encode, 'UTF-8')))) .
                '&region_name3=' . (empty($this->routers['r3_name']) ? '' : (empty($encode) ? urlencode(mb_convert_encoding($this->routers['r3_name'], 'EUC-JP', 'UTF-8')) : urlencode(mb_convert_encoding($this->routers['r3_name'], $encode, 'UTF-8')))) .
                '&country=' . (empty($this->routers['country_code']) ? '' : $this->routers['country_code']) .
                '&utc_time=' . CommonUtil::getUtcTime(RESPONSE_DATE_FORMAT, RESPONSE_TIME_FORMAT) .
                '&client_timezone=' . (empty($this->routers['timezone']) ? '' : $this->routers['timezone']) .
                '&res_ver=' . RES_VER_NU_FORMAT_VER .
                '&token=' . (empty($token) ? 'null' : $token) . "\r\n" ;
            LogUtil::debugLog("[レスポンス文字列生成]response=" . $this->response);
        }

        // 認証ログテーブルに格納するデバッグ文字列の生成
        $this->createDebugInfo($paramGameId, $paramVer, $parmSerial, $ip,
        (empty($this->routers['place_id']) ? '' : $this->routers['place_id']),
            (empty($this->routers['allnet_id']) ? '' : $this->routers['allnet_id']),
            (empty($this->routers['router_id']) ? '' : $this->routers['router_id']),
            (empty($this->machine['setting']) ? '' : $this->machine['setting']));

        $this->createPlaceDebugInfo($hops,
            (empty($this->routers['name']) ? '' : $this->routers['name']),
            (empty($this->routers['nickname']) ? '' : $this->routers['nickname']));
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
     * @param $paramGameId リクエストパラメータのゲームID
     * @param $paramVer リクエストパラメータのゲームVer
     * @param $serial リクエストパラメータの基板シリアル
     * @param $queryString 認証リクエスト文字列
     */
    public function putLog($paramGameId, $paramVer, $serial, $queryString, $globalIp, $ip)
    {
        // 認証ログの登録
        AuthLog::save($paramGameId, $paramVer, $serial, $globalIp, $this->status['result'], $this->status['cause'],
            empty($this->routers['place_id']) ? null : $this->routers['place_id'],
            empty($this->routers['country_code']) ? null : $this->routers['country_code'],
            empty($this->routers['allnet_id']) ? null : $this->routers['allnet_id'],
            empty($ip) ? null : $ip,
            $queryString, $this->response, $this->debugInfo, $this->placeDebugInfo);
    }

    /**
     * 認証ログテーブルに格納するデバッグ文字列を生成します。
     * @param $gameId ゲームタイトルID
     * @param $ver ゲームタイトルVer.
     * @param $machineId 基板クライアントID
     * @return 認証ログテーブルに格納するデバッグ文字列
     */
    private function createDebugInfo($gameId, $ver, $machineId, $placeIp, $placeId, $allnetId, $routerId, $setting)
    {
        $this->debugInfo = 'game_id=' . $gameId
                            . ',ver=' . $ver
                            . ',serial=' . $machineId
                            . ',ip=' . $placeIp
                            . ',place_id=' . $placeId
                            . ',allnet_id=' . $allnetId
                            . ',router_no=' . $routerId
                            . ',stat=' . $this->status['result']
                            . ',setting=' . $setting;
    }

    /**
     * 認証ログテーブルに格納する店舗情報デバッグ文字列を生成します。
     * @return 認証ログテーブルに格納する店舗情報デバッグ文字列
     */
    private function createPlaceDebugInfo($hops, $name, $nickname)
    {
        $this->placeDebugInfo = (empty($hops) ? '' : 'hops=' . $hops)
                                   . '['. $name. ',' . $nickname . ']';
    }

    /**
     * @return array
     */
    public function getStatus()
    {
        return $this->status;
    }

    /**
     * @param $result
     * @param $cause
     */
    public function setStatus($result, $cause)
    {
        $this->status['result'] = $result;
        $this->status['cause'] = $cause;
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

    /**
     * ルータ情報から紐づく店舗と基板情報の店舗は同じか
     *
     * @return
     */
    public function isSamePlace() {
        if (!isset($this->router['router_id']) || !isset($this->router['allnet_id'])) {
            if (!isset($this->machine['allnet_id'])) {
                return true;
            }
            return false;
        }
        if ($this->router['router_id'] === $this->machine['allnet_id']) {
            return true;
        }
        return false;
    }
}