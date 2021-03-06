/**
 * 将插件封装在一个闭包里面，防止外部代码污染 冲突
 */
(function() {
	/**
	 * 定义一个插件
	 */
	var NetworkDisk, privateMethod; // 插件的私有方法
	/**
	 * 这里是一个自运行的单例模式。
	 */
	NetworkDisk = (function() {
		/**
		 * 插件实例化部分，初始化时调用的代码可以放这里
		 */
		function NetworkDisk(element, options) {
			// 将插件的默认参数及用户定义的参数合并到一个新的obj里
			this.settings = $.extend({}, $.fn.nd.defaults, options);
			// 将dom jquery对象赋值给插件，方便后续调用
			this.$element = $(element);
		}
		/**
		 * 插件的公共方法，相当于接口函数，用于给外部调用
		 */
		NetworkDisk.prototype.doSomething = function() {
			alert(options.tag);
		};
		return NetworkDisk;
	})();
	/**
	 * 插件的私有方法
	 */
	privateMethod = function() {};
	/**
	 * 这里是关键 定义一个插件 NetworkDisk
	 */
	$.fn.nd = function(options) {
		var instance;
		instance = this.data('NetworkDisk');
		/**
		 * 判断插件是否已经实例化过，如果已经实例化了则直接返回该实例化对象
		 */
		if (!instance) {
			return this.each(function() {
				// 将实例化后的插件缓存在dom结构里（内存里）
				return $(this).data('NetworkDisk', new NetworkDisk(this, options));
			});
		}
		if (options === true)
			return instance;
		/**
		 * 优雅处： 如果插件的参数是一个字符串，则 调用 插件的 字符串方法。 如 $('#id').NetworkDisk('doSomething')
		 * 则实际调用的是 $('#id).NetworkDisk.doSomething(); doSomething是刚才定义的接口。 这种方法 在
		 * juqery ui 的插件里 很常见。
		 */
		if ($.type(options) === 'string')
			instance[options]();
		return this;
	};
	/**
	 * 插件的默认值
	 */
	$.fn.nd.defaults = {
		tag : 'allfile',
		property2 : 'value'
	};
	/**
	 * 优雅处： 通过data-xxx 的方式 实例化插件。 这样的话 在页面上就不需要显示调用了。
	 */
	$(function() {
		return new NetworkDisk($('[data-NetworkDisk]'));
	});
}).call(this);
