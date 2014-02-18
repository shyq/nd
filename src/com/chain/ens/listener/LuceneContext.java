package com.chain.ens.listener;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.SearcherWarmer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.chain.ens.utils.PropertyUtil;
import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;


public class LuceneContext {
	private static Logger logger = Logger.getLogger(LuceneContext.class);
	private static LuceneContext instance;
	private static String INDEX_PATH ;
	public static String CONRENT_PATH;
	private static IndexWriter writer;
	private static Analyzer analyzer;
	private static Version version;
	private static NRTManager nrtMgr;
	private static SearcherManager mgr;
	private static Directory directory;
	private LuceneContext(){}
	
	static{
		INDEX_PATH = PropertyUtil.readValue("indexDir");
		CONRENT_PATH = PropertyUtil.readValue("ContentDir");
	}
	
	public static LuceneContext getInstance() {
		if(instance==null){
			logger.info("lucene 初始化");
			init();
			instance = new LuceneContext();
		}
		return instance;
	}
	
	private static void init() {
		try {
			directory = FSDirectory.open(new File(INDEX_PATH));
			version = Version.LUCENE_35;
			analyzer = new MMSegAnalyzer();
			writer = new IndexWriter(directory,new IndexWriterConfig(version,analyzer));
			nrtMgr = new NRTManager(writer, new SearcherWarmer() {
				public void warm(IndexSearcher arg0) throws IOException {
					logger.info("reopen index");
				}
			});
			mgr = nrtMgr.getSearcherManager(true);
			
			NRTManagerReopenThread reopenThread = new NRTManagerReopenThread(nrtMgr, 5.0,0.025);
			reopenThread.setName("NRTManager reopen thread");
			reopenThread.setDaemon(true);
			reopenThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public IndexSearcher getSearcher() {
		return mgr.acquire();
	}
	
	public void releaseSearcher(IndexSearcher searcher) {
		try {
			mgr.release(searcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void commitIndex() {
		try {
			writer.commit();
			writer.forceMerge(10);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NRTManager getNRTManager() {
		return nrtMgr;
	}

	public void close(){
		if(writer!=null){
			try {
				writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public Version getVersion() {
		return version;
	}
	
	public Analyzer getAnalyzer() {
		return analyzer;
	}
	
	public static enum TYPE {
		whole, field
	};
}
