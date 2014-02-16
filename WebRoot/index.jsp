<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>登陆页面</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <%@ include file="/common/taglibs.jsp"%>
	
	<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet" />
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet" />
    <link href='http://fonts.googleapis.com/css?family=Abel|Open+Sans:400,600' rel='stylesheet'>
    <style>
        /* http://css-tricks.com/perfect-full-page-background-image/ */
        html {
            background: url(img/bg.jpg) no-repeat center center fixed; 
            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            background-size: cover;
        }

        body {
            padding-top: 20px;
            font-size: 16px;
            font-family: "Open Sans",serif;
            background: transparent;
        }

        h1 {
            font-family: "Abel", Arial, sans-serif;
            font-weight: 400;
            font-size: 40px;
        }

        /* Override B3 .panel adding a subtly transparent background */
        .panel {
            background-color: rgba(255, 255, 255, 0.9);
        }
        .margin-base-vertical {
            margin: 40px 0;
        }

    </style>

</head>
<body>

    <div class="container">
        <div class="row">
            <div class="col-md-6 col-md-offset-3 panel panel-default">

                <h1 class="margin-base-vertical">Have you ever seen the rain?</h1>

                <p>
                    Someone told me long ago there's a calm before the storm. I know, It's been comin for some time.
                </p>
                <p>
                    When it's over, so they say, it'll rain a sunny day. I know,    Shinin down like water.
                </p>

                <form class="margin-base-vertical" action="${ctx}/login!login.action" method="post">
                	<p class="input-group">
                        <span class="input-group-addon"><span class="icon-envelope"></span></span>
                        <input type="text" class="form-control input-lg" name="username" placeholder="请输入您的账号" />
                    </p>
                    <p class="input-group">
                        <span class="input-group-addon"><span class="icon-envelope"></span></span>
                        <input type="text" class="form-control input-lg" name="password" placeholder="请输入您的账号密码" />
                    </p>
                    <p class="help-block text-center"><small>We won't send you spam. Unsubscribe at any time.</small></p>
                    <p class="text-right">
                        <button type="submit" class="btn btn-success btn-lg">  登  陆  系 统</button>
                        <button type="button" class="btn btn-info btn-lg" onclick="close();">  关 闭</button>
                    </p>
                </form>

                <div class="margin-base-vertical">
                    <small class="text-muted"><a href="">找回密码</a>. 如果您已经找不回您的密码了请 <a href="">联系管理员</a>.</small>
                </div>

            </div><!-- //main content -->
        </div><!-- //row -->
    </div> <!-- //container -->

</body>
</html>
