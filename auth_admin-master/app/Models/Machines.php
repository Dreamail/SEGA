<?php

namespace App\Models;

use Illuminate\Support\Carbon;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\Log;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;
use function Psy\debug;

/**
 * 基盤情報
 *
 * Class Machines
 * @package App\Models
 */
class Machines extends Model
{
    const CREATED_AT = 'create_date';
    const UPDATED_AT = 'update_date';

    // テーブル指定
    protected $table = 'machines';
    // PrimaryKey
    protected $primaryKey = 'serial';

    public $incrementing = false;

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $serial
     * @return mixed
     */
    public function getData($serial) {

        $data = DB::table($this->table)->where('serial', $serial)->get();
        return $data;
    }

    /**
     * PKを条件にレコードが存在するかチェックします
     * @param $serial
     * @return bool 存在する場合、true
     */
    public function existsByPk($serial) {
        $result = $this->getData($serial);
        return count($result) >= 1;
    }

    /**
     * PrimaryKeyをKEYにしたデータ取得(バージョン取得用)
     *
     * @param $serial
     * @return mixed
     */
    public function getDataVer($serial) {

        $data = DB::table($this->table)
            ->where('serial', $serial)
            ->orderBy('version','desc')
            ->limit(1)
            ->get()
            ->toArray();
        return $data;
    }

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $serial
     * @return mixed
     */
    public function getDataPlace($serial) {

        $data = DB::table($this->table)->where('serial', $serial)
            ->join('places', 'machines.allnet_id', '=', 'places.allnet_id')
            ->select('machines.*', 'places.name')
//            ->orderBy('machines.version', 'DESC')
//            ->limit(1)
            ->get()
            ->toArray();

        return $data;
    }

    /**
     * 基板情報検索
     * @param null $serial
     * @param null $allNetId
     * @param null $gemeId
     * @param null $placeName
     * @return mixed
     */
    public function getFindData($serial = null, $allNetId = null, $gemeId = null, $placeName = null) {

        $machines = DB::table('machines')
            ->join('places', 'machines.allnet_id', '=', 'places.allnet_id')
            ->join('games', 'machines.game_id', '=', 'games.game_id')
            ->join('link_machine_place', 'machines.serial','=','link_machine_place.serial')
            ->select('places.name AS place_name', 'places.allnet_id', 'machines.serial', 'machines.game_id', 'machines.setting', 'games.title AS title_name', 'link_machine_place.place_ip')
            ->addSelect(DB::raw("to_char(machines.update_date,'yyyy-mm-dd hh24:mi:ss') AS update_date"));

        if(!empty($serial)) {
            $machines = $machines->where('machines.serial', $serial);
        }
        if(!empty($allNetId)) {
            $machines = $machines->where('machines.allnet_id', $allNetId);
        }
        if(!empty($gemeId)) {
            $machines = $machines->where('machines.game_id', $gemeId);
        }
        if(!empty($placeName)) {
            $machines = $machines->where('places.name', 'like', '%'.$placeName.'%');
        }

        $machines = $machines
            ->orderBy('machines.serial')
            ->orderBy('places.allnet_id')
            ->orderBy('machines.game_id')
            ->get();

        return $machines;
    }

    /**
     * 認証されたボードのみの検索
     *
     * @param $gameId
     * @return mixed
     */
    public function writeAuthorized($gameId) {

        $machines = DB::table('machines')
            ->join('places', 'machines.allnet_id', '=', 'places.allnet_id')
            ->join('link_machine_place', 'machines.serial', '=', 'link_machine_place.serial')
            ->select('places.name AS place_name', 'places.allnet_id', 'machines.serial', 'machines.game_id','machines.reserved_game_id', 'machines.setting','link_machine_place.place_ip')
            ->whereExists(function ($query) {
                $query->from('machine_statuses')
                      ->whereRaw('machines.serial = machine_statuses.serial')
                      ->whereNotNull('machine_statuses.last_auth');
            });
        // ゲームID：ALL以外は条件にする

        if ($gameId != 'ALL')
        {
            $machines = $machines->whereIn('machines.game_id',array($gameId));
        }

        $machines = $machines
            ->orderBy('places.allnet_id')
            ->orderBy('machines.game_id')
            ->distinct()->get()->toArray();

        return $machines;
    }

