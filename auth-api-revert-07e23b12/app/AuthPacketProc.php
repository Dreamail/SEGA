<?php
require_once  "./const.php";

class AuthPacketProc {

    public $ini;
    public $con;
    public $key_allnet_auth;
    public $resp_attribute_tag;
    public $rcv_packet = "";
    public $rsp_packet = "";
    public $expires = "";
    public $verify_data = "";

    function __construct() {
        $this->ini = parse_ini_file("./conf/config.ini");
        $host = $this->ini['host'];
        $dbname = $this->ini['dbname'];
        $user = $this->ini['user'];
        $password = $this->ini['password'];

        $this->con = pg_connect("host=$host dbname=$dbname user=$user password=$password");

        $this->key_allnet_auth = $this->get_key_allnet_auth($this->ini['key_allnet_auth_file']);

        $this->resp_attribute_tag = AL_TAG_AUTH;
    }

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
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * キーチップ生産情報の取得
     * @return false : array
     */
    function get_keychip_product_info($keychip_id) : array {
        $result = pg_query($this->con,'select keychip_id, key_server, key_keychip from keychip_product_info where keychip_id = '."'".$keychip_id."'");
        if ( ! $result ) {
            return false;
        }
        $row = pg_fetch_array($result, NULL,PGSQL_ASSOC );
        if ( ! $row ) {
            throw new Exception();
        }
        return $row;
    }

    function aes_dec_cbc($key, $iv, $data) {
        return $decrypted_data = openssl_decrypt($data,"aes-128-cbc",$key,OPENSSL_RAW_DATA, $iv );
    }

    function aes_enc_cbc($key, $iv, $data) {
        return openssl_encrypt($data, "aes-128-cbc", $key, OPENSSL_RAW_DATA, $iv );
    }

    function send_resonse() : void {
        $rsp_packet_data_B64 = base64_encode($this->rsp_packet);
        header("Content-Type: application/json; charset=utf-8");
        echo json_encode(array('packet_data' => "$rsp_packet_data_B64",'expires' => "$this->expires", 'verify_data' => "$this->verify_data"));
    }
}
?>