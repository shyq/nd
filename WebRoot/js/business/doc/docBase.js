/**
 * 格式化文件大小
 */
var units = [ "字节", "KB", "MB", "GB" ];
function getHumanSize(size) {
	// 设置位数
	
	var i = 0;
	while (size > 1024) {
		size = Math.round(size / 1024);
		i++;
	}
	return (size.toFixed(2)) + units[i];
}



/**
 * 弹出窗口 提示信息.
 * 
 * @param msg
 *            消息内容
 */
function showMsg(msg,iconType) {
	$.jBox.messager(msg, "提示", 3000, {
	    width: 350,
	    icon: iconType,
	    showType: 'slide'
	});
}
// 判断元素是否存在
function exists(id){
	if($("#" + id).length > 0){
		return true;
	}else{
		return false;
	}
}


function Map() {
    this.elements = new Array();

    // 获取MAP元素个数
    this.size = function() {
        return this.elements.length;
    };

    // 判断MAP是否为空
    this.isEmpty = function() {
        return (this.elements.length < 1);
    };

    // 删除MAP所有元素
    this.clear = function() {
        this.elements = new Array();
    };

    // 向MAP中增加元素（key, value)
    this.put = function(_key, _value) {
        this.elements.push( {
            key : _key,
            value : _value
        });
    };

    // 删除指定KEY的元素，成功返回True，失败返回False
    this.remove = function(_key) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    // 获取指定KEY的元素值VALUE，失败返回NULL
    this.get = function(_key) {
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    return this.elements[i].value;
                }
            }
        } catch (e) {
            return null;
        }
    };

    // 获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
    this.element = function(_index) {
        if (_index < 0 || _index >= this.elements.length) {
            return null;
        }
        return this.elements[_index];
    };

    // 判断MAP中是否含有指定KEY的元素
    this.containsKey = function(_key) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    // 判断MAP中是否含有指定VALUE的元素
    this.containsValue = function(_value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    // 获取MAP中所有VALUE的数组（ARRAY）
    this.values = function() {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].value);
        }
        return arr;
    };

    // 获取MAP中所有KEY的数组（ARRAY）
    this.keys = function() {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].key);
        }
        return arr;
    };
  
}



String.prototype.startWith=function(str){     
	  var reg=new RegExp("^"+str);     
	  return reg.test(this);        
	}  

String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
} 

Array.prototype.contains = function (element) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == element) {
			return true;
		}
	}
	return false;
} 
