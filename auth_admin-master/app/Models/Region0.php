<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;

class Region0 extends Model
{
    // テーブル指定
    protected $table = 'region0';
    // PrimaryKey
    protected $primaryKey = ['country_code', 'region_id'];

    public $incrementing = false;

    /**
     * 国コード、名称をKEYにしたデータ取得
     *
     * @param $countryCode
     * @param $name
     * @return mixed
     */
    public function getData($countryCode,$name) {

        $data = DB::table($this->table)
            ->where('country_code', $countryCode)
            ->where('name', $name)
            ->get();
        return $data;
    }

    /**
     * 国コード、地域IDをKEYにしたデータ取得
     *
     * @param $countryCode
     * @param $regionId
     * @return mixed
     */
    public function getDataByRegionId($countryCode, $regionId) {

        $data = DB::table($this->table)
            ->where('country_code', $countryCode)
            ->where('region_id', $regionId)
            ->get();
        return $data;
    }

    /**
     * 国コードをKEYにしたデータ取得
     *
     * @param $countryCode
     * @return mixed
     */
    public function getDataByCountryCode($countryCode) {

        $data = DB::table($this->table)
            ->where('country_code', $countryCode)
            ->get();
        return $data;
    }

    /**
     * 国コード、名称から地域名0を取得
     *
     * @param $countryCode
     * @param $name
     * @return string
     */
    public function getRegionCode($countryCode,$name) {

        $data = DB::table($this->table)
            ->where('country_code', $countryCode)
            ->where('name', $name)
            ->get()
            ->toArray();

        foreach ($data as $row)
        {
            $regionCode = $row->region_id;
        }
        return $regionCode;
    }
}
