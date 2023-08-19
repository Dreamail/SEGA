<?php
### 定数定義
//ALLNET_TAG
define("AL_TAG_AUTH",hex2bin("D0"));

//ALLNET_FUNCTION_CODE
define("AL_AUTH_SERVER_CHALLENGE",hex2bin("01"));
define("AL_AUTH_SERVER_RESPONSE_AND_AUTH_KEYCHIP_CHALLENGE",hex2bin("02"));
define("AL_AUTH_KEYCHIP_RESPONSE",hex2bin("03"));
define("AL_AUTH_ERROR",hex2bin("04"));

//ALLNET_ERROR
define("AL_ERROR_TAG",hex2bin("0001"));
define("AL_ERROR_FUNCTION_CODE",hex2bin("0002"));
define("AL_ERROR_AUTH",hex2bin("0004"));

?>