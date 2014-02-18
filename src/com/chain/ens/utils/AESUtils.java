package com.chain.ens.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.chain.base.utils.StringUtils;

/**
 * 非称加密和解密
 * @author tsq
 *      2012-04-26
 *  AES加/解密
 */
public class AESUtils {

	private static File file;
	private static SecretKey key;
	
	static{
		file = new File(EnsConstants.getWEBINFDir(),"key.jsp");
		try {
			//createKey();
			key = getKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 产生密钥
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static void createKey() throws NoSuchAlgorithmException, IOException {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		SecretKey secretKey = generator.generateKey();
		FileOutputStream fosKey = new FileOutputStream(file);
		ObjectOutputStream obs = new ObjectOutputStream(fosKey);
		obs.writeObject(secretKey);
		obs.close();
		fosKey.close();
	}

	/**
	 * 获取密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static SecretKey getKey() throws Exception {
		FileInputStream is = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(is);
		SecretKey key = (SecretKey) ois.readObject();
		if (null != key) {
			return key;
		}
		return null;
	}

	/**
	 * 对文件进行加密
	 * 
	 * @param src
	 *            源文件
	 * @param dest
	 *            加密后的文件
	 * @param key
	 * @return    加密的结果保存到另一个文件中
	 * @throws Exception
	 */
	public static void encrypt(File src, File dest)
			throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		IODealWith(src, dest, cipher);
	}

	/**
	 * 对文件进行加密,
	 * 
	 * @param src
	 *            源文件
	 * @param dest
	 *            加密后的文件
	 * @param key
	 * @return 加密后的二进制
	 * @throws Exception
	 */
	public static byte[] encrypt(File src) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return IODealWith(src, cipher);
	}

	/**
	 * 对字符串进行加密,
	 * 
	 * @param content
	 *            加密的内容
	 * @param key
	 * @return 加密后的二进制
	 * @throws Exception
	 */
	public static byte[] encrypt(String content)
			throws Exception {
		if(StringUtils.isBlank(content)){
			return null;
		}
		
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(content.getBytes());
	}

	/**
	 * 对二进制进行加密,
	 * 
	 * @param content
	 *            加密的内容
	 * @param key
	 * @return 加密后的二进制
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] content)
			throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(content);
	}

	/**
	 * 解密二制文件
	 * 
	 * @param ciphertext
	 *            密文
	 * @param key
	 * @return 明文的二进制
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] ciphertext)
			throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(ciphertext);
	}
	
	/**
	 * 解密二制文件
	 * 
	 * @param ciphertext
	 *            密文
	 * @param key
	 * @return 字符串
	 * @throws Exception
	 */
	public static String decrypt2String(byte[] ciphertext)
			throws Exception {
		if(ciphertext == null || ciphertext.length ==0){
			return "";
		}
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] data =  cipher.doFinal(ciphertext);
		return new String(data);
	}

	/**
	 * 解密文件
	 * 
	 * @param ciphertext
	 *            密文
	 * @param key
	 * @return 明文的二进制
	 * @throws Exception
	 */
	public static byte[] decrypt(File src) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return IODealWith(src, cipher);
	}

	/**
	 * 对文件进行解密
	 * 
	 * @param src
	 *            密文件
	 * @param dest
	 *            解密后的文件
	 * @param key
	 *            解密的结果保存到另一个文件中
	 * @throws Exception
	 */
	public static void decrypt(File src, File dest)
			throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		IODealWith(src, dest, cipher);
	}

	private static void IODealWith(File src, File dest, Cipher cipher)
			throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(src);
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		if(!dest.exists()){
			dest.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(dest);
		int len = 0;
		byte[] buffer = new byte[1024];
		while ((len = cis.read(buffer)) != -1) {
			fos.write(buffer, 0, len);
		}
		fos.close();
		cis.close();
		fis.close();
	}

	private static byte[] IODealWith(File src, Cipher cipher)
			throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(src);
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = 0;
		byte[] buffer = new byte[1024];
		while ((len = cis.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		cis.close();
		cis.close();
		return bos.toByteArray();
	}

	public static void main(String[] args) throws Exception {
		// createKey();
		//SecretKey key = getKey();
//		byte[] data = encrypt("very good", key);
//		byte[] deData = decrypt(data, key);
//		System.out.println("加密后:" + new String(data));
//		System.out.println("解密后:" + new String(deData));
		
		//encrypt(new File("c:/Home.jpg"), new File("c:/加密后.jpg"), key);
		//decrypt(new File("c:/加密后.jpg"), new File("c:/解密后.jpg"), key);
		
//		File file = new File("f:/software/adobe_photoshop_cs4.exe");
//		long start = System.currentTimeMillis();
//		encrypt(file, new File("c:/加密后.exe"));
//		decrypt(new File("c:/加密后.exe"), new File("c:/解密后.exe"));
//		long end = System.currentTimeMillis();
//		System.out.println("文件大小：" + FileUtils.getHumanSize(file.length()) + "   消耗时间：" + ((end - start)/1000) + "秒");
	}
	
	
}
