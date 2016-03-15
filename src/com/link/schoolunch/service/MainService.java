package com.link.schoolunch.service;

import java.util.HashMap;

import org.json.JSONObject;

import com.link.schoolunch.model.User;
import com.link.schoolunch.model.Version;
import com.link.schoolunch.util.Utils;

import android.os.Handler;
import android.os.Message;

public class MainService extends BaseService {
	
	public final static int resetPassword 	= 1;
	public final static int login 			= 2;
	public final static int logout 			= 3;
	public final static int getShopID		= 4;
	public final static int registe			= 5;
	public final static int checkNewVersion = 6;
	
	public MainService( Handler handler ) {
		super(handler);
	}
	
	private boolean resetPassword( String email ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("action", "resetPassword");
		map.put("email", email );
		
		request.setParams( key , map);
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("success") ) {
			return true;
		} else {
			this.Log(json);
			return false;
		}
	}
	
	private User login( String username , String password ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "User" );
		map.put("method", "login" );
		map.put("user", username );
		map.put("passwd", password );
		
		request.setParams( key , map);
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			User user = new User();
			JSONObject data = json.getJSONObject("data");
			
			user.uid = data.getInt("uid");
			user.user = username;
			user.isShop = data.getInt("type") == 1 ? true : false;
			user.state = data.getInt("state");
			user.passwd = password;
			
			return user;
		} else {
			this.Log(json);
			return null;
		}
	}
	
	private boolean logout( int uid ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "User" );
		map.put("method", "logout" );
		map.put("uid", uid );
		
		request.setParams( key , map );
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			
			return true;
		} else {
			this.Log(json);
			return false;
		}
	}
	
	private int getShopID( int uid ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "User" );
		map.put("method", "logout" );
		map.put("uid", uid );
		
		request.setParams( key , map );
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			
			return json.getInt("data");
		} else {
			this.Log(json);
			return -1;
		}
	}
	
	private int registe( String username , String password ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "User" );
		map.put("method", "registe" );
		map.put("user", username );
		map.put("passwd", password );
		
		request.setParams( key , map);
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			int data = json.getInt("data");
			
			return data;
		} else {
			this.Log(json);
			return json.getInt("data");
		}
	}
	
	private Version checkNewVersion() throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "User" );
		map.put("method", "checkNewVersion" );
		
		request.setParams( key , map );
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			Version version = new Version();
			JSONObject data = json.getJSONObject("data");
			version.force_to_update = data.getInt("force_update");
			version.link = data.getString("link");
			version.text = data.getString("text");
			version.version = data.getString("version");
			System.out.println("test1");
			return version;
		} else {
			this.Log(json);
			return null;
		}
	}
	@Override
	protected Object doInBackground(Object... params) {
		System.out.println( key_set.size() );
		int method = Integer.valueOf(params[0].toString());
		Message message = new Message();
		message.what = Utils.MSG_SERVICE_CALLBACK;
		message.arg1 = Utils.SERVICE_MAIN;
		message.arg2 = method;
		try {
			createRequest();
			switch( method ) {
				case resetPassword: {
					message.obj = resetPassword( params[1].toString() );
					break;
				}
				case login: {
					message.obj = login( params[1].toString() , params[2].toString() );
					break;
				}
				case logout: {
					message.obj = logout( Integer.valueOf( params[1].toString() ) );
					break;
				}
				case getShopID: {
					message.obj = getShopID( Integer.valueOf( params[1].toString() ) );
					break;
				}
				case registe: {
					message.obj = registe( params[1].toString() , params[2].toString() );
					break;
				}
				case checkNewVersion: {
					message.obj = checkNewVersion();
					break;
				}
			}
			handler.sendMessage(message);
		} catch ( Exception e ) {
			e.printStackTrace();
			message.obj = null;
			handler.sendMessage(message);
		}
		return null;
	}

}
