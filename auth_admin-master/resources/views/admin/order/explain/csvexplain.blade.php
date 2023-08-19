@extends('admin.common.onlylayout')

@section('content')
    <h2>配信指示書設定情報&nbsp;-&nbsp;CSVフォーマット詳細</h2><br>

    <div class="row-fluid">
        <div class="span12">
            <h4>アップロードファイルについて</h4>
            <ul>
                <li>アップロードするファイルの文字コードは「UTF-8」とする。</li>
            </ul>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>各行について</h4>
            <ul>
                <li>1行目以降 .. 1行1件の配信指示書情報です。</li>
            </ul>
        </div>
    </div>
    <br>
    <div class="row-fluid">
        <div class="span12">
            <h4>各列の意味について</h4>
            <ul>
                <li>データ内にカンマが含まれる場合、データの両端がダブルクォートで括られている必要があります。</li>
                <li>データ内にダブルクォートが含まれる場合、そのダブルクォートはダブルクォート二個(" -&gt; "")に変換されます。</li>
            </ul>
        </div>
    </div>
    <br>
    <div class="row-fluid">
        <div class="span12">
            <h4>配信指示書の種類</h4>
            <ul>
                <li><a href="#game">配信指示書</a></li>
                <li><a href="#machine">基板配信指示書</a></li>
                <li><a href="#clientgroup">シリアルグループ配信指示書</a></li>
            </ul>
        </div>
    </div>
    <br>
    <div class="row-fluid">
        <hr>
        <h3 id='game'>配信指示書</h3>
        <br>
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
                    <td>ゲームID</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">2</td>
                    <td>ゲームVer</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">3</td>
                    <td>イメージタイプ</td>
                    <td>APP:アプリケーション、OPT:オプション</td>
                </tr>
                <tr>
                    <td class="numeric">4</td>
                    <td>URL</td>
                    <td>空の場合は削除処理を行う</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>CSVの例</h4>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <td>
                        SBXX,1.00,APP,test<br>
                        SBXX,2.00,OPT,test<br>
                        SBXX,3.00,APP,<br>
                    </td>
                </tr>
                </thead>
            </table>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12" style="text-align: right">
            <button type="button" class="btn" onclick="window.close();"><i class="icon-remove"></i>&nbsp;閉じる</button>
        </div>
    </div>

    <div class="row-fluid">
        <hr>
        <h3 id='machine'>基板配信指示書</h3>
        <br>
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
                    <td>ゲームID</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">2</td>
                    <td>シリアル番号</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="numeric">3</td>
                    <td>イメージタイプ</td>
                    <td>APP:アプリケーション、OPT:オプション</td>
                </tr>
                <tr>
                    <td class="numeric">4</td>
                    <td>URL</td>
                    <td>空の場合は削除処理を行う</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>CSVの例</h4>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <td>
                        SBXX,A0000000001,APP,test<br>
                        SBXX,A0000000002,OPT,test<br>
                        SBXX,A0000000003,APP,<br>
                    </td>
                </tr>
                </thead>
            </table>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12" style="text-align: right">
            <button type="button" class="btn" onclick="window.close();"><i class="icon-remove"></i>&nbsp;閉じる</button>
        </div>
    </div>

    <div class="row-fluid">
        <hr>
        <h3 id='clientgroup'>シリアルグループ配信指示書</h3>
        <br>
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
                        <td>ゲームID</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="numeric">2</td>
                        <td>ゲームVer</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="numeric">3</td>
                        <td>シリアルグループ</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="numeric">4</td>
                        <td>イメージタイプ</td>
                        <td>APP:アプリケーション、OPT:オプション</td>
                    </tr>
                    <tr>
                        <td class="numeric">5</td>
                        <td>URL</td>
                        <td>空の場合は削除処理を行う</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>

    <div class="row-fluid">
        <div class="span12">
            <h4>CSVの例</h4>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <td>
                        SBXX,1.000,20180801,APP,http://0.0.0.0/lite/SBXX_1.000_20180801_APP.ini<br>
                        SBXX,2.000,20181101,OPT,<br>
                        SBXX,3.000,20190401,APP,http://0.0.0.0/lite/SBXX_3.000_20190401_APP.ini<br>
                    </td>
                </tr>
                </thead>
            </table>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12" style="text-align: right">
            <button type="button" class="btn" onclick="window.close();"><i class="icon-remove"></i>&nbsp;閉じる</button>
        </div>
    </div>
@stop
