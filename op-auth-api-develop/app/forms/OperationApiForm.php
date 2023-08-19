<?php

namespace app\forms;

require_once "app/forms/AbstractForm.php";

use app\forms\AbstractForm;
use app\utils\LogUtil;

/**
 * 基板認証APIリクエストフォーム
 * Class InitializeApiForm
 * @package app\forms
 */
class OperationApiForm extends AbstractForm
{
    /**
     * InitializeApiForm constructor.
     * @param $method $_SERVER['REQUEST_METHOD']
     * @param $userAgent $_SERVER['HTTP_USER_AGENT']
     * @param $requestBody file_get_contents('php://input')
     * @param $globalIp
     */
    public function init($method, $userAgent, $requestBody, $globalIp)
    {
        parent::init($method, $userAgent, $requestBody, $globalIp);

        // 認証リクエスト文字列から各パラメータの値をセットする
        $this->setRequestParam();

        //format_verの値がない、または負数の場合1.00をセット
        if(empty($this->formatVer)||$this->formatVer<0){
            $this->formatVer = FORMAT_VER;
        }
        //encodeの値がない場合EUC-JPをセット
        if(empty($this->encode)){
            $this->encode = ENCODE;
        }

        //hopsの値がない場合はをデフォルト値をセット
        if(empty($this->hops)){
            $this->hops = HOPS;
        }
    }

    /**
     * 認証リクエスト文字列から各パラメータの値をセットします
     */
    public function setRequestParam()
    {
        LogUtil::debugLog("認証リクエスト文字列 queryString=" . $this->queryString);
        if (empty($this->queryString)) {
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
            switch ($paramKeyValue[0]) {
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
                case 'firm_ver':
                    $this->firmVer = $paramKeyValue[1];
                    break;
                case 'boot_ver':
                    $this->bootVer = $paramKeyValue[1];
                    break;
                case 'encode':
                    $this->encode = $paramKeyValue[1];
                    break;
                case 'format_ver':
                    $this->formatVer = $paramKeyValue[1];
                    break;
                case 'hops':
                    $this->hops = $paramKeyValue[1];
                    break;
                case 'token':
                    $this->token = $paramKeyValue[1];
                    break;
            }
        }
    }

    /**
     * @return mixed
     */
    public function getToken()
    {
        return $this->token;
    }

    /**
     * 入力チェック
     */
    public function validateRequest()
    {
        parent::validateRequest();
    }
}
