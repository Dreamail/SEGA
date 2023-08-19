@extends('admin.common.layout')

@section('content')

    <!--/span-->
    <div id="content" class="span10">
        <ul class="breadcrumb">
            <li class="notActive">
                データ検索
                <span class="divider">/</span>
            </li>
            <li class="active">
                ゲーム情報
            </li>
        </ul>

        <h2>ゲーム情報</h2><br>

        @if (count($errors) > 0)
            <div class="alert alert-error">
                @foreach ($errors->all() as $error)
                    <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                @endforeach
            </div>
        @endif

        <div class="accordion" id="searchForm">
            <div class="accordion-group">
                <div class="accordion-heading" title="クリックすることでフォームを開閉できます">
                    <span class="accordion-toggle" data-toggle="collapse"
                          data-parent="#searchForm" href="#searchFormBody">
                        <i id="searchFormIcon" class="icon-minus-sign"></i>&nbsp;検索フォーム
                    </span>
                </div>
                <div id="searchFormBody" class="accordion-body collapse in searchFormBody" style="background-color: #f5f5f5">
                    <div class="accordion-inner">
                        <form id="viewConfigOrderNuForm" class="form-horizontal" action="/admin/view/game/search" method="get">
                            <fieldset>
                                <div class="control-group">
                                    <label for="gameId" class="control-label">ゲームID</label>
                                    <div class="controls">
                                        <select id="gameId" name="gameId" class="focused">
                                            <option value="">ゲームIDを選択してください</option>
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
                                    <div class="controls">
                                        <button type="submit" class="btn btn-primary"><i class="icon-search icon-white"></i>&nbsp;{{Config::get('message.SEARCH')}}</button>
                                        <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>
                                    </div>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        @if(isset($data))
            <div class="accordion" id="downloadOrderTable"><!--TODO：基本情報を表示-->
                <div class="accordion-group">
                    <div class="accordion-heading" title="クリックすることでフォームを開閉できます">
                        <span class="accordion-toggle" data-toggle="collapse" data-parent="#downloadOrderTable" href="#downloadOrderTableBody">
                            <i id="downloadOrderTableIcon" class="icon-minus-sign"></i>&nbsp;基本情報
                        </span>
                    </div>
                    <div id="downloadOrderTableBody" class="accordion-body searchFormBody in collapse" style="height: auto;">
                        <div class="accordion-inner">
                                <table class="table table-striped table-bordered">
                                    <thead>
                                        <tr>
                                            <th style="width: 65px">ゲームID</th>
                                            <th style="width: 85px">パスワード</th>
                                            <th style="width: 70px">基本名称</th>
                                            <th style="width: 70px">登録日</th>
                                            <th style="width: 70px">更新日</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {{--@foreach ($data['downData'] as $game)
                                            <tr>
                                                <td>{{$game->game_id}}</td>
                                                <td>{{$game->game_ver}}</td>
                                                @if($game->type=="0")
                                                    <td><a class="label label-info" rel="tooltip" data-original-title="{{Config::get('message.TYPE_APP')}}"><i class="icon-file icon-white"></i>APP</a></td>
                                                @elseif($game->type=="1")
                                                    <td><a class="label label-info" rel="tooltip" data-original-title="{{Config::get('message.TYPE_OPT')}}"><i class="icon-file icon-white"></i>OPT</a></td>
                                                @endif
                                                <td>{{$game->uri}}</td>
                                                <td>{{$game->create_date}}</td>
                                                <td>{{$game->update_date}}</td>
                                                <td>
                                                    <form action="/admin/order/update/game" method="get" class="inline">
                                                        <input type="hidden" name="gameId" value="{{$game->game_id}}">
                                                        <input type="hidden" name="gameVer" value="{{$game->game_ver}}">
                                                        <input type="hidden" name="imageType" value="{{$game->type}}">
                                                        <button type="submit" class="btn"><i class="icon-edit"></i>&nbsp;{{Config::get('message.EDIT')}}</button>
                                                    </form>
                                                </td>
                                                <td>
                                                    <form action="/admin/order/delete/game/confirm" method="get" class="inline">
                                                        <input type="hidden" name="gameId" value="{{$game->game_id}}">
                                                        <input type="hidden" name="gameVer" value="{{$game->game_ver}}">
                                                        <input type="hidden" name="imageType" value="{{$game->type}}">
                                                        <button id="deleteGame_SBZW_1.00_0" type="submit" class="btn" value="0"><i class="icon-remove"></i>&nbsp;{{Config::get('message.DELETE')}}</button>
                                                    </form>
                                                </td>
                                            </tr>
                                        @endforeach--}}
                                    </tbody>
                                </table>
                        </div>
                    </div>
                    <script>
                        <!--
                        $('#downloadOrderTableBody').on('show', function () {
                            $("#downloadOrderTableIcon").removeClass("icon-plus-sign");
                            $("#downloadOrderTableIcon").addClass("icon-minus-sign");
                        });
                        $('#downloadOrderTableBody').on('hide', function () {
                            $("#downloadOrderTableIcon").removeClass("icon-minus-sign");
                            $("#downloadOrderTableIcon").addClass("icon-plus-sign");
                        });
                        // -->
                    </script>
                </div>
            </div>
            <div class="accordion" id="machineDownloadOrderTable"><!--TODO:検索結果を表示-->
                <div class="accordion-group">
                    <div class="accordion-heading" title="クリックすることでフォームを開閉できます">
                        <span class="accordion-toggle" data-toggle="collapse" data-parent="#machineDownloadOrderTable" href="#machineDownloadOrderTableBody">
                            <i id="machineDownloadOrderTableIcon" class="icon-minus-sign"></i>&nbsp;基板配信指示書
                        </span>
                    </div>
                    <div id="machineDownloadOrderTableBody" class="accordion-body collapse in searchFormBody">
                        <div class="accordion-inner">
                                <table  class="table table-striped table-bordered">
                                    <thead>
                                        <tr>
                                            <th style="width: 65px">ゲームVer</th>
                                            <th style="width: 85px">国コード</th>
                                            <th style="width: 70px">タイトル名</th>
                                            <th style="width: 70px">タイトルURI</th>
                                            <th style="width: 70px">タイトルホスト</th>
                                            <th style="width: 70px">認証方式</th>
                                            <th style="width: 70px">登録日</th>
                                            <th style="width: 70px">更新日</th>
                                        </tr>
                                    </thead>
                                    <tbody>{{--
                                        @foreach ($data['machineDownData'] as $machine)
                                            <tr>
                                                <td>{{$machine->client_id}}</td>
                                                <td>{{$machine->game_id}}</td>
                                                @if($machine->type=="0")
                                                    <td><a class="label label-info" rel="tooltip" data-original-title="{{Config::get('message.TYPE_APP')}}"><i class="icon-file icon-white"></i>APP</a></td>
                                                @elseif($machine->type=="1")
                                                    <td><a class="label label-info" rel="tooltip" data-original-title="{{Config::get('message.TYPE_OPT')}}"><i class="icon-file icon-white"></i>OPT</a></td>
                                                @endif
                                                <td>{{$machine->uri}}</td>
                                                <td>{{$machine->create_date}}</td>
                                                <td>{{$machine->update_date}}</td>
                                                    <td>
                                                        <form action="/admin/order/update/machine" method="get" class="inline">
                                                            <input type="hidden" name="gameId" value="{{$machine->game_id}}" />
                                                            <input type="hidden" name="clientId" value="{{$machine->client_id}}" />
                                                            <input type="hidden" name="imageType" value="{{$machine->type}}" />
                                                            <button type="submit" class="btn"><i class="icon-edit"></i>&nbsp;{{Config::get('message.EDIT')}}</button>
                                                        </form>
                                                    </td>
                                                    <td>
                                                        <form action="/admin/order/delete/machine/confirm" method="get" class="inline">
                                                            <input type="hidden" name="clientId" value="{{$machine->client_id}}" />
                                                            <input type="hidden" name="imageType" value="{{$machine->type}}" />
                                                            <button id="deleteMachine_" type="submit" class="btn" value=""><i class="icon-remove"></i>&nbsp;{{Config::get('message.DELETE')}}</button>
                                                        </form>
                                                    </td>
                                            </tr>
                                        @endforeach--}}
                                    </tbody>
                                </table>
                        </div>
                    </div>
                    <script>
                        <!--
                        $('#machineDownloadOrderTableBody').on('show', function () {
                            $("#machineDownloadOrderTableIcon").removeClass("icon-plus-sign");
                            $("#machineDownloadOrderTableIcon").addClass("icon-minus-sign");
                        });
                        $('#machineDownloadOrderTableBody').on('hide', function () {
                            $("#machineDownloadOrderTableIcon").removeClass("icon-minus-sign");
                            $("#machineDownloadOrderTableIcon").addClass("icon-plus-sign");
                        });
                        // -->
                    </script>
                </div>
            </div>
        @endif
    </div>
    <!--/span-->

@stop
