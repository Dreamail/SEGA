<?php

return array(
    // CSV取込時の画面出力文言
    'CSV_SUCCESS' => '正常に登録・更新が完了しました。',
    'CSV_ERROR' => 'CSV取込時にエラーが発生しました。',

    // チェック時のエラーメッセージ一覧
    'CSV_FORMAT_ERROR' => 'CSVのフォーマットが不正です。',
    //'TITLEID_ERROR' => '游戏ID的格式不正确。',
    'TITLEID_CHOICE_ERROR' => 'ゲームIDが画面で選択したゲームIDと異なります。',
    'TITLEVER_ERROR' => 'ゲームVerに数値以外か範囲外の数値が入力されています。',
    'LIMIT_TITLEVER_ERROR' => '上限ゲームVerに数値以外か範囲外の数値が入力されています。',
    'LIMIT_DECIMAL_ERROR' => '上限ゲームVerは小数点第2位まで入力可能です。',
    'TITLE_ERROR' => 'ゲームタイトルが空または33文字以上が入力されています。',
    'TITLE_URI_ERROR' => 'タイトルサーバURIが空または129文字以上が入力されています。',
    'TITLE_URI_FORMAT_ERROR' => 'タイトルサーバURIのURIフォーマットが不正です。',
    'TITLE_HOST_ERROR' => 'タイトルサーバホストが空または129文字以上が入力されています。',
    'AUTH_ERROR' => '認証方式に「-1」、「0」、「1」以外の値が入力されています。',
    'CLIENTID_ERROR' => 'シリアルのフォーマットが不正です。',
    'TYPE_ERROR' => 'イメージタイプに不正な値が選択されました。',
    'URI_ERROR' => '配信指示書URIのURIフォーマットが不正です。',
    'REGISTERTYPE_ERROR' => '登録情報が不正です。',
    'USERID_ERROR' => 'ユーザIDのフォーマットが不正です。',
    'ROLE_ID_ERROR' => 'ロールIDは英数字16文字以内で入力してください。',
    'PASSWORD_ERROR' => 'パスワードのフォーマットが不正です。',
    'USERID_MAX_ERROR' => 'ユーザIDは16文字以内で入力してください。',
    'PASSWORD_MAX_ERROR' => 'パスワードは16文字以内で入力してください。',
    'ROLE_ID_MAX_ERROR' => 'ロールIDは80文字以内で入力してください。',
    'EXPLANATION_MAX_ERROR' => '説明は200文字以内で入力してください。',
    'TITLE_CHARACTER_LIMIT_ERROR' => 'タイトル名が33文字以上入力されています。',
    'URI_CHARACTER_LIMIT_ERROR' => 'タイトルURIが129文字以上入力されています。',
    'HOST_CHARACTER_LIMIT_ERROR' => 'タイトルホストが129文字以上入力されています。',
    'URI_FORMAT_ERROR' => 'タイトルURIのフォーマットが不正です。',


    'TITLE_EXI_ERROR' => 'ゲームID：$1は存在しません。',
    'PLACE_EXI_ERROR' => '店舗ID：$1は存在しません。',
    'CLIENT_EXI_ERROR' => 'シリアル：$1は存在しません。',

    'USER_EXI_ERROR' => 'ユーザ情報が存在しません。凍結または削除された可能性があります。',
    'ROLE_USER_ERROR' => '削除対象のロールはユーザ情報に紐づいているため削除できません。',

    'PLACE_EXIST_ERROR' => '店舗ID：inputは存在しません。',
    'ALLNETID_EXIST_ERROR' => 'ALL.Net ID：inputは存在しません。',
    'TITLE_EXIST_ERROR' => 'ゲームID：:inputは存在しません。',
    'CLIENT_EXIST_ERROR' => 'シリアル：:inputは存在しません。',

    'USER_EXIST_ERROR' => 'ユーザID：:inputは既に存在します。',

    'REQUIRED' => array(
        'PLACEID' => '店舗IDは必須項目です。',
        'TITLEID' => 'ゲームIDは必須項目です。',
        'TITLEVER' => 'ゲームVerは必須項目です。',
        'LIMIT_TITLEVER' => '上限ゲームVerは必須項目です',
        'SERIAL' => 'シリアルは必須項目です。',
        'TYPE' => 'イメージタイプは必須項目です。',
        'URI' => '配信指示書URIは必須項目です。',
        'FILE' => 'ファイルを選択してください。',
        'ROLE' => 'ロールIDが未入力です。',
        'EXPLANATION' => '説明は必須項目です。',
        'TITLE' => 'タイトル名は必須項目です。',
        'TITLE_URI' => 'タイトルサーバURIは必須項目です。',
        'TITLE_HOST' => 'タイトルサーバホストは必須項目です。',
        'USERID' => 'ユーザIDは必須項目です。',
        'PASSWORD' => 'パスワードは必須項目です。',
        'ROLEID' => 'ロールは必須項目です。',
        'DEFAULTGAME' => 'ゲーム権限は必須項目です。',
        'TITLEID_LIST' => 'ゲーム権限でゲームIDを指定を選択している場合、ゲームIDは必須です。',
        'ROLE_NAME' => 'ロール名は必須項目です。',
        'AUTH_SET_IDS' => '操作権限は最低１つにチェックを入れてください。',
        'SETTING' => '通信設定は必須項目です。',
    ),
    'LOGIN_ERROR' => 'ログインに失敗しました。ログインIDとパスワードを確認してください。',
    'REQUIRED_WITHOUT_ALL' => '必須項目は１つ以上指定してください。',
    'ALLNETID_ERROR' => 'ALL.Net IDに数値以外か範囲外の数値が入力されています。',
    'PLACEID_ERROR' => '店舗IDのフォーマットが不正です。',
    'DATE_ERROR' => '日付フォーマットが不正です。',
    'TIME_ERROR' => '時刻に数値以外か範囲外の数値が入力されています。',
    'BILLING_LOG_RESULT_ERROR' => '出力対象（オプション）の選択値が不正です。',
    'DELFLG_ERROR' => '削除フラグが不正です。',

    // データ管理機能
    'PLACEID_ERROR' => '店舗IDのフォーマットが不正です。',
    'NAME_ERROR' => '店舗名に半角カナは入力できません。',
    'TEL_ERROR' => '電話番号のフォーマットが不正です。',
    'ADDRESS_ERROR' => '住所のフォーマットが不正です。',
    'STATION_ERROR' => 'アクセス情報のフォーマットが不正です。',
    'SPECIALINFO_ERROR' => 'PR文のフォーマットが不正です。',
    'NICKNAME_ERROR' => 'ニックネームに半角カナは入力できません。',
    'REGION0_ERROR' => '地域名0はデータが存在しません。',
    'LOCATIONTYPE_ERROR' => '店舗タイプのフォーマットが不正です。',
    'GROUPINDEX_ERROR' => 'グループ分け番号のフォーマットが不正です。',
    'SETTING_ERROR' => '通信セッティングのフォーマットが不正です。',
    'DELETIONREASONNO_ERROR' => '削除理由番号のフォーマットが不正です。',
    'NAME_DIFFER_ERROR' => '店舗名が不正です。(ALL.Net IDに紐づいた店舗名と一致しておりません。)',

    // 共通エラー
    'SYSTEM_ERROR' => 'システムエラーが発生しました',

    // 以下、画面に表示する項目やボタンなどの文言パーツ
    'SEARCH' => '検索',
    'CLEAR' => 'クリア',
    'CSV_DL' => 'CSVダウンロード',
    'REGIST' => '登録',
    'UPDATE' => '更新',
    'RETURN' => '戻る',
    'DELETE' => '削除',
    'EDIT' => '編集',
    'UPLOAD' => 'アップロード',
    'DOWNLOAD' => 'ダウンロード',

    // イメージタイプ
    'IMAGE_TYPE' => 'イメージタイプ',
    'TYPE_APP' => 'アプリケーション',
    'TYPE_OPT' => 'オプション',

    // 検索タイプ
    'SEARCH_TYPE' => '検索タイプ',
    'TYPE_PLACE' => '店舗名',
    'TYPE_ALLNETID' => 'ALL.Net ID',
    'TYPE_PLACEID' => '店舗ID',
    'TYPE_PHONE' => '電話番号',
    'TYPE_ROUTER' => 'ルータID',
    'TYPE_IPADDRESS' => 'IPアドレス',

    //認証状況
    'STAT_OK' => '認証OK',
    'STAT_NG_ALL' => '認証NG(すべて)',
    'STAT_NG_GAME' => '認証NG(-1:ゲーム情報に関するNG)',
    'STAT_NG_SERIAL' => '認証NG(-2:基板シリアル情報に関するNG)',
    'STAT_NG_LOCATION' => '認証NG(-3:ロケーション情報に関するNG)',

    // CSVアップロード
    'CSV_TITLEID_ERROR' => 'ゲームIDが選択したゲームIDと異なっています。',

    'ROLE_ERROR' => '対象ページを表示することはできません。',
    'FRAUD_ERROR' => '不正な操作が行われました。',//操作并不正确。
    'EXCEPTION' => '予期せぬエラーが発生しました。',

    // 中国語（簡体字）対応
    'CN' => array(
        'CSV_SUCCESS' => '正常に登録・更新が完了しました。', // 注册・更新正常完成。
        'CSV_ERROR' => 'CSV取込時にエラーが発生しました。', // 导入CSV时发生错误。

        'FILE' => 'ファイルを選択してください。', // 请选择文件。
        'CSV_FORMAT_ERROR' => 'CSVのフォーマットが不正です。', // CSV格式不正确。
        'NAME_ERROR' => '店舗名に半角カナは入力できません。', // 店铺名不能使用半角输入。
        'NAME_FORMAT_ERROR' => '店舗名のフォーマットが不正です。', // 店铺名不的格式不正确。
        'PLACEID_ERROR' => '店舗IDのフォーマットが不正です。', // 店铺ID的格式不正确。
        'LOCATIONTYPE_ERROR' => '店舗タイプのフォーマットが不正です。', // 店铺类型的格式不正确。
        'TEL_ERROR' => '電話番号のフォーマットが不正です。', // 电话号码的格式不正确。
        'ADDRESS_ERROR' => '住所のフォーマットが不正です。', // 地址的格式不正确。
        'STATION_ERROR' => 'アクセス情報のフォーマットが不正です。', // 链接信息的格式不正确。
        'SPECIALINFO_ERROR' => 'PR文のフォーマットが不正です。', // PR文的格式不正确。
        'REGION0_ERROR' => '地域名0はデータが存在しません。', // 地区名0的数据不存在。
        'NICKNAME_ERROR' => 'ニックネームに半角カナは入力できません。', // 昵称不能使用半角输入。
        'NICKNAME_FORMAT_ERROR' => 'ニックネームのフォーマットが不正です。', // 昵称不的格式不正确。
        'TITLEID_REQUIRED_ERROR' => 'ゲームIDを選択してください。', // 请选择游戏ID。
        'TITLEID_ERROR' => 'ゲームIDのフォーマットが不正です。', // 游戏ID的格式不正确。
        'CLIENTID_ERROR' => 'シリアルのフォーマットが不正です。', // '串行的格式不正确。',
        'SETTING_ERROR' => '通信セッティングのフォーマットが不正です。', // '通信设定的格式不正确。',
        'SEARCH_TYPE_REQUIRED_ERROR' => '検索タイプを選択してください。', // 请选择搜索类型。
        'DAY_ERROR' => '日数の値が不正です。', // 天数的值不正确。
        'REQUIRED_WITHOUT_ALL' => '必須項目は１つ以上指定してください。', // '请指定1个以上的必须项目。',
        'DATE_ERROR' => '日付フォーマットが不正です。', // '日期的格式不正确。',
        'TIME_ERROR' => '時刻に数値以外か範囲外の数値が入力されています。', // '输入的时刻是数值以外或范围外的数值。',
        'BILLING_LOG_RESULT_ERROR' => '出力対象（オプション）の選択値が不正です。', // '输出对象（选项）的选择值不正确。',
        'NAME_DIFFER_ERROR' => '店舗名が不正です。(店舗IDから取得した店舗名と異なっています。)', // '店铺名不正确。(与店铺ID的店铺名不一致。)',
        'DELFLG_ERROR' => '削除フラグが不正です。', // '删除标记不正确。',
        'LOCK_FILE_ERROR' => '処理中です。', // '正在处理中。',
        'UPDATE_FORM' => '処理中です。', // '上传表格。',



        'NAME_MAX_ERROR' => '店舗名は80文字以内で入力してください。',
        'NICKNAME_MAX_ERROR' => 'ニックネームは56文字以内で入力してください。',

        'NAME_REQUIRED' => '店舗名は必須項目です。',
        'PLACE_TYPE_REQUIRED' => '店舗タイプは必須項目です。',
        'NICKNAM_REQUIRED' => 'ニックネームは必須項目です。',
        'REGION_REQUIRED' => '地域名は必須項目です。',
    ),


    'TITLEID_REQUIRED_ERROR' => 'ゲームIDを選択してください。',
    'SEARCH_TYPE_REQUIRED_ERROR' => '検索タイプを選択してください。',
    'DAY_ERROR' => '日数の値が不正です。',

    'ALLNET_ID_ERROR' => 'ALL.NetIDのフォーマットが不正です。',
    'NAME_FORMAT_ERROR' => '店舗名のフォーマットが不正です。',
    'MASHINEID_ERROR' => 'シリアルのフォーマットが不正です。',
    'TITLEID_ERROR' => 'ゲームIDのフォーマットが不正です。',

    'REQUIRED_TITLEID' => 'ゲームIDは必須項目です。',
    'REQUIRED_MACHINEID' => 'シリアルは必須項目です。',
    'REQUIRED_ALLNETID' => 'ALL.NetIDは必須項目です。',
    'REQUIRED_PLACEIP' => '店舗IPは必須項目です。',
    'ALLNETID_EXIST_ERROR' => 'ALL.NetID：:inputは存在しません。',
    'TITLEID_EXIST_ERROR' => 'ゲームID：:inputは存在しません。',
    'REQUIRED_SETTING' => '通信セッティングは必須項目です。',

    'MACHINEID_EXIST_ERROR' => 'シリアル：:inputは存在しません。',
    'PLACEIP_ERROR' => '店舗IPのフォーマットが不正です。',
    'GAMEID_DIFFER_ERROR' => '選択したゲームIDとCSV内のゲームIDが一致しません。',
    'GAMEID_NOT_FOUND_ERROR' => 'シリアルに紐づくゲームIDが存在しません。',

);
