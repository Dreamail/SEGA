<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\DB;

class Games extends Model
{
    /** 登録日時 */
    const CREATED_AT = 'create_date';
    /** 更新日時 */
    const UPDATED_AT = 'update_date';

    // テーブル指定
    protected $table = 'games';
    // PrimaryKey
    protected $primaryKey = 'game_id';

    public $incrementing = false;

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $gameId
     * @return mixed
     */
    public function getData($gameId) {

        $data = DB::table($this->table)->where('game_id', $gameId)->get();
        return $data;
    }

    /**
     * 検索画面で使用するデータ取得
     *
     * @param $gameId
     * @return mixed
     */
    public function getFindData($gameId) {

        $data = DB::table($this->table)->where('game_id', $gameId)
            ->select($this->table.'.game_id', $this->table.'.title')
            ->addSelect(DB::raw("to_char(games.create_date,'yyyy-mm-dd hh24:mi:ss') AS create_date"))
            ->addSelect(DB::raw("to_char(games.update_date,'yyyy-mm-dd hh24:mi:ss') AS update_date"))
            ->get();
        return $data;
    }

    /**
     * PKを条件にレコードが存在するかチェックします
     * @param $gameId
     * @return bool 存在する場合、true
     */
    public function existsByPk($gameId) {
        $result = $this->getData($gameId);
        return count($result) == 1;
    }

    /**
     * 無効以外のゲーム情報取得
     *
     * @return mixed
     */
    public function getGameData() {

        $data = DB::table($this->table)
            ->select('game_id', 'title')
            ->where('invalided', '0')->get()->toArray();
        return $data;
    }

    /**
     * ゲーム指定権限用ゲーム情報取得
     *
     * @param $userId
     * @return mixed
     */
    public function getGameOpenData($userId) {

        $data = DB::table($this->table)
            ->Join('game_competences', $this->table.'.game_id', '=', 'game_competences.game_id')
            ->select($this->table.'.game_id', $this->table.'.title')
            ->where('game_competences.user_id', $userId)
            ->where('invalided', '0')->get()->toArray();
        return $data;
    }

    /**
     * ゲーム情報を取得(属性情報含む)
     *
     * @param $gameId
     * @return mixed
     */
    public function gatGameAttributeData($gameId) {

        $data = DB::table($this->table)
            ->Join('game_attributes', $this->table.'.game_id', '=', 'game_attributes.game_id')
            ->select($this->table.'.game_id', 'game_attributes.game_ver', $this->table.'.title', 'game_attributes.uri1', 'game_attributes.uri2', 'game_attributes.auth')
            ->orderBy($this->table.'.game_id', 'ASC')
            ->orderBy('game_attributes.title_ver', 'DESC');

        if(Config::get('const.ALL_SEARCH') !== $gameId) {
            $data = $data->where($this->table.'.game_id', $gameId);
        }

        return $data->get()->toArray();
    }

    /**
     * ゲーム情報登録・更新処理
     * @param $gameId
     * @param $title
     */
    public function saveGameData($gameId, $title) {

        // データが存在しない場合のみ登録処理を実施
        if(!$this->existsByPk($gameId)) {

            $this->game_id = $gameId;
            $this->title = $title;
//            $this->fix_interval = Config::get('const.GAME_DATA')['FIX_INTERVAL'];
//            $this->fix_log_cnt = Config::get('const.GAME_DATA')['FIX_LOG_CNT'];
//            $this->play_limit = Config::get('const.GAME_DATA')['PLAY_LIMIT'];
//            $this->near_full = Config::get('const.GAME_DATA')['NEAR_FULL'];
//            $this->cipher_key = Config::get('const.GAME_DATA')['CIPHER_KEY'];
            $this->create_user_id = 'system';
            $this->update_user_id = 'system';

            $this->save();
        } else {
            Games::where('game_id', $gameId)
                ->update(['title' => $title, 'update_user_id' => 'system']);
        }
    }

    /**
     * ゲーム情報の削除処理
     *
     * @param $gameId
     */
    public function deleteGameData($gameId) {
        Games::where('game_id', $gameId)
            ->delete();
    }
}
