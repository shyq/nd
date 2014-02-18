package com.chain.ens.utils;



public class PdfUtil {
	private static String suffixs = PropertyUtil.readValue("format");
	private static long limitSize = Long.valueOf(PropertyUtil.readValue("fileSize"));
	
	/**
	 * 判断文件是否需要转换为pdf
	 * @param format
	 * @param fileSize
	 * @return
	 * @author Chain
	 */
	public static boolean isNeedConvert(String format,long fileSize){
		return suffixs.indexOf(format+",") >= 0 && fileSize <= limitSize;
	}
	
}
