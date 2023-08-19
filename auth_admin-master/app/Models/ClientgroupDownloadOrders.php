<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use DB;

/**
 * クライアントグループ配信指示書
 *
 * Class ClientgroupDownloadOrders
 * @package App\Models
 */
class ClientgroupDownloadOrders extends Model
{

    /** 登録日時 */
    const CREATED_AT = 'create_date';
    /** 更新日時 */
    const UPDATED_AT = 'update_date';

    /** テーブル */
    protected $table = 'clientgroup_download_orders';
    /** PrimaryKey */
    protected $primaryKey = ['game_id', 'game_ver', 'client_group', 'type'];
    /** PKのインクリメント */
    public $incrementing = false;

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $gameId
     * @param $gameVer
     * @param $clientGroup
     * @param $type
     * @return mixed
     */
    public function findByPk($gameId, $gameVer, $clientGroup, $type) {
        return DB::table($this->table)
            ->where('title_id', $gameId)
            ->where('title_ver', $gameVer)
            ->where('client_group', $clientGroup)
            ->where('type', $type)
            ->get()
            ->toArray();
    }

    /**
     * 登録・更新処理
     *
     * @param $gameId
     * @param $gameVer
     * @param $clientGroup
     * @param $type
     * @param $uri
     */
    public function dataSave($gameId, $gameVer, $clientGroup, $type, $uri) {

        $data = $this->findByPk($gameId, $gameVer, $clientGroup, $type);
        if(count($data) > 0) {
            ClientgroupDownloadOrders::where('title_id', $gameId)
                ->where('title_ver', $gameVer)
                ->where('client_group', $clientGroup)
                ->where('type', $type)
                ->update([
                    'uri' => $uri,
                    'update_user_id' => 'system',
                    ]);
        } else {
            $this->game_id = $gameId;
            $this->game_ver = $gameVer;
            $this->client_group = $clientGroup;
            $this->type = $type;
            $this->uri = $uri;
            $this->create_user_id = 'system';
            $this->update_user_id = 'system';

            $this->save();
        }
    }

    /**
     * ゲームIDに紐づくデータを取得します
     *
     * @param $gameId
     * @return mixed
     */
    public function getClientgroupOrderData($gameId)
    {
        return DB::table($this->table)
            ->select('title_ver', 'title_id', 'client_group', 'type', 'uri', 'create_date', 'update_date')
            ->where('title_id', $gameId)
            ->orderBy('client_group', 'desc')
            ->orderBy('title_ver', 'asc')
            ->orderBy('type', 'asc')
            ->get()
            ->toArray();
    }

    /**
     * PKに紐づくレコードを削除します（1件削除）
     *
     * @param $gameId
     * @param $gameVer
     * @param $clientGroup
     * @param $type
     */
    public function deleteByPk($gameId, $gameVer, $clientGroup, $type) {
        DB::table($this->table)
            ->where('game_id', $gameId)
            ->where('game_ver', $gameVer)
            ->where('client_group', $clientGroup)
            ->where('type', $type)
            ->delete();
    }

    /**
     * 配信指示書登録・更新時に紐づくクライアントグループ配信指示書が存在するか確認します
     * @param $gameId
     * @param $gameVer
     * @param $type
     * @return boolean クライアントグループ配信指示書が存在する場合、true
     */
    public function existsClientgroupDownloadOrder($gameId, $gameVer, $type) {
        $cnt = DB::table($this->table)
            ->where('title_id', $gameId)
            ->where('title_ver', $gameVer)
            ->where('type', $type)
            ->count();
        return 0 < $cnt;
    }

    /**
     * 配信指示書登録・更新時にクライアントグループ配信指示書を削除します
     *
     * @param $gameId
     * @param $gameVer
     * @param $type
     */
    public function deleteByDownloadOrder($gameId, $gameVer, $type) {
        DB::table($this->table)
            ->where('title_id', $gameId)
            ->where('title_ver', $gameVer)
            ->where('type', $type)
            ->delete();
    }
}
