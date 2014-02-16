<%@ page language="java" contentType="text/html; charset=gb2312"   
    pageEncoding="utf-8"%>   
<%@taglib prefix="s" uri="/struts-tags" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"    
"http://www.w3.org/TR/html4/loose.dtd">   
<html>   
<head>   
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">   
<title>显示用户信息</title>   
</head>   
<body>   
<a href="/ens/user/toAdd">添加用户</a> 
<a href="/ens/user/query">查询用户</a>   
<hr>   
<table border="1" cellpadding="1" cellspacing="0" align="center">   
    <tr>   
        <td>编号</td>   
        <td>姓名</td>   
        <td>性别</td>   
        <td>年龄</td>   
        <td>操作</td>   
    </tr>   
<s:iterator value="list">   
    <tr>   
        <td><s:property value="id"/> </td>   
        <td><s:property value="name"/> </td>   
        <td><s:property value="sex"/> </td>   
        <td><s:property value="age"/> </td>   
        <td>   
           <a href="/ens/userToUpdate.action?user.id=<s:property value='id'/>">修改</a> 
           <a href="javascript:if (confirm('确认删除吗?')) window.location.href='/ens/userDelete.action?user.id=<s:property value='id'/>'">删除</a>   
        </td>   
    </tr>   
</s:iterator>   
</table>   
</body>   
</html>