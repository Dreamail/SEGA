@extends('admin.common.onlylayout')

@section('content')

    <div class="span3">
        <table class="table table-striped table-bordered">
            <caption class="caption">認証状態について</caption>
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
    <div class="span9">
        <table class="table table-striped table-bordered">
            <caption class="caption">ダウンロード状態コードについて</caption>
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
    <div class="row">
        <div class="span12" style="text-align: right">
            <button type="button" class="btn" onclick="window.close();"><i class="icon-remove"></i>&nbsp;閉じる</button>
        </div>
    </div>
    </div>

    <!--/row-->
@stop
