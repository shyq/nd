<%@ page language="java" contentType="text/html; charset=utf-8"   
    pageEncoding="utf-8"%>   
<%@taglib prefix="s" uri="/struts-tags" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"   
 "http://www.w3.org/TR/html4/loose.dtd">   
<html>   
<head>   
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">   
<title>查询用户信息</title>   
</head>   
<body>   
<s:form action="userQuery" method="post">   
    <s:textfield name="uqm.id" label="编号" value="0"/>   
    <s:textfield name="uqm.name" label="姓名"/>   
    <s:select list="sexs" name="uqm.sex" label="性别"    
	headerKey="" headerValue="请选择"/>   
    <s:textfield name="uqm.age" label="年龄最小值" value="0"/>   
    <s:textfield name="uqm.maxAge" label="年龄最大值" value="0"/>   
    <s:submit value="查询"/>   
</s:form>   
</body>   
</html> 