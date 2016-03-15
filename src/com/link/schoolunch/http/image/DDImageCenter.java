package com.link.schoolunch.http.image;

//import com.baidu.android.pushservice.RegistrationReceiver;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.link.schoolunch.util.Utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 闁告娲戠欢銉ノ熼垾宕囩
 * @author Yinli
 *
 */

public class DDImageCenter {
	public static final int DDIMAGECENTER_MESSAGE = 0x10010;
	public static final int GET_IMAGE_SUCCESS = 0x11000;
	public static final int GET_IMAGE_FAILED = 0x11001;
	
	private static DDImageCenter m_inst;
	
	public static DDImageCenter getInstance(){
		if(m_inst == null){
			m_inst = new DDImageCenter();
		}
		return m_inst;
	}
	
	private Map<String, SoftReference<Bitmap> > caches;
	
	public void setCaches(Map<String, SoftReference<Bitmap>> n_caches){
		this.caches = n_caches;
	}
	
	public Map<String, SoftReference<Bitmap> > getCaches(){
		return this.caches;
	}
	
	private DDImageHandler m_handler;
	
	private Map<String, List<Handler>> handlerList;
	
	public void setImageViewList(Map<String, List<Handler>> list){
		this.handlerList = list;
	}
	
	public Map<String, List<Handler>> getImageViewList(){
		return this.handlerList;
	}

	private boolean useExternalStorage;
	
	public boolean getUseExternalStorage(){
		return this.useExternalStorage;
	}
	
	private String imageRootFolderPath;
		
	private DDImageCenter(){
		this.useExternalStorage = false;
		this.imageRootFolderPath = Utils.IMG_PATH;
		this.caches = new HashMap<String, SoftReference<Bitmap>>();
		this.handlerList = new HashMap<String, List<Handler>>();
		this.m_handler = new DDImageHandler();
	}
	
	public Bitmap getImage(Handler imgHandler, String imgName, String imgFolder, String imgURL){
		if(imgHandler == null||imgName==null||imgURL==null||imgFolder==null){
			return defaultImage;
		}
		
		String key = imgFolder +"/"+imgName;
		if(caches.containsKey(key)){
			SoftReference<Bitmap> bit = caches.get(key);
			if(bit!=null && bit.get()!=null){
				return bit.get();
			}
			else{
				Log.v("ImageCenter", "image has been collected by gc");
				caches.remove(key);
			}
		}

		if(this.imageRootFolderPath != null){
			File folder = new File(imageRootFolderPath +"/" +imgFolder+"/");
			if(!folder.exists()){
				folder.mkdirs();
			}
			File imgFile = new File(imageRootFolderPath +"/" +imgFolder+"/"+ imgName);
			if(imgFile.exists()){
				Bitmap file_bit = null;
				try{
					file_bit = BitmapFactory.decodeFile(imgFile.getPath());
				}
				catch(Exception e){
					e.printStackTrace();
				}
				if(file_bit != null){
					SoftReference<Bitmap> bit = new SoftReference<Bitmap>(file_bit);
					this.caches.put(key, bit);
					return bit.get();
				}
				else{
					imgFile.delete();
				}
			}
		}

		if(this.handlerList.containsKey(key)){
			handlerList.get(key).add(imgHandler);
		}else{
			List<Handler> img_list = new ArrayList<Handler>();
			img_list.add(imgHandler);
			this.handlerList.put(key, img_list);
			new DDImgDownloader(imgURL, m_handler, key, imageRootFolderPath);	
		}		
		
		return null;
	}
	
	public void removeHandlerFromList(Handler imageHandler){
		if(imageHandler == null){
			return;
		}
		Collection< List<Handler>> collection = handlerList.values();
		Iterator< List<Handler>> it = collection.iterator();
		while(it.hasNext()){
			List<Handler> view_list = it.next();
			if(null != view_list){
				view_list.remove(imageHandler);
			}			
		}
	}
	
	public void removeHandlerFromList(Handler imageHandler, String imgName, String imgFolder){
		if(imageHandler == null){
			return;
		}
		List<Handler> img_list= this.handlerList.get(imgFolder+"/"+imgName);
		img_list.remove(imageHandler);
	}
	
	
	/**
	 * 阎犱礁澧介悿鍡涘炊閸撗冾暬阎庢稒锚閸嫔秹寮介崷颛熺獥鐟滃府鎷?	 * 闁兼眹鍎冲ú浼村冀閸ャ剑鐎ù钟烘硾閵囨瑦绋夊锻憼闁革负锷岄崹顖炲礆濞戞绱﹂柡鍌氭矗濞嗐垺寰勯敓锟? * 闁兼眹鍎扮粭澶屾暜鐏炵偓绠块悗娑櫭崑宥夋晬瀹€鍐惧歼阎忓繐妫楀顒勫极阉峰矈鍟庣纾鍐惧栀镨愮劆ull 
	 * @param absolutelyPath
	 */
	public void setImageRootFolder(String absolutelyPath){
		Log.v("ImageCenter", absolutelyPath);
		if(absolutelyPath == null){
			this.useExternalStorage = false;
			this.imageRootFolderPath = null;
		}
		else{
			boolean createDir  = false;
			this.useExternalStorage = true;
			this.imageRootFolderPath = absolutelyPath + "/";
			File rootDir = new File(absolutelyPath);
			if(!rootDir.exists()){
				createDir = rootDir.mkdirs();
			}
			else if(rootDir.isFile()){
				rootDir.delete();
				createDir = rootDir.mkdirs();
			}
			if(!rootDir.exists()&&!createDir){
				this.useExternalStorage = false;
				this.imageRootFolderPath = null;
			}
		}
	}
	
