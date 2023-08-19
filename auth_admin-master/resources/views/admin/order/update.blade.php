@extends('admin.common.layout')

@section('content')

    <!--/span-->
    <div id="content" class="span10">
        <ul class="breadcrumb">
            <li class="notActive">
                配信指示書設定
                <span class="divider">/</span>
            </li>
            <li class="notActive">
                設定の閲覧・更新・削除
                <span class="divider">/</span>
            </li>
            <li class="active">
                設定の更新
            </li>
        </ul>
        <h2>設定の更新</h2><br>

        @if (count($errors) > 0)
            <div class="alert alert-error">
                @foreach ($errors->all() as $error)
                    <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                @endforeach
            </div>
        @endif

        <div>
            @if($registerType=="game")
                <form id="form" class="form-horizontal" action="/admin/order/update/game/confirm" method="post">
                    @foreach ($data as $game)
                        <fieldset>
                            <div class="control-group">
                                <label for="gameId" class="control-label">ゲームID&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="gameId" name="gameId" type="hidden" value="{{$game->game_id}}">
                                    <span class="input-xlarge uneditable-input">{{$game->game_id}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="gameVer" class="control-label">ゲームVer&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="gameVer" name="gameVer" type="hidden" value="{{$game->game_ver}}">
                                    <span class="input-xlarge uneditable-input">{{$game->game_ver}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="imageType" class="control-label">イメージタイプ&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="imageType" name="imageType" type="hidden" value="{{$game->type}}">
                                    <span class="input-xlarge uneditable-input">
                                        @if($game->type=="0"){{Config::get('message.TYPE_APP')}}
                                        @elseif($game->type=="1"){{Config::get('message.TYPE_OPT')}}
                                        @endif
                                    </span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="uri" class="control-label">配信指示書URI&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="uri" name="uri" class="form-large" type="text" value="{{count($errors) > 0 ? Input::old('uri') : (Input::old('uri') ? Input::old('uri') : $game->uri)}}">
                                </div>
                            </div>
                        </fieldset>
                    @endforeach
                    <div class="form-actions">
                        <button id="updateBtn" type="submit" class="btn btn-primary" name="action" value="post"><i class="icon-edit icon-white"></i>&nbsp;{{Config::get('message.UPDATE')}}</button>
                        <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>&nbsp;&nbsp;
                        <button id="returnBtn" type="submit" class="btn" name="action" value="back"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                    </div>
                </form>
            @elseif($registerType=="machine")
                <form id="form" class="form-horizontal" action="/admin/order/update/machine/confirm" method="post">
                    @foreach ($data as $machine)
                        <fieldset>
                            <div class="control-group">
                                <label for="gameId" class="control-label">ゲームID&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="gameId" name="gameId" type="hidden" value="{{$machine->title_id}}">
                                    <span class="input-xlarge uneditable-input">{{$machine->title_id}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="titleVer" class="control-label">シリアル&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="clientId" name="clientId" type="hidden" value="{{$machine->client_id}}">
                                    <span class="input-xlarge uneditable-input">{{$machine->client_id}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="imageType" class="control-label">イメージタイプ&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="imageType" name="imageType" type="hidden" value="{{$machine->type}}">
                                    <span class="input-xlarge uneditable-input">
                                        @if($machine->type=="0"){{Config::get('message.TYPE_APP')}}
                                        @elseif($machine->type=="1"){{Config::get('message.TYPE_OPT')}}
                                        @endif
                                    </span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="uri" class="control-label">配信指示書URI&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="uri" name="uri" class="form-large" type="text" value="{{count($errors) > 0 ? Input::old('uri') : (Input::old('uri') ? Input::old('uri') : $machine->uri)}}">
                                </div>
                            </div>
                        </fieldset>
                        <div class="form-actions">
                            <button id="updateBtn" type="submit" class="btn btn-primary" name="action" value="post"><i class="icon-edit icon-white"></i>&nbsp;{{Config::get('message.UPDATE')}}</button>
                            <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>&nbsp;&nbsp;
                            <button id="returnBtn" type="submit" class="btn" name="action" value="back"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                        </div>
                    @endforeach
                </form>
            @elseif($registerType=="clientGroup")
                <form id="form" class="form-horizontal" action="/admin/order/update/clientgroup/confirm" method="post">
                    @if(count($data) == 1)
                        <fieldset>
                            <div class="control-group">
                                <label for="gameId" class="control-label">ゲームID&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="gameId" name="gameId" type="hidden" value="{{$data[0]->game_id}}">
                                    <span class="input-xlarge uneditable-input">{{$data[0]->game_id}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="gameVer" class="control-label">ゲームVer&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="gameVer" name="gameVer" type="hidden" value="{{$data[0]->game_ver}}">
                                    <span class="input-xlarge uneditable-input">{{$data[0]->game_ver}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="clientGroup" class="control-label">シリアルグループ <span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="clientGroup" name="clientGroup" type="hidden" value="{{$data[0]->client_group}}">
                                    <span class="input-xlarge uneditable-input">{{$data[0]->client_group}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="imageType" class="control-label">イメージタイプ&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="imageType" name="imageType" type="hidden" value="{{$data[0]->type}}">
                                    <span class="input-xlarge uneditable-input">
                                        @if($data[0]->type=="0"){{Config::get('message.TYPE_APP')}}
                                        @elseif($data[0]->type=="1"){{Config::get('message.TYPE_OPT')}}
                                        @endif
                                    </span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="uri" class="control-label">配信指示書URI&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="uri" name="uri" class="form-large" type="text" value="{{count($errors) > 0 ? Input::old('uri') : (Input::old('uri') ? Input::old('uri') : $data[0]->uri)}}">
                                </div>
                            </div>
                        </fieldset>
                    @endif
                    <div class="form-actions">
                        <button id="updateBtn" type="submit" class="btn btn-primary" name="action" value="post"><i class="icon-edit icon-white"></i>&nbsp;{{Config::get('message.UPDATE')}}</button>
                        <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>&nbsp;&nbsp;
                        <button id="returnBtn" type="submit" class="btn" name="action" value="back"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                    </div>
                </form>
            @endif
        </div>
    </div>
    <!--/span-->
@stop
