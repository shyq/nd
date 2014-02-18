/*
 * tBox 1.0 - a webpage UI dialog widget written in javascript on top of the jQuery library
 * By Daniel Lin(http://www.aspstat.com)
 * Copyright (c) 2007 Daniel Lin
 * Licensed under the MIT License: http://www.opensource.org/licenses/mit-license.php
*/

var tBox={
    imgs:[ctx+'/img/tBox/min.gif', ctx+'/img/tBox/close.gif', ctx+'/img/tBox/restore.gif',ctx+'/img/tBox/resize.gif'],
	boxes: [],minimizeorder: 0,
	init:function(id){
		var holder =$('#tBoxHolder');
	    if (holder.length == 0){ 
		   holder = $('<div id="tBoxHolder"></div>');
		   $(document.body).append( holder );   
		}
		var box = $('<div class="tBox" id="jb'+id+'"></div' ).appendTo(holder);
			box.append('<div class="tBoxHandler">tBox<div class="tBoxControls"><img src="'+this.imgs[0]+'" title="Minimize" /><img src="'+this.imgs[1]+'" title="Close"/></div></div>')
            .append('<div class="tBoxContent"></div>')
		    .append('<div class="tBoxStatus"><div class="tBoxResize" style="background: url('+this.imgs[3]+')  no-repeat;">&nbsp;</div></div');
		box.css( 'zIndex' , $('div.tBox').length+99);
		box.handler = box.find('div.tBoxHandler');
		box.controls = box.find( 'div.tBoxControls');
		box.content = box.find('div.tBoxContent');
		box.status = box.find( 'div.tBoxStatus' );
		box.Resize = box.find( 'div.tBoxResize' );
		box.handler._parent=box;
		box.controls._parent=box;
		box.content._parent=box;
        box.onclose=function(){return true};
		box.isResize=function(bol){tBox.isResize(this, bol);};
	    box.isScrolling=function(bol){tBox.isScrolling(this, bol);};		
	    box.setSize=function(w, h){tBox.setSize(this, w, h);};
	    box.moveTo=function(x, y){tBox.moveTo(this, x, y);};
	    box.show=function(){ tBox.show(this);};
	    box.hide=function(){ tBox.close(this);};
	    box.load=function(type, source, title,ajaxOpt){tBox.load(this, type, source, title,ajaxOpt);};
	    box.dispose = function(){ tBox.dispose(this);}
		box.attr( 'tBoxID' , this.boxes.length );
		box.tBoxID = this.boxes.length;
	    this.boxes[this.boxes.length]=box;		
	    return box;
	},
	load:function(box,type,source,title,ajaxOpt){
	    var type=type.toLowerCase();
  	    if (typeof title!='undefined') box.handler.get(0).firstChild.nodeValue=title;
        if (title==''){
			 box.handler.css( 'backgroundColor' ,'#fff' );
             box.handler.css('padding' ,'0px');
             box.content.css('padding','0px 10px');
        }
	    if (type=="inline"){
		     box.content.html(source);
	    }
	    else if (type=="div"){
		     box.content.html($('#'+source).html());
	    }else if (type=="iframe"){
		     box.content.css('overflow','hidden');
		     if ( box.content.children().length<1 || box.content.children().get(0).tagName!="IFRAME")
			    box.content.html('<iframe src="" style="margin:0; padding:0; width:100%; height: 100%" name="_iframe-'+box.tBoxID+'" frameborder="0"></iframe>');
		     window.frames["_iframe-"+box.tBoxID].location.replace(source);		     
		}
	    box.content.datatype=type;
	},
	open:function( id, type, source, title,attr,ajaxOpt){	
		function getValue(Name){
			var config=new RegExp(Name+"=([^,]+)", "i");
			return (config.test(attr))? eval( '('+RegExp.$1+')') : 0;
		}
		var box;
		//Find a tBox element has been created else create new element
		if ($('#jb'+id).length==0)
			box=this.init(id);
		else{
			box=tBox.boxes[ parseInt( $('#jb'+id).get(0).getAttribute('tBoxID') ) ];
		}
		box.setSize(getValue(("width")), (getValue("height")));
		var xpos=getValue("center")? "middle" : getValue("left");
		var ypos=getValue("center")? "middle" : getValue("top");
		box.css("visibility" ,"visible");
	    box.css("display" , "block");
		box.css("background","#000");
	    box.content.css("display","block");
		box.isResize(getValue("resize")); 
		box.isScrolling(getValue("scrolling")); 			    	    
	    box.isModel = getValue("model");
	    if ( box.isModel ) tBox.showModel();
		box.minimizable=getValue("minimizable");
		if( !box.minimizable  ){if (box.controls.find('img[@title=Minimize]').length>0)box.controls.find('img[@title=Minimize]').remove()};	
		if ( getValue("draggable") ){box.handler.mousedown(function(e){tBox.etarget=this;tBox.setupDrag(e);});}
		if ( getValue("scrolling") ){box.Resize.mousedown(function(e){tBox.etarget=this;tBox.setupDrag(e);});}
        box.controls.click( function(e){tBox.setupControls(e);} );	
		box.load(type, source, title,ajaxOpt);     
		box.moveTo(xpos, ypos);
		return box;
    },
	isResize:function(box, bol){box.status.css("display",(bol)? "block" : "none");box.resizeBool=(bol)? 1 : 0},
    isScrolling:function(box, bol){box.content.css("overflow",(bol)? "auto" : "hidden");},
	setSize:function(box, w, h){box.css("width",Math.max(parseInt(w),150)+'px');box.content.css("height",Math.max(parseInt(h),150)+'px');},
	reSize:function(box, e){
	      box.css('width', Math.max(tBox.width+tBox.distancex, 150)+"px");
	      box.content.css('height',Math.max(tBox.contentheight+tBox.distancey, 100)+"px");
    },
	moveTo:function(box, x, y){
	    this.getViewPoint();
        box.css("left",(x=="middle")? this.scrollPos[0]+(this.docSize[0]-box.get(0).offsetWidth)/2+"px" : this.scrollPos[0]+parseInt(x)+"px");
        box.css("top",(y=="middle")? this.scrollPos[1]+(this.docSize[1]-box.get(0).offsetHeight)/2+"px" : this.scrollPos[1]+parseInt(y)+"px");
	},
    show:function(box){
		box.css('display','block');
		box.state='restore';
		if ( box.isModel ) tBox.showModel();
	},
    close:function(box){
		if(box==undefined){ 
		box=tBox.boxes[0]; 
		} 
	   try{var closewinbol=box.onclose();}
	   catch(err){var closewinbol=true;}
	   finally{
		 if (typeof closewinbol=="undefined"){
			alert("An error has occured somwhere inside your \"onclose\" event handler")
			var closewinbol=true
		 }
	   }
	   if (closewinbol){
		 if (box.state!="minimized") 
			tBox.saveViewState(box);
		 box.css('display','none');
	     if ( box.isModel ) tBox.hideModel();
	   }
	   return closewinbol;
    },
    dispose:function(box){
       if ( box.isModel) tBox.hideModel();       
       for (var i=0; i<tBox.boxes.length; i++){
         if ( tBox.boxes[i] == box ){
            box.handler._parent=null;
		    box.controls._parent=null;
		    box.content._parent=null;          
		    tBox.boxes[i] = null;  
            break;
         }
       }   
       box.remove();
    },
	minimize:function(btn,box){
       tBox.saveViewState(box);
	   btn.setAttribute('src',tBox.imgs[2]);
	   btn.setAttribute('title','Restore');
	   box.state = 'minimized';
	   box.content.css('display','none');
	   box.status.css('display','none');
	   if( typeof box.minimizeorder == 'undefined' ){
		   tBox.minimizeorder++;
		   box.minimizeorder = tBox.minimizeorder;
	   }
	   box.css('left','10px');
	   box.css('width','200px');
       var margin = box.minimizeorder*10;
	   box.css('top',tBox.scrollPos[1]+tBox.docSize[1]-(box.handler.get(0).offsetHeight*box.minimizeorder)-margin+'px');
	},
	restore:function(btn,box){
		tBox.getViewPoint();
		btn.setAttribute('src',tBox.imgs[0]);
		btn.setAttribute('title','Minimize');
		
		box.state='restore';
		box.css('display','block');
		box.content.css('display','block');
		if( box.resizeBool )
			box.status.css('display','block');
		box.css( 'left',parseInt(box.lastPos[0])+tBox.scrollPos[0]+'px');
		box.css( 'top',parseInt(box.lastPos[1])+tBox.scrollPos[1]+'px');
		box.css( 'width',parseInt(box.lastSize[0])+'px');
	},
    setupControls:function(e){
		var j=tBox;		
	    var sourceobj=window.event? window.event.srcElement : e.target;
		var box = j._retBox(sourceobj);
		
	    if (/Minimize/i.test(sourceobj.getAttribute("title")))
		   j.minimize(sourceobj, box);
	    else if (/Restore/i.test(sourceobj.getAttribute("title")))
		   j.restore(sourceobj, box);
	    else if (/Close/i.test(sourceobj.getAttribute("title")))
		   j.close(box);
	    return false;
	},
	showModel:function(){
      var model =$('#tBox_hideIframe');      
 
	  if ( model.length==0 )
	  {
	     model = $('<iframe id="tBox_hideIframe" scrolling="no" frameborder="0" style="background:#000;position:absolute; top:0px; left:0px;-moz-opacity:0.7; opacity:0.7;filter:alpha(opacity=70)"></iframe>');
		 model.appendTo( document.body );	     	     
		 $(window).bind( 'resize', function(){tBox.showModel();})
	  }
	  tBox.getViewPoint();
	  model.css('width' , tBox.pageSize[0]+'px');
	  model.css('height', tBox.pageSize[1]+'px');	  	  
	},
    hideModel:function(){
	    $(window).unbind( 'resize')
	    var model =$('#tBox_hideIframe');
	    if ( model.length>0 )	
    	   model.remove();
    },
    setupDrag:function(e){
		var j=tBox;
	    var boxE=j.etarget;
		var box = j._retBox(boxE);
	    var e=window.event || e;
	    j.initmousex=e.clientX;
	    j.initmousey=e.clientY;
	    j.initx=parseInt(box.get(0).offsetLeft);
	    j.inity=parseInt(box.get(0).offsetTop);
	    j.width=parseInt(box.get(0).offsetWidth);
	    j.contentheight=parseInt(box.content.get(0).offsetHeight);
	    if (box.content.datatype=="iframe"){
		   box.css('backgroundColor','#F8F8F8');
		   box.content.css('visibility','hidden');
	    }
	    document.onmousemove=j.getDistance;
	    document.onmouseup=function(){
		  if (box.content.datatype=="iframe"){
			 box.content.css('visibility','visible');
		  }
		  j.stopDrag();
	    }
	    return false;
	},
	getDistance:function(e){
		var j=tBox;
	    var etarget=j.etarget;
	    var e=window.event || e;
	    j.distancex=e.clientX-j.initmousex;
	    j.distancey=e.clientY-j.initmousey;
		var box = j._retBox(etarget);
	    if (etarget.className=='tBoxHandler')
		{
			box.css('left',j.distancex+j.initx+'px')
   	        box.css('top',j.distancey+j.inity+'px')
		}
	    else if (etarget.className=='tBoxResize')
		   j.reSize(box, e);
	    return false;
	},
	stopDrag:function(){
		tBox.etarget=null;
	    document.onmousemove=null;
	    document.onmouseup=null;
	},
	getViewPoint:function(){ 
		var ie=document.all && !window.opera
		var domclientWidth=document.documentElement && parseInt(document.documentElement.clientWidth) || 100000;
		this.standardbody=(document.compatMode=="CSS1Compat")? document.documentElement : document.body;
		this.scrollPos= [(ie)? this.standardbody.scrollLeft : window.pageXOffset,
			(ie)? this.standardbody.scrollTop : window.pageYOffset];	
		this.docSize=[(ie)? this.standardbody.clientWidth : (/Safari/i.test(navigator.userAgent))? window.innerWidth : Math.min(domclientWidth, window.innerWidth-16),
			(ie)? this.standardbody.clientHeight: window.innerHeight];
		if ( ie ){
		   this.scrollSize  = [(document.body.scrollWidth > document.body.offsetWidth)?document.body.scrollWidth:document.body.offsetWidth,
			   (document.body.scrollHeight > document.body.offsetHeight)?document.body.scrollHeight:document.body.offsetHeight];
		}
		else{
		   this.scrollSize = [document.body.scrollWidth,window.innerHeight + window.scrollMaxY];
		}
		this.pageSize = [(this.scrollSize[0] < this.docSize[0])?this.docSize[0]:this.scrollSize[0],
			(this.scrollSize[1]< this.docSize[1])?this.docSize[1]:this.scrollSize[1]];
    },
	saveViewState:function(box){
		this.getViewPoint();
		box.lastPos=[ parseInt((box.css('left')||box.get(0).offsetLeft))-tBox.scrollPos[0],
			parseInt((box.css('top')||box.get(0).offsetTop))-tBox.scrollPos[1] ];
		box.lastSize = [parseInt (box.css('width')),
			parseInt( box.css('height'))];

		
	},
    _retBox:function(dom){		
		return tBox.boxes[ parseInt(  $(dom).parents('div.tBox').get(0).getAttribute('tBoxID') ) ];
	}
}

