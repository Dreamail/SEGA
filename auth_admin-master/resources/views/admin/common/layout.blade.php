<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="utf-8"/>
    <title>Auth Admin</title>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="-1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>

    <link href="/admin/resources/styles/bootstrap.css" rel="stylesheet" />

    <link href="/admin/resources/styles/bootstrap-test.css" rel="stylesheet" />

    <link href="/admin/resources/styles/bootstrap-responsive.css" rel="stylesheet" />
    <link href="/admin/resources/styles/datepicker.css" rel="stylesheet" />
    <link href="/admin/resources/styles/admin.css" rel="stylesheet" />

    <link href="/admin/resources/styles/admin-test.css" rel="stylesheet" />


    <!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="/admin/resources/js/html5.js"></script>
    <![endif]-->
    <script src="/admin/resources/js/jquery-1.7.2.min.js"></script>
</head>
<body>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <!-- ヘッダー -->
            @include('admin.common.header')
        </div>
        <!--/container-->
    </div>
</div>

<div class="container-fluid">
    <div class="row-fluid">
        <div id="menu" class="span2">
            @include('admin.common.menu')
        </div>
        <!--/span-->

        @yield('content')
        <!--/span-->
    </div>
    <!--/row-->
    <hr>
    <!-- フッター -->
    @include('admin.common.footer')

</div>
<!--/.fluid-container-->
<script src="/admin/resources/js/bootstrap.min.js"></script>
<script src="/admin/resources/js/bootstrap-datepicker.js"></script>
<script src="/admin/resources/js/jquery.cookie.js"></script>
<script src="/admin/resources/js/bootstrap-admin-ext.js"></script>
<script src="/admin/resources/js/admin.js"></script>

</body>
</html>
