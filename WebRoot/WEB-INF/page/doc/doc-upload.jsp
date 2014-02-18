<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%request.setCharacterEncoding("utf-8"); %>
<script type="text/javascript" src="${ctx}/js/business/doc/docBase.js?${version}"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/plugin/uploadify/css/uploadify.css">
<script type="text/javascript" src="${ctx}/plugin/jquery/jquery.min.1.7.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/plugin/uploadify/scripts/jquery.uploadify.js"></script>
<script type="text/javascript">

var uploaded = new Map();//已上传列表，提交后清空
var submited = new Map();//已经提交列表，不清空，文件名为主键，ID为key
var ids = new Array();   //存储本次提交的文件ID,为了防止后台出错时，更改状态
var status = 1;//1：可以关闭的。0：上传中
var fileFlag = '';//文件提取码
var _path = decodeURIComponent('${param.path}');
var fullPath = _path;
var successTotal = 0;
var failTotal = 0;

$(document).ready(function(){
	if(_path.length > 30){
		_path = _path.substring(0,10)+"..."+_path.substring(_path.length - 20);
	}
	$("#curUploadPath").html(_path);
	$("#uploadCompleteBtn").live("click",function(){
		if(status == 0){
			if(!confirm("文件正在上传中，您确定取消吗？")){
				return;
			}
		}
		if(successTotal > 0){
			window.parent.$.fn.uploadComplete('${param.folder}');
		//	window.top.openURL("","完成 文件描述 ",ctx+"/doc/upload-complete.action?flag="+fileFlag+"&folder=${param.folder}&path="+fullPath+"&type=${param.type}");
		}
		if(successTotal > 0){
			
		}
		 window.parent.window.tBox.close();
	})
	
	$('#add_file_button').uploadify({
	      'auto': true, 
	      'swf' : ctx + '/plugin/uploadify/scripts/uploadify.swf',  
	      'expressInstall': '${ctx}/plugin/uploadify/scripts/expressInstall.swf',
	      'uploader': ctx + '/doc/upload!upLoad.action;jsessionid='+sid,    
	      'buttonImage': '${ctx}/img/v4_up_btn.png',
	      'progressData' : 'speed',
	      'fileTypeDesc': '支持的文件类型',     
	      'queueSizeLimit':5000000,
	      'removeCompleted':false, 
	      'width':100,
	      'fileTypeExts': '*.*',     
	      'fileSizeLimit': '2097152000', 
	      'queueID' : 'fileQueue',  	
	      'fileObjName': 'file',  
	      'onSelectError': function(file, errorCode, errorMsg){ 
	      },
	      'onDialogClose':function(data){
		      //filesSelected,filesQueued,filesReplaced,filesCancelled,filesErrored
				//alert(filesQueued + "===== " + filesSelected)
		   },
	      'onFallback': function(){ //flash报错触发
	          alert("请您先安装flash控件");
	      },
	      'onUploadSuccess': function(file, data, response){ //上传成功后触发
	    	  $("#"+file.id).html(file.name + "<br/>上传到服务器成功,请等待转存....");
	    	  $("#"+file.id).attr("status","uploaded");
	    	  uploaded.put(file.id,file.name);
	      },
	      'onUploadError':function(file,errorCode,errorMsg,errorString){
		      alert(errorCode);
	    	  $("#"+file.id).html(file.name + "<br/>上传到服务器失败，出错原因：" + errorMsg);
	    	  $("#"+file.id).attr("status","complete");
		   },
	      'onUploadComplete':function(file){
				
		   },
	      'onUploadStart':function(file){
			   $('#clientInfo').html(file.name + "   上传中...");
			   $('#completeSpan').html('取消');
			   $("#" + file.id).attr("status","uploading");
			   status = 0;
		   },
	      'onUploadProgress' : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
			   //$('#clientInfo').html(file.name + "   上传中...");
          },
	      'onQueueComplete':function(data){
        	  ids = [];
    	      if(data.uploadsSuccessful > 0){
        	      $.each(uploaded.keys(),function(i,id){
            	      	ids.push(id);
						$file = $("#"+id);
						$file.attr("status","submit");
						$file.html(uploaded.get(id) + "<br>文件转存中，请稍等...");
						submited.put(uploaded.get(id),id);
            	   })
            	  $('#clientInfo').html(data.uploadsSuccessful + "个文件正在转存中....");
    	    	  submitDoc();
        	   }
	      }
		}); 

})

function showList(flag){
	
}

function cancelUpload(){
   $('#add_file_button').uploadify('cancel',"*");
   //清session
   window.parent.window.jBox.close();
}


