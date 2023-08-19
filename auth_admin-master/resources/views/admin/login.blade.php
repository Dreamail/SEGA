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
            <span class="allnet-icon">Auth Admin</span>
        </div>
        <!--/container-->
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="span6 offset3">
            @if (count($errors) > 0)
                <div class="alert alert-error">
                    @foreach ($errors->all() as $error)
                        <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                        @break
                    @endforeach
                </div>
            @endif
            <h3>ログイン</h3>
            <p>ユーザIDとパスワードを入力してログインしてください。</p>
            <div class="well">
                <form id="loginForm" action="/admin/login" method="post" class="form-horizontal form-login">
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label" for="loginId">ユーザID</label>
                            <div class="controls">
                                <input type="text" name="loginId" value="" id="loginId" />
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label" for="password">パスワード</label>
                            <div class="controls">
                                <input type="password" name="password" id="password" autocomplete="off"/>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="control-group">
                            <div class="controls">
                                <button type="submit" class="btn btn-primary">ログイン</button>&nbsp;&nbsp;&nbsp;
                                <button type="reset" class="btn">クリア</button>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
    <hr />
    <!-- フッター -->
    @include('admin.common.footer')
</div>
</body>
</html>
