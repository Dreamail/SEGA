<?php
/** 文字長：ゲームID */
const TITLE_ID_LENGTH = 5;
/** 文字長：ゲームVer */
const TITLE_VER_LENGTH = 5;
/** 文字長：基板シリアル */
const CLIENT_ID_LENGTH = 11;
/** MAX数値：ファームウェアバージョン、ブートROMバージョン */
const VERSION_MAX_LENGTH = 99999;
/** MIN数値：ファームウェアバージョン、ブートROMバージョン */
const VERSION_MIN_LENGTH = -99999;
/** 対象：応答文字エンコーディング */
const ENCODING_TARGET = array('SHIFT-JIS', 'EUC-JP', 'UTF-8');
/** 対象：フォーマットバージョン */
const FORMAT_VERSION = 5;

/** ALL.Net Slim用ルータ種別属性 */
const ALLNET_ROUTER_TYPE_ATTR = '1'; // 従来のALL.Net認証用
const LITE_ROUTER_TYPE_ATTR = '2'; // Lite用
const SLIM_ROUTER_TYPE_ATTR = '3'; // Slim用

/** ステータスresult：成功 */
const STAT_RESULT_SUCCESS = 1;
/** ステータスresult：ゲーム情報エラー */
const STAT_RESULT_FAIL_GAME = -1;
/** ステータスresult：基板情報エラー */
const STAT_RESULT_FAIL_MACHINE = -2;
/** ステータスresult：その他エラー */
const STAT_RESULT_FAIL_LOC = -3;

/** ステータスcause：成功 */
const STAT_CAUSE_SUCCESS = 1;
/** ステータスcause：ゲーム情報エラー */
const STAT_CAUSE_FAIL_GAME = -3;
/** ステータスcause：基板情報エラー */
const STAT_CAUSE_FAIL_MACHINE = -1004;
/** ステータスcause：店舗エラー */
const STAT_CAUSE_FAIL_PLACE = -1005;
/** ステータスcause：店舗IPエラー */
const STAT_CAUSE_FAIL_PLACE_IP = -1009;

/** レスポンスバージョン */
const RES_VER_NU_FORMAT_VER = 5;

/** ゲーム認証方式：デバッグ */
const GAME_AUTH_DEBUG = array(1);
/** ゲーム認証方式：自動認証を許可 */
const GAME_AUTH_AUTO_ALLOWED = array(2, 3, 4, 5);
/** ゲーム認証方式：移設を許可 */
const GAME_AUTH_MOVE_ALLOWED = array(4, 5);

/** 基板情報の通信ステータス：通信を許可 */
const COMMUNICATION_SETTING_ALLOWED = 1;

/** DB登録者ID */
const DB_CREATE_USER_ID = 'PowenOn';
/** DB更新者ID */
const DB_UPDATE_USER_ID = 'PowenOn';

/** レスポンス現在時刻のフォーマット（年月日） */
const RESPONSE_DATE_FORMAT = 'Y-m-d';
/** レスポンス現在時刻のフォーマット（時分秒） */
const RESPONSE_TIME_FORMAT = 'H:i:s';

/** 配信種別：アプリケーションイメージ */
const DOWNLOAD_ORDER_IMAGE_TYPE_APP = 0;
/** 配信種別：オプションイメージ */
const DOWNLOAD_ORDER_IMAGE_TYPE_OPT = 1;

/** 暗号メソッド */
const CIPHER_METHODS = 'aes-128-cbc'; // AES-128 CBCモード
/** iv文字長 */
const CIPHER_IV_LENGTH = 16;

/** ホップ数 */
const HOPS = -1;

/** フォーマットバージョン */
const FORMAT_VER = 1.00;

/** 応答文字エンコーディング */
const ENCODE = 'EUC-JP';
