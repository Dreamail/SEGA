<?php

namespace App\Http\Controllers;

use App\Models\DownloadOrder;
use App\Models\MachineDownloadOrders;
use App\Models\ClientgroupDownloadOrders;
use Illuminate\Http\Request;

/**
 * 配信指示書設定・閲覧
 *
 * Class OrderFindController
 * @package App\Http\Controllers
 */
class OrderFindController extends Controller
{
    /**
     * 配信指示書設定・閲覧画面を表示します
     * @param Request $request
     * @return mixed
     */
    public function find(Request $request) {

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

        $downOrder = new DownloadOrder();
        $machineDownOrder = new MachineDownloadOrders();
        $clientgroupDownOrder = new ClientgroupDownloadOrders();

        $downData = $downOrder->getOrderData($gameId);
        $machineDownData = $machineDownOrder->getMachineOrderData($gameId);
        $clientgroupDownData = $clientgroupDownOrder->getClientgroupOrderData($gameId);

        $data = array(
            'downData' => $downData,
            'machineDownData' => $machineDownData,
            'clientgroupDownData' => $clientgroupDownData,
        );

        return view('admin.order.config')
            ->with('gameId', $gameId)
            ->with('data', $data)
            ->with('gameData', $this->getGameData());
    }
}
