<?php

namespace App\Http\Controllers;

use App\Http\Requests\OrderRegisterGameRequest;
use App\Http\Requests\OrderRegisterMachineRequest;
use App\Http\Requests\OrderRegisterClientgroupRequest;
use App\Models\DownloadOrder;
use App\Models\MachineDownloadOrders;
use App\Models\ClientgroupDownloadOrders;
use Illuminate\Http\Request;

class OrderRegisterController extends Controller
{
    /**
     * ゲーム情報登録確認
     *
     * @param OrderRegisterGameRequest $request
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function confirm_game(OrderRegisterGameRequest $request) {

        $data = $request->all();
        $data = array_merge($data,array('confirming'=>'false'));
        $request->session()->put($data);

        // 登録済みの基板・クライアントグループ配信指示書が存在するかチェックし、存在する場合、画面に警告を表示する
        $machineDownloadOrders = new MachineDownloadOrders();
        $existsMachineDownloadOrder =
            $machineDownloadOrders->existsMachineDownloadOrder($data['gameId'], $data['imageType']);
        $clientgroupDownloadOrders = new ClientgroupDownloadOrders();
        $existsClientgroupDownloadOrder =
            $clientgroupDownloadOrders->existsClientgroupDownloadOrder($data['gameId'], $data['gameVer'], $data['imageType']);

        return view('admin.order.reg_confirm', compact(
            "data",
            "existsMachineDownloadOrder",
            "existsClientgroupDownloadOrder"
            ));
    }

    /**
     * 基板情報登録確認
     *
     * @param OrderRegisterMachineRequest $request
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function confirm_machine(OrderRegisterMachineRequest $request) {

        $data = $request->all();
        $data = array_merge($data,array('confirming'=>'false'));
        $request->session()->put($data);

        return view('admin.order.reg_confirm', compact("data"));
    }

    /**
     * クライアントグループ登録確認
     *
     * @param OrderRegisterClientgroupRequest $request
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function confirm_clientgroup(OrderRegisterClientgroupRequest $request) {
        $data = $request->all();
        $data = array_merge($data, array('confirming' => 'false'));
        $request->session()->put($data);

        return view('admin.order.reg_confirm', compact("data"));
    }

    /**
     * ゲーム情報登録処理
     *
     * @param Request $request
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function register_game(Request $request) {

        $input = $request->except('action');
        if ($request->get('action') === 'back') {
            return redirect()->action('OrderViewController@view_register')->withInput($input);
        } else if($request->get('action') === 'end') {
            return redirect('order/register');
        }

        $data = $request->all();

        // 基板・クライアントグループがもつ配信指示書URI情報をクリアする
        $machineDownloadOrders = new MachineDownloadOrders();
        $machineDownloadOrders->deleteMachineDownloadOrder($data['gameId'], $data['imageType']);
        $clientgroupDownloadOrders = new ClientgroupDownloadOrders();
        $clientgroupDownloadOrders->deleteByDownloadOrder($data['gameId'], $data['gameVer'], $data['imageType']);

        // 配信指示書登録処理
        $downLadOrder = new DownloadOrder();
        $downLadOrder->dataSave($data['gameId'], $data['gameVer'], $data['imageType'], $data['uri']);

        $data = array_merge($data,array('confirming'=>'true'));
        $data = array_merge($data,array('registerType'=>'game'));

        $request->session()->put($data);

        return view('admin.order.reg_confirm', compact("data"));
    }

    /**
     * 基板情報登録処理
     *
     * @param Request $request
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function machine_game(Request $request) {

        $input = $request->except('action');
        if ($request->get('action') === 'back') {
            return redirect()->action('OrderViewController@view_register')->withInput($input);
        } else if($request->get('action') === 'end') {
            return redirect('order/register');
        }

        $data = $request->all();

        // 基板配信指示書登録処理
        $downLadOrder = new MachineDownloadOrders();
        $downLadOrder->dataSave($data['serial'], $data['gameId'], $data['imageType'], $data['uri']);

        $data = array_merge($data,array('confirming'=>'true'));
        $data = array_merge($data,array('registerType'=>'machine'));

        $request->session()->put($data);

        return view('admin.order.reg_confirm', compact("data"));
    }

    /**
     * クライアントグループ配信指示書登録処理
     *
     * @param OrderRegisterClientgroupRequest $request
     * @return $this|\Illuminate\Contracts\View\Factory|\Illuminate\Http\RedirectResponse|\Illuminate\Routing\Redirector|\Illuminate\View\View
     */
    public function register_clientgroup(OrderRegisterClientgroupRequest $request) {

        $input = $request->except('action');
        if ($request->get('action') === 'back') {
            return redirect()->action('OrderViewController@view_register')->withInput($input);
        } else if($request->get('action') === 'end') {
            return redirect('order/register');
        }

        $data = $request->all();

        // クライアントグループ配信指示書登録処理
        $clientgroupDownLadOrder = new ClientgroupDownloadOrders();
        $clientgroupDownLadOrder->dataSave($data['titleId'], $data['titleVer'], $data['clientGroup'], $data['imageType'], $data['uri']);

        $data = array_merge($data, array('confirming' => 'true'));
        $data = array_merge($data, array('registerType' => 'clientGroup'));

        $request->session()->put($data);

        return view('admin.order.reg_confirm', compact("data"));
    }
}
