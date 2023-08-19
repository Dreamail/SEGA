@extends('admin.common.layout')

@section('content')

    <!--/span-->
    <div id="content" class="span10">

        @if($confirming === 'false')
        <ul class="breadcrumb">
            <li class="notActive">
                配信指示書設定
                <span class="divider">/</span>
            </li>
            <li class="notActive">
                設定の閲覧・更新・削除
                <span class="divider">/</span>
            </li>
            <li class="notActive">
                設定の削除
                <span class="divider">/</span>
            </li>
            <li class="active">
                削除の確認
            </li>
        </ul>
        <h2>削除の確認</h2><br>
        <div class="alert alert-info">
            <span class="badge badge-info">info</span>&nbsp;この情報で削除します。よろしいですか？
        </div>
        @else
            <ul class="breadcrumb">
                <li class="notActive">
                    配信指示書設定
                    <span class="divider">/</span>
                </li>
                <li class="notActive">
                    設定の閲覧・更新・削除
                    <span class="divider">/</span>
                </li>
                <li class="notActive">
                    設定の削除
                    <span class="divider">/</span>
                </li>
                <li class="active">
                    削除完了
                </li>
            </ul>

            <h2>設定の完了</h2><br>
            <div class="alert alert-info">
                <span class="badge badge-info">info</span>&nbsp;正常に削除が完了しました。
            </div>
        @endif
        <div>
            @if($registerType=="game")
                <div>
                    <form id="form" class="form-horizontal" action="/admin/order/delete/game" method="post">
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
                        @if($confirming === 'false')
                            <div class="form-actions">
                                <button id="registerBtn" type="submit" class="btn btn-primary" name="action" value="post"><i class="icon-edit icon-white"></i>&nbsp;{{Config::get('message.DELETE')}}</button>
                                <button id="returnBtn" type="submit" class="btn offset-button" name="action" value="back"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                            </div>
                        @else
                            <div class="form-actions">
                               <button id="returnBtn" type="submit" class="btn offset-button" name="action" value="end"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                            </div>
                        @endif
                    </form>
                </div>
            @elseif($registerType=="machine")
                <div>
                    <form id="form" class="form-horizontal" action="/admin/order/delete/machine" method="post">
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
                                <label for="clientId" class="control-label">シリアル</label>
                                <div class="controls">
                                    <input id="clientId" name="clientId" type="hidden" value="{{$data['clientId']}}">
                                    <span class="input-xlarge uneditable-input">{{$data['clientId']}}</span>
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
                        @if($confirming === 'false')
                            <div class="form-actions">
                                <button id="registerBtn" type="submit" class="btn btn-primary" name="action" value="post"><i class="icon-edit icon-white"></i>&nbsp;{{Config::get('message.DELETE')}}</button>
                                <button id="returnBtn" type="submit" class="btn offset-button" name="action" value="back"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                            </div>
                        @else
                            <div class="form-actions">
                                <button id="returnBtn" type="submit" class="btn offset-button" name="action" value="end"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                            </div>
                        @endif
                    </form>
                </div>
            @elseif($registerType=="clientGroup")
                <div>
                    <form id="form" class="form-horizontal" action="/admin/order/delete/clientgroup" method="post">
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
                        @if($confirming === 'false')
                            <div class="form-actions">
                                <button id="registerBtn" type="submit" class="btn btn-primary" name="action" value="post"><i class="icon-edit icon-white"></i>&nbsp;{{Config::get('message.DELETE')}}</button>
                                <button id="returnBtn" type="submit" class="btn offset-button" name="action" value="back"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                            </div>
                        @else
                            <div class="form-actions">
                                <button id="returnBtn" type="submit" class="btn offset-button" name="action" value="end"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                            </div>
                        @endif
                    </form>
                </div>
            @endif
        </div>
    </div>
    <!--/span-->
@stop