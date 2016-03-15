package com.link.schoolunch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.link.schoolunch.model.User;
import com.link.schoolunch.util.Utils;

import android.app.Application;

public class Data extends Application {

	private User user = null;
	
	public Data() {
		
	}
	
	public void setUser( User user ) {
		this.user = user;
		saveUser();
	}
	
	public User getUser() {
		if( user == null )
			user = loadUser();
		return this.user;
	}
	
	public void clear() {
		File file = new File( Utils.CACHE_PATH + "cache" );
		file.delete();
		this.user = null;
	}
	
	private void saveUser() {
		try {
			File file = new File( Utils.CACHE_PATH + "cache" );
			FileOutputStream out = new FileOutputStream( file );
			
			String buff = user.user + "&&" + user.passwd + "&&";
			out.write( buff.getBytes() );
			
			out.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private User loadUser() {
		User user = new User();
		try {
			File file = new File( Utils.CACHE_PATH + "cache" );
			if( !file.exists() ) {
				return null;
			}
			FileInputStream in = new FileInputStream( file );
			
			byte[] buffer = new byte[200];
			in.read(buffer);
			String temp = new String( buffer );
			System.out.println( temp );
			String[] list = temp.split("&&");
			
			user.user = list[0];
			user.passwd = list[1];
			System.out.println( user.user );
			System.out.println( user.passwd );
			in.close();
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
		return user;
	}

}