function submitDoc(){
	$.post("${ctx}/doc/upload!submit.action;",{"folder":"${param.folder}",'flag':fileFlag},function(data){
		if(data.code == 1){
			var success = data.successList;
			for(var i = 0 ;i < success.length; i++){
				var id = submited.get(success[i]);
				if(id){
					successTotal ++;
					$("#"+id).attr("status","complete");
					$("#"+id).html("恭喜您,    "+success[i]+"<br>成功上传到:"+_path);
				}
			}
			var fail = data.failList;
			for(var i = 0 ;i < fail.length; i++){
				var id = submited.get(fail[i]);
				if(id){
					failTotal ++;
					$("#"+id).html(" "+fail[i]+"  <br>文件转存失败");
					$("#"+id).attr("status","complete");
				}
			}

			fileFlag = data.flag;
			 $('#clientInfo').html("成功上传:" + successTotal + "个, 失败:" +failTotal + "个" );

			 //检查是否可以关闭窗口
			var total = $("#fileQueue > div").length;
			var complete =  $("#fileQueue > div[status='complete']").length;
			if(total == complete){
				status = 1;
				$('#completeSpan').html('完成');
			}
		}else if(data.code == 3){
			$("#clientInfo").html("文件服务器连接异常,请与管理员联系!失败的文件我们为您缓存30分钟。");
			$.each(status,function(i,id){
				$("#"+id).html(" "+submited.get.get(id)+"  <br>文件转存失败");
				$("#"+id).attr("status","complete");
			})
			status = 1;
			$('#completeSpan').html('完成');
		}
		 uploaded.clear();
   });
}

</script>
<style type="text/css">
		.clearfix:after {
			clear: both;
			content: " ";
			display: block;
			height: 0;
			visibility: hidden;
		}
		
		.clearfix {
			zoom: 1;
		}
		
		#upload_file {
			padding: 0 10px;
		}
		
		#upload_file .upload_toolbar {
			margin-top: 10px;
			margin-bottom: 10px;
			position: relative;
		}
		
		#upload_file .upload_btns {
			float: left;
			height: 30px;
			margin-right: 15px;
			padding-left:10px;
		}
		
		#upload_file .uploadPath {
			float: left;
			height: 30px;
			line-height: 30px;
			vertical-align: middle;
			white-space: nowrap;
			width: 200px;
		}
		
		#fileQueue {
			background-color: #FFF;
			border: 1px solid #DDD;
			clear: both;
			height: 220px;
			overflow-y: auto;
			zoom: 1;
		}
		
		#upload_file .ft {
			background-color: #fff;
			height: 60px;
			text-align: center;
			padding: 10px 0;
		}
		
		#upload_file .upload-tips {
			color: #aaa;
			float: left;
			font-size: 13px;
			margin-left: 20px;
			text-align: left;
		}
		
		
		#uploadCompleteBtn {
			float: right;
		}
		.ft a.x-button-un, .ft a.cancel, .ft a.completeBtn {
			background-position: -85px 0;
			color: #388fc9;
			width: 75px;
		}
		
		.ft a.x-button-on, .ft a.x-button-un, .ft a.completeBtn, .ft a.cancel, .ft a.x-button-move, .ft a.x-button-new {
			background: url('${ctx}/img/dialog-btns.gif') -85px 0 no-repeat;
			height: 30px;
			line-height: 30px;
		}
		.ft .x-button {
			cursor: pointer;
			display: inline-block;
			margin: 10px 5px 0;
			white-space: nowrap;
		}
		
		
		.ft a,.ft a:hover {
			text-decoration: none;
		}
		
		.ft a {
			color: #0068b7;
			outline: none;
		}
		#curUploadPath{
			font-size: 12px;
		}
</style>

<div id="upload_file"  >
	<div class="upload_toolbar clearfix">
		<div class="upload_btns ajax-select-folder" style="cursor: pointer;">
			<input type="file" id="add_file_button" />
		</div>
		<div class="uploadPath" style="">
			到：<span id="curUploadPath"></span>
		</div>
	</div>
	<div id="fileQueue"></div>
	<div class="ft">
		<div id="upload_tips" class="upload-tips">
			<p id="clientInfo"></p>
		</div>
		<a href="javascript:void(0);" 
			class="x-button completeBtn cancel" id="uploadCompleteBtn">
			<span class="x-button-o">
				<span class="x-button-i" id="completeSpan" >关闭</span>
			</span>
		</a>
	</div>
	
	
</div>	

	
	