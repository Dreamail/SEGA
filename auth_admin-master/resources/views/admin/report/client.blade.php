@extends('admin.common.layout')

@section('content')
    <!--/span-->
    <div id="content" class="span10">
        <ul class="breadcrumb">
            <li class="notActive">配信ステータス閲覧(Nu版)<span class="divider">/</span></li>
            <li class="active">配信ステータス詳細</li>
        </ul>
        <h2>配信ステータス詳細</h2>
        <br>
        <div class="detail-table">
            <h3>基板情報</h3>
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <th>シリアル</th>
                        <th>ゲームID</th>
                        <th>店舗ID</th>
                        <th>店舗名</th>
                    </tr>
                    <tr>
                        @foreach ($machineData as $machine)
                            <?php $clientId = $machine->serial; ?>
                            <td>{{ $machine->serial }}</td>
                            <td>{{ $machine->game_id }}</td>
                            <td>{{ $machine->place_id }}</td>
                            <td>{{ $machine->name }}</td>
                        @endforeach
                    </tr>
                </tbody>
            </table>
            <p>
                <span class="label label-info">info</span>&nbsp;
                <a href="/admin/report/status/explain" onclick="window.open('/admin/report/status/explain', '', 'width=500,height=400,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes'); return false;">認証状態とダウンロード状態について（別ウィンドウ）</a>
            </p>
        </div>

        @if(isset($appData) && count($appData) > 0)
            @foreach ($appData as $data)
                <div class="detail-table">
                    <h3>アプリケーション</h3>
                    <div class="row-fluid">
                        <div class="span6">
                            <table class="table table-bordered">
                                <tbody>
                                <tr>
                                    <th class="header-field-wide">DL状態</th>
                                    <td>{{$data->download_state}}</td>
                                </tr>
                                <tr>
                                    <th>DL率</th>
                                    <td>{{$data->download_ratio}}&nbsp;({{$data->segs_downloaded}}&nbsp;/&nbsp;{{$data->segs_total}})</td>
                                </tr>
                                <tr>
                                    <th>認証状態</th>
                                    <td>{{$data->auth_state}}</td>
                                </tr>
                                <tr>
                                    <th>認証時刻</th>
                                    <td style="background-color: lavenderblush">{{$data->auth_time}}</td>
                                </tr>
                                <tr>
                                    <th>DL開始日時</th>
                                    <td>{{$data->order_time}}</td>
                                </tr>
                                <tr>
                                    <th>公開日時</th>
                                    <td>{{$data->release_time}}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="span6">
                            <table class="table table-bordered">
                                <tbody>
                                <tr>
                                    <th class="header-field-wide">公開済みAPバージョン</th>
                                    <td>{{$data->ap_ver_released}}</td>
                                </tr>
                                <tr>
                                    <th class="header-field-wide">作業中APバージョン</th>
                                    <td>{{$data->ap_ver_working}}</td>
                                </tr>
                                <tr>
                                    <th>公開済みOSバージョン</th>
                                    <td>{{$data->os_ver_released}}</td>
                                </tr>
                                <tr>
                                    <th>作業中OSバージョン</th>
                                    <td>{{$data->os_ver_working}}</td>
                                </tr>
                                <tr>
                                    <th>アクセス時刻</th>
                                    <td>{{$data->update}}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <table class="table table-bordered">
                            <tbody>
                            <tr>
                                <th class="header-field-wide">指示書説明</th>
                                <td>{{$data->description}}</td>
                            </tr>
                            <tr>
                                <th>公開済みファイルリスト</th>
                                <td>{{$data->files_released}}</td>
                            </tr>
                            <tr>
                                <th>作業中ファイルリスト</th>
                                <td>{{$data->files_working}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            @endforeach
        @endif
        @if(isset($appHistoryData) && count($appHistoryData) > 0)
            <br>
            <b>配信レポート履歴</b>
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
                    @foreach ($appHistoryData as $data)
                        <tr>
                            <th>{{$data->access}}</th>
                            <td>{{$data->download_state}}</td>
                            <td>{{$data->download_ratio}}</td>
                            <td>{{$data->auth_state}}</td>
                            <td>{{$data->auth_time}}</td>
                        </tr>
                    @endforeach
                </tbody>
            </table>
            <form id="viewDeliverReportLogForm" action="/admin/report/search/history" method="get" target="_blank">
                <input type="hidden" name="client" value="{{$clientId}}">
                <input type="hidden" name="imagetype" value="0">
                <button type="submit" class="btn">さらに表示</button>
            </form>

        @endif

        @if(isset($optData) && count($optData) > 0)
            @foreach ($optData as $data)
                <div class="detail-table">
                    <h3>オプション</h3>
                    <div class="row-fluid">
                        <div class="span6">
                            <table class="table table-bordered">
                                <tbody>
                                <tr>
                                    <th class="header-field-wide">DL状態</th>
                                    <td>{{$data->download_state}}</td>
                                </tr>
                                <tr>
                                    <th>DL率</th>
                                    <td>{{$data->download_ratio}}&nbsp;({{$data->segs_downloaded}}&nbsp;/&nbsp;{{$data->segs_total}})</td>
                                </tr>
                                <tr>
                                    <th>認証状態</th>
                                    <td>{{$data->auth_state}}</td>
                                </tr>
                                <tr>
                                    <th>認証時刻</th>
                                    <td style="background-color: lavenderblush">{{$data->auth_time}}</td>
                                </tr>
                                <tr>
                                    <th>DL開始日時</th>
                                    <td>{{$data->order_time}}</td>
                                </tr>
                                <tr>
                                    <th>公開日時</th>
                                    <td>{{$data->release_time}}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="span6">
                            <table class="table table-bordered">
                                <tbody>
                                <tr>
                                    <th class="header-field-wide">公開済みAPバージョン</th>
                                    <td>{{$data->ap_ver_released}}</td>
                                </tr>
                                <tr>
                                    <th>公開済みOSバージョン</th>
                                    <td>{{$data->os_ver_released}}</td>
                                </tr>
                                <tr>
                                    <th>アクセス時刻</th>
                                    <td>{{$data->update}}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <table class="table table-bordered">
                            <tbody>
                            <tr>
                                <th class="header-field-wide">指示書説明</th>
                                <td>{{$data->description}}</td>
                            </tr>
                            <tr>
                                <th>公開済みファイルリスト</th>
                                <td>{{$data->files_released}}</td>
                            </tr>
                            <tr>
                                <th>作業中ファイルリスト</th>
                                <td>{{$data->files_working}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            @endforeach
        @endif

        @if(isset($optHistoryData) && count($optHistoryData) > 0)
            <br>
            <b>配信レポート履歴</b>
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
                @foreach ($optHistoryData as $data)
                    <tr>
                        <th>{{$data->access}}</th>
                        <td>{{$data->download_state}}</td>
                        <td>{{$data->download_ratio}}</td>
                        <td>{{$data->auth_state}}</td>
                        <td>{{$data->auth_time}}</td>
                    </tr>
                @endforeach
                </tbody>
            </table>

            <form id="viewDeliverReportLogForm" action="/admin/report/search/history" method="get" target="_blank">
                <input type="hidden" name="client" value="{{$clientId}}">
                <input type="hidden" name="imagetype" value="1">
                <button type="submit" class="btn">さらに表示</button>
            </form>
        @endif

        <br>
        <form method="get" action="/admin/report/search/return">
            <button class="btn" type="submit">戻る</button>
        </form>
    </div>
    <!--/span-->
@stop
