<?php

namespace App\Http\Controllers;

use App\Http\Requests\ReportClientIdRequest;
use App\Http\Requests\ReportGroupRequest;
use App\Http\Requests\ReportPlaceIdRequest;
use App\Http\Requests\ReportTitleIdRequest;
use App\Models\AppDeliverReports;
use App\Models\AppDeliverReportsHistory;
use App\Models\Machines;
use App\Models\OptDeliverReports;
use App\Models\OptDeliverReportsHistory;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Validator;

/**
 * 配信レポート閲覧関連処理
 *
 * Class ReportSearchController
 * @package App\Http\Controllers
 */
class ReportSearchController extends Controller
{
    /**
     * 配信ステータス閲覧表示
     * @return $this
     */
    public function view(Request $request) {

        $request->session()->forget('returnSearchType');
        $request->session()->forget('returnImageType');
        return view('admin.report.search')
            ->with('gameData', $this->getGameData())
            ->with('imageType', Config::get('const.IMAGE_TYPE_CHAR')['APP'])
            ->with('searchType', Config::get('const.SEARCH_TYPE')['TITLE']);
    }

    /**
     * 検索・CSV出力メイン処理
     *
     * @param Request $request
     * @return ReportSearchController|mixed
     */
    public function data_search(Request $request) {

        $request->session()->put('returnSearchType', $request->input('searchType'));
        $request->session()->put('returnImageType', $request->input('imageType'));

        // 各ボタンによる処理分岐
        switch ($request->input('searchType')) {

            case Config::get('const.SEARCH_TYPE')['SERIAL']:
                return $this->client_search($request);
            case Config::get('const.SEARCH_TYPE')['TITLE']:
                return $this->title_search($request);
            case Config::get('const.SEARCH_TYPE')['PLACE']:
                return $this->place_search($request);
            case Config::get('const.SEARCH_TYPE')['GROUP']:
                return $this->group_search($request);
//            default:
//                return redirect()->action('ReportSearchController@view');
        }
    }

    /**
     * 戻るボタン押下時の処理
     * @param Request $request
     * @return mixed
     */
    public function return_search(Request $request) {

        if($request->session()->has('returnSearchType')) {

            $data = $request->all();
            $data = array_merge($data,array('searchType'=>$request->session()->get('returnSearchType')));
            $data = array_merge($data,array('imageType'=>$request->session()->get('returnSearchType')));

            $request->session()->forget('returnSearchType');
            $request->session()->forget('returnImageType');

            return redirect()->action('ReportSearchController@view')
                ->withInput($data);
        }
        return view('admin.report.search')
            ->with('gameData', $this->getGameData())
            ->with('imageType', Config::get('const.IMAGE_TYPE_CHAR')['APP'])
            ->with('searchType', Config::get('const.SEARCH_TYPE')['TITLE']);
    }

    /**
     * シリアル詳細表示
     *
     * @param Request $request
     * @return mixed
     */
    public function client_detail(Request $request) {

        // バリデーション
        $this->clientDetail_validate($request);
        // 配信ステータス詳細情報を取得し返却
        $view =  $this->clientDetailSearch(Input::get("client"), Config::get('const.SEARCH_TYPE')['TITLE']);

        return $view;
    }

    /**
     * 検索からの詳細表示
     *
     * @param ReportClientIdRequest $request
     * @return mixed
     */
    public function client_search($request) {

        // バリデーション
        $this->client_validate($request);
        // 配信ステータス詳細情報を取得し返却
        $view =  $this->clientDetailSearch($request->input('serial'), Config::get('const.SEARCH_TYPE')['SERIAL']);

        return $view;
    }

    /**
     * レポート履歴を取得
     *
     * @param Request $request
     * @return $this
     */
    public function report_history(Request $request) {

        // バリデーション
        $this->history_validate($request);

        $clientId = Input::get("client");
        $imageType = Input::get('imagetype');

        // イメージタイプにより参照するMODELを切り分ける
        switch ($imageType) {

            case Config::get('const.IMAGE_TYPE_CHAR')['APP'];
                $model = new AppDeliverReportsHistory();
                break;
            case Config::get('const.IMAGE_TYPE_CHAR')['OPT'];
                $model = new OptDeliverReportsHistory();
                break;
        }
        $data = $model->seachByClientId($clientId, 30);

        return view('admin.report.history')
            ->with('result',$data)
            ->with('client',$clientId)
            ->with('imagetype',$imageType);
    }

