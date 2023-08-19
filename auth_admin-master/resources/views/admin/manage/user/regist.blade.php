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
                    ユーザの管理
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
                <form id="form" class="form-horizontal" action="/admin/user/regist/confirm" method="post">
                    <fieldset>
                        <div class="control-group">
                            <label for="userId" class="control-label">ユーザID&nbsp;<span class="required">（必須）</span></label>
                            <div class="controls">
                                <input id="userId" name="userId" type="text" value="{{old('userId')}}" maxlength='16'>&nbsp;<a rel="tooltip" data-original-title="アルファベット（大小文字）、数字、_（アンダースコア）、-（ハイフォン）が16文字まで入力可能です。"><i class="icon-info-sign"></i></a>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label for="password" class="control-label">パスワード&nbsp;<span class="required">（必須）</span></label>
                            <div class="controls">
                                <input id="password" name="password" type="text" value="{{old('password')}}" maxlength='16'>&nbsp;<a rel="tooltip" data-original-title="アルファベット（大小文字）、数字、_（アンダースコア）、-（ハイフォン）が16文字まで入力可能です。"><i class="icon-info-sign"></i></a>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label for="roleId" class="control-label">ロール&nbsp;<span class="required">（必須）</span></label>
                            <div class="controls">
                                <select id="roleId" name="roleId">
                                    <option value="">ロールを選択してください</option>
                                    @foreach ($roleData as $role)
                                    <option value="{{ $role->role_id }}" @if(old('roleId') == $role->role_id) selected @endif>{{ $role->role_id }}</option>
                                    @endforeach
                                </select>

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
                $("#btnGameAll").click(function() {
                    var c = $("#gameSelect");
                    c.addClass("hide");
                });
                $("#btnGameSelect").click(function() {
                    var c = $("#gameSelect");
                    c.removeClass("hide");
                });
                $("#registerBtn").click(function() {
                    $("#form").attr("action", "/admin/user/regist/confirm");
                    $("#form").submit();
                });
                $("#returnBtn").click(function() {
                    $("#form").attr("action", "/admin/user/search");
                    $("#form").submit();
                });
                // -->
            </script>
        </div>
        <!--/span-->
@stop
