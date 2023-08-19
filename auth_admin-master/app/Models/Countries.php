<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\DB;

class Countries extends Model
{
    /** 登録日時 */
    const CREATED_AT = 'create_date';

    // テーブル指定
    protected $table = 'countries';
    // PrimaryKey
    protected $primaryKey = 'country_code';

    public $incrementing = false;

    /**
     * 全データ取得
     *
     * @return mixed
     */
    public function getAllData() {

        $data = DB::table($this->table)->orderBy($this->table.'.country_code', 'ASC')->get()->toArray();
        return $data;
    }
}
