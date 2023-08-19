<?php

namespace App\Http\Controllers;

use App\Http\Requests\OrderRegisterGameRequest;
use App\Http\Requests\OrderRegisterMachineRequest;
use App\Http\Requests\OrderRegisterClientgroupRequest;
use App\Models\DownloadOrder;
use App\Models\MachineDownloadOrders;
use App\Models\ClientgroupDownloadOrders;
use Illuminate\Http\Request;

class OrderUpdateController extends Controller
{
    /**
     * 配信指示書更新画面表示
     *
     * @param Request $request
     * @return $this
     */
    public function update_game(Request $request) {

        $game = new DownloadOrder();

        $titleId = null;
        $titleVer = null;
        $imageType = null;
        if($request->has('titleId')) {
            $titleId = $request->input('titleId');
            $titleVer = $request->input('titleVer');
            $imageType = $request->input('imageType');
        } else {
            $titleId = $request->old('titleId');
            $titleVer = $request->old('titleVer');
            $imageType = $request->old('imageType');
        }

        $data = $game->getData($titleId, $titleVer, $imageType);
        $request->session()->put($data);

        return view('admin.order.update', compact("data"))
            ->with('confirming', 'false')
            ->with('registerType', 'game')
            ->with('gameData', $this->getGameData());
    }

    /**
     * 基板配信指示書更新画面表示
     *
     * @param Request $request
     * @return bool
     */
    public function update_machine(Request $request) {

        $machine = new MachineDownloadOrders();

        $titleId = null;
        $titleVer = null;
        $imageType = null;
        if($request->has('clientId')) {
            $clientId = $request->input('clientId');
            $imageType = $request->input('imageType');
        } else {
            $clientId = $request->old('clientId');
            $imageType = $request->old('imageType');
        }

        $data = $machine->getData($clientId, $imageType);
        $request->session()->put($data);

        return view('admin.order.update', compact("data"))
            ->with('confirming', 'false')
            ->with('registerType', 'machine')
            ->with('gameData', $this->getGameData());
    }

    /**
     * クライアントグループ配信指示書更新画面表示
     * @param Request $request
     * @return $this
     */
    public function update_clientgroup(Request $request) {

        $titleId = null;
        $titleVer = null;
        $clientGroup = null;
        $imageType = null;
        if($request->has('titleId')) {
            $titleId = $request->input('titleId');
            $titleVer = $request->input('titleVer');
            $clientGroup = $request->input('clientGroup');
            $imageType = $request->input('imageType');
        } else {
            $titleId = $request->old('titleId');
            $titleVer = $request->old('titleVer');
            $clientGroup = $request->old('clientGroup');
            $imageType = $request->old('imageType');
        }

        $clientgroupDownloadOrder = new ClientgroupDownloadOrders();
        $data = $clientgroupDownloadOrder->findByPk($titleId, $titleVer, $clientGroup, $imageType);

        $request->session()->put($data);

        return view('admin.order.update', compact("data"))
            ->with('registerType', 'clientGroup');
    }

    /**
     * 配信指示書更新確認画面表示
     *
     * @param Request $request
     * @return $this
     */
    public function confirm_game(OrderRegisterGameRequest $request) {

        $data = $request->all();
        $find = new OrderFindController();
        if ($request->get('action') === 'back') {
//            return $find->getData($data['titleId']);
//            return redirect('order/config/find')->withInput($data);
            return redirect()->action('OrderFindController@find')->withInput($data);
        }

        $data = array_merge($data,array('confirming'=>'false'));
        $data = array_merge($data,array('registerType'=>'game'));
        $request->session()->put($data);

        // 登録済みの基板・クライアントグループ配信指示書が存在するかチェックし、存在する場合、画面に警告を表示する
        $machineDownloadOrders = new MachineDownloadOrders();
        $existsMachineDownloadOrder =
            $machineDownloadOrders->existsMachineDownloadOrder($data['titleId'], $data['imageType']);
        $clientgroupDownloadOrders = new ClientgroupDownloadOrders();
        $existsClientgroupDownloadOrder =
            $clientgroupDownloadOrders->existsClientgroupDownloadOrder($data['titleId'], $data['titleVer'], $data['imageType']);

        return view('admin.order.upd_confirm', compact(
            "data",
            "existsMachineDownloadOrder",
            "existsClientgroupDownloadOrder"
        ));
    }

