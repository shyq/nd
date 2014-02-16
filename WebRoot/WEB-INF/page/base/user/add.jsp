<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>   
<%@taglib prefix="s" uri="/struts-tags" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">   
<html>   
<head>   
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">  
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%> 
<title>用户添加</title>   
</head>   
<body>   
	<form action="${ctx}/base/user!add.action">
		Username:<input name="username"/>
		Password:<input name="password"/>
		<input type="submit" value="Submit"/>
	</form>
</body>   
</html>