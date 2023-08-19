@extends('admin.common.layout')

@section('content')
        <!--/span-->
        <div id="content" class="span10">
            <ul class="breadcrumb">
                <li class="notActive">
                    データ管理
                    <span class="divider">/</span>
                </li>
                <li class="notActive">
                    CSVでの登録・更新
                    <span class="divider">/</span>
                </li>
                <li class="active">
                    基板情報
                </li>
            </ul>
            <h2>基板情報（ALL.Net Slimのみ）</h2><br>

            @if (count($errors) > 0)
                <div class="alert alert-error">
                    @foreach ($errors->all() as $error)
                        <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                    @endforeach
                </div>
            @endif

            @if (isset($result))
                @if ($result === false)
                    <div class="alert alert-error">
                        <span class="badge badge-important">error</span>&nbsp;{{Config::get('message.CN.CSV_ERROR')}}</br>
                    </div>
                @else
                    <div class="alert alert-info">
                        <span class="badge badge-info">info</span>&nbsp;{{Config::get('message.CN.CSV_SUCCESS')}}</br>
                    </div>
                @endif
            @endif

            <div class="accordion" id="uploadForm">
                <div class="accordion-group">
                    <div class="accordion-heading" title="您可以通过单击打开和关闭表单">
                <span class="accordion-toggle" data-toggle="collapse"
                      data-parent="#uploadForm" href="#uploadFormBody">
                    <i id="uploadFormIcon" class="icon-minus-sign"></i> アップロードフォーム
                </span>
                    </div>
                    <div id="uploadFormBody" class="accordion-body collapse in uploadFormBody">
                        <div class="accordion-inner">
                            <form action="/admin/manage/csv/upload/machine" method="post" enctype="multipart/form-data" class="form-horizontal">
                                <fieldset>
                                    <div class="control-group">
                                        <label class="control-label">CSVファイル</label>
                                        <div class="controls">
                                            <input type="file" name="file" id="file">

                                        </div>
                                    </div>
                                </fieldset>
                                <fieldset>
                                    <div class="control-group">
                                        <label class="control-label">ゲームID</label>
                                        <div class="controls">
                                            <select name="gameId" >
                                                <option value="">ゲームIDを選択してください。</option>
                                                @foreach ($gameData as $game)
                                                    <option value="{{ $game->game_id }} ">{{ $game->game_id }}:{{ $game->title }}</option>
                                                @endforeach
                                            </select>
                                            <label class="checkbox">
                                                <input type = "checkbox" name = "reserve" id = "reserve" checked="checked" value="true">ゲームIDを予約ゲームIDとして更新する（更新のみ）</label>
                                        </div>
                                    </div>
                                </fieldset>
                                <fieldset>
                                    <div class="controls">
                                        <p><span class="label label-info">info</span>&nbsp;<a href="/admin/manage/csv/upload/machine/explain" onclick="window.open('/admin/manage/csv/upload/machine/explain', '', 'width=700,height=400,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes'); return false;">CSVフォーマットの詳細について（別ウィンドウ）</a></p>
                                        <button type="submit" class="btn btn-primary"><i class="icon-upload icon-white"></i>&nbsp;アップロード</button>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            @if (isset($csverror) && count($csverror) > 0)
                <div class="tablebox-body">
                    <table class="table table-striped table-bordered table-condensed fixed">
                        <thead>
                        <tr>
                            <th style="width:10%">行</th>
                            <th style="width:90%">指摘内容&nbsp;
                                <a rel="tooltip" data-original-title="黒字は警告、赤字はエラーとなります。エラーがある場合、処理は行われません。"><i class="icon-info-sign"></i></a></th>
                        </tr>
                        </thead>
                        <tbody>
                        @foreach ($csverror as $key => $value)
                            <tr>
                                <td>{{$key}}</td>
                                <td>
                                    <ul class="help-block error">
                                        @foreach ($value as $errordata)
                                            <li>{{$errordata}}</li>
                                        @endforeach
                                    </ul>
                                </td>
                            </tr>
                        @endforeach
                        </tbody>
                    </table>
                </div>
            @endif
      </div>
        <!--/span-->
@stop