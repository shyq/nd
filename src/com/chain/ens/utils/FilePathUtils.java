package com.chain.ens.utils;

import java.io.File;


/**
 * 文件路径工具类
 * 
 *  
 * @author taosq
 *
 */
public final class FilePathUtils {

	
	private FilePathUtils(){};
	
	/**
	 * 获取加密目录
	 * @return
	 */
	public static String getEncryptDir(){
		return PropertyUtil.readValue("encryptDir");
	}

	/**
	 * 获取解密目录
	 * @return
	 */
	public static String getDecryptDir(){
		return getTempDir("decryptDir");
	}

	/**
	 * 获取备份目录目录
	 * @return
	 */
	public static String getBackupDir(){
		return getTempDir("backupDir");
	}
	
	/**
	 * 获取索引库临时存储目录
	 * @return
	 */
	public static String getContentDir(){
		return getTempDir("contentDir");
	}
	
	/**
	 * get分词器的词库
	 * @return
	 */
	public static String getIndexDicDir(){
		return getTempDir("indexDicDir");
	}
	
	/**
	 * 获取桌面背景图片的存放目录
	 * @return
	 */
	public static String getBgDir(){
		return getTempDir("bgDir");
	}
	
	/**
	 * 获取头像的存放目录
	 * @return
	 */
	public static String getAvatarDir(){
		return getTempDir("avatarDir");
	}
	
	/**
	 * 获取pdf目录
	 * @return
	 * @author Chain
	 */
	public static String getPdfDir(){
		return getTempDir("pdfDir");
	}
	
	/**
	 * 获取pdf转换成功目录
	 * @return
	 * @author Chain
	 */
	public static String getPdfSucDir(){
		return getTempDir("pdfSucDir");
	}
	/**
	 * 获取pdf转换失败目录
	 * @return
	 * @author Chain
	 */
	public static String getPdfFailDir(){
		return getTempDir("pdfFailDir");
	}
	/**
	 * 获取view目录
	 * @return
	 * @author Chain
	 */
	public static String getViewDir(){
		return PropertyUtil.readValue("viewDir");
	}
	
	/**
	 * 获取pdf备份目录目录
	 * @return
	 */
	public static String getPdfBackupDir(){
		return getTempDir("pdfBackDir");
	}
	
	private static String getTempDir(String dir){
		return EnsConstants.getWEBINFDir() + File.separator + EnsConstants.getAppConfig().getProperty(dir);
	}

	public static String getOdtDir() {
		return getTempDir("odtDir");
	}
	
}
