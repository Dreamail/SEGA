<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class CsvUploadController extends Controller
{
    // 基盤情報：CSVアップロード画面を表示
    public function view_machine()
    {
        return view('admin.upload.machine')
            ->with('gameData', $this->getGameData());
    }
}
