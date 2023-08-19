<?php
namespace app\api;

require_once 'AuthPacketProc.php';
require_once '../utils/LogUtil.php';

use app\utils\LogUtil;

class KeychipResponsePacketReceiver extends AuthPacketProc
{

    public $request_data;

    function __construct()
    {
        parent::__construct();

        # リクエストデータ取得
        $this->request_data = file_get_contents( 'php://input' );
    }

    function doApi() {
        #4-a. cv_allnet_auth_3(16) = auth_keychip_response_packet[16, 16]
        # 及び raw(32) = aes_dec_cbc(key_allnet_auth, cv_allnet_auth, auth_keychip_response_packet(32)) を得ます
        if ( isset($_POST['packet_data']) ){
            $req_packet_data = base64_decode($_POST['packet_data']);
            $req_packet_data_ary = str_split($req_packet_data);
        } else {
            # エラー時の挙動-No.6 リクエストパラメータが取得できない
            $resp_attribute_tag = AL_AUTH_ERROR;
            goto rec_kc_resp;
        }

        $cv_allnet_auth_2 = $_SESSION['cv_allnet_auth_2'];
        $cv_allnet_auth_3 = array_slice($req_packet_data_ary,-16,16);
        $raw = parent::aes_dec_cbc($this->key_allnet_auth, hex2bin($cv_allnet_auth_2), $req_packet_data);
        if ( ! $raw ) {
            # エラー時の挙動-No.4 クライアント側で生成したパケットを復号化できない。
            $resp_attribute_tag = AL_AUTH_ERROR;
            goto rec_kc_resp;
        }

        #4-b. 属性タグ(1) = raw[0, 1], 機能コード(1) = raw[1, 1], keychip_id(11) = raw[2, 11], result(2) = raw[14, 2], auth_keychip_response(16) = raw[16, 16] を得ます
        $raw_ary = str_split($raw);
        $attribute_tag = implode(array_slice($raw_ary, 0,1)); // ヘッダ情報：属性タグ
        $function_code = implode(array_slice($raw_ary, 1,1)); // ヘッダ情報：機能コード
        $keychip_id = implode(array_slice($raw_ary,2,11));    // ヘッダ情報：キーチップID
        $result = implode(array_slice($raw_ary,14,2));        // 結果
        $auth_keychip_response = implode(array_slice($raw_ary,16)); // キーチップ認証レスポンス

        #4-c. (4-b) での header の各値が想定外の値である等不正である場合は通信エラーのためログに残し、以下のキーチップ認証処理は行わず認証失敗とし、(4-f) 以降の処理を行います
        if ( ( $attribute_tag != AL_TAG_AUTH ) && ( $function_code != AL_AUTH_KEYCHIP_RESPONSE ) ) {
            # エラー時の挙動-No.5 header の各値が想定外の値
            $resp_attribute_tag = AL_AUTH_ERROR;
            goto rec_kc_resp;
        }

        #4-d. result(2) が AL_ERROR_AUTH の場合、サーバ認証に失敗しているのでログに残します
        if ( $result == AL_ERROR_AUTH ) {
            # エラー時の挙動-No.7 サーバ認証が失敗
            $resp_attribute_tag = AL_AUTH_ERROR;
            goto rec_kc_resp;
        }

        #4-e. recv_keychip_response(16) = aes_dec_cbc(key_keychip, iv_keychip_challenge, auth_keychip_response(16)) を行い、
        #保存していた auth_keychip_challenge(16) と recv_keychip_response(16) が等しければキーチップ認証成功、等しくなければキーチップ認証失敗です
        $keychip_product_info = parent::get_keychip_product_info($keychip_id);
        $key_keychip = $keychip_product_info["key_keychip"];
        $iv_keychip_challenge = $_SESSION["iv_keychip_challenge"];
        $auth_keychip_challenge = $_SESSION["auth_keychip_challenge"];
        $recv_keychip_response = parent::aes_dec_cbc(hex2bin($key_keychip), hex2bin($iv_keychip_challenge), $auth_keychip_response);
        if ( ! $recv_keychip_response ) {
            # エラー時の挙動-No.4 クライアント側で生成したパケットを復号化できない。
            $resp_attribute_tag = AL_AUTH_ERROR;
        }

        rec_kc_resp:
        #4-f. キーチップ認証成功した場合、ALLNET_ERROR result(2) = 0, キーチップ認証失敗の場合 ALLNET_ERROR result(2) = AL_ERROR_AUTH とします
        if ( bin2hex($recv_keychip_response) == $auth_keychip_challenge ) {
            $result = 0;
        } else {
            $result = AL_ERROR_AUTH;
        }

        #4-g. この後サーバ-キーチップ認証成功の場合、オプショナルのリクエストパケットを返却、そうでなければサーバ-キーチップ認証の成否に関わらず
        #認証クライアント宛にサーバ署名を付加した認証結果を返却します。このデータはA7/AXPキーチップには渡されません
        // 署名付与
        $message = $this->create_message($result, $keychip_id );
        $verify_message = $this->add_sign($message,PRIVATE_KEY_FILE, CERTIFICATE_REQUEST_FILE, CMS_TEMP_DIR);
        if ( ! $verify_message ) {
            # エラー時の挙動-No.11 署名が作成できない
            http_response_code(500);
            exit();
        }

        // レスポンス
        $response_data = json_encode(array('packet_data' => "",'expires' => $message['limit'], 'verify_data' => "$verify_message"));
        header("Content-Type: application/json; charset=utf-8");
        echo $response_data;

        //output_auth_log("S", $request_data, $response_data, "");

    }

