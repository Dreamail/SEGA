<?php

namespace app\forms;

require_once "app/constant.php";
require_once "app/exceptions/ValidateException.php";
require_once "app/utils/LogUtil.php";
require_once "app/utils/CommonUtil.php";
require_once "app/services/CipherService.php";

use app\exceptions\ValidateException;
use app\services\CipherService;
use app\utils\CipherUtil;
use app\utils\CommonUtil;
use app\utils\LogUtil;

/**
 * Class AbstractForm
 * @package app\forms
 */
abstract class AbstractForm
{
    /** リクエストメソッド */
    protected $method;
    /** UserAgent */
    protected $userAgent;
    /** 認証リクエスト */
    protected $queryString;
    /** グローバルIP */
    protected $globalIp;
    /** 認証結果 */
    protected $verifyData;
    /** ゲームタイトルID */
    protected $gameId;
    /** ゲームタイトルVer. */
    protected $ver;
    /** 基板シリアル */
    protected $serial;
    /** 店舗IP */
    protected $ip;
    /** ファームウェアバージョン */
    protected $firmVer;
    /** ブートROMバージョン */
    protected $bootVer;
    /** 応答文字エンコーディング */
    protected $encode;
    /** フォーマットバージョン */
    protected $formatVer;
    /** ホップ数 */
    protected $hops;
    /** トークン */
    protected $token;

    /**
     * init.
     * @param string $method
     * @param string $userAgent
     * @param $requestBody
     * @param $globalIp
     * @return mixed
     */
    protected function init($method, $userAgent, $requestBody, $globalIp)
    {
        // HTTPメソッド
        $this->method = $method;
        // ユーザエージェント
        $this->userAgent = $userAgent;
        //認証リクエスト文字列
        $this->queryString = trim(urldecode($requestBody));
        // グローバルIP
        $this->globalIp = $globalIp;
    }

    /**
     * リクエストパラメータ文字列から各パラメータの値をセットします（フォームごとに実装）
     */
    protected abstract function setRequestParam();

    /**
     * 入力チェック
     */
    protected function validateRequest()
    {
        if (empty($this->queryString)) {
            throw new ValidateException('query string empty. queryString', $this->queryString);
        }
        if (empty($this->gameId)) {
            throw new ValidateException('Title Id is empty. titleId', $this->gameId);
        }
        if (TITLE_ID_LENGTH < mb_strlen($this->gameId)) {
            throw new ValidateException('Length error. length=' . mb_strlen($this->gameId) . ' title_id', $this->gameId);
        }
        if (empty($this->ver)) {
            throw new ValidateException('Title Ver is empty. titleVer', $this->ver);
        }
        if (TITLE_VER_LENGTH < mb_strlen($this->ver)) {
            throw new ValidateException('Length error. length=' . mb_strlen($this->ver) . ' title_ver', $this->ver);
        }
        if (empty($this->serial)) {
            throw new ValidateException('Machine Id empty. machineId', $this->serial);
        }
        if (CLIENT_ID_LENGTH < mb_strlen($this->serial)) {
            throw new ValidateException('Length error. length=' . mb_strlen($this->serial) . ' machine', $this->serial);
        }
        if(!empty($this->firmVer)) {
            if(!is_numeric($this->firmVer)) {
                throw new ValidateException('Numeric error. firmVer', $this->firmVer);
            } else if(intval($this->firmVer) < VERSION_MIN_LENGTH || intval($this->firmVer) > VERSION_MAX_LENGTH) {
                throw new ValidateException('Range check error. firmVer', $this->firmVer);
            }
        }
        if(!empty($this->bootVer)) {
            if(!is_numeric($this->bootVer)) {
                throw new ValidateException('Numeric error. bootVer', $this->bootVer);
            } else if(intval($this->bootVer) < VERSION_MIN_LENGTH || intval($this->bootVer) > VERSION_MAX_LENGTH) {
                throw new ValidateException('Range check error. bootVer', $this->bootVer);
            }
        }
        if(!empty($this->encode) && !in_array(strtoupper($this->encode), ENCODING_TARGET)) {
            $this->encode = ENCODE;
            throw new ValidateException('Encode check error. encode', $this->encode);
        }
        if (empty($this->formatVer)) {
            throw new ValidateException('Format Ver empty. formatVer', $this->formatVer);
        }
        if(!preg_match('/^[0-9]+$/', $this->formatVer) || FORMAT_VERSION != intval($this->formatVer)) {
            throw new ValidateException('Format version check error. formatVer', $this->formatVer);
        }
    }
}