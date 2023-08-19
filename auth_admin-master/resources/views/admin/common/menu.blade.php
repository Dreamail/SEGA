<?php
    $var = '';

    if(Request::is('auth/log') || Request::is('billing/log*') || Request::is('log/csv/auth/export')) {
        $var = 'log';
    } elseif(Request::is('view/*')) {
        $var = 'view';
    } elseif(Request::is('order/*') || Request::is('machine_clientgroup/*')) {
        $var = 'order';
    } elseif(Request::is('manage/*')) {
        $var = 'manage';
    } elseif(Request::is('report/*')) {
        $var = 'report';
    } elseif(Request::is('user/*') || Request::is('role/*')) {
        $var = 'admin';
    }
?>

<div class="sidebar-nav accordion" id="accordion">

    @if(isset(Session::get('menu')['150_REF_LOG_ADV']))
    <div class="accordion-group">
        <div class="accordion-heading">
                <span class="accordion-toggle" data-toggle="collapse" data-parent="#accordionLogSearch" href="#accordionLogSearch">
                    <i id="accordionLogSearchIcon" class="{{ $var === 'log' ? 'icon-minus-sign' : 'icon-plus-sign' }}"></i>&nbsp;認証ログ
                </span>
        </div>
        <div id="accordionLogSearch" class="accordion-body {{ $var === 'log' ? 'in' : '' }} collapse">
            <div class="accordion-inner menu-body">
                <ul class="nav nav-list">
                    <li>
                        <li class="{{ Request::is('auth/log') || Request::is('log/csv/auth/export') ? 'active' : '' }}"><a href="/admin/auth/log">通常検索</a></li>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    @endif
        <!--TODO:権限-->
        {{--@if(isset(Session::get('menu')['100_OPE_LOG']))
            <div class="accordion-group">
                <div class="accordion-heading">
                <span class="accordion-toggle" data-toggle="collapse" data-parent="#accordionViewSearch" href="#accordionViewSearch">
                    <i id="accordionViewSearchIcon" class="{{ $var === 'view' ? 'icon-minus-sign' : 'icon-plus-sign' }}"></i>&nbsp;データ検索
                </span>
                </div>
                <div id="accordionViewSearch" class="accordion-body {{ $var === 'view' ? 'in' : '' }} collapse">
                    <div class="accordion-inner menu-body">
                        <ul class="nav nav-list">
                            <li>
                            <li class="{{ Request::is('view/game') || Request::is('view/game/*') ? 'active' : '' }}"><a href="/admin/view/game">ゲーム情報</a></li>
                            <li class="{{ Request::is('view/place') || Request::is('view/place/*') ? 'active' : '' }}"><a href="/admin/view/place">店舗情報</a></li>
                            <li class="{{ Request::is('view/router') || Request::is('view/router/*') ? 'active' : '' }}"><a href="/admin/view/router">ルータ情報</a></li>
                            <li class="{{ Request::is('view/machine') || Request::is('view/machine/*') ? 'active' : '' }}"><a href="/admin/view/machine">基板情報</a></li>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        @endif--}}
    {{--@if(isset(Session::get('menu')['200_OPE_DLO']))
        <div class="accordion-group">
            <div class="accordion-heading">
                <span class="accordion-toggle" data-toggle="collapse"
                      data-parent="#accordionDownloadorderNu" href="#accordionDownloadorderNu">
                    <i id="accordionDownloadorderNuIcon" class="{{ $var === 'order' ? 'icon-minus-sign' : 'icon-plus-sign' }}"></i>&nbsp;配信指示書設定
                </span>
            </div>

            <div id="accordionDownloadorderNu" class="accordion-body {{ $var === 'order' ? 'in' : '' }} collapse">
                <div class="accordion-inner menu-body">
                    <ul class="nav nav-list">

                        <li class="{{ Request::is('order/register') || Request::is('order/register/*') ? 'active' : '' }}"><a href="/admin/order/register">新規登録</a></li>
                        <li class="{{ Request::is('order/config') || Request::is('order/config/find')
                             || Request::is('order/update/*')  || Request::is('order/delete/*')
                             ? 'active' : '' }}"><a href="/admin/order/config">設定の閲覧・更新・削除</a></li>
                        <li class="{{ Request::is('order/csv') || Request::is('order/csv/*') ? 'active' : '' }}"><a href="/admin/order/csv">CSVでの登録</a></li>


                    </ul>
                </div>
            </div>
        </div>
    @endif--}}
    {{--@if(isset(Session::get('menu')['300_REF_STA']))
        <div class="accordion-group">
            <div class="accordion-heading">
                <span class="accordion-toggle" data-toggle="collapse" data-parent="#accordionDeliverReport" href="#accordionDeliverReport">
                    <i id="accordionDeliverReportIcon" class="{{ $var === 'report' ? 'icon-minus-sign' : 'icon-plus-sign' }}"></i>&nbsp;配信ステータス閲覧
                </span>
            </div>
            <div id="accordionDeliverReport" class="accordion-body {{ $var === 'report' ? 'in' : '' }} collapse">
                <div class="accordion-inner menu-body">
                    <ul class="nav nav-list">
                        <li class="{{ Request::is('report/*') ? 'active' : '' }}"><a href="/admin/report/search">配信ステータス閲覧</a></li>
                    </ul>
                </div>
            </div>
        </div>
    @endif--}}
    {{--@if(isset(Session::get('menu')['400_REG_DAT']) || isset(Session::get('menu')['410_TIT_OPE']) || isset(Session::get('menu')['420_PLA_OPE'])
     || isset(Session::get('menu')['430_MAC_OPE']) || isset(Session::get('menu')['435_MAC_REF']) || isset(Session::get('menu')['440_GRO_OPE']))
    <div class="accordion-group">
        <div class="accordion-heading">
            <span class="accordion-toggle" data-toggle="collapse"
                  data-parent="#accordionDataManage" href="#accordionDataManage">
                <i id="accordionDataManageIcon" class="{{ $var === 'manage' ? 'icon-minus-sign' : 'icon-plus-sign' }}"></i>&nbsp;データ管理
            </span>
        </div>
        <div id="accordionDataManage" class="accordion-body {{ $var === 'manage' ? 'in' : '' }} collapse">
            <div class="accordion-inner menu-body">
                <ul class="nav nav-list">
                    @if(isset(Session::get('menu')['410_TIT_OPE']) || isset(Session::get('menu')['420_PLA_OPE']) || isset(Session::get('menu')['430_MAC_OPE'])
                     || isset(Session::get('menu')['435_MAC_REF']) || isset(Session::get('menu')['440_GRO_OPE']))
                    <li>
                        <span style="text-decoration:underline;">参照・更新・削除</span>
                        <ul class="nav nav-list">
                            @if(isset(Session::get('menu')['430_MAC_OPE']))
                                <li class="{{ Request::is('manage/reference/machine') || Request::is('manage/reference/machine/*') ? 'active' : '' }}"><a href="/admin/manage/reference/machine">基板情報</a></li>
                            @endif
                        </ul>
                    </li>
                    @if(isset(Session::get('menu')['430_MAC_OPE']))
                    <li>
                        <span style="text-decoration:underline;">CSVでの登録・更新</span>
                        <ul class="nav nav-list">
                                <li class="{{ Request::is('manage/csv/upload/machine') ? 'active' : '' }}"><a href="/admin/manage/csv/upload/machine">基板情報</a></li>
                        </ul>
                    </li>
                    @endif
                    @if(isset(Session::get('menu')['430_MAC_OPE']) || isset(Session::get('menu')['435_MAC_REF']))
                    <li>
                        <span style="text-decoration:underline;">CSVデータのダウンロード</span>
                        <ul class="nav nav-list">
                                <li class="{{ Request::is('manage/csv/download/machine') ? 'active' : '' }}"><a href="/admin/manage/csv/download/machine">基板情報</a></li>
                        </ul>
                    </li>
                    @endif
                    @endif
                </ul>
            </div>
        </div>
    </div>
    @endif--}}
    @if(Session::get('role') == 'SUPERUSER')
    <div class="accordion-group">
        <div class="accordion-heading">
            <span class="accordion-toggle" data-toggle="collapse" data-parent="#accordionDeliverAdmin" href="#accordionDeliverAdmin">
                <i id="accordionDeliverAdminIcon" class="{{ $var === 'admin' ? 'icon-minus-sign' : 'icon-plus-sign' }}"></i>&nbsp;管理者機能
            </span>
        </div>
        <div id="accordionDeliverAdmin" class="accordion-body {{ $var === 'admin' ? 'in' : '' }} collapse">
            <div class="accordion-inner menu-body">
                <ul class="nav nav-list">
                    <li class="{{ Request::is('user/*') ? 'active' : '' }}"><a href="/admin/user/top">ユーザ管理</a></li>
                    <li class="{{ Request::is('role/*') ? 'active' : '' }}"><a href="/admin/role/top">ロールの管理</a></li>
                </ul>
            </div>
        </div>
    </div>
    @endif
</div>
