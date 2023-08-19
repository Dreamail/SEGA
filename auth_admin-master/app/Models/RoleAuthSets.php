<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;

class RoleAuthSets extends Model
{
    /** 登録日時 */
    const CREATED_AT = 'create_date';
    /** 更新日時 */
    const UPDATED_AT = null;

    // テーブル指定
    protected $table = 'function_role_auth_sets';
    // PrimaryKey
    protected $primaryKey = ['role_id', 'auth_set_id'];

    public $incrementing = false;

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $roleId
     * @param $authSetId
     * @return mixed
     */
    public function getData($roleId, $authSetId) {

        $data = DB::table($this->table)
            ->where('role_id', $roleId)
            ->where('auth_set_id', $authSetId)
            ->get();
        return $data;
    }

    /**
     * RoleIdをKEYにしたデータ取得
     *
     * @param $roleId
     * @return mixed
     */
    public function getDataRoleId($roleId) {

        $data = DB::table($this->table)
            ->where('role_id', $roleId)
            ->get();
        return $data;
    }

    /**
     * 権限IDをキーに操作権限を取得
     *
     * @param $roleId
     * @return mixed
     */
    public function getDataByRoleId($roleId){

        $data = DB::table($this->table)
            ->Join('function_auth_sets', $this->table.'.auth_set_id', '=', 'function_auth_sets.auth_set_id')
            ->select('function_auth_sets.name')
            ->where('role_id', $roleId)
            ->orderBy($this->table.'.auth_set_id', 'ASC')
            ->get()
            ->toArray();

        return $data;
    }

    /**
     * 登録・更新処理
     *
     * @param $roleId
     * @param $authSetId
     */
    public function dataSave($roleId, $authSetId) {

        DB::table($this->table)
            ->insert([
                'role_id' => $roleId,
                'auth_set_id' => $authSetId,
                'create_user_id' => 'system',
            ]);
    }

    /**
     * 権限情報の削除処理
     *
     * @param $roleId
     */
    public function deleteRoleAuthData($roleId) {

        $data = $this->getDataRoleId($roleId);
        if(count($data) > 0) {
            RoleAuthSets::where('role_id', $roleId)
                ->delete();
        }
    }
}
