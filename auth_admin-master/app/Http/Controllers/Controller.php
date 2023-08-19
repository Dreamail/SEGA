<?php

namespace App\Http\Controllers;

use App\Models\Places;
use Session;
use App\Models\Games;
use Illuminate\Foundation\Bus\DispatchesJobs;
use Illuminate\Routing\Controller as BaseController;
use Illuminate\Foundation\Validation\ValidatesRequests;
use Illuminate\Foundation\Auth\Access\AuthorizesRequests;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Config;

class Controller extends BaseController
{
    use AuthorizesRequests, DispatchesJobs, ValidatesRequests;

    /**
     * NULL、空文字判定(NULLの場合はTrueを返却)
     *
     * @param $data
     * @return bool
     */
    public function isEmpty($data) {
        if(is_null($data) || $data === "") {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ゲーム情報取得
     *
     * @return mixed
     */
    public function getGameData() {

        $games = new Games();

        $gameData = null;
        if(Session::get('gameOpenFlg') == 0) {
            $gameData = $games->getGameOpenData(Session::get('login'));
        } else {
            $gameData = $games->getGameData();
        }

        return $gameData;
    }

    /**
     * 店舗情報取得
     *
     * @return mixed
     */
    public function getPlaceData() {

        $places = new Places();
        
        $placeData = $places->getAllPlace();

        return $placeData;
    }

    /**
     * コンストラクタ
     *
     * Controller constructor.
     */
    public function __construct()
    {
        try {
            DB::connection()->getPdo();
        } catch (\Exception $e) {
            die("Could not connect to the database.  Please check your configuration.");
        }
    }
}