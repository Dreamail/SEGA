@extends('admin.common.layout')

@section('content')
        <!--/span-->
        <div id="content" class="span10">
            @if($data['confirming'] === 'false')
            <ul class="breadcrumb">
                <li class="notActive">
                    管理者機能
                    <span class="divider">/</span>
                </li>
                <li class="notActive">
                    ユーザの管理
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
            @else
            <ul class="breadcrumb">
                <li class="notActive">
                    管理者機能
                    <span class="divider">/</span>
                </li>
                <li class="notActive">
                    ユーザの管理
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
                <span class="badge badge-success">success</span>&nbsp;この情報の登録が正常に終了しました。
            </div>
            @endif
            <div>
                <form id="form" class="form-horizontal" action="/admin/user/regist/process" method="post">
                    <fieldset>
                        <div class="control-group">
                            <label for="name" class="control-label">ユーザID</label>
                            <div class="controls">
                                <input id="name" name="userId" type="hidden" value="{{$data['userId']}}">
                                <span class="input-xlarge uneditable-input">{{$data['userId']}}</span>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label for="explanation" class="control-label">パスワード</label>
                            <div class="controls">
                                <input id="password" name="password" type="hidden" value="{{$data['password']}}">
                                <span class="input-xlarge uneditable-input">{{$data['password']}}</span>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label for="roleId" class="control-label">ロール</label>
                            <div class="controls">
                                <input id="roleId" name="roleId" type="hidden" value="{{$data['roleData'][0]->role_id}}">
                                <span class="input-xlarge uneditable-input">{{$data['roleData'][0]->role_id}}</span>
                            </div>
                        </div>
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
        <!--/span-->
@stop
