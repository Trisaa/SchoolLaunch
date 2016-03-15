package com.link.schoolunch.http.image;


import java.io.Serializable;

import android.graphics.Bitmap;

public class DDSerializableBitmap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Bitmap image = null;
	
	public DDSerializableBitmap(Bitmap bitmap){
		this.image = bitmap;
	}
	
	public void setImage(Bitmap bitmap){
		this.image = bitmap;
	}
	
	public Bitmap getImage(){
		return this.image;
	}

}
