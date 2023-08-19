@extends('admin.common.onlylayout')

@section('content')
    <h2>配信レポート履歴</h2>

    <div class="pagination">
        {{ $result->appends(['client' => $client, 'imagetype' => $imagetype])->render() }}
    </div>

    <p>
        <span class="label label-info">info</span>&nbsp;
        <a href="/admin/report/status/explain" onclick="window.open('/admin/report/status/explain', '', 'width=500,height=400,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes'); return false;">
            認証状態とダウンロード状態について（別ウィンドウ）
        </a>
    </p>

    <table class="table table-bordered">
        <thead>
            <tr>
                <th class="header-field-wide"></th>
                <th>DL状態</th>
                <th>DL率</th>
                <th>認証状態</th>
                <th>認証時刻</th>
            </tr>
        </thead>
        <tbody>
        @if(isset($result) && count($result) > 0)
            @foreach ($result as $data)
                <tr>
                    <th>{{$data->access}}</th>
                    <td>{{$data->download_state}}</td>
                    <td>{{$data->download_ratio}}</td>
                    <td>{{$data->auth_state}}</td>
                    <td>{{$data->auth_time}}</td>
                </tr>
            @endforeach
        @else
            <tr>
                <th></th>
                <td colspan="4">配信レポート履歴は存在しません。</td>
            </tr>
        @endif
        </tbody>
    </table>

    <div class="pagination">
        {{ $result->appends(['client' => $client, 'imagetype' => $imagetype])->render() }}
    </div>
@stop