/**
 *  自定义文档管理插件
 * author :  chain
 */
// 创建一个闭包  
(function($) {
	var opts;//参数
	var current_page=null;//当前页
	var current_node=null;//当前节点
	var dataMap = null;//保存数据的map
	var current_path = null;// 地址导航;
	var folder_tree = null;// 文件目录树
	var folder_right_menu_enter = [ "upload", "addFolder" ];// 在文件夹右键菜单事件发生时，需要进入文件夹的功能
	/**
	 * 操作初始化
	 */
	var fun_array = new Map();
	fun_array.put("newfolder", {name : '新建文件夹',icon:"new",authorName : "mdk"});
	fun_array.put("upload", {name : '上传',icon:"upload",authorName : "upload"});
	fun_array.put("download", {name : '下载',icon:"download",authorName : "upload"});
	fun_array.put("share", {name : '共享',icon:"share",authorName : "share"});
	fun_array.put("cancel_share", {name : '取消共享',icon:"_share",authorName : "cancelShare"});
	fun_array.put("rename", {name : '重命名',icon:"rename",authorName : "modName"});
	fun_array.put("delete", {name : '删除',icon:"delete",authorName : "del"});
	fun_array.put("moveUp", {name : '上移',icon:"up",authorName : "moveUp"});
	fun_array.put("moveDown", {name : '下移',icon:"down",authorName : "moveDown"});
	fun_array.put("batchDelete", {name : '批量删除',icon:"delete",authorName : "del"});
	fun_array.put("recover", {name : '还原',icon:"recover",authorName : "recover"});
	fun_array.put("destory", {name : '删除',icon:"destory",authorName : "destory"});
	fun_array.put("audit", {name : '审批',icon:"audit",authorName : "audit"});
	
	
	var liOptionsMap = new Map();
	liOptionsMap.put("delete","删除");
	liOptionsMap.put("download","下载");
	liOptionsMap.put("newfolder","新建文件夹");
	liOptionsMap.put("rename","重命名");
	liOptionsMap.put("audit","审批");
	liOptionsMap.put("destory","删除");
	liOptionsMap.put("recover","还原");
	
	
	
	// 插件的定义
	$.fn.nd = function(options) {
		opts = $.extend( {}, $.fn.nd.defaults, options);
		initBtnListener();
		opts.treeUrl && opts.treeUrl!="" && initFolderTree();
		queryFile(null);
	};
	
	
	/**
	 * 事件初始化
	 */
	function initBtnListener(){
		$("#barCmdNewFolder").live("click", function() {
			newFolder(current_node==null? null : current_node.id,getCurrentNodeOwn());
		});
		
		$("#barCmdUpload").live('click', function() {
			uploadFile(getCurrentNodeId());
		});
		
		$("#barCmdCancelShare").live("click",function(){
			batchCancelShare();
		});
		$("#barCmdDownload").live("click",function(){
			batchDownload();
		});
		$("#barCmdAudit").live("click",function(){
			batchAudit();
		});
		
		$("#barCmdDelete").live('click', function() {
			batchDelete();
		});
		
		$("#barCmdRecoverer").live('click',function(){
			batchRecover();
		});
		
		$("#search_btn").live('click', function() {
			search();
		});

		$("#barCmdDestory").live('click', function() {
			destory();
		});
		
		$("#barCmdEmpty").live('click', function() {
			empty();
		});
		
		
		
		var timer = null;
		
		///:~ 鼠标单击文档事件
		// 选中文档行
		$("#data_list_inner li").live("click", function() {
			// 取消上次延时未执行的方法
			setCheckOrCancel(timer,$(this),false);
		});
		
		///:~ 鼠标双击文档行事件
		$("#data_list_inner li").live("dblclick", function() {
			// 取消上次延时未执行的方法
				clearTimeout(timer);
				var id = $(this).attr("id").split("_")[1];
				if ($(this).attr("file_type") == 'folder') {
					var node = getTreeNode(id);
					folder_tree.selectNode(node, false);
					folderTreeClick(node);
				} else {
					view(id);
				}
		});
		
		///:~ 初始化导航点击事件
		opts.navigation && setPathListener();
		
		///:~ 初始化全选按钮事件
		exists("check_all") && setCheckAllListener();
		
		
		///:~ 复选框改变选中事件
		$("#data_list_inner li :checkbox").live("click", function(event) {
			if (this.checked) {
				$(this).parent().addClass("focus");
			} else {
				$("#check_all").attr("checked", false);
				$(this).parent().removeClass("focus");
			}
			showOrHiddenButton();
			event.stopPropagation();
		});
		
		///:~ 阻止事件冒泡
		$("#data_list_inner li :checkbox").live("dblclick", function(event) {
			event.stopPropagation();
		});
		
		$("#data_list li a").live('click',function() {
			if($(this).attr("class") == undefined)
				return;
			if($(this).parent().parent() == undefined)
				return;
			var id = $(this).parent().parent().attr("id");
			var key = $(this).attr("class").split("-")[1];
			id.split("_")[0] == "folder"
					&& 　folder_right_menu_enter.contains(key)
					&&　selectNodeAndOnClick(id.split("_")[1]);
				
			exeFunction(key, id.split("_")[1], 3,false,false);
		});
	}
	
	function setCheckOrCancel(timer,obj,isRight){
		if(obj==undefined || obj== null)return;
		clearTimeout(timer);
			// 执行延时
		timer = setTimeout(function() {
			// 取消所有选中行S
			$("#data_list_inner li :checkbox:checked").each(function() {
				$li = $(this).parent();
				if ($li.attr("id") != obj.attr("id")) {
					$li.find(":checkbox").attr("checked",false);
					$("#check_all").attr("checked", false);
					$li.removeClass("focus");
				}
			});
			// 更改选中状态
			if(isRight){
				obj.find(":checkbox").attr("checked", true);
				obj.addClass("focus");
			}else{
				if (obj.find(":checkbox:checked").length > 0) {
					obj.find(":checkbox").attr("checked", false);
					$("#check_all").attr("checked", false);
					obj.removeClass("focus");
				} else {
					obj.find(":checkbox").attr("checked", true);
					obj.addClass("focus");
				}
			}
			
			showOrHiddenButton();
		}, 300);
	}
	
	/*
	 * 设置全选按钮事件
	 */
	function setCheckAllListener(){
		$("#check_all").click(function() {
			$("#data_list_inner li :checkbox").attr("checked",this.checked);
			if (this.checked) {
				$("#data_list_inner li").addClass("focus");
			} else {
				$("#data_list_inner li").removeClass("focus");
			}
			showOrHiddenButton();
		});
	}
	
	function checkAllData(){
		$("#check_all").attr("checked",true);
		$("#data_list_inner li :checkbox").attr("checked",true);
		$("#data_list_inner li").addClass("focus");
	}
	
	/*
	 * 设置路径监听事件
	 */
	function setPathListener(){
		$(".path-contents a").live("click", function() {
			var id = $(this).attr("code");
			if (id == -1) {// 根目录
				folder_tree.cancelSelectedNode(current_node);
				current_node = null;
				current_path = "";
				var pathHtml = " <a href='javascript:void(0);' code='-1' title='"
							    + opts.title + "'>" + opts.title
							    + "</a><i>»</i>"	;
				$(".path-contents").html(pathHtml);
				opts.page = 1;
//				hidenButton(not_select_hiden_btn);
				queryFile(current_node);
			} else {
				current_node != null && current_node.id != id && selectNodeAndOnClick(id);
			}
		});
	}
	
	function search() {
		page = 1;
		model = 1;
		var path = " <a href='javascript:void(0);' code='-1' title='"
				+ opts.title + "'>" + opts.title + "</a><i>»</i>";
		path += '<a title="aa" code="-2" href="javascript:void(0);">搜索结果</a><i>»</i><em></em>'
		$(".path-contents").html(path);
		showOrHiddenButton();
		queryFile(current_node);
		return false;
	}
	
	/**
	 * 显示或者隐藏操作按钮
	 */
	function showOrHiddenButton(){
		var fileSelect = $("#data_list_inner li[file_type!='folder'] :checkbox:checked").length;
		var folderSelect = $("#data_list_inner li[file_type='folder'] :checkbox:checked").length;
		if(fileSelect > 0){
			$("#barCmdDownload").removeClass("disabled");
		}else{
			$("#barCmdDownload").addClass("disabled");
		}
		
		if(fileSelect > 0 || folderSelect > 0){
			$("#barCmdDelete").removeClass("disabled");
			$("#barCmdRecoverer").removeClass("disabled");
			$("#barCmdDestory").removeClass("disabled");
		}else{
			$("#barCmdDelete").addClass("disabled");
			$("#barCmdRecoverer").addClass("disabled");
			$("#barCmdDestory").addClass("disabled");
		}
		
	}
	
	/**
	 * 方法调用
	 * @param key
	 * @param obj
	 * @param o_from
	 * @return
	 */
	function exeFunction(key, obj, o_from,isTree,isRight) {
		var id = o_from == 2 ? obj.id : obj;
		var name = o_from == 2 ? obj.name : dataMap.get(id).name;
		var own = o_from == 2 ? obj.own : dataMap.get(id).own;
		var type = o_from == 2 ? obj.type : dataMap.get(id).type;
		switch (key) {
			case "download":  // 文件下载
				download(id);
				break;
			case "upload":  // 文件下载
				uploadFile(id,isRight);
				break;
				
			case "delete"://文件删除
				var msg = "您确定要删除所选择的文件吗?";
				if(type==0){
					msg = "该文件夹下面可能包含有子文件，您确定要删除吗?";
				}
				deletefile(id,msg,isTree);
				break;
				
			case "newfolder"://新建文件夹
				newFolder(id,own);
				break;
				
			case "rename":
				rename(id,name);
				break;
				
			case "share":
				share(id,type);
				break;
				
			case "destory":
				destoryOne(id);
				break;
				
			case "recover":
				recover(id);
				break;
			case "audit":
				audit(id);
				break;
			default:
				break;
		}
		
	}
	
	
	///:~  文件和文件夹操作事件 
	/**
	 * 单文件下载
	 */
	function download(id) {
		window.top.download( [ id ], false);
	}
	
	function batchDownload() {
		if(getCheckedFile().length < 1)return;
		window.top.download(getCheckedFile(), false);
	}

	/**
	 * 在线预览
	 * @param id
	 * @return
	 */
	function view(id){
		var doc = dataMap.get(id);
		window.top.download( [ id ], true, doc.name,doc.format);
	}
	
	
	/**
	 * 单个文件删除
	 * @param id
	 * @return
	 */
	function deletefile(id,msg,isTree){
		var submit = function(v, h, f) {
			if(v == 'ok' ){
				deleteDoc(id,isTree);
			}else if(v=='cancel'){
				return true;
			}
			return false;
		}
		$.jBox.confirm(msg, "提示", submit);
	}
	
	function batchCancelShare(){
		var count = getCheckedFolderAndFile().length;
		var msg = "您确定要取消所有共享吗?";
		var ids = "";
		if(count < 1) {
			
		} else{
			msg = "您确定要对选中的" + count + "项取消共享吗？";
			ids = getCheckedFolderAndFile().toString();
		}
		var submit = function(v, h, f) {
			if(v == 'ok' ){
				cancelShare(ids);
			}else if(v=='cancel'){
				return true;
			}
			return false;
		}
		$.jBox.confirm(msg, "提示", submit);
	}
	
	/**
	 * 文件批量删除
	 */
	function batchDelete(){
		var count = getCheckedFolderAndFile().length;
		if(count < 1) return ;
		var submit = function(v, h, f) {
			if(v == 'ok' ){
				deleteDoc(getCheckedFolderAndFile().toString(),false);
			}else if(v=='cancel'){
				return true;
			}
			return false;
		}
		$.jBox.confirm("您确定删除选中的" + count + "项吗？", "提示", submit);
	}

	/**
	 * 文件删除
	 */
	function deleteDoc(ids,isTree){
		$.jBox.tip("正在删除文件...","loading");
		$.post(ctx + "/doc/doc!deleteDoc.action", {codes : ids},
			function(data) {
				if (data.code == 1) {
					//					showMsg(data.msg, 'success');
					$.jBox.tip(data.msg,"success");
					if (current_node == null) {
						if(folder_tree)
							folder_tree.reAsyncChildNodes(null, 'refresh')
						queryFile();
					}else{
						if(isTree){
							var node = folder_tree.getNodeByParam("id", current_node.pid);
							setCurrentPath(node);
							current_node = node;
							folder_tree.reAsyncChildNodes(node,	'refresh')
							queryFile(node);
						}else{
							current_node.isParent = true;
							folder_tree.reAsyncChildNodes(current_node,	'refresh')
							queryFile(current_node);
						}
					}
				}else{
//					showMsg(data.msg, 'error');
					$.jBox.tip(data.msg,"error");
				}
				$.jBox.close(true);
			}
		);
	}
	
	 function destoryDoc(ids){
		$.jBox.tip("正在删除文件...","loading");
		$.post(ctx + "/doc/recycle!destory.action", {codes : ids},
			function(data){
				if (data.code == 1) {
					$.jBox.tip(data.msg,"success")
					queryFile();
				}else{
					$.jBox.tip(data.msg,"error");
				}
				$.jBox.close(true);
			}
		);			
	 }
	
	function destory(){
		var count = getCheckedFolderAndFile().length;
		if(count < 1) return ;
		var submit = function(v, h, f) {
			if(v == 'ok' ){
				destoryDoc(getCheckedFolderAndFile().toString(),false);
			}else if(v=='cancel'){
				return true;
			}
			return false;
		}
		$.jBox.confirm("确定要删除这些文件吗？删除了就再也找不回来了…", "提示", submit);
	}
	
	function destoryOne(id){
		if(id){
			var submit = function(v, h, f) {
				if(v == 'ok' ){
					destoryDoc(id,false);
				}else if(v=='cancel'){
					return true;
				}
				return false;
			}
			$.jBox.confirm("确定要删除该文件吗？删除了就再也找不回来了…", "提示", submit);
		}
	}
	
	function emptyDocs(){
		$.jBox.tip("正在清空回收站...","loading");
		$.post(ctx + "/doc/recycle!empty.action",function(data){
			if (data.code == 1) {
				$.jBox.tip(data.msg,"success")
				queryFile();
			}else{
				$.jBox.tip(data.msg,"error");
			}
			$.jBox.close(true);
		});			
	}
	
	function empty(){
		if($("#data_list_inner li").length < 1){
			$.jBox.tip("没有需要删除的文档。","error");
			return ;
		} 
		var submit = function(v, h, f) {
			if(v == 'ok' ){
				emptyDocs();
			}else if(v=='cancel'){
				return true;
			}
			return false;
		}
		$.jBox.confirm("确定要清空回收站吗？删除了就再也找不回来了…", "提示", submit);
	}
	
	function batchRecover(){
		var count = getCheckedFolderAndFile().length;
		if(count < 1) return ;
		recover(getCheckedFolderAndFile().toString());
	}
	
	function recover(ids){
		$.jBox.tip("正在还原所选文件(夹)，请稍后...","loading");
		$.post(ctx + "/doc/recycle!recover.action",{codes:ids},
			function(data){
				if (data.code == 1) {
					$.jBox.tip(data.msg,"success")
					queryFile();
				}else{
					$.jBox.tip(data.msg,"error");
				}
				$.jBox.close(true);
		});	
	}
	
	
	function cancelShare(ids){
		$.jBox.tip("正在取消共享,请稍后...","loading");
		$.post(ctx + "/doc/doc!cancelShare.action", {codes : ids},function(data){
			if (data.code == 1) {
				$.jBox.tip(data.msg,"success");
				if (current_node == null) {
					folder_tree.reAsyncChildNodes(null, 'refresh')
					queryFile();
				}else{
					current_node.isParent = true;
					folder_tree.reAsyncChildNodes(current_node,	'refresh')
					queryFile(current_node);
				}
			}else{
//				showMsg(data.msg, 'error');
				$.jBox.tip(data.msg,"error");
			}
			$.jBox.close(true);
		});
	}
	
	/**
	 * 文件(夹)共享
	 * id 文件(夹)id
	 * type ： 1为文件 0为文件夹
	 */
	function share(id,type){
		$.jBox("iframe:" + ctx + '/doc/doc-share.action?id=' + id + "&type="
				+ type, {
			title : "共享设置",
			id : '1000789200',
			width : 500,
			height : 420
		});
		$("#1000789200 div[class='jbox-button-panel']").remove();
	}
	
	/**
	 * 共享完成事件
	 */
	$.fn.shareCompelte = function() {
		if (current_node != null) {
			folderTreeClick(current_node);
		} else {
			queryFile(null);
		}
	}
	
	$.fn.approveComplete = function(count) {
		window.top.setAuditCount(count);
		queryFile();
	}
	
	function audit(id){
		$.jBox("iframe:" + ctx + "/doc/audit-page.action?files="
				+ id, {
			title : "文档审批",
			id : '1000324500',
			width : 400,
			height : 250
		});
		$("#1000324500 div[class='jbox-button-panel']").remove();
	}
	
	/**
	 * 批量审核
	 */
	function batchAudit(){
		if($("#data_list_inner li").length < 1){
			$.jBox.tip("没有需要审批的文档。","error");
			return ;
		} 
		if(getCheckedFile() == null || getCheckedFile().length < 1){
			checkAllData();
		}
		audit(getCheckedFile().toString());
	}
	
	/**
	 * 获取当前节点所属部门 如果是公共文档则得到部门id，
	 * 如果是个人文档则得到用户id
	 */
	function getCurrentNodeOwn() {
		if (current_node != null) {
			return current_node.own;
		}
		return null;
	}
	
	/**
	 * 获取当前选中的所有文件
	 */
	function getCheckedFile() {
		var ids = new Array();
		$("#data_list_inner li[file_type!='folder'] :checkbox:checked").each(
				function(i, obj) {
					ids.push($(this).val());
		});
		return ids;
	}
	
	/**
	 * 获取当前选中的所有文件夹
	 */
	function getCheckedFolder() {
		var ids = new Array();
		$("#data_list_inner li[file_type='folder'] :checkbox:checked").each(
				function(i, obj) {
					ids.push($(this).val());
				});
		return ids;
	}
	
	/**
	 * 获取当前选中的所有文件夹和文件
	 */
	function getCheckedFolderAndFile() {
		var ids = new Array();
		$("#data_list_inner li :checkbox:checked").each(
				function(i, obj) {
					ids.push($(this).val());
				});
		return ids;
	}
	
	/**
	 * 重命名操作，需要严格控制文件名的合法化，特别是不允许出现HTML标签。
	 */
	function rename(id, name) {
//		if (type == 1) {
//			if (isRelationFile(id)) {
//				showMsg('关联文件不能重命名!', "warning");
//				return;
//			}
//			name = name.substring(0, name.lastIndexOf("."));
//		}
		var html = "<div style='padding:10px;'>文件名：<input type='text' id='name'  maxlength='50' size='40' class='formText' name='name' value='"
				+ name + "' /></div>";
		var submit = function(v, h, f) {
			var _name = $.trim(f.name);
			if (_name == '') {
				showMsg("请输入文件名", 'error');
				return false;
			}
			if (!fileNameIsRihgt(_name)) {
				showMsg("文件名不能包含以下任何字符</br> / \\ < > : \" | # ", 'error');
				return false;
			}
			if (_name != name) {
				$.jBox.tip("正在修改文件名...","loading");
				$.post(ctx + "/doc/doc!reName.action", {
					name : _name,
					id : id
				}, function(data) {
					if (data.code == 1) {
//						showMsg(data.msg, 'success');
						$.jBox.tip(data.msg,"success");
						queryFile(current_node);
						$.jBox.close(true);
					} else {
						$.jBox.tip(data.msg,"error");
//						showMsg(data.msg, 'error');
					}
				});
			} else {// 文件名相同不用修改
				return true;
			}
			return false;
		};

		$.jBox(html, {
			title : "重命名",
			submit : submit
		});
	}
	
	function isSelectOnlyDir(){
		if(getCheckedFolder().length!=1){
			return false;
		}else{
			folderId = getCheckedFolder();
			var name = dataMap.get(folderId).name;
			current_path = current_path==null ? opts.title+">" + name : 
				current_path + name;
			current_node = getTreeNode(folderId);
		}
		return true;
	}
	
	/**
	 * parentId
	 * 
	 */
	function uploadFile(folderId,isRight) {
		if(folderId==undefined || folderId == null || folderId == ""){
			$.jBox.tip("请先(双击)进入您要上传文件的目录。","error");
			return;
		}
		if(isRight){
			current_node = folder_tree.getNodeByParam("id", folderId);
			var name = dataMap.get(folderId).name;
			current_path = current_path==null ? opts.title+">" + name : 
				current_path + name;
		}
		
		tBox.open('1000789000','iframe',
					ctx + '/doc/doc-upload.action?folder='
							+ folderId + "&path="
							+ encodeURIComponent(current_path==null ? opts.title : current_path),
					'文件上传',
					'width=520,height=360,center=true,close=false,minimizable=true,resize=false,draggable=true,model=false,scrolling=false')
		$("#jb1000789000 img[title='Close']").remove();
	}

	$.fn.uploadComplete = function(folder) {
		if(opts.pageTag == "common"){
			$.jBox.tip("您上传的公共文档将在审批通过之后入库，请耐心等待...。","error");
		}
		if (folder == getCurrentNodeId()) {
			folder_tree.selectNode(current_node, false);
			folderTreeClick(current_node);
			queryFile(current_node);
		}
	}
	
	//-----------------------左侧树目录初始化

	/**
	 * 初始化树
	 */
	function initFolderTree(){
		folder_tree = $.fn.zTree.init($("#" + opts.treeId),
				folder_tree_setting);
	}
	/**
	 * folder_tree_setting
	 */
	var folder_tree_setting = {
		async : {
			dataType : "text",
			enable : true,
			type : "post",
			url : function getAsyncUrl(treeId, treeNode) {
				return opts.treeUrl + "?parentId="
						+ (treeNode == null ? '' : treeNode.id);
			}
		},
		callback : {
			onClick : function(event, treeId, treeNode, clickFlag) {
				folderTreeClick(treeNode);
			},
			onRightClick:folderTreeRirhtClick
		},
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pid",
				rootPId : 0
			}
		},
		view : {
			autoCancelSelected : false,
			dblClickExpand : false,
			expandSpeed : "fast",
			fontCss : {},
			selectedMulti : false,
			showIcon : true,
			showLine : false,
			showTitle : true
	   }
	};
	
	/**
	 * 文件夹树的点击事件
	 */
	function folderTreeClick(treeNode){
		current_node = treeNode;//设置当前节点
		opts.page = 1; //重置第一页
		$("#check_all").attr("checked", false);// 重置全选按钮
		if (opts.navigation) {//如果有路径(文档管理有路径,高级查询没有路径)
			// 当前路径
			var path = "";
			var rootPath = " <a href='javascript:void(0);' code='-1' title='"
					+ opts.title + "'>" + opts.title + "</a><i>»</i>";
			var _rootPath = opts.title;
			var _path = "";
			var node = treeNode;
			while (node != null) {
				var name = node.name;
				var _name = node.name;
				if (node.name.length > 5) {
					name = node.name.substring(0, 3)
						  + '...'
						  + node.name.substring(node.name.length - 2);
				}
				path = "<a href='javascript:void(0);'code='" + node.id
				   		+ "' title='" + node.name + "'>" + name
				   		+ "</a><i>»</i>" + path;
				_path = _name + ">" + _path;
				node = folder_tree.getNodesByParam('id', node.pid)[0];
			}
			current_path = _rootPath + ">" + _path;
			path = rootPath + path + "<em></em>";
			$(".path-contents").html(path);
		}
		// 展开节点
		if (treeNode.isParent) {
			if (!treeNode.open) {
				folder_tree.expandNode(treeNode, true, false, true, false);
			} else {
				// folder_tree.expandNode(treeNode, false, false, true, false);
			}
		}
		// 显示信息
		queryFile(treeNode);
	}
	
	// 刷新节点
	function refreshTree(isRoot) {
		// 添加根节点
		if (isRoot) {
			folder_tree.reAsyncChildNodes(null, 'refresh');
		} else {
			// 查找当前添加节点
			var node = getTreeNode(getCurrentNodeId());
			node.isParent = true;// isParent = false时不会异步加载,
			folder_tree.reAsyncChildNodes(node, 'refresh');
		}
	}
	
	/**
	 * ztree右键菜单
	 */
	function folderTreeRirhtClick(event, treeId, treeNode){
		if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
			folder_tree.cancelSelectedNode();
		} else if (treeNode && !treeNode.noR) {
			folder_tree.selectNode(treeNode);
		}
		folder_tree.selectNode(treeNode);
		selectNodeAndOnClick(treeNode.id);
		var itemArray = createMenuItemArray(opts.treeMenu,treeNode,"tree");
		var menu = createMenu(itemArray,'',true,false).show(); 
	    doRightMenu(menu,event.clientX,event.clientY);
	}
	
	/**
	 * 获取当前节点编号， 如果是目录树，得到目录ID 如果是用户树，得到用户ID
	 */
	function getCurrentNodeId() {
		if (current_node != null) {
			return current_node.id;
		}
		return '';
	}
	
	// 获取指定ID的ztree节点对象
	function getTreeNode(id) {
		return folder_tree.getNodesByParam('id', id)[0];
	}
	
	/**
	 * 选中树节点并单击
	 */
	function selectNodeAndOnClick(id) {
		var node = getTreeNode(id);
		folder_tree.selectNode(node, false)
		folderTreeClick(node);
	}
	
	/**
	 * 文件或文件夹列表的右键菜单
	 */
	function loadFileOrFolderContext(obj,menus,doc,type){
		var itemArray = createMenuItemArray(menus,doc,type);
		obj.bind('contextmenu', function(e) {	
			var id = obj.attr("id");
//			if (id.split("_")[0] == "folder"){
//				setCurrentPath(doc);
//			}
		    var menu = createMenu(itemArray,'',false,true).show();
		    doRightMenu(menu,e.pageX,e.pageY,obj)
		    // Cancel event, so real browser popup doesn't appear.
		    return false;
		  });
		return obj;
	}
	
	//:~右键菜单初始化
	/**
	 * 创建右键菜单items  [{}]
	 * obj 需要创建菜单的html Object
	 * menus 菜单内容
	 * doc 数据库对象  o_from : 2 单击的是树 传过来的obj是treeNode o_from : 3 单击的右键菜单 传过来的是id
	 * type 类型  3为文件或文件夹右键菜单，2为目录树的右键菜单
	 */
	function createMenuItemArray(menus,doc,type){
		var itemArray = new Array();
		if(menus==null || menus.length==0)
			return itemArray;
		var items = menus.split(" ");
		var getObjType = type == "tree" ? 2 : 3;
		$.each(items,function(i,_key){
			if("-"==_key){
				itemArray.push(null);
				return;
			}
			var fun = fun_array.get(_key);//fun.authorName
			if(fun==null || fun==undefined)return;
			//TODO 代码优化,时间关系 先这么着吧。权限验证
//			if(eval(isSuper)){
			var _id = 2 == getObjType ? doc : doc.id;
			var icontmp  = ctx + "/js/lib/contextMenu/icons/" + fun.icon + ".png";
			itemArray.push({label:fun.name,icon:icontmp ,id:_id,key:_key,type:getObjType});
//			}else{
//				if(fun.authorName==null || fun.authorName==undefined || fun.authorName=="") return;
//				if(!Author.isPassAuthor(opts.type,doc[fun.authorName],fun.authorName,type)) return;
//				var _id = 2 == getObjType ? doc : doc.id;
//				itemArray.push({label:fun.name,id:_id,key:_key,type:getObjType});
//			}
		});
		return itemArray;
	}
	
	/**
	 * 右键菜单创建  
	 * items  [{}] 右键菜单内容
	 * title 菜单标题
	 */
	function createMenu(items,title,isTree,isRight) {
		if($(".contextMenuPlugin").length > 0 ){
			$.each($(".contextMenuPlugin"),function(i,v){
				document.body.removeChild(v);
			});
		}
	    var menu = $('<ul class="contextMenuPlugin"><div class="gutterLine"></div></ul>')
	      .appendTo(document.body);
	    if (title) {
	      $('<li class="header"></li>').text(title).appendTo(menu);
	    }
	    $.each(items,function(i,item){
	    	if(item){
	    		var rowCode = '<li><a href="#"><span></span></a></li>';
	    		var row = $(rowCode).appendTo(menu);
	    		if(item.icon){
    	          var icon = $('<img>');
    	          icon.attr('src', item.icon);
    	          icon.insertBefore(row.find('span'));
	    	    }
	    		row.find('span').text(item.label);
	    		row.find('a').unbind("click");
	    		row.find('a').bind("click",{key:item.key,id:item.id,type:item.type,isTree:isTree,isRight:isRight},cthandler);
	    	}else{
	    		$('<li class="divider"></li>').appendTo(menu);
	    	}
	    });
	    return menu;
	 }
	/**
	 * 右键菜单项的点击事件
	 */
	function cthandler(e){
		var key=e.data.key;
		var id = e.data.id;
		var type=e.data.type;
		var isTree = e.data.isTree;
		var isRight = e.data.isRight;
		exeFunction(key, id, type,isTree,isRight);
	}
	
	/**
	 * 菜单的处理事件 
	 */
	function doRightMenu(menu,x,y,obj){
		var left = x + 5, /* nudge to the right, so the pointer is covering the title */
	        top = y;
	    if (top + menu.height() >= $(window).height()) {
	        top -= menu.height();
	    }
	    if (left + menu.width() >= $(window).width()) {
	        left -= menu.width();
	    }
	    setCheckOrCancel(null,obj,true);
	    // Create and show menu
	    menu.css({zIndex:1000001, left:left, top:top})
	      .bind('contextmenu', function() { return false; });
	    // Cover rest of page with invisible div that when clicked will cancel the popup.
	    var bg = $('<div></div>')
	      .css({left:0, top:0, width:'100%', height:'100%', position:'absolute', zIndex:1000000})
	      .appendTo(document.body)
	      .bind('contextmenu click', function() {
	        // If click or right click anywhere else on page: remove clean up.
	        bg.remove();
	        menu.remove();
	        return false;
	      });

	    // When clicking on a link in menu: clean up (in addition to handlers on link already)
	    menu.find('a').click(function() {
	      bg.remove();
	      menu.remove();
	    });
	}
	
	
	/**
	 * 设置导航路径
	 */
	function setCurrentPath(node){
		if (opts.navigation) {
			// 当前路径
			var path = "";
			var rootPath = " <a href='javascript:void(0);' code='-1' title='"
					+ opts.title + "'>" + opts.title + "</a><i>»</i>";
			var _rootPath = opts.title;
			var _path = "";
			if (node != null) {
				var name = node.name;
				var _name = node.name;
				if (node.name.length > 5) {
					name = node.name.substring(0, 3) + '...'
							+ node.name.substring(node.name.length - 2);
				}
				path = "<a href='javascript:void(0);'code='" + node.id
						+ "' title='" + node.name + "'>" + name
						+ "</a><i>»</i>" + path;
				_path = _name + ">" + _path;
			}

			current_path = _rootPath + ">" + _path;
			path = rootPath + path + "<em></em>";
			$(".path-contents").html(path);
		}
	}
	//----------------------- 文件列表初始化
	
	/**
	 * 初始化分页 并 显示文档/文档类型信息
	 */
	function queryFile(treeNode) {
		if (exists("check_all")) {
			$("#check_all").attr("checked", false);
		}
		// 当前点击的节点的数据不能清除
		$("#data_list_inner").empty();
		
		var _query_url = opts.queryUrl;
		if(opts.searchForm != null){
			_query_url = opts.queryUrl+"?"+$("#"+opts.searchForm).serialize();
		}
		//$.jBox.tip("正在加载数据...","loading");
		$.ajax( {
			type : "POST",
			dataType : "json",
			cache : false,
			url : _query_url,
			data : {
				'page' : opts.page,
				"rows" : opts.rows,
				"sort" : opts.orderby,
				"order" : opts.order,
				'parentId' : treeNode == null ? -1 : treeNode.id
			},
			success : function(data) {
				initPagination(data.totalCount, data.pageNo);
				// 不存在文档/文档信息
				if (data.totalCount == 0) {
					$("#data_list_outer").hide();
					$("#no_file_box").show();
					$("#barCmdDestory").addClass("disabled");
					return;
				}
				
				$("#barCmdEmpty").removeClass("disabled");
				$("#data_list_outer").show();
				$("#no_file_box").hide();
				$("#barCmdEmpty").show();
				var data_list = "";
				$("#data_list_inner").attr("search", "");
				dataMap = new Map();// 把当前的数据保存在数组中
				var new_html = "";
				
				var fileBtnHtml =  "";
				var folderBtnHtml = "";
				
				if(opts.fileBtns!=null && opts.fileBtns.length > 0){
					var btnItems = opts.fileBtns.split(" ");
					if(btnItems){
						$.each(btnItems,function(i,item){
							var name = liOptionsMap.get(item);
							fileBtnHtml += "<a href='javascript:void(0);' class='i-"+ item +"' title='"+name+"'>"+name+"</a>"
						});
					}
				}
				if(opts.folderBtns!=null && opts.folderBtns.length > 0){
					var btnItems = opts.folderBtns.split(" ");
					if(btnItems){
						$.each(btnItems,function(i,item){
							var name = liOptionsMap.get(item);
							folderBtnHtml += "<a href='javascript:void(0);' class='i-"+ item +"' title='"+name+"'>"+name+"</a>"
						});
					}
				}
				
				// 遍历信息
				$.each(data.result,function(i, obj) {
					dataMap.put(obj.id, obj);
					var share_ico = " ";
					// 文档是否共享
					if (obj.isShare) {
						share_ico = "<b class='shared'></b>";
					}
					var cancelShare = "";
					if(obj.isShare){
						
					}
					
					
					
					// 判断是否是文档分类
					if (obj.type == 0) {
						data_list = data_list
								+ "<li rel='item' id='folder_"
								+ obj.id
								+ "' file_type='folder'>"
								+ "<input type='checkbox' value='"
								+ obj.id
								+ "'>"
								+ "<i class='file-type tp-folder'>"
								+ share_ico
								+ "</i>"
								+ "<div class='file-name'>"
								+ "<a href='javascript:void(0);' btn='goto_dir' cid='"
								+ obj.id
								+ "'  rel='view_folder'>"
								+ obj.name
								+ "</a>"
								+ "</div>"
								+ "<div class='file-info'>"
								+ "<em>"
								+ obj.createTime
								+ "</em>"
								+ "</div>"
								+ "<div class='file-opt'>"
								+ folderBtnHtml
								+ "</div>" + "</li>";
					} else { // 文档
						data_list = data_list
								+ "<li rel='item' id='file_"
								+ obj.id
								+ "' file_type='file' >"
								+ "<input type='checkbox' value='"
								+ obj.id
								+ "'>"
								+ "<i class='file-type tp-"
								+ obj.format
								+ "'>"
								+ share_ico
								+ "</i>"
								+ "<div class='file-name'>"
								+ "<a href='javascript:void(0)' field='file_name'>"
								+ obj.name
								+ "</a>"
								+ "</div>"
								+ "<div class='file-info'>"
								+ "<em>"
								+ obj.createTime
								+ "</em>"
								+ "<em>"
								+ getHumanSize(obj.fileSize)
								+ "</em>"
								+ "</div>"
								+ "<div class='file-opt'>"
								+ fileBtnHtml
								// + more_html
								+ "</div>" + "</li>";
					}
					$("#data_list_inner").html(data_list);
				});
				//-------------------添加右边文件或者文件夹列表右键菜单
				$.each(data.result,function(i, doc) {
					var menus=new Array();
					var obj;
					var type;
					if(doc.type==0){
						menus = opts.folderMenu;
						obj = $("#folder_"+doc.id);
						type = "folder";
					}else{
						menus = opts.fileMenu;
						obj = $("#file_"+doc.id)
						type = "file";
					}
					loadFileOrFolderContext(obj,menus,doc,type);
				});
			}
		});
		showOrHiddenButton();//查询完成后重新控制按钮
	};
	
	
	
	
	//----------------------- 分页
	// 初始化分页控件
	function initPagination(total, current_page) {
		opts.page = current_page;
		$("#pagination").pagination(total, {
			callback : pageCallback,
			items_per_page : opts.rows, // 显示条数
			prev_text : "上一页", // 上一页按钮里text
			next_text : "下一页", // 下一页按钮里text
			num_display_entries : 6, // 连续分页主体部分分页条目
			current_page : opts.page - 1, // 当前页索引
			num_edge_entries : 2
		// 两侧首尾分页条目数
		});
	};

	// 分页控件换页回调方法
	function pageCallback(index, jq) {
		// 变更当前页
		opts.page = index + 1;
		// 加载数据
		queryFile(current_node);
	};
	
	function fileNameIsRihgt(name) {
		return !new RegExp("[\\/\:\?\"<>\*\#\|]+").test(name)
	}
	
	//----------------------- 新建文件夹
	/**
	 * 新建文件夹
	 * pid : 父文件夹id
	 */
	function newFolder(pid,depId){
		var submit = function(v, h, f) {
			if (v == 'ok') {
				var _name = $.trim(f.name);
				var ownVal = h.find("#own").val();
				if(opts.pageTag == "common"){
					if(ownVal==null ||　ownVal == ""){
						showMsg("请选择部门。", "warning");
						return false;
					}
				}
				if(h.find("#own").val())
				if (_name == "") {
					showMsg("请输入名称!", "warning");
					return false;
				}
				if (!fileNameIsRihgt(_name)) {
					showMsg("文件名不能包含以下任何字符</br> / \\ < > : \" | # ", 'error');
					return false;
				}
				
				if (_name.length > 50) {
					showMsg("文件名不超过50个字符", "warning");
					return false;
				}
				$.post(ctx + "/doc/doc!save.action", f, function(data) {
					if (data.code == 1) {
						showMsg("添加成功!", "success");
						var obj = eval("(" + data.obj + ")")
						var node = {
							"id" : obj.id,
							"name" : obj.name
						}
						refreshTree(f.parentId == "");
						// 如果不是顶层节点，执行单击树的事件
						if (f.parentId != "") {
							folderTreeClick(current_node);
						} else {
							// 如果是顶层节点添加了文件夹,需要重新加文件夹信息
							queryFile();
						}
					} else {
						showMsg(data.msg, "error");
						return false;
					}
				}, 'json')
			}
		}
		
		$.jBox(opts.newFolderHtml,{
			title : "新建文件夹",
			submit : submit,
			height:150,
			width:330,
			buttons : {
				'确定' : 'ok',
				'取消' : 'cancel'
			},
			loaded:function(h){
				if(pid!=null) h.find("#folder-parent-id").val(pid);
				if(opts.pageTag == "common"){
					h.find("#ownType").val(0);
					if(depId != null){
						h.find("#own").val(0);
						h.find("#depart_tr").remove();
					}else{
						h.find('#folder_depart').combotree({
							url : ctx + '/base/organ!parentOrgan.action'
									+ (depId == null ? ''	: '?parentId=' + depId),
							multiple : false,// 是否可多选
							editable : false,// 是否可编辑
							width : 220,
							height : 30,
							valueField : 'id',
							displayField : 'text',
							onClick : function(node) {
								h.find("#own").val(node.id)
							}
						});
					}
				}
			}
		});
	}
	
	// 插件的defaults
	$.fn.nd.defaults = {
		newFolderHtml:'',//新建文件夹html
		treeUrl:'',//文档树url
		treeId:'',
		pageTag:'manager',//manager:文档管理,query:高级查询
		treeMenus:'',//文档目录树菜单
		fileMenu:'',//文件列表右键菜单
		folderMenu:'',//文件列表右键菜单
		fileBtns:'',//文件操作按钮
		folderBtns:'',//文件夹操作按钮
		navigation : true,
		title : null,// 标题
		queryUrl:'',//文档查询url
		searchForm:null,//文档搜索条件form
		page : 1, // 当前页数
		rows : 10, // 每页显示多少条
		orderby : 'type', // 排序字段
		order : "asc"// 排序方式
	};
	// 闭包结束
})(jQuery);
