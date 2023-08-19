<?php

namespace App\Models;

use Carbon\Carbon;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

/**
 * 認証ログ
 * Class AuthLogs
 * @package App\Models
 */
class AuthLogs extends Model
{
    //const CREATED_DATE = 'create_date';

    // テーブル指定
    protected $table = 'auth_logs';

    public $incrementing = false;

    /**
     * 認証ログ取得
     *
     * @param $serial
     * @param $startDateTime
     * @param $endDateTime
     * @param $sortItems
     * @param $stat
     * @param $usedRange
     * @param $authResult
     * @return mixed
     */
    public function getLogData($serial, $startDateTime, $endDateTime, $sortItems, $stat, $usedRange, $authResult) {

        $authLogData = DB::table($this->table);

        if(!$this->isEmpty($serial)) {
            $authLogData = $authLogData->where('auth_logs.keychip_id', $serial);
        }
        if(!$this->isEmpty($authResult)) {
            $authLogData = $authLogData->where('auth_logs.auth_result', $authResult);
        }

        if(!$this->isEmpty($sortItems)) {
            foreach ($sortItems as $key => $value) {
                $authLogData = $authLogData->orderBy($key, $value);
            }
        }else {
            //デフォルトはアクセス時刻の降順
            $authLogData = $authLogData->orderBy('auth_logs.create_date', 'desc');
        }

        if($usedRange) {
            $authLogData
                ->where(DB::raw('to_timestamp(to_char(auth_logs.create_date, \'YYYYMMDDHH24MI\'), \'YYYYMMDDHH24MI\')'), '>=', $startDateTime)
                ->where(DB::raw('to_timestamp(to_char(auth_logs.create_date, \'YYYYMMDDHH24MI\'), \'YYYYMMDDHH24MI\')'), '<=', $endDateTime);
        }

        if(!$this->isEmpty($stat)) {
            $authLogData = $authLogData->where('auth_logs.auth_result', $stat);
        }

        return $authLogData->paginate(10);
    }

    /**
     * 認証ログ取得
     *
     * @param $gameId
     * @param $serial
     * @param $startDateTime
     * @param $endDateTime
     * @param $sortItems
     * @param $stat
     * @param $countryCode
     * @param $usedRange
     * @param $authResult
     * @return mixed
     */
    public function getCsvLogData($serial, $startDateTime, $endDateTime, $sortItems, $stat, $usedRange, $authResult) {

        $authLogData = DB::table($this->table);

        if(!$this->isEmpty($serial)) {
            $authLogData = $authLogData->where('auth_logs.keychip_id', $serial);
        }
        if(!$this->isEmpty($authResult)) {
            $authLogData = $authLogData->where('auth_logs.auth_result', $authResult);
        }

        if(!$this->isEmpty($sortItems)) {
            foreach ($sortItems as $key => $value) {
                $authLogData = $authLogData->orderBy($key, $value);
            }
        }else {
            //デフォルトはアクセス時刻の降順
            $authLogData = $authLogData->orderBy('auth_logs.create_date', 'desc');
        }

        if($usedRange) {
            $authLogData
                ->where(DB::raw('to_timestamp(to_char(auth_logs.create_date, \'YYYYMMDDHH24MI\'), \'YYYYMMDDHH24MI\')'), '>=', $startDateTime)
                ->where(DB::raw('to_timestamp(to_char(auth_logs.create_date, \'YYYYMMDDHH24MI\'), \'YYYYMMDDHH24MI\')'), '<=', $endDateTime);
        }

        if(!$this->isEmpty($stat)) {
            $authLogData = $authLogData->where('auth_logs.auth_result', $stat);
        }

        return $authLogData
            ->get()->toArray();;
    }

    /**
     * keychip_idをKEYに認証ログを取得
     * @param $clientId
     * @return mixed
     */
    public function getLogDataByClient($clientId) {

        $authLogData = DB::table($this->table)
                    ->where('keychip_id', '=', $clientId)
                    ->select('*');

        return $authLogData->orderBy('create_date', 'DESC')
                            ->paginate(20);
    }

    /**
     * NULL、空文字判定(NULLの場合はTrueを返却)
     *
     * @param $data
     * @return bool
     */
    private function isEmpty($data) {
        if(is_null($data) || $data === "") {
            return true;
        } else {
            return false;
        }
    }
}
