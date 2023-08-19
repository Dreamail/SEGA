<?php

namespace app\forms;

require_once "app/constant.php";
require_once "app/exceptions/ValidateException.php";
require_once "app/utils/LogUtil.php";
require_once "app/utils/CommonUtil.php";
require_once "app/services/CipherService.php";

use app\exceptions\ValidateException;
use app\services\CipherService;
use app\utils\SignatureVerify;
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
    protected $authData;
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
        $this->queryString = trim(gzuncompress(base64_decode($requestBody)));
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
        if(empty($this->ip)) {
            throw new ValidateException('IP empty. ip', $this->ip);
        }
        if(!filter_var($this->ip, FILTER_VALIDATE_IP)) {
            throw new ValidateException('Format error. ip', $this->ip);
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
//            throw new ValidateException('Encode check error. encode', $this->encode);
        }
//        if (empty($this->formatVer)) {
//            throw new ValidateException('Format Ver empty. formatVer', $this->formatVer);
//        }
//        if(FORMAT_VERSION != intval($this->formatVer)) {
//            throw new ValidateException('Format version check error. formatVer', $this->formatVer);
//        }
    }

    /**
     * @return mixed
     */
    public function getMethod()
    {
        return $this->method;
    }

    /**
     * @return mixed
     */
    public function getUserAgent()
    {
        return $this->userAgent;
    }

    /**
     * @return mixed
     */
    public function getAuthData()
    {
        return $this->authData;
    }

    /**
     * @return mixed
     */
    public function getGameId()
    {
        return $this->gameId;
    }

    /**
     * @return mixed
     */
    public function getVer()
    {
        return $this->ver;
    }

    /**
     * @return mixed
     */
    public function getSerial()
    {
        return $this->serial;
    }

    /**
     * @return mixed
     */
    public function getIp()
    {
        return $this->ip;
    }

    /**
     * @return mixed
     */
    public function getQueryString()
    {
        return $this->queryString;
    }

    /**
     * @return mixed
     */
    public function getGlobalIp()
    {
        return $this->globalIp;
    }

    /**
     * @return mixed
     */
    public function getFirmVer()
    {
        return $this->firmVer;
    }

    /**
     * @return mixed
     */
    public function getBootVer()
    {
        return $this->bootVer;
    }

    /**
     * @return mixed
     */
    public function getHops()
    {
        return $this->hops;
    }

    /**
     * @return mixed
     */
    public function getEncode()
    {
        return $this->encode;
    }

    /**
     * @return mixed
     */
    public function getFormatVer()
    {
        return $this->formatVer;
    }
}