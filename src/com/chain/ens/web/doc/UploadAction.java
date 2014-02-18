package com.chain.ens.web.doc;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.chain.base.orm.hibernate.EntityManager;
import com.chain.base.utils.DateUtil;
import com.chain.base.utils.SysConstants;
import com.chain.base.web.struts2.StrutsAction;
import com.chain.base.web.struts2.utils.Struts2Utils;
import com.chain.ens.entity.base.User;
import com.chain.ens.entity.doc.Doc;
import com.chain.ens.entity.doc.state.DeleteState;
import com.chain.ens.entity.doc.state.DocType;
import com.chain.ens.listener.LuceneContext;
import com.chain.ens.lucene.AbstractContent;
import com.chain.ens.service.doc.DocManager;
import com.chain.ens.service.doc.FtrManager;
import com.chain.ens.utils.AESUtils;
import com.chain.ens.utils.AppUtils;
import com.chain.ens.utils.EnsConstants;
import com.chain.ens.utils.FilePathUtils;
import com.chain.ens.utils.FileUtils;
import com.chain.ens.utils.PdfUtil;
import com.chain.ens.utils.PropertyUtil;


/**
 * 文件上传
 *   文件上传流程
 *   1.每点击一次添加文件按钮添加的文件称为一批文件，
 *           文件信息（名称，存储路径temp/attached/uuid）存储在session中。
 *   2.一批文件上传完成后，调用commit方法。
 *   3.从session中取出所有文件，清空session。
 *   4.文件加密，加密后文件转存到temp/encrypt（文件的uuid不变）
 *   5.逐个上传到FTP，并将加密文件移动到待备份目录
 *   6.保存文件信息到数据库
 *   7.移动加密文件到待备份目录temp/backup
 *   8.文本提取,存储到index目录，等待FTP备份
 *   9.创建索引
 *   10.删除源文件
 * 
 * @author taosq
 *
 */
public class UploadAction extends StrutsAction<Doc> {

	private static final long serialVersionUID = -9045128987171636125L;
	private File file;  
    private String fileFileName;  
    private String createDate;//创建时间
    private String modifyDate;//创建时间
    private Long folder;//文件夹
    private String flag;//标识，第一次提交的时候生成，以后，每次都传入，用于标识上传的同一批文件

    private static String imgSuffix = "";
    static{
    	imgSuffix =  PropertyUtil.readValue("imgSuffix");
    }
    @Autowired
	private DocManager docManager;
    
    @Autowired
    private FtrManager ftrManager;

