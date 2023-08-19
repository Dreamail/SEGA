@extends('admin.common.layout')

@section('content')

    <!--/span-->
    <div id="content" class="span10">
        <ul class="breadcrumb">
            <li class="notActive">
                データ管理
                <span class="divider">/</span>
            </li>
            <li class="notActive">
                参照・更新・削除
                <span class="divider">/</span>
            </li>
            <li class="active">
                基板情報
            </li>
        </ul>
        <h2>基板情報</h2><br>

        @if (count($errors) > 0)
            <div class="alert alert-error">
                @foreach ($errors->all() as $error)
                    <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                @endforeach
            </div>
        @endif

        <div class="accordion" id="searchForm">
            <div class="accordion-group">
                <div class="accordion-heading" title="クリックすることでフォームを開閉できます ">
                <span class="accordion-toggle" data-toggle="collapse"
                      data-parent="#searchForm" href="#searchFormBody">
                    <i id="searchFormIcon" class="icon-minus-sign"></i>&nbsp;検索フォーム
                </span>
                </div>
                <div id="searchFormBody" class="accordion-body collapse in searchFormBody">
                    <div class="accordion-inner">
                        <form id="viewMachineForm" class="form-horizontal" action="/admin/manage/reference/machine/find" method="post">
                            <fieldset>
                                <div class="control-group">
                                    <label for="searchSerial" class="control-label">基板シリアル</label>
                                    <div class="controls">
                                        <input id="searchClientId" name="searchSerial" type="text" value="{{!empty($serial) ? $serial : ''}}"/>

                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <label for="searchAllnetId" class="control-label">All.Net ID</label>
                                    <div class="controls">
                                        <select id="searchPlaceId" name="searchAllNetId" class="focused">
                                            <option value="">ALL.NetIDを選択してください。</option>
                                            @foreach ($placeData as $place)
                                                @if (isset( $AllNetId ) && $place->allnet_id === $allNetId))
                                                <option value="{{ $place->allnet_id }} " selected >{{ $place->allnet_id }}({{ $place->name }})</option>
                                                @else
                                                    <option value="{{ $place->allnet_id }} ">{{ $place->allnet_id }}({{ $place->name }})</option>
                                                @endif
                                            @endforeach
                                        </select>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <label for="searchGameId" class="control-label">ゲームID</label>
                                    <div class="controls">
                                        <select id="searchTitleId" name="searchGameId" class="focused">
                                            <option value="">ゲームIDを選択してください。</option>
                                            @foreach ($gameData as $game)
                                                @if (isset( $gameId ) && $game->game_id === $gameId))
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
                                    <label for="searchPlaceName" class="control-label">店舗名</label>
                                    <div class="controls">
                                        <input id="searchPlaceName" name="searchPlaceName" type="text" value="{{!empty($placeName) ? $placeName : ''}}"/>

                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <div class="controls">
                                        <button type="submit" class="btn btn-primary"><i class="icon-search icon-white"></i>&nbsp;検索</button>
                                        <button type="reset" class="btn offset-button">クリア</button>
                                    </div>
                                </div>
                            </fieldset>
                            <input id="offset" name="offset" value="0" type="hidden" value="0"/>
                            <input id="fixedHideSearchForm" name="hideSearchForm" value="false" type="hidden" value="false"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        @if(isset($data))
            <table class="table table-striped table-bordered fixed">
                <thead>
                    <tr>
                        <th style="width: 100px">基板シリアル</th>
                        <th style="width: 45px">All.Net ID</th>
                        <th style="width: 70px">店舗IP</th>
                        <th style="width: 250px">店舗名</th>
                        <th style="width: 100px">ゲームID</th>
                        <th style="width: 80px">通信設定</th>
                        <th style="width: 120px">更新日</th>
                        <th style="width: 70px">編集</th>
                        <th style="width: 70px">削除</th>
                    </tr>
                </thead>
                <tbody>
                @foreach ($data as $machineData)
                    <tr>
                        <td>{{$machineData->serial}}</td>
                        <td>{{$machineData->allnet_id}}</td>
                        <td>{{$machineData->place_ip}}</td>
                        <td>{{$machineData->place_name}}</td>
                        <td>{{$machineData->game_id}}</td>
                        <td class="numeric">{{$machineData->setting}}</td>
                        <td>{{$machineData->update_date}}</td>
                        <td>
                            <form action="/admin/manage/reference/machine/update" method="post" class="inline">
                                <input type="hidden" name="serial" value="{{$machineData->serial}}">
                                <button type="submit" class="btn"><i class="icon-edit"></i>&nbsp;編集</button>
                            </form>
                        </td>
                        <td>
                            <button type="button" class="btn" onclick="deleteConfirm('{{$machineData->serial}}', '{{$machineData->allnet_id}}', '{{$machineData->game_id}}', '{{$machineData->place_ip}}');"><i class="icon-remove"></i>&nbsp;削除</button>
                        </td>
                    </tr>
                @endforeach
                </tbody>
            </table>

            <div class="modal hide" id="confirmModal">
                <div class="modal-header">
                    <h3>削除確認</h3>
                </div>
                <div class="modal-body">
                    以下のデータを削除します。よろしいですか？
                    <table class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <th>基板シリアル</th>
                            <th>ALL.Net ID</th>
                            <th>ゲームID</th>
                            <th>店舗IP</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td><span id="delClientText"></span></td>
                            <td><span id="delPlaceIdText"></span></td>
                            <td><span id="delTitleIdText"></span></td>
                            <td><span id="delPlaceIpText"></span></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <form action="/admin/manage/reference/machine/delete" method="post">
                        <input id="delClientId" type="hidden" name="serial">
                        <button type="submit" class="btn btn-primary"><i class="icon-remove icon-white"></i>&nbsp;削除</button>
                        <button class="btn offset-button" data-dismiss="modal">キャンセル</button>
                    </form>
                </div>
            </div>
            <script>
                <!--
                function deleteConfirm(serial, placeId, gameId, placeIp) {
                    $("#delClientText").text(serial);
                    $("#delPlaceIdText").text(placeId);
                    $("#delTitleIdText").text(gameId);
                    $("#delPlaceIpText").text(placeIp);
                    $("#delClientId").val(serial);
                    $("#confirmModal").modal('show');
                }
                // -->
            </script>
        @endif
    </div>
    <!--/span-->

@stop