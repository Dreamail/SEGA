<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use DB;

/**
 * クライアントグループ
 *
 * Class ClientGroups
 * @package App\Models
 */
class ClientGroups extends Model
{
    /** 登録日時 */
    const CREATED_AT = 'create_date';
    /** 更新日時 */
    const UPDATED_AT = 'update_date';

    /** テーブル */
    protected $table = 'client_groups';
    /** PrimaryKey */
    protected $primaryKey = ['title_id', 'client_group'];
    /** PKのインクリメント */
    public $incrementing = false;

    /**
     * クライアントグループを全件取得します（クライアントグループの降順）
     *
     * @return mixed
     */
    public function getClientgroups() {
        return DB::table($this->table)
            ->select('title_id', 'client_group')
            ->orderBy('client_group', 'desc')
            ->get()->toArray();
    }

    /**
     * PKでレコードを取得します
     * @param $titleId
     * @param $clientGroup
     * @return mixed
     */
    public function findByPk($titleId, $clientGroup) {
        return DB::table($this->table)
            ->where('title_id', $titleId)
            ->where('client_group', $clientGroup)
            ->get()->toArray();
    }

    /**
     * PKを条件にレコードが存在するかチェックします
     * @param $titleId
     * @param $clientGroup
     * @return bool 存在する場合、true
     */
    public function existsByPk($titleId, $clientGroup) {
        $result = $this->findByPk($titleId, $clientGroup);
        return count($result) == 1;
    }

    /**
     * titleIdを条件にレコードを取得
     * @param $titleId
     * @return mixed
     */
    public function findByTitle($titleId) {

        $group = DB::table($this->table);
        // ゲームID：ALL以外は条件にする
        if ($titleId != 'ALL')
        {
            $group = $group->where('title_id',$titleId);
        }

        return $group
            ->orderBy('title_id', 'ASC')
            ->orderBy('client_group', 'DESC')
            ->get()->toArray();
    }

    /**
     * クライアントグループ検索
     * @param $gameId
     * @param $clientGroup
     * @return mixed
     */
    public function getFindData($gameId, $clientGroup) {

        $group = DB::table($this->table)
            ->select($this->table.'.client_group', $this->table.'.game_id', $this->table.'.limit_title_ver')
            ->addSelect(DB::raw("to_char(client_groups.update_date,'yyyy-mm-dd hh24:mi:ss') AS update_date"));

        if(!empty($titleId)) {
            $group = $group->where('game_id', $gameId);
        }
        if(!empty($clientGroup)) {
            $group = $group->where('client_group', $clientGroup);
        }
        return $group
            ->orderBy('game_id', 'ASC')
            ->orderBy('client_group', 'DESC')
            ->get();
    }

    /**
     * 登録
     *
     * @param $titleId
     * @param $clientGroup
     * @param $limitTitleVer
     */
    public function dataSave($titleId, $clientGroup, $limitTitleVer) {

        // データが存在しない場合のみ登録処理を実施
        if(!$this->existsByPk($titleId, $clientGroup)) {

            $this->title_id = $titleId;
            $this->client_group = $clientGroup;
            $this->limit_title_ver = $limitTitleVer;
            $this->create_user_id = 'system';
            $this->update_user_id = 'system';

            $this->save();
        } else {
            ClientGroups::where('title_id', $titleId)->where('client_group', $clientGroup)
                ->update(['limit_title_ver' => $limitTitleVer, 'update_user_id' => 'system']);
        }
    }

    /**
     * クライアントグループ情報データ削除
     * @param $titleId
     * @param $clientGroup
     */
    public function deleteData($titleId, $clientGroup) {
        DB::table($this->table)->where('title_id', $titleId)->where('client_group', $clientGroup)->delete();
    }
}
