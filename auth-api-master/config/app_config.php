<?php
const LOCAL_TIMEZONE = 'Asia/Tokyo';

# サーバ証明書
// 公開キーファイル
const PUBLIC_KEY_FILE='C:/tmp/web_public_pem.key';
// 秘密キーファイル
const PRIVATE_KEY_FILE='C:/tmp/web_private_pem.key';
// サーバ証明書ファイル
const CERTIFICATE_FILE='C:/tmp/web_server_pem.crt';

# 認証API用一時作業ディレクトリ
const AUTH_TEMP_DIR='C:/apache/Apache24/htdocs/net/tmp';

# 暗号用共通キーファイル格納先
const KEY_ALLNET_AUTH_FILE='C:/work/key_allnet_auth.txt';

// RANDOM_PACKET_LENGTH
// 異常終了時、正常終了と同じバイト数の乱数値でパケットデータを作成
// パケットデータバイト数（92Byte) = パケット本体（64Byte) +
//   パケット本体を暗号化した時のハッシュ値(16Byte) + パケット本体に含まれる項目を暗号化した時のハッシュ値（16Byte)
const RANDOM_PACKET_LENGTH = 64 + 16 + 16;
