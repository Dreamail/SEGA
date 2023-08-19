@extends('admin.common.layout')

@section('content')
    <!--/span-->
    <div id="content" class="span10">
        <ul class="breadcrumb">
            <li class="notActive">
                配信指示書設定
                <span class="divider">/</span>
            </li>
            <li class="active">
                CSVでの登録・更新
            </li>
        </ul>

        <h2>配信指示書設定&nbsp;-&nbsp;CSVでの登録・更新</h2><br>

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
                        <span class="badge badge-important">error</span>&nbsp;{{Config::get('message.CSV_ERROR')}}</br>
                </div>
            @else
                <div class="alert alert-info">
                    <span class="badge badge-info">info</span>&nbsp;{{Config::get('message.CSV_SUCCESS')}}</br>
                </div>
            @endif
        @endif

        <div class="alert alert-info">
            <span class="badge badge-info">info</span> 対象に「配信指示書」を選択した場合、登録済みの以下の配信指示書が削除されます。<br>
            ・「ゲームID」「イメージタイプ」が同一の基板配信指示書<br>
            ・「ゲームID」「ゲームVer」「イメージタイプ」が同一のシリアルグループ配信指示書<br>
        </div>

        <div class="accordion" id="uploadForm">
            <div class="accordion-group">
                <div class="accordion-heading" title="クリックすることでフォームを開閉できます">
                <span class="accordion-toggle" data-toggle="collapse"
                      data-parent="#uploadForm" href="#uploadFormBody">
                    <i id="uploadFormIcon" class="icon-minus-sign"></i>&nbsp;アップロードフォーム
                </span>
                </div>
                <div id="uploadFormBody" class="accordion-body collapse in uploadFormBody">
                    <div class="accordion-inner">
                        <form action="/admin/order/csv" method="post" enctype="multipart/form-data" class="form-horizontal">
                            <fieldset>
                                <div class="control-group">
                                    <label class="control-label">対象</label>
                                    <div class="controls">
                                        <select id="target" name="target">
                                            <option value="">対象を選択してください</option>
                                            <option value="game" @if(old('target')=="game") selected @endif>配信指示書</option>
                                            <option value="machine" @if(old('target')=="machine") selected @endif>基板配信指示書</option>
                                            <option value="clientGroup" @if(old('target')=="clientGroup") selected @endif>シリアルグループ配信指示書</option>
                                        </select>
                                    </div>
                                </div>
                            </fieldset>
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
                                    <div class="controls">
                                        <p><span class="label label-info">info</span>&nbsp;<a href="/admin/order/csv/explain" onclick="window.open('/admin/order/csv/explain', '', 'width=700,height=400,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes'); return false;">CSVフォーマットの詳細について（別ウィンドウ）</a></p>
                                        <button type="submit" class="btn btn-primary"><i class="icon-upload icon-white"></i>&nbsp;{{Config::get('message.UPLOAD')}}</button>
                                    </div>
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