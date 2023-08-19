<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\DB;

class UserRole extends Model
{
    /** 登録日時 */
    const CREATED_AT = 'create_date';
    /** 更新日時 */
    const UPDATED_AT = null;

    // テーブル指定
    protected $table = 'user_function_roles';
    // PrimaryKey
    protected $primaryKey = ['user_id', 'role_id'];

    public $incrementing = false;

    /**
     * UserIdをKEYにしたデータ取得
     *
     * @param $userId
     * @return mixed
     */
    public function getData($userId) {

        $data = DB::table($this->table)->where('user_id', $userId)->get();
        return $data;
    }

    /**
     * ユーザに紐づいた権限の操作権限を取得
     * @param $userId
     * @return mixed
     */
    public function getAuthSetsData($userId) {

        $data = DB::table($this->table)
            ->Join('function_role_auth_sets', $this->table.'.role_id', '=', 'function_role_auth_sets.role_id')
            ->select('function_role_auth_sets.auth_set_id')
            ->where($this->table.'.user_id', $userId)
            ->orderBy('function_role_auth_sets.auth_set_id', 'ASC')
            ->get();

        return $data;
    }

    /**
     * RoleIdをKEYにしたデータ取得
     *
     * @param $role
     * @return mixed
     */
    public function getRoleCount($role) {

        $dataCount = DB::table($this->table)->where('role_id', $role)->count();
        return $dataCount;
    }

    /**
     * 登録処理
     *
     * @param $userId
     * @param $roleId
     */
    public function dataSave($userId, $roleId) {

        $data = $this->getData($userId);
        if(count($data) > 0) {
            $this->deleteUserData($userId);
        }
        $this->user_id = $userId;
        $this->role_id = $roleId;
        $this->create_user_id = 'system';

        $this->save();
    }

    /**
     * ユーザ情報の削除処理
     *
     * @param $userId
     */
    public function deleteUserData($userId) {
        UserRole::where('user_id', $userId)
            ->delete();
    }
}
