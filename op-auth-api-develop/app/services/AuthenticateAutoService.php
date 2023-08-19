<?php

namespace app\services;

require_once "app/constant.php";
require_once "app/utils/CommonUtil.php";
require_once "app/utils/LogUtil.php";
require_once "app/models/Machine.php";
require_once "app/models/Vkeychips.php";
require_once "app/models/PrdCheckGames.php";
require_once "app/models/AuthAllowedPlaces.php";
require_once "app/models/AuthAllowedComps.php";
require_once "app/models/AuthDenied.php";

use app\models\AuthAllowedComps;
use app\models\AuthAllowedPlaces;
use app\models\AuthDenied;
use app\models\Machine;
use app\models\PrdCheckGames;
use app\models\Vkeychips;
use app\utils\CommonUtil;
use app\utils\LogUtil;

/**
 * 自動認証サービス
 * Class AuthenticateAutoService
 * @package app\services
 */
class AuthenticateAutoService
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
     * 自動認証処理
     * @param $paramGameId リクエストパラメータのゲームタイトルID
     * @param $parmSerial リクエストパラメータのシリアル
     */
    public function authenticateAuto($paramGameId, $parmSerial)
    {
        // すでにエラーの場合、処理しない
        if ($this->status['result'] != STAT_RESULT_SUCCESS) {
            LogUtil::debugLog("[自動認証]処理前にすでにエラー。 result=" . $this->status['result']);
            return;
        }

        if(strlen($parmSerial) <= 0) {
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_SELIAL;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 9);
            LogUtil::debugLog("[自動認証]登録時のシリアルが0文字。 serial=" . $parmSerial);
            return;
        }

        if(!$this->checkKeychipInfo($paramGameId, $parmSerial)) {
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_SELIAL;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 4);
            LogUtil::debugLog("[自動認証]生産情報チェックに失敗。 serial=" . $parmSerial . " gameId=" . $paramGameId);
            return;
        }

        $authDenied = false;
        $compCode = '';
        if(AUTH_TYPE_PLACE_AUTO == $this->gameAttribute['auth']) {
            // 店舗認証情報を取得
            $authAllowedPlace = AuthAllowedPlaces::findAuthAllowedPlace($paramGameId, $this->routers['allnet_id']);
        } else {
            $billCode = $this->routers['bill_code'];
            if(!empty($billCode)) {
                if(strlen($billCode) <= COMP_CODE_LENGTH) {
                    $compCode = $billCode;
                } else {
                    $compCode = substr($billCode, 0, COMP_CODE_LENGTH);
                }
            }

            $authDenied = AuthDenied::checkAuthDenied($paramGameId, $compCode, $billCode);
            if(AUTH_TYPE_COMP_AUTO_MOVE == $this->gameAttribute['auth']) {
                $authAllowedComp = AuthAllowedComps::findAuthAllowedComp($paramGameId, $compCode);
            }
        }

        switch ($this->gameAttribute['auth']) {
            case AUTH_TYPE_PLACE_AUTO:
                if(empty($authAllowedPlace)) {
                    // ステータスをセット
                    $this->status['result'] = STAT_RESULT_FAIL_LOC;
                    $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 11);
                    LogUtil::debugLog("[自動認証]店舗自動認証が許可されていない。 gameId=" . $paramGameId . "allnetId=" . $this->routers['allnet_id']);
                    return;
                }
                /* 認証方式をログ出力 */
                LogUtil::operationApiLog("Authentication type: AUTO(PLACE)");
                break;
            case AUTH_TYPE_COMP_AUTO_MOVE:
                if(empty($authAllowedComp)) {
                    // ステータスをセット
                    $this->status['result'] = STAT_RESULT_FAIL_LOC;
                    $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 12);
                    LogUtil::debugLog("[自動認証]包括先自動認証が許可されていない。 gameId=" . $paramGameId . "compCode=" . $compCode);
                    return;
                }
                break;
            default:
                if($authDenied) {
                    // ステータスをセット
                    $this->status['result'] = STAT_RESULT_FAIL_LOC;
                    $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 10);
                    LogUtil::debugLog("[自動認証]自動認証が不許可。 gameId="
                        . $paramGameId . " compCode=" . $compCode . " billCode=" . $billCode);
                    return;
                }
                /* 認証方式をログ出力 */
                if(AUTH_TYPE_COMP_AUTO_MOVE == $this->gameAttribute['auth']) {
                    LogUtil::operationApiLog("Authentication type: AUTO(COMP)");
                } else {
                    LogUtil::operationApiLog("Authentication type: AUTO");
                }
        }
        $this->machine['serial'] = $parmSerial;
        $this->machine['allnet_id'] = $this->routers['allnet_id'];
        $this->machine['game_id'] = $this->gameAttribute['game_id'];
        $this->machine['place_id'] = $this->routers['place_id'];

        Machine::insertMachine($this->machine);
    }

    /**
     * キーチップ生産情報をチェックする。
     *
     * @param gameId
     * @param serial
     * @return
     */
    private function checkKeychipInfo($gameId, $serial) {

        // キーチップ生産情報を参照するかチェック
        if (PrdCheckGames::checkPrdCheckGame($gameId)) {

            $result = Vkeychips::findKeychipStat($serial, $gameId);
            if (!empty($result)) {
                switch ($result['keychip_stat']) {
                case 1:
                case 2:
                case 3:
                case 4:
                    return true;
                }
            }
            return false;
        }
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