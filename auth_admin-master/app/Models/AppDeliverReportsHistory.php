<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;

class AppDeliverReportsHistory extends Model
{
    // テーブル指定
    protected $table = 'app_deliver_reports_history';
    // PrimaryKey
    protected $primaryKey = ['client_id','create_date'];

    public $incrementing = false;

    /**
     * クライアントIDをKEYに履歴情報を取得
     *
     * @param $clientId
     * @return mixed
     */
    public function  seachByClientId($clientId, $limit) {
        $data = DB::table($this->table)
            ->select($this->table.'.*')
            ->addSelect(DB::raw("to_char(app_deliver_reports_history.access_time,'yyyy-mm-dd hh24:mi:ss') AS access"))
            ->addSelect(DB::raw("(CASE WHEN app_deliver_reports_history.segs_downloaded = 0 OR app_deliver_reports_history.segs_total = 0 THEN 0 
                               ELSE ROUND(app_deliver_reports_history.segs_downloaded / app_deliver_reports_history.segs_total * 100) END)
                               AS download_ratio"))
            ->where($this->table.'.client_id', $clientId)
            ->orderBy('create_date', 'DESC')
            ->paginate(intval($limit));

        return $data;
    }
}
