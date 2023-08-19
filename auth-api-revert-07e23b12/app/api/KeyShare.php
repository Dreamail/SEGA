<?php
namespace app\api;

class KeyShare {

  function doApi() {
    # サーバ認証ファイル格納先取得
    if ( ! file_exists(PUBLIC_KEY_FILE) ) {
      error_response();
      exit();
    }

    # サーバ証明書ファイルより公開キーのリソースIDを取得
    $public_key = openssl_pkey_get_public(file_get_contents(PUBLIC_KEY_FILE));

    if( $public_key == false ) {
      # 公開キー取得失敗
      error_response();
      exit();
    }

    # サーバ証明書：公開キーのリソースIDから公開キーの値を取得
    # レスポンスBODY作成（JSONデータ）
    $public_key = openssl_pkey_get_details($public_key)["key"];
    $public_key = str_replace(array("\r\n","\r","\n"),'', $public_key);
    $public_key = str_replace('-----BEGIN PUBLIC KEY-----',"",$public_key);
    $public_key = str_replace('-----END PUBLIC KEY-----',"",$public_key);
    $response = json_encode(array('verify_key' => $public_key));

    # レスポンスデータ出力）
    header("Content-Type: application/json; charset=utf-8");
    echo $response;

    function error_response(){
      http_response_code(500);
    }

  }

}
?>
