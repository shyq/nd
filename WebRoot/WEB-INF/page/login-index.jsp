<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
     <%@ include file="/common/taglibs.jsp"%>
     <%@ include file="/common/meta.jsp"%>
    <link rel="shortcut icon" href="../../assets/ico/favicon.ico">

    <title>Dashboard Template for Bootstrap</title>

    <!-- Custom styles for this template -->
    <link href="${ctx}/css/board.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
    	<%@ include file="menu.jsp"%>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <p class="">
			2014-02-14 16:24:23,278  INFO (com.opensymphony.xwork2.interceptor.TimerInterceptor:42) - Executed action [//login!login] took 137 ms.
			2014-02-14 16:24:35,559  INFO (com.opensymphony.xwork2.interceptor.TimerInterceptor:42) - Executed action [//login!login] took 148 ms.
			2014-02-14 16:24:44,552  INFO (com.opensymphony.xwork2.interceptor.TimerInterceptor:42) - Executed action [//login!login] took 156 ms.
		 </p>
    	</div>
    	<%@ include file="bottom.jsp"%>
  </body>
</html>

