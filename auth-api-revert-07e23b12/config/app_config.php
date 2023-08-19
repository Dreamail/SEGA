<?php
const TIMEZONE = 'Asia/Tokyo';

# サーバ証明書
// 公開キーファイル
const PUBLIC_KEY_FILE='C:/tmp/web_public_pem.key';
// 秘密キーファイル
const PRIVATE_KEY_FILE='C:/tmp/web_private_pem.key';
// 証明書署名要求ファイル
const CERTIFICATE_REQUEST_FILE='C:/tmp/web_server_pem.csr';

# 署名ファイル一時格納先
const CMS_TEMP_DIR='C:/apache/Apache24/htdocs/net/tmp';

# 暗号用共通キーファイル格納先
const KEY_ALLNET_AUTH_FILE='C:/work/key_allnet_auth.txt';
