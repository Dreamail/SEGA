@extends('admin.common.layout')

@section('content')

    <!--/span-->
    <div id="content" class="span10">
        <ul class="breadcrumb">
            <li class="notActive">
                データ検索
                <span class="divider">/</span>
            </li>
            <li class="active">
                ルータ情報
            </li>
        </ul>

        <h2>ルータ情報</h2><br>

        @if (count($errors) > 0)
            <div class="alert alert-error">
                @foreach ($errors->all() as $error)
                    <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                @endforeach
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
                <div id="searchFormBody" class="accordion-body collapse in searchFormBody" style="background-color: #f5f5f5">
                    <div class="accordion-inner">
                        <form id="viewConfigOrderNuForm" class="form-horizontal" action="/admin/view/router/search" method="get">
                            <fieldset>
                                <div class="control-group">
                                    <label for="searchType" class="control-label">{{Config::get('message.SEARCH_TYPE')}}</label>
                                    <div class="controls">
                                        <select id="searchType" name="searchType">
                                            <option value="{{Config::get('const.VIEW_SEARCH_TYPE')['PLACE']}}" @if($searchType==Config::get('const.VIEW_SEARCH_TYPE')['PLACE']) selected @endif>{{Config::get('message.TYPE_PLACE')}}</option>
                                            <option value="{{Config::get('const.VIEW_SEARCH_TYPE')['ROUTER']}}" @if($searchType==Config::get('const.VIEW_SEARCH_TYPE')['ROUTER']) selected @endif>{{Config::get('message.TYPE_ROUTER')}}</option>
                                            <option value="{{Config::get('const.VIEW_SEARCH_TYPE')['ALLNETID']}}" @if($searchType==Config::get('const.VIEW_SEARCH_TYPE')['ALLNETID']) selected @endif>{{Config::get('message.TYPE_ALLNETID')}}</option>
                                            <option value="{{Config::get('const.VIEW_SEARCH_TYPE')['PLACEID']}}" @if($searchType==Config::get('const.VIEW_SEARCH_TYPE')['PLACEID']) selected @endif>{{Config::get('message.TYPE_PLACEID')}}</option>
                                            <option value="{{Config::get('const.VIEW_SEARCH_TYPE')['IPADDRESS']}}" @if($searchType==Config::get('const.VIEW_SEARCH_TYPE')['IPADDRESS']) selected @endif>{{Config::get('message.TYPE_IPADDRESS')}}</option>
                                        </select>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <label for="keyword" class="control-label">キーワード</label>
                                    <div class="controls">
                                        <input id="keyword" name="keyword" type="text" value="@if(isset($keyword)){{$keyword}}@endif"/>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset>
                                <div class="control-group">
                                    <div class="controls">
                                        <button type="submit" class="btn btn-primary"><i class="icon-search icon-white"></i>&nbsp;{{Config::get('message.SEARCH')}}</button>
                                        <button type="reset" class="btn offset-button">{{Config::get('message.CLEAR')}}</button>
                                    </div>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        @if(isset($result))
            <div class="accordion" id="machineDownloadOrderTable"><!--TODO:検索結果を表示-->
                <div class="accordion-group">
                    <div id="machineDownloadOrderTableBody" class="accordion-body collapse in searchFormBody">
                        <div class="accordion-inner">
                            <table  class="table table-striped table-bordered">
                                <thead>
                                <tr>
                                    <th style="width: 65px">ルータID</th>
                                    <th style="width: 85px">ALL.NetID</th>
                                    <th style="width: 70px">店舗名</th>
                                    <th style="width: 70px">ルータ種別</th>
                                    <th style="width: 70px">ルータ種別名</th>
                                    <th style="width: 70px">帯域種別</th>
                                    <th style="width: 70px">帯域種別名</th>
                                    <th style="width: 70px">IPアドレス</th>
                                </tr>
                                </thead>
                                <tbody>{{--
                                        @foreach ($data['machineDownData'] as $machine)
                                            <tr>
                                                <td>{{$machine->client_id}}</td>
                                                <td>{{$machine->game_id}}</td>
                                                @if($machine->type=="0")
                                                    <td><a class="label label-info" rel="tooltip" data-original-title="{{Config::get('message.TYPE_APP')}}"><i class="icon-file icon-white"></i>APP</a></td>
                                                @elseif($machine->type=="1")
                                                    <td><a class="label label-info" rel="tooltip" data-original-title="{{Config::get('message.TYPE_OPT')}}"><i class="icon-file icon-white"></i>OPT</a></td>
                                                @endif
                                                <td>{{$machine->uri}}</td>
                                                <td>{{$machine->create_date}}</td>
                                                <td>{{$machine->update_date}}</td>
                                                    <td>
                                                        <form action="/admin/order/update/machine" method="get" class="inline">
                                                            <input type="hidden" name="gameId" value="{{$machine->game_id}}" />
                                                            <input type="hidden" name="clientId" value="{{$machine->client_id}}" />
                                                            <input type="hidden" name="imageType" value="{{$machine->type}}" />
                                                            <button type="submit" class="btn"><i class="icon-edit"></i>&nbsp;{{Config::get('message.EDIT')}}</button>
                                                        </form>
                                                    </td>
                                                    <td>
                                                        <form action="/admin/order/delete/machine/confirm" method="get" class="inline">
                                                            <input type="hidden" name="clientId" value="{{$machine->client_id}}" />
                                                            <input type="hidden" name="imageType" value="{{$machine->type}}" />
                                                            <button id="deleteMachine_" type="submit" class="btn" value=""><i class="icon-remove"></i>&nbsp;{{Config::get('message.DELETE')}}</button>
                                                        </form>
                                                    </td>
                                            </tr>
                                        @endforeach--}}
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <script>
                        <!--
                        $('#machineDownloadOrderTableBody').on('show', function () {
                            $("#machineDownloadOrderTableIcon").removeClass("icon-plus-sign");
                            $("#machineDownloadOrderTableIcon").addClass("icon-minus-sign");
                        });
                        $('#machineDownloadOrderTableBody').on('hide', function () {
                            $("#machineDownloadOrderTableIcon").removeClass("icon-minus-sign");
                            $("#machineDownloadOrderTableIcon").addClass("icon-plus-sign");
                        });
                        // -->
                    </script>
                </div>
            </div>
        @endif
    </div>
    <!--/span-->

@stop
