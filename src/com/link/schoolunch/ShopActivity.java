package com.link.schoolunch;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.link.schoolunch.model.ShopOrderType;
import com.link.schoolunch.service.ProduceService;
import com.link.schoolunch.ui.adapter.ShopViewAdapter;
import com.link.schoolunch.util.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class ShopActivity extends Activity {
	
	public static Data app;
	
	private ExpandableListView listview;
	private ShopViewAdapter adapter;
	private ArrayList<ShopOrderType> list;
	private static MainHandler handler;
	private ProgressDialog bar;
	public static boolean isInit = true;
	
	private Button beginscan , update , send;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		app = (Data)getApplication();
		handler = new MainHandler( this );
		isInit = true;
		downloadData();
		this.listview = (ExpandableListView)this.findViewById( R.id.shop_produce_list );
		beginscan = (Button) this.findViewById( R.id.btn_shop_beginscan );
		beginscan.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass( ShopActivity.this , CaptureActivity.class );
				startActivity( intent );
			}
		});
		
		send = (Button) this.findViewById( R.id.btn_shop_send );
		send.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				ProduceService service = new ProduceService( handler );
				service.execute( ProduceService.resetOrders , app.getUser().uid );
				downloadData();
			}
		});
		
		update = (Button) this.findViewById( R.id.btn_shop_update );
		update.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadData();
			}
		});
	}
	
	@Override
    public void onResume() {
        super.onResume();
    }
	
	private void downloadData() {
		if( MainActivity.getNetState( this ) == Utils.NETWORK_UNREACHED ) {
			Toast.makeText( this , "无法连接网络，请检查网络设置后重试", Toast.LENGTH_SHORT ).show();
		} else {
			bar = new ProgressDialog( this );
			bar.setTitle("正在加载中，请稍候...");
			bar.show();
		}
		ProduceService service = new ProduceService( handler );
		service.execute( ProduceService.getProduceOrder , app.getUser().uid );
	}
	
	private void intiView( ArrayList<ShopOrderType> list ) {
		if( list != null )
			this.list = list;
		
		bar.dismiss();
		if( isInit ) {
			adapter = new ShopViewAdapter( this , this.list );
			listview.setAdapter(adapter);
			isInit = false;
		} else {
			adapter.setList(this.list);
			adapter.notifyDataSetChanged();
		}
		
	}
	
	
	public static class MainHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<ShopActivity> mOuter;

        public MainHandler(ShopActivity activity) {
            mOuter = new WeakReference<ShopActivity>(activity);
        }

        @SuppressWarnings("unchecked")
		@Override
        public void handleMessage(Message msg) {
        	ShopActivity outer = mOuter.get();
            if (outer != null) {
            	
            	if( msg.what == Utils.MSG_SERVICE_CALLBACK ) {
            		if( msg.obj == null ) {
            			Toast.makeText( outer , "获取数据失败，请检查网络设置后重试", Toast.LENGTH_SHORT ).show();
            			return;
            		}
            		if( msg.arg1 == Utils.SERVICE_PRODUCE ) {
	            		if( msg.arg2 == ProduceService.getProduceOrder ) {
	            			ArrayList<ShopOrderType> list = (ArrayList<ShopOrderType>)msg.obj;
	            			outer.intiView(list);
	            		} else if( msg.arg2 == ProduceService.resetOrders ) {
	            			boolean flag = Boolean.valueOf( msg.obj.toString() );
	            			if( flag ) {
	            				Toast.makeText( outer , "重置订单成功！", Toast.LENGTH_SHORT ).show();
	            			} else {
	            				Toast.makeText( outer , "重置订单失败！", Toast.LENGTH_SHORT ).show();
	            			}
	            		}
            		}
            	}
            }
        }

    }
}
