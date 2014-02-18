package com.chain.ens.web.doc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.chain.ens.entity.doc.Doc;


/**
 * 
 * 文件上传队列
 * @author taosq
 *
 */
public class UploadQueue {
	
	/**
	 * 每个用户上传文件时的session主键
	 */
	private static String SESSION_KEY = "file_session_key";
	
	
	/**
	 * 入队
	 *   根据session来判断用户是否上传过文件
	 *   并将文件缓存到session中
	 * @param session
	 * @param doc
	 */
	@SuppressWarnings("unchecked")
	public static void enQuene(HttpSession session, Doc doc){
		List<Doc> quene = null;
		if(session.getAttribute(SESSION_KEY) != null){
			quene = (List<Doc>) session.getAttribute(SESSION_KEY);
		}else{
			quene = new ArrayList<Doc>();
		}
		quene.add(doc);
		session.setAttribute(SESSION_KEY, quene);
	}
	
	/**
	 * 入队
	 *   根据session来判断用户是否上传过文件
	 *   并将文件缓存到session中
	 * @param session
	 * @param doc
	 */
	@SuppressWarnings("unchecked")
	public static void enQuene(HttpSession session, List<Doc> docs){
		List<Doc> quene = null;
		if(session.getAttribute(SESSION_KEY) != null){
			quene = (List<Doc>) session.getAttribute(SESSION_KEY);
		}else{
			quene = new ArrayList<Doc>();
		}
		quene.addAll(docs);
		session.setAttribute(SESSION_KEY, quene);
	}
	
	/**
	 * 出队
	 *  从session中获取已上传文件队列，并清理session
	 */
	@SuppressWarnings("unchecked")
	public static List<Doc> deQuene(HttpSession session){
		if(session.getAttribute(SESSION_KEY) != null){
			List<Doc> quene = (List<Doc>) session.getAttribute(SESSION_KEY);
			session.removeAttribute(SESSION_KEY);
			return quene;
		}
		return null;
	}
	
	
}
