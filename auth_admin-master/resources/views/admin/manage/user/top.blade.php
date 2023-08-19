@extends('admin.common.layout')

@section('content')
        <!--/span-->
        <div id="content" class="span10">
            <ul class="breadcrumb">
                <li class="notActive">
                    管理者機能
                    <span class="divider">/</span>
                </li>
                <li class="active">
                    ユーザの管理
                </li>
            </ul>
            <h2>ユーザの管理</h2><br>

            @if (count($errors) > 0)
                <div class="alert alert-error">
                    @foreach ($errors->all() as $error)
                        <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                    @endforeach
                </div>
            @else
                @if(!isset($userdata) || count($userdata) == 0)
                    <div class="alert alert-info">
                        <span class="badge badge-info">info</span>&nbsp;該当するデータが見つかりませんでした。
                    </div>
                @endif
            @endif

            <div>
                <form action="/admin/user/regist" method="get">
                    <button type="submit" class="btn"><i class="icon-upload"></i>&nbsp;新規登録</button>
                </form>
            </div>
            <div class="accordion" id="searchForm">
                <div class="accordion-group">
                    <div class="accordion-heading" title="クリックすることでフォームを開閉できます">
                <span class="accordion-toggle" data-toggle="collapse"
                      data-parent="#searchForm" href="#searchFormBody">
                    <i id="searchFormIcon" class="{{ !isset($search) || $search == '1' ? 'icon-minus-sign' : 'icon-plus-sign' }}"></i>&nbsp;検索フォーム
                </span>
                    </div>
                    <div id="searchFormBody" class="accordion-body collapse {{ !isset($search) || $search == '1' ? 'in' : '' }} searchFormBody">
                        <div class="accordion-inner">
                            <form id="viewUserForm" class="form-horizontal" action="/admin/user/search" method="post">
                                <fieldset>
                                    <div class="control-group">
                                        <label for="searchUserId" class="control-label">ユーザID</label>
                                        <div class="controls">
                                            <input id="searchUserId" name="searchUserId" type="text" value="{{!empty($searchUserId) ? $searchUserId : ''}}"/>
                                        </div>
                                    </div>
                                </fieldset>
                                <fieldset>
                                    <div class="control-group">
                                        <div class="controls">
                                            <label class="checkbox">
                                                <input id="onlyFreeze1" name="onlyFreeze" type="checkbox" {{!empty($onlyFreeze) ? 'checked="checked"' : ''}} value="true"/>&nbsp;凍結されたユーザのみ表示
                                            </label>
                                        </div>
                                    </div>
                                </fieldset>
                                <fieldset>
                                    <div class="control-group">
                                        <div class="controls">
                                            <button type="submit" class="btn btn-primary"><i class="icon-search icon-white"></i>&nbsp;検索</button>
                                            <button type="reset" class="btn offset-button">クリア</button>
                                        </div>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="pagination">
            </div>
            @if(isset($userdata) && count($userdata) > 0)
            <table class="table table-striped table-bordered">
                <thead>
                <tr>
                    <th>ユーザID</th>
                    <th>ロールID</th>
                    <th>状態</th>
                    <th>更新日</th>
                    <th style="width: 100px">編集</th>
                    <th style="width: 100px">凍結</th>
                    <th style="width: 100px">削除</th>
                </tr>
                </thead>
                <tbody>
                    @foreach ($userdata as $data)
                        <tr>
                            <form action="/admin/user/update" method="get" class="inline">
                                <input type="hidden" name="userId" value="{{$data->user_id}}" />
                                <td>{{$data->user_id}}</td>
                                <td>
                                    {{$data->role_id}}
                                </td>
                                <td>
                                    @if($data->invalided == 1)
                                        凍結
                                    @endif
                                </td>
                                <td>
                                    {{$data->update_date}}
                                </td>
                                <td>
                                    @if($data->role_id != "SUPERUSER")
                                    <button type="submit" class="btn"><i class="icon-edit"></i>&nbsp;編集</button>
                                    @endif
                                </td>
                                <td>
                                    @if($data->role_id != "SUPERUSER")
                                        @if($data->invalided == 1)
                                            <button id="freeze_{{$data->user_id}}" type="button" class="btn" value="0">
                                                <i class="icon-ban-circle"></i>&nbsp;凍結解除
                                            </button>
                                        @else
                                            <button id="freeze_{{$data->user_id}}" type="button" class="btn" value="1">
                                                <i class="icon-ban-circle"></i>&nbsp;凍結
                                            </button>
                                        @endif
                                    @endif
                                </td>
                                <td>
                                    @if($data->role_id != "SUPERUSER")
                                    <button id="delete_{{$data->user_id}}" type="button" class="btn" value="{{$data->user_id}}"><i class="icon-remove"></i>&nbsp;削除</button>
                                    @endif
                                </td>
                            </form>
                        </tr>
                    @endforeach
                @endif
                </tbody>
            </table>
                <div class="modal hide" id="freezeConfirmModal">
                    <div class="modal-header">
                        <h3>
                    <span id="modalFreezeTitle">
                        凍結確認
                    </span>
                    <span id="modalReleaseTitle">
                        凍結解除確認
                    </span>
                        </h3>
                    </div>
                    <div class="modal-body">
                <span id="modalFreezeMessage">
                    以下のデータを凍結します。よろしいですか？
                </span>
                <span id="modalReleaseMessage">
                    以下のデータを凍結解除します。よろしいですか？
                </span>
                        <table class="table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th>ユーザID</th>
                                <th>ロールID</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><span id="freUserIdText"></span></td>
                                <td><span id="freRoleNameText"></span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <form action="/admin/user/freeze" method="post">
                            <input id="freUserId" type="hidden" name="userId">
                            <input id="freInvalided" type="hidden" name="invalided">
                            <input type="hidden" name="searchUserId" value="">
                            <button id="modalFreezeSubmit" type="submit" class="btn btn-primary">
                                <i class="icon-ban-circle icon-white"></i>&nbsp;凍結
                            </button>
                            <button id="modalReleaseSubmit" type="submit" class="btn btn-primary">
                                <i class="icon-ok-circle icon-white"></i>&nbsp;凍結解除
                            </button>
                            <button class="btn offset-button" data-dismiss="modal">キャンセル</button>
                        </form>
                    </div>
                </div>

                <div class="modal hide" id="deleteConfirmModal">
                    <div class="modal-header">
                        <h3>削除確認</h3>
                    </div>
                    <div class="modal-body">
                        以下のデータを削除します。よろしいですか？
                        <table class="table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th>ユーザID</th>
                                <th>ロールID</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><span id="delUserIdText"></span></td>
                                <td><span id="delRoleNameText"></span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <form action="/admin/user/delete" method="post">
                            <input id="delUserId" type="hidden" name="userId">
                            <input type="hidden" name="searchUserId" value="">
                            <button type="submit" class="btn btn-primary"><i class="icon-remove icon-white"></i>&nbsp;削除</button>
                            <button class="btn offset-button" data-dismiss="modal">キャンセル</button>
                        </form>
                    </div>
                </div>
                <script type="text/javascript" src="/admin/resources/js/views/admin/user/viewUser.js"></script>
        </div>
        <!--/span-->
@stop
