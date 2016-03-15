package com.link.schoolunch.ui.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.link.schoolunch.MainActivity;
import com.link.schoolunch.ProduceActivity;
import com.link.schoolunch.R;
import com.link.schoolunch.model.Shop;
import com.link.schoolunch.service.ProduceService;
import com.link.schoolunch.ui.adapter.ShopAdapter;
import com.link.schoolunch.util.Utils;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ShopListFragment extends Fragment implements OnItemClickListener {

	private static MainHandler handler;
	private ProgressDialog bar;
	
	private ListView listview;
	private ShopAdapter adapter;
	private ArrayList<Shop> list;
	private static boolean isInit = true;
	
	public ShopListFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.shop_list_fragment , container, false);
	}
	
	@Override
    public void onResume() {
		super.onResume();
        handler = new MainHandler( this );
        this.listview = (ListView)this.getActivity().findViewById( R.id.shop_list );
        this.listview.setOnItemClickListener( this );
        
        if( MainActivity.getNetState(getActivity()) == Utils.NETWORK_UNREACHED ) {
			Toast.makeText(getActivity(), "无法连接网络，请检查网络设置后重试", Toast.LENGTH_SHORT ).show();
		} else {
			bar = new ProgressDialog( this.getActivity() );
			bar.setTitle("正在加载中，请稍候...");
			bar.show();
			downloadData();
			isInit = true;
		}
    }
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Shop shop = list.get(position);
		Bundle bundle = new Bundle();
		bundle.putString("shop", shop.shop );
		bundle.putInt("sid", shop.sid );
		
		Intent intent = new Intent();
		intent.setClass( this.getActivity() ,  ProduceActivity.class );
		intent.putExtra("shop", bundle);
		this.getActivity().startActivity( intent );
	}
	
	public void initAll( ArrayList<Shop> list ) {
		this.list = list;
		bar.dismiss();
		if( isInit ) {
			adapter = new ShopAdapter( this.getActivity() , this.list );
			listview.setAdapter(adapter);
			isInit = false;
		} else {
			updateUI();
		}
	}
	
	public void updateUI() {
		adapter.notifyDataSetChanged();
	}
	
	public void downloadData() {
		ProduceService service = new ProduceService( ShopListFragment.handler );
		service.execute( ProduceService.listShop );
	}

	
	public static class MainHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<ShopListFragment> mOuter;

        public MainHandler(ShopListFragment activity) {
            mOuter = new WeakReference<ShopListFragment>(activity);
        }

        @SuppressWarnings("unchecked")
		@Override
        public void handleMessage(Message msg) {
        	ShopListFragment outer = mOuter.get();
            if (outer != null) {
            	if( msg.what == Utils.MSG_SERVICE_CALLBACK ) {
            		if( msg.arg1 == Utils.SERVICE_PRODUCE && msg.arg2 == ProduceService.listShop ) {
            			ArrayList<Shop> list = ( ArrayList<Shop > ) msg.obj;
            			outer.initAll( list );
            		}
            	}
            }
        }

    }
}
