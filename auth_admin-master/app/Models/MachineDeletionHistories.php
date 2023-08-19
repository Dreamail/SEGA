<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

/**
 * 基板削除履歴
 *
 * Class MachineDeletionHistories
 * @package App\Models
 */
class MachineDeletionHistories extends Model
{

    const CREATED_AT = 'create_date';

    // テーブル指定
    protected $table = 'machine_deletion_histories';
    // PrimaryKey
    protected $primaryKey = ['machine','create_date'];

    public $incrementing = false;

    /**
     * データを設定
     *
     * @param $machine
     * @param $userId
     * @param $deletionReasonNo
     */
    public function setData($machine, $userId, $deletionReasonNo) {

        $this->serial= $machine->serial;
        $this->game_id = $machine->game_id;
        $this->allnet_id = $machine->allnet_id;
        $this->place_name = $machine->name;
        $this->reason_id = $deletionReasonNo;
        $this->create_user_id = $userId;
    }
}
