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
                    ロールの管理
                </li>
            </ul>
            <h2>ロールの管理</h2><br>
            @if (count($errors) > 0)
                <div class="alert alert-error">
                    @foreach ($errors->all() as $error)
                        <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                    @endforeach
                </div>
            @else
                @if(!isset($roleData) || count($roleData) == 0)
                    <div class="alert alert-info">
                        <span class="badge badge-info">info</span>&nbsp;該当するデータが見つかりませんでした。
                    </div>
                @endif
            @endif
            <div>
                <form action="/admin/role/regist" method="get">
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
                            <form id="viewRoleForm" class="form-horizontal" action="/admin/role/search" method="post">
                                <fieldset>
                                    <div class="control-group">
                                        <label for="searchRoleId" class="control-label">ロールID</label>
                                        <div class="controls">
                                            <input id="searchRoleId" name="searchRoleId" type="text" value="{{!empty($searchRoleId) ? $searchRoleId : ''}}"/>
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
                                <input id="offset" name="offset" value="0" type="hidden" value="0"/>
                                <input id="fixedHideSearchForm" name="hideSearchForm" value="false" type="hidden" value="true"/>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            @if(isset($roleData) && count($roleData) > 0)
            <table class="table table-striped table-bordered">
                <thead>
                <tr>
                    <th>ロールID</th>
                    <th>説明</th>
                    <th>操作権限</th>
                    <th>更新日</th>
                    <th style="width: 80px">編集</th>
{{--                    <th style="width: 80px">削除</th>--}}
                </tr>
                </thead>
                <tbody>
                    @foreach ($roleData as $data)
                    <tr>
                        <td>{{$data->role_id}}</td>
                        <td>{{$data->explanation}}</td>
                        <td>
                            @foreach ($data->auth_sets as $authData)
                                {{$authData->name}}<br>
                            @endforeach
                        </td>
                        <td>{{$data->update_date}}</td>
                        <td>
                            <form action="/admin/role/update" method="get" class="inline">
                                <input type="hidden" name="roleId" value="{{$data->role_id}}" />
                                <button type="submit" class="btn"><i class="icon-edit"></i>&nbsp;編集</button>
                            </form>
                        </td>
{{--                        <td>--}}
{{--                            <button id="delete_{{$data->role_id}}" type="button" class="btn" value="{{$data->role_id}}"><i class="icon-remove"></i>&nbsp;削除</button>--}}
{{--                        </td>--}}
                     </tr>
                    @endforeach
                </tbody>
            </table>
            @endif
            <div class="modal hide" id="deleteConfirmModal">
                <div class="modal-header">
                    <h3>削除確認</h3>
                </div>
                <div class="modal-body">
                    以下のデータを削除します。よろしいですか？
                    <table class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <th>ロールID</th>
                            <th>説明</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td><span id="delRoleId" ></span></td>
                            <td><span id="delRoleExp" ></span></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <form action="/admin/role/delete" method="post">
                        <input id="droleId" type="hidden" name="roleId"/>
                        <button type="submit" class="btn btn-primary"><i class="icon-remove icon-white"></i>&nbsp;削除</button>
                        <button class="btn offset-button" data-dismiss="modal">キャンセル</button>
                    </form>
                </div>
            </div>

            <script type="text/javascript" src="/admin/resources/js/views/admin/role/viewRole.js"></script>
        </div>
        <!--/span-->
@stop
