package com.link.schoolunch.ui.adapter;

import java.util.ArrayList;

import com.link.schoolunch.MainActivity;
import com.link.schoolunch.R;
import com.link.schoolunch.http.image.DDImageCenter;
import com.link.schoolunch.model.Shop;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopAdapter extends BaseAdapter {
	
	private  ArrayList<Shop> list;
	private LayoutInflater inflate;

	public ShopAdapter( Context context , ArrayList<Shop> list ) {
		inflate = LayoutInflater.from(context);
		this.list = list;
	}
	
	private class ItemView{
		public ImageView icon;
		public TextView info;
		public TextView name;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemView viewHolder;
		if (convertView == null) {
			convertView = inflate.inflate( R.layout.shop_adapter , null);
        	viewHolder = new ItemView();
        	
        	viewHolder.name = ( TextView )convertView.findViewById( R.id.tv_shop_name );
        	viewHolder.icon = ( ImageView )convertView.findViewById( R.id.img_shop_icon );
        	viewHolder.info = ( TextView )convertView.findViewById( R.id.tv_shop_info );
        	
        	convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ItemView) convertView.getTag();
        }
        if(convertView != null){
        	Shop shop = list.get(position);
        	viewHolder.name.setText( shop.shop );
        	viewHolder.info.setText( "当前订单："+shop.amount+"份  本月累计：" + shop.total + "份" );
        	
        	String key = "icon/img_"+ shop.shop;
        	MainActivity.handler.getImageViewMap().addImageViewValues(key, viewHolder.icon );
        	Bitmap getBitmap = DDImageCenter.getInstance().getImage( MainActivity.handler, "img_" + shop.shop
        			, "icon" , shop.icon_url );
			if(getBitmap!=null){
				viewHolder.icon.setImageBitmap(getBitmap);
				MainActivity.handler.getImageViewMap().removeImageViewValues(viewHolder.icon, key);
			}
			else{
				viewHolder.icon.setImageBitmap(DDImageCenter.getInstance().getDefaultImage());
			}
        }
		return convertView;
	}

}
