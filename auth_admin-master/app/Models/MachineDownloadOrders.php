<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use DB;
use Illuminate\Support\Facades\Config;

/**
 * 基板配信指示書
 *
 * Class MachineDownloadOrders
 * @package App\Models
 */
class MachineDownloadOrders extends Model
{

    const CREATED_AT = 'create_date';
    const UPDATED_AT = 'update_date';

    // テーブル指定
    protected $table = 'machine_download_orders';
    // PrimaryKey
    protected $primaryKey = ['serial','type'];

    public $incrementing = false;

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $clientId
     * @param $type
     * @return mixed
     */
    public function getData($clientId, $type) {

        $data = DB::table($this->table)
            ->where('serial', $clientId)
            ->where('type', $type)
            ->get()
            ->toArray();
        return $data;
    }

    /**
     * 基板配信指示書削除(KEY:TYPE)
     *
     * @param $clientId
     */
    public function deleteData($clientId) {
        // アプリケーションタイプの削除
        DB::table($this->table)->where('serial',$clientId)->where('type', Config::get('const.APP'))->delete();
        // オプションタイプの削除
        DB::table($this->table)->where('serial',$clientId)->where('type', Config::get('const.OPT'))->delete();
    }

    /**
     * 配信指示書登録・更新時に紐づく基板配信指示書が存在するか確認します
     * @param $gameId
     * @param $type
     * @return boolean クライアントグループ配信指示書が存在する場合、true
     */
    public function existsMachineDownloadOrder($gameId, $type) {
        $cnt = DB::table($this->table)
            ->where('game_id', $gameId)
            ->where('type', $type)
            ->count();
        return 0 < $cnt;
    }

    /**
     * 基板がもつ配信指示書URI情報をクリア
     *
     * @param $gameId
     * @param $type
     */
    public function deleteMachineDownloadOrder($gameId, $type) {

        DB::table($this->table)->where('game_id',$gameId)->where('type', $type)->delete();
    }

    /**
     * ゲームIDをKEYに基板配信指示書を取得
     *
     * @param $gameId
     * @return mixed
     */
    public function getMachineOrderData($gameId) {

        $orderData = DB::table($this->table)
                        ->leftJoin('machine_statuses', 'machine_download_orders.serial', '=', 'machine_statuses.serial')
                        ->leftJoin('machines', 'machine_download_orders.serial', '=', 'machines.serial')
                        ->select('machine_download_orders.serial','machine_download_orders.game_id', 'machine_download_orders.uri', 'machine_download_orders.type', 'machine_download_orders.create_date', 'machine_download_orders.update_date')
                        ->where('machine_download_orders.game_id', $gameId)
                        ->whereNotNull('machine_download_orders.uri')
//                        ->whereRaw('NOW() BETWEEN machines.expiratin_date_start AND machines.expiratin_date_end')
                        ->orderBy('machine_download_orders.serial')
                        ->orderBy('machine_download_orders.type')
                        ->get()
                        ->toArray();
        return $orderData;
    }
    /**
     * 登録・更新処理
     *
     * @param $serial
     * @param $gameId
     * @param $type
     * @param $uri
     */
    public function dataSave($serial, $gameId, $type, $uri) {

        $data = $this->getData($serial, $type);
        if(count($data) > 0) {
            MachineDownloadOrders::where('serial', $serial)
                ->where('type', $type)
                ->update(['uri' => $uri, 'update_user_id' => 'system']);
        } else {
            $this->serial = $serial;
            $this->game_id = $gameId;
            $this->type = $type;
            $this->uri = $uri;
            $this->create_user_id = 'system';
            $this->update_user_id = 'system';

            $this->save();
        }
    }

    /**
     * 削除処理
     *
     * @param $serial
     * @param $type
     */
    public function deleteDataPk($serial, $type) {

        DB::table($this->table)
            ->where('serial', $serial)
            ->where('type', $type)->delete();
    }
}
