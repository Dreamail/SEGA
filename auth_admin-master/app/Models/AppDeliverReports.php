<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

/**
 * アプリ配信レポート
 *
 * Class AppDeliverReports
 * @package App\Models
 */
class AppDeliverReports extends Model
{
    // テーブル指定
    protected $table = 'app_deliver_reports';
    // PrimaryKey
    protected $primaryKey = 'serial';

    public $incrementing = false;

    public function deleteData($clientId) {
        DB::table($this->table)->where('serial', $clientId)->delete();
    }

    private function mainQuery() {

        $reportsData = DB::table($this->table)
            ->join('machines', 'machines.serial', '=', $this->table.'.serial')
            ->join('places', 'places.place_id', '=', 'machines.place_id')
            ->select($this->table.'.*','places.place_id','places.name','machines.game_id')
            ->addSelect(DB::raw("to_char(app_deliver_reports.update_date,'yyyy-mm-dd hh24:mi:ss') AS update"))
            ->addSelect(DB::raw("(CASE WHEN app_deliver_reports.segs_downloaded = 0 OR app_deliver_reports.segs_total = 0 THEN 0
                               ELSE ROUND(app_deliver_reports.segs_downloaded / app_deliver_reports.segs_total * 100) END)
                               AS download_ratio"));
            //->whereRaw(DB::raw("machines.version = (SELECT MAX(version) FROM machines WHERE machines.serial = app_deliver_reports.serial)"));

        return $reportsData;
    }

    /**
     * 基板シリアルをKEYにレポート情報を取得
     *
     * @param $serial
     * @return mixed
     */
    public function  seachByClientId($serial) {
        $reportsData = $this->mainQuery();
        $data = $reportsData->where($this->table.'.serial', $serial)
                    ->get()
                    ->toArray();

        return $data;
    }

    /**
     * ゲームIDをKEYにレポート情報を取得
     *
     * @param $gameId
     * @param $dlCode
     * @return mixed
     */
    public function  seachByTitleId($gameId, $dlCode) {
        $reportsData = $this->mainQuery();
        $reportsData->where('machines.game_id', $gameId);

        if(isset($dlCode) && count($dlCode) > 0) {
            $reportsData->whereIn('app_deliver_reports.download_state', $dlCode);
        }
        $data = $reportsData->orderBy('places.place_id', 'ASC')
                    ->orderBy('app_deliver_reports.serial', 'ASC')
                    ->get()
                    ->toArray();

        return $data;
    }

    /**
     * 店舗IDをKEYにレポート情報を取得
     *
     * @param $placeId
     * @return mixed
     */
    public function  seachByPlaceId($placeId) {
        $reportsData = $this->mainQuery();
        $data = $reportsData->where('machines.place_id', $placeId)
                    ->orderBy('machines.game_id', 'ASC')
                    ->orderBy('app_deliver_reports.serial', 'ASC')
                    ->get()
                    ->toArray();

        return $data;
    }

    /**
     * クライアントグループをKEYにレポート情報を取得
     *
     * @param $clientGroup
     * @return mixed
     */
    public function  seachByClientGroup($clientGroup) {
        $reportsData = $this->mainQuery();
        $data = $reportsData->join('machine_clientgroups', 'machine_clientgroups.client_id', '=', $this->table.'.client_id')
            ->addSelect('machine_clientgroups.client_group')
            ->where('machine_clientgroups.client_group', $clientGroup)
            ->orderBy('places.place_id', 'ASC')
            ->orderBy('app_deliver_reports.client_id', 'ASC')
            ->get()
            ->toArray();

        return $data;
    }
}