    function create_message($result, $keychip_id) {

        // 再認証時刻、有効期限の取得
        $now = new DateTime("now",new DateTimeZone('Asia/Tokyo'));
        $renewal = new DateTime($now->setTime(7,0,0)->format('Y-m-d H:i:s'),new DateTimeZone('Asia/Tokyo'));
        $limit = new DateTime($now->setTime(10,0,0)->format('Y-m-d H:i:s'),new DateTimeZone('Asia/Tokyo'));
        $now_sec = strtotime($now->format('Y-m-d H:i:s'));
        $renewal_sec = strtotime($renewal->format('Y-m-d H:i:s'));
        $limit_sec = strtotime($limit->format('Y-m-d H:i:s'));
        if ( strtotime($now->format('Y-m-d H:i:s')) > $renewal_sec ) {
            $renewal = new DateTime(date('Y-m-d H:i:s', strtotime("+1 day", $renewal_sec)), new DateTimeZone('Asia/Tokyo'));
            $limit = new DateTime(date('Y-m-d H:i:s', strtotime("+1 day", $limit_sec)), new DateTimeZone('Asia/Tokyo'));
        }
        $renewal->setTimezone(new DateTimeZone('UTC'));
        $limit->setTimezone(new DateTimeZone('UTC'));

        // 署名対象メッセージ生成
        $message['result'] = $result;   //認証の結果
        $message['key_id'] = $keychip_id;   //認証の対象のキーチップID
        $message['limit'] = $limit->format('Y-m-d\TH:i:s\Z');    //認証の結果の有効期限
        $message['renewal'] = $renewal->format('Y-m-d\TH:i:s\Z');  //再認証日時
        $message['serial'] = $this->getSerial();   //発番シリアル

        return $message;
    }

    function getSerial(){
        return 1;
    }

    function add_sign($message, $private_key_file, $certificate_file, $cms_tmp_dir ) {
        // TODO 署名付与は、PHP8.0のリリースまで仮実装　それまでは、opensslコマンドの外部実行で実装
        // 参考：openssl_cms_sign(string $infile, string $outfile, $signcert, $signkey, ?array $headers, int $flags = 0, int $encoding = OPENSSL_ENCODING_SMIME, ?string $extracertsfilename = null): bool {}
        // 署名データ作成用一時ファイル名
        $infile = $cms_tmp_dir."./in_".session_id();
        $outfile = $cms_tmp_dir."./out_".session_id();;

        // 署名対象ファイル作成
        $message_cms = json_encode($message);
        $fh = fopen($infile,"w");
        fwrite($fh, $message_cms);
        fclose($fh);

        // OPENSSLの署名コマンド生成
        $command = "openssl cms -sign -signer ".$certificate_file;
        $command .= " -inkey ".$private_key_file;
        $command .= " -binary -in ".$infile;
        $command .= " -outform pem -out ".$outfile;
        $command .= " -nodetach -md sha256 -nocerts -nosmimecap -noattr";

        // OPENSSL署名コマンドの実行
        exec($command);
        echo $command."\n";
        // 署名済みデータの取得
        $fh = fopen($outfile, "r");
        $verify_data = fread($fh, filesize($outfile));

        // 署名済みデータ返却
        return $verify_data;
    }

}