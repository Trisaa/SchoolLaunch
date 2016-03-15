package com.link.schoolunch;

import com.link.schoolunch.model.Shop;
import com.link.schoolunch.ui.fragment.MyFragmentManager;
import com.link.schoolunch.ui.fragment.ProduceFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class ProduceActivity extends Activity {

	private MyFragmentManager myFragmentManager;
	private Data app;
	private Shop shop = new Shop();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_produce);
		
		Bundle bundle = this.getIntent().getBundleExtra("shop");
		
		shop.shop = bundle.getString("shop");
		shop.sid = bundle.getInt("sid");
		
		this.getActionBar().setTitle( shop.shop );
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		app = (Data)getApplication();
		initWidget( shop.sid );
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        int id = item.getItemId();
	        if (id == android.R.id.home) {
	        	this.finish();
	        }
	        return super.onOptionsItemSelected(item);
	    }
	
	public Data getData() {
		return app;
	}
	
	public void initWidget( int sid ) {
		myFragmentManager = new MyFragmentManager(this);
		ProduceFragment fragment = new ProduceFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("sid", sid );
		bundle.putInt("uid", app.getUser().uid );
		fragment.setArguments(bundle);
		myFragmentManager.setFragment(R.id.activity_produce , fragment , "produce" );
	}

}
