@extends('admin.common.onlylayout')

@section('content')

    <h2>配信レポート&nbsp;-&nbsp;CSVフォーマット詳細</h2><br>

    <div class="row-fluid">
        <div class="span12">
            <h4>各行について</h4>
            <ul>
                <li>1行目 .. 各列の意味を示すヘッダ行です</li>
                <li>2行目以降 .. 1行1件の配信レポートです。</li>
            </ul>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>各列の意味について</h4>
            <ul>
                <li>配信レポートのCSVデータは「アプリ」「オプション」で独立しており、列のフォーマットが少し異なります。<br>具体的には、「オプション」には "作業中APPバージョン", "作業中OSバージョン" の2つの項目がありません。</li>
            </ul>
            <ul>
                <li>データ内に改行が含まれる場合、その改行は削除されて表示されます。</li>
                <li>データ内にカンマが含まれる場合、データの両端がダブルクォートで括られます。</li>
                <li>データ内にダブルクォートが含まれる場合、そのダブルクォートはダブルクォート二個(" -&gt; "")に変換されます。</li>
            </ul>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>アプリイメージのフォーマット</h4>
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
                    <td>シリアル</td>
                    <td>キーチップシリアル(例：A69E01A9999)</td>
                </tr>
                <tr>
                    <td class="numeric">2</td>
                    <td>ゲームID</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">3</td>
                    <td>ALL.Net ID</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">4</td>
                    <td>店舗ID</td>
                    <td>16進数表記の店舗ID</td>
                </tr>
                <tr>
                    <td class="numeric">5</td>
                    <td>店舗名</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">6</td>
                    <td>DL率</td>
                    <td>通信エラーなどで総セグメント数が -1 となった場合は -1 になる</td>
                </tr>
                <tr>
                    <td class="numeric">7</td>
                    <td>公開済みファイルリスト</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">8</td>
                    <td>作業中ファイルリスト</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">9</td>
                    <td>総セグメント数</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">10</td>
                    <td>DL済みセグメント数</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">11</td>
                    <td>認証時刻</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">12</td>
                    <td>DL開始日時</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">13</td>
                    <td>公開日時</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">14</td>
                    <td>認証状態</td>
                    <td>1:認証失敗 2:認証成功</td>
                </tr>
                <tr>
                    <td class="numeric">15</td>
                    <td>DL状態</td>
                    <td>DL状態の詳細は別途記載</td>
                </tr>
                <tr>
                    <td class="numeric">16</td>
                    <td>指示書説明</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">17</td>
                    <td>公開済みAPPバージョン</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">18</td>
                    <td>作業中APPバージョン</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">19</td>
                    <td>公開済みOSバージョン</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">20</td>
                    <td>作業中OSバージョン</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">21</td>
                    <td>アクセス時刻</td>
                    <td>レポートの受信時刻</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <h4>オプションイメージのフォーマット</h4>
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
                    <td>シリアル</td>
                    <td>キーチップシリアル(例：A69E01A9999)</td>
                </tr>
                <tr>
                    <td class="numeric">2</td>
                    <td>ゲームID</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">3</td>
                    <td>ALL.Net ID</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">4</td>
                    <td>店舗ID</td>
                    <td>16進数表記の店舗ID</td>
                </tr>
                <tr>
                    <td class="numeric">5</td>
                    <td>店舗名</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">6</td>
                    <td>DL率</td>
                    <td>通信エラーなどで総セグメント数が -1 となった場合は -1 になる</td>
                </tr>
                <tr>
                    <td class="numeric">7</td>
                    <td>公開済みファイルリスト</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">8</td>
                    <td>作業中ファイルリスト</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">9</td>
                    <td>総セグメント数</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">10</td>
                    <td>DL済みセグメント数</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">11</td>
                    <td>認証時刻</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">12</td>
                    <td>DL開始日時</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">13</td>
                    <td>公開日時</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">14</td>
                    <td>認証状態</td>
                    <td>1:認証失敗 2:認証成功</td>
                </tr>
                <tr>
                    <td class="numeric">15</td>
                    <td>DL状態</td>
                    <td>DL状態の詳細は別途記載</td>
                </tr>
                <tr>
                    <td class="numeric">16</td>
                    <td>指示書説明</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">17</td>
                    <td>公開済みAPPバージョン</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">18</td>
                    <td>公開済みOSバージョン</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">19</td>
                    <td>アクセス時刻</td>
                    <td>レポートの受信時刻</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <h4>認証状態について</h4>
            <table class="table table-striped table-bordered">
                <thead>
                <tr>
                    <th style="width: 60px">認証状態</th>
                    <th>内容</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="numeric">1</td>
                    <td>認証失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">2</td>
                    <td>認証成功。</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span12">
            <h4>ダウンロード状態コードについて</h4>
            <table class="table table-striped table-bordered">
                <thead>
                <tr>
                    <th style="width: 60px">DL状態</th>
                    <th>内容</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="numeric">0</td>
                    <td>初期状態。</td>
                </tr>
                <tr>
                    <td class="numeric">1</td>
                    <td>配信指示書の取得中。</td>
                </tr>
                <tr>
                    <td class="numeric">2</td>
                    <td>ダウンロードの待機中。</td>
                </tr>
                <tr>
                    <td class="numeric">3</td>
                    <td>イメージをダウンロード中。</td>
                </tr>
                <tr>
                    <td class="numeric">100</td>
                    <td>配信指示書が指定されていないので配信処理を行わない。</td>
                </tr>
                <tr>
                    <td class="numeric">101</td>
                    <td>配信指示書にダウンロード項目が無いため、何もダウンロードしないでレポートだけPOSTする。</td>
                </tr>
                <tr>
                    <td class="numeric">102</td>
                    <td>(オプション用)アプリイメージが解禁状態に入ったため、オプションイメージのダウンロード処理はキャンセルされた。</td>
                </tr>
                <tr>
                    <td class="numeric">103</td>
                    <td>(アプリ用)オプションイメージが解禁状態に入ったため、アプリイメージのダウンロード処理はキャンセルされた。</td>
                </tr>
                <tr>
                    <td class="numeric">110</td>
                    <td>配信処理開始後に全てのファイルをダウンロード済み。（解禁は行わない）</td>
                </tr>
                <tr>
                    <td class="numeric">120</td>
                    <td>配信処理開始時に全てのファイルをダウンロード済み。（RELEASE_TIME待ち）</td>
                </tr>
                <tr>
                    <td class="numeric">121</td>
                    <td>配信処理開始時に全てのファイルをダウンロード済みだが、サーバーとの同期が取れていないため解禁できない。</td>
                </tr>
                <tr>
                    <td class="numeric">122</td>
                    <td>配信処理開始時に全てのファイルをダウンロード済み。（2度目以降のresumeのため、即時解禁は行わず次回投電時に解禁する）</td>
                </tr>
                <tr>
                    <td class="numeric">123</td>
                    <td>配信処理開始時に全てのファイルをダウンロード済み。（2度目以降のアプリ起動のため、即時解禁は行わず次回投電時に解禁する）</td>
                </tr>
                <tr>
                    <td class="numeric">124</td>
                    <td>配信処理開始時に全てのファイルをダウンロード済み。（アプリによる解禁キック待ち）</td>
                </tr>
                <tr>
                    <td class="numeric">130</td>
                    <td>全てのファイルをダウンロード済みで、かつ公開済み。</td>
                </tr>
                <tr>
                    <td class="numeric">200</td>
                    <td>認証に失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">300</td>
                    <td>配信指示書をHTTP経由での取得に失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">301</td>
                    <td>(現在未使用)配信指示書をHTTP経由でもバックアップからも取得に失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">302</td>
                    <td>配信指示書の解析に失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">303</td>
                    <td>キーチップと配信指示書でGameIDが不一致のエラー。</td>
                </tr>
                <tr>
                    <td class="numeric">304</td>
                    <td>配信指示書内のアプリイメージリスト、もしくはリストとターゲット内のICFとの間に矛盾あり。</td>
                </tr>
                <tr>
                    <td class="numeric">305</td>
                    <td>(オプション用)アプリ用配信指示書の取得に失敗したので、オプション用配信指示書の取得処理がキャンセルされた。</td>
                </tr>
                <tr>
                    <td class="numeric">400</td>
                    <td>BOOTIDの取得・解析に失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">401</td>
                    <td>ファイルサーバーへのアクセスに失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">402</td>
                    <td>URLに指定されたイメージが存在しなかった。</td>
                </tr>
                <tr>
                    <td class="numeric">403</td>
                    <td>URLに指定されたイメージへのアクセスに失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">500</td>
                    <td>アプリイメージファイルのダウンロードに失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">501</td>
                    <td>オプションイメージファイルのダウンロードに失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">600</td>
                    <td>(現在未使用)ストレージ要領不足。</td>
                </tr>
                <tr>
                    <td class="numeric">601</td>
                    <td>(現在未使用)ファイルのアンインストールに失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">602</td>
                    <td>アプリイメージファイルのインストールに失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">603</td>
                    <td>オプションイメージファイルのインストールに失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">900</td>
                    <td>(内部エラー)配信指示書取得処理に失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">901</td>
                    <td>(内部エラー)ICF関連の処理に失敗。</td>
                </tr>
                <tr>
                    <td class="numeric">902</td>
                    <td>(内部エラー)解禁チェック処理に失敗。</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span12" style="text-align: right">
            <button type="button" class="btn" onclick="window.close();"><i class="icon-remove"></i>&nbsp;閉じる</button>
        </div>
    </div>
    </div>
    <!--/row-->
@stop
