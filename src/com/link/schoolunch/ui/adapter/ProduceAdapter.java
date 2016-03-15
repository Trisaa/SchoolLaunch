package com.link.schoolunch.ui.adapter;

import java.util.ArrayList;

import com.link.schoolunch.MainActivity;
import com.link.schoolunch.R;
import com.link.schoolunch.http.image.DDImageCenter;
import com.link.schoolunch.model.Produce;
import com.link.schoolunch.model.ProduceType;
import com.link.schoolunch.ui.callback.ProduceListCallback;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProduceAdapter extends BaseExpandableListAdapter {

	private Context mContext; 
    private LayoutInflater mInflater = null; 
    private ArrayList<ProduceType>   list = null; 
    private ProduceListCallback callback;
    
    private class GroupViewHolder { 
        TextView name; 
        TextView count; 
    } 
 
    private class ChildViewHolder { 
        ImageView icon; 
        TextView name; 
        TextView info;
        TextView total;
        Button download; 
    } 
 
    public ProduceAdapter(Context ctx, ArrayList<ProduceType> list , ProduceListCallback callback ) { 
        mContext = ctx; 
        mInflater = (LayoutInflater) mContext 
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        
        this.list = list; 
        this.callback = callback;
    } 
    
	public ProduceAdapter() {
		// TODO Auto-generated constructor stub
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
			ProduceType item = list.get(groupPosition);
			viewHolder.name.setText( item.name );
			viewHolder.count.setText("[" + item.list.size() + "]");
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final int group = groupPosition;
		final int child = childPosition;
		
		ChildViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate( R.layout.produce_adapter , null);
        	viewHolder = new ChildViewHolder();
        	
        	viewHolder.name = ( TextView )convertView.findViewById( R.id.tv_produce_name );
        	viewHolder.info = ( TextView )convertView.findViewById( R.id.tv_produce_info );
        	viewHolder.icon = ( ImageView )convertView.findViewById( R.id.img_produce_icon );
        	viewHolder.download = ( Button )convertView.findViewById( R.id.btn_buy );
        	viewHolder.total = ( TextView )convertView.findViewById( R.id.tv_introduce );
        	
        	convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ChildViewHolder) convertView.getTag();
        }
		if(convertView != null){
			Produce item = list.get(groupPosition).list.get(childPosition);
			
			viewHolder.name.setText( item.name );
			viewHolder.info.setText( item.price + "/份  已预订：" + item.amount + "份" );
			viewHolder.total.setText("本月累计：" + item.total );
			
			String key = "icon/img_"+ item.name;
        	MainActivity.handler.getImageViewMap().addImageViewValues(key, viewHolder.icon );
        	Bitmap getBitmap = DDImageCenter.getInstance().getImage( MainActivity.handler, "img_" + item.name
        			, "icon" , item.img_url );
			if( getBitmap != null && item.img_url != "" ){
				viewHolder.icon.setImageBitmap(getBitmap);
				MainActivity.handler.getImageViewMap().removeImageViewValues(viewHolder.icon, key);
			}
			else{
				viewHolder.icon.setImageBitmap(DDImageCenter.getInstance().getDefaultImage());
			}
			
			viewHolder.download.setOnClickListener( new OnClickListener(){

				@Override
				public void onClick(View v) {
					callback.onClick(group, child);
				}
				
			});
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		
		return true;
	}
}
