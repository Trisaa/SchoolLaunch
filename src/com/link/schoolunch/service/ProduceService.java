package com.link.schoolunch.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.link.schoolunch.model.Produce;
import com.link.schoolunch.model.ProduceType;
import com.link.schoolunch.model.Shop;
import com.link.schoolunch.model.ShopOrder;
import com.link.schoolunch.model.ShopOrderType;
import com.link.schoolunch.util.Utils;

import android.os.Handler;
import android.os.Message;

public class ProduceService extends BaseService {

	public final static int listShop 			= 1;
	public final static int listProduceType 	= 2;
	public final static int listProduce			= 3;
	public final static int getProduceOrder		= 4;
	public final static int resetOrders			= 5;
	
	public ProduceService( Handler handler ) {
		super( handler );
	}
	
	private ArrayList<Shop> listShop() throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "Shop" );
		map.put("method", "listShop" );
		
		request.setParams( key , map );
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			ArrayList<Shop> list = new ArrayList<Shop>();
			JSONArray json_array = json.getJSONArray("data");
			for( int i = 0 ; i < json_array.length() ; i ++ ) {
				Shop item = new Shop();
				JSONObject temp = json_array.getJSONObject(i);
				
				item.sid = temp.getInt("sid");
				item.shop = temp.getString("shop");
				item.total = temp.getInt("total");
				item.amount = temp.getInt("amount");
				item.icon_url = temp.getString("img_url");
				list.add(item);
			}
			
			return list;
		} else {
			this.Log(json);
			return null;
		}
	}
	
	private ArrayList<ProduceType> listProduceType( int sid ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "Shop" );
		map.put("method", "listProduceType" );
		map.put("sid", sid );
		
		request.setParams( key , map );
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			ArrayList<ProduceType> list = new ArrayList<ProduceType>();
			JSONArray json_array = json.getJSONArray("data");
			for( int i = 0 ; i < json_array.length() ; i ++ ) {
				ProduceType item = new ProduceType();
				JSONObject temp = json_array.getJSONObject(i);
				
				item.sid = sid;
				item.type_id = temp.getInt("type_id");
				item.name = temp.getString("name");
				
				list.add(item);
			}
			
			return list;
		} else {
			this.Log(json);
			return null;
		}
	}
	
	private ArrayList<Produce> listProduce( int sid , int type_id ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "Shop" );
		map.put("method", "listProduce" );
		map.put("sid", sid );
		map.put("type_id", type_id );
		
		request.setParams( key , map );
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			ArrayList<Produce> list = new ArrayList<Produce>();
			JSONArray json_array = json.getJSONArray("data");
			for( int i = 0 ; i < json_array.length() ; i ++ ) {
				Produce item = new Produce();
				JSONObject temp = json_array.getJSONObject(i);
				
				item.sid 		= sid;
				item.type_id 	= type_id;
				item.p_id 		= temp.getInt("p_id");
				item.price 		= temp.getInt("price");
				item.name 		= temp.getString("name");
				item.img_url	= temp.getString("img_url");
				item.amount		= temp.getInt("amount");
				item.total		= temp.getLong("total");
				
				list.add(item);
			}
			
			return list;
		} else {
			this.Log(json);
			return null;
		}
	}
	
	private ArrayList<ShopOrderType> getProduceOrder( int uid ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "Shop" );
		map.put("method", "getProduceOrder" );
		map.put("sid", uid );
		
		request.setParams( key , map );
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			ArrayList<ShopOrderType> list = new ArrayList<ShopOrderType>();
			JSONArray json_array = json.getJSONArray("data");
			for( int i = 0 ; i < json_array.length() ; i ++ ) {
				JSONObject temp = json_array.getJSONObject(i);
				ShopOrder order = new ShopOrder();
				
				order.name = temp.getString("name");
				order.amount = temp.getInt("amount");
				order.total = temp.getInt("total");
				order.price = temp.getInt("price");
				order.p_id = temp.getInt("p_id");
				
				Utils.Shop_list.put( order.p_id , order ); 
				String typename = temp.getString("typename");
				boolean flag = true;
				for( int j = 0 ; j < list.size() ; j ++ ) {
					if( typename.equals( list.get(j).name ) ) {
						list.get(j).list.add(order);
						flag = false;
					}
				}
				if( flag ) {
					ShopOrderType type = new ShopOrderType();
					type.name = typename;
					type.list = new ArrayList<ShopOrder>();
					type.list.add( order );
					list.add(type);
				}
			}
			
			return list;
		} else {
			this.Log(json);
			return null;
		}
	}
	
	private boolean resetOrders( int uid ) throws Exception {
		String key = key_set.get(0);
		HashMap<String , Object> map = new HashMap<String , Object>();
		map.put("ctrl", "Shop" );
		map.put("method", "resetOrders" );
		map.put("uid", uid );
		
		request.setParams( key , map );
		JSONObject json = request.send(key);
		key_set.remove(0);
		System.out.println( json.toString() );
		if( json.getBoolean("state") ) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		int method = Integer.valueOf(params[0].toString());
		Message message = new Message();
		message.what = Utils.MSG_SERVICE_CALLBACK;
		message.arg1 = Utils.SERVICE_PRODUCE;
		message.arg2 = method;
		try {
			createRequest();
			switch( method ) {
				case listShop:{
					message.obj = listShop();
					break;
				}
				case listProduceType:{
					message.obj = listProduceType( Integer.valueOf( params[1].toString() ) );
					break;
				}
				case listProduce:{
					message.obj = listProduce( Integer.valueOf( params[1].toString() ) , Integer.valueOf( params[2].toString() ));
					break;
				}
				case getProduceOrder:{
					message.obj = getProduceOrder( Integer.valueOf( params[1].toString() ) );
					break;
				}
				case resetOrders:{
					message.obj = resetOrders( Integer.valueOf( params[1].toString() ) );
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
