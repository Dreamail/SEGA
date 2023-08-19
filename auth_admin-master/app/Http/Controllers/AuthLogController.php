<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Config;
use App\Http\Requests\AuthLogRequest;
use App\Models\AuthLogs;
use App\Models\Countries;
use Log;
use Illuminate\Http\Request;

/**
 * 認証ログ
 *
 * Class AuthLogController
 * @package App\Http\Controllers
 */
class AuthLogController extends Controller
{
    /**
     * 画面表示
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function top() {

        //オプションの国コード取得
        $countries = new Countries();
        $countryData = $countries->getAllData();

        return view('admin.log.auth')
            ->with([
                'countryData' => $countryData
                , 'stat' => null
                , 'startDateStr' => date('Y/m/d', strtotime("-1 month"))
                , 'endDateStr' => date('Y/m/d')
                , 'startHour' => date('H')
                , 'startMinutes' => date('i')
                , 'endHour' => date('H')
                , 'endMinutes' => date('i')
            ]);
    }

    /**
     * 検索・CSV出力メイン処理
     *
     * @param AuthLogRequest $request
     * @return ReportSearchController|mixed
     */
    public function data_search(AuthLogRequest $request) {

        $countries = new Countries();
        $countryData = $countries->getAllData();

        $serial = $request->input('serial');
        $authResult = $request->input('authResult');
        $usedRange = $request->input('usedRange');
        $startDateTime = date('Y/m/d H:i' , strtotime("-1 month"));
        $endDateTime = date('Y/m/d H:i');
        $startHour = $request->input('startHour');
        $startMinutes = $request->input('startMinutes');
        $endHour = $request->input('endHour');
        $endMinutes = $request->input('endMinutes');

        // 範囲指定のチェックが選択されていた場合はバリデーションと文字数調整を実施
        if($usedRange) {
            // バリデーション
            $this->range_validate($request);

            $startDateTime = $request->input('startDateStr').' '
                .str_pad($request->input('startHour'), 2, '0', STR_PAD_LEFT).':'
                .str_pad($request->input('startMinutes'), 2, '0', STR_PAD_LEFT);
            $endDateTime = $request->input('endDateStr').' '
                .str_pad($request->input('endHour'), 2, '0', STR_PAD_LEFT).':'
                .str_pad($request->input('endMinutes'), 2, '0', STR_PAD_LEFT);
        }

        //ソート
        $usedSort = $request->input('usedSort');
        $sortItem = null;
        if($usedSort && !is_null($request->input('sortSequenceList'))) {
            $sortItem = $this->create_sortItems($request->input('sortSequenceList'));
        }

        //オプション
        $usedOption = $request->input('usedOption');
        $stat = null;
        if($usedOption) {
            $stat = $request->input('stat');
        }

        // 認証ログ取得
        $authLogs = new AuthLogs();

        // 各ボタンによる処理分岐
        switch ($request->input('action')) {
            // 検索ボタン押下時の処理
            case Config::get('const.ACTION')['SEARCH'];
                $result = $authLogs->getLogData($serial, $startDateTime, $endDateTime, $sortItem, $stat, $usedRange, $authResult);

                foreach ($result as $Key => &$data) {
                    $data->response_2 = $this->createResponse($data->response_2);
                }
                unset($data);

                return view('admin.log.auth')
                    ->with('result', $result)
                    ->with('serial', $serial)
                    ->with('authResult', $authResult)
                    ->with('usedRange', $usedRange)
                    ->with('startDateStr', date('Y/m/d',  strtotime($startDateTime)))
                    ->with('startHour', $startHour)
                    ->with('startMinutes', $startMinutes)
                    ->with('endDateStr', date('Y/m/d',  strtotime($endDateTime)))
                    ->with('endHour', $endHour)
                    ->with('endMinutes', $endMinutes)
                    ->with('usedSort', $usedSort)
                    ->with('usedOption', $usedOption)
                    ->with('stat', $stat)
                    ->with('countryData', $countryData);

            // CSVダウンロードボタン押下時の処理
            case Config::get('const.ACTION')['CSV_DL'];
                //file名
                $filename = "AUTH_LOG.csv";

                //header
                $headers = array(
                    'Content-Type' => 'text/csv',
                    'Content-Disposition' => 'attachment; filename="' . $filename . '"'
                );
                $csvResult = $authLogs->getCsvLogData($serial, $startDateTime, $endDateTime, $sortItem, $stat, $usedRange, $authResult);
                //response
                return \Response::make($this->authLogCsvCreate($csvResult), 200, $headers);

            default:
                return view('admin.log.auth')
                    ->with('countryData', $countryData)
                    ->with('stat', null)
                    ->with('startDateStr', date('Y/m/d', strtotime("-1 month")))
                    ->with('endDateStr', date('Y/m/d'))
                    ->with('startHour', date('H'))
                    ->with('startMinutes', date('i'))
                    ->with('endHour', date('H'))
                    ->with('endMinutes', date('i'));
        }
    }

