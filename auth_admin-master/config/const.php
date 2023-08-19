<?php

return array(
    // フラグ
    'FLG' => array(
        'ON' => 1,
        'OFF' => 0,
    ),
    // イメージタイプ
    'APP' => 0, // アプリケーション
    'OPT' => 1, // オプション
    'IMAGE_TYPE_NUM' => array(
        '0' => 'APP',
        '1' => 'OPT',
    ),
    'IMAGE_TYPE_CHAR' => array(
        'APP' => '0',
        'OPT' => '1',
    ),

    //検索タイプ
    'VIEW_SEARCH_TYPE' => array(
        'PLACE' => 'place',
        'ALLNETID' => 'allnetid',
        'PLACEID' => 'placeid',
        'PHONE' => 'phone',
        'ROUTER' => 'router',
        'IPADDRESS' => 'ipaddress',
    ),

    //認証状況
    'STAT' => array(
        'OK' => '1',
        'NG_ALL' => '0',
        'NG_GAME' => '-1',
        'NG_SERIAL' => '-2',
        'NG_LOCATION' => '-3',
    ),

    // 国コード
    'COUNTY_CODE' => 'CHN',

    'ACTION' => array(
      'SEARCH' => 'search',
      'CSV_DL' => 'CsvDownload',
    ),

    'CSV_FILE_NAME' => array(
        '0' => 'appDeliverReports.csv',
        '1' => 'optDeliverReports.csv',
    ),

    'SEARCH_TYPE' => array(
        'SERIAL' => 'serial',
        'TITLE' => 'title',
        'PLACE' => 'place',
        'GROUP' => 'group',
    ),

    'GAME_DATA' => array(
        'FIX_INTERVAL' => '600',
        'FIX_LOG_CNT' => '200',
        'PLAY_LIMIT' => '1024',
        'NEAR_FULL' => '512',
        'CIPHER_KEY' => '6D.mUyKhS7OcAiYu',
    ),

    // すべて検索
    'ALL_SEARCH' => 'ALL',

    // MAX店舗ID
    'MAX_PLACE_ID' => 65535,

    // MAX ALL.Net id
    'MAX_ALLNET_ID' => 2147483647,

    // 変換前文字コード(店舗情報取込時に仕様)
    'BEFORE_CHARACTER_CODE' => 'UTF-8',
    // 変換後文字コード(店舗情報取込時に仕様)
    'AFTER_CHARACTER_CODE' => 'UTF-8',

    // 基板情報.グループ分け番号（登録時の固定値）
    'MACHINE_GROUP_INDEX' => 1,

    // 中国語（簡体字）対応
    'CN' => array(
        'BILLING_LOG_RESULT' => array(
            '0' => '仅未输出',
            '1' => '仅已输出',
            '2' => '全部',
        ),
    ),

    'CSV_FILE_PATH' => '/var/www/AllnetLite/Admin/csv_download/',
    'ANY_CSV_FILE_PATH' => '/var/www/AllnetLite/Admin/any_csv_download/',
//    'CSV_FILE_PATH' => 'D:/git/Lite/Admin/public/admin/csv_download/',
//    'ANY_CSV_FILE_PATH' => 'D:/git/Lite/Admin/public/admin/any_csv_download/',
    'LOCK_FILE_PATH' => '/var/www/AllnetLite/Admin/lock',
    'ANY_LOCK_FILE_PATH' => '/var/www/AllnetLite/Admin/any_lock',
//    'LOCK_FILE_PATH' => 'D:/git/Lite/Admin/public/admin/lock',
//    'ANY_LOCK_FILE_PATH' => 'D:/git/Lite/Admin/public/admin/any_lock',
    'LOCK_SAVE_TIME' => '3600',

    'DEFAULT_TIME_ZONE' => 'UTC',
    'CSV_TIME_ZONE' => 'Asia/Chungking',

    'GAME_AUTH_TYPE' => array(
        '1' => '通常認証',
        '2' => '自動認証',
        '3' => '店舗認証',
        '4' => '包括先自動認証',
        '5' => '包括先内自動認証・移設',
    ),

    'FIND_TYPE' => array(
        '0' => 'placeName',
        '1' => 'placeId',
        '2' => 'tel',
    ),

//    'ENV' => 'DEVELOPMENT',
//    'ENV' => 'STAGING',
    'ENV' => 'PRODUCTION',
);
