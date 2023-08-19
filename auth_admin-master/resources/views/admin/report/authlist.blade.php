@extends('admin.common.onlylayout')

@section('content')
    <h2>通常検索</h2>

    <div class="pagination">
        {{ $result->appends(Request::only('client'))->render() }}
    </div>

    <p>
        <span class="label label-info">info</span>&nbsp;
        <a href="/admin/report/auth/explain" onclick="window.open('/admin/report/auth/explain', '', 'width=500,height=400,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes'); return false;">
                認証状況と失敗原因コードについて（別ウィンドウ）
        </a>
    </p>

    <table class="table table-striped table-bordered fixed">
        <thead>
            <tr>
                <th style="width:100px">アクセス時刻</th>
                <th style="width:75px">認証状況</th>
                <th style="width:100px">失敗原因コード</th>
                <th style="width:75px">ゲームID</th>
                <th style="width:75px">ゲームVer</th>
                <th style="width:100px">シリアル</th>
                <th style="width:50px">店舗ID</th>
                <th style="width:600px">リクエスト</th>
                <th style="width:1200px">レスポンス</th>
            </tr>
        </thead>
        <tbody>
        @if(isset($result) && count($result) > 0)
            @foreach ($result as $data)
                <tr>
                    <td>{{date("Y-m-d H:i:s", strtotime($data->access_date))}}</td>
                    <td>{{$data->result}}</td>
                    <td>{{$data->cause}}</td>
                    <td>{{$data->title_id}}</td>
                    <td>{{$data->title_ver}}</td>
                    <td>{{$data->client_id}}</td>
                    <td>{{$data->place_id}}</td>
                    <td><p class="word-break">{{$data->request}}</p></td>
                    <td><p class="word-break">{{$data->response}}</p></td>
                </tr>
            @endforeach
        @else
            <tr>
                <td colspan="13">認証ログは存在しません。</td>
            </tr>
        @endif
        </tbody>
    </table>

    <div class="pagination">
        {{ $result->render() }}
    </div>
@stop