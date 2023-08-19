<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;

class OptDeliverReportsHistory extends Model
{
    // テーブル指定
    protected $table = 'opt_deliver_reports_history';
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
            ->addSelect(DB::raw("to_char(opt_deliver_reports_history.access_time,'yyyy-mm-dd hh24:mi:ss') AS access"))
            ->addSelect(DB::raw("(CASE WHEN opt_deliver_reports_history.segs_downloaded = 0 OR opt_deliver_reports_history.segs_total = 0 THEN 0 
                               ELSE ROUND(opt_deliver_reports_history.segs_downloaded / opt_deliver_reports_history.segs_total * 100) END)
                               AS download_ratio"))
            ->where($this->table.'.client_id', $clientId)
            ->orderBy('create_date', 'DESC')
            ->paginate(intval($limit));

        return $data;
    }
}
