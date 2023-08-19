<?php

namespace app\services;

require_once "app/constant.php";
require_once "app/utils/CommonUtil.php";
require_once "app/utils/LogUtil.php";
require_once "app/models/Machine.php";
require_once "app/models/MoveDenied.php";
require_once "app/models/AuthAllowedComps.php";
require_once "app/models/AuthDenied.php";

use app\models\AuthAllowedComps;
use app\models\AuthDenied;
use app\models\Machine;
use app\models\MoveDenied;
use app\utils\CommonUtil;
use app\utils\LogUtil;

/**
 * 自動移設認証サービス
 * Class AuthenticateAndMoveService
 * @package app\services
 */
class AuthenticateAndMoveService
{
    /* ステータス */
    private $status = array();

    /* ゲーム属性情報 */
    private $gameAttribute = array();

    /* ルータ情報 */
    private $routers = array();

    /* 基板情報 */
    private $machine = array();

    /**
     * AuthenticateNormalService constructor.
     */
    public function __construct($status, $gameAttribute, $machine, $routers)
    {
        $this->status = $status;
        $this->gameAttribute = $gameAttribute;
        $this->machine = $machine;
        $this->routers = $routers;
    }

    /**
     * 自動移設処理
     * @param $paramGameId リクエストパラメータのゲームタイトルID
     * @param $paramVer リクエストパラメータのゲームVer
     * @param $parmSerial リクエストパラメータのシリアル
     * @throws \Exception
     */
    public function authenticateAndMove($paramGameId, $paramVer, $parmSerial)
    {
        // すでにエラーの場合、処理しない
        if ($this->status['result'] != STAT_RESULT_SUCCESS) {
            LogUtil::debugLog("[自動移設]処理前にすでにエラー。 result=" . $this->status['result']);
            return;
        }

        // 基板情報の通信セッティングが通信許可でない場合、エラーステータスをセットして処理終了
        if ($this->machine['setting'] != COMMUNICATION_SETTING_ALLOWED) {
            $this->status['result'] = STAT_RESULT_FAIL_SELIAL;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 8);
            LogUtil::debugLog("[自動移設]基板情報の通信セッティングが通信許可でない。 setting=" . $this->machine['setting']);
            return;
        }

        // 基板に紐づく店舗情報なし
        if(empty($this->machine['allnet_id'])) {
            $this->status['result'] = STAT_RESULT_FAIL_LOC;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 13);
            LogUtil::debugLog("[自動移設]基板情報に紐づく店舗情報がない。 keychip=" . $this->machine['serial']);
            return;
        }

        $routerBillCode = $this->routers['bill_code'];
        $machineBillCode = $this->machine['bill_code'];

        if(empty($routerBillCode) || strlen($routerBillCode) != BILL_CODE_LENGTH) {
            $this->status['result'] = STAT_RESULT_FAIL_LOC;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 14);
            LogUtil::debugLog("[自動移設]ルータに紐づく請求先コードが3桁ではない。 routerBillCode=" . $routerBillCode);
            return;
        }

        if(empty($machineBillCode) || strlen($machineBillCode) != BILL_CODE_LENGTH) {
            $this->status['result'] = STAT_RESULT_FAIL_LOC;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 15);
            LogUtil::debugLog("[自動移設]基板に紐づく請求先コードが3桁ではない。 machineBillCode=" . $machineBillCode);
            return;
        }

        // 請求先コードから包括先コードを取得
        $compCode = substr($routerBillCode, 0, COMP_CODE_LENGTH);
        if($compCode !== substr($machineBillCode, 0, COMP_CODE_LENGTH)) {
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_LOC;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 16);
            LogUtil::debugLog("[自動移設]ルータに紐づく包括先コードと基板に紐づく包括先コードが同じではない。 machineBillCode=" .
                $machineBillCode . " routerBillCode=". $routerBillCode);
            return;
        }

        // 自動認証チェック
        $authDenied = AuthDenied::checkAuthDenied($paramGameId, $compCode, $routerBillCode);
        // 自動移設チェック
        $moveDenied = MoveDenied::checkMoveDenied($paramGameId, $paramVer, $compCode, $routerBillCode, $machineBillCode);

        if(AUTH_TYPE_COMP_AUTO_MOVE == $this->gameAttribute['auth']) {
            $authAllowedComp = AuthAllowedComps::findAuthAllowedComp($paramGameId, $compCode);
        }

        if(AUTH_TYPE_COMP_AUTO_MOVE == $this->gameAttribute['auth'] && empty($authAllowedComp)) {
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_LOC;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 12);
            LogUtil::debugLog("[自動移設]包括先自動認証が許可されていない。 gameId=" . $paramGameId . " compCode=". $compCode);
            return;
        }

        if($authDenied) {
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_LOC;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 18);
            LogUtil::debugLog("[自動移設]自動認証が不許可。 gameId=" . $paramGameId
                . " compCode=". $compCode . " routerBillCode=" . $routerBillCode);
            return;
        }

        if($moveDenied) {
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_LOC;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 17);
            LogUtil::debugLog("[自動移設]自動認証が不許可。 gameId=" . $paramGameId
                . " compCode=". $compCode . " routerBillCode=" . $routerBillCode . " machineBillCode=" . $machineBillCode);
            return;
        }

        // リクエストのゲームIDが基板情報の予約ゲームIDと一致する場合は基板情報を更新
        $exchange = $this->exchangeGameId($paramGameId);
        if ($this->status['result'] === STAT_RESULT_FAIL_GAME) {
            return;
        }

        /* 認証方式をログ出力 */
        LogUtil::operationApiLog("Authentication type: AUTO MOVE");
        if($exchange) {
            LogUtil::operationApiLog("GameId exchanged [gameId:".
            $this->machine['game_id'] .", reservedGameId:". $this->machine['reserved_game_id'] ."]");
        }

        $this->machine['serial'] = $parmSerial;
        $this->machine['allnet_id'] = $this->routers['allnet_id'];
        $this->machine['game_id'] = $this->gameAttribute['game_id'];

        Machine::updateMachine($this->machine, $this->routers);
    }

    /**
     * リクエストのゲームIDが基板情報の予約ゲームIDと一致する場合は基板情報を更新
     * @param $gameId
     * @return
     */
    public function exchangeGameId($gameId)
    {
        if ($gameId === $this->machine['game_id']) {
            return false;
        }

        if ($gameId !== $this->machine['reserved_game_id']) {
            $this->status['result'] = STAT_RESULT_FAIL_GAME;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 7);
            return false;
        }

        // リクエストのゲームIDが基板情報の予約ゲームIDと一致するならゲームIDと交換する
        $machines = new Machine();
        $machines::exchangeMachineGameId($this->machine);

        // 取得済みGameを交換
        $tmp['game_id'] = $this->machine['game_id'];
        $this->machine['game_id'] = $this->machine['reserved_game_id'];
        $this->machine['reserved_game_id'] = $tmp['game_id'];
        return true;
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
}