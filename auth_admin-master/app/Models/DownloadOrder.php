<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

/**
 * 配信指示書
 *
 * Class DownloadOrder
 * @package App\Models
 */
class DownloadOrder extends Model
{
    const CREATED_AT = 'create_date';
    const UPDATED_AT = 'update_date';

    // テーブル指定
    protected $table = 'download_orders';
    // PrimaryKey
    protected $primaryKey = ['game_id','game_ver','type'];

    public $incrementing = false;

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $gameId
     * @param $gameVer
     * @param $type
     * @return mixed
     */
    public function getData($gameId, $gameVer, $type) {

        $data = DB::table($this->table)->where('game_id', $gameId)
            ->where('game_ver', $gameVer)
            ->where('type', $type)
            ->get()
            ->toArray();
        return $data;
    }

    /**
     * ゲームIDをKEYにデータを取得
     *
     * @param $gameId
     * @return mixed
     */
    public function getOrderData($gameId) {

        $data = DB::table($this->table)
            ->select('download_orders.game_ver','download_orders.game_id', 'download_orders.uri', 'download_orders.type', 'download_orders.create_date', 'download_orders.update_date')
            ->where('game_id', $gameId)->get();
        return $data;
    }

    /**
     * 登録・更新処理
     *
     * @param $gameId
     * @param $gameVer
     * @param $type
     * @param $uri
     */
    public function dataSave($gameId, $gameVer, $type, $uri) {

        $data = $this->getData($gameId, $gameVer, $type);
        if(count($data) > 0) {
            DownloadOrder::where('game_id', $gameId)
                ->where('game_ver', $gameVer)
                ->where('type', $type)
                ->update(['uri' => $uri, 'update_user_id' => 'system']);
        } else {
            $this->game_id = $gameId;
            $this->game_ver = $gameVer;
            $this->type = $type;
            $this->uri = $uri;
            $this->create_user_id = 'system';
            $this->update_user_id = 'system';

            $this->save();
        }
    }

    /**
     * 削除処理
     *
     * @param $gameId
     * @param $gameVer
     * @param $type
     */
    public function deleteData($gameId, $gameVer, $type) {

        DB::table($this->table)->where('game_id', $gameId)
            ->where('game_ver', $gameVer)
            ->where('type', $type)->delete();
    }
}