jQuery.fn.opentBox = function(boxAttr,title){
	var $this = jQuery(this);
	$this.attr( 'tBoxID' , tBox.open( 'tBox'+$this.attr('id') ,'div',$this.attr('id'),title,boxAttr).tBoxID );
}
jQuery.fn.disposetBox = function(boxAttr,title){
	var $this = jQuery(this);
	if ( $this.get(0).getAttribute( 'tBoxID' )!=null ){
	   tBox.boxes[parseInt($this.get(0).getAttribute( 'tBoxID' ))].dispose();
	   $this.removeAttr('tBoxID' );
	}
	else{
	   alert( 'pls use opentBox first to Instance a tBox instance!');
	}
}
jQuery.fn.showtBox = function(boxAttr,title){
	var $this = jQuery(this); 
	if (  $this.get(0).getAttribute( 'tBoxID' )!=null  ){
	   tBox.boxes[parseInt($this.get(0).getAttribute( 'tBoxID' ))].show();
	}
	else{
	   alert( 'pls use opentBox first to Instance a tBox instance!');
	}
}
jQuery.fn.hidetBox = function(boxAttr,title){
	var $this = jQuery(this);
	if ( $this.get(0).getAttribute( 'tBoxID' )!=null  ){
	   tBox.boxes[parseInt($this.get(0).getAttribute( 'tBoxID' ))].hide();
	}
	else{
	   alert( 'pls use opentBox first to Instance a tBox instance!');
	}
}

