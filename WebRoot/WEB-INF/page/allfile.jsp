<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
     <%@ include file="/common/taglibs.jsp"%>
     <%@ include file="/common/meta.jsp"%>

    <title>Chain 网盘</title>
    <link href="${ctx}/css/board.css" rel="stylesheet">
    <script type="text/javascript">
    $(function(){
		$.fn.nd({
			pageTag:"all",
			queryUrl:"${ctx}/doc/doc!getAllDocs.action"
		});
	})
    
    </script>
  </head>

  <body>
   	<%@ include file="menu.jsp"%>
       <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
         <h5 class="page-header">全部文件</h5>
         <div class="row">
         	<a class="btn btn-primary btn-lg" role="button"  id="cmdNewFolder">新建文件夹</a>
         	<a class="btn btn-primary btn-lg" role="button">Learn more</a>
         	<a class="btn btn-primary btn-lg" role="button">Learn more</a>
         </div>
         <h6 class="sub-header" id="cmd-header"></h6>
         <div class="bs-docs-grid">
	         <div class="row show-grid-board" >
				  <div class="col-xs-12 col-md-6">
				  	<input id="check_all" class="css-checkbox" type="checkbox" />
					<label for="check_all" class="css-label"> </label>
					<label> 文件名  </label>
				  </div>	
				  <div class="col-md-2"><span >大小</span></div>
				  <div class="col-md-2">上传日期</div>
				  <div class="col-md-2">操作</div>
			</div>
			<div>
				<ul class="list-docs ksknns" id="data_list_inner">
					<li class="row li-header" >
						<div class="col-xs-12 col-md-7">
					  		<input type="checkbox"  id="demo_box_2" class="css-checkbox"/>
					  		<label for="demo_box_2" name="demo_lbl_1" class="css-label"></label>
					  		<label name="demo_lbl_1" >asdasds</label>
					  	</div>
					    <div class="col-md-2"><span>4551KB</span></div>
					    <div class="col-md-3"><span>2014-05-02</span></div>
					</li>
					<li class="row li-header" >
						<div class="col-xs-12 col-md-7">
					  		<input type="checkbox" class="css-checkbox"/>
					  		<span>新建文件夹</span>
					  	</div>
					    <div class="col-md-2">4551KB</div>
					    <div class="col-md-3">2014-05-02</div>
					</li>
					<li class="row li-header" >
						<div class="col-xs-12 col-md-7">
					  		<input type="checkbox" class="css-checkbox"/>
					  		<span>新建文件夹</span>
					  	</div>
					    <div class="col-md-2">4551KB</div>
					    <div class="col-md-3">2014-05-02</div>
					</li>
				</ul>
			</div>
			<!-- 分页 -->
        	<div id="pagination" ></div>
		</div>
       <%@ include file="bottom.jsp" %>
       <!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h4 class="modal-title" id="myModalLabel">新建文件夹</h4>
	      </div>
	      <div class="modal-body">
	      	<form class="form-horizontal" role="form" id="folder-form">
				<div class="form-group">
					<label for="inputEmail3" class="col-sm-3 control-label">文件夹名称</label>
					<div class="col-sm-9">
						<input type="text" name="name" class="form-control" id="inputEmail3" placeholder="请输入文件夹名称"/>
					</div>
				</div>
				<div class="form-group">
					<p class="text-center" id="msg">注意：文件夹名称不能为空,也不能包含有特殊字符.</p>
				</div>
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	        <button type="button" class="btn btn-primary" id="cmdSaveDoc">保存文件夹</button>
	      </div>
	    </div>
	  </div>
	</div>
	
  </body>
</html>

