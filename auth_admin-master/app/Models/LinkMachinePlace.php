<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Carbon;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;


/**
 * 基板情報と店舗情報の紐づけ
 *
 * Class LinkMachinePlaceController
 * @package App\Models
 */
class LinkMachinePlace extends Model
{
    const CREATED_AT = 'create_date';
    const UPDATED_AT = 'update_date';

    // テーブル指定
    protected $table = 'link_machine_place';
    // PrimaryKey
    protected $primaryKey = 'serial';

    public $incrementing = false;

    /**
     * 基板シリアルをKEYにしたデータ取得
     *
     * @param $serial
     * @return mixed
     */
    public function getData($serial) {

        $data = DB::table($this->table)->where('serial', $serial)->get();
        return $data;
    }

    /**
     * 基板シリアルを条件にレコードが存在するかチェックします
     * @param $serial
     * @return bool 存在する場合、true
     */
    public function existsByPk($serial) {
        $result = $this->getData($serial);
        return count($result) >= 1;
    }

    /**
     * ゲームIDに対応する基板シリアルの店舗との紐づき状態を取得
     * @param $gameId
     * @return
     */
    public function getLinkData($gameId) {
        $linkData = DB::table('link_machine_place')
            ->join('machines', 'link_machine_place.serial', '=', 'machines.serial')
            ->select('link_machine_place.serial', 'link_machine_place.place_ip')
            ->where('machines.game_id','=',$gameId);

        $linkData = $linkData
            ->orderBy('link_machine_place.no')
            ->get()->toArray();

        return $linkData;
    }

    /**
     * 基板、ゲームIDをKeyにした基板・店舗の紐づき状態を検索
     * @param null $serial
     * @param null $gemeId
     * @return mixed
     */
    public function getFindData($serial = null, $gemeId = null) {

        $linkData = DB::table('link_machine_place')
            ->join('machines', 'link_machine_place.serial', '=', 'machines.serial')
            ->select('link_machine_place.no','link_machine_place.serial', 'link_machine_place.place_ip')
            ->addSelect(DB::raw("to_char(link_machine_place.update_date,'yyyy-mm-dd hh24:mi:ss') AS update_date"));

        if(!empty($serial)) {
            $linkData = $linkData->where('machines.serial', $serial);
        }
        if(!empty($gemeId)) {
            $linkData = $linkData->where('machines.game_id', $gemeId);
        }

        $linkData = $linkData
            ->orderBy('link_machine_place.no')
            ->get();

        return $linkData;
    }

    /**
     * 登録・更新処理
     *
     * @param $array
     */
    public function dataSave($array) {

        $this->dataCreateData(
            trim($array[2]),   // serial
            trim($array[5])    // place_ip
        );
    }

    /**
     * 登録実処理
     * @param $serial
     * @param $placeIp
     */
    public function dataCreateData($serial, $placeIp) {

        //serialが存在する場合は更新、存在しない場合は新規登録を行う
        if (!$this->existsByPk($serial)>0) {
            DB::table($this->table)
                ->insert(['serial'=>$serial,
                    'place_ip'=>$placeIp,
                    'update_user_id'=>'system',
                    'create_user_id'=>'system',
            ]);
        }else{
            DB::table($this->table)
                ->where('serial', $serial)
                ->update(['serial' => $serial,
                    'place_ip' => $placeIp,
                    'update_user_id' => 'system',
                    'create_user_id' => 'system',
                    'update_date' => Carbon::now()]);
        }
    }

    /**
     * 紐づけデータ削除
     *
     * @param $serial
     */
    public function deleteData($serial) {
        DB::table($this->table)->where('serial', $serial)->delete();
    }
}
