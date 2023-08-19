<%@ page pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%response.addHeader("Pragma","no-cache");%>
<%response.addHeader("Cache-Control","no-cache");%>
<%response.addHeader("Expires","-1");%>

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

<link href="<c:url value="/resources/styles/bootstrap.css"/>" rel="stylesheet" />
<c:if test="${!production}">
<link href="<c:url value="/resources/styles/bootstrap-test.css"/>" rel="stylesheet" />
</c:if>
<link href="<c:url value="/resources/styles/bootstrap-responsive.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/styles/datepicker.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/styles/admin.css"/>" rel="stylesheet" />
<c:if test="${!production}">
<link href="<c:url value="/resources/styles/admin-test.css"/>" rel="stylesheet" />
</c:if>

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<script src="<c:url value="/resources/js/html5.js"/>"></script>
<![endif]-->
<script src="<c:url value="/resources/js/jquery-1.7.2.min.js"/>"></script>

<!-- Le fav and touch icons -->
<!--
<link rel="shortcut icon" href="../assets/ico/favicon.ico">
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="../assets/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="../assets/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="../assets/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed" href="../assets/ico/apple-touch-icon-57-precomposed.png">
-->
</head>

<body>

    <div class="navbar navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid">
                <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <a class="allnet-icon" href="<c:url value="/"/>">ALL.Net Admin</a>
            </div>
            <!--/container-->
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="span6 offset3">
                <div class="alert alert-error">
                    <span class="badge badge-important">error</span>&nbsp;エラーが発生しました。
                </div>
                <div class="well">
                    <p>予期しないエラーが発生しました。</p>
                </div>
            </div>
        </div>
    </div>
    <!--/container-->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
    <script src="<c:url value="/resources/js/bootstrap-datepicker.js"/>"></script>
    <script src="<c:url value="/resources/js/jquery.cookie.js"/>"></script>
    <script src="<c:url value="/resources/js/bootstrap-admin-ext.js"/>"></script>
    <script src="<c:url value="/resources/js/admin.js"/>"></script>

</body>
</html>
