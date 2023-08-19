@extends('admin.common.layout')

@section('content')

    <!--/span-->
    <div id="content" class="span10">

        @if($data['confirming'] === 'false')
        <ul class="breadcrumb">
            <li class="notActive">
                配信指示書設定
                <span class="divider">/</span>
            </li>
            <li class="notActive">
                新規登録
                <span class="divider">/</span>
            </li>
            <li class="active">
                設定の確認
            </li>
        </ul>

        <h2>設定の確認</h2><br>
        <div class="alert alert-info">
            <span class="badge badge-info">info</span>&nbsp;この情報を登録します。よろしいですか？
        </div>
            @if($data['registerType']=="game")
                @if($existsMachineDownloadOrder || $existsClientgroupDownloadOrder)
                    <div class="alert alert-error">
                        @if($existsMachineDownloadOrder)
                            <span class="badge badge-important">warn</span> 基板配信指示書が登録されています。<br>
                            　　※登録を行うと「ゲームID」「イメージタイプ」が同一の基板配信指示書が削除されます。<br>
                        @endif
                        @if($existsClientgroupDownloadOrder)
                            <span class="badge badge-important">warn</span> シリアルグループ配信指示書が登録されています。<br>
                            　　※登録を行うと「ゲームID」「ゲームVer」「イメージタイプ」が同一のシリアルグループ配信指示書が削除されます。<br>
                        @endif
                    </div>
                @endif
            @endif

        @else
            <ul class="breadcrumb">
                <li class="notActive">
                    配信指示書設定
                    <span class="divider">/</span>
                </li>
                <li class="notActive">
                    新規登録
                    <span class="divider">/</span>
                </li>
                <li class="active">
                    設定の完了
                </li>
            </ul>

            <h2>設定の完了</h2><br>
            <div class="alert alert-info">
                <span class="badge badge-info">info</span>&nbsp;この情報の登録が正常に終了しました。
            </div>
        @endif
        <div>
            @if($data['registerType']=="game")
            <ul class="nav nav-tabs">
                <li class="active"><a href="#">ゲーム</a></li>
            </ul>

            <div class="tab-content">
                <div class="tab-pane active">
                    <form id="form" class="form-horizontal" action="/admin/order/register/game" method="post">
                        <fieldset>
                            <div class="control-group">
                                <label for="gameId" class="control-label">ゲームID</label>
                                <div class="controls">
                                    <input id="gameId" name="gameId" type="hidden" value="{{$data['gameId']}}">
                                    <span class="input-xlarge uneditable-input">{{$data['gameId']}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="gameVer" class="control-label">ゲームVer</label>
                                <div class="controls">
                                    <input id="gameVer" name="gameVer" type="hidden" value="{{$data['gameVer']}}">
                                    <span class="input-xlarge uneditable-input">{{$data['gameVer']}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="imageType" class="control-label">イメージタイプ</label>
                                <div class="controls">
                                    <input id="imageType" name="imageType" class="form-large" type="hidden" value="{{$data['imageType']}}">
                                    <span class="input-xlarge uneditable-input">
                                        @if($data['imageType']=="0"){{Config::get('message.TYPE_APP')}}
                                        @elseif($data['imageType']=="1"){{Config::get('message.TYPE_OPT')}}
                                        @endif
                                    </span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="uri" class="control-label">配信指示書URI</label>
                                <div class="controls">
                                    <input id="uri" name="uri" type="hidden" value="{{$data['uri']}}">
                                    <span class="input-xlarge uneditable-input">{{$data['uri']}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <input id="registerType" name="registerType" type="hidden" value="{{$data['registerType']}}"/>
                        </fieldset>
                        @if($data['confirming'] === 'false')
                            <div class="form-actions">
                                <button id="registerBtn" type="submit" class="btn btn-primary" name="action" value="post"><i class="icon-edit icon-white"></i>&nbsp;{{Config::get('message.REGIST')}}</button>
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
            @elseif($data['registerType']=="machine")
            <ul class="nav nav-tabs">
                <li class="active"><a href="#">基板</a></li>
            </ul>

            <div class="tab-content">
                <div class="tab-pane active">
                    <form id="form" class="form-horizontal" action="/admin/order/register/machine" method="post">
                        <fieldset>
                            <div class="control-group">
                                <label for="gameId" class="control-label">ゲームID</label>
                                <div class="controls">
                                    <input id="gameId" name="gameId" type="hidden" value="{{$data['gameId']}}">
                                    <span class="input-xlarge uneditable-input">{{$data['gameId']}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="serial" class="control-label">シリアル</label>
                                <div class="controls">
                                    <input id="serial" name="serial" type="hidden" value="{{$data['serial']}}">
                                    <span class="input-xlarge uneditable-input">{{$data['serial']}}</span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="imageType" class="control-label">イメージタイプ</label>
                                <div class="controls">
                                    <input id="imageType" name="imageType" class="form-large" type="hidden" value="{{$data['imageType']}}">
                                        <span class="input-xlarge uneditable-input">
                                            @if($data['imageType']=="0"){{Config::get('message.TYPE_APP')}}
                                            @elseif($data['imageType']=="1"){{Config::get('message.TYPE_OPT')}}
                                            @endif
                                        </span>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <label for="uri" class="control-label">配信指示書URI</label>
                                    <div class="controls">
                                        <input id="uri" name="uri" type="hidden" value="{{$data['uri']}}">
                                        <span class="input-xlarge uneditable-input">{{$data['uri']}}</span>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <input id="registerType" name="registerType" type="hidden" value="{{$data['registerType']}}"/>
                            </fieldset>
                            @if($data['confirming'] === 'false')
                                <div class="form-actions">
                                    <button id="registerBtn" type="submit" class="btn btn-primary" name="action" value="post"><i class="icon-edit icon-white"></i>&nbsp;{{Config::get('message.REGIST')}}</button>
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
            @elseif($data['registerType']=="clientGroup")
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#">シリアルグループ</a></li>
                </ul>

                <div class="tab-content">
                    <div class="tab-pane active">
                        <form id="form" class="form-horizontal" action="/admin/order/register/clientgroup" method="post">
                            <fieldset>
                                <div class="control-group">
                                    <label for="titleId" class="control-label">ゲームID</label>
                                    <div class="controls">
                                        <input id="titleId" name="titleId" type="hidden" value="{{$data['titleId']}}">
                                        <span class="input-xlarge uneditable-input">{{$data['titleId']}}</span>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <label for="titleVer" class="control-label">ゲームVer</label>
                                    <div class="controls">
                                        <input id="titleVer" name="titleVer" type="hidden" value="{{$data['titleVer']}}">
                                        <span class="input-xlarge uneditable-input">{{$data['titleVer']}}</span>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <label for="clientGroup" class="control-label">シリアルグループ</label>
                                    <div class="controls">
                                        <input id="clientGroup" name="clientGroup" type="hidden" value="{{$data['clientGroup']}}">
                                        <span class="input-xlarge uneditable-input">{{$data['clientGroup']}}</span>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <label for="imageType" class="control-label">イメージタイプ</label>
                                    <div class="controls">
                                        <input id="imageType" name="imageType" class="form-large" type="hidden" value="{{$data['imageType']}}">
                                        <span class="input-xlarge uneditable-input">
                                            @if($data['imageType']=="0"){{Config::get('message.TYPE_APP')}}
                                            @elseif($data['imageType']=="1"){{Config::get('message.TYPE_OPT')}}
                                            @endif
                                        </span>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <label for="uri" class="control-label">配信指示書URI</label>
                                    <div class="controls">
                                        <input id="uri" name="uri" type="hidden" value="{{$data['uri']}}">
                                        <span class="input-xlarge uneditable-input">{{$data['uri']}}</span>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <input id="registerType" name="registerType" type="hidden" value="{{$data['registerType']}}"/>
                            </fieldset>
                            @if($data['confirming'] === 'false')
                                <div class="form-actions">
                                    <button id="registerBtn" type="submit" class="btn btn-primary" name="action" value="post"><i class="icon-edit icon-white"></i>&nbsp;{{Config::get('message.REGIST')}}</button>
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
            @endif
        </div>
    </div>
    <!--/span-->
@stop
