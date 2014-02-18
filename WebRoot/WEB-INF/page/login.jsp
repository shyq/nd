<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>登陆页面</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <%@ include file="/common/taglibs.jsp"%>
	
	<link href="${ctx}/plugin/bootstrap/css/bootstrap.css" rel="stylesheet" />
    <%--<link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet" />--%>
    <link href='http://fonts.googleapis.com/css?family=Abel|Open+Sans:400,600' rel='stylesheet'>
    <script type="text/javascript" src="${ctx}/plugin/jquery/jquery.min.1.7.js"></script>
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
	<script type="text/javascript">
		var array = ['用户名','密码','验证码'];
		array["loginName"]= ["用户名"];
		array["password"] = ["密码"];
		array["code"] = ["验证码"];



		function login(){
			var loginName = $.trim($("#loginName").val());
/*			if(loginName == "" || loginName == array["loginName"]){
				showMsg("请输入用户名","warning");
				$('#loginName').focus();
				$("#loginName").val("");
				return false;
			}
*/
			var password = $.trim($("#password").val());
/*			if(password == "" || password == array["password"]){
				showMsg("请输入密码","warning");
				$('#password').focus();
				$("#password").val("");
				return false;
			}
*/				
/*
			var code = $.trim($("#code").val());
			if(code == "" || code == array["code"] || code.length < 4){
				showMsg(code.length > 0 ?"请输入4位有效的验证码":"请输入验证码","warning");
				$('#code').focus();
				return false;
			}	
*/			
			$("#loginBtn").attr("disabled","disabled");
			$("#loginBtn").val("正在登录");
			$.post('${ctx}/login!login.action',{"loginName":$("#loginName").val(),"password":$("#password").val(),"validateCode":$("#code").val()}, function(data) {
						data = eval("("+data+")");
				if (data.code ==1){
					$("#loginBtn").removeAttr("disabled");
					/*						if($("#saveCookie").attr("checked")){
		                setCookie("fmloginName",$("#loginName").val(),24,"/");   
		                setCookie("fmpassword",$("#password").val(),24,"/");   
		            }else{
		            	deleteCookie("fmloginName","/");
		            	deleteCookie("fmpassword","/");
				    }
*/				   
					window.location = data.obj;
				}else {
					showMsg(data.msg,'error');
					refreshCheckCode();
					$("#loginBtn").removeAttr("disabled");
					$("#loginBtn").val("登    录");
				}
			});
			
		}
	</script>
</head>
<body>
	<s:debug></s:debug>
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

                <form class="margin-base-vertical">
                	<p class="input-group">
                        <span class="input-group-addon"><span class="icon-envelope"></span></span>
                        <input type="text" class="form-control input-lg" id="loginName" name="loginName" placeholder="请输入您的账号" value="admin" />
                    </p>
                    <p class="input-group">
                        <span class="input-group-addon"><span class="icon-envelope"></span></span>
                        <input type="password" class="form-control input-lg" id="password" name="password" placeholder="请输入您的账号密码" value="admin" />
                    </p>
                    <p class="help-block text-center"><small>We won't send you spam. Unsubscribe at any time.</small></p>
                    <p class="text-right">
                        <button type="button" class="btn btn-success btn-lg" id="loginBtn" onclick="login()">  登  陆  系 统</button>
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
