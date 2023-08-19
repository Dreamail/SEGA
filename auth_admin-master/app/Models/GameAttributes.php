<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;

class GameAttributes extends Model
{
    /** 登録日時 */
    const CREATED_AT = 'create_date';
    /** 更新日時 */
    const UPDATED_AT = 'update_date';

    // テーブル指定
    protected $table = 'game_attributes';
    // PrimaryKey
    protected $primaryKey = ['game_id','game_ver','country_code'];

    public $incrementing = false;

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $titleId
     * @return mixed
     */
    public function getData($titleId, $titleVer, $countryCode) {

        $data = DB::table($this->table)
            ->where('game_id', $titleId)
            ->where('game_ver', $titleVer)
            ->where('country_code', $countryCode)
            ->get();
        return $data;
    }

    /**
     * タイトルIDをKEYにデータ取得
     *
     * @param $titleId
     * @return mixed
     */
    public function getDataByTitleId($titleId) {

        $data = DB::table($this->table)
            ->where('game_id', $titleId)
            ->select($this->table.'.game_id', $this->table.'.game_ver', $this->table.'.country_code', $this->table.'.title', $this->table.'.uri', $this->table.'.host', $this->table.'.auth')
            ->addSelect(DB::raw("to_char(game_attributes.create_date,'yyyy-mm-dd hh24:mi:ss') AS create_date"))
            ->addSelect(DB::raw("to_char(game_attributes.update_date,'yyyy-mm-dd hh24:mi:ss') AS update_date"))
            ->orderBy('game_ver', 'asc')
            ->orderBy('country_code', 'asc')
            ->get();
        return $data;
    }

    /**
     * タイトルIDと国コードをKEYにデータ件数を取得
     *
     * @param $titleId
     * @param $countryCode
     * @return mixed
     */
    public function getTitleDataCount($titleId, $countryCode) {

        $data = DB::table($this->table)
            ->where('game_id', $titleId)
            ->where('country_code', $countryCode)
            ->count();
        return $data;
    }

    /**
     * PKを条件にレコードが存在するかチェックします
     *
     * @param $titleId
     * @param $titleVer
     * @param $countryCode
     * @return bool
     */
    public function existsByPk($titleId, $titleVer, $countryCode) {
        $result = $this->getData($titleId, $titleVer, $countryCode);
        return count($result) == 1;
    }

    /**
     * ゲーム情報登録・更新処理
     * @param $titleId
     * @param $titleVer
     * @param $title
     * @param $uri
     * @param $host
     * @param $auth
     */
    public function saveAttributesData($titleId, $titleVer, $title, $uri, $host, $auth, $countryCode = 'CHN') {

        // データが存在しない場合のみ登録処理を実施
        if(!$this->existsByPk($titleId, $titleVer, $countryCode)) {

            $this->game_id = $titleId;
            $this->title = $title;
            $this->game_ver = $titleVer;
            $this->uri = $uri;
            $this->host = $host;
            $this->auth = $auth;
            $this->country_code = $countryCode;
            $this->create_user_id = 'system';
            $this->update_user_id = 'system';

            $this->save();
        } else {
            GameAttributes::where('game_id', $titleId)
                ->where('game_ver', $titleVer)
                ->where('country_code', $countryCode)
                ->update(['title' => $title, 'uri' => $uri, 'host' => $host, 'auth' => $auth, 'update_user_id' => 'system']);
        }
    }

    /**
     * ゲーム情報の削除処理
     *
     * @param $titleId
     * @param $titleVer
     */
    public function deleteAttributesData($titleId, $titleVer, $countryCode = 'CHN') {

        GameAttributes::where('game_id', $titleId)
            ->where('game_ver', $titleVer)
            ->where('country_code', $countryCode)
            ->delete();
    }
}
