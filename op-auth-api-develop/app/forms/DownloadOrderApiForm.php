<?php
namespace app\forms;

require_once "app/forms/AbstractForm.php";
require_once "app/exceptions/ValidateException.php";

use app\exceptions\ValidateException;
use app\forms\AbstractForm;
use app\utils\LogUtil;

/**
 * 指示書URI取得APIリクエストフォーム
 * Class DownloadOrderApiForm
 * @package app\forms
 */
class DownloadOrderApiForm extends AbstractForm
{

    /**
     * DownloadOrderApiForm constructor.
     * @param $method $_SERVER['REQUEST_METHOD']
     * @param $userAgent $_SERVER['HTTP_USER_AGENT']
     * @param $requestBody file_get_contents('php://input')
     */
    public function init($method, $userAgent, $requestBody, $globalIp)
    {
        // HTTPメソッド
        $this->method = $method;
        // ユーザエージェント
        $this->userAgent = $userAgent;
        //認証リクエスト文字列
        $this->queryString = trim(gzuncompress(base64_decode($requestBody)));

        // リクエストパラメータ文字列から各パラメータの値をセットする
        $this->setRequestParam();
    }

    /**
     * リクエストパラメータ文字列から各パラメータの値をセットします
     */
    public function setRequestParam()
    {
        LogUtil::debugLog("認証リクエスト文字列 queryString=" . $this->queryString);
        if (empty($this->queryString))
        {
            // リクエストパラメータ文字列が空の場合は何もしない
            return;
        }
        // リクエストパラメータ文字列を'&'で分割して各パラメータにセットします
        foreach (explode('&', $this->queryString) as $param) {
            $paramKeyValue = explode('=', $param, 2);
            if (count($paramKeyValue) != 2) {
                // key=valueの形式でない場合次へ
                continue;
            }
            switch ($paramKeyValue[0])
            {
                case 'auth_data':
                    $this->authData = $paramKeyValue[1];
                    break;
                case 'game_id':
                    $this->gameId = $paramKeyValue[1];
                    break;
                case 'ver':
                    $this->ver = $paramKeyValue[1];
                    break;
                case 'serial':
                    $this->serial = $paramKeyValue[1];
                    break;
                case 'ip':
                    $this->ip = $paramKeyValue[1];
                    break;
                case 'encode':
                    $this->encode = $paramKeyValue[1];
                    break;
            }
        }
    }

    /**
     * 入力チェック
     */
    public function validateRequest()
    {
        if (empty($this->queryString)) {
            throw new ValidateException('query string empty. queryString', $this->queryString);
        }
        if (empty($this->authData)) {
            throw new ValidateException('Auth Data is empty. authData', $this->authData);
        }
        if (empty($this->gameId)) {
            throw new ValidateException('Game Id is empty. game_id', $this->gameId);
        }
        if (GAME_ID_LENGTH < mb_strlen($this->gameId)) {
            throw new ValidateException('Length error. length=' . mb_strlen($this->gameId) . ' game_id', $this->gameId);
        }
        if (empty($this->ver)) {
            throw new ValidateException('Ver is empty. ver', $this->ver);
        }
        if (VER_LENGTH < mb_strlen($this->ver)) {
            throw new ValidateException('Length error. length=' . mb_strlen($this->ver) . ' ver', $this->ver);
        }
        if (!(is_numeric($this->ver) && VER_MIN <= floatval($this->ver) && VER_MAX >= floatval($this->ver))) {
            throw new ValidateException('Format error. ver', $this->ver);
        }
        if (empty($this->serial)) {
            throw new ValidateException('Serial empty. serial', $this->serial);
        }
        if (SERIAL_LENGTH < mb_strlen($this->serial)) {
            throw new ValidateException('Length error. length=' . mb_strlen($this->serial) . ' serial', $this->serial);
        }
        if(!empty($this->encode) && !in_array(strtoupper($this->encode), ENCODING_TARGET)) {
            $this->encode = ENCODE;
        }
    }
}
