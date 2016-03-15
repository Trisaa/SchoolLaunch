package com.link.schoolunch.ui.adapter;

import java.util.ArrayList;

import com.link.schoolunch.R;
import com.link.schoolunch.model.Order;
import com.link.schoolunch.model.Time;
import com.link.schoolunch.ui.callback.OrderCallback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class OrderAdapter extends BaseAdapter {
	
	private ArrayList<Order> list;
	private LayoutInflater inflate;
	private OrderCallback callback;

	public OrderAdapter( Context context , ArrayList<Order> list , OrderCallback callback ) {
		inflate = LayoutInflater.from(context);
		this.list = list;
		this.callback = callback;
	}
	
	private class ItemView {
		public TextView info;
		public TextView name;
		public Button cancel;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		
		final int pos = position;
		ItemView viewHolder;
		if (convertView == null) {
			convertView = inflate.inflate( R.layout.order_adapter , null);
        	viewHolder = new ItemView();
        	
        	viewHolder.name = ( TextView )convertView.findViewById( R.id.tv_order_name );
        	viewHolder.info = ( TextView )convertView.findViewById( R.id.tv_order_shop );
        	viewHolder.cancel = (Button)convertView.findViewById( R.id.btn_cancel );
        	
        	convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ItemView) convertView.getTag();
        }
        if(convertView != null){
        	Order item = list.get(position);
        	System.out.println( item == null );
        	viewHolder.name.setText( item.name );
        	Time time = new Time( item.n_time );
        	viewHolder.info.setText( "店铺: " + item.shop + " 预计送达时间: " + time.getSendTime() );
        	
        	viewHolder.cancel.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					callback.cancelOrder(pos);
				}
        	});
        }
    	return convertView;
	}

}
