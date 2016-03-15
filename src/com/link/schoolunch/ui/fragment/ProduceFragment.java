package com.link.schoolunch.ui.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import com.link.schoolunch.MainActivity;
import com.link.schoolunch.R;
import com.link.schoolunch.model.Produce;
import com.link.schoolunch.model.ProduceType;
import com.link.schoolunch.model.Time;
import com.link.schoolunch.service.OrderService;
import com.link.schoolunch.service.ProduceService;
import com.link.schoolunch.ui.adapter.ProduceAdapter;
import com.link.schoolunch.ui.callback.ProduceListCallback;
import com.link.schoolunch.util.Utils;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class ProduceFragment extends Fragment implements ProduceListCallback {

	private static MainHandler handler;
	private ProgressDialog bar;
	
	private ExpandableListView listview;
	private ProduceAdapter adapter;
	private int sid;
	private int uid;
	private int count;
	private static boolean isInit = true;
	
	private ArrayList<ProduceType> list;
	
	public ProduceFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		sid = bundle.getInt("sid");
		uid = bundle.getInt("uid");
		handler = new MainHandler( this );
	        
		if( MainActivity.getNetState(getActivity()) == Utils.NETWORK_UNREACHED ) {
			Toast.makeText(getActivity(), "无法连接网络，请检查网络设置后重试", Toast.LENGTH_SHORT ).show();
		} else {
			bar = new ProgressDialog( this.getActivity() );
			bar.setTitle("正在加载中，请稍候...");
			bar.show();
			downloadData();
			isInit = true;
		}
		return inflater.inflate(R.layout.produce_list_fragment, container, false);
	}
	
	@Override
    public void onResume() {
        super.onResume();
        this.listview = (ExpandableListView)this.getActivity().findViewById( R.id.list );
        //initAll();
    }
	
	public void initAll() {
		if( isInit ) {
			adapter = new ProduceAdapter( this.getActivity() , list , this );
			listview.setAdapter(adapter);
			bar.dismiss();
		} else {
			adapter.notifyDataSetChanged();
		}
	}
	
	public void downloadData() {
		ProduceService service = new ProduceService( ProduceFragment.handler );
		service.execute( ProduceService.listProduceType , sid );
	}
	
	public void downloadProduce( ArrayList<ProduceType> list ) {
		this.list = list;
		Iterator<ProduceType> iter = list.iterator();
		count = list.size();
		while( iter.hasNext() ) {
			ProduceType temp = iter.next();
			ProduceService service = new ProduceService( ProduceFragment.handler );
			service.execute( ProduceService.listProduce , sid , temp.type_id );
		}
	}
	
	public void addOrder( int o_id ) {
		if( o_id == -1 ) {
			Time time = new Time();
			if( !time.onTime() ) {
				Toast.makeText( this.getActivity(), "不在预订时间内，预订失败", Toast.LENGTH_SHORT ).show();
			}else {
				Toast.makeText( this.getActivity(), "预订失败，请重试！", Toast.LENGTH_SHORT ).show();
			}
		}
		else {
			Toast.makeText( this.getActivity(), "预订成功，请到\"我的订单\"中查看", Toast.LENGTH_SHORT ).show();
		}
	}
	
	public void initList( ArrayList<Produce> list ) {
		count --;
		for( int i = 0 ; i < this.list.size() ; i ++ ) {
			ProduceType temp = this.list.get(i);
			if( temp.type_id == list.get(0).type_id ) {
				temp.list = list;
				break;
			}
		}
		if( count == 0 )
			initAll();
	}

	
	public static class MainHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<ProduceFragment> mOuter;

        public MainHandler(ProduceFragment activity) {
            mOuter = new WeakReference<ProduceFragment>(activity);
        }

        @SuppressWarnings("unchecked")
		@Override
        public void handleMessage(Message msg) {
        	ProduceFragment outer = mOuter.get();
            if (outer != null) {
            	
            	if( msg.what == Utils.MSG_SERVICE_CALLBACK ) {
            		if( msg.obj == null )
            			return;
            		if( msg.arg1 == Utils.SERVICE_PRODUCE ) {
	            		if( msg.arg2 == ProduceService.listProduceType ) {
	            			outer.downloadProduce( (ArrayList<ProduceType>) msg.obj );
	            		} else if( msg.arg2 == ProduceService.listProduce ) {
	            			outer.initList( (ArrayList<Produce>) msg.obj );
	            		}
            		} else if( msg.arg1 == Utils.SERVICE_ORDER ) {
            			if( msg.arg2 == OrderService.addOrder ) {
            				int o_id = Integer.valueOf( msg.obj.toString() );
            				outer.addOrder(o_id);
            			}
            		}
            	}
            }
        }

    }


	@Override
	public void onClick(int groupPosition, int childPostion) {
		Produce produce = list.get(groupPosition).list.get(childPostion);
		
		OrderService service = new OrderService( handler );
		service.execute( OrderService.addOrder , uid , produce.p_id );
	}
}
