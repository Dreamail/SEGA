<?php
namespace app\api;

require_once './app/api/AbstractAuthApi.php';
require_once './app/models/AuthSequence.php';
require_once './app/models/KeychipProductInfo.php';
require_once './app/models/AuthLog.php';

use app\utils\DbUtil;
use Exception;
use app\utils\LogUtil;
use app\models\AuthSequence;
use app\models\KeychipProductInfo;
use app\models\AuthLog;

class ServerChallengePacketReceiver extends AbstractAuthApi

{

    public $request_data;
    public $packet_data;
    public $auth_seq;
    public $result;

    function __construct()
    {
        parent::__construct();

        # リクエストデータ取得
        $this->request_data = file_get_contents( 'php://input' );

    }

    function doApi() {

        try {
            // 開始メッセージをアプリケーションログに出力
            LogUtil::serverChallnegePacketReceiverApiLog("start ServerChallengePacketReceiverAPI:request_data=".$this->request_data);

            // DB接続
            DbUtil::getConnect();
            DbUtil::trace();

            // 変数初期化
            $keychip_id = "";
            $iv_keychip_challenge = "";
            $auth_keychip_challenge = "";
            $req_packet_data ="";
            $this->result = hex2bin("0000");

            # シーケンス値取得
            $auth_seq = AuthSequence::getAuthSequence();
            $this->auth_seq = sprintf('%010d', $auth_seq);
            $_SESSION['auth_seq'] = $this->auth_seq;

            $queryString = trim($this->request_data);

            $this->createData($queryString);

            #2-f. iv_keychip_challenge = rand(16) を生成します（この値は保存しておきます
            $iv_keychip_challenge = openssl_random_pseudo_bytes(16);
            $_SESSION['iv_keychip_challenge'] = bin2hex($iv_keychip_challenge);

            #2-g. auth_keychip_challenge = rand(16) を生成します（この値は保存しておきます
            $auth_keychip_challenge = openssl_random_pseudo_bytes(16);
            $_SESSION['auth_keychip_challenge'] = bin2hex($auth_keychip_challenge);

            # サーバーチャレンジパケットの取得
            if (!$this->createData($queryString) && empty($this->packet_data)){
                LogUtil::debugLog("リクエストパラメータ受取不可");
                # エラー時の挙動-No.1 リクエストパラメータが取得できない
                $this->rsp_packet = base64_encode(openssl_random_pseudo_bytes( RANDOM_PACKET_LENGTH));
                goto resp_data;
            }
            $req_packet_data_ary = str_split(base64_decode($this->packet_data));

            #2-a.iv_allnet_auth = auth_server_challenge_packet[0, 16] を得ます
            $iv_allnet_auth = implode(array_slice($req_packet_data_ary,0,16));

            #2-b. cv_allnet_auth_1(16) = auth_server_challenge_packet[48, 16]
            # 及び raw(48) = aes_dec_cbc(key_allnet_auth, iv_allnet_auth, auth_server_challenge_packet[16, 48]) を得ます
            $cv_allnet_auth_1 = implode(array_slice($req_packet_data_ary,-16, 16));
            $data = implode(array_slice($req_packet_data_ary,16));
            $raw = parent::aes_dec_cbc($this->key_allnet_auth,$iv_allnet_auth, $data);
            if ( ! $raw ) {
                LogUtil::debugLog("サーバチャレンジパケット復号化失敗");
                # エラー時の挙動-No.2 ククライアント側で生成したパケットを復号化できない。
                $this->rsp_packet = base64_encode(openssl_random_pseudo_bytes( RANDOM_PACKET_LENGTH));
                goto resp_data;
            }

            #2-c. 属性タグ(1) = raw[0, 1], 機能コード(1) = raw[1, 1], keychip_id(11) = raw[2, 11],
            # iv_server_challenge(16) = raw[16, 16], auth_server_challenge(16) = raw[32, 16] を得ます
            $raw_ary = str_split($raw);
            $attribute_tag = implode(array_slice($raw_ary, 0,1));          // ヘッダ情報：属性タグ
            $function_code = implode(array_slice($raw_ary, 1,1));          // ヘッダ情報：機能コード
            $keychip_id = implode(array_slice($raw_ary,2,11));             // ヘッダ情報：キーチップID
            $iv_server_challenge = implode(array_slice($raw_ary,16,16));   // サーバ認証レスポンス
            $auth_server_challenge = implode(array_slice($raw_ary,32,16)); // キーチップ認証チャレンジ
            $resp_keychip_id = $keychip_id;                                            // ヘッダ情報(レスポンス用) :キーチップID（キーチップ生産情報取得失敗時は”00000000000"を設定する）

            #2-d. (2-c) での header の各値が想定外の値である等不正である場合は通信エラーのためログに残し、以下のサーバ内処理はスキップし、
            # auth_server_response_and_auth_keychip_challenge_packet(64) = rand(64) をキーチップに返却します。
            #この場合は通信を継続します
            // キーチップIDチェック　※keychip_idにバイナリデータが混在すると認証ログ登録時に問題を起こすので一番最初にチェックする事
            if ( ! preg_match("/^[A-HJ-NP-Z0-9]+$/",$keychip_id)  ) {
                LogUtil::debugLog("ヘッダ情報不正(キーチップIDが文字列以外)");
                $keychip_id = "00000000000";
                $this->rsp_packet = base64_encode(openssl_random_pseudo_bytes( RANDOM_PACKET_LENGTH));
                goto resp_data;
            }
            // タグ情報チェック
            if ( ( $attribute_tag != AL_TAG_AUTH ) || ($function_code != AL_AUTH_SERVER_CHALLENGE) ) {
                LogUtil::debugLog("ヘッダ情報不正");
                // エラー時の挙動-No.3 header の各値が想定外の値（タグ、機能コード）
                $this->rsp_packet = base64_encode(openssl_random_pseudo_bytes( RANDOM_PACKET_LENGTH));
                goto resp_data;
            }

            #2-e. auth_server_response(16) = aes_enc_cbc(key_server, iv_server_challenge, auth_server_challenge(16))
            # を生成します
            $keychip_product_info = KeychipProductInfo::getKeychipProductInfo($keychip_id);
            if ( count($keychip_product_info) == 0 ) {
                LogUtil::debugLog("キーチップ生産情報取得失敗");
                $keychip_product_info['key_server'] = bin2hex(openssl_random_pseudo_bytes(16));
                $keychip_product_info['key_keychip'] = bin2hex(openssl_random_pseudo_bytes(16));
                $this->result = AL_ERROR_AUTH;
            }

            $auth_server_response = parent::aes_enc_cbc(hex2bin($keychip_product_info['key_server']),$iv_server_challenge, $auth_server_challenge);
            if ( ! $auth_server_response ) {
                LogUtil::debugLog("暗号化失敗");
                // エラー時の挙動-No.9
                $this->result = AL_ERROR_AUTH;
                $auth_server_response = openssl_random_pseudo_bytes(16);
            }

            #2-h. header(16) = 属性タグAL_TAG_AUTH(1) || 機能コードAL_AUTH_SERVER_RESPONSE_AND_AUTH_KEYCHIP_CHALLENGE(1)
            # || keychip_id(11) || zero(3) を生成します
            $header = AL_TAG_AUTH.AL_AUTH_SERVER_RESPONSE_AND_AUTH_KEYCHIP_CHALLENGE.$resp_keychip_id
                .hex2bin("00").$this->result;

            #2-i. auth_server_response_and_auth_keychip_challenge_packet(64) = aes_enc_cbc(
            #     key_allnet_auth,
            #     cv_allnet_auth_1,
            #     header(16) || auth_server_response(16) || iv_keychip_challenge(16) || auth_keychip_challenge(16))
            # を生成します
            $data = $header.$auth_server_response.$iv_keychip_challenge.$auth_keychip_challenge;
            $data_aesenc = parent::aes_enc_cbc( $this->key_allnet_auth, $cv_allnet_auth_1, $data );
            $this->rsp_packet = base64_encode($data_aesenc);

            #2-j. cv_allnet_auth_2(16) = auth_server_response_and_auth_keychip_challenge_packet[48, 16]を
            #得ます（この値は保存しておきます）
            $cv_allnet_auth_2 = implode(array_slice(str_split($data_aesenc), -16, 16));
            $_SESSION['cv_allnet_auth_2'] = bin2hex($cv_allnet_auth_2);

            resp_data:

            #2-k. auth_server_response_and_auth_keychip_challenge_packet(64) をキーチップに返却します
            parent::send_resonse();

            # サーバチャレンジパケット処理結果を認証ログに登録
            $continue_check = AuthLog::continueCheck($keychip_id);
            AuthLog::authRequest(
                $this->auth_seq,
                $keychip_id,
                $continue_check,
                bin2hex($iv_keychip_challenge),
                bin2hex($auth_keychip_challenge),
                $this->request_data,
                $this->response
            );

        } catch( Exception $e ) {
            # エラー時の挙動-No.12 システムエラー
            http_response_code(500);
            LogUtil::errorLog($e);
        } finally {
            // DB解放
            DbUtil::untrace();
            DbUtil::closeConnect();
            // 終了メッセージをアプリケーションログに出力
            LogUtil::serverChallnegePacketReceiverApiLog("end ServerChallengePacketReceiverAPI:auth_seq="
                .$this->auth_seq.",response=".$this->response);
        }
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