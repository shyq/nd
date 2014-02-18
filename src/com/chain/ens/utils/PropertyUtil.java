package com.chain.ens.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyUtil {
	public static String readValue(String filePath, String key) {
		Properties props = new Properties();
		try {
			InputStream in = PropertyUtil.class.getClassLoader()
					.getResourceAsStream(filePath);
			props.load(in);
			return props.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String readValue(String key) {
		Properties props = new Properties();
		try {
			InputStream in = PropertyUtil.class.getClassLoader()
					.getResourceAsStream("appconfig.properties");
			props.load(in);
			return props.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void updateProperties(String key, String value,
			String filePath) {
		Properties props = new Properties();
		try {
			InputStream in = PropertyUtil.class.getClassLoader()
					.getResourceAsStream(filePath);
			props.load(in);
			OutputStream fos = new FileOutputStream(PropertyUtil.class
					.getClassLoader().getResource(filePath).getPath());
			props.setProperty(key, value);
			props.store(fos, "Update '" + key + "' value '" + value + "'");
			fos.close();
		} catch (IOException e) {
			System.err.println("属性文件更新错误");
		}
	}

	public static void writeProperties(String key, String value, String filePath) {
		Properties props = new Properties();
		try {
			OutputStream fos = new FileOutputStream(PropertyUtil.class
					.getClassLoader().getResource(filePath).getPath());
			props.setProperty(key, value);
			props.store(fos, "Update '" + key + "' value");
		} catch (IOException e) {
			System.err.println("属性文件更新错误");
		}
	}

	public static void main(String[] args) {
		/**
		 * username=root password=root dbname=fmsapp port=3306
		 */
//		updateProperties("username", "root", "db.properties");
//		updateProperties("dbname", "fms", "db.properties");
		System.out.println(readValue("appconfig.properties", "jdbc.url"));
		System.out.println(readValue("appconfig.properties", "jdbc.driverClassName"));
		System.out.println(readValue("appconfig.properties", "jdbc.username"));
		System.out.println(readValue("appconfig.properties", "jdbc.password"));
	}
}
