package com.chain.ens.service.doc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chain.base.orm.Page;
import com.chain.base.orm.hibernate.EntityManager;
import com.chain.base.orm.hibernate.HibernateDao;
import com.chain.base.utils.StringUtils;
import com.chain.ens.entity.doc.Doc;
import com.chain.ens.listener.LuceneContext;
import com.chain.ens.lucene.AbstractContent;
import com.chain.ens.lucene.DocEnum;
import com.chain.ens.lucene.FileField;
import com.chain.ens.lucene.KeywordFilter;
import com.chain.ens.web.vo.DocIndexField;

/**
 * 全文检索Manager
 * @author Chain
 *
 */
@Service
public class FtrManager extends EntityManager<Doc, Long> {
	private Logger logger = Logger.getLogger(FtrManager.class);
	private HibernateDao<Doc, Long> docDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		docDao = new HibernateDao<Doc, Long>(sessionFactory, Doc.class);
	}
	
	protected HibernateDao<Doc, Long> getEntityDao() {
		return docDao;
	}
	
	/**
	 * 索引增加
	 * @param d  需要添加所引的文档
	 * @param insertDb  是否需要添加到数据库
	 */
	public void saveIndex(Doc d, boolean insertDb){
		try {
			if(insertDb){
//			TempIndex ti = new TempIndex(dif.getTarget());
			}
			List<Doc> docs = new ArrayList<Doc>();
			docs.add(d);
			saveOrUpdateIndex(docs,true);
		} catch (Exception e) {
		}
	}
	
	/**
	 *  多个文档索引增加
	 * @param docs  文档
	 * @param insertDb  是否需要添加到数据库
	 */
	public void saveIndex(List<Doc> docs, boolean insertDb){
		try {
			if(insertDb){
//			TempIndex ti = new TempIndex(dif.getTarget());
			}
			saveOrUpdateIndex(docs,true);
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 多个文档索引库更新
	 * @param docs  文档
	 */
	public void updateIndex(List<Doc> docs){
		if(docs == null || docs.isEmpty())
			return ;
		try{
			this.saveOrUpdateIndex(docs, false);
		}catch(Exception e){
			///logger.info("创建索引失败" + e.getCause().getMessage());
		}
	}
	
	/**
	 * 单个文档索引库更新
	 * @param doc  文档
	 */
	public void updateIndex(Doc doc) {
		if(doc == null)
			return ;
		try{
			List<Doc> docs = new ArrayList<Doc>();
			docs.add(doc);
			this.saveOrUpdateIndex(docs, false);
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 单个文档索引更新
	 * @param docId  文档id
	 */
	public void updateIndex(Long docId){
		Doc doc = docDao.get(docId);
		updateIndex(doc);
	}
	
	
	/**
	 * 删除索引
	 * 
	 * @param ids 文档编号
	 * @param isUpdate 索引是否更新
	 */
	public void deleteIndex(Long[] ids){
		if(ids == null || ids.length <= 0)
			return ;
		try{
			NRTManager nrtMgr = LuceneContext.getInstance().getNRTManager();
			for(Long str : ids){
				nrtMgr.deleteDocuments(new Term(DocEnum.id.name(),str + ""));
				logger.info("ID：" +str + " 索引删除成功！");
			}
		}catch(Exception e){
			//logger.warn("索引库删除时出现异常，原因是：" + e.getMessage());
		}
	}
	
	
	/**
	 * 删除索引
	 * 
	 * @param id 文档编号
	 * @param isUpdate 索引是否更新
	 */
	public void deleteIndex(Long id){
		if(id == null)return;
		try{
			NRTManager nrtMgr = LuceneContext.getInstance().getNRTManager();
			nrtMgr.deleteDocuments(new Term(DocEnum.id.name(),id + ""));
			logger.info("ID：" +id + " 索引删除成功！");
		}catch(Exception e){
			logger.warn("索引库删除时出现异常，原因是：" + e.getMessage());
		}
	}
	
	/**
	 * 增加或更新索引
	 */
	public Set<File> saveOrUpdateIndex(List<Doc> docs ,boolean save)
			throws Exception {
		NRTManager nrtMgr = LuceneContext.getInstance().getNRTManager();
		for(Doc doc : docs){
			try{
				Document document = createDocument(setValues(doc));
				if(save){
					nrtMgr.addDocument(document);
					logger.info(doc.getName() + "文件索引创建成功");
				}else{
					nrtMgr.updateDocument(new Term(DocEnum.id.name(),doc.getId() + ""),document);
					logger.info(doc.getName() + "文件索引更新成功");
				}
			}catch(Exception e){
				logger.info(doc.getName() + (save? "创建":"更新") + "失败，原因：" + e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * 全文解索设置
	 */
	private Map<Enum<?>, FileField> setValues(Doc doc) throws Exception {

		Map<Enum<?>, FileField> params = new HashMap<Enum<?>, FileField>();
		// 设置文档信息
		if (doc != null) {
			DocEnum docid = DocEnum.id;
			docid.setValue(doc.getId()+"");
			params.put(docid, docid.getF());
			
			DocEnum title = DocEnum.name;
			title.setValue(doc.getName());
			params.put(title, title.getF());
			
			
			DocEnum createTime = DocEnum.createTimeStr;
			createTime.setValue(doc.getCreateTime()+"");
			params.put(createTime, createTime.getF());

			DocEnum format = DocEnum.format;
			format.setValue("."+doc.getFormat());//前面加上一点为了更好的检索
			params.put(format, format.getF());
			
			
			//内容
			DocEnum content = DocEnum.content;
			File file = new File(LuceneContext.CONRENT_PATH,doc.getId() +  ".txt");
			String contents = "";
			if(!file.exists()){//如果文件不存在，重构索引的时候，提取的文本可能被删除
				//下载文件
				
				//提取内容
			}
			if(file.exists() && file.canRead()){
				contents = AbstractContent.extractFullText(file);
			}
			content.setValue(contents);
			params.put(content, content.getF());
			
			//文体保存路径
			DocEnum cpath = DocEnum.cpath;
			cpath.setValue(doc.getId() + ".txt");
			params.put(cpath, cpath.getF());
		
		}
		return params;
	}
	
	/**
	 * 创建lucene Document
	 * @param maps
	 * @return
	 * @throws Exception
	 */
	private Document createDocument( Map<Enum<?>, FileField> maps) throws Exception{
		Document doc = new Document();
		for(FileField f : maps.values()){
			Field field = new Field(f.getName(), f.getContent(),
					f.isStored() ? Field.Store.YES : Field.Store.NO,
					f.isIndexed() ? Field.Index.ANALYZED : Field.Index.NOT_ANALYZED,
					f.isHasPosition() ? Field.TermVector.WITH_POSITIONS_OFFSETS : Field.TermVector.NO);
			field.setBoost(f.getBoost());
			doc.add(field);
		}
		return doc;
	}

	
	/*****************************查询***********************************************************/
	public Page<DocIndexField> queryDocs(String queryStr, int pageNo, int rows,boolean highLighter) throws Exception{
		if(StringUtils.isBlank(queryStr))
			return null;
		
		Page<DocIndexField> page = new Page<DocIndexField>(rows);
		page.setPageNo(pageNo);
		
		Map<Enum<?>, Object> params= new HashMap<Enum<?>, Object>();
		Map<Enum<?>, Object> filters= new HashMap<Enum<?>, Object>();
		
		
		params.put(DocEnum.content, queryStr);
		params.put(DocEnum.name, queryStr);
		//filters.put(DocEnum.blong,userManager.getCurrentUser().getId());
		//filters.put(DocEnum.isPublic, "1");
		
		List<Map<Enum<?>, Object>> docList = searchDocument(params,
				filters, rows, pageNo, highLighter,page);

		List<DocIndexField> list = new ArrayList<DocIndexField>();
		for (Map<Enum<?>, Object> docField : docList) {

			DocIndexField doc = new DocIndexField();

			Set<Enum<?>> keys = docField.keySet();

			for (Enum<?> e : keys) {
				if (e != null) {
					try {
						BeanUtils.copyProperty(doc, e.name(), docField.get(e));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			list.add(doc);
		}
		page.setResult(list);
		return page;
	} 
	
	@SuppressWarnings("unchecked")
	public List<Map<Enum<?>, Object>> searchDocument(Map<?, Object> params,
			Map<?, Object> filters, int pageSize, int currentPage,
			boolean highLighter,Page page) throws Exception{
		List<Map<Enum<?>, Object>> docList = new ArrayList<Map<Enum<?>, Object>>();
		
		int contenLen = 400;
		BooleanQuery bQuery = groupQueryTerm(params);
		if (bQuery.clauses().size() == 0)
			return docList;
		logger.info("全部搜索词汇: " + bQuery.toString());

		BooleanQuery bfQuery = groupFilterTerm(filters);
		QueryWrapperFilter filter = new QueryWrapperFilter(bfQuery);
		logger.info("全部过滤词汇: " + bfQuery.toString());
		
		TopScoreDocCollector collector = TopScoreDocCollector.create(currentPage* pageSize, false);
		
		IndexSearcher searcher = LuceneContext.getInstance().getSearcher();
		
		if (bfQuery.clauses().size() != 0){
			searcher.search(bQuery, filter, collector);
		}else{
			searcher.search(bQuery, collector);
		}

		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		page.setTotalCount(Long.valueOf(collector.getTotalHits()));
	    logger.info(page.getTotalCount() + " total matching documents");
		if (page.getTotalCount() == 0)
			return docList;

		int start = (currentPage - 1) * pageSize;
		int end = Math.min(hits.length, start + pageSize);

		// 用这个进行高亮显示，默认是<b>..</b>
		// 用这个指定<read>..</read>
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
				"<b><font color='red'>", "</font></b>");
		QueryScorer queryScorer = new QueryScorer(bQuery);
		// 构造高亮
		// 指定高亮的格式
		// 指定查询评分
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
				queryScorer);
		// 这个一般等于你要返回的，高亮的数据长度
		// 如果太小，则只有数据的开始部分被解析并高亮，且返回的数据也少
		// 太大，有时太浪费了。
		highlighter.setTextFragmenter(new SimpleSpanFragmenter(queryScorer, contenLen));
		for (int i = start; i < end; i++) {
			Document doc = searcher.doc(hits[i].doc);
			logger.info(doc.get("name") + " 匹配分数: " + hits[i].score);
			Map<Enum<?>, Object> fieldMap = new HashMap<Enum<?>, Object>();
			List<Fieldable> fieldList = doc.getFields();
			for (Fieldable field : fieldList) {
				String name = field.name();
				String content = doc.get(name);
				Enum<?> filedEnum = DocEnum.getValue(name);
				//读取内容
				if (filedEnum != null) {
					DocEnum fileF = (DocEnum) filedEnum;
					if (fileF.equals(DocEnum.cpath)) {
						String cFile = doc.get(DocEnum.cpath.name());
						File file = new File(LuceneContext.CONRENT_PATH,cFile);
						if (file.exists()) {
							FileReader ir = new FileReader(file);
							StringBuffer sb = new StringBuffer();
							int r = -1;
							char[] buf = new char[1024];
							while ((r = ir.read(buf)) != -1)
								sb.append(buf, 0, r);
							ir.close();
							content = sb.toString();
						} else {
							content = "";
						}
						filedEnum = DocEnum.content;
						fileF = DocEnum.content;
					}
					if (highLighter && fileF.f.isHighLighter() && content != null && content.trim().length() > 0) {
						content = highLigher(highlighter, hits[i].doc, content,contenLen).toString();
					} else if (fileF.equals(DocEnum.content)) {
						content = content.substring(0, Math.min(content.length(), contenLen));
					}
				} else {
					filedEnum = DocEnum.getValue(name);
					if (filedEnum != null) {
						DocEnum docF = (DocEnum) filedEnum;
						if (highLighter && docF.getF().isHighLighter()) {
							content = highLigher(highlighter, hits[i].doc,content, contenLen).toString();
						} else if (docF.equals(DocEnum.name)) {
							content = content.substring(0, Math.min(content.length(), contenLen));
						}
					} else {
						filedEnum = DocEnum.getValue(name);

						if (filedEnum != null) {
							DocEnum docF = (DocEnum) filedEnum;
							if (highLighter && docF.getF().isHighLighter()) {
								content = highLigher(highlighter, hits[i].doc,content, contenLen).toString();
							} else {
								content = content.substring(0, Math.min(content.length(), contenLen));
							}
						}
					}
				}
				fieldMap.put(filedEnum, content);
			}
			docList.add(fieldMap);
		}
		LuceneContext.getInstance().releaseSearcher(searcher);
		return docList;
	}
	
	/**
	 * 高亮处理
	 * 
	 * @param highlighter
	 * @param doc
	 * @param content
	 * @param limit
	 * @return
	 * @throws IOException
	 * @throws InvalidTokenOffsetsException
	 */
	@SuppressWarnings("all")
	private StringBuffer highLigher(Highlighter highlighter, int doc,
			String content, int limit) throws IOException,
			InvalidTokenOffsetsException {
		if (content != null) {
			StringBuffer sb = new StringBuffer();
			TokenStream tokenStream = LuceneContext.getInstance().getAnalyzer().tokenStream(DocEnum.content.name(),
					new StringReader(content));
			String str = highlighter.getBestFragment(tokenStream, content);
			sb.append(str);
			int length = Math.min(limit, content.length());
			return str == null ? new StringBuffer(content.substring(0, length)) : sb;
		}
		return null;
	}

	
	/**
	 * 组装过滤条件
	 * @param filters
	 * @return
	 */
	private BooleanQuery groupFilterTerm(Map<?, Object> filters) {
		//组装过滤条件
		BooleanQuery bfQuery = new BooleanQuery(); // 组合查询
		for (Object filter : filters.keySet()) {
			String qStr = (String) filters.get(filter);
			if (filter instanceof DocEnum ) {
				Enum<?> e = (Enum<?>) filter;
				Term term = new Term(e.name(), qStr);
				Query query = new TermQuery(term);
				bfQuery.add(query, Occur.SHOULD);
				logger.info("过滤词汇: " + query.toString());
			} else if (filter instanceof String) {
				Term term = new Term(filter.toString(), qStr);
				Query query = new TermQuery(term);
				bfQuery.add(query, Occur.SHOULD);
				logger.info("过滤词汇: " + query.toString());
			} else {
				logger.info("无法判断该条件的类型： " + filter.getClass().getName() + " : 过滤词汇: " + qStr);
			}
		}
		return bfQuery;
	}

	/**
	 * 组装查询条件
	 * @param params
	 * @return
	 */
	private BooleanQuery groupQueryTerm(Map<?, Object> params)
			throws org.apache.lucene.queryParser.ParseException,
			IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException {
		// 组件查询条件
		BooleanQuery bQuery = new BooleanQuery(); 
		Set<?> keys = params.keySet();
		for (Object param : keys) {
			String qStr = (String) params.get(param);
			if (qStr == null || qStr.length() == 0) continue;
			try {
				// 去掉特殊字符
				qStr = KeywordFilter.filterKeyword(qStr);
				Query preQuery = new QueryParser(LuceneContext.getInstance().getVersion(), " ",
						LuceneContext.getInstance().getAnalyzer()).parse(qStr);
				qStr = preQuery.toString().replaceAll("\"", "").replaceAll(":","");
			} catch (ParseException ex) {
				Logger.getLogger(FtrManager.class).warn(ex);
				continue;
			}
			
			if (qStr.length() == 0) {
				continue;
			} else if (param.equals(LuceneContext.TYPE.whole)) {
				doWholeQuery(bQuery, qStr);
			} else if (param instanceof DocEnum) {
				Enum<?> e = (Enum<?>) param;
				FileField f = DocEnum.getValue(e.name()).getF();
				Query fieldQuery = new QueryParser(LuceneContext.getInstance().getVersion(), e.name(),
						LuceneContext.getInstance().getAnalyzer()).parse(qStr);
				bQuery.add(fieldQuery, f.getOccurInt() == 3 ? Occur.SHOULD : f
						.getOccurInt() == 2 ? Occur.MUST_NOT : Occur.MUST);
				logger.info("搜索词汇: " + fieldQuery.toString());
			} else if (param instanceof String) {
				Query fieldQuery = new QueryParser(LuceneContext.getInstance().getVersion(), param
						.toString(), LuceneContext.getInstance().getAnalyzer()).parse(qStr);
				bQuery.add(fieldQuery, Occur.SHOULD);
				logger.info("搜索词汇: " + fieldQuery.toString());
			} else {
				logger.info("无法判断该条件的类型： " + param.getClass().getName()+ " : 搜索词汇: " + qStr);
			}
		}
		return bQuery;
	}
	
	
	/**
	 * 全字段查询
	 * 
	 * @param bQuery
	 * @param qStr
	 * @throws ParseException
	 * @throws ParseException 
	 */
	private void doWholeQuery(BooleanQuery bQuery, String qStr)
			throws ParseException {

		// 遍历扩展字段
		for (DocEnum key : DocEnum.values()) {
			Query fieldQuery = new QueryParser(LuceneContext.getInstance().getVersion(), key.name(),
					LuceneContext.getInstance().getAnalyzer()).parse(qStr);
			bQuery.add(fieldQuery, Occur.SHOULD);
			logger.info("搜索词汇: " + fieldQuery.toString());
		}
	}
}
