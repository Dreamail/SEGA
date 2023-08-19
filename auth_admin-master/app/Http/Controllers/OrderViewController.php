<?php

namespace App\Http\Controllers;

use App\Models\ClientGroups;

/**
 * 配信指示書設定画面表示
 *
 * Class OrderViewController
 * @package App\Http\Controllers
 */
class OrderViewController extends Controller
{

    /**
     * 新規登録
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function view_register() {

        // プルダウン用クライアントグループリストを取得します
        $clientGroups = new ClientGroups();
        $clientGroupArray = $clientGroups->getClientgroups();

        return view('admin.order.register')
            ->with([
                'gameData' => $this->getGameData(),
                'clientGroups' => $clientGroupArray,
            ]);
    }

    /**
     * 設定の閲覧・更新・削除
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function view_config() {
        return view('admin.order.config')
            ->with('gameData', $this->getGameData());
    }

    /**
     * CSV登録
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function view_csvimport() {
        return view('admin.order.csvimport');
    }
}
