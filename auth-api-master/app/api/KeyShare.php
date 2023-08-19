<?php
namespace app\api;

use Exception;
use app\utils\LogUtil;
use Cassandra\Exception\ExecutionException;

class KeyShare {

  function __contruct() {

  }

  function doApi() {

    LogUtil::keyShareApiLog("KeyShareApi Start");

    try {

      # サーバ証明書ファイルより公開キーのリソースIDを取得
      $public_key = openssl_pkey_get_public(file_get_contents(PUBLIC_KEY_FILE));
      if( $public_key == false ) {
        throw new Exception();
      }

      # サーバ証明書：公開キーのリソースIDから公開キーの値を取得
      # レスポンスBODY作成（JSONデータ）
      $public_key = openssl_pkey_get_details($public_key)["key"];
      $public_key = str_replace(array("\r\n","\r","\n"),'', $public_key);
      $public_key = str_replace('-----BEGIN PUBLIC KEY-----',"",$public_key);
      $public_key = str_replace('-----END PUBLIC KEY-----',"",$public_key);
      $response = json_encode(array('verify_cert' => $public_key));

      # レスポンスデータ出力）
      header("Content-Type: application/json; charset=utf-8");
      echo $response;

    } catch ( Exception $e) {
      http_response_code(500);
      LogUtil::errorLog( $e );
    }

    LogUtil::keyShareApiLog("KeyShareApi End");

  }

}
?>
