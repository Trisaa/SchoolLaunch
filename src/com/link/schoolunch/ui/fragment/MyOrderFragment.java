package com.link.schoolunch.ui.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import com.link.schoolunch.MainActivity;
import com.link.schoolunch.R;
import com.link.schoolunch.model.Order;
import com.link.schoolunch.service.OrderService;
import com.link.schoolunch.ui.adapter.OrderAdapter;
import com.link.schoolunch.ui.callback.OrderCallback;
import com.link.schoolunch.util.Common;
import com.link.schoolunch.util.Utils;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MyOrderFragment extends Fragment implements OrderCallback , OnItemClickListener {

	private ListView listview;
	private OrderAdapter adapter;
	private ArrayList<Order> list;
	private ImageView codeView;
	
	private static MainHandler handler;
	private static boolean isInit = true;
	public ProgressDialog bar;
	private int uid;
	private int position;
	
	public MyOrderFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		uid = bundle.getInt("uid");
		handler = new MainHandler( this );
	        
		
		return inflater.inflate(R.layout.my_order_fragment, container, false);
	}
	
	@Override
    public void onResume() {
        super.onResume();
        if( MainActivity.getNetState(getActivity()) == Utils.NETWORK_UNREACHED ) {
			Toast.makeText(getActivity(), "无法连接网络，请检查网络设置后重试", Toast.LENGTH_SHORT ).show();
		} else {
			bar = new ProgressDialog( this.getActivity() );
			bar.setTitle("正在加载中，请稍候...");
			bar.show();
			downloadData();
			isInit = true;
		}
        this.listview = (ListView)this.getActivity().findViewById( R.id.order_list );
        listview.setOnItemClickListener( this );
        
        codeView = (ImageView) this.getActivity().findViewById( R.id.code_img );
        codeView.setVisibility( View.INVISIBLE );
        codeView.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				 codeView.setVisibility( View.INVISIBLE );
			}
        	
        });
    }
	
	public void initAll( ArrayList<Order> list ) {
		this.list = list;
		System.out.println( list.size() );
		bar.dismiss();
		if( isInit ) {
			adapter = new OrderAdapter( this.getActivity() , this.list , this );
			this.listview.setAdapter(adapter);
			isInit = false;
		} else {
			adapter.notifyDataSetChanged();
		}
		
	}
	
	public void removeOrder( int o_id ) {
		Iterator<Order> iter = list.iterator();
		
		while( iter.hasNext() ) {
			Order temp = iter.next();
			if( temp.o_id == o_id ) {
				iter.remove();
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	public void downloadData() {
		OrderService service = new OrderService( handler );
		service.execute( OrderService.listOrder , uid );
	}
	
	public void cancelOrderByPosition() {
		Order order = list.get(position);
		OrderService service = new OrderService( handler );
		service.execute( OrderService.cancelOrder , order.o_id );
	}
	
	public static class MainHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<MyOrderFragment> mOuter;

        public MainHandler(MyOrderFragment activity) {
            mOuter = new WeakReference<MyOrderFragment>(activity);
        }

        @SuppressWarnings("unchecked")
		@Override
        public void handleMessage(Message msg) {
        	MyOrderFragment outer = mOuter.get();
            if (outer != null) {
            	if( msg.what == Utils.MSG_SERVICE_CALLBACK ) {
            		if( msg.obj == null ) {
            			outer.bar.dismiss();
            			return;
            		}
            		if( msg.arg1 == Utils.SERVICE_ORDER ) {
            			if( msg.arg2 == OrderService.listOrder ) {
            				outer.initAll((ArrayList<Order>) msg.obj );
            			}
            			else if( msg.arg2 == OrderService.cancelOrder ) {
            				int flag = Integer.valueOf( msg.obj.toString() );
            				if( flag == -1 ) {
            					Toast.makeText( outer.getActivity() , "已超时5分钟，无法退订" , Toast.LENGTH_SHORT ).show();
            				} else {
            					outer.removeOrder( flag );
            				}
            			}
            		}
            	}
            }
        }
	}

	@Override
	public void cancelOrder(int position) {
		this.position = position;
    	NoticeDialog dialog = NoticeDialog.newInstance( NoticeDialog.CANCEL_ORDER );
    	dialog.show( this.getFragmentManager() , "dialog" );
    	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Order order = list.get(position);
		String code = order.code;
		System.out.println( code );
		Bitmap bitmap = Common.createQRImage(code);
		System.out.println( bitmap == null );
		if( bitmap != null ) {
			codeView.setVisibility( View.VISIBLE );
			codeView.setImageBitmap( bitmap );
		}
	}

}
