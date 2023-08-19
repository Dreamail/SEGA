<?php
namespace app\api;

require_once 'AuthPacketProc.php';

class ServerChallengePacketReceiver extends AuthPacketProc

{

    public $request_data;

    function __construct()
    {
        parent::__construct();

        # リクエストデータ取得
        $this->request_data = file_get_contents( 'php://input' );
    }

    function doApi() {

        if ( ! isset($_POST['packet_data']) ){
            # エラー時の挙動-No.1 リクエストパラメータが取得できない
            $this->rsp_packet = openssl_random_pseudo_bytes( 64 + 16);
            goto resp_data;
        }
        $rcv_packet_data = base64_decode($_POST['packet_data']);
        $req_packet_data_ary = str_split($rcv_packet_data);
        $iv_allnet_auth = implode(array_slice($req_packet_data_ary,0,16));

        #2-b. cv_allnet_auth_1(16) = auth_server_challenge_packet[48, 16]
        # 及び raw(48) = aes_dec_cbc(key_allnet_auth, iv_allnet_auth, auth_server_challenge_packet[16, 48]) を得ます
        $cv_allnet_auth_1 = implode(array_slice($req_packet_data_ary,-16, 16));
        $data = implode(array_slice($req_packet_data_ary,16));
        $raw = parent::aes_dec_cbc($this->key_allnet_auth,$iv_allnet_auth, $data);
        if ( ! $raw ) {
            # エラー時の挙動-No.2 ククライアント側で生成したパケットを復号化できない。
            $this->rsp_packet = openssl_random_pseudo_bytes( 64 + 16);
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
        $resp_keychip_id = $keychip_id;                                            // ヘッダ情報(レスポンス用) : キーチップID（キーチップ生産情報取得失敗時は”00000000000"を設定する）

        #2-d. (2-c) での header の各値が想定外の値である等不正である場合は通信エラーのためログに残し、以下のサーバ内処理はスキップし、
        # auth_server_response_and_auth_keychip_challenge_packet(64) = rand(64) をキーチップに返却します。この場合は通信を継続します
        if ( ( $attribute_tag != AL_TAG_AUTH ) && ($function_code != AL_AUTH_KEYCHIP_RESPONSE) ) {
            // エラー時の挙動-No.3 header の各値が想定外の値（タグ、機能コード）
            $this->rsp_packet = openssl_random_pseudo_bytes( 64 + 16);
            goto resp_data;
        }

        #2-e. auth_server_response(16) = aes_enc_cbc(key_server, iv_server_challenge, auth_server_challenge(16)) を生成します
        try {
            $keychip_product_info = parent::get_keychip_product_info($keychip_id);
        } catch( Exception $e ) {
            $keychip_product_info['key_server'] = openssl_random_pseudo_bytes(16);
            $keychip_product_info['key_keychip'] = openssl_random_pseudo_bytes(16);
            $resp_keychip_id = "00000000000";
            $this->resp_attribute_tag = AL_AUTH_ERROR;
        }

        $auth_server_response = parent::aes_enc_cbc($keychip_product_info['key_server'],$iv_server_challenge, $auth_server_challenge);
        if ( ! $auth_server_response ) {
            // エラー時の挙動-No.9
            $this->resp_attribute_tag = AL_AUTH_ERROR;
            $auth_server_response = openssl_random_pseudo_bytes(16);
        }

        #2-f. iv_keychip_challenge = rand(16) を生成します（この値は保存しておきます
        $iv_keychip_challenge = openssl_random_pseudo_bytes(16);
        $_SESSION['iv_keychip_challenge'] = bin2hex($iv_keychip_challenge);

        #2-g. auth_keychip_challenge = rand(16) を生成します（この値は保存しておきます
        $auth_keychip_challenge = openssl_random_pseudo_bytes(16);
        $_SESSION['auth_keychip_challenge'] = bin2hex($auth_keychip_challenge);

        #2-h. header(16) = 属性タグAL_TAG_AUTH(1) || 機能コードAL_AUTH_SERVER_RESPONSE_AND_AUTH_KEYCHIP_CHALLENGE(1) || keychip_id(11) || zero(3) を生成します
        $header = $this->resp_attribute_tag.AL_AUTH_SERVER_RESPONSE_AND_AUTH_KEYCHIP_CHALLENGE.$resp_keychip_id.hex2bin("000000");

        #2-i. auth_server_response_and_auth_keychip_challenge_packet(64) = aes_enc_cbc(
        #     key_allnet_auth,
        #     cv_allnet_auth_1,
        #     header(16) || auth_server_response(16) || iv_keychip_challenge(16) || auth_keychip_challenge(16)) を生成します
        $data = $header.$auth_server_response.$iv_keychip_challenge.$auth_keychip_challenge;
        $this->rsp_packet = parent::aes_enc_cbc( $this->key_allnet_auth, $cv_allnet_auth_1, $data );

        #2-j. cv_allnet_auth_2(16) = auth_server_response_and_auth_keychip_challenge_packet[48, 16]を得ます（この値は保存しておきます）
        $cv_allnet_auth_2 = implode(array_slice(str_split($this->rsp_packet), -16, 16));
        $_SESSION['cv_allnet_auth_2'] = bin2hex($cv_allnet_auth_2);

        resp_data:
        #2-k. auth_server_response_and_auth_keychip_challenge_packet(64) をキーチップに返却します
        parent::send_resonse();

    }

}