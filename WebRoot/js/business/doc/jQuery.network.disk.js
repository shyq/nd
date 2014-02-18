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
	var parent_id=-1;
	var current_path = "全部文件>>新建文件夹";
	// 插件的定义
	$.fn.nd = function(options) {
		opts = $.extend( {}, $.fn.nd.defaults, options);
		initBtnListener();
		showOrHiddenButton();
//		opts.treeUrl && opts.treeUrl!="" && initFolderTree();
		queryFile(null);
	};
	
	/**
	 * 事件初始化
	 */
	function initBtnListener(){
		$("#barCmdNewFolder").live("click", function() {
			newFolder();
		});
		
		$("#barCmdUpload").live('click', function() {
			uploadFile(parent_id);
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
		
		$('#cmdSaveDoc').live('click',function(){
			saveDoc();
		});
		
		
		
		var timer = null;
		
		///:~ 鼠标单击文档事件
		// 选中文档行
		$("#data_list_inner .liclass").live("click", function() {
			// 取消上次延时未执行的方法
			setCheckOrCancel(timer,$(this),false);
		});
		
		///:~ 鼠标双击文档行事件
		$("#data_list_inner li").live("dblclick", function() {
			// 取消上次延时未执行的方法
				clearTimeout(timer);
				var id = $(this).attr("id").split("_")[1];
				if ($(this).attr("file_type") == 'folder') {
					parent_id = id;
					folderDbClick($(this).attr("absname"));
				} else {
					view(id);
				}
		});
		
		///:~ 初始化导航点击事件
		opts.navigation && setPathListener();
		
		///:~ 初始化全选按钮事件
		$("#check_all") && setCheckAllListener();
		
		
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
					&& folder_right_menu_enter.contains(key)
					&& selectNodeAndOnClick(id.split("_")[1]);
				
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
			$li = $(this).parent().parent;
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
	
	/**
	 * 文件夹双击事件
	 * @return
	 */
	function folderDbClick(name,id){
		opts.page = 1; //重置第一页
		$("#check_all").attr("checked", false);// 重置全选按钮
		if (opts.navigation) {//如果有路径(文档管理有路径,高级查询没有路径)
			var aPath = $("<a href='javascript:void;' onclick='query(" + id + ")'>" + name + "</a><i>»</i>");
			//设置路径
			$("#path").append(aPath);
		}
		queryFile();
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
	
	/*
	 * 设置路径监听事件
	 */
	function setPathListener(){
		$("#path a").live("click", function() {
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
	
	/**
	 * 文件上传
	 * 
	 */
	function uploadFile(folderId) {
//		if(isRight){
//			current_node = folder_tree.getNodeByParam("id", folderId);
//			var name = dataMap.get(folderId).name;
//			current_path = current_path==null ? opts.title+">" + name : 
//				current_path + name;
//		}
		
		tBox.open('1000789000','iframe',
					ctx + '/doc/doc-upload.action?folder='
							+ folderId + "&path="
							+ encodeURIComponent(current_path),
					'文件上传',
					'width=520,height=380,center=true,close=false,minimizable=true,resize=false,draggable=true,model=false,scrolling=false')
		$("#jb1000789000 img[title='Close']").remove();
	}

	$.fn.uploadComplete = function(folder) {
		queryFile();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 初始化分页 并 显示文档/文档类型信息
	 */
	function queryFile() {
//		if (exists("check_all")) {
//			$("#check_all").attr("checked", false);
//		}
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
				"parentId":parent_id
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
				var data_list = "";				
				// 遍历信息
				$.each(data.result,function(i, obj) {
					if (obj.docType == 0) {//文件
						data_list =  data_list 
							+ "<li class='row li-header' file_type='file' id='file_" + obj.id + "'>"
							+ "<div class='col-xs-12 col-md-7'>"
							+ "<input type='checkbox'  id='dox_ck_" + obj.id + "' class='css-checkbox'/>"
							+ "<label for='dox_ck_" + obj.id + "' name='name_" + obj.id + "' class='css-label'> </label>"
							+ "<a>" + obj.name + "</a>"
							+ "</div>"
							+ "<div class='col-md-2'><span>4551KB</span></div>"
							+ "<div class='col-md-3'><span>2014-05-02</span></div>"
							+"</li>"
					}else{//1 文件夹
						data_list =  data_list 
							+ "<li class='row li-header' file_type='folder' id='folder_" + obj.id + "' absname='"+obj.name+"'>"
							+ "<div class='col-xs-12 col-md-1'>"
							+ "<input type='checkbox'  id='dox_ck_" + obj.id + "' class='css-checkbox'/>"
							+ "<label for='dox_ck_" + obj.id + "' name='name_" + obj.id + "' class='css-label'>"
							+ "<div style='margin-top: -8px;'><img  src='"+ctx+"/img/folder-32.png'/></div>"
							+ "</label>"
							+ "</div>"
							+ "<div class='col-xs-12 col-md-6 liclass'>"
							+ "<a class='filename-css'>" + obj.name + "</a>"
							+ "</div>"
							+ "<div class='col-md-2 liclass'><span>4551KB</span></div>"
							+ "<div class='col-md-3 liclass'><span>2014-05-02</span></div>"
							+"</li>"
					}
					$("#data_list_inner").html(data_list);
				});
			}
		});
//		showOrHiddenButton();//查询完成后重新控制按钮
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
	
	
	function newFolder(){
		$('#myModal').modal();
	}

	function saveDoc(){
		$.post(ctx + "/doc/doc!save.action?parentId=" + parent_id,
				$('#folder-form').serialize(),
				function(data) {
					$('#myModal').modal("hide");
		  			$("#inputEmail3").val("");
		  			queryFile();
		}, 'json');
	}
	
	function fileNameIsRihgt(name) {
		return !new RegExp("[\\/\:\?\"<>\*\#\|]+").test(name)
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
		orderby : 'docType', // 排序字段
		order : "asc"// 排序方式
	};
	// 闭包结束
})(jQuery);