    /**
     * 基板配信指示書更新確認画面表示
     *
     * @param Request $request
     * @return bool
     */
    public function confirm_machine(OrderRegisterMachineRequest $request) {

        $data = $request->all();
        $find = new OrderFindController();
        if ($request->get('action') === 'back') {
            return redirect()->action('OrderFindController@find')->withInput($data);
        }

        $data = array_merge($data,array('confirming'=>'false'));
        $data = array_merge($data,array('registerType'=>'machine'));
        $request->session()->put($data);

        return view('admin.order.upd_confirm', compact("data"));
    }

    /**
     * クライアントグループ配信指示書更新確認画面表示
     *
     * @param OrderRegisterClientgroupRequest $request
     * @return bool
     */
    public function confirm_clientgroup(OrderRegisterClientgroupRequest $request) {

        $data = $request->all();
        if ($request->get('action') === 'back') {
            return redirect()->action('OrderFindController@find')->withInput($data);
        }

        $data = array_merge($data, array('confirming' => 'false'));
        $data = array_merge($data, array('registerType' => 'clientGroup'));
        $request->session()->put($data);

        return view('admin.order.upd_confirm', compact("data"));
    }

    /**
     * 配信指示書更新処理
     *
     * @param Request $request
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function done_game(Request $request) {

        $data = $request->all();

        if ($request->get('action') === 'back') {
            $data = array_merge($data,array('registerType'=>'game'));
            return redirect()->action('OrderUpdateController@update_game')->withInput($data);
        } else if($request->get('action') === 'end') {
            return redirect()->action('OrderFindController@find')->withInput($data);
        }

        // 基板・クライアントグループがもつ配信指示書URI情報をクリアする
        $machineDownloadOrders = new MachineDownloadOrders();
        $machineDownloadOrders->deleteMachineDownloadOrder($data['titleId'], $data['imageType']);
        $clientgroupDownloadOrders = new ClientgroupDownloadOrders();
        $clientgroupDownloadOrders->deleteByDownloadOrder($data['titleId'], $data['titleVer'], $data['imageType']);

        // 配信指示書登録処理
        $downLadOrder = new DownloadOrder();
        $downLadOrder->dataSave($data['titleId'], $data['titleVer'], $data['imageType'], $data['uri']);

        $data = array_merge($data,array('confirming'=>'true'));
        $data = array_merge($data,array('registerType'=>'game'));

        $request->session()->put($data);

        return view('admin.order.upd_confirm', compact("data"));
    }

    /**
     * 基板配信指示書更新登録処理
     *
     * @param Request $request
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function done_machine(Request $request) {

        $data = $request->all();
        if ($request->get('action') === 'back') {
            $data = array_merge($data,array('registerType'=>'game'));
            return redirect()->action('OrderUpdateController@update_machine')->withInput($data);
        } else if($request->get('action') === 'end') {
            return redirect()->action('OrderFindController@find')->withInput($data);
        }

        // 基板配信指示書登録処理
        $downLadOrder = new MachineDownloadOrders();
        $downLadOrder->dataSave($data['clientId'], $data['titleId'], $data['imageType'], $data['uri']);

        $data = array_merge($data,array('confirming'=>'true'));
        $data = array_merge($data,array('registerType'=>'machine'));

        $request->session()->put($data);

        return view('admin.order.upd_confirm', compact("data"));
    }

    /**
     * クライアントグループ配信指示書更新登録処理
     *
     * @param OrderRegisterClientgroupRequest $request
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function done_clientgroup(OrderRegisterClientgroupRequest $request) {

        $data = $request->all();
        if ($request->get('action') === 'back') {
            return redirect()->action('OrderUpdateController@update_clientgroup')->withInput($data);
        } else if($request->get('action') === 'end') {
            return redirect()->action('OrderFindController@find')->withInput($data);
        }

        // 基板配信指示書登録処理
        $clientgroupDownLadOrder = new ClientgroupDownloadOrders();
        $clientgroupDownLadOrder->dataSave($data['titleId'], $data['titleVer'], $data['clientGroup'], $data['imageType'], $data['uri']);

        $data = array_merge($data, array('confirming' => 'true'));
        $data = array_merge($data, array('registerType' => 'clientGroup'));

        $request->session()->put($data);

        return view('admin.order.upd_confirm', compact("data"));
    }
}
