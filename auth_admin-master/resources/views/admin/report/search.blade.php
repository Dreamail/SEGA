@extends('admin.common.layout')

@section('content')
    <script type="text/javascript">
        <!--
            function getTableData(obj) {
                //プルダウンで選択されたValueを取得
                var selectVal = obj.options[obj.selectedIndex].value;
                var clientGroup = $("#client_group");	//連動するプルダウンのID
                clientGroup.children().remove();	//子要素は毎回全て削除します(初期化)
                if(selectVal != "") {
                    $.get("/admin/find/title?titleId=" + selectVal ,function(data){ //ajaxの通信に成功した場合
                        var obj = $.parseJSON(data);
                        for (i in data) {
                            var row = data[i];
                            //取得したデータをAppendで1行ずつ追加
                            clientGroup.append(new Option(row['client_group'], row['client_group']));
                        }
                    });
                }
            }
    </script>

    <!--/span-->
    <div id="content" class="span10">
        <ul class="breadcrumb">
            <li class="active">
                配信ステータス閲覧
            </li>
        </ul>
        <h2>配信ステータス閲覧</h2><br>

        @if (count($errors) > 0)
            <div class="alert alert-error">
                @foreach ($errors->all() as $error)
                    <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                @endforeach
            </div>
        @endif
        @if(isset($result) && count($result) == 0)
        <div class="alert alert-info">
            <span class="badge badge-info">info</span>&nbsp;該当するデータが見つかりませんでした。
        </div>
        @endif
        <div class="accordion" id="searchForm">
            <div class="accordion-group">
                <div class="accordion-heading" title="クリックすることでフォームを開閉できます">
                <span class="accordion-toggle" data-toggle="collapse"
                      data-parent="#searchForm" href="#searchFormBody">
                    <i id="searchFormIcon" class="icon-minus-sign"></i>&nbsp;{{Config::get('message.SEARCH')}}フォーム
                </span>
                </div>
                <div id="searchFormBody" class="accordion-body collapse in searchFormBody">
                    <div class="accordion-inner">
                        <ul class="nav nav-tabs" id="searchDeliverReportFormTab">
                            <li @if(isset($searchType) && $searchType=="serial" || old('searchType')=="serial") class="active" @endif>
                                    <a href="#searchFormSerialTab" title="タブをクリックすると{{Config::get('message.SEARCH')}}フォームが切り替わります">シリアル</a>
                            </li>
                            <li @if((isset($searchType) && $searchType=="title" && old('searchType')=="") || old('searchType')=="title") class="active" @endif>
                                <a href="#searchFormGameIdTab" title="タブをクリックすると{{Config::get('message.SEARCH')}}フォームが切り替わります">ゲームID</a>
                            </li>
                            <li @if(isset($searchType) && $searchType=="place" || old('searchType')=="place") class="active" @endif>
                                <a href="#searchFormPlaceIdTab" title="タブをクリックすると{{Config::get('message.SEARCH')}}フォームが切り替わります">店舗ID</a>
                            </li>
                            <li @if(isset($searchType) && $searchType=="group" || old('searchType')=="group") class="active" @endif>
                                <a href="#searchFormGroupIdTab" title="タブをクリックすると{{Config::get('message.SEARCH')}}フォームが切り替わります">クライアントグループ</a>
                            </li>
                        </ul>

                        <div class="tab-content">
                            <div @if($searchType=="serial"|| old('searchType')=="serial") class="tab-pane active" @else class="tab-pane" @endif id="searchFormSerialTab">
                                <form id="serialForm" class="form-horizontal" action="/admin/report/search" method="post">
                                    <fieldset>
                                        <div class="control-group">
                                            <label for="serial" class="control-label">シリアル</label>
                                            <div class="controls">
                                                <input id="serial" name="serial" type="text" value=""/>
                                            </div>
                                        </div>
                                    </fieldset>
                                    <fieldset>
                                        <input id="searchType" name="searchType" value="serial" type="hidden" />
                                    </fieldset>
                                    <fieldset>
                                        <div class="control-group">
                                            <div class="controls">
                                                <button type="submit" class="btn btn-primary"><i class="icon-search icon-white"></i>&nbsp;{{Config::get('message.SEARCH')}}</button>&nbsp;&nbsp;
                                                <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>
                                            </div>
                                        </div>
                                    </fieldset>
                                </form>
                            </div>

                            <div @if(($searchType=="title" && old('searchType')=="") || old('searchType')=="title") class="tab-pane active" @else class="tab-pane" @endif id="searchFormGameIdTab">
                                <form id="gameIdForm" class="form-horizontal" action="/admin/report/search" method="post">
                                    <fieldset>
                                        <div class="control-group">
                                            <label for="gameId" class="control-label">ゲームID</label>
                                            <div class="controls">
                                                <select id="gameId" name="gameId" class="focused">
                                                    <option value="">
                                                        ゲームIDを選択してください
                                                    </option>
                                                    @foreach ($gameData as $game)
                                                        @if (isset($gameId) && $game->game_id === $gameId))
                                                            <option value="{{ $game->game_id }} " selected >{{ $game->game_id }}:{{ $game->title }}</option>
                                                        @else
                                                            <option value="{{ $game->game_id }} ">{{ $game->game_id }}:{{ $game->title }}</option>
                                                        @endif
                                                    @endforeach
                                                </select>

                                            </div>
                                        </div>
                                    </fieldset>
                                    <fieldset>
                                        <div class="control-group">
                                            <label for="imageType" class="control-label">{{Config::get('message.IMAGE_TYPE')}}</label>
                                            <div class="controls">
                                                <select id="imageType" name="imageType">
                                                    <option value="{{Config::get('const.IMAGE_TYPE_CHAR')['APP']}}" @if($imageType==Config::get('const.IMAGE_TYPE_CHAR')['APP']) selected @endif>{{Config::get('message.TYPE_APP')}}</option>
                                                    <option value="{{Config::get('const.IMAGE_TYPE_CHAR')['OPT']}}" @if($imageType==Config::get('const.IMAGE_TYPE_CHAR')['OPT']) selected @endif>{{Config::get('message.TYPE_OPT')}}</option>
                                                </select>
                                            </div>
                                        </div>
                                    </fieldset>
                                    <fieldset>
                                        <div class="control-group">
                                            <label for="downloadStates" class="control-label">
                                                DL状態
                                                <a rel="popover" title="DL状態" data-content="DL状態を選択することで、選択されたDL状態のレポートのみ表示できます。<br>「Shift」または「Ctrl」キーを押しながら選択することで複数選択が可能です。"><i class="icon-question-sign"></i></a>
                                            </label>
                                            <div class="controls">
                                                <select id="downloadStates" name="downloadStates[]" multiple="multiple" size="5">
                                                    <option value="0">0:初期状態</option>
                                                    <option value="1">1:配信指示書の取得中</option>
                                                    <option value="2">2:ダウンロード待機中</option>
                                                    <option value="3">3:イメージをダウンロード中</option>
                                                    <option value="100">100:配信指示書が指定されていないので配信処理を行わない</option>
                                                    <option value="101">101:配信指示書にダウンロード項目が無いため、何もダウンロードしないでレポートだけPOSTする</option>
                                                    <option value="102">102:アプリイメージが解禁状態に入ったため、オプションイメージのダウンロード処理はキャンセルされた</option>
                                                    <option value="103">103:オプションイメージが解禁状態に入ったため、アプリイメージのダウンロード処理はキャンセルされた</option>
                                                    <option value="110">110:ダウンロード済み（解禁は行わない）</option>
                                                    <option value="120">120:ダウンロード済み（RELEASE_TIME待ち）</option>
                                                    <option value="121">121:ダウンロード済み（サーバーとの同期が取れていないため、解禁できない）</option>
                                                    <option value="122">122:ダウンロード済み（2度目以降のresumeのため、即時解禁は行わず次回投電時に解禁する）</option>
                                                    <option value="123">123:ダウンロード済み（2度目以降のアプリ起動のため、即時解禁は行わず次回投電時に解禁する）</option>
                                                    <option value="124">124:ダウンロード済み（アプリによる解禁キック待ち）</option>
                                                    <option value="130">130:ダウンロード済み（公開済み）</option>
                                                    <option value="200">200:エラー状態（認証に失敗）</option>
                                                    <option value="300">300:エラー状態（配信指示書をHTTP経由での取得に失敗）</option>
                                                    <option value="301">301:エラー状態（配信指示書をHTTP経由およびバックアップから取得に失敗）</option>
                                                    <option value="302">302:エラー状態（配信指示書の解析に失敗）</option>
                                                    <option value="303">303:エラー状態（キーチップと配信指示書でGameID が不一致）</option>
                                                    <option value="304">304:エラー状態（配信指示書内のアプリイメージリスト、もしくはリストとターゲット内のICFとの間に矛盾あり）</option>
                                                    <option value="305">305:エラー状態（アプリ用配信指示書取得失敗により、オプション用配信指示書の取得処理がキャンセル）</option>
                                                    <option value="400">400:エラー状態（BOOTIDの取得・解析に失敗）</option>
                                                    <option value="401">401:エラー状態（ファイルサーバへのアクセスに失敗）</option>
                                                    <option value="402">402:エラー状態（URLに指定されたイメージが存在しなかった）</option>
                                                    <option value="403">403:エラー状態（URLに指定されたイメージへのアクセスに失敗）</option>
                                                    <option value="500">500:エラー状態（アプリイメージファイルのダウンロードに失敗）</option>
                                                    <option value="501">501:エラー状態（オプションイメージファイルのダウンロードに失敗）</option>
                                                    <option value="600">600:エラー状態（ストレージ要領不足）</option>
                                                    <option value="601">601:エラー状態（ファイルのアンインストールに失敗）</option>
                                                    <option value="602">602:エラー状態（アプリイメージファイルのインストールに失敗）</option>
                                                    <option value="603">603:エラー状態（オプションイメージファイルのインストールに失敗）</option>
                                                    <option value="900">900:内部エラー（配信指示書取得処理に失敗）</option>
                                                    <option value="901">901:内部エラー（ICF関連の処理に失敗）</option>
                                                    <option value="902">902:内部エラー（解禁チェック処理に失敗）message.search.account.onlyFreeze=凍結されたユーザのみ表示</option>
                                                </select>
                                                <input type="hidden" name="_downloadStates" value="1"/>
                                            </div>
                                        </div>
                                    </fieldset>
                                    <fieldset>
                                        <input id="searchType" name="searchType" value="title" type="hidden" />
                                    </fieldset>
                                    <fieldset>
                                        <div class="control-group">
                                            <div class="controls">
                                                <p><span class="label label-info">info</span>&nbsp;<a href="/admin/report/csv/explain" onclick="window.open('/admin/report/csv/explain', '', 'width=700,height=400,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes'); return false;">CSVフォーマットの詳細について（別ウィンドウ）</a></p>
                                                <button type="submit" class="btn btn-primary" name="action" value="search"><i class="icon-search icon-white"></i>&nbsp;{{Config::get('message.SEARCH')}}</button>&nbsp;&nbsp;
                                                <button type="submit" name="action" value="CsvDownload" class="btn"><i class="icon-download"></i>&nbsp;{{Config::get('message.CSV_DL')}}</button>
                                                <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>
                                            </div>
                                        </div>
                                    </fieldset>
                                </form>
                            </div>

                            <div @if($searchType=="place" || old('searchType')=="place") class="tab-pane active" @else class="tab-pane" @endif id="searchFormPlaceIdTab">
                                <form id="allnetIdForm" class="form-horizontal" action="/admin/report/search" method="post">
                                    <fieldset>
                                        <div class="control-group">
                                            <label for="placeId" class="control-label">店舗ID</label>
                                            <div class="controls">
                                                <input id="placeId" name="placeId" type="text" value="@if(isset($placeId)){{$placeId}}@endif"/>
                                            </div>
                                        </div>
                                    </fieldset>
                                    <fieldset>
                                        <div class="control-group">
                                            <label for="imageType" class="control-label">{{Config::get('message.IMAGE_TYPE')}}</label>
                                            <div class="controls">
                                                <select id="imageType" name="imageType">
                                                    <option value="{{Config::get('const.IMAGE_TYPE_CHAR')['APP']}}" @if($imageType==Config::get('const.IMAGE_TYPE_CHAR')['APP']) selected @endif>{{Config::get('message.TYPE_APP')}}</option>
                                                    <option value="{{Config::get('const.IMAGE_TYPE_CHAR')['OPT']}}" @if($imageType==Config::get('const.IMAGE_TYPE_CHAR')['OPT']) selected @endif>{{Config::get('message.TYPE_OPT')}}</option>
                                                </select>
                                            </div>
                                        </div>
                                    </fieldset>
                                    <fieldset>
                                        <input id="searchType" name="searchType" value="place" type="hidden" />
                                    </fieldset>
                                    <fieldset>
                                        <div class="control-group">
                                            <div class="controls">
                                                <p><span class="label label-info">info</span>&nbsp;<a href="/admin/report/csv/explain" onclick="window.open('/admin/report/csv/explain', '', 'width=700,height=400,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes'); return false;">CSVフォーマットの詳細について（別ウィンドウ）</a></p>
                                                <button type="submit" class="btn btn-primary" name="action" value="search"><i class="icon-search icon-white"></i>&nbsp;{{Config::get('message.SEARCH')}}</button>&nbsp;&nbsp;
                                                <button type="submit" name="action" value="CsvDownload" class="btn"><i class="icon-download"></i>&nbsp;{{Config::get('message.CSV_DL')}}</button>
                                                <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>
                                            </div>
                                        </div>
                                    </fieldset>
                                </form>
                            </div>
                            <div @if($searchType=="group" || old('searchType')=="group") class="tab-pane active" @else class="tab-pane" @endif id="searchFormGroupIdTab">
                                <form id="allnetIdForm" class="form-horizontal" action="/admin/report/search" method="post">
                                    <fieldset>
                                        <div class="control-group">
                                            <label for="gameId" class="control-label">ゲームID</label>
                                            <div class="controls">
                                                <select id="gameId" name="gameId" class="focused" onchange="getTableData(this);">
                                                    <option value="">
                                                        ゲームIDを選択してください
                                                    </option>
                                                    @foreach ($gameData as $game)
                                                        @if (isset($gameId) && $game->game_id === $gameId))
                                                        <option value="{{ $game->game_id }} " selected >{{ $game->game_id }}:{{ $game->title }}</option>
                                                        @else
                                                            <option value="{{ $game->game_id }} ">{{ $game->game_id }}:{{ $game->title }}</option>
                                                        @endif
                                                    @endforeach
                                                </select>

                                            </div>
                                        </div>
                                    </fieldset>
                                    <fieldset>
                                        <div class="control-group">
                                            <label for="client_group" class="control-label">クライアントグループ</label>
                                            <div class="controls">
                                                <select id="client_group" name="client_group" class="focused">
                                                </select>
                                            </div>
                                        </div>
                                    </fieldset>
                                    <fieldset>
                                        <div class="control-group">
                                            <label for="imageType" class="control-label">{{Config::get('message.IMAGE_TYPE')}}</label>
                                            <div class="controls">
                                                <select id="imageType" name="imageType">
                                                    <option value="{{Config::get('const.IMAGE_TYPE_CHAR')['APP']}}" @if($imageType==Config::get('const.IMAGE_TYPE_CHAR')['APP']) selected @endif>{{Config::get('message.TYPE_APP')}}</option>
                                                    <option value="{{Config::get('const.IMAGE_TYPE_CHAR')['OPT']}}" @if($imageType==Config::get('const.IMAGE_TYPE_CHAR')['OPT']) selected @endif>{{Config::get('message.TYPE_OPT')}}</option>
                                                </select>
                                            </div>
                                        </div>
                                    </fieldset>
                                    <fieldset>
                                        <input id="searchType" name="searchType" value="group" type="hidden" />
                                    </fieldset>
                                    <fieldset>
                                        <div class="control-group">
                                            <div class="controls">
                                                <p><span class="label label-info">info</span>&nbsp;<a href="/admin/report/csv/explain" onclick="window.open('/admin/report/csv/explain', '', 'width=700,height=400,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes'); return false;">CSVフォーマットの詳細について（別ウィンドウ）</a></p>
                                                <button type="submit" class="btn btn-primary" name="action" value="search"><i class="icon-search icon-white"></i>&nbsp;{{Config::get('message.SEARCH')}}</button>&nbsp;&nbsp;
                                                <button type="submit" name="action" value="CsvDownload" class="btn"><i class="icon-download"></i>&nbsp;{{Config::get('message.CSV_DL')}}</button>
                                                <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>
                                            </div>
                                        </div>
                                    </fieldset>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        @if(isset($result) && count($result) > 0)

        <p><span class="label label-info">info</span>&nbsp;<a href="/admin/report/status/explain" onclick="window.open('/admin/report/status/explain', '', 'width=500,height=400,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes'); return false;">認証状態とダウンロード状態について（別ウィンドウ）</a></p>

        <table class="table table-striped table-bordered fixed">
            <thead>
                <tr>
                    <th style="width:85px">シリアル</th>
                    <th style="width:70px">認証ログ</th>
                    <th style="width:50px">ゲームID</th>
                    <th style="width:40px">店舗ID</th>
                    <th style="width:200px">店舗名</th>
                    <th style="width:100px">DL率</th>
                    <th style="width:70px">認証時刻</th>
                    <th style="width:75px">DL開始日時</th>
                    <th style="width:70px">公開日時</th>
                    <th style="width:30px">認証状態</th>
                    <th style="width:30px">DL状態</th>
                    <th style="width:80px">公開済みAPバージョン</th>
                    <th style="width:80px">公開済みOSバージョン</th>
                    <th style="width:80px">アクセス時刻</th>
                </tr>
            </thead>
            <tbody>
                @foreach ($result as $data)
                    <tr>
                        <td><a href="/admin/report/search/client?client={{$data->serial}}" title="この基板の配信レポート詳細を表示します。">{{$data->serial}}</a></td>
                        <td>
                            <a href="/admin/report/search/auth?client={{$data->serial}}" title="この基板のログ一覧を表示します。" target="_blank">
                                <span class="label label-inverse"><i class="icon-file icon-white"></i>表示</span>
                            </a>
                        </td>
                        <td>{{$data->title_id}}</td>
                        <td>{{$data->place_id}}</td>
                        <td>{{$data->name}}</td>
                        <td>{{$data->download_ratio}}<br>({{$data->segs_downloaded}}/{{$data->segs_total}})</td>
                        <td style="background-color: lavenderblush">
                            {{$data->auth_time}}
                        </td>
                        <td>{{$data->order_time}}</td>
                        <td>{{$data->release_time}}</td>
                        <td>{{$data->auth_state}}</td>
                        <td>{{$data->download_state}}</td>
                        <td>{{$data->ap_ver_released}}</td>
                        <td>{{$data->os_ver_released}}</td>
                        <td>{{$data->update}}</td>
                    </tr>
                @endforeach
            </tbody>
        </table>
        @endif
    </div>
    <!--/span-->

@stop