    /**
     * 配信ステータス詳細情報を取得し返却
     *
     * @param $clientId
     * @return mixed
     */
    private function clientDetailSearch($serial, $searchType) {

        $appReport = new AppDeliverReports();
        $optReport = new OptDeliverReports();
        $machines = new Machines();
        $appReportHistory = new AppDeliverReportsHistory();
        $optReportHistory = new OptDeliverReportsHistory();

        // イメージタイプ：アプリケーションの配信レポート情報を取得
        $appdata = $appReport->seachByClientId($serial);
        // イメージタイプ：アプリケーションの配信レポート履歴情報を取得
        $apphisdata = $appReportHistory->seachByClientId($serial, 10);
        // イメージタイプ：オプションの配信レポート情報を取得
        $optdata = $optReport->seachByClientId($serial);
        // イメージタイプ：オプションの配信レポート履歴情報を取得
        $opthisdata = $optReportHistory->seachByClientId($serial, 10);
        // 基盤情報
        $machineData = $machines->getDataPlace($serial);

        if(count($appdata) == 0 && count($optdata) == 0) {
            return view('admin.report.search')
                ->with('searchType', $searchType)
                ->with('imageType', Config::get('const.IMAGE_TYPE_CHAR')['APP'])
                ->with('gameData', $this->getGameData())
                ->with('result', array());
        } else {
            return view('admin.report.client')
                ->with('serial', $serial)
                ->with('machineData', $machineData)
                ->with('appData', $appdata)
                ->with('optData', $optdata)
                ->with('appHistoryData', $apphisdata)
                ->with('optHistoryData', $opthisdata);
        }
    }

    /**
     * ゲームID検索
     * @param $request
     * @return $this
     */
    private function title_search($request) {
        // バリデーション
        $this->title_validate($request);

        $model = $this->modelSelect($request->input('imageType'));
        Log::info('report search');
        Log::info($request->input('downloadStates'));
        $result = $model->seachByTitleId($request->input('gameId'), $request->input('downloadStates'));

        $returnData = $this->makeReturnData($request,$result);

        if($request->input('action') === Config::get('const.ACTION')['SEARCH']) {
            $returnData->with('gameId', $request->input('gameId'));
        }
        return $returnData;
    }

    /**
     * 店舗ID検索
     * @param ReportPlaceIdRequest $request
     * @return $this
     */
    private function place_search($request) {

        // バリデーション
        $this->place_validate($request);
        Log::info($request->input('placeId'));

        $model = $this->modelSelect($request->input('imageType'));
        $result = $model->seachByPlaceId($request->input('placeId'));

        $returnData = $this->makeReturnData($request,$result);

        if($request->input('action') === Config::get('const.ACTION')['SEARCH']) {
            $returnData->with('placeId', $request->input('placeId'));
        }
        return $returnData;
    }

    /**
     * クライアントグループ検索
     *
     * @param ReportGroupRequest $request
     * @return $this
     */
    private function group_search($request) {

        // バリデーション
        $this->group_validate($request);

        $model = $this->modelSelect($request->input('imageType'));
        $result = $model->seachByClientGroup($request->input('client_group'));

        $returnData = $this->makeReturnData($request,$result);

        if($request->input('action') === Config::get('const.ACTION')['SEARCH']) {
            $returnData->with('titleId', $request->input('titleId'));
            $returnData->with('client_group', $request->input('client_group'));
        }
        return $returnData;
    }

    /**
     * returnデータを作成
     *
     * @param $request
     * @param $result
     * @return $this
     */
    private function makeReturnData($request, $result){

        // 各ボタンによる処理分岐
        switch ($request->input('action')) {

            // 検索ボタン押下時の処理
            case Config::get('const.ACTION')['SEARCH'];

                return view('admin.report.search')
                    ->with('gameData', $this->getGameData())
                    ->with('searchType', $request->input('searchType'))
                    ->with('imageType', $request->input('imageType'))
                    ->with('result', $result);

            // CSVダウンロードボタン押下時の処理
            case Config::get('const.ACTION')['CSV_DL'];
                //file名
                $filename = Config::get('const.CSV_FILE_NAME')[$request->input('imageType')];

                //header
                $headers = array(
                    'Content-Type' => 'text/csv',
                    'Content-Disposition' => 'attachment; filename="' . $filename . '"'
                );

                $stream = $this->csvHeaderCreate($request->input('imageType'));

                //response
                return \Response::make($this->csvCreate($result, $stream, $request->input('imageType')), 200, $headers);
        }
    }

