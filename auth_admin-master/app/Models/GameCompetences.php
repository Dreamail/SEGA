<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\DB;

class GameCompetences extends Model
{
    /** 登録日時 */
    const CREATED_AT = 'create_date';
    /** 更新日時 */
    const UPDATED_AT = null;

    // テーブル指定
    protected $table = 'game_competences';
    // PrimaryKey
    protected $primaryKey = ['user_id', 'game_id'];

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
     * 登録処理
     *
     * @param $userId
     * @param $gameId
     */
    public function dataSave($userId, $gameId) {

        DB::table($this->table)
            ->insert([
                'user_id' => $userId,
                'game_id' => $gameId,
                'create_user_id' => 'system',
                'update_user_id' => 'system',
            ]);
    }

    /**
     * ユーザ情報の削除処理
     *
     * @param $userId
     */
    public function deleteUserData($userId) {
        GameCompetences::where('user_id', $userId)
            ->delete();
    }
}
