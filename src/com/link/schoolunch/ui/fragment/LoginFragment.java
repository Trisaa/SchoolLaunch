package com.link.schoolunch.ui.fragment;

import java.lang.ref.WeakReference;

import com.link.schoolunch.Data;
import com.link.schoolunch.MainActivity;
import com.link.schoolunch.R;
import com.link.schoolunch.RegisteActivity;
import com.link.schoolunch.ShopActivity;
import com.link.schoolunch.model.User;
import com.link.schoolunch.service.MainService;
import com.link.schoolunch.util.SHA;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {

	private EditText username , password;
	private Button login , regis;
	public ProgressDialog bar;
	private Data app;
	private User user;
	
	public static MainHandler handler;
	
	public LoginFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.login_fragment, container, false);
	}

	@Override
    public void onResume() {
        super.onResume();
        
        handler = new MainHandler( this );
		app = (Data)this.getActivity().getApplication();
        if( MainActivity.getNetState(this.getActivity()) == Utils.NETWORK_UNREACHED ) {
			Toast.makeText(this.getActivity(), "无法连接网络，请检查网络设置后重试", Toast.LENGTH_SHORT ).show();
		} else {
			initWidget();
		}
	}
	
	public Data getData() {
		return this.app;
	}
	
	public void initWidget() {
		user = app.getUser();
		
		username = (EditText) this.getActivity().findViewById( R.id.edit_username );
		password = (EditText) this.getActivity().findViewById( R.id.edit_password );
		login = (Button) this.getActivity().findViewById( R.id.btn_login );
		regis = (Button) this.getActivity().findViewById( R.id.btn_registe );
		
		bar = new ProgressDialog( this.getActivity() );
		bar.setTitle("登录中...");
		
		if( user != null ) {
			username.setVisibility( View.INVISIBLE );
			password.setVisibility( View.INVISIBLE );
			login.setVisibility(View.INVISIBLE);
			regis.setVisibility(View.INVISIBLE);
			MainService service = new MainService( handler );
			service.execute( MainService.login , user.user , user.passwd );
			bar.show();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		login.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				String user = username.getText().toString();
				String pass = password.getText().toString();
				
				if( user.isEmpty() || pass.isEmpty() ) {
					Toast.makeText( LoginFragment.this.getActivity() , "请输入用户名和密码", Toast.LENGTH_SHORT ).show();
				} else {
					MainService service = new MainService( handler );
					service.execute( MainService.login , user , SHA.SHA1( pass ) );
					bar.show();
				}
			}
		});
		
		regis.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass( LoginFragment.this.getActivity() , RegisteActivity.class);  
				startActivity(intent);
				//finish();
			}
		});
	}
	public static class MainHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<LoginFragment> mOuter;

        public MainHandler(LoginFragment activity) {
            mOuter = new WeakReference<LoginFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
        	LoginFragment outer = mOuter.get();
            if (outer != null) {
            	if( msg.what == Utils.MSG_SERVICE_CALLBACK ) {
            		if( msg.arg1 == Utils.SERVICE_MAIN && msg.arg2 == MainService.login ) {
            			outer.bar.dismiss();
            			if( msg.obj == null ) {
            				Toast.makeText( outer.getActivity() , "用户名或密码错误", Toast.LENGTH_SHORT ).show();
            			} else {
            				User user = (User)msg.obj;
            				if( user.state == 3 ) {
            					Toast.makeText( outer.getActivity()  , "该账户已被封号", Toast.LENGTH_SHORT ).show();
            				} else {
            					if( user.isShop ) {
            						outer.getData().setUser(user);
	            					Intent intent = new Intent();
	            					intent.setClass(outer.getActivity() , ShopActivity.class);  
	            					outer.getActivity() .startActivity(intent);
	            					outer.getActivity() .finish();
            					} else {
	            					outer.getData().setUser(user);
	            					Intent intent = new Intent();
	            					intent.setClass(outer.getActivity() , MainActivity.class);  
	            					outer.getActivity() .startActivity(intent);
	            					outer.getActivity() .finish();
            					}
            				}
            			}
            		}
            	}
            }
        }
	}
}
