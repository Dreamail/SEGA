<?php
require_once 'ServerChallengePacketProc.php';
require_once 'KeychipResponsePacketProc.php';

session_start();

# 変数初期化
$resp_attribute_tag = AL_TAG_AUTH;

# リクエストデータ取得
$request_data = file_get_contents( 'php://input' );

if ( ! isset($_SESSION['iv_keychip_challenge']) && ! isset($_SESSION['auth_keychip_challenge'] ) ) {
    //iv_keychip_challenge、auth_keychip_challengeがセッションに設定されていない場合、認証サーバチャレンジパケットと判定
    $srv_challenge = new ServerChallengePacketProc();
    $srv_challenge->proc();

} else {
    //iv_keychip_challenge、auth_keychip_challengeがセッションに設定されている場合、キーチップレスポンスパケットと判定
    $key_response = new KeychipResponsePacketProc();
    $key_response->proc();

}
exit();

?>