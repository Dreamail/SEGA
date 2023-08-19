<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

//Route::get('/', function () {
//    return view('welcome');
//});

/* Login */
Route::get('login', function () {
    return view('admin/login');
});
Route::post('login', 'LoginController@login');
Route::get('logout', 'LoginController@logout');

/* TOP */
Route::match(['get', 'post'],'top', function () {
    return view('admin/admin');
})->middleware('user');

/* ログ管理 */
// 認証ログ
Route::get('auth/log', 'AuthLogController@top')->middleware('user:150_REF_LOG_ADV');

// 認証ログ CSVダウンロード
Route::match(['get', 'post'],'log/csv/auth/export', 'AuthLogController@data_search')->middleware('user:150_REF_LOG_ADV');
Route::get('log/auth/explain', function () {
    return view('admin/log/explain/authexplain');
});

/* データ検索 */
// ゲーム情報
Route::get('view/game', 'ViewGameController@view_game')->middleware('user:200_REF_GAM');
// 検索
Route::get('view/game/search', 'ViewGameController@data_search')->middleware('user:200_REF_GAM');

// 店舗情報
Route::get('view/place', 'ViewPlaceController@view_place')->middleware('user:300_REF_PLC');
// 検索
Route::get('view/place/search', 'ViewPlaceController@data_search')->middleware('user:300_REF_PLC');

// ルータ情報
Route::get('view/router', 'ViewRouterController@view_router')->middleware('user:300_REF_PLC');
// 検索
Route::get('view/router/search', 'ViewRouterController@data_search')->middleware('user:300_REF_PLC');

// 基板情報
Route::get('view/machine', 'ViewGameController@view_game')->middleware('user:400_REF_MCN');
// 検索
//Route::get('view/machine/search', 'ViewGameController@data_search')->middleware('user:400_REF_MCN');

// 基板情報
Route::get('manage/reference/machine', 'ReferenceMachineController@view')->middleware('user:430_MAC_OPE');
Route::match(['get', 'post'],'manage/reference/machine/find', 'ReferenceMachineController@find')->middleware('user:430_MAC_OPE');
Route::match(['get', 'post'],'manage/reference/machine/update', 'ReferenceMachineController@update')->middleware('user:430_MAC_OPE');
Route::post('manage/reference/machine/update/confirm', 'ReferenceMachineController@upd_confirm')->middleware('user:430_MAC_OPE');
Route::post('manage/reference/machine/update/process', 'ReferenceMachineController@upd_process')->middleware('user:430_MAC_OPE');
Route::post('manage/reference/machine/delete', 'ReferenceMachineController@delete')->middleware('user:430_MAC_OPE');

// CSV登録・更新・削除
Route::get('manage/csv/upload/machine', 'CsvUploadController@view_machine')->middleware('user:430_MAC_OPE');
Route::get('manage/csv/upload/group', 'CsvUploadController@view_group')->middleware('user:440_GRO_OPE');
Route::post('manage/csv/upload/machine', 'MachineImportController@machine_import')->middleware('user:430_MAC_OPE');
Route::get('manage/csv/upload/machine/explain', function () {
    return view('admin/upload/explain/machine');
});

// CSVデータダウンロード
Route::get('manage/csv/download/machine', 'CsvDownloadController@view_machine')->middleware('user:430_MAC_OPE,435_MAC_REF');
Route::post('download/export/machine', 'CsvDownloadController@export_machine')->middleware('user:430_MAC_OPE,435_MAC_REF');
Route::get('manage/csv/download/machine/explain', function () {
    return view('admin/download/explain/machine');
});

/* 配信指示設定 */
Route::get('order/register', 'OrderViewController@view_register')->middleware('user:200_OPE_DLO');
Route::get('order/config', 'OrderViewController@view_config')->middleware('user:200_OPE_DLO');

// 配信指示書登録
Route::post('order/register/game/confirm', 'OrderRegisterController@confirm_game')->middleware('user:200_OPE_DLO');
Route::post('order/register/game', 'OrderRegisterController@register_game')->middleware('user:200_OPE_DLO');

// 配信指示書更新
Route::get('order/update/game', 'OrderUpdateController@update_game')->middleware('user:200_OPE_DLO');
Route::post('order/update/game/confirm', 'OrderUpdateController@confirm_game')->middleware('user:200_OPE_DLO');
Route::post('order/update/game/done', 'OrderUpdateController@done_game')->middleware('user:200_OPE_DLO');

// 配信指示書削除
Route::get('order/delete/game/confirm', 'OrderDeleteController@confirm_game')->middleware('user:200_OPE_DLO');
Route::post('order/delete/game', 'OrderDeleteController@delete_game')->middleware('user:200_OPE_DLO');

// 基板配信指示書登録
Route::post('order/register/machine/confirm', 'OrderRegisterController@confirm_machine')->middleware('user:200_OPE_DLO');
Route::post('order/register/machine', 'OrderRegisterController@machine_game')->middleware('user:200_OPE_DLO');

// 基板配信指示書更新
Route::get('order/update/machine', 'OrderUpdateController@update_machine')->middleware('user:200_OPE_DLO');
Route::post('order/update/machine/confirm', 'OrderUpdateController@confirm_machine')->middleware('user:200_OPE_DLO');
Route::post('order/update/machine/done', 'OrderUpdateController@done_machine')->middleware('user:200_OPE_DLO');

