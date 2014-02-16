<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"   
 "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>修改用户信息</title>
	</head>
	<body>
		<s:form action="userUpdate" method="post">
			<s:textfield name="user.id" label="编号" />
			<s:textfield name="user.name" label="姓名" />
			<s:select list="sexs" name="user.sex" label="性别" />
			<s:textfield name="user.age" label="年龄" />
			<s:submit value="提交" />
		</s:form>
	</body>
</html>

