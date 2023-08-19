@extends('admin.common.onlylayout')

@section('content')
    <h2>基板情報&nbsp;-&nbsp;CSVフォーマット詳細</h2><br>

    <div class="row-fluid">
        <div class="span12">
            <h4>アップロードファイルについて</h4>
            <ul>
                <li>1行が1キーチップ(ゲーム基板)を意味するcsvフォーマットのデータとなる。</li>
                <li>アップロードするファイルの文字コードは「UTF-8」とする。(※店舗名を入れた場合)</li>
            </ul>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>各行について</h4>
            <ul>
                <li>1行目以降..1行1件の基板情報です。</li>
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
                        <td>「店舗情報」で登録した店舗名を入れる。未登録の店舗名は設定不可。<br>また、次のALL.NetIDと食い違う店舗名の場合エラーとなる。省略可能。</td>
                    </tr>
                    <tr>
                        <td class="numeric">2</td>
                        <td>ALL.NetID</td>
                        <td>「店舗情報」で登録したALL.NetIDを入れる。<br>未登録のALL.NetIDは設定不可。</td>
                    </tr>
                    <tr>
                        <td class="numeric">3</td>
                        <td>シリアル番号</td>
                        <td>対象のキーチップに記載されているシリアル番号。<br>「-」は省略すること(例：「A69X-20A1569」 → 「A69X20A1569」)</td>
                    </tr>
                    <tr>
                        <td class="numeric">4</td>
                        <td>ゲームID</td>
                        <td>対象のキーチップに記載されているゲームID。(例：頭文字Dzeroの場合「SDDF」)</td>
                    </tr>
                    <tr>
                        <td class="numeric">5</td>
                        <td>通信セッティング</td>
                        <td>運用状態。「1」を入れる。<br>何らかの理由で稼働を一時的に停止したい場合「2」にする。「2」の場合、通信エラーとなりゲーム機が稼働しない。</td>
                    </tr>
                    <tr>
                        <td class="numeric">6</td>
                        <td>店舗IP</td>
                        <td>"xx.xx.xx.xx"の形式</td>
                    </tr>
                    <tr>
                        <td class="numeric">7</td>
                        <td>削除フラグ</td>
                        <td>通常、この行は記載しない。<br>「1」とした場合、このキーチップのサーバ上の情報が削除される。運用終了時に使用する。</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>注意</h4>
            <ul>
                <li>「7.削除フラグ」を使用すると、それまでの課金情報まで無くなるため、(料金未払いや店舗移動等で)一時的に動作を止めたい場合は「6.通信セッティング」を使用する事。</li>
                <li>「7.削除フラグ」はキーチップを回収し、最終締め処理完了後に設定する事。</li>
            </ul>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>CSVの例</h4>
            <pre>動作テスト用店舗1,1101,A69X20A0001,SDDF,1,10.1.100.254
動作テスト用店舗2,1101,A69X20A0002,SDDF,1,10.2.100.254
動作テスト用店舗3,1102,A69X20A0003,SDDF,2,10.3.100.254
,1102,A69X20A0004,SDDF,1,10.4.100.254,1</pre>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12" style="text-align: right">
            <button type="button" class="btn" onclick="window.close();"><i class="icon-remove"></i>&nbsp;閉じる</button>
        </div>
    </div>
@stop