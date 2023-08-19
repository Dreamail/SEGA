<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\DB;

class AuthSets extends Model
{
    /** 登録日時 */
    const CREATED_AT = 'create_date';

    // テーブル指定
    protected $table = 'function_auth_sets';
    // PrimaryKey
    protected $primaryKey = 'auth_set_id';

    public $incrementing = false;

    /**
     * 全データ取得
     *
     * @return mixed
     */
    public function getAllData() {

        $data = DB::table($this->table)->orderBy($this->table.'.auth_set_id', 'ASC')->get()->toArray();
        return $data;
    }

    /**
     * 管理者機能以外のデータを取得
     *
     * @return mixed
     */
    public function getNotManData() {

        $data = DB::table($this->table)
            ->where('auth_set_id', '!=', '500_USE_DAT')
            ->orderBy($this->table.'.auth_set_id', 'ASC')->get()->toArray();
        return $data;
    }

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $authSetId
     * @return mixed
     */
    public function getData($authSetId) {

        $data = DB::table($this->table)->where('auth_set_id', $authSetId)->get();
        return $data;
    }

    /**
     * PKを条件にレコードが存在するかチェックします
     * @param $authSetId
     * @return bool 存在する場合、true
     */
    public function existsByPk($authSetId) {
        $result = $this->getData($authSetId);
        return count($result) == 1;
    }
}
