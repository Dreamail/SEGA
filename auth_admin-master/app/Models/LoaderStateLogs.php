<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use DB;

/**
 * 配信PC稼働状況ログ
 *
 * Class LoaderStateLogs
 * @package App\Models
 */
class LoaderStateLogs extends Model
{
    // テーブル指定
    protected $table = 'loader_state_logs';
    // PrimaryKey
    protected $primaryKey = ['client_id','log_date'];

    public $incrementing = false;

    public function deleteData($clientId) {
        DB::table($this->table)->where('client_id', $clientId)->delete();
    }
}
