@extends('admin.common.onlylayout')

@section('content')
    <h2>基板情報&nbsp;-&nbsp;CSVフォーマット詳細</h2><br>

    <div class="row-fluid">
        <div class="span12">
            <h4>ダウンロードファイルについて</h4>
            <ul>
                <li>1行が1キーチップ(ゲーム基板)を意味するcsvファイルです。</li>
                <li>ダウンロードファイルの文字コード「UTF-8」でダウンロードされます。</li>
            </ul>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>各行について</h4>
            <ul>
                <li>1行目以降 .. 1行1件の基板情報です。</li>
            </ul>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>各列の意味について</h4>
            <ul>
                <li>データ内にカンマが含まれる場合、データの両端がダブルクォートで括られている必要があります。</li>
                <li>データ内にダブルクォートが含まれる場合、そのダブルクォートはダブルクォート二個(" -> "")に変換されます。</li>
            </ul>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>フォーマット</h4>
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th style="width: 60px">位置</th>
                        <th>意味</th>
                        <th>補足説明</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="numeric">1</td>
                        <td>店舗名</td>
                        <td>「店舗情報」で登録した店舗名。</td>
                    </tr>
                    <tr>
                        <td class="numeric">2</td>
                        <td>ALL.Net ID</td>
                        <td>「店舗情報」で登録したALL.NetID。</td>
                    </tr>
                    <tr>
                        <td class="numeric">3</td>
                        <td>シリアル番号</td>
                        <td>対象のキーチップに記載されているシリアル番号。</td>
                    </tr>
                    <tr>
                        <td class="numeric">4</td>
                        <td>ゲームID</td>
                        <td>対象のキーチップに記載されているゲームID。(例：頭文字Dzeroの場合「SDDF」)</td>
                    </tr>
                    <tr>
                        <td class="numeric">5</td>
                        <td>通信セッティング</td>
                        <td>運用状態…「1」<br>稼働一時停止…「2」</td>
                    </tr>
                    <tr>
                        <td class="numeric">6</td>
                        <td>店舗IP</td>
                        <td>"xx.xx.xx.xx"の形式</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>CSVの例</h4>
            <pre>動作テスト用店舗1,1101,A69X20A0001,SDDF,1,10.1.100.254
動作テスト用店舗2,1101,A69X20A0002,SDDF,1,10.2.100.254
動作テスト用店舗3,1102,A69X20A0003,SDDF,2,10.3.100.254
,1102,A69X20A0004,SDDF,1,10.4.100.254</pre>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12" style="text-align: right">
            <button type="button" class="btn" onclick="window.close();"><i class="icon-remove"></i>&nbsp;閉じる</button>
        </div>
    </div>
@stop