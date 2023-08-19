<?php

namespace app\services;

require_once "app/constant.php";
require_once "app/utils/CommonUtil.php";
require_once "app/utils/LogUtil.php";

use app\models\Machine;
use app\utils\CommonUtil;
use app\utils\LogUtil;

/**
 * 認証サービス
 * Class AuthenticateService
 * @package app\services
 */
class AuthenticateService
{
    /* ステータス */
    private $status = array();

    /* ゲーム属性情報 */
    private $gameAttribute = array();

    /* 基板情報 */
    private $machine = array();

    /**
     * AuthenticateService constructor.
     */
    public function __construct($status, $gameAttribute, $machine)
    {
        $this->status = $status;
        $this->gameAttribute = $gameAttribute;
        $this->machine = $machine;
    }

    /**
     * 通常認証判定処理
     * @param $paramTitleId リクエストパラメータのゲームタイトルID
     */
    public function authenticateNormal($paramTitleId)
    {
        // すでにエラーの場合、処理しない
        if ($this->status['result'] != STAT_RESULT_SUCCESS) {
            LogUtil::debugLog("[通常認証]処理前にすでにエラー。 result=" . $this->status['result']);
            return;
        }

        // リクエストのゲームIDが基板情報の予約ゲームIDと一致する場合は基板情報を更新
        $this->exchangeGameId($paramTitleId);
        if ($this->status['result'] === STAT_RESULT_FAIL_GAME) {
            return;
        }

        // 基板情報の通信セッティングが通信許可でない場合、エラーステータスをセットして処理終了
        if ($this->machine['setting'] != COMMUNICATION_SETTING_ALLOWED) {
            // ステータスをセット
            $this->status['result'] = STAT_RESULT_FAIL_MACHINE;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 7);
            LogUtil::debugLog("[通常認証]基板情報の通信セッティングが通信許可でない。 setting=" . $this->machine['setting']);
            return;
        }

        /* 認証方式をログ出力 */
        LogUtil::activateApiLog("Authentication type: NORMAL");
    }

    /**
     * リクエストのゲームIDが基板情報の予約ゲームIDと一致する場合は基板情報を更新
     * @param $titleId
     * @return
     */
    public function exchangeGameId($titleId)
    {
        if ($titleId === $this->machine['game_id']) {
            return;
        }

        if ($titleId !== $this->machine['reserved_game_id']) {
            $this->status['result'] = STAT_RESULT_FAIL_GAME;
            $this->status['cause'] = CommonUtil::getCause($this->gameAttribute['auth'], 6);
            return;
        }

        // リクエストのゲームIDが基板情報の予約ゲームIDと一致するならゲームIDと交換する
        $machines = new Machine();
        $machines::exchangeMachineGameId($this->machine);

        // 取得済みGameを交換
        $tmp['game_id'] = $this->machine['game_id'];
        $this->machine['game_id'] = $this->machine['reserved_game_id'];
        $this->machine['reserved_game_id'] = $tmp['game_id'];
        return;
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