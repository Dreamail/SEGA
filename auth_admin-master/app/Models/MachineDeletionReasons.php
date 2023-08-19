<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

/**
 * 基板削除理由
 *
 * Class MachineDeletionReasons
 * @package App\Models
 */
class MachineDeletionReasons extends Model
{
    // テーブル指定
    protected $table = 'machine_deletion_reasons';
    // PrimaryKey
    protected $primaryKey = 'reason_id';

    public $incrementing = false;

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $reasonId
     * @return mixed
     */
    public function getData($reasonId) {

        $data = DB::table($this->table)->where('reason_id', $reasonId)->get();
        return $data;
    }
}
