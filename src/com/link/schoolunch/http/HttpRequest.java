package com.link.schoolunch.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class HttpRequest {
	
	private HttpClient client;
	private String params;
	private HashMap< String , HttpPost > post_list;
	
	public HttpRequest() {
		client = new DefaultHttpClient();
		post_list = new HashMap< String , HttpPost >();
	}
	
	@SuppressWarnings("unchecked")
	public boolean setParams( String pkey , HashMap<String , Object> map ) {
		JSONObject json = new JSONObject();
		
		try {
			HttpPost httpPost = post_list.get(pkey);
			if( httpPost == null ) {
				return false;
			}
			Iterator<String> iter = map.keySet().iterator();
			
			while( iter.hasNext() ) {
				String key = iter.next();
				Object ob = map.get(key);
				
				if( ob instanceof ArrayList ) {
					ArrayList<Object> list = (ArrayList<Object>) ob;
					JSONArray array = new JSONArray();
					for( int i = 0 ; i < list.size() ; i ++ ) {
						array.put( list.get(i) );
					}
					json.put( key , array );
				} else 
					json.put( key , ob );
			}
			
			params = json.toString();
			httpPost.setEntity( new StringEntity( this.params , HTTP.UTF_8 ) );
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public String createRequest() throws Exception {
		HttpPost httpPost = new HttpPost( "http://schoollunch.duapp.com/" );
		
		String key = "task_key_" + System.currentTimeMillis();
		post_list.put(key, httpPost);
		return key;		
	}
	
	public JSONObject send( String key ) throws Exception {
		HttpPost httpPost = post_list.get(key);
		if( httpPost == null ) {
			return null;
		}
		HttpResponse s = client.execute(httpPost);
		JSONObject result = new JSONObject( EntityUtils.toString(s.getEntity()) );
		return result;
		
	}
	
	public void stop( String key ) {
		HttpPost httpPost = post_list.get(key);
		if( httpPost != null ) {
			httpPost.abort();
		}
		
	}
}
