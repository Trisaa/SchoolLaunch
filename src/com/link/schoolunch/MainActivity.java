package com.link.schoolunch;

import java.lang.ref.WeakReference;

import com.link.schoolunch.http.image.DDImageCenter;
import com.link.schoolunch.http.image.ImageLoaderHandler;
import com.link.schoolunch.model.Version;
import com.link.schoolunch.service.MainService;
import com.link.schoolunch.ui.fragment.DrawerFragment;
import com.link.schoolunch.ui.fragment.HelpFragment;
import com.link.schoolunch.ui.fragment.MyFragmentManager;
import com.link.schoolunch.ui.fragment.MyOrderFragment;
import com.link.schoolunch.ui.fragment.NoticeDialog;
import com.link.schoolunch.ui.fragment.ShopListFragment;
import com.link.schoolunch.util.Utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	public static ImageLoaderHandler handler = new ImageLoaderHandler();
	
	private MyFragmentManager myFragmentManager;
	private DrawerLayout drawerLayout;
	
	
	private DrawerFragment drawerFragment = new DrawerFragment();
	private ShopListFragment shoplistfragment = new ShopListFragment();
	private MyOrderFragment orderfragment = new MyOrderFragment();
	private HelpFragment helpfragment = new HelpFragment();
	
	private ActionBarDrawerToggle drawerToggle;
	private View drawerParams;
	public static Data app;
	
	private static MainHandler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		app = (Data)getApplication();
		initWidget();
	}
	
	public void initWidget(){
		mHandler = new MainHandler( this );
        myFragmentManager = new MyFragmentManager(this);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        myFragmentManager.setFragment(R.id.right_main_layout,shoplistfragment , Utils.TAG_SHOPLIST_FRAGMENT );
        myFragmentManager.setFragment(R.id.left_drawer_layout,drawerFragment , Utils.TAG_DRAWER_FRAGMENT );

        drawerParams = (View)findViewById(R.id.left_drawer_layout);
        DDImageCenter.getInstance().setDefaultImage( BitmapFactory.decodeResource( this.getResources() , R.drawable.default_icon ) );
        
        MainService service = new MainService(mHandler);
    	service.execute( MainService.checkNewVersion );
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(drawerParams)){
                drawerLayout.closeDrawer(drawerParams);
            }else{
                drawerLayout.openDrawer(drawerParams);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void checkNewVersion( Version version ) {
    	if( ! Utils.VERSION.version.equals( version.version ) ) {
    		Utils.NEW_VERSION = version;
    		NoticeDialog dialog = NoticeDialog.newInstance( NoticeDialog.NEW_VERSION );
        	dialog.show( this.getFragmentManager() , "version" );
    	}
    }

    public static class MainHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<MainActivity> mOuter;

        public MainHandler(MainActivity activity) {
            mOuter = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
        	System.out.println("msg");
            MainActivity outer = mOuter.get();
            if (outer != null) {
            	if( msg.what == Utils.MSG_SERVICE_CALLBACK ) {
            		System.out.println("msg1");
            		if( msg.arg1 == Utils.SERVICE_MAIN && msg.arg2 == MainService.checkNewVersion ) {
            			System.out.println("test");
            			if( msg.obj == null ) {
            				return;
            			}
            			Version version = (Version)msg.obj;
            			System.out.println( version.text );
            			outer.checkNewVersion(version);
            		}
            	}
            }
        }

    }
    
    public static int getNetState(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo networkinfo = connectivity.getActiveNetworkInfo();
				if (networkinfo != null) {
					if (networkinfo.isAvailable() && networkinfo.isConnected()) {
						return Utils.NETWORK_AVALIABLE;
					}
				}
			}
			return Utils.NETWORK_UNREACHED;
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.NETWORK_UNREACHED;
		}
		
	}

    public void OnClickListeners(View view){
        switch (view.getId()){
            case R.id.drawer_button_shoplist:
            	System.out.println( shoplistfragment.isAdded() );
            	if( !shoplistfragment.isAdded() )
            		myFragmentManager.replaceFragmentByTag(R.id.right_main_layout,shoplistfragment , Utils.TAG_SHOPLIST_FRAGMENT );
                break;
            case R.id.drawer_button_mycollection:
                break;
            case R.id.drawer_button_orderlist:
            	if( !orderfragment.isAdded() ) {
	            	Bundle bundle = new Bundle();
	            	bundle.putInt("uid" , app.getUser().uid );
	            	orderfragment.setArguments(bundle);
	            	myFragmentManager.replaceFragmentByTag(R.id.right_main_layout,orderfragment , Utils.TAG_MYORDER_FRAGMENT );
            	}
                break;
            case R.id.drawer_button_help:
            	if( !helpfragment.isAdded() )
            		myFragmentManager.replaceFragmentByTag(R.id.right_main_layout,helpfragment , Utils.TAG_HELP_FRAGMENT );
                break;
            case R.id.drawer_button_newversion:
            	MainService service = new MainService(mHandler);
            	service.execute( MainService.checkNewVersion );
                break;
            case R.id.drawer_button_logout:
            	NoticeDialog dialog = NoticeDialog.newInstance( NoticeDialog.ALTER_DIALOG );
            	dialog.show( this.getFragmentManager() , "dialog" );
                break;
            case R.id.drawer_button_quit:
            	MainActivity.this.finish();
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(drawerParams);
    }

}