    /**
     * 認証ログCSV作成
     * @param $result
     * @return null|string|string[]
     */
    function authLogCsvCreate($result) {

        //仮ファイルOpen
        $stream = fopen('php://temp','w');
        foreach ($result as $row) {
            $array = array(
                $row->create_date,
                ($row->auth_result == '0'? '認証成功' : '認証失敗'),
                ($row->keychip_id == '00000000000' ? '' : $row->keychip_id),
                ($row->auth_result == '0'? '' : $row->request_1),
                ($row->auth_result == '0'? '' : $row->response_1),
                ($row->auth_result == '0'? '' : $row->request_2)
            );
            fputcsv($stream, (array) $array);
        }
        //ポインタの先頭へ
        rewind($stream);

        //いろいろ変換
        $csv = str_replace(PHP_EOL, "\r\n", stream_get_contents($stream));

        return $csv;
    }

    /**
     * ソートオーダー設定用のリストを作成する
     * @param $items
     * @return string[]
     */
    public function create_sortItems($items) {
        $sortItems = array();

        foreach ($items as $item) {
            $item_split = explode(":", $item);

            switch ($item_split[0]) {
                case 'TIME';
                    if( count($item_split) > 1 ){
                        //desc
                        $sortItems = array_merge($sortItems, array('logs.create_date' => $item_split[1]));
                    }else{
                        $sortItems = array_merge($sortItems, array('logs.create_date' => 'ASC'));
                    }
                case 'SERIAL';
                    if( count($item_split) > 1 ){
                        //desc
                        $sortItems = array_merge($sortItems, array('logs.keychip_id' => $item_split[1]));
                    }else{
                        $sortItems = array_merge($sortItems, array('logs.keychip_id' => 'ASC'));
                    }
            }
        }
        return $sortItems;
    }

    /**
     * 範囲指定バリデーション
     * @param $request
     */
    protected function range_validate($request) {

        $rules =  [
            'startDateStr' => 'date_format:Y/m/d',
            'startHour' => 'integer|max:23|min:0',
            'startMinutes' => 'integer|max:59|min:0',
            'endDateStr' => 'date_format:Y/m/d',
            'endHour' => 'integer|max:23|min:0',
            'endMinutes' => 'integer|max:59|min:0',
        ];
        $messages =  [
            'startDateStr.*' => '検索範囲（開始日）'.Config::get('message.DATE_ERROR'),
            'startHour.*' => '検索範囲（開始日）'.Config::get('message.TIME_ERROR'),
            'startMinutes.*' => '検索範囲（開始日）'.Config::get('message.TIME_ERROR'),
            'endDateStr.*' => '検索範囲（終了日）'.Config::get('message.DATE_ERROR'),
            'endHour.*' => '検索範囲（終了日）'.Config::get('message.TIME_ERROR'),
            'endMinutes.*' => '検索範囲（終了日）'.Config::get('message.TIME_ERROR'),
        ];

        $this->validate($request, $rules, $messages);
    }

    /**
     * 認証リクエスト文字列から各パラメータの値をセットします
     */
    public function createResponse($response)
    {
        $returnResponse = "";
        if (empty($response)) {
            return $returnResponse;
        }
        // リクエストパラメータ文字列を'&'で分割して各パラメータにセットします
        foreach (explode('&', $response) as $param) {
            $paramKeyValue = explode('=', $param, 2);
            if (count($paramKeyValue) != 2) {
                // key=valueの形式でない場合次へ
                continue;
            }
            switch ($paramKeyValue[0]) {
                case 'auth_data':
                    $returnResponse = trim($paramKeyValue[1]);
                    break;
            }
        }
        return $returnResponse;
    }
}
