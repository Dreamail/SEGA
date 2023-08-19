@extends('admin.common.onlylayout')

@section('content')

    <div class="span6">
        <table class="table table-striped table-bordered">
            <caption class="caption">認証状況について</caption>
            <thead>
            <tr>
                <th style="width: 60px">認証状況</th>
                <th>内容</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td class="numeric">1</td>
                <td>認証成功</td>
            </tr>
            <tr>
                <td class="numeric">-1</td>
                <td>ゲーム情報に関するNG</td>
            </tr>
            <tr>
                <td class="numeric">-2</td>
                <td>基板情報に関するNG</td>
            </tr>
            <tr>
                <td class="numeric">-3</td>
                <td>店舗・ルータなどロケーションに関するNG</td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="span6">
        <table class="table table-striped table-bordered">
            <caption class="caption">失敗原因コードについて</caption>
            <thead>
            <tr>
                <th style="width: 60px">失敗原因コード</th>
                <th>内容</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td class="numeric">1</td>
                <td>認証は成功しました。</td>
            </tr>
            <tr>
                <td class="numeric">-1</td>
                <td><span style="text-decoration: underline;">ルータ情報が登録されていません。</span><br>店舗IPに一致するルータ情報が必要です。</td>
            </tr>
            <tr>
                <td class="numeric">-2</td>
                <td><span style="text-decoration: underline;">ルータ情報の内容が不正です。</span><br>店舗IPに一致するルータ情報には、有効な店舗を設定する必要があります。</td>
            </tr>
            <tr>
                <td class="numeric">-3</td>
                <td><span style="text-decoration: underline;">ゲーム情報が登録されていません。</span><br>ゲームID、バージョン、および店舗IPから取得した店舗情報の国コードに一致するゲーム情報が必要です。</td>
            </tr>
            <tr>
                <td class="numeric">-1004</td>
                <td><span style="text-decoration: underline;">基板情報が登録されていません。</span><br>基板シリアルに一致する基板情報が必要です。</td>
            </tr>
            <tr>
                <td class="numeric">-1005</td>
                <td><span style="text-decoration: underline;">基板情報の内容が不正です。</span><br>基板シリアルに一致する基板情報には、有効な店舗を設定する必要があります。</td>
            </tr>
            <tr>
                <td class="numeric">-1006</td>
                <td><span style="text-decoration: underline;">基板情報とルータ情報の内容が不正です。</span><br>基板シリアルに一致する基板情報と店舗IPに一致するルータ情報には、同じ店舗を設定する必要があります。</td>
            </tr>
            <tr>
                <td class="numeric">-1007</td>
                <td><span style="text-decoration: underline;">基板情報の内容が不正です。</span><br>基板シリアルに一致する基板情報のゲームIDと予約ゲームIDは、リクエストのゲームIDと一致する必要があります。</td>
            </tr>
            <tr>
                <td class="numeric">-1008</td>
                <td><span style="text-decoration: underline;">基板情報の内容が不正です。</span><br>基板シリアルに一致する基板情報には、通信セッティングを「許可(1)」に設定する必要があります。</td>
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
