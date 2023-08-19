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
                    ロールの管理
                    <span class="divider">/</span>
                </li>
                <li class="notActive">
                    設定の更新
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
                    管理者機能
                    <span class="divider">/</span>
                </li>
                <li class="notActive">
                    ロールの管理
                    <span class="divider">/</span>
                </li>
                <li class="notActive">
                    設定の更新
                    <span class="divider">/</span>
                </li>
                <li class="active">
                    設定の完了
                </li>
            </ul>
            <h2>設定の完了</h2><br>

            <div class="alert alert-info">
                <span class="badge badge-success">success</span>&nbsp;この情報の更新が正常に終了しました。
            </div>
            @endif
            <div>
                <form id="form" class="form-horizontal" action="/admin/role/update/process" method="post">
                    <fieldset>
                        <div class="control-group">
                            <label for="roleId" class="control-label">ロールID</label>
                            <div class="controls">
                                <input id="roleId" name="roleId" type="hidden" value="{{$data['roleId']}}">
                                <span class="input-xlarge uneditable-input">{{$data['roleId']}}</span>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label for="explanation" class="control-label">説明</label>
                            <div class="controls">
                                <input id="explanation" name="explanation" type="hidden" value="{{$data['explanation']}}">
                                <span class="input-xlarge uneditable-input" style="width: 50%" >{{$data['explanation']}}</span>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label for="authSetIds" class="control-label">操作権限</label>
                            <div class="controls">
                                <table class="table table-striped table-bordered">
                                    <thead>
                                    <tr>
                                        <th style="width:10%;">操作権限</th>
                                        <th>説明</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    @foreach ($authSets as $row)
                                        <tr>
                                            <input id="{{$row->auth_set_id}}" name="authSetIds[]" type="hidden" value="{{$row->auth_set_id}}">
                                            <td>{{$row->name}}</td>
                                            <td>{{$row->explanation}}</td>
                                        </tr>
                                    @endforeach
                                    </tbody>
                                </table>
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
