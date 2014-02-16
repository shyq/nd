$(document).ready(function() {
	//cmdNewFolder
	$('#cmdNewFolder').bind('click',function(){
		$('#myModal').modal();
	}); 
	$('#cmdSaveDoc').bind('click',function(){
		$.post(ctx + "/doc/doc!save.action",$('#folder-form').serialize(), function(data) {
			$('#myModal').modal("hide");
  			$("#inputEmail3").val("");
  			queryByPage();
		}, 'json');
	}); 
	
	
	var  queryByPage=function(){
	      $("#tby tr").remove();      
	      $.ajax({      
	      		type: "post",        
	      		url: ctx + "/doc/doc!list.action",        
	      		dataType: "json",   /*这句可用可不用，没有影响*/  
	      		contentType: "application/json; charset=utf-8",      
	      		success: function (data) {    
	      			var array=data;       
	      			var tby=$("#tby");  
	      			//循环json中的数据 
	      			for(var i=0,len=array.length;i<len;i++){   
	      				var td1=$("<td style='vertical-align:middle;width:10px;'><input type='checkbox' name='chk'></td>");  
	      				var td2 =$("<td style='word-break:keep-all;'> <img src='"+ctx+"/img/folder-32.png' class='img-rounded' height:20px; alt='' /><a class='badge'>"+array[i].name+"</a></td>");  
	      				var td3 =$("<td>"+array[i].docType+"</td>");  
	      				var td4 =$("<td>"+array[i].createUser+"</td>");   
	      				var td5 =$("<td>"+array[i].dir+"</td>");   
	      				var tr=$("<tr></tr>"); 
	      				tr.append(td1).append(td2).append(td3).append(td4).append(td5);
	      				tr.appendTo(tby);    
	      			}  
	      		},      
	      		error: function (XMLHttpRequest, textStatus, errorThrown) {     
	      			alert(errorThrown);     
	      		}     
	     });    
	    }  
	      //初始化列表 
	   queryByPage();
});