    /**
     * イメージタイプより参照するMODELを選択し返す
     *
     * @param $imageType
     * @return AppDeliverReports|OptDeliverReports|null
     */
    private function modelSelect($imageType) {

        $model = null;
        switch ($imageType) {

            case Config::get('const.IMAGE_TYPE_CHAR')['APP'];
                $model = new AppDeliverReports();
                break;
            case Config::get('const.IMAGE_TYPE_CHAR')['OPT'];
                $model = new OptDeliverReports();
                break;
        }

        return $model;
    }

    /**
     * CSV作成
     * @param $result
     * @return null|string|string[]
     */
    function csvCreate($result, $stream, $imageType) {

        foreach ($result as $row) {

            $array = array(
                $row->client_id,
                $row->title_id,
                $row->place_id,
                $row->name,
                $row->download_ratio,
                $row->files_released,
                $row->files_working,
                $row->segs_total,
                $row->segs_downloaded,
                $row->auth_time,
                $row->order_time,
                $row->release_time,
                $row->auth_state,
                $row->download_state,
                $row->description,
                $row->ap_ver_released,
                $row->os_ver_released,
                $row->update,
            );

            if(Config::get('const.IMAGE_TYPE_CHAR')['APP'] === $imageType) {
                array_splice($array, 16,0, "作業中APバージョン");
                array_splice($array, 18,0, "作業中OSバージョン");
            }

            fputcsv($stream, (array) $array);
        }
        //ポインタの先頭へ
        rewind($stream);

        //いろいろ変換
        $csv = mb_convert_encoding(str_replace(PHP_EOL, "\r\n", stream_get_contents($stream)), 'UTF-8');

        return $csv;
    }

    /**
     * CSVヘッダー
     *
     * @return array
     */
    function csvHeaderCreate($imageType) {

        //仮ファイルOpen
        $stream = fopen('php://temp','w');

        $array = array(
            "シリアル",
            "ゲームID",
            "店舗ID",
            "店舗名",
            "DL率",
            "公開済みファイルリスト",
            "作業中ファイルリスト",
            "総セグメント数",
            "DL済みセグメント数",
            "認証時刻",
            "DL開始日時",
            "公開日時",
            "認証状態",
            "DL状態",
            "指示書説明",
            "公開済みAPバージョン",
            "公開済みOSバージョン",
            "アクセス時刻"
        );

        if(Config::get('const.IMAGE_TYPE_CHAR')['APP'] === $imageType) {
            array_splice($array, 16,0, "作業中APPバージョン");
            array_splice($array, 18,0, "作業中OSバージョン");
        }

        fputcsv($stream, (array) $array);
        return $stream;
    }

    /**
     * シリアル詳細用バリデーション
     * @param $request
     */
    protected function clientDetail_validate($request) {

        $rules =  ['client' => 'required',];
        $messages =  ['required' => Config::get('message.REQUIRED')['SERIAL'],];

        $this->validate($request, $rules, $messages);
    }

    /**
     * レポート履歴用バリデーション
     * @param $request
     */
    protected function history_validate($request) {

        $rules =  ['client' => 'required', 'imagetype' => 'required'];
        $messages =  ['client.required' => Config::get('message.REQUIRED')['SERIAL'],
                      'imagetype.required' => Config::get('message.REQUIRED')['TYPE'],];

        $this->validate($request, $rules, $messages);
    }

    /**
     * シリアル検索用バリデーション
     * @param $request
     */
    protected function client_validate($request) {

        $req = new ReportClientIdRequest();

        $this->validate($request, $req->rules(), $req->messages());
    }

    /**
     * ゲームID検索用バリデーション
     * @param $request
     */
    protected function title_validate($request) {

        $req = new ReportTitleIdRequest();

        $this->validate($request, $req->rules(), $req->messages());
    }

    /**
     * 店舗ID検索用バリデーション
     * @param $request
     */
    protected function place_validate($request) {

        $req = new ReportPlaceIdRequest();

        $this->validate($request, $req->rules(), $req->messages());
    }

    /**
     * クライアントグループ検索用バリデーション
     * @param $request
     */
    protected function group_validate($request) {

        $req = new ReportGroupRequest();

        $this->validate($request, $req->rules(), $req->messages());
    }
}
