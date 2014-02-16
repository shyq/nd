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
            <li><a href="#">Dashboard</a></li>
            <li><a href="#">Settings</a></li>
            <li><a href="#">Profile</a></li>
            <li><a href="#">Help</a></li>
          </ul>
          <form class="navbar-form navbar-right">
            <input type="text" class="form-control" placeholder="Search...">
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
        </div>
        