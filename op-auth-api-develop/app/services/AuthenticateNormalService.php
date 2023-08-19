<?php

namespace app\services;

require_once "app/constant.php";
require_once "app/utils/CommonUtil.php";
require_once "app/utils/LogUtil.php";

use app\models\Machine;
use app\utils\CommonUtil;
use app\utils\LogUtil;

/**
 * 通常認証サービス
 * Class AuthenticateNormalService
 * @package app\services
 */
class AuthenticateNormalService
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
     * 通常認証判定処理
     * @param $paramGameId リクエストパラメータのゲームタイトルID
     */
    public function authenticateNormal($paramGameId)
    {
        // すでにエラーの場合、処理しない
        if ($this->status['result'] != STAT_RESULT_SUCCESS) {
            LogUtil::debugLog("[通常認証]処理前にすでにエラー。 result=" . $this->status['result']);
            return;
        }

        // リクエストのゲームIDが基板情報の予約ゲームIDと一致する場合は基板情報を更新
        $exchange = $this->exchangeGameId($paramGameId);
        if ($this->status['result'] === STAT_RESULT_FAIL_GAME) {
            return;
        }

        if($this->gameAttribute['auth'] !== strval(AUTH_TYPE_DEBUG)
                && !CommonUtil::isSamePlace($this->routers, $this->machine)) {
            // デバッグではなく店舗が不一致
            $this->status['result'] = STAT_RESULT_FAIL_LOC;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 6);
            LogUtil::debugLog("[通常認証]デバッグではなく店舗が不一致。 routers->allnetId=" .
                $this->routers['allnet_id'] . "machine->allnetId=" . $this->machine['allnet_id']);
            return;
        }

        // 基板情報の通信セッティングが通信許可でない場合、エラーステータスをセットして処理終了
        if ($this->machine['setting'] != COMMUNICATION_SETTING_ALLOWED) {
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_SELIAL;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 8);
            LogUtil::debugLog("[通常認証]基板情報の通信セッティングが通信許可でない。 setting=" . $this->machine['setting']);
            return;
        }

        if($this->gameAttribute['auth'] === AUTH_TYPE_DEBUG) {
            /* 認証方式をログ出力 */
            LogUtil::operationApiLog("Authentication type: NORMAL(DEBUG)");
        } else {
            /* 認証方式をログ出力 */
            LogUtil::operationApiLog("Authentication type: NORMAL");
        }
        if($exchange) {
            LogUtil::operationApiLog("GameId exchanged [gameId:".
                $this->machine['game_id'] .", reservedGameId:". $this->machine['reserved_game_id'] ."]");
        }
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