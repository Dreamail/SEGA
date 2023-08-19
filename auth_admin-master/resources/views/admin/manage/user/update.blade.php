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
                <form id="form" class="form-horizontal" action="/admin/user/update" method="post">
                    <fieldset>
                        <div class="control-group">
                            <label for="userId" class="control-label">ユーザID</label>
                            <div class="controls">
                                <input id="userId" name="userId" type="hidden" value="{{$data->user_id}}">
                                <span class="input-large uneditable-input">{{$data->user_id}}</span>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label for="password" class="control-label">パスワード&nbsp;</label>
                            <div class="controls">
                                <input id="password" name="password" type="text" value="">&nbsp;<a rel="tooltip" data-original-title="アルファベット（大小文字）、数字、_（アンダースコア）、-（ハイフォン）の16文字まで入力可能です。また未入力の場合は更新されません。"><i class="icon-info-sign"></i></a>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label for="roleId" class="control-label">ロール&nbsp;<span class="required">（必須）</span></label>
                            <div class="controls">
                                <select id="roleId" name="roleId">
                                    <option value="">ロールIDを選択してください。</option>
                                    @foreach ($roleData as $role)
                                        <option value="{{ $role->role_id }}" @if ($userRoleDate->role_id == $role->role_id) selected @endif>{{ $role->role_id}}</option>
                                    @endforeach
                                </select>

                            </div>
                        </div>
                    </fieldset>
                    <div class="form-actions">
                        <button id="updateBtn" type="button" class="btn btn-primary"><i class="icon-upload icon-white"></i>&nbsp;{{Config::get('message.UPDATE')}}</button>
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
                $("#updateBtn").click(function() {
                    $("#form").attr("action", "/admin/user/update/confirm");
                    $("#form").submit();
                });
                $("#clearBtn").click(function() {
                    $("#userId").val("");
                    $("#gameIds").val("");
                    var b = $("#btnGameAll");
                    b.attr("checked", true);
                    $("#gameSelect").addClass("hide");
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
