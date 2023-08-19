<?php

namespace App\Http\Controllers;

use App\Http\Requests\MachineUpdateConfirmRequest;
use App\Http\Requests\MachineUpdateRequest;
use App\Models\Games;
use App\Models\LinkMachinePlace;
use App\Models\Machines;
use App\Models\MachineStatuses;
use App\Models\Places;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\Log;

/**
 * 基板情報
 * 参照・更新・削除画面表示
 *
 * Class ReferenceMachineController
 * @package App\Http\Controllers
 */
class ReferenceMachineController extends Controller
{
    /**
     * 基板情報
     * @return mixed
     */
    public function view(Request $request) {
        $request->session()->forget('updateSerial');
        return view('admin.reference.machine.find')
            ->with('gameData', $this->getGameData())
            ->with('placeData', $this->getPlaceData());
    }

    /**
     * 基板情報検索
     *
     * @param Request $request
     * @return mixed
     */
    public function find(Request $request) {

        $request->session()->forget('updateSerial');

        $serial = null;
        $allNetId = null;
        $gameId = null;
        $placeName = null;
        if($request->has('searchGameId')) {

            $rules =  ['searchGameId' => ['required','regex:/^[A-Z0-9]{3,5}$/']];
            $messages =  [
                'searchGameId.required' => Config::get('message.REQUIRED_TITLEID'),
                'searchGameId.regex' => Config::get('message.TITLEID_ERROR'),
            ];

            $validator = $this->getValidationFactory()->make($request->all(), $rules, $messages, []);

            if ($validator->fails()) {
                return view('admin.reference.machine.find')
                    ->withErrors($validator)
                    ->withInput($request->all)
                    ->with('gameData', $this->getGameData())
                    ->with('placeData', $this->getPlaceData());
            }

            $serial = $request->input('searchSerial');
            $allNetId = $request->input('searchAllNetId');
            $gameId = $request->input('searchGameId');
            $placeName = $request->input('searchPlaceName');

            $request->session()->put('searchSerial', $serial);
            $request->session()->put('searchAllNetId', $allNetId);
            $request->session()->put('searchGameId', $gameId);
            $request->session()->put('searchPlaceName', $placeName);

        } else {
            $serial = $request->session()->get('searchSerial');
            $allNetId = $request->session()->get('searchAllNetId');
            $gameId = $request->session()->get('searchGameId');
            $placeName = $request->session()->get('searchPlaceName');
        }

        $machine = new Machines();

        $data = $machine->getFindData($serial,$allNetId,$gameId,$placeName);

        return view('admin.reference.machine.find')
            ->with('serial', $serial)
            ->with('allNetId', $allNetId)
            ->with('gameId', $gameId)
            ->with('placeName', $placeName)
            ->with('data', $data)
            ->with('gameData', $this->getGameData())
            ->with('placeData', $this->getPlaceData());
    }

    /**
     * 更新
     *
     * @return $this
     */
    public function update(Request $request)
    {
        $serial = null;
        if ($request->has('serial')) {
            $serial = $request->input('serial');
        } else {
            $serial = $request->old('serial');
        }
        if($request->session()->has('updateSerial')) {
            if($request->session()->get('updateSerial') !== $serial) {
                $serial = $request->session()->get('updateSerial');
            }
        }

        $request->session()->put('updateSerial', $serial);

        $machine = new Machines();
        if(!$machine->existsByPk($serial)) {
            return redirect('manage/reference/machine')->withErrors(str_replace('$1', $serial,Config::get('message.CLIENT_EXI_ERROR')));
        }

        $data = $machine->getFindData($serial);

        return view('admin.reference.machine.update')
            ->with('data', $data[0])
            ->with('gameData', $this->getGameData())
            ->with('placeData', $this->getPlaceData());
    }

    /**
     * 更新確認
     * @param MachineUpdateRequest $request
     * @return mixed
     */
    public function upd_confirm(MachineUpdateRequest $request) {

        $data = $request->all();
        $data = array_merge($data,array('confirming'=>'false'));

        if($request->session()->has('updateSerial')) {
            if($request->session()->get('updateSerial') !== $data['serial']) {
                return redirect('manage/reference/machine')->withErrors(Config::get('message.FRAUD_ERROR'));
            }
        } else {
            return redirect('manage/reference/machine')->withErrors(Config::get('message.FRAUD_ERROR'));
        }

        $machine = new Machines();
        if(!$machine->existsByPk($data['serial'])) {
            return redirect('manage/reference/machine')->withErrors(str_replace('$1', $data['serial'],Config::get('message.CLIENT_EXI_ERROR')));
        }

        $game = new Games();
        $gameData = $game->getData($data['gameId']);

        $place = new Places();
        $placeData = $place->getData($data['allNetId']);

        $request->session()->put($data);
        return view('admin.reference.machine.upd_confirm')
            ->with('data', $data)
            ->with('gameData', $gameData[0])
            ->with('placeData', $placeData[0]);
    }

    /**
     * 更新処理
     *
     * @param MachineUpdateConfirmRequest $request
     * @return $this
     */
    public function upd_process(MachineUpdateConfirmRequest $request)
    {
        $data = $request->all();
        // 戻るボタン押下時処理
        if ($request->get('action') === 'back') {
            return redirect()->action('ReferenceMachineController@update')->withInput($data);
        } else if ($request->get('action') === 'end') {
            return redirect('manage/reference/machine');
        }

        if($request->session()->has('updateSerial')) {
            if($request->session()->get('updateSerial') !== $data['serial']) {
                return redirect('manage/reference/machine')->withErrors(Config::get('message.FRAUD_ERROR'));
            }
        } else {
            return redirect('manage/reference/machine')->withErrors(Config::get('message.FRAUD_ERROR'));
        }

        $game = new Games();
        $gameData = $game->getData($data['gameId']);

        $place = new Places();
        $placeData = $place->getData($data['allNetId']);
        foreach ($placeData as $val) {
            $placeId = $val->place_id;
        }

        $machine = new Machines();
        $machine->dataCreateData($data['serial'], $data['allNetId'], $data['gameId'], $data['setting'],$placeId);

        $link = new LinkMachinePlace();
        $link->dataCreateData($data['serial'],$data['placeIp']);

        $data = array_merge($data,array('confirming'=>'true'));

        $request->session()->forget('updateSerial');

        return view('admin.reference.machine.upd_confirm')
            ->with('data', $data)
            ->with('gameData', $gameData[0])
            ->with('placeData', $placeData[0]);
    }

    /**
     * 削除処理
     *
     * @param Request $request
     * @return $this
     */
    public function delete(Request $request) {

        $data = $request->all();

        $machine = new Machines();
        $machine->deleteData($data['serial']);

        $link = new LinkMachinePlace();
        $link->deleteData($data['serial']);

        $machineStatus = new MachineStatuses();
        $machineStatus->deleteData($data['serial']);

        return redirect('manage/reference/machine');
    }
}
