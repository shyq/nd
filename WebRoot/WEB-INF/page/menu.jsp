<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="navbar  navbar-default navbar-static-top navbar-fixed-top" role="navigation" style="color: maroon">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">sdasdsad</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Chain 网盘</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li>
            	<div class="btn-group" style="margin-left:20px;margin-top: 8px;">
	            	<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
					   ${sessionScope.user.name} <span class="caret"></span>
					</button>
	            	<ul class="dropdown-menu" role="menu">
					    <li><a href="#">密码修改</a></li>
					    <li><a href="#">账号安全中心</a></li>
					    <li class="divider"></li>
					    <li><a href="#">网盘消息</a></li>
					    <li class="divider"></li>
					    <li><a href="#">消息反馈</a></li>
					    <li><a href="#">帮助中心</a></li>
					  </ul>
				 </div> 
            </li>
            <li><a href="#" onclick="loginout()">退出系统</a></li>
          </ul>
          <form class="navbar-form navbar-right">
            <div class="input-group">
		      <input type="text" class="form-control" placeholder="搜索一下">
		      <span class="input-group-btn">
		        <button class="btn btn-default" type="button">搜素</button>
		      </span>
		    </div><!-- /input-group -->
          </form>
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <li><a href="${ctx}/allfile.action">全部文件</a></li>
            <li><a href="#" onclick="alert(111)">图片</a></li>
            <li><a href="#">文档</a></li>
            <li><a href="#">音乐</a></li>
            <li><a href="#">其他</a></li>
          </ul>
          <ul class="nav nav-sidebar">
            <li><a href="">分享的文件</a></li>
            <li><a href="">收藏的文件</a></li>
            <li><a href="">回收站</a></li>
          </ul>
          
           <ul class="nav nav-sidebar">
            <li><a href="${ctx}/base/user.action">用户管理</a></li>
            <li><a href="">角色管理</a></li>
            <li><a href="">资源管理</a></li>
          </ul>
        </div>
        