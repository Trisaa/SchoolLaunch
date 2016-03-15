package com.link.schoolunch.ui.adapter;

import java.util.ArrayList;

import com.link.schoolunch.R;
import com.link.schoolunch.model.ShopOrder;
import com.link.schoolunch.model.ShopOrderType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ShopViewAdapter extends BaseExpandableListAdapter {

	private Context mContext; 
    private LayoutInflater mInflater = null; 
    private ArrayList<ShopOrderType>   list = null; 
    
    private class GroupViewHolder { 
        TextView name; 
        TextView count; 
    } 
 
    private class ChildViewHolder { 
        TextView name; 
        TextView info;
        TextView total;
    } 
    
	public ShopViewAdapter( Context ctx, ArrayList<ShopOrderType> list ) {
	    mContext = ctx; 
        mInflater = (LayoutInflater) mContext 
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        
        this.list = list; 
	}
	
	public void setList(  ArrayList<ShopOrderType> list  ) {
		this.list = list;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).list.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).list.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate( R.layout.produce_type_adapter , null);
        	viewHolder = new GroupViewHolder();
        	
        	viewHolder.name = ( TextView )convertView.findViewById( R.id.tv_type_name );
        	viewHolder.count = ( TextView )convertView.findViewById( R.id.tv_type_count );
        	
        	convertView.setTag(viewHolder);
        } else {
        	viewHolder = (GroupViewHolder) convertView.getTag();
        }
		if(convertView != null){
			ShopOrderType item = list.get(groupPosition);
			viewHolder.name.setText( item.name );
			viewHolder.count.setText("[" + item.list.size() + "]");
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate( R.layout.shop_view_adapter , null);
        	viewHolder = new ChildViewHolder();
        	
        	viewHolder.name = ( TextView )convertView.findViewById( R.id.tv_shop_adapter_name );
        	viewHolder.info = ( TextView )convertView.findViewById( R.id.tv_shop_adapter_order );
        	viewHolder.total = ( TextView )convertView.findViewById( R.id.tv_shop_adapter_count );
        	
        	convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ChildViewHolder) convertView.getTag();
        }
		if(convertView != null){
			ShopOrder item = list.get(groupPosition).list.get(childPosition);
			
			viewHolder.name.setText( item.name );
			viewHolder.info.setText( "预订：" + item.amount + "份" );
			viewHolder.total.setText("本月累计" + item.total + "份" );
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