	/**
	 * 闁告帞濞€濞呭孩娼婚崶銊﹀焸闁搞儱澧芥晶锟?	 * 闁哄秷颤夊畵渚€宕堕崜褍顣婚柡鍫嫹阉绋夐敓绛嫔仹濞ｅ浂鍠楅弫镶╂崉濠靛牜鐎查柣婊勬缈濠€颜堟儍閸曨剚顦ч梻鍌涙尦濡潡姊鹃敓锟? * @param time
	 */
	public void clearImageByTimeInterval(int days){
		if(this.imageRootFolderPath == null){
			Log.e("DDImageCenter ERROR", "imageRootFolder is null");
			return;
		}
		File rootFolder = new File(this.imageRootFolderPath);
		if( ! rootFolder.exists()){
			Log.e("DDImageCenter ERROR", "imageRootFolder does not exist");
			return;
		}
		checkTime(rootFolder, days);
	}
	
	private void checkTime(File dir, int days){
		File[] files = dir.listFiles();
		long now = new Date().getTime();
		long interval_time = days * 24 * 3600 * 1000;
		for(int i = 0; i< files.length; i++){
			if(files[i].isFile()){
				if(now - files[i].lastModified() > interval_time){
					files[i].delete();
				}
			}
			else{
				checkTime(files[i], days);
			}			
		}
		
	}
	
	/**
	 * 阎犱礁澧介悿鍡涘礉阎樼儤绁伴柡鍫簻阎ｎ剟骞嬮幇颛烨槯闁汇刿瀚崕妤呭疾椤栨碍绂堥柣妤嬫嫹
	 * 闁兼眹鍎扮粭澶屾媪閸撗呮瀭婵縿链濋妴宥夊礆濞嗘垶绁查幖瀛樻焕鐏忣垶宕洪悢锻娾枖缂侊拷娅ｉ埞镙儌阌燂拷	 * 濠㈣埖纰嶉镶╂媪閸撗呮瀭濞村寸淇洪々顐︽儎閺嵮冩枾闁哄墙顦卞▓鎴﹀炊阉冨壖
	 */
	private Bitmap defaultImage;
	
	public void setDefaultImage(Bitmap bitmap){
		this.defaultImage = bitmap;
	}
	
	public Bitmap getDefaultImage(){
		return this.defaultImage;
	}
	
	/**
	 * 闁告垼濮ら弪镡枫€掗崨顖楁曦缂傚倹鎸搁悺锟?	 */
	public void clearCache(){
		Collection<SoftReference<Bitmap>> col = caches.values();
		Iterator<SoftReference<Bitmap>> it = col.iterator();
		while(it.hasNext()){
			Bitmap bit = it.next().get();
			bit.recycle();
		}
		caches.clear();
	}
	
	/**
	 * 闁告垼濮ら弪镡枫€掗崨顖楁曦闁哄牜鍓椤﹢瀛樼┍濠靛棛鎽犻柛銉ュ⒔婢ф牠寮崶锔筋偨
	 */
	public void clearLocalFile(){
		deleteDir(this.imageRootFolderPath);
	}
	
	private void deleteDir(String path){
		File dir = new File(path);
		if(dir.exists()){
			File[] files = dir.listFiles();
			if(files!=null){
				for(int i=0; i<files.length;i++){
					if(files[i].isFile()){
						files[i].delete();
					}
					if(files[i].isDirectory()){
						deleteDir(files[i].getAbsolutePath());
					}
				}
			}
		}
		dir.delete();
	}
	
	
	/**
	 * Handler闁挎冻鎷烽悘蹇撴濞存﹢镇ч崶褍鐎婚柛娆欐嫹
	 * @author Yinli
	 *
	 */	
	@SuppressLint("HandlerLeak")
	public class DDImageHandler extends Handler{
				
		@Override
		public void handleMessage(Message msg){
			Log.v("handle message", "");
			String key = msg.obj.toString();
			final List<Handler> iList = DDImageCenter.this.handlerList.get(key);
			if(msg.getData()!=null){
				DDSerializableBitmap ddBitmap =  (DDSerializableBitmap)msg.getData().getSerializable("image");
				if(ddBitmap== null){
					for( int i=0; i< iList.size();i++ ){
						Handler iHandler =  iList.get(i);
						Message message = iHandler.obtainMessage();
						message.what = DDImageCenter.DDIMAGECENTER_MESSAGE;
						message.arg1 = DDImageCenter.GET_IMAGE_FAILED;
						iHandler.sendMessage(message);
					}
					iList.clear();
					DDImageCenter.this.handlerList.remove(key);
					return;
				}
				Bitmap bit = ddBitmap.getImage();
				caches.put(key, new SoftReference<Bitmap>(bit));
				for( int i=0; i< iList.size();i++ ){
					Handler iHandler =  iList.get(i);
					Message message = iHandler.obtainMessage();
					message.copyFrom(msg);
					message.what = DDImageCenter.DDIMAGECENTER_MESSAGE;
					message.arg1 = DDImageCenter.GET_IMAGE_SUCCESS;
					iHandler.sendMessage(message);
				}	
				
			}			
			iList.clear();
			DDImageCenter.this.handlerList.remove(key);
		}
	}
}
