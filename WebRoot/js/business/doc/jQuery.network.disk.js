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
	
	// 插件的定义
	$.fn.nd = function(options) {
		opts = $.extend( {}, $.fn.nd.defaults, options);
//		initBtnListener();
//		opts.treeUrl && opts.treeUrl!="" && initFolderTree();
		queryFile(null);
	};
	
	/**
	 * 初始化分页 并 显示文档/文档类型信息
	 */
	function queryFile(parent) {
//		if (exists("check_all")) {
//			$("#check_all").attr("checked", false);
//		}
		// 当前点击的节点的数据不能清除
		//$("#data_list_inner").empty();
		
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
				"order" : opts.order
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
							+ "<li class='row li-header'>"
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
							+ "<li class='row li-header'>"
							+ "<div class='col-xs-12 col-md-7'>"
							+ "<input type='checkbox'  id='dox_ck_" + obj.id + "' class='css-checkbox'/>"
							+ "<label for='dox_ck_" + obj.id + "' name='name_" + obj.id + "' class='css-label'>"
							+ "<div style='margin-top: -8px;'><img  src='"+ctx+"/img/folder-32.png'/></div>"
							+ "</label>"
							+ "<a>" + obj.name + "</a>"
							+ "</div>"
							+ "<div class='col-md-2'><span>4551KB</span></div>"
							+ "<div class='col-md-3'><span>2014-05-02</span></div>"
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
