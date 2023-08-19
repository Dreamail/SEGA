<?php

namespace App\Http\Controllers;

use App\Models\DownloadOrder;
use App\Models\MachineDownloadOrders;
use App\Models\ClientgroupDownloadOrders;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;

class OrderDeleteController extends Controller
{
    /**
     * 配信指示書削除確認画面表示
     *
     * @param Request $request
     * @return $this
     */
    public function confirm_game(Request $request) {

        $game = new DownloadOrder();

        $titleId = $request->input('gameId');
        $titleVer = $request->input('gameVer');
        $imageType = $request->input('imageType');

        $datas = $game->getData($titleId, $titleVer, $imageType);

        $data = array();
        foreach ($datas as $game) {
            $data = array(
                'gameId' => $game->game_id,
                'gameVer' => $game->game_ver,
                'imageType' => $game->type,
                'uri' => $game->uri,
            );
        }
        Log::info($data);

        $request->session()->put($data);

        return view('admin.order.del_confirm', compact("data"))
            ->with('confirming', 'false')
            ->with('registerType', 'game')
            ->with('gameData', $this->getGameData());
    }

    /**
     * 基板配信指示書削除確認画面表示
     *
     * @param Request $request
     * @return bool
     */
    public function confirm_machine(Request $request) {

        $machine = new MachineDownloadOrders();

        $clientId = $request->input('clientId');
        $imageType = $request->input('imageType');

        $datas = $machine->getData($clientId, $imageType);

        $data = array();
        foreach ($datas as $machine) {
            $data = array(
                'gameId' => $machine->game_id,
                'serial' => $machine->serial,
                'imageType' => $machine->type,
                'uri' => $machine->uri,
            );
        }
        $request->session()->put($data);

        return view('admin.order.del_confirm', compact("data"))
            ->with('confirming', 'false')
            ->with('registerType', 'machine')
            ->with('gameData', $this->getGameData());
    }

    /**
     * クライアントグループ配信指示書削除確認画面表示
     *
     * @param Request $request
     * @return bool
     */
    public function confirm_clientgroup(Request $request) {

        $titleId = $request->input('titleId');
        $titleVer = $request->input('titleVer');
        $clientGroup = $request->input('clientGroup');
        $imageType = $request->input('imageType');

        $clientgroupDownloadOrder = new ClientgroupDownloadOrders();
        $datas = $clientgroupDownloadOrder->findByPk($titleId, $titleVer, $clientGroup, $imageType);

        $data = array();
        if (count($datas) == 1)
        {
            $data = array(
                'titleId'     => $datas[0]->title_id,
                'titleVer'    => $datas[0]->title_ver,
                'clientGroup' => $datas[0]->client_group,
                'imageType'   => $datas[0]->type,
                'uri'          => $datas[0]->uri,
            );
        }

        $request->session()->put($data);

        return view('admin.order.del_confirm', compact("data"))
            ->with('confirming', 'false')
            ->with('registerType', 'clientGroup');
    }

    /**
     * 配信指示書削除処理
     *
     * @param Request $request
     * @return $this
     */
    public function delete_game(Request $request) {

        $data = $request->all();

        if ($request->get('action') === 'back' or $request->get('action') === 'end') {
            return redirect()->action('OrderFindController@find')->withInput($data);
        }

        // 配信指示書登録処理
        $downLadOrder = new DownloadOrder();
        $downLadOrder->deleteData($data['titleId'], $data['titleVer'], $data['imageType']);

        Log::info($data);

        $request->session()->put($data);

        return view('admin.order.del_confirm', compact("data"))
            ->with('confirming', 'true')
            ->with('registerType', 'game');
    }

    /**
     * 基板配信指示書削除処理
     *
     * @param Request $request
     * @return $this
     */
    public function delete_machine(Request $request) {

        $data = $request->all();

        if ($request->get('action') === 'back' or $request->get('action') === 'end') {
            return redirect()->action('OrderFindController@find')->withInput($data);
        }

        // 基板配信指示書登録処理
        $downLadOrder = new MachineDownloadOrders();
        $downLadOrder->deleteDataPk($data['clientId'], $data['imageType']);

        Log::info($data);
        $request->session()->put($data);

        return view('admin.order.del_confirm', compact("data"))
            ->with('confirming', 'true')
            ->with('registerType', 'machine');
    }

    /**
     * クライアントグループ配信指示書削除処理
     *
     * @param Request $request
     * @return $this
     */
    public function delete_clientgroup(Request $request) {

        $data = $request->all();

        if ($request->get('action') === 'back' or $request->get('action') === 'end') {
            return redirect()->action('OrderFindController@find')->withInput($data);
        }

        // クライアントグループ配信指示書登録処理
        $clientgroupDownloadOrder = new ClientgroupDownloadOrders();
        $clientgroupDownloadOrder->deleteByPk($data['titleId'], $data['titleVer'], $data['clientGroup'], $data['imageType']);

        Log::info($data);
        $request->session()->put($data);

        return view('admin.order.del_confirm', compact("data"))
            ->with('confirming', 'true')
            ->with('registerType', 'clientGroup');
    }
}
