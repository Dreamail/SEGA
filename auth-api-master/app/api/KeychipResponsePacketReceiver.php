<?php
namespace app\api;

require_once 'AbstractAuthApi.php';
require_once './app/utils/CMSUtil.php';

use app\utils\DbUtil;
use Exception;
use app\utils\LogUtil;
use app\models\KeychipProductInfo;
use app\models\AuthLog;
use app\utils\CMSUtil;

class KeychipResponsePacketReceiver extends AbstractAuthApi
{

    public $request_data;
    public $auth_seq;
    public $cause = "";
    public $result;
    public $packet_data;

    function __construct()
    {
        parent::__construct();

        # リクエストデータ取得
        $this->request_data = file_get_contents( 'php://input' );

    }

    function doApi() {

        try {
            LogUtil::serverChallnegePacketReceiverApiLog("start KeychipResponsePacketReceiverAPI:request_data=".$this->request_data);

            // DB接続
            DbUtil::getConnect();
            DbUtil::trace();

            // シーケンス値取得
            $this->auth_seq = $_SESSION['auth_seq'];

            // 変数初期化
            $recv_keychip_response = "";
            $auth_keychip_challenge = "";
            $keychip_id = "";

            // 処理結果初期化
            $this->result = hex2bin("0000");

            $queryString = trim($this->request_data);
            $this->createData($queryString);

            #4-a. cv_allnet_auth_3(16) = auth_keychip_response_packet[16, 16]
            # 及び raw(32) = aes_dec_cbc(key_allnet_auth, cv_allnet_auth, auth_keychip_response_packet(32)) を得ます
            if (!$this->createData($queryString) && empty($this->packet_data)){
                # エラー時の挙動-No.6 リクエストパラメータが取得できない
                LogUtil::debugLog("リクエストパラメータ取得不可");
                $this->result = AL_ERROR_AUTH;
                $this->cause = "リクエストパラメータ取得不可";
                goto rec_kc_resp;
            }
            $packet_data_b64dec = base64_decode($this->packet_data);
            $req_packet_data_ary = str_split($packet_data_b64dec);

            $cv_allnet_auth_2 = array_key_exists('cv_allnet_auth_2', $_SESSION) ? $_SESSION['cv_allnet_auth_2'] : '';
            $cv_allnet_auth_3 = array_slice($req_packet_data_ary,-16,16);
            $raw = parent::aes_dec_cbc($this->key_allnet_auth, hex2bin($cv_allnet_auth_2), $packet_data_b64dec);
            if ( ! $raw ) {
                LogUtil::debugLog("クライアントパケット復号化不可");
                # エラー時の挙動-No.4 クライアント側で生成したパケットを復号化できない。
                $this->result = AL_ERROR_AUTH;
                $this->cause = "クライアントパケット復号化不可";
                goto rec_kc_resp;
            }

            #4-b. 属性タグ(1) = raw[0, 1], 機能コード(1) = raw[1, 1], keychip_id(11) = raw[2, 11], result(2) = raw[14, 2],
            # auth_keychip_response(16) = raw[16, 16] を得ます
            $raw_ary = str_split($raw);
            $attribute_tag = implode(array_slice($raw_ary, 0,1)); // ヘッダ情報：属性タグ
            $function_code = implode(array_slice($raw_ary, 1,1)); // ヘッダ情報：機能コード
            $keychip_id = implode(array_slice($raw_ary,2,11));    // ヘッダ情報：キーチップID
            $kc_result = implode(array_slice($raw_ary,14,2));        // 結果
            $auth_keychip_response = implode(array_slice($raw_ary,16, 16)); // キーチップ認証レスポンス

            #4-c. (4-b) での header の各値が想定外の値である等不正である場合は通信エラーのためログに残し、以下のキーチップ認証処理は
            #行わず認証失敗とし、(4-f) 以降の処理を行います
            // キーチップIDチェック　※keychip_idにバイナリデータが混在すると認証ログ登録時に問題を起こすので一番最初にチェックする事
            if ( ! preg_match("/^[A-HJ-NP-Z0-9]+$/",$keychip_id) ) {
                LogUtil::debugLog("不正ヘッダ(キーチップIDが文字列以外)");
                $keychip_id = "00000000000";
                $this->result = AL_ERROR_AUTH;
                $this->cause = "不正ヘッダ(キーチップIDが文字列以外)";
                goto rec_kc_resp;
            }
            // タグ情報チェック
            if ( ( $attribute_tag != AL_TAG_AUTH ) || ( $function_code != AL_AUTH_KEYCHIP_RESPONSE ) ) {
                LogUtil::debugLog("不正ヘッダ");
                # エラー時の挙動-No.5 header の各値が想定外の値
                $this->result = AL_ERROR_AUTH;
                $this->cause = "不正ヘッダ";
                goto rec_kc_resp;
            }

            #4-d. result(2) が AL_ERROR_AUTH の場合、サーバ認証に失敗しているのでログに残します
            if ( strcmp($kc_result, AL_ERROR_AUTH) === 0 ) {
                LogUtil::debugLog("サーバ認証失敗");
                # エラー時の挙動-No.7 サーバ認証が失敗
                $this->result = AL_ERROR_AUTH;
                $this->cause = "サーバ認証失敗";
                goto rec_kc_resp;
            }

            #4-e. recv_keychip_response(16) = aes_dec_cbc(key_keychip, iv_keychip_challenge, auth_keychip_response(16))
            # を行い、保存していた auth_keychip_challenge(16) と recv_keychip_response(16) が等しければキーチップ認証成功、
            #等しくなければキーチップ認証失敗です
            $keychip_product_info = KeychipProductInfo::getKeychipProductInfo($keychip_id);
            if ( count($keychip_product_info) == 0 ) {
                LogUtil::debugLog("キーチップ生産情報取得失敗");
                $key_keychip = bin2hex(openssl_random_pseudo_bytes(16));
                $keychip_id = "00000000000";
                $this->result = AL_ERROR_AUTH;
            } else {
                $key_keychip = $keychip_product_info["key_keychip"];
            }
            $iv_keychip_challenge = $_SESSION["iv_keychip_challenge"];
            $auth_keychip_challenge = $_SESSION["auth_keychip_challenge"];
            $recv_keychip_response = parent::aes_dec_cbc(
                hex2bin($key_keychip),
                hex2bin($iv_keychip_challenge),
                $auth_keychip_response
            );
            if ( ! $recv_keychip_response ) {
                LogUtil::debugLog("キーチップレスポンス復号化不可");
                # エラー時の挙動-No.4 クライアント側で生成したパケットを復号化できない。
                $this->cause = "キーチップレスポンス復号化不可";
                $this->result = AL_ERROR_AUTH;
            }

            rec_kc_resp:
            #4-f. キーチップ認証成功した場合、ALLNET_ERROR result(2) = 0, キーチップ認証失敗の場合
            # ALLNET_ERROR result(2) = AL_ERROR_AUTH とします
            if ( strcmp($recv_keychip_response, hex2bin($auth_keychip_challenge) ) !== 0 ) {
                LogUtil::debugLog("キーチップチャレンジ＆レスポンス不一致:recv_keychip_response=".bin2hex($recv_keychip_response).",auth_keychip_challenge=".$auth_keychip_challenge);
                $this->result = AL_ERROR_AUTH;
                $this->cause = "キーチップチャレンジ＆レスポンス不一致";
            }

            #4-g. この後サーバ-キーチップ認証成功の場合、オプショナルのリクエストパケットを返却、そうでなければサーバ-キーチップ認証の
            #成否に関わらず認証クライアント宛にサーバ署名を付加した認証結果を返却します。このデータはA7/AXPキーチップには渡されません
            // 署名付与
            $auth_result_info = CMSUtil::createAuthResultInfo($this->result, $keychip_id, $_SESSION['auth_seq']);
            $this->expires = $auth_result_info['limit'];
            $this->auth_data = CMSUtil::signedAuthResultInfo(
                $auth_result_info,
                PRIVATE_KEY_FILE,
                CERTIFICATE_FILE,
                AUTH_TEMP_DIR
            );

            // レスポンス
            if ( ! empty($this->auth_data) ) {
                parent::send_resonse();
            } else {
                LogUtil::debugLog("署名付与失敗:keychip_id=".$keychip_id.",auth_seq=".$_SESSION['auth_seq'].",result=".bin2hex($this->result));
                $this->result = AL_ERROR_AUTH;
                $this->cause = "署名付与失敗";
                http_response_code(500);
            }

            AuthLog::authResult(hexdec(bin2hex($this->result)), $this->cause, $this->request_data, $this->expires, $this->response, $this->auth_seq);

        } catch ( Exception $e ) {
            http_response_code(500);
            LogUtil::errorLog($e);
        } finally {
            DbUtil::untrace();
            DbUtil::closeConnect();
        }


        LogUtil::serverChallnegePacketReceiverApiLog("end KeychipResponsePacketReceiverAPI:auth_seq=".$this->auth_seq
            .",response=".$this->response);

    }

    private function createData($queryString) {

        if (empty($queryString)) {
            // リクエストパラメータ文字列が空の場合は何もしない
            return false;
        }
        // リクエストパラメータ文字列を'&'で分割して各パラメータにセットします
        foreach (explode('&', $queryString) as $param) {
            $paramKeyValue = explode('=', $param, 2);
            if (count($paramKeyValue) != 2) {
                // key=valueの形式でない場合次へ
                continue;
            }
            switch ($paramKeyValue[0]) {
                case 'packet_data':
                    $this->packet_data = $paramKeyValue[1];
                    break;
            }
        }
        return true;
    }

}