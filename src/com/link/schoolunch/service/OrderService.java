package com.link.schoolunch.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.link.schoolunch.model.Order;
import com.link.schoolunch.util.Utils;

import android.os.Handler;
import android.os.Message;

public class OrderService extends BaseService {

	public final static int addOrder	= 1;
	public final static int cancelOrder	= 2;
	public final static int listOrder	= 3;
	public final static int finishOrder	= 4;
	
	public OrderService(Handler handler) {
		super(handler);
	}
	
	private int addOrder( int uid , int p_id ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "Order" );
		map.put("method", "addOrder" );
		map.put("uid", uid );
		map.put("p_id", p_id );
		
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
	
	private int cancelOrder( int o_id ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "Order" );
		map.put("method", "cancelOrder" );
		map.put("o_id", o_id );
		
		request.setParams( key , map );
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			
			return o_id;
		} else {
			this.Log(json);
			return -1;
		}
	}
	
	private ArrayList<Order> listOrder( int uid ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "Order" );
		map.put("method", "listOrder" );
		map.put("uid", uid );
		
		request.setParams( key , map );
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			ArrayList<Order> list = new ArrayList<Order>();
			JSONArray json_array = json.getJSONArray("data");
			for( int i = 0 ; i < json_array.length() ; i ++ ) {
				Order item = new Order();
				JSONObject temp = json_array.getJSONObject(i);
				
				item.uid = uid;
				item.p_id = temp.getInt("p_id");
				item.o_id = temp.getInt("o_id");
				item.code = temp.getString("code");
				item.n_time = temp.getString("n_time");
				item.finish = false;
				item.name = temp.getString("name");
				item.shop = temp.getString("shop");
				
				list.add(item);
			}
			
			return list;
		} else {
			this.Log(json);
			return null;
		}
	}
	
	private boolean finishOrder( int o_id ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "Order" );
		map.put("method", "finishOrder" );
		map.put("o_id", o_id );
		
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

	@Override
	protected Object doInBackground(Object... params) {
		int method = Integer.valueOf(params[0].toString());
		Message message = new Message();
		message.what = Utils.MSG_SERVICE_CALLBACK;
		message.arg1 = Utils.SERVICE_ORDER;
		message.arg2 = method;
		try {
			createRequest();
			switch( method ) {
				case addOrder:{
					message.obj = addOrder( Integer.valueOf( params[1].toString() ) , Integer.valueOf( params[2].toString() ) );
					break;
				}
				case cancelOrder:{
					message.obj = cancelOrder( Integer.valueOf( params[1].toString() ) );
					break;
				}
				case listOrder:{
					message.obj = listOrder( Integer.valueOf( params[1].toString() ) );
					break;
				}
				case finishOrder:{
					message.obj = finishOrder( Integer.valueOf( params[1].toString() ) );
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
