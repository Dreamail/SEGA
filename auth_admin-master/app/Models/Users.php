<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\DB;

class Users extends Model
{
    /** 登録日時 */
    const CREATED_AT = 'create_date';
    /** 更新日時 */
    const UPDATED_AT = 'update_date';

    // テーブル指定
    protected $table = 'users';
    // PrimaryKey
    protected $primaryKey = 'user_id';

    public $incrementing = false;

    /**
     * ユーザ情報に紐づいた権限情報も併せて取得
     * @param $limit
     * @param $userId
     * @param $invalided
     * @return mixed
     */
    public function getFindData($limit, $userId = null, $invalided = null) {

        $data = DB::table($this->table)
            ->leftJoin('user_function_roles', $this->table.'.user_id', '=', 'user_function_roles.user_id')
            ->select($this->table.'.user_id', $this->table.'.invalided', 'user_function_roles.role_id')
            ->addSelect(DB::raw("to_char(users.update_date,'yyyy-mm-dd hh24:mi:ss') AS update_date"))
            ->orderBy($this->table.'.user_id', 'DESC');

        if(!is_null($userId)) {
            $data = $data->where($this->table.'.user_id', 'like', '%'.$userId.'%');
        }

        if(!is_null($invalided)) {
            $data = $data->where($this->table.'.invalided', '1');
        }

        return $data->paginate(intval($limit));
    }

    /**
     * ログイン可能ユーザ判定
     *
     * @param $userId
     * @return mixed
     */
    public function loginUserCheck($userId) {

        $data = DB::table($this->table)
            ->where('user_id', $userId)
            ->where('invalided', Config::get('const.FLG')['OFF'])->get();

        return count($data) == 1;
    }

    /**
     * ログイン時に使用するユーザ情報取得
     *
     * @param $userId
     * @param $password
     * @return mixed
     */
    public function getUserData($userId, $password) {

        $data = DB::table($this->table)
            ->where('user_id', $userId)
            ->where('password', $password)
            ->where('invalided', Config::get('const.FLG')['OFF'])->get();
        return $data;
    }

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $userId
     * @return mixed
     */
    public function getData($userId) {

        $data = DB::table($this->table)->where('user_id', $userId)->get();
        return $data;
    }

    /**
     * PKを条件にレコードが存在するかチェックします
     * @param $userId
     * @return bool 存在する場合、true
     */
    public function existsByPk($userId) {
        $result = $this->getData($userId);
        return count($result) == 1;
    }

    /**
     * 凍結されたのユーザ情報取得
     * @param $userId
     * @return mixed
     */
    public function getInvelidData($userId) {

        $data = DB::table($this->table)
            ->where('invalided', Config::get('const.FLG')['ON'])->get()->toArray();

        if(self::isEmpty($userId)) {
            $data = $data->where('user_id', $userId);
        }
        return $data;
    }

    /**
     * 登録・更新処理
     *
     * @param $userId
     * @param $password
     */
    public function dataSave($userId, $password) {

        $data = $this->getData($userId);
        if(count($data) > 0) {
            if(empty($password)) {
                Users::where('user_id', $userId)
                    ->update(['update_user_id' => 'system']);
            } else {
                $hash_password = hash('sha256', $password);
                Users::where('user_id', $userId)
                    ->update(['password' => $hash_password, 'update_user_id' => 'system']);
            }
        } else {
            $this->user_id = $userId;
            $this->password = hash('sha256', $password);
            $this->company_id = '0';
            $this->invalided = '0';
            $this->create_user_id = 'system';
            $this->update_user_id = 'system';

            $this->save();
        }
    }

    /**
     * パスワード更新処理
     *
     * @param $userId
     * @param $password
     */
    public function passSave($userId, $password) {

        $data = $this->getData($userId);
        if(count($data) > 0) {
            $hash_password = hash('sha256', $password);
            Users::where('user_id', $userId)
                ->update(['password' => $hash_password, 'update_user_id' => 'system']);
        }
    }

    /**
     * 凍結、解除処理
     * @param $userId
     */
    public function updateInvelided($userId, $invelided, $updateUser = "system") {
        Users::where('user_id', $userId)
            ->update(['invalided' => $invelided, 'update_user_id' => $updateUser]);
    }

    /**
     * ユーザ情報の削除処理
     *
     * @param $userId
     */
    public function deleteUserData($userId) {
        Users::where('user_id', $userId)
            ->delete();
    }
}
