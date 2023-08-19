<?php

namespace App\Http\Controllers;

use App\Http\Requests\ReportTitleIdRequest;
use App\Models\ClientGroups;
use App\Models\MachineDownloadOrders;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Config;

/**
 * データ検索画面表示
 *
 * Class ViewGameController
 * @package App\Http\Controllers
 */
class ViewGameController extends Controller
{

    /**
     * 画面表示
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function view_game() {

        return view('admin.view.game.find')
            ->with([
                'gameData' => $this->getGameData(),
            ]);
    }

    /**
     * ゲームID検索
     * @param Request $request
     * @return mixed
     */
    public function data_search(Request $request) {

        $gameId = null;
        if($request->has('gameId')) {
            $this->validate($request, [
                'gameId' => ['required','regex:/^[A-Z0-9]{3,5}$/'],
            ], [
                'gameId.required' => 'ゲームIDを選択してください。',
                'gameId.regex' => 'ゲームIDのフォーマットが不正です。',
            ]);
            $gameId = $request->input('gameId');

        } else {
            $gameId = $request->old('gameId');
        }

        return $this->getData($gameId);
    }

    /**
     * ゲームIDに紐づく各配信指示書設定を取得し、閲覧画面を表示します
     * @param $gameId
     * @return mixed
     */
    public function getData($gameId) {

        //$downOrder = new DownloadOrder();
        $machineDownOrder = new MachineDownloadOrders();
        //$clientgroupDownOrder = new ClientgroupDownloadOrders();

        //$downData = $downOrder->getOrderData($gameId);
        $machineDownData = $machineDownOrder->getMachineOrderData($gameId);
        //$clientgroupDownData = $clientgroupDownOrder->getClientgroupOrderData($gameId);

        $data = array(
            //'downData' => $downData,
            'machineDownData' => $machineDownData,
            //'clientgroupDownData' => $clientgroupDownData,
        );

        return view('admin.view.game.find')
            ->with('gameId', $gameId)
            ->with('data', $data)
            ->with('gameData', $this->getGameData());
    }


}
