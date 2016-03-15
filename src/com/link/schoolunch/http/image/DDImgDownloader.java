package com.link.schoolunch.http.image;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class DDImgDownloader extends Thread {
	
	private String url;
	private String fileName;
	private String rootFolder;
	private Handler handler;
	
	public DDImgDownloader(String url, Handler handler, String fileName, String rootFolder){
		this.url = url;
		this.handler = handler;
		this.fileName = fileName;
		this.rootFolder = rootFolder;
		this.start();
	}
	
	public DDImgDownloader(String url, Handler handler, String fileName){
		this.url = url;
		this.handler = handler;
		this.fileName = fileName;
		this.rootFolder = null;
		this.start();
	}
	
	@Override
	public void run(){
		URL imageURL = null;
		try{
			imageURL = new URL(url);
		}catch(MalformedURLException e1){
			e1.printStackTrace();
		}
		
		BitmapDrawable image = null;
		
		try{
			HttpURLConnection hp = (HttpURLConnection) imageURL.openConnection();
			image = (BitmapDrawable) BitmapDrawable.createFromStream( hp.getInputStream(), null );
			hp.disconnect();
			
			Message message = handler.obtainMessage();
			message.obj = this.fileName;
			Bundle bundle = new Bundle();
			bundle.putSerializable("image", new DDSerializableBitmap(image.getBitmap()));
			message.setData(bundle);
			handler.handleMessage(message);
			
			if(rootFolder!=null){
				File imgFile = new File(rootFolder + "/" + this.fileName);
				if(imgFile.exists()){
					imgFile.delete();
				}
				imgFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(imgFile);
				image.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);
			}			
		}catch(Exception e){
			Message message = handler.obtainMessage();
			message.obj = this.fileName;
			handler.handleMessage(message);
			e.printStackTrace();
		}
	}
}
