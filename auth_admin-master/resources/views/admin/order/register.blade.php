@extends('admin.common.layout')

@section('content')

    <!--/span-->
    <div id="content" class="span10">
        <ul class="breadcrumb">
            <li class="notActive">
                配信指示書設定
                <span class="divider">/</span>
            </li>
            <li class="active">
                新規登録
            </li>
        </ul>
        <h2>新規登録</h2><br>

        @if (count($errors) > 0)
            <div class="alert alert-error">
                @foreach ($errors->all() as $error)
                    <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                @endforeach
            </div>
        @endif

        <div>
            <ul class="nav nav-tabs" id="registerOrderFormTab">
                <li @if(old('registerType')=="game" || old('registerType')=="") class="active" @endif >
                    <a href="#registerFormGameTab" title="タブをクリックすると検索フォームが切り替わります">ゲーム</a>
                </li>
                <li @if(old('registerType')=="machine") class="active" @endif>
                    <a href="#registerFormMachineTab" title="タブをクリックすると検索フォームが切り替わります">基板</a>
                </li>
                <li @if(old('registerType')=="clientGroup") class="active" @endif>
                    <a href="#registerFormClientGroupTab" title="タブをクリックすると検索フォームが切り替わります">シリアルグループ</a>
                </li>
            </ul>

            <div class="tab-content">
                <div @if(old('registerType')=="game" || old('registerType')=="") class="tab-pane active" @else class="tab-pane" @endif id="registerFormGameTab">
                    <form id="registerOrderNuGameForm" class="form-horizontal" action="/admin/order/register/game/confirm" method="post">
                        <fieldset>
                            <div class="control-group">
                                <label for="gameId" class="control-label">ゲームID&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <select id="gameId" name="gameId">
                                        <option value="">ゲームIDを選択してください</option>
                                        @foreach ($gameData as $game)
                                            @if ($game->game_id === old('gameId'))
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
                                <label for="gameVer" class="control-label">ゲームVer&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="gameVer" name="gameVer" type="text" value="{{old('gameVer')}}" />
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                            <label for="imageType" class="control-label">イメージタイプ&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                <select id="imageType" name="imageType">
                                    <option value="">イメージタイプを選択してください</option>
                                    <option value="0" @if(old('imageType')=="0") selected @endif>{{Config::get('message.TYPE_APP')}}</option>
                                    <option value="1" @if(old('imageType')=="1") selected @endif>{{Config::get('message.TYPE_OPT')}}</option>
                                </select>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="uri" class="control-label">配信指示書URI&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="uri" name="uri" class="form-large" type="text" value="{{old('uri')}}" />
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <input id="registerType" name="registerType" value="game" type="hidden" />
                        </fieldset>
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary"><i class="icon-upload icon-white"></i>&nbsp;{{Config::get('message.REGIST')}}</button>
                            <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>
                        </div>
                    </form>
                </div>
                <div @if(old('registerType')=="machine") class="tab-pane active" @else class="tab-pane" @endif id="registerFormMachineTab">
                    <form id="registerOrderNuMachineForm" class="form-horizontal" action="/admin/order/register/machine/confirm" method="post">
                        <fieldset>
                            <div class="control-group">
                                <label for="gameId" class="control-label">ゲームID&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <select id="gameId" name="gameId">
                                        <option value="">ゲームIDを選択してください</option>
                                        @foreach ($gameData as $game)
                                            @if ($game->game_id === old('gameId'))
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
                                <label for="serial" class="control-label">シリアル&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="serial" name="serial" type="text" value="{{old('serial')}}" />
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="imageType" class="control-label">イメージタイプ&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <select id="imageType" name="imageType">
                                        <option value="">イメージタイプを選択してください</option>
                                        <option value="0" @if(old('imageType')=="0") selected @endif>{{Config::get('message.TYPE_APP')}}</option>
                                        <option value="1" @if(old('imageType')=="1") selected @endif>{{Config::get('message.TYPE_OPT')}}</option>
                                    </select>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="uri" class="control-label">配信指示書URI&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="uri" name="uri" class="form-large" type="text" value="{{old('uri')}}" />
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <input id="registerType" name="registerType" value="machine" type="hidden" />
                        </fieldset>
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary"><i class="icon-upload icon-white"></i>&nbsp;{{Config::get('message.REGIST')}}</button>
                            <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>
                        </div>
                    </form>
                </div>
                <div @if(old('registerType')=="clientGroup") class="tab-pane active" @else class="tab-pane" @endif id="registerFormClientGroupTab">
                    <form id="registerOrderNuClientGroupForm" class="form-horizontal" action="/admin/order/register/clientgroup/confirm" method="post">
                        <fieldset>
                            <div class="control-group">
                                <label for="gameId" class="control-label">ゲームID&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <select id="gameId" name="gameId">
                                        <option value="">ゲームIDを選択してください</option>
                                        @foreach ($gameData as $game)
                                            @if ($game->game_id === old('gameId'))
                                                <option value="{{ $game->game_id }}" selected >{{ $game->game_id }}:{{ $game->title }}</option>
                                            @else
                                                <option value="{{ $game->game_id }}">{{ $game->game_id }}:{{ $game->title }}</option>
                                            @endif
                                        @endforeach
                                    </select>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="titleVer" class="control-label">ゲームVer&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="titleVer" name="titleVer" type="text" value="{{old('titleVer')}}" />
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="clientGroup" class="control-label">シリアルグループ&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <select id="clientGroup" name="clientGroup">
                                        <option value="">シリアルグループを選択してください</option>
                                        @foreach ($clientGroups as $clientGroup)
                                            <option value="{{ $clientGroup->client_group }}"<?php echo (($clientGroup->client_group === old('clientGroup')) ? ' selected="selected"' : ""); ?> class="ti{{$clientGroup->title_id}}">
                                                {{ $clientGroup->client_group }}
                                            </option>
                                        @endforeach
                                    </select>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="imageType" class="control-label">イメージタイプ&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <select id="imageType" name="imageType">
                                        <option value="">イメージタイプを選択してください</option>
                                        <option value="0" @if(old('imageType')=="0") selected @endif>{{Config::get('message.TYPE_APP')}}</option>
                                        <option value="1" @if(old('imageType')=="1") selected @endif>{{Config::get('message.TYPE_OPT')}}</option>
                                    </select>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="uri" class="control-label">配信指示書URI&nbsp;<span class="required">（必須）</span></label>
                                <div class="controls">
                                    <input id="uri" name="uri" class="form-large" type="text" value="{{old('uri')}}" />
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <input id="registerType" name="registerType" value="clientGroup" type="hidden" />
                        </fieldset>
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary"><i class="icon-upload icon-white"></i>&nbsp;{{Config::get('message.REGIST')}}</button>
                            <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!--/span-->
    <script>
        $(function(){
            drillDown($("#registerFormClientGroupTab #gameId").val());
        });
        $("#registerFormClientGroupTab #gameId").change(function(){
            $("#clientGroup option").attr("selected",false);
            drillDown($(this).val());
        });
        function drillDown(gameId){
            if(gameId){
                $("#clientGroup > option").each(function(i,elem){
                    if(!$(elem).attr("class")||$(elem).hasClass("ti"+gameId.trim())){
                        $(elem).css("display","block");
                    }else{
                        $(elem).css("display","none");
                    }
                });
            }else{
                $("#clientGroup > option").css("display","block");
            }
        }
    </script>

@stop
