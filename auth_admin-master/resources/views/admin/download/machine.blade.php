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
                    CSVデータのダウンロード
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

            <div class="accordion" id="uploadForm">
                <div class="accordion-group">
                    <div class="accordion-heading" title="您可以通过单击打开和关闭表单">
                <span class="accordion-toggle" data-toggle="collapse" data-parent="#uploadForm" href="#uploadFormBody">
                    <i id="uploadFormIcon" class="icon-minus-sign"></i>&nbsp;ダウンロードフォーム
                </span>
                    </div>
                    <div id="uploadFormBody" class="accordion-body collapse in uploadFormBody" style="background-color: #f5f5f5">
                        <div class="accordion-inner">
                            <form id="downloadMachineForm" class="form-horizontal" action="/admin/download/export/machine" method="post">
                                <fieldset>
                                    <div class="control-group">
                                        <label for="gameId" class="control-label">ゲームID</label>
                                        <div class="controls">
                                            <select id="titleId" name="gameId" class="focused">
                                                <option value="">ゲームIDを選択してください</option>
                                                @foreach ($gameData as $game)
                                                    <option value="{{ $game->game_id }} ">{{ $game->game_id }}:{{ $game->title }}</option>
                                                @endforeach
                                            </select>
                                        </div>
                                    </div>
                                </fieldset>
                                <fieldset>
                                    <div class="control-group">
                                        <label for="type" class="control-label">検索タイプ</label>
                                        <div class="controls">
                                            <label class="radio"><input id="type1" name="type" type="radio" value="onlyAuth"@if(!old('type') || old('type') == 'onlyAuth') checked="checked"@endif/>&nbsp;認証されたボードのみ</label>
                                            <label class="radio"><input id="type2" name="type" type="radio" value="byTime"@if(old('type') == 'byTime') checked="checked"@endif/>&nbsp;
                                                <input id="day" name="day" style="width:20px" type="text" value="{{old('day') ? old('day') : '30'}}"/>日 以上認証に成功していないボードのみ</label>
                                            <label class="radio"><input id="type3" name="type" type="radio" value="onlyNotAuth"@if(old('type') == 'onlyNotAuth') checked="checked"@endif/>&nbsp;認証されていないボードのみ</label>
                                        </div>
                                    </div>
                                </fieldset>
                                <fieldset>
                                    <div class="control-group">
                                        <div class="controls">
                                            <p><span class="label label-info">info</span>&nbsp;<a href="/admin/manage/csv/download/machine/explain" onclick="window.open('/admin/manage/csv/download/machine/explain', '', 'width=700,height=400,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes'); return false;">CSVフォーマットの詳細について（別ウィンドウ）</a></p>
                                            <button type="submit" class="btn btn-primary"><i class="icon-download icon-white"></i>&nbsp;ダウンロード</button>
                                            <button id="clearBtn" type="button" class="btn offset-button">クリア</button>
                                        </div>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                <!--
                $("#clearBtn").click(function() {
                    $("#titleId").val("");
                    $("#type1").attr("checked", "checked");
                    $("#day").val("30");
                });
                // -->
            </script>


        </div>
        <!--/span-->
@stop