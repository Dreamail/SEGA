@extends('admin.common.layout')

@section('content')
        <!--/span-->
        <div id="content" class="span10">
            @if($data['confirming'] === 'false')
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
                <li class="notActive">
                    更新設定
                    <span class="divider">/</span>
                </li>
                <li class="active">
                    設定の確認
                </li>
            </ul>
            <h2>設定の確認</h2><br>

            <div class="alert alert-info">
                <span class="badge badge-info">info</span>&nbsp;この情報を更新します。よろしいですか？
            </div>
            @else
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
                <li class="notActive">
                    更新设定
                    <span class="divider">/</span>
                </li>
                <li class="active">
                    設定の完了
                </li>
            </ul>
            <h2>設定の完了</h2><br>

            <div class="alert alert-info">
                <span class="badge badge-success">success</span>&nbsp;この情報を更新が正常に終了しました。
            </div>
            @endif
            <div>
                <form id="form" class="form-horizontal" action="/admin/manage/reference/machine/update/process" method="post">
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label">基板シリアル</label>
                            <div class="controls">
                                <input id="clientId" name="serial" type="hidden" value="{{$data['serial']}}">
                                <span class="input-xlarge uneditable-input">{{$data['serial']}}</span>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label">ALL.NetID</label>
                            <div class="controls">
                                <input id="placeId" name="allNetId" type="hidden" value="{{$placeData->allnet_id}}">
                                <span class="input-xlarge uneditable-input">{{$placeData->allnet_id}}({{$placeData->name}})</span>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label">ゲームID</label>
                            <div class="controls">
                                <input id="titleId" name="gameId" type="hidden" value="{{$gameData->game_id}}">
                                <span class="input-xlarge uneditable-input">{{$gameData->game_id}}:{{$gameData->title}}</span>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label">通信設定</label>
                            <div class="controls">
                                <input id="setting" name="setting" type="hidden" value="{{$data['setting']}}">
                                <span class="input-xlarge uneditable-input">
                                    @if ($data['setting'] == 1) 1:通常
                                    @elseif ($data['setting'] == 2) 2:利用停止
                                    @elseif ($data['setting'] == 3) 3:強制停止
                                    @endif
                                </span>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label">店舗IP</label>
                            <div class="controls">
                                <input id="placeId" name="placeIp" type="hidden" value="{{$data['placeIp']}}">
                                <span class="input-xlarge uneditable-input">{{$data['placeIp']}}</span>
                            </div>
                        </div>
                    </fieldset>
                    @if($data['confirming'] === 'false')
                        <div class="form-actions">
                            <button id="registerBtn" type="submit" class="btn btn-primary" name="action" value="post"><i class="icon-edit icon-white"></i>&nbsp;{{Config::get('message.UPDATE')}}</button>
                            <button id="returnBtn" type="submit" class="btn offset-button" name="action" value="back"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                        </div>
                    @else
                        <div class="form-actions">
                            <button id="returnBtn" type="submit" class="btn offset-button" name="action" value="end"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                        </div>
                    @endif
                </form>
            </div>
        </div>
        <!--/span-->
@stop