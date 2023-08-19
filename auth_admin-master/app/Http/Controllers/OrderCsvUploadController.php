<?php

namespace App\Http\Controllers;

use App\Http\Requests\OrderCsvUploadRequest;
use App\Library\Util;
use App\Models\DownloadOrder;
use App\Models\MachineDownloadOrders;
use App\Models\ClientgroupDownloadOrders;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\Log;

/**
 * 配信指示書設定CSV取込
 *
 * Class OrderCsvUploadController
 * @package App\Http\Controllers
 */
class OrderCsvUploadController extends Controller
{
    /**
     * 配信指示書設定CSV取込処理
     *
     * @param OrderCsvUploadRequest $request
     * @return \Illuminate\Http\RedirectResponse|\Illuminate\Routing\Redirector
     */
    public function csv_import(OrderCsvUploadRequest $request) {

        $target = $request->input('target');
        $file = $request->file('file');

        $lines = explode("\n", Util::deleteBOM(file_get_contents($file->getRealPath())));
        Log::info('target:'.$target);

        $error = array();
        // targetによる取込先の切り分け
        if($target === 'game') {
            $error = $this->validate_game($lines);
            if(count($error) == 0) $this->game_impport($lines);
        } elseif($target === 'machine') {
            $error = $this->validate_machine($lines);
            if(count($error) == 0) $this->machine_impport($lines);
        } else if ($target === 'clientGroup') {
            $error = $this->validate_clientgroup($lines);
            if(count($error) == 0) $this->clientgroup_impport($lines);
        }

        $result = true;
        if(count($error) > 0) $result = false;

        return view('admin.order.csvimport')
            ->with('csverror', $error)
            ->with('result', $result);
    }

    /**
     * 配信指示書取込処理
     *
     * @param $lines
     */
    private function game_impport($lines) {

        foreach ((array) $lines as $row) {

            $data = explode(",", $row);

            $titleId = $data[0];
            $titleVer = $data[1];
            $imageType = Config::get('const.IMAGE_TYPE_CHAR')[$data[2]];
            $uri = trim($data[3]); // 末尾に改行文字がある可能性があるためtrimする

            // 基板配信指示書・クライアントグループ配信指示書がもつ配信指示書URI情報をクリアする
            $machineDownloadOrders = new MachineDownloadOrders();
            $machineDownloadOrders->deleteMachineDownloadOrder($titleId, $imageType);
            $clientgroupDownloadOrders = new ClientgroupDownloadOrders();
            $clientgroupDownloadOrders->deleteByDownloadOrder($titleId, $titleVer, $imageType);

            // 配信指示書登録処理
            $downLadOrder = new DownloadOrder();
            $downLadOrder->dataSave($titleId, $titleVer, $imageType, $uri);
        }
    }

    /**
     * 基板配信指示書取込処理
     *
     * @param $lines
     */
    private function machine_impport($lines) {

        foreach ((array) $lines as $row) {

            $data = explode(",", $row);
            $titleId = $data[0];
            $clientId = $data[1];
            $imageType = Config::get('const.IMAGE_TYPE_CHAR')[$data[2]];
            $uri = trim($data[3]); // 末尾に改行文字がある可能性があるためtrimする

            // 基板配信指示書登録処理
            $downLadOrder = new MachineDownloadOrders();
            $downLadOrder->dataSave($clientId, $titleId, $imageType, $uri);
        }
    }

    /**
     * クライアントグループ配信指示書取込処理
     * @param $lines
     */
    private function clientgroup_impport(array $lines)
    {
        foreach ($lines as $row) {
            $data = explode(",", $row);

            // 空行は処理しない
            if (count($data) == 1 && trim($data[0]) == '') {
                continue;
            }

            // クライアントグループ配信指示書登録処理
            $clientgroupDownLadOrder = new ClientgroupDownloadOrders();
            $clientgroupDownLadOrder->dataSave(
                $data[0],
                $data[1],
                $data[2],
                Config::get('const.IMAGE_TYPE_CHAR')[$data[3]],
                trim($data[4]) // 末尾に改行文字がある可能性があるためtrimする
            );
        }
    }

    /**
     * 配信指示書バリデーション
     *
     * @param $lines
     * @return array
     */
    private function validate_game($lines) {

        $error = array();
        $count = 1;
        foreach ((array) $lines as $row) {

            $data = explode(",", $row);
            Log::info($data);

            if(!Util::itemsCheck($data, 4)) {
                $error[$count]['csv'] = Config::get('message.CSV_FORMAT_ERROR');
            } else {
                if(!Util::titleIdCheck($data[0]))  $error[$count]['gameId'] = Config::get('message.TITLEID_ERROR');
                if(!Util::titleVerCheck($data[1]))  $error[$count]['gameVer'] = Config::get('message.TITLEVER_ERROR');
                if(!Util::imageTypeCheck($data[2]))  $error[$count]['type'] = Config::get('message.TYPE_ERROR');
                if(!Util::uriCheck($data[3]))  $error[$count]['uri'] = Config::get('message.URI_ERROR');
            }
            $count++;
        }
        return $error;
    }

    /**
     * 基板配信指示書バリデーション
     *
     * @param $lines
     * @return array
     */
    private function validate_machine($lines) {

        $error = array();
        $count = 1;
        foreach ((array) $lines as $row) {

            $data = explode(",", $row);

            if(!Util::itemsCheck($data, 4)) {
                $error[$count]['csv'] = Config::get('message.CSV_FORMAT_ERROR');
            } else {
                if(!Util::titleIdCheck($data[0]))  $error[$count]['titleId'] = Config::get('message.TITLEID_ERROR');
                if(!Util::clientIdCheck($data[1]))  $error[$count]['clientId'] = Config::get('message.CLIENTID_ERROR');
                if(!Util::imageTypeCheck($data[2]))  $error[$count]['type'] = Config::get('message.TYPE_ERROR');
                if(!Util::uriCheck($data[3]))  $error[$count]['uri'] = Config::get('message.URI_ERROR');
            }
            $count++;
        }
        return $error;
    }

    /**
     * クライアントグループ配信指示書バリデーション
     * @param $lines
     */
    private function validate_clientgroup(array $lines)
    {
        $error = array();
        $count = 1;
        foreach ($lines as $row) {

            $data = explode(",", $row);
            Log::info($data);

            // 空行は処理しない
            if (count($data) == 1 && trim($data[0]) == '') {
                $count++;
                continue;
            }

            if(!Util::itemsCheck($data, 5)) {
                $error[$count]['csv'] = Config::get('message.CSV_FORMAT_ERROR');
            } else {
                if(!Util::titleIdCheck($data[0]))     $error[$count]['titleId'] = Config::get('message.TITLEID_ERROR');
                if(!Util::titleVerCheck($data[1]))    $error[$count]['titleVer'] = Config::get('message.TITLEVER_ERROR');
                if(!Util::clientgroupCheck($data[2])) $error[$count]['clientGroup'] = Config::get('message.CLIENT_GROUP_ERROR');
                if(!Util::imageTypeCheck($data[3]))   $error[$count]['type'] = Config::get('message.TYPE_ERROR');
                if(!Util::uriCheck($data[4]))         $error[$count]['uri'] = Config::get('message.URI_ERROR');
            }
            $count++;
        }
        return $error;
    }
}