package com.link.schoolunch.http.image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.widget.ImageView;

public class DDImageViewList {
	private Map<String, List<ImageView>> map;
	
	public DDImageViewList(){
		map = new HashMap<String, List<ImageView>>();
	}
	
	public void addImageViewValues(String key, ImageView imgView){
		if(imgView == null){
			return;
		}
		removeImageViewValues(imgView);
		if(map.containsKey(key)){
			map.get(key).add(imgView);
		}
		else{
			List<ImageView> list = new ArrayList<ImageView>();
			list.add(imgView);
			map.put(key, list);
		}
	}

	public void removeImageViewValues(ImageView imgView) {
		// TODO Auto-generated method stub
		if(imgView == null){
			return;
		}
		Collection<List<ImageView>> collection = map.values();
		Iterator<List<ImageView>> it = collection.iterator();
		while(it.hasNext()){
			List<ImageView> view_list = it.next();
			if(null != view_list){
				view_list.remove(imgView);
			}			
		}
	}
	
	public void removeImageViewValues(ImageView imgView, String key) {
		// TODO Auto-generated method stub
		if(imgView == null){
			return;
		}
		
		List<ImageView> view_list = map.get(key);
		if(null != view_list){
			view_list.remove(imgView);
		}			
	}
	
	
	public void removeKey(String key){
		map.put(key, null);
		map.remove(key);
	}
	
	public List<ImageView> getImageViewList(String key){
		return map.get(key);
	}
	
}
