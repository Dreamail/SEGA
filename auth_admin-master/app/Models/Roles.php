<?php

namespace App\Models;

use Session;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\DB;

class Roles extends Model
{
    /** 登録日時 */
    const CREATED_AT = 'create_date';
    /** 更新日時 */
    const UPDATED_AT = 'update_date';

    // テーブル指定
    protected $table = 'function_roles';
    // PrimaryKey
    protected $primaryKey = 'role_id';

    public $incrementing = false;

    /**
     * SUPERUER以外の権限情報を取得
     *
     * @return mixed
     */
    public function getNonSuperUserData($limit) {

        $data = DB::table($this->table)
            ->select($this->table.'.role_id', $this->table.'.explanation')
            ->addSelect(DB::raw("to_char(function_roles.update_date,'yyyy-mm-dd hh24:mi:ss') AS update_date"))
            ->where('role_id', '!=', 'SUPERUSER')
            ->orderBy($this->table.'.role_id', 'DESC')
            ->paginate(intval($limit));
        return $data;
    }

    /**
     * 全データ取得
     *
     * @return mixed
     */
    public function getAllData() {

        $data = DB::table($this->table)->get()->toArray();
        return $data;
    }

    /**
     * 全データ取得
     *
     * @return mixed
     */
    public function getNonSuperData() {

        $data = DB::table($this->table)->where('role_id', '!=', 'SUPERUSER')->get()->toArray();
        return $data;
    }

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $roleId
     * @return mixed
     */
    public function getData($roleId) {

        $data = DB::table($this->table)->where('role_id', $roleId)->get()->toArray();
        return $data;
    }

    /**
     * PKを条件にレコードが存在するかチェックします
     * @param $roleId
     * @return bool 存在する場合、true
     */
    public function existsByPk($roleId) {
        $result = $this->getData($roleId);
        return count($result) == 1;
    }

    /**
     * 登録・更新処理
     *
     * @param $roleId
     * @param $explanation
     */
    public function dataSave($roleId, $explanation) {

        $data = $this->getData($roleId);
        if(count($data) > 0) {
            Roles::where('role_id', $roleId)
                ->update(['explanation' => $explanation, 'update_user_id' => Session::get('login')]);
        } else {
            $this->role_id = $roleId;
            $this->explanation = $explanation;
            $this->create_user_id = Session::get('login');
            $this->update_user_id = Session::get('login');

            $this->save();
        }
    }

    /**
     * 権限情報の削除処理
     *
     * @param $roleId
     */
    public function deleteRoleData($roleId) {
        Roles::where('role_id', $roleId)
            ->delete();
    }
}
