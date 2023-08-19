@extends('admin.common.onlylayout')

@section('content')
    <h2>認証ログ&nbsp;-&nbsp;CSVフォーマット詳細</h2><br>

    <div class="row-fluid">
        <div class="span12">
            <h4>各行について</h4>
            <ul>
                <li>1行目以降 .. 1行1件の認証ログです。 <br>※データヘッダは出力されません。</li>
            </ul>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>各行の意味について</h4>
            <ul>
                <li>データ内に改行が含まれる場合、その改行は削除されて表示されます。</li>
                <li>データ内にカンマ(,)が含まれる場合、データの両端がダブルクォート(")で括られます。</li>
                <li>データ内にダブルクォート(")が含まれる場合、そのダブルクォートはダブルクォート二個(" -> "")に変換されます。</li>
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
                    <td>アクセス時刻</td>
                    <td>認証リクエストの受信時刻</td>
                </tr>
                <tr>
                    <td class="numeric">2</td>
                    <td>認証結果</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">3</td>
                    <td>シリアル</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">4</td>
                    <td>原因</td>
                    <td>エラーが発生した場合の原因</td>
                </tr>
                <tr>
                    <td class="numeric">5</td>
                    <td>リクエスト1</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">6</td>
                    <td>レスポンス1</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">7</td>
                    <td>リクエスト2</td>
                    <td></td>
                </tr>
                <tr id="statTarget">
                    <td class="numeric">8</td>
                    <td>レスポンス2</td>
                    <td></td>
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
@stop
