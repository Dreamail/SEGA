<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Config;

/**
 * 店舗情報テーブルModel
 *
 * Class Places
 * @package App\Models
 */
class Places extends Model
{

    const CREATED_AT = 'create_date';
    const UPDATED_AT = 'update_date';

    // テーブル指定
    protected $table = 'places';
    // PrimaryKey
    protected $primaryKey = 'allnet_id';
    // 店舗ID
    protected $priceid = 'place_id';

    public $incrementing = false;

    /**
     * PrimaryKeyをKEYにしたデータ取得
     *
     * @param $placeId
     * @return mixed
     */
    public function getData($allNetId) {

        $data = DB::table($this->table)->where('allnet_id', $allNetId)->get();

        return $data;
    }

    /**
     * PKを条件にレコードが存在するかチェックします
     * @param $allNetId
     * @return bool 存在する場合、true
     */
    public function existsByPk($allNetId) {
        $result = $this->getData($allNetId);
        return count($result) == 1;
    }

//    /**
//     * 実店舗のみを取得
//     * @return mixed
//     */
//    public function getRealPlace() {
//
//        $data = DB::table($this->table)->where('location_type', '1')->orderBy('places.allnet_id')->get()->toArray();
//
//        return $data;
//    }

    /**
     * 全店舗取得
     * @return mixed
     */
    public function getAllPlace() {

        $data = DB::table($this->table)->orderBy('places.allnet_id')->get()->toArray();
        return $data;
    }


    /**
     * 店舗IDをKEYにしたデータ取得
     *
     * @param $priceId
     * @return mixed
     */
    public function getDataPlaceId($priceId) {

        $data = DB::table($this->table)->where('place_id', $priceId)->get();

        return $data;
    }

    /**
     * ALL.NetIDをKEYにしたデータ取得
     *
     * @param $allNetId
     * @return mixed
     */
    public function getDataAllNetId($allNetId) {

        $data = DB::table($this->table)->where('allnet_id', $allNetId)->get();

        return $data;
    }

    /**
     * 店舗名をKEYにしたデータ取得
     *
     * @param $name
     * @return mixed
     */
    public function getDataName($name) {

        $data = DB::table($this->table)->where('name', $name)->get();

        return $data;
    }

    /**
     * 指定された日時以降にアクセスがない基板が存在する店舗情報を取得する。
     *
     * @param day
     * @return mixed
     */
    function findPlacesNotActive($day) {

        $day = date('Y/m/d H:i:s' , strtotime("-$day day"));
        $placesData = DB::table($this->table)
            ->leftJoin('region0', 'places.region0_id', '=', 'region0.region_id')
            ->whereNotIn('places.place_id', function ($query) use ($day) {
                $query->select(DB::raw('distinct machines.place_id'))
                    ->from('machine_statuses')
                    ->join('machines', 'machines.client_id', '=', 'machine_statuses.client_id')
                    ->where('machine_statuses.last_access', '>=', "'$day'");
            })
            ->select('places.*','region0.name AS region0')
            ->orderBy('places.place_id')
            ->get()
            ->toArray();
        return $placesData;
    }

    /**
     * 指定された日時以内にアクセスがあった基板がある店舗情報を取得する。
     *
     * @param $day
     * @return mixed
     */
    function findPlacesActive($day) {

        $day = date('Y/m/d H:i:s' , strtotime("-$day day"));
        $placesData = DB::table('places')
            ->leftJoin('region0', 'places.region0_id', '=', 'region0.region_id')
            ->whereIn('places.place_id', function ($query) use ($day) {
                $query->select(DB::raw('distinct machines.place_id'))
                    ->from('machine_statuses')
                    ->join('machines', 'machines.client_id', '=', 'machine_statuses.client_id')
                    ->where('machine_statuses.last_access', '>=', "'$day'");
            })
            ->select('places.*','region0.name AS region0')
            ->orderBy('places.place_id')
            ->get()
            ->toArray();

        return $placesData;
    }

    /**
     * 検索タイプにより取得処理を変えて店舗情報を取得
     * @param $searchType
     * @param $keyword
     * @return |null
     */
    function searchTypeFindPlaces($searchType, $keyword) {

        $placesData = DB::table($this->table)
            ->join('region0', 'region0.region_id', '=', $this->table.'.region0_id')
            ->where('region0.country_code', Config::get('const.COUNTY_CODE'))
            ->select('places.*', 'region0.name AS region0')
            ->orderByRaw('to_number(places.place_id, \'99999\') asc');

        switch ($searchType) {
            case Config::get('const.FIND_TYPE')['0']:
                $placesData = $placesData->where($this->table.'.name', 'LIKE', '%'.$keyword.'%')->get();
                break;
            case Config::get('const.FIND_TYPE')['1']:
                $placesData = $placesData->where($this->table.'.place_id', $keyword)->get();
                break;
            case Config::get('const.FIND_TYPE')['2']:
                $placesData = $placesData->where($this->table.'.tel', $keyword)->get();
                break;
        }
        return $placesData;
    }

    /**
     * すべての店舗情報を取得する。
     *
     * @return mixed
     */
    function findPlaces() {

        $placesData = DB::table('places')
            ->leftJoin('region0', 'places.region0_id', '=', 'region0.region_id')
            ->select('places.*', 'region0.name AS region0')
            ->orderBy('places.place_id')
            ->distinct()
            ->get()
            ->toArray();

        return $placesData;
    }

    /**
     * 登録・更新処理
     *
     * @param $array
     */
    public function dataSave($array) {

        $region0 = new Region0();
        $regionCode = $region0->getRegionCode(Config::get('const.COUNTY_CODE'),trim($array[7]));
        $placeId = intval(trim($array[1]));
        $data = $this->getDataPlaceId($placeId);
        if(count($data) > 0) {
            $this->dataUpdate($placeId,
                trim($array[0]),
                trim($array[2]),
                trim($array[3]),
                trim($array[4]),
                trim($array[5]),
                trim($array[6]),
                trim($array[8]),
                $regionCode);
        } else {
            $this->name = trim($array[0]);
            $this->place_id = $placeId;
            $this->location_type = trim($array[2]);
            $this->tel = trim($array[3]);
            $this->address = trim($array[4]);
            $this->station = trim($array[5]);
            $this->special_info = trim($array[6]);
            $this->region0_id = $regionCode;
            $this->nickname = trim($array[8]);
            $this->country_code = Config::get('const.COUNTY_CODE');
            $this->create_user_id = 'system';
            $this->update_user_id = 'system';

            $this->save();
        }
    }

    /**
     * 登録・更新処理
     *
     * @param $array
     */
    public function dataUpdate($placeId, $name, $locationType, $tel, $address, $station, $specialInfo, $nickname, $regionId) {

        Places::where('place_id', $placeId)
            ->update(['name' => $name,
                'location_type' => $locationType,
                'tel' => $tel,
                'address' => $address,
                'station' => $station,
                'special_info' => $specialInfo,
                'region0_id' => $regionId,
                'nickname' => $nickname,
                'update_user_id' => 'system']);
    }

    public function deletePlaceData($placeId, $countryCode) {

        Places::where('place_id', $placeId)
            ->where('country_code', $countryCode)
            ->delete();
    }
}
