package com.chain.ens.lucene;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

/**
 * 提取件内容 无论文件多大，提取内容时都不会内存溢出
 * 
 * @author taosq
 * 
 */
public class AbstractContent {
	private static Logger logger = Logger.getLogger(AbstractContent.class);
	/**
	 * 提取文件内容 为了防止内存溢出，提取内容时，只提取前1000个字符
	 * 
	 * @param file
	 *            待提取内容的文件
	 * @return
	 * @throws IOException
	 */

	public static String getText(File file) throws IOException {
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
		InputStream is = new FileInputStream(file);
		Parser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();

		try {
			parser.parse(is, handler, metadata, new ParseContext());
		} catch (Exception e) {
			is.close();
		}
		return handler.toString();
	}

	/**
	 * 文件提取出来以后，保存到一个txt文件中 如果全文提取失败，只提取前1000个字符 一般情况下，一个1000页以下的word文档不会提取失败
	 * 
	 * @param file
	 *            源文件，待提取内容的文件
	 * @param saveFile
	 *            提取出来的文件保存路径
	 * @return
	 * @throws IOException
	 */
	public static void extractText(File file, File saveFile) {
		if (file == null) {
			return;
		}

		if (!saveFile.exists()) {
			if (saveFile.isFile())
				try {
					saveFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		try {
			extractFullText(file, saveFile);
		} catch (Exception e) {
			logger.warn(file.getName() + "完全提取失败");
			try {
				extractPartText(file, saveFile);
			} catch (Exception e1) {
				logger.warn(file.getName() + "提取失败");
			}
		}
	}

	/**
	 * 全文提取
	 * 
	 * @param file
	 *            源文件，待提取内容的文件
	 * @param saveFile
	 *            提取出来的文件保存路径
	 * @throws Exception
	 */
	public static void extractFullText(File file, File saveFile)
			throws Exception {
		if (file == null) {
			return;
		}
		
		Tika tika = new Tika();
		Reader reader = tika.parse(file);
		char[] buffer = new char[1024];
		int len = 0;
		Writer writer = new FileWriter(saveFile);
		//如果文件本提取成功
		while ((len = reader.read(buffer)) != -1) {
			String str = new String(buffer, 0, len);
			if (str != null && str.length() > 0) {
				writer.write(str.trim());
			}
		}
		writer.flush();
		writer.close();
	}

	/**
	 * 全文提取
	 *     该方法在产生索引时，读取记事本的中的文本
	 *     建议不要用于提取除记事本以外的其他文件，否则会内存溢出
	 * @param file
	 *            源文件，待提取内容的文件
	 * @throws Exception
	 */
	public static String  extractFullText(File file)
			throws Exception {
		if (file == null || !file.exists()) {
			return "";
		}
		//如果文件大于10M时，提取前1000个字符
		if(file.length() > 10 * 1024 * 1024){
			return getText(file);
		}
		Tika tika = new Tika();
		Reader reader = tika.parse(file);
		char[] buffer = new char[1024];
		int len = 0;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		while ((len = reader.read(buffer)) != -1) {
			String str = new String(buffer, 0, len);
			if (str != null && str.length() > 0) {
				os.write(str.getBytes());
			}
		}
		return new String(os.toByteArray());
	}
	
	
	
	/**
	 * 提取文件内容 为了防止内存溢出，提取内容时，只提取前1000个字符
	 * 
	 * @param file
	 *            源文件，待提取内容的文件
	 * @param saveFile
	 *            提取出来的文件保存路径
	 * @throws IOException
	 */

	private static void extractPartText(File file, File saveFile)
			throws Exception {
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
		InputStream is = new FileInputStream(file);
		Parser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();

		try {
			parser.parse(is, handler, metadata, new ParseContext());
			Writer writer = new FileWriter(saveFile);
			writer.write(handler.toString().trim());
			writer.flush();
			writer.close();
		} finally{
			is.close();
		}
	}
	
	
}
