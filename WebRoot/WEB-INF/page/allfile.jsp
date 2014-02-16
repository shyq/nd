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
         	<a id="barCmdUpload" href="javascript:;" class="btn-upload" >
		 		<em>上&nbsp;传</em>
		 	</a>
		 	<a title="新建文件夹" id="barCmdNewFolder" href="javascript:;" hidefocus="true" 
		 		class="btn-create" >
		 		<em>新建文件夹</em>
		 	</a>
		 	
	 		<a title="下载文件" id="barCmdDownload" class="btn-download" href="javascript:;" hidefocus="true">
		 		<em>下&nbsp;载</em>
		 	</a>
		 	<a title="删除文件" id="barCmdDelete" class="btn-delete" href="javascript:;" hidefocus="true">
		 		<em>删&nbsp;除</em>
		 	</a>
         </div>
         <p class="sub-header" ></p>
         <div class="bs-docs-grid">
	         <div class="row show-grid-board" >
				  <div class="col-xs-12 col-md-7" style="height: 42px;">
				  	<input id="check_all" class="css-checkbox" type="checkbox" />
					<label for="check_all" class="css-label"> </label>
					<label> 文件名  </label>
				  </div>	
				  <div class="col-md-2"><span >大小</span></div>
				  <div class="col-md-3">上传日期</div>
			</div>
			<div>
				<ul class="list-docs ksknns" id="data_list_inner">
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