    @Override
	public EntityManager<Doc, Long> getEntityManager() {
		return docManager;
	}
	
	
	/**
	 * @author TSQ
	 * 上传的文件先保存在session中，一批文件中不允许重复的文件，否则覆盖
	 *  
	 * @return
	 */
	public String upLoad(){
		File tempFile = new File(EnsConstants.getUploadDirectory(),UUID.randomUUID().toString());
		file.renameTo(tempFile);
		Doc doc = new Doc();
		doc.setName(fileFileName);
		doc.setPath(tempFile.getAbsolutePath());
		doc.setCreateTime(new Date());
		UploadQueue.enQuene(Struts2Utils.getSession(), doc);
		return null;
	}
	
	
	/**
	 * 
	 * 提交session中的文件
	 * 
	 *    返回值约定
	 *    status : 1  sucess;
	 *             2  fail
	 *             3  ftp连接出错
	 *    sucessList: 成功队列
	 *    failList :  失败队列
	 *    
	 */
	public void submit(){
		List<Doc> quene = UploadQueue.deQuene(Struts2Utils.getSession());
		Map<Object,Object> result = new HashMap<Object,Object>();
		
		if(quene != null && !quene.isEmpty()){
			List<String> sucessQuene = new ArrayList<String>();
			List<String> failQuene = new ArrayList<String>();
			File encryptFile = null;
			File backupFile = null;
			File indexFile = null;
			File contentFile = null;
				
			Doc parentDoc = folder == null ? null : docManager.getById(folder);
				
			User currentUser = AppUtils.getUser();
			
			/**
			 * 每个文件的处理失败，不能影响下一个
			 */
			for(Doc doc : quene){
				File src = new File(doc.getPath());
				try{
					//加密
					encryptFile = new File(getFilePath(FilePathUtils.getEncryptDir()),src.getName());
					AESUtils.encrypt(src, encryptFile);
					
					//加密后的文件保存 可以考虑用ftp实现
					doc.setPath(encryptFile.getAbsolutePath());//把文件的路径改为加密后的存储路径
					doc.setFormat(FileUtils.getFileSuffix(doc.getName()));
					doc.setFileSize(src.length());
					doc.setOwn(currentUser);//上传用户
					//doc.setFlag(flag);
					doc.setParent(parentDoc);
					doc.setDocType(DocType.file.getValue());
					doc.setDeleteState(DeleteState.undeleted.getValue());
					if(parentDoc!=null){
						doc.setDir(parentDoc.getDir() + "/" + parentDoc.getName() + "#" + parentDoc.getId());
					}else{
						doc.setDir("/");
					}
					docManager.save(doc);
					
					//加密文件移动到待备份目录
						//暂时不做备份。如需备份则需要用ftp实现
//					backupFile = new File(FilePathUtils.getBackupDir(),doc.getId()+"");
//					encryptFile.renameTo(backupFile);
					
					//提取内容
					indexFile = new File(getFilePath(FilePathUtils.getContentDir()),doc.getId() + ".txt");
					AbstractContent.extractFullText(src,indexFile);
					
					//复制索引文件到索引库
					contentFile = new File(LuceneContext.CONRENT_PATH,indexFile.getName());
					FileUtils.copyFile(indexFile, contentFile);
					
					try{
					//创建索引
						ftrManager.saveIndex(doc,false);
					}catch(Exception e){
						logger.info(e.getMessage());
					}
					sucessQuene.add(doc.getName());
					if(PdfUtil.isNeedConvert(doc.getFormat(), doc.getFileSize())){
						File need2PdfFile = new File(FilePathUtils.getPdfDir(), doc.getId()+"");
//						boolean b = src.renameTo(need2PdfFile);
						FileUtils.copyFile(src, need2PdfFile);
					}
					if(imgSuffix.indexOf(doc.getFormat()) != -1){
						File f = new File(getFilePath(FilePathUtils.getViewDir()),
									doc.getId() + "." + doc.getFormat());
						FileUtils.copyFile(src, f);
						doc.setPdfPath(f.getAbsolutePath());
						docManager.update(doc);
					}
					src.delete();
				}catch(Exception e){
					//上传失败处理
					e.printStackTrace();
					failQuene.add(doc.getName());
					src.delete();
					if(encryptFile != null) encryptFile.delete();
					if(backupFile != null) backupFile.delete();
					if(indexFile != null) indexFile.delete();
					if(contentFile != null)contentFile.delete();
					logger.info(doc.getName() + "上传失败, 原因" + e.getMessage());
				}
				//操作日志
			}
			result.put("code",1);
			result.put("successList", sucessQuene);
			result.put("flag", flag);
			result.put("failList", failQuene);
		}else{
			result.put("code", 2);
		}
		Struts2Utils.renderJson(result, "encoding:UTF-8");
	}
	
	private String getFilePath(String parentPath){
		String encryptDir = parentPath + "/" + DateUtil.curDate();
		File edf = new File(encryptDir);
		if(!edf.exists())
			edf.mkdir();
		return edf.getAbsolutePath();
	}
	
	
	public File getFile() {  
        return file;  
    }  
  
    public void setFile(File file) {  
        this.file = file;  
    }  
  
    public String getFileFileName() {  
        return fileFileName;  
    }  
  
    public void setFileFileName(String fileFileName) {  
        this.fileFileName = fileFileName;  
    }


	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public String getModifyDate() {
		return modifyDate;
	}


	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}


	public Long getFolder() {
		return folder;
	}


	public void setFolder(Long folder) {
		this.folder = folder;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}

    
}
