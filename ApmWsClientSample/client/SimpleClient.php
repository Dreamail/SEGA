<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of JsonClient
 *
 * @author TsuboiY
 */
class SimpleClient {

    const DEFAULT_USER_AGENT = "ApmWsClientSample/1.0";

    const DEFAULT_CONTENT_TYPE = "text/plain";

    private $proxy;

    private $userAgent;

    public function __construct($proxy = null, $userAgent = self::DEFAULT_USER_AGENT) {
        $this->proxy = $proxy;
        $this->userAgent = $userAgent;
    }

    public function executeRequest($url, $body, $method, $contentType = self::DEFAULT_CONTENT_TYPE) {
        $header = self::createHeader($url, strlen($body), $this->userAgent, $contentType);
        $context = self::createContext($header, $body, $method);

        $res = file_get_contents($url, false, $context);
        if (!$res) {
            throw new Exception("通信に失敗");
        }

        return  $res;
    }

    private function createHeader($url, $length, $userAgent, $contentType) {
        $host = preg_replace("/\/.+$/", "", preg_replace("/^.+\:\/\//", "", $url));
        return array(
            sprintf("Host: %s", $host),
            sprintf("User-Agent: %s", $userAgent),
            sprintf("Content-Type: %s", $contentType),
            sprintf("Content-Length: %s", $length)
        );
    }

    private function createContext($header, $body, $method) {
        $httpContext = array(
                "method" => $method,
                "header" => implode("\r\n", $header)
        );
        if ($body != null) {
            $httpContext["content"] = $body;
        }
        if ($this->proxy != null) {
            $httpContext["proxy"] = $this->proxy;
            $httpContext["request_fulluri"] = true;
        }
        $context = array(
            "http" => $httpContext
        );
        return stream_context_create($context);
    }
}

/* END OF FILE SimpleClient.php */