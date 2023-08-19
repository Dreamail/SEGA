<?php

namespace App\Models;

use Illuminate\Support\Carbon;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\Log;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;

class MachineStatuses extends Model
{
    const CREATED_AT = 'create_date';
    const UPDATED_AT = 'update_date';

    // テーブル指定
    protected $table = 'machine_statuses';
    // PrimaryKey
    protected $primaryKey = 'machine';

    public $incrementing = false;

    /**
     * 基板ステータスデータ削除
     *
     * @param $serial
     */
    public function deleteData($serial) {
        DB::table($this->table)->where('serial', $serial)->delete();
    }
}
