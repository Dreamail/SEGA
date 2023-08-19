<?php

namespace App\Http\Controllers;

use App\Http\Requests\MachineCsvDownloadRequest;
use App\Http\Requests\TitleCsvDownloadRequest;

use Illuminate\Http\Request;
use function Psy\debug;
use App\Models\Machines;
use DB;
use Illuminate\Support\Facades\Log;

class CsvDownloadController extends Controller
{
    //基板情報 CSVダウンロード画面を表示
    public function view_machine()
    {
        return view('admin.download.machine')
            ->with('gameData', $this->getGameData());
    }

    /**
     * 基板情報ダウンロード処理
     *
     * @param MachineCsvDownloadRequest $request
     * @return mixed
     */
    public function export_machine(MachineCsvDownloadRequest $request)
    {

        $gameId = $request->input('gameId');
        $type = $request->input('type');

        $machines = new Machines();

        $result = null;
        switch ($type) {
            case 'onlyAuth':
                $result = $machines->writeAuthorized($gameId);
                break;
            case 'onlyNotAuth':
                $result = $machines->writeUnauhtorized($gameId);
                break;
            case 'byTime':
                $day = \Request::input('day');
                $result = $machines->writeNotActive($gameId, $day);
        }

        //file名
        $filename = $gameId.".csv";

        //header
        $headers = array(
            'Content-Type' => 'text/csv',
            'Content-Disposition' => 'attachment; filename="' . $filename . '"'
        );

        //response
        return \Response::make($this->machineCsvCreate($result), 200, $headers);
    }

    /**
     * 基板情報CSV作成
     * @param $result
     * @return null|string|string[]
     */
    function machineCsvCreate($result) {

        //仮ファイルOpen
        $stream = fopen('php://temp','w');
        foreach ($result as $row) {
            $array = array(
                $row->place_name,
                $row->allnet_id,
                $row->serial,
                $row->game_id,
                $row->setting,
                $row->place_ip,
            );
            fputcsv($stream, $array);
        }
        //ポインタの先頭へ
        rewind($stream);

        //いろいろ変換
        $csv = str_replace(PHP_EOL, "\r\n", stream_get_contents($stream));

        return $csv;
    }
}
