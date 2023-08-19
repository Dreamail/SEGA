<?php
function optionLoop($start, $end, $value = null){

    for($i = $start; $i <= $end; $i++){
        if(isset($value) &&  $value == $i){
            echo "<option value=\"{$i}\" selected=\"selected\">{$i}</option>";
        }else{
            echo "<option value=\"{$i}\">{$i}</option>";
        }
    }
}
?>

@extends('admin.common.layout')

@section('content')

<!--/span-->
<div id="content" class="span10">

    <ul class="breadcrumb">
        <li class="notActive">
            ログ管理
            <span class="divider">/</span>
        </li>
        <li class="active">
            認証ログ
        </li>
    </ul>

    <h2>認証ログ</h2><br>

    @if (count($errors) > 0)
        <div class="alert alert-error">
            @foreach ($errors->all() as $error)
                <span class="badge badge-important">error</span>&nbsp;{{ $error }}</br>
            @endforeach
        </div>
    @endif
    @if(isset($result) && count($result) == 0)
        <div class="alert alert-info">
            <span class="badge badge-info">info</span>&nbsp;該当するデータが見つかりませんでした。
        </div>
    @endif

    <div class="accordion" id="searchForm">
        <div class="accordion-group">
            <div class="accordion-heading" title="クリックすることでフォームを開閉できます">
                <span class="accordion-toggle" data-toggle="collapse"
                      data-parent="#searchForm" href="#searchFormBody">
                    <i id="searchFormIcon" class="icon-minus-sign"></i>&nbsp;検索フォーム
                </span>
            </div>
            <div id="searchFormBody" class="accordion-body collapse in searchFormBody">
                <div class="accordion-inner">
                    <form id="form1" class="form-horizontal" action="/admin/log/csv/auth/export" method="post">
                        <legend>
                            {{--<span class="item-name">必須項目</span>&nbsp;--}}
                            {{--<span style="font-size: 80%; color: red;">以下の項目から少なくとも1つ入力してください</span>--}}
                        </legend>
                        {{--<fieldset>
                            <div class="control-group">
                                <label for="gameId" class="control-label">ゲームID</label>
                                <div class="controls">
                                    <select id="gameId" name="gameId">
                                        <option value="">ゲームIDを選択してください。</option>
                                        @foreach ($gameData as $game)
                                            @if (isset( $gameId ) && $game->game_id === $gameId))
                                                <option value="{{ $game->game_id }} " selected >{{ $game->game_id }}:{{ $game->title }}</option>
                                            @else
                                                <option value="{{ $game->game_id }} ">{{ $game->game_id }}:{{ $game->title }}</option>
                                            @endif
                                        @endforeach
                                    </select>
                                </div>
                            </div>
                        </fieldset>--}}
                        <fieldset>
                            <div class="control-group">
                                <label for="serial" class="control-label">基板シリアル</label>
                                <div class="controls">
                                    <input id="serial" name="serial" type="text" value="{{!empty($serial) ? $serial : ''}}"/>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <div class="control-group">
                                <label for="authResult" class="control-label">認証結果</label>
                                <div class="controls">
                                    <select id="authResult" name="authResult">
                                        <option value="">未選択</option>
                                        <option value="0" @if(isset($authResult) && $authResult == '0') selected @endif>認証成功</option>
                                        <option value="4" @if(isset($authResult) && $authResult == '4') selected @endif>認証失敗</option>
                                    </select>
                                </div>
                            </div>
                        </fieldset>
                        {{--<fieldset>
                            <div class="control-group">
                                <label for="allNetId" class="control-label">ALL.Net ID</label>
                                <div class="controls">
                                    <input id="allNetId" name="allNetId" type="text" value="{{!empty($allNetId) ? $allNetId : ''}}"/>
                                </div>
                            </div>
                        </fieldset>--}}
                        <legend>
                            <label class="checkbox">
                                <input id="btnRange" name="usedRange" type="checkbox" value="true"@if(isset( $usedRange )) checked="checked"@endif><input type="hidden" name="_usedRange" value="on">
                                <span class="item-name">&nbsp;範囲指定</span>
                                <a rel="popover" data-content="受信日時で範囲を指定したい場合は範囲指定にチェックを入れ、検索範囲を選択してください。<br>また、範囲を指定しない場合は1ヶ月前までが検索範囲となります。" data-original-title="範囲指定"><i class="icon-question-sign"></i></a>
                            </label>
                        </legend>
                        <div id="range" class="hide">
                            <fieldset>
                                <div class="control-group">
                                    <label for="startDateStr" class="control-label">検索範囲（開始日）</label>
                                    <div class="controls">
                                        <div class="input-append date" id="dpStart" data-date="{{ old('startDateStr') ? old('startDateStr') : $startDateStr }}" data-date-format="yyyy-mm-dd">
                                            <input id="startDateStr" name="startDateStr" style="width:70px" readonly="readonly" type="text" value="{{ old('startDateStr') ? old('startDateStr') : $startDateStr }}"/>
                                            <span class="add-on"><i class="icon-th"></i></span>

                                            <select name="startHour" id="startHour" style="width:52px">
                                                @for ($i = 0; $i <= 23; $i++)
                                                    @if (isset( $startHour ) && $startHour== $i))
                                                    <option value="{{ $i }} " selected >{{ $i }}</option>
                                                    @else
                                                        <option value="{{ $i }}">{{ $i }}</option>
                                                    @endif
                                                @endfor
                                            </select>&nbsp;：
                                            <select name="startMinutes" id="startMinutes" style="width:52px">
                                                @for ($i = 0; $i <= 59; $i++)
                                                    @if (isset( $startMinutes ) && $startMinutes== $i))
                                                    <option value="{{ $i }} " selected >{{ $i }}</option>
                                                    @else
                                                        <option value="{{ $i }}">{{ $i }}</option>
                                                    @endif
                                                @endfor
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <label for="endDateStr" class="control-label">検索範囲（終了日）</label>
                                    <div class="controls">
                                        <div class="input-append date" id="dpEnd" data-date="{{ old('endDateStr') ? old('endDateStr') : $endDateStr }}" data-date-format="yyyy-mm-dd">
                                            <input id="endDateStr" name="endDateStr" style="width:70px" readonly="readonly" type="text" value="{{ old('endDateStr') ? old('endDateStr') : $endDateStr }}"/>
                                            <span class="add-on"><i class="icon-th"></i></span>
                                            <select name="endHour" id="endHour" style="width:52px">
                                                @for ($i = 0; $i <= 23; $i++)
                                                    @if (isset( $endHour ) && $endHour== $i))
                                                    <option value="{{ $i }} " selected >{{ $i }}</option>
                                                    @else
                                                        <option value="{{ $i }}">{{ $i }}</option>
                                                    @endif
                                                @endfor
                                            </select>&nbsp;：
                                            <select name="endMinutes" id="endMinutes" style="width:52px">
                                                @for ($i = 0; $i <= 59; $i++)
                                                    @if (isset( $endMinutes ) && $endMinutes== $i))
                                                    <option value="{{ $i }} " selected >{{ $i }}</option>
                                                    @else
                                                        <option value="{{ $i }}">{{ $i }}</option>
                                                    @endif
                                                @endfor
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                        </div>
                        {{--<legend>
                            <label class="checkbox">
                                <input id="btnSort" name="usedSort" type="checkbox" value="true"@if(isset( $usedSort )) checked="checked"@endif><input type="hidden" name="_usedSort" value="on">
                                <span class="item-name">&nbsp;ソート設定</span>
                                <a rel="popover" data-content="ソート順序を指定したい場合はソート設定にチェックを入れ、項目を選択した状態で上下ボタンを押してソートの順序を設定してください<br>また、項目を選択した状態で昇順/降順ボタンを押すことでソートの設定を変更することができます" data-original-title="ソート設定"><i class="icon-question-sign"></i></a>
                            </label>
                        </legend>--}}
                        {{--<div id="sort" class="hide">
                            <fieldset>
                                <div class="control-group">
                                    <label for="sortSequenceList" class="control-label">ソート順序</label>
                                    <div class="controls">
                                        <select id="sortSequenceList" name="sortSequenceList[]" multiple="multiple" size="5">
                                            <option value="TIME:DESC">アクセス時刻（降順）</option>
                                            <option value="SERIAL">シリアル</option>
                                        </select>
                                        <input type="hidden" name="_sortSequenceList" value="1"/><br>
                                    </div>
                                </div>
                                <div class="control-group"><div class="controls">
                                        <button id="btnMoveUp" type="button" class="btn">▲</button>
                                        <button id="btnMoveDown" type="button" class="btn">▼</button>
                                        <button id="btnToggleOrder" type="button" class="btn">昇順/降順</button>
                                    </div></div>
                            </fieldset>
                        </div>--}}
                        {{--<legend>
                            <label class="checkbox">
                                <input id="btnOption" name="usedOption" type="checkbox" value="true"@if(isset( $usedOption )) checked="checked"@endif><input type="hidden" name="_usedOption" value="on">
                                <span class="item-name">&nbsp;オプション</span>
                                <a rel="popover" data-content="その他のフィルタリングを行いたい場合はオプションにチェックを入れて、各項目に値を入力してください" data-original-title="オプション"><i class="icon-question-sign"></i></a>
                            </label>
                        </legend>--}}
                        {{--<div id="option" class="hide">
                            <fieldset>
                                <div class="control-group">
                                    <label for="stat" class="control-label">認証状況</label>
                                    <div class="controls">
                                        <select id="stat" name="stat">
                                            <option value="">条件に含まない</option>
                                            <option value="{{Config::get('const.STAT')['OK']}}" @if($stat==Config::get('const.STAT')['OK']) selected @endif>{{Config::get('message.STAT_OK')}}</option>
                                            <option value="{{Config::get('const.STAT')['NG_ALL']}}" @if($stat==Config::get('const.STAT')['NG_ALL']) selected @endif>{{Config::get('message.STAT_NG_ALL')}}</option>
                                            <option value="{{Config::get('const.STAT')['NG_GAME']}}" @if($stat==Config::get('const.STAT')['NG_GAME']) selected @endif>{{Config::get('message.STAT_NG_GAME')}}</option>
                                            <option value="{{Config::get('const.STAT')['NG_SERIAL']}}" @if($stat==Config::get('const.STAT')['NG_SERIAL']) selected @endif>{{Config::get('message.STAT_NG_SERIAL')}}</option>
                                            <option value="{{Config::get('const.STAT')['NG_LOCATION']}}" @if($stat==Config::get('const.STAT')['NG_LOCATION']) selected @endif>{{Config::get('message.STAT_NG_LOCATION')}}</option>
                                        </select>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <label for="countryCode" class="control-label">国コード</label>
                                    <div class="controls">
                                        <select id="countryCode" name="countryCode">
                                            <option value="">条件に含まない</option>
                                            @foreach ($countryData as $country)
                                                @if (isset( $countryCode ) && $country->country_code === $countryCode))
                                                    <option value="{{ $country->country_code }}" selected >{{ $country->country_code }}</option>
                                                @else
                                                    <option value="{{ $country->country_code }}">{{ $country->country_code }}</option>
                                                @endif
                                            @endforeach
                                        </select>
                                    </div>
                                </div>
                            </fieldset>
                            </fieldset>
                        </div>--}}

                        <fieldset>
                            <div class="control-group">
                                <div class="controls">
                                    <button type="submit" class="btn btn-primary" name="action" value="search"><i class="icon-search icon-white"></i>&nbsp;{{Config::get('message.SEARCH')}}</button>&nbsp;&nbsp;
                                    <button type="submit" name="action" value="CsvDownload" class="btn"><i class="icon-download"></i>&nbsp;{{Config::get('message.CSV_DL')}}</button>
                                    <button type="button" class="btn offset-button" id="btnClear">クリア</button>&nbsp;&nbsp;&nbsp;
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
    @if(isset($result) && count($result) > 0)

        <table class="table table-striped table-bordered fixed">
            <thead>
            <tr>
                <th style="width:100px">アクセス時刻</th>
                <th style="width:80px">認証結果</th>
                <th style="width:100px">シリアル</th>
                <th style="width:200px">原因</th>
                <th style="width:620px">レスポンス</th>
            </tr>
            </thead>
            <tbody>
            @foreach ($result as $data)
                <tr>
                    <td style="word-wrap:break-word;">@if(isset($data->create_date)){{date('Y-m-d H:i:s', strtotime($data->create_date))}}@endif</td>
                    <td style="word-wrap:break-word;">@if($data->auth_result == '0') 認証成功 @else 認証失敗 @endif</td>
                    <td style="word-wrap:break-word;">@if($data->keychip_id == '00000000000')  @else {{$data->keychip_id}} @endif</td>
                    <td style="word-wrap:break-word;">{{$data->cause}}</td>
                    <td style="word-wrap:break-word;">@if($data->auth_result == '0') {{$data->response_2}} @endif</td>
                </tr>
            　@endforeach
            </tbody>
        </table>

        <div class="d-flex justify-content-center">
            {{ $result->appends(request()->input())->links()}}
            {{ $result->currentPage() }} / {{ $result->lastPage() }}
        </div>
    @endif
