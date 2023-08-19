<?php
/** ヘッダー内情報 */
const STR_ENCODED = 'DFI';
/** IP */
const ROUTER_IP_END_STRING = '254';
/** 文字長：ゲームID */
const GAME_ID_LENGTH = 5;
/** 文字長：ゲームVer */
const VER_LENGTH = 5;
/** MIN：ゲームVer */
const VER_MIN = 0;
/** MAX：ゲームVer */
const VER_MAX = 65.54;
/** 文字長：基板シリアル */
const SERIAL_LENGTH = 11;
/** MAX数値：ファームウェアバージョン、ブートROMバージョン */
const VERSION_MAX_LENGTH = 99999;
/** MIN数値：ファームウェアバージョン、ブートROMバージョン */
const VERSION_MIN_LENGTH = -99999;
/** 対象：応答文字エンコーディング */
const ENCODING_TARGET = array('SHIFT-JIS', 'SHIFT_JIS' , 'SJIS' , 'EUC-JP', 'EUC_JP' , 'EUCJP' , 'UTF-8', 'UTF_8' , 'UTF8' , 'UTF-16' , 'UTF_16' , 'UTF16');

/** 対象：フォーマットバージョン */
const FORMAT_VERSION = 6;
/** 請求先コード 長さ */
const BILL_CODE_LENGTH = 9;

const COMP_CODE_LENGTH = 6;

/** ALL.Net Slim用ルータ種別属性 */
const ALLNET_ROUTER_TYPE_ATTR = '1'; // 従来のALL.Net認証用
const LITE_ROUTER_TYPE_ATTR = '2'; // Lite用
const SLIM_ROUTER_TYPE_ATTR = '3'; // Slim用

/** ステータスresult：成功 */
const STAT_RESULT_SUCCESS = 1;
/** ステータスresult：ゲーム情報エラー */
const STAT_RESULT_FAIL_GAME = -1;
/** ステータスresult：基板情報エラー */
const STAT_RESULT_FAIL_SELIAL = -2;
/** ステータスresult：その他エラー */
const STAT_RESULT_FAIL_LOC = -3;
/** ステータスresult：署名エラー */
const STAT_RESULT_FAIL_SIGNATURE = -4;

/** ステータスcause：成功 */
const STAT_CAUSE_SUCCESS = 1;
/** ステータスcause：ルータ情報エラー */
const STAT_CAUSE_FAIL_ROUTER = -1;
/** ステータスcause：ゲーム情報エラー */
const STAT_CAUSE_FAIL_GAME = -3;
/** ステータスcause：署名エラー */
const STAT_CAUSE_FAIL_SIGNATURE = -4;
/** ステータスcause：基板情報エラー */
const STAT_CAUSE_FAIL_MACHINE = -1004;
/** ステータスcause：店舗エラー */
const STAT_CAUSE_FAIL_PLACE = -1005;

/** レスポンスバージョン */
const RES_VER_NU_FORMAT_VER = 3;

/** ゲーム認証方式：デバッグ */
const GAME_AUTH_DEBUG = -1;
/** ゲーム認証方式：自動認証を許可 */
const GAME_AUTH_AUTO_ALLOWED = array(2, 3, 4, 5);
/** ゲーム認証方式：移設を許可 */
const GAME_AUTH_MOVE_ALLOWED = array(4, 5);

/** ゲーム認証方式 */
const AUTH_TYPE_NORMAL = 1;
const AUTH_TYPE_AUTO = 2;
const AUTH_TYPE_PLACE_AUTO = 3;
const AUTH_TYPE_COMP_AUTO_MOVE = 4;
const AUTH_TYPE_AUTO_MOVE = 5;
const AUTH_TYPE_DEBUG = -1;

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

/** デフォルトINDEX */
const DEFAULT_INDEX = 1;

/** 署名検証で使用 */
const CMS_START = '-----BEGIN CMS-----';
const CMS_END = '-----END CMS-----';

/** 認証結果 ：成功 */
const AUTH_SUCCESS = 0;

/** 認証結果項目 */
const AUTH_ITEMS = array('result', 'key_id', 'renewal', 'limit',' serial');

const CONNECT_CHAR = '&';

/** 配信レポート用 */
/** 返却値：OK */
const RESULT_BODY_OK = 'OK';
/** 返却値：NG */
const RESULT_BODY_NG = 'NG';

/** DB登録者ID */
const DB_REPORT_CREATE_USER_ID = 'report';
/** DB更新者ID */
const DB_REPORT_UPDATE_USER_ID = 'report';

/** アプリケーションイメージタイプ */
const IMAGE_TYPE_APP = 'appimage';
/** オプションイメージタイプ */
const IMAGE_TYPE_OPT = 'optimage';

const EMPTY_MESSAGE = ' is invalid value.';
const CALENDER_MAX_DATE = 253402300799;
const CALENDER_DEFAULT_DATE = -62135596800;
const CONVERT_MILLI_TO_SECOND = 1000;
const AUTH_STATE_FAILED = 1;
const AUTH_STATE_SUCCESS = 2;