// 基板配信指示書削除
Route::get('order/delete/machine/confirm', 'OrderDeleteController@confirm_machine')->middleware('user:200_OPE_DLO');
Route::post('order/delete/machine', 'OrderDeleteController@delete_machine')->middleware('user:200_OPE_DLO');

// クライアントグループ配信指示書登録
Route::post('order/register/clientgroup/confirm', 'OrderRegisterController@confirm_clientgroup')->middleware('user:200_OPE_DLO');
Route::post('order/register/clientgroup', 'OrderRegisterController@register_clientgroup')->middleware('user:200_OPE_DLO');

// クライアントグループ配信指示書更新
Route::get('order/update/clientgroup', 'OrderUpdateController@update_clientgroup')->middleware('user:200_OPE_DLO');
Route::post('order/update/clientgroup/confirm', 'OrderUpdateController@confirm_clientgroup')->middleware('user:200_OPE_DLO');
Route::post('order/update/clientgroup/done', 'OrderUpdateController@done_clientgroup')->middleware('user:200_OPE_DLO');

// クライアントグループ配信指示書削除
Route::get('order/delete/clientgroup/confirm', 'OrderDeleteController@confirm_clientgroup')->middleware('user:200_OPE_DLO');
Route::post('order/delete/clientgroup', 'OrderDeleteController@delete_clientgroup')->middleware('user:200_OPE_DLO');

// 設定閲覧
Route::get('order/config/find', 'OrderFindController@find')->middleware('user:200_OPE_DLO');

// CSV登録
Route::get('order/csv', 'OrderViewController@view_csvimport')->middleware('user:200_OPE_DLO');
Route::post('order/csv', 'OrderCsvUploadController@csv_import')->middleware('user:200_OPE_DLO');
Route::get('order/csv/explain', function () {
    return view('admin/order/explain/csvexplain');
});

/* 配信ステータス閲覧 */
// 画面表示
Route::get('report/search', 'ReportSearchController@view')->middleware('user:300_REF_STA');
// 検索、CSV出力
Route::post('report/search', 'ReportSearchController@data_search')->middleware('user:300_REF_STA');
// 詳細表示
Route::get('report/search/client', 'ReportSearchController@client_detail')->middleware('user:300_REF_STA');
// 認証ログ一覧
Route::get('report/search/auth', 'AuthSearchController@search_view')->middleware('user:300_REF_STA');
// レポート履歴
Route::get('report/search/history', 'ReportSearchController@report_history')->middleware('user:300_REF_STA');
Route::get('report/csv/explain', function () {
    return view('admin/report/explain/csvexplain');
});
Route::get('report/status/explain', function () {
    return view('admin/report/explain/statusexplain');
});
Route::get('report/auth/explain', function () {
    return view('admin/report/explain/authexplain');
});
// 戻る
Route::get('report/search/return', 'ReportSearchController@return_search')->middleware('user:300_REF_STA');


/* ユーザ管理 */
Route::match(['get', 'post'],'user/top', 'UserController@top')->middleware('user:500_USE_DAT');
Route::get('user/regist', 'UserController@regist')->middleware('user:500_USE_DAT');
Route::post('user/regist/confirm', 'UserController@reg_confirm')->middleware('user:500_USE_DAT');
Route::post('user/regist/process', 'UserController@reg_process')->middleware('user:500_USE_DAT');
Route::get('user/update', 'UserController@update')->middleware('user:500_USE_DAT');
Route::post('user/update/confirm', 'UserController@upd_confirm')->middleware('user:500_USE_DAT');
Route::post('user/update/process', 'UserController@upd_process')->middleware('user:500_USE_DAT');
Route::post('user/delete', 'UserController@delete')->middleware('user:500_USE_DAT');
Route::post('user/freeze', 'UserController@freeze')->middleware('user:500_USE_DAT');
Route::post('user/search', 'UserController@search')->middleware('user:500_USE_DAT');
Route::get('user/search', function () {
    return view('admin/manage/user/top');
})->middleware('user:500_USE_DAT');

/* パスワード更新 */
Route::get('user/pass/update', 'UserController@pass_update')->middleware('user');
Route::post('user/pass/confirm', 'UserController@pass_confirm')->middleware('user');
Route::post('user/pass/process', 'UserController@pass_process')->middleware('user');

// 権限管理
Route::match(['get', 'post'],'role/top', 'RoleController@top')->middleware('user:500_USE_DAT');
Route::get('role/regist', 'RoleController@regist')->middleware('user:500_USE_DAT');
Route::post('role/regist/confirm', 'RoleController@reg_confirm')->middleware('user:500_USE_DAT');
Route::post('role/regist/process', 'RoleController@reg_process')->middleware('user:500_USE_DAT');
Route::get('role/update', 'RoleController@update')->middleware('user:500_USE_DAT');
Route::post('role/update/confirm', 'RoleController@upd_confirm')->middleware('user:500_USE_DAT');
Route::post('role/update/process', 'RoleController@upd_process')->middleware('user:500_USE_DAT');
Route::post('role/delete', 'RoleController@delete')->middleware('user:500_USE_DAT');
Route::post('role/search', 'RoleController@search')->middleware('user:500_USE_DAT');
Route::get('role/search', function () {
    return view('admin/manage/role/top');
})->middleware('user:500_USE_DAT');
