<?php
require_once 'SimpleClient.php';

/**
 * Description of JsonClient
 *
 * @author TsuboiY
 */
class JsonClient extends SimpleClient {

    const DEFAULT_CONTENT_TYPE = "application/json";

    const DEFAULT_HTTP_METHOD = "POST";

    public function executeRequest($url, $param, $method = self::DEFAULT_HTTP_METHOD, $contentType = self::DEFAULT_CONTENT_TYPE) {
        $encoded = json_encode($param);

        $res = parent::executeRequest($url, $encoded, $method, $contentType);

        $decoded = json_decode($res);
        if ($decoded == null) {
            throw new Exception("レスポンスJSONの解析に失敗");
        }

        return  $decoded;
    }
}

/* END OF FILE JsonClient.php */
