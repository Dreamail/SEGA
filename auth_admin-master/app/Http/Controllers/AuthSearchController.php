<?php

namespace App\Http\Controllers;

use App\Models\AuthLogs;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\Log;

/**
 * 認証ログ検索
 *
 * Class AuthSearchController
 * @package App\Http\Controllers
 */
class AuthSearchController extends Controller
{
    /**
     * 認証ログ一覧表示処理
     *
     * @param Request $request
     * @return AuthSearchController
     */
    public function search_view(Request $request) {

        // バリデーション
        $this->search_validate($request);

        // 配信ステータス詳細情報を取得し返却
        return  $this->search_auth(Input::get("client"));
    }

    /**
     * バリデーション
     * @param $request
     */
    protected function search_validate($request) {

        $rules =  ['client' => 'required',];
        $messages =  ['required' => Config::get('message.REQUIRED')['SERIAL'],];

        $this->validate($request, $rules, $messages);
    }

    /**
     * 認証ログ取得
     *
     * @param $clientId
     * @return $this
     */
    protected function search_auth($clientId) {

        $auth = new AuthLogs();
        $authLog = $auth->getLogDataByClient($clientId);

        return view('admin.report.authlist')
            ->with('result',$authLog);
    }
}
