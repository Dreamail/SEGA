<?php

namespace App\Http\Controllers;

use App\Http\Requests\MachineCsvUploadRequest;
use App\Http\Requests\MachineImportRequest;
use App\Library\Util;
use App\Models\AppDeliverReports;
use App\Models\Games;
use App\Models\LinkMachinePlace;
use App\Models\MachineDeletionHistories;
use App\Models\MachineDownloadOrders;
use App\Models\Machines;
use App\Models\OptDeliverReports;
use App\Models\Places;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\Log;

/**
 * 基盤情報CSV取込
 *
 * Class MachineImportController
 * @package App\Http\Controllers
 */
class MachineImportController extends Controller
{
    /**
     * CSV取込による基板情報の登録・更新処理
     *
     * @param MachineImportRequest $request
     * @return \Illuminate\Http\RedirectResponse|\Illuminate\Routing\Redirector
     */
    public function machine_import(MachineImportRequest $request){

        $gameId = $request->input('gameId');
        $reserve = $request->input('reserve');
        $file = $request->file('file');

        $error = $this->validate_machine($file,$gameId);

        if(count($error) == 0) {

            $buffer = mb_convert_encoding(
                Util::deleteBOM(file_get_contents($file->getRealPath())),
                Config::get('const.AFTER_CHARACTER_CODE'), Config::get('const.BEFORE_CHARACTER_CODE'));
            $fp = tmpfile();
            fwrite($fp, $buffer);
            rewind($fp);

            $link = new LinkMachinePlace();

            while (($data = fgetcsv($fp)) !== FALSE) {

                // 削除理由番号（削除理由番号は1固定）
                $deletionReasonNo = array_key_exists(6, $data) ? $data[6] : null;

                // 基板情報、店舗情報モデル
                $machine = new Machines();

                // シリアル番号をKEYに基板情報取得
                $machineData = $machine->getDataPlace($data[2]);

                // 削除理由存在判定
                if(isset($deletionReasonNo) && 0 < strlen($deletionReasonNo)) {

                    if(count($machineData) > 0) {
                        // 削除履歴登録
                        $this->createMachineDeletionHistories($machineData, $deletionReasonNo);
                        // 基盤情報削除
                        $this->removeMachine($machineData);
//                        // 基板配信指示書削除
//                        $this->removeMachineDownloadOrders($machineData);
                        //基板店舗管理情報削除
                        $link->deleteData($data[2]);
//                        // 配信PC稼働状況ログ削除
//                        $this->removeLoaderStateLogs($machineData);
//                        // 配信レポート削除
//                        $this->removeDeliverReports($machineData);

                    } else {
                        // 削除対象なし
                        continue;
                    }
                } else {
                    $place = new Places();
                    $placeId = $place->getData($data[1]);
                    foreach ($placeId as $val) {
                            $placeId = $val->place_id;
                    }
                    // データ設定および登録・更新処理
                    $machine->dataSave($data,$placeId,$reserve);
                    $link->dataSave($data);
                }
            }
        }

        $result = true;
        if(count($error) > 0) $result = false;

        return view('admin.upload.machine')
            ->with('csverror', $error)
            ->with('result', $result)
            ->with('gameData', $this->getGameData());
    }

