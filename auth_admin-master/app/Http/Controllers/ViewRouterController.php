<?php

namespace App\Http\Controllers;

use App\Http\Requests\ReportPlaceIdRequest;
use App\Http\Requests\ReportTitleIdRequest;
use App\Http\Requests\ViewPlaceIdRequest;
use App\Models\ClientGroups;
use App\Models\MachineDownloadOrders;
use App\Models\Places;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\Log;

/**
 * データ検索画面　店舗情報表示
 *
 * Class ViewPlaceController
 * @package App\Http\Controllers
 */
class ViewRouterController extends Controller
{

    /**
     * 画面表示
     * @param Request $request
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function view_router(Request $request) {

        $request->session()->forget('returnSearchType');
        return view('admin.view.router.find')
            ->with('searchType', Config::get('const.VIEW_SEARCH_TYPE')['PLACE']);
    }

    /**
     * 検索
     *
     * @param Request $request
     * @return ReportSearchController|mixed
     */
    public function data_search(Request $request) {

        $request->session()->put('returnSearchType', $request->input('searchType'));

        // 各ボタンによる処理分岐
        switch ($request->input('searchType')) {

            case Config::get('const.VIEW_SEARCH_TYPE')['PLACE']:
                return $this->place_search($request);
            case Config::get('const.VIEW_SEARCH_TYPE')['ROUTER']:
                return $this->router_search($request);
            case Config::get('const.VIEW_SEARCH_TYPE')['ALLNETID']:
                return $this->allnetid_search($request);
            case Config::get('const.VIEW_SEARCH_TYPE')['PLACEID']:
                return $this->placeid_search($request);
            case Config::get('const.VIEW_SEARCH_TYPE')['IPADDRESS']:
                return $this->ipaddress_search($request);

//            default:
//                return redirect()->action('ReportSearchController@view');
        }
    }

    /**
     * returnデータを作成
     *
     * @param $request
     * @param $result
     * @return $this
     */
    private function makeReturnData($request, $result){
        return view('admin.view.router.find')
            ->with('searchType', $request->input('searchType'))
            ->with('result', $result);
    }

    /**
     * 店舗名検索
     * @param ReportPlaceIdRequest $request
     * @return $this
     */
    private function place_search($request) {

        // バリデーション
        $keyword = null;
        if($request->has('keyword')) {
            $this->validate($request, [
                'keyword' => ['required'],//,'regex:/^[A-Z0-9]{3,5}$/'
            ], [
                'keyword.required' => '値が未入力です。',
                //'keyword.regex' => '店舗名のフォーマットが不正です。',
            ]);
            $keyword = $request->input('keyword');

        } else {
            $keyword = $request->old('keyword');
        }

        $model = new Places();

        $result = $model->getDataName($request->input('keyword'));

        $returnData = $this->makeReturnData($request,$result);

        if($request->input('action') === Config::get('const.ACTION')['SEARCH']) {
            $returnData->with('keyword', $request->input('keyword'));
        }
        return $returnData;
    }

    /**
     * ALLNetID検索
     * @param ViewPlaceIdRequest $request
     * @return $this
     */
    private function allnetid_search($request) {

        // バリデーション
        $keyword = null;
        if($request->has('keyword')) {
            $this->validate($request, [
                'keyword' => ['required'],//,'regex:/^[A-Z0-9]{3,5}$/'
            ], [
                'keyword.required' => '値が未入力です。',
                //'keyword.regex' => 'ALL.NetIDのフォーマットが不正です。',
            ]);
            $keyword = $request->input('keyword');

        } else {
            $keyword = $request->old('keyword');
        }

        $model = new Places();

        $result = $model->getDataPlaceId($request->input('keyword'));

        $returnData = $this->makeReturnData($request,$result);

        if($request->input('action') === Config::get('const.ACTION')['SEARCH']) {
            $returnData->with('keyword', $request->input('keyword'));
        }
        return $returnData;
    }

    /**
     * 店舗ID検索
     * @param ViewPlaceIdRequest $request
     * @return $this
     */
    private function placeid_search($request) {

        // バリデーション
        $keyword = null;
        if($request->has('keyword')) {
            $this->validate($request, [
                'keyword' => ['required'],//,'regex:/^[A-Z0-9]{3,5}$/'
            ], [
                'keyword.required' => '値が未入力です。',
                //'keyword.regex' => '店舗IDのフォーマットが不正です。',
            ]);
            $keyword = $request->input('keyword');

        } else {
            $keyword = $request->old('keyword');
        }

        $model = new Places();

        $result = $model->getDataPlaceId($request->input('keyword'));

        $returnData = $this->makeReturnData($request,$result);

        if($request->input('action') === Config::get('const.ACTION')['SEARCH']) {
            $returnData->with('keyword', $request->input('keyword'));
        }
        return $returnData;
    }

    /**
     * IPアドレス検索
     * @param ViewPlaceIdRequest $request
     * @return $this
     */
    private function ipaddress_search($request) {

        // バリデーション
        $keyword = null;
        if($request->has('keyword')) {
            $this->validate($request, [
                'keyword' => ['required'],//,'regex:/^[A-Z0-9]{3,5}$/'
            ], [
                'keyword.required' => '値が未入力です。',
                //'keyword.regex' => '電話番号のフォーマットが不正です。',
            ]);
            $keyword = $request->input('keyword');

        } else {
            $keyword = $request->old('keyword');
        }

        $model = new Places();

        $result = $model->getDataPlaceId($request->input('keyword'));

        $returnData = $this->makeReturnData($request,$result);

        if($request->input('action') === Config::get('const.ACTION')['SEARCH']) {
            $returnData->with('keyword', $request->input('keyword'));
        }
        return $returnData;
    }

    /**
     * ルーター検索
     * @param ViewPlaceIdRequest $request
     * @return $this
     */
    private function router_search($request) {

        $keyword = null;
        if($request->has('keyword')) {
            $this->validate($request, [
                'keyword' => ['required'],//,'regex:/^[A-Z0-9]{3,5}$/'
            ], [
                'keyword.required' => '値が未入力です。',
                //'keyword.regex' => 'ゲームIDのフォーマットが不正です。',
            ]);
            $keyword = $request->input('keyword');

        } else {
            $keyword = $request->old('keyword');
        }
        Log::info($request->input('keyword'));

        $model = new Places();

        $result = $model->getDataPlaceId($request->input('keyword'));

        $returnData = $this->makeReturnData($request,$result);

        if($request->input('action') === Config::get('const.ACTION')['SEARCH']) {
            $returnData->with('keyword', $request->input('keyword'));
        }
        return $returnData;
    }
}
