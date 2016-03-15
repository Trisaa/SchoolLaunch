package com.link.schoolunch.http.image;


import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoaderHandler extends Handler{

	private DDImageViewList viewMap;
	
	
	public ImageLoaderHandler(){
		viewMap = new DDImageViewList();
	}
	
	public DDImageViewList getImageViewMap(){
		return viewMap;
	}
	@Override
	public void handleMessage(Message msg){
		if(msg.what == DDImageCenter.DDIMAGECENTER_MESSAGE){
			if(msg.arg1 == DDImageCenter.GET_IMAGE_SUCCESS){
				String key = msg.obj.toString();
				Log.v("KEY", key);
				DDSerializableBitmap ddBitmap = (DDSerializableBitmap)msg.getData().getSerializable("image");
				List<ImageView> viewList = viewMap.getImageViewList(key);
				if(viewList == null){
					return;
				}
				for(int i = 0;i<viewList.size();i++){
					viewList.get(i).setImageBitmap(ddBitmap.getImage());
				}
				viewMap.removeKey(key);
			}
		}
	}
}