    /**
     * 基板情報バリデーション
     *
     * @param $lines
     * @return array
     */
    private function validate_machine($lines,$gameId)
    {
        $error = array();
        $count = 1;
        $buffer = mb_convert_encoding(
            Util::deleteBOM(file_get_contents($lines->getRealPath())),
            Config::get('const.AFTER_CHARACTER_CODE'), Config::get('const.BEFORE_CHARACTER_CODE'));
        $fp = tmpfile();
        fwrite($fp, $buffer);
        rewind($fp);

        while (($data = fgetcsv($fp)) !== FALSE) {
            $data = mb_convert_encoding($data, Config::get('const.BEFORE_CHARACTER_CODE'), Config::get('const.AFTER_CHARACTER_CODE'));
            if(!Util::itemsCheck($data, 6) && !Util::itemsCheck($data, 7)) {
                $error[$count]['csv'] = Config::get('message.CSV_FORMAT_ERROR');
            } else {
                if (!Util::isEmpty($data[0])) { // 基板情報の店舗名は空を許容（省略可）
                    if (!Util::digitsCheck($data[0], 80)) $error[$count]['name'] = Config::get('message.NAME_FORMAT_ERROR');
                    if(Util::nameCheck($data[0]))  $error[$count]['name'] = Config::get('message.NAME_ERROR');
                }
                if(!Util::allNetIdCheck($data[1]))  $error[$count]['allnet_id'] = Config::get('message.ALLNET_ID_ERROR');

                if (!Util::isEmpty($data[0]) && !isset($error[$count]['name']) && !isset($error[$count]['allnet_id'])) {
                    // 店舗名の入力あり、かつ店舗名・All.NetIDがエラーでない場合、CSVの店舗名とAll.NetIDに紐づく店舗名が一致するかチェックを行う
                    if(!Util::checkPlaceNameByAllNetId($data[0], $data[1]))  $error[$count]['name'] = Config::get('message.NAME_DIFFER_ERROR');
                }
                if(!Util::serialCheck($data[2]))  $error[$count]['serial'] = Config::get('message.MASHINEID_ERROR');
                $game = new Games();
                if(!$game->existsByPk($gameId)) $error[$count]['gameId'] = str_replace('$1', $gameId,Config::get('message.TITLE_EXI_ERROR'));
                //CSV内のゲームIDとプルダウンで選択したゲームIDが一致するかチェックを行う
                if(strcmp($gameId,$data[3])!=0 && ($data[3] != '' || $data[3] != Null)){
                    $error[$count]['gameId'] = Config::get('message.GAMEID_DIFFER_ERROR');
                }
                if(!Util::gameIdCheck($data[3]))  $error[$count]['gameId'] = Config::get('message.TITLEID_ERROR');
                if(!Util::settingCheck($data[4]))  $error[$count]['setting'] = Config::get('message.SETTING_ERROR');
                if(!Util::placeIpCheck($data[5])) $error[$count]['placeIp'] = Config::get('message.PLACEIP_ERROR');
                if (array_key_exists(6, $data)) {
                    if(!Util::delFlgCheck($data[6]))  $error[$count]['deletionReasonNo'] = Config::get('message.DELFLG_ERROR');
                }
            }
            $count++;
        }
        return $error;
    }

    /**
     * 基盤情報削除
     *
     * @param $machines
     */
    private function removeMachine($machines) {

        // 基板情報モデル
        $machine = new Machines();

        // データ削除
        foreach ($machines as $row)
        {
            $machine->deleteData($row->serial);
        }
    }

    /**
     * 配信PCの稼働状況ログ削除
     *
     * @param $machines
     */
    private function removeLoaderStateLogs($machines) {

        // 配信PCの稼働状況ログモデル
        $loaderStateLogs = new LoaderStateLogs();

        // データ削除
        $loaderStateLogs->deleteData($machines->client_id);

    }

    /**
     * 基板配信指示書削除
     *
     * @param $machines
     */
    private function removeMachineDownloadOrders($machines) {

        // 基板配信指示書モデル
        $machineDownloadOrders = new MachineDownloadOrders();

        // データ削除
        foreach ($machines as $row)
        {
            $machineDownloadOrders->deleteData($row->serial);
        }

    }

    /**
     * 配信レポート削除処理
     *
     * @param $machines
     */
    private function removeDeliverReports($machines) {
        $appDeliverReports = new AppDeliverReports();
        $optDeliverReports = new OptDeliverReports();

        // アプリ配信レポート削除
        $appDeliverReports->deleteData($machines->client_id);
        // オプション配信レポート削除
        $optDeliverReports->deleteData($machines->client_id);
    }

    /**
     * 削除履歴登録
     *
     * @param $machines
     * @param $place
     * @param $deletionReasonNo
     */
    private function createMachineDeletionHistories($machines, $deletionReasonNo){

        foreach ($machines as $rows) {

            // 削除理由情報モデル
            $machineDeletionHistories = new MachineDeletionHistories();

            // データ設定
            $machineDeletionHistories->setData($rows, 'system', $deletionReasonNo);

            // 登録処理（更新日付は不使用）
            $machineDeletionHistories->timestamps = false;
            $machineDeletionHistories->save();
        }


    }
}
