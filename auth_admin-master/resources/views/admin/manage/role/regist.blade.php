@extends('admin.common.layout')

@section('content')
        <!--/span-->
        <div id="content" class="span10">
            <ul class="breadcrumb">
                <li class="notActive">
                    管理者機能
                    <span class="divider">/</span>
                </li>
                <li class="notActive">
                    ロールの管理
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
                <form id="form" class="form-horizontal" action="/admin/role/regist/confirm" method="post">
                    <fieldset>
                        <div class="control-group">
                            <label for="roleId" class="control-label">ロールID&nbsp;<span class="required">（必須）</span></label>
                            <div class="controls">
                                <input id="roleId" name="roleId" type="text" value="{{old('roleId')}}" maxlength='16'>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label for="explanation" class="control-label">説明&nbsp;<span class="required">（必須）</span></label>
                            <div class="controls">
                                <input id="explanation" name="explanation" type="text" style="width: 50%" value="{{old('explanation')}}" maxlength='200'>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label for="authSetIds" class="control-label">操作権限&nbsp;<span class="required">（必須）</span></label>
                            <div class="controls">
                                <table class="table table-striped table-bordered">
                                    <thead>
                                    <tr>
                                        <th style="width:56px;">チェック</th>
                                        <th style="width:10%;">操作権限</th>
                                        <th>説明</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    @foreach ($authSets as $data)
                                        <tr>
                                            <td align="center"><input id="authSetIds_{{$loop->index}}" name="authSetIds[]" type="checkbox" value="{{$data->auth_set_id}}"></td>
                                            <td>{{$data->name}}</td>
                                            <td>{{$data->explanation}}</td>
                                        </tr>
                                    @endforeach
                                    </tbody>
                                </table>

                            </div>
                        </div>
                    </fieldset>
                    <div class="form-actions">
                        <button id="registerBtn" type="button" class="btn btn-primary"><i class="icon-upload icon-white"></i>&nbsp;{{Config::get('message.REGIST')}}</button>
                        <button type="reset" class="btn offset-button">クリア</button>&nbsp;&nbsp;
                        <button id="returnBtn" type="button" class="btn"><i class="icon-circle-arrow-left"></i>&nbsp;{{Config::get('message.RETURN')}}</button>
                    </div>
                </form>
            </div>
            <script>
                <!--
                $("#registerBtn").click(function() {
                    $("#form").attr("action", "/admin/role/regist/confirm");
                    $("#form").submit();
                });
                $("#returnBtn").click(function() {
                    $("#form").attr("action", "/admin/role/search");
                    $("#form").submit();
                });
                // -->
            </script>
        </div>
        <!--/span-->
@stop