    /**
     * 認証されていないボードのみの検索
     *
     * @param $gameId
     * @return mixed
     */
    public function writeUnauhtorized($gameId) {
        $machines = DB::table('machines')
            ->join('places', 'machines.allnet_id', '=', 'places.allnet_id')
            ->join('link_machine_place', 'machines.serial', '=', 'link_machine_place.serial')
            ->select('places.name AS place_name', 'places.allnet_id', 'machines.serial', 'machines.game_id', 'machines.setting','link_machine_place.place_ip')
            ->Where(function ($query) {
                $query->Where(function ($query) {
                    $query->whereNotExists(function ($query) {
                        $query->from('machine_statuses')
                            ->whereRaw('machines.serial = machine_statuses.serial');
                    });
                })
                ->orWhere(function ($query) {
                    $query->whereExists(function ($query) {
                            $query->from('machine_statuses')
                                ->whereRaw('machines.serial = machine_statuses.serial')
                                ->whereNull('machine_statuses.last_auth');
                        });
                });
            });
        // ゲームID：ALL以外は条件にする
        if ($gameId != 'ALL')
        {
            $machines = $machines->whereIn('machines.game_id',array($gameId));
        }
        $machines = $machines
            ->orderBy('places.allnet_id')
            ->orderBy('machines.serial')
            ->distinct()->get()->toArray();

        return $machines;
    }

    /**
     * 指定日数以上認証に成功していないボードのみの検索
     *
     * @param $gameId
     * @param $day
     * @return mixed
     */
    public function writeNotActive($gameId, $day) {
        $limit = date('Y/m/d H:i:s' , strtotime("-$day day"));
        $machines = DB::table('machines')
            ->join('places', 'machines.allnet_id', '=', 'places.allnet_id')
            ->join('link_machine_place', 'machines.serial', '=', 'link_machine_place.serial')
            ->select('places.name AS place_name', 'places.allnet_id', 'machines.serial', 'machines.game_id', 'machines.setting','link_machine_place.place_ip')
            ->Where(function ($query) use ($limit) {
                $query->Where(function ($query) {
                        $query->whereNotExists(function ($query) {
                            $query->from('machine_statuses')
                                ->whereRaw('machines.serial = machine_statuses.serial');
                        });
                    })
                    ->orWhere(function ($query) use ($limit) {
                        $query->whereExists(function ($query) use ($limit) {
                            $query->from('machine_statuses')
                                ->whereRaw('machines.serial = machine_statuses.serial')
                                ->where('machine_statuses.last_auth', '<=', "'$limit'");
                        });
                    });
            });
        // ゲームID：ALL以外は条件にする
        if ($gameId != 'ALL')
        {
            $machines = $machines->whereIn('machines.game_id',array($gameId));
        }
        $machines = $machines
            ->orderBy('places.allnet_id')
            ->orderBy('machines.serial')
            ->distinct()->get()->toArray();

        return $machines;
    }

    /**
     * 登録・更新処理
     *
     * @param $array
     */
    public function dataSave($array,$placeId,$reserve) {

        $this->dataCreateData(
            trim($array[2]),            // serial
            intval(trim($array[1])),    // allnet_id
            trim($array[3]),            // game_id
            trim($array[4]),            // setting
            $placeId,
            $reserve
        );
    }

    /**
     * 登録実処理
     * @param $serial
     * @param $placeId
     * @param $gameId
     * @param $setting
     */
    public function dataCreateData($serial, $allNetId, $gameId, $setting, $placeId, $reserve) {

        $this->serial = $serial;
        $this->allnet_id = intval($allNetId);
        $this->game_id = $gameId;
        $this->reserved_game_id = $gameId;
        $this->group_index = Config::get('const.MACHINE_GROUP_INDEX');
        $this->setting = $setting;
        $this->place_id = $placeId;
        $this->update_user_id = 'system';
        $this->create_user_id = 'system';

        //serialが存在する場合は更新、存在しない場合は新規登録を行う
        if (!$this->existsByPk($serial)>0) {
            $this->save();
        }else{
            if($reserve) {
                DB::table($this->table)
                    ->where('serial', $serial)
                    ->update(['serial' => $serial,
                        'allnet_id' => $allNetId,
                        'reserved_game_id' => $gameId,
                        'group_index' => Config::get('const.MACHINE_GROUP_INDEX'),
                        'setting' => $setting,
                        'place_id' => $placeId,
                        'update_user_id' => 'system',
                        'create_user_id' => 'system',
                        'update_date' => Carbon::now()]);
            }else{
                DB::table($this->table)
                    ->where('serial', $serial)
                    ->update(['serial' => $serial,
                        'allnet_id' => $allNetId,
                        'game_id' => $gameId,
                        'group_index' => Config::get('const.MACHINE_GROUP_INDEX'),
                        'setting' => $setting,
                        'place_id' => $placeId,
                        'update_user_id' => 'system',
                        'create_user_id' => 'system',
                        'update_date' => Carbon::now()]);
            }
        }
    }

    /**
     * 基板情報データ削除
     *
     * @param $serial
     */
    public function deleteData($serial) {
        DB::table($this->table)->where('serial', $serial)->delete();
    }
}
