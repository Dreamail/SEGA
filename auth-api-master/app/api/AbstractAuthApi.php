<?php
namespace app\api;

require_once "./app/constant.php";
require_once "./config/app_config.php";
require_once "./config/db_config.php";
require_once "./config/log_config.php";
require_once "./app/utils/DbUtil.php";

use app\utils\DbUtil;
use app\utils\LogUtil;

abstract class AbstractAuthApi {

    public $key_allnet_auth;
    public $resp_attribute_tag;
    public $rcv_packet = "";
    public $rsp_packet = "";
    public $expires = "";
    public $auth_data = "";
    public $response;

    function __construct() {

        $this->key_allnet_auth = $this->get_key_allnet_auth(KEY_ALLNET_AUTH_FILE);
        $this->resp_attribute_tag = AL_TAG_AUTH;
    }

    abstract function doApi();

    /**
     * 共通キー（key_allnet_auth）の取得
     * @return string
     */
    function get_key_allnet_auth($key_allnet_auth_file_location) {
        if ( file_exists($key_allnet_auth_file_location) ) {
            if ( $fp = fopen($key_allnet_auth_file_location, 'r') ) {
                $line= fgets($fp);
                fclose($fp);
                return hex2bin($line);
            } else {
                LogUtil::debugLog("共通キー取得不可");
                $this->resp_attribute_tag = AL_AUTH_ERROR;
                return openssl_random_pseudo_bytes(16);
            }
        } else {
            LogUtil::debugLog("共通キー取得不可");
            $this->resp_attribute_tag = AL_AUTH_ERROR;
            return openssl_random_pseudo_bytes(16);;
        }
    }

    function aes_dec_cbc($key, $iv, $data) {
        return $decrypted_data = openssl_decrypt($data,"aes-128-cbc",$key,OPENSSL_RAW_DATA|OPENSSL_NO_PADDING, $iv );
    }

    public function aes_enc_cbc($key, $iv, $data) {
        return openssl_encrypt($data, "aes-128-cbc", $key, OPENSSL_RAW_DATA|OPENSSL_NO_PADDING, $iv );
    }

    public function send_resonse() : void {
        $this->response = "packet_data=".$this->rsp_packet."&auth_data=".$this->auth_data;

        header("Content-Type: text/html; charset=utf-8");
        echo $this->response;

    }
}
?>