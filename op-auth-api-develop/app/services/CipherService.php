<?php

namespace app\services;

require_once "app/constant.php";
require_once "app/models/Game.php";

use app\models\Game;

/**
 * Class CipherService
 * @package app\services
 */
class CipherService
{
    /**
     * 暗号化キーを取得します
     * @param $uaTitleId ユーザエージェント内タイトルID
     * @return 暗号化キー
     * @throws \Exception
     */
    public function getCipherKey($uaTitleId)
    {
        /* ゲーム情報取得 */
        $game = Game::findByPk($uaTitleId);
        if (count($game) == 0) {
            return null;
        }
        return $game['cipher_key'];
    }
}