</div>

<script>
    <!--
    $(document).ready(function() {
        $("#dpStart").datepicker({
            format: "yyyy/mm/dd"
        }).on('changeDate', function(ev){
            $("#dpStart").datepicker("hide");
        });
    });
    $(document).ready(function() {
        $("#dpEnd").datepicker({
            format: "yyyy/mm/dd"
        }).on('changeDate', function(ev){
            $("#dpEnd").datepicker("hide");
        });
    });
    $(document).ready(function() {
        if($("#btnRange").prop("checked") && $("#range").hasClass("hide")){
            $("#range").removeClass("hide");
        }
    });
    $("#btnRange").click(function() {
        var c = $("#range");
        if(c.hasClass("hide")){
            c.removeClass("hide");
        }else{
            c.addClass("hide");
        }
    });
    $(document).ready(function() {
        if($("#btnSort").prop("checked") && $("#sort").hasClass("hide")){
            $("#sort").removeClass("hide");
        }
    });
    $("#btnSort").click(function() {
        var c = $("#sort");
        if(c.hasClass("hide")){
            c.removeClass("hide");
        }else{
            c.addClass("hide");
        }
    });
    $(document).ready(function() {
        if($("#btnOption").prop("checked") && $("#option").hasClass("hide")){
            $("#option").removeClass("hide");
        }
    });
    $("#btnOption").click(function() {
        var c = $("#option");
        if(c.hasClass("hide")){
            c.removeClass("hide");
        }else{
            c.addClass("hide");
        }
    });
    $("#btnClear").click(function() {
        $("#gameId").val("");
        $("#serial").val("");
        $("#allNetId").val("");
        $("#authResult").val("");
        $("#btnRange").removeAttr("checked");
        $("#range").addClass("hide");
        $("#startDateStr").val("{{ old('startDateStr') ? old('startDateStr') : $startDateStr }}");
        $("#startHour").val("0");
        $("#startMinutes").val("0");
        $("#endDateStr").val("{{ old('endDateStr') ? old('endDateStr') : $endDateStr }}");
        $("#endHour").val("0");
        $("#endMinutes").val("0");
        $("#btnSort").removeAttr("checked");
        $("#sort").addClass("hide");
        $("#btnOption").removeAttr("checked");
        $("#option").addClass("hide");
    });
    $("#btnMoveUp,#btnMoveDown").click(function() {
        var $opt = $("#sortSequenceList option:selected:first");
        if (!$opt.length) {
            return;
        }
        if (this.id === "btnMoveUp") {
            $opt.prev().before($opt);
        } else {
            $opt.next().after($opt);
        }
    });
    $("#btnToggleOrder").click(function() {
        var $opt = $("#sortSequenceList option:selected:first");
        if (!$opt.length) {
            return;
        }
        if($opt.val().match(/:DESC/) != null) {
            $opt.val($opt.val().replace(/:DESC/, ""));
            $opt.text($opt.text().replace(/（降順）/, ""));
        } else {
            $opt.val($opt.val() + ":DESC");
            $opt.text($opt.text() + "（降順）");
        }
    });
    // -->
</script>

@stop
