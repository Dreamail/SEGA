            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
            </a>
            <a class="allnet-icon" href="/admin/top">Auth Admin</a>

            <div class="btn-group pull-right">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="icon-user"></i>&nbsp;{{ Session::get('login') }}&nbsp;<span class="caret"></span>
                </a>
                <ul class="dropdown-menu">

                    <li><a href="/admin/logout">ログアウト</a></li>
                    <li><a href="/admin/user/pass/update">パスワード更新</a></li>
                </ul>
            </div>

            <div class="nav-collapse collapse">
                <ul class="nav">
                    <li><a id="displayMenuOff" class="pointer" title="隐藏菜单。"><i class="icon-chevron-left icon-white"></i></a></li>
                    <li class="active"><a id="displayMenuOn" class="pointer" title="显示菜单。"><i class="icon-chevron-right icon-white"></i></a></li>
                </ul>
            </div>
            <!--/.nav-collapse -->
