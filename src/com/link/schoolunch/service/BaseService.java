package com.link.schoolunch.service;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;

import com.link.schoolunch.http.HttpRequest;
import com.link.schoolunch.util.Common;
import com.link.schoolunch.util.ErrLog;

public abstract class BaseService extends AsyncTask<Object , Object , Object> {

	protected HttpRequest request;
	protected Handler handler;
	
	protected ArrayList<String> key_set = new ArrayList<String>();
	
	public BaseService( Handler handler ) {
		request = new HttpRequest();
		this.handler = handler;
	}
	
	protected void createRequest() throws Exception {
		String key = request.createRequest();
		key_set.add(key);
	}
	
	public void stopTask() {
		if( key_set.size() > 0 ) {
			request.stop( key_set.remove(0) );
		}
		this.cancel(true);
	}
	
	protected void Log( JSONObject json ) throws Exception {
		Common.checkToken(json);
		if( json.has("message") )
			ErrLog.LogMessage( json.getString("message") );
		if( json.has("error"))
			ErrLog.LogError( json.getString("error") );
	}

}
