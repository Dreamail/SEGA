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
                <li class="notActive">
                    基板情報
                    <span class="divider">/</span>
                </li>
                <li class="active">
                    更新設定
                </li>
            </ul>

            <h2>更新設定</h2><br>

            @if (count($errors) > 0)
                <div class="alert alert-error">
                    @foreach ($errors->all() as $error)
                        <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                    @endforeach
                </div>
            @endif
            <form id="form" class="form-horizontal" action="/admin/manage/reference/machine/update/confirm" method="post">
                <fieldset>
                    <div class="control-group">
                        <label for="serial" class="control-label">基板シリアル</label>
                        <div class="controls">
                            <input id="clientId" name="serial" type="hidden" value="{{$data->serial}}">
                            <span class="input-large uneditable-input">{{$data->serial}}</span>
                        </div>
                    </div>
                </fieldset>
                <fieldset>
                    <div class="control-group">
                        <label for="allnetId" class="control-label">ALL.Net ID&nbsp;<span class="required">（必須）</span></label>
                        <div class="controls">
                            <select id="placeId" name="allNetId" class="focused">
                                <option value="">ALL.NetIDを選択してください。</option>
                                @foreach ($placeData as $place)
                                    @if (isset( $data ) && $place->allnet_id === $data->allnet_id))
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
                        <label for="gameId" class="control-label">ゲームID&nbsp;<span class="required">（必須）</span></label>
                        <div class="controls">
                            <select id="titleId" name="gameId" class="focused">
                                <option value="">ゲームIDを選択してください。</option>
                                @foreach ($gameData as $game)
                                    @if (isset( $data ) && $game->game_id === $data->game_id))
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
                        <label for="setting" class="control-label">通信設定&nbsp;<span class="required">（必須）</span></label>
                        <div class="controls">
                            <select id="setting" name="setting">
                                <option value="">通信設定を選択してください。</option>
                                <option value="1" @if ($data->setting == 1) selected @endif>1:通常</option>
                                <option value="2" @if ($data->setting == 2) selected @endif>2:利用停止</option>
                                <option value="3" @if ($data->setting == 3) selected @endif>3:強制停止</option>
                            </select>
                        </div>
                    </div>
                </fieldset>
                <fieldset>
                    <div class="control-group">
                        <label for="placeIp" class="control-label">店舗IP&nbsp;<span class="required">（必填）</span></label>
                        <div class="controls">
                            <input id="placeIp" name="placeIp" type="text" value="{{$data->place_ip}}">
                        </div>
                    </div>
                </fieldset>
                <div class="form-actions">
                    <button id="updateBtn" type="button" class="btn btn-primary"><i class="icon-edit icon-white"></i>&nbsp;更新</button>
                    <button type="reset" class="btn offset-button">クリア</button>&nbsp;&nbsp;
                    <button id="returnBtn" type="button" class="btn"><i class="icon-circle-arrow-left"></i>&nbsp;戻る</button>
                </div>
            </form>

            <script>
                <!--
                $("#updateBtn").click(function() {
                    $("#form").attr("action", "/admin/manage/reference/machine/update/confirm");
                    $("#form").submit();
                });
                $("#returnBtn").click(function() {
                    $("#form").attr("action", "/admin/manage/reference/machine/find");
                    $("#form").submit();
                });
                // -->
            </script>
        </div>
        <!--/span-->
@stop