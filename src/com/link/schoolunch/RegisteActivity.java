package com.link.schoolunch;

import java.lang.ref.WeakReference;

import com.link.schoolunch.service.MainService;
import com.link.schoolunch.ui.fragment.NoticeDialog;
import com.link.schoolunch.util.SHA;
import com.link.schoolunch.util.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisteActivity extends Activity  {

	private Button send;
	public ProgressDialog bar;
	private TextView username , passwd , confirm;
	
	public static MainHandler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_register);
		
		handler = new MainHandler( this );
		
		bar = new ProgressDialog( this );
		bar.setTitle("正在注册...");
		
		username = (TextView) this.findViewById( R.id.activity_register_user_name );
		passwd = (TextView) this.findViewById( R.id.activity_register_password );
		confirm = (TextView) this.findViewById( R.id.activity_register_btn_repeat );
		
		send = (Button) this.findViewById( R.id.activity_register_button_register );
		send.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				String text_user = username.getText().toString();
				String text_passwd = passwd.getText().toString();
				String text_repeat = confirm.getText().toString();
				
				if( !text_passwd.equals( text_repeat ) ) {
					Toast.makeText( RegisteActivity.this , "两次输入密码不同！", Toast.LENGTH_SHORT ).show();
					return;
				}
				
				MainService service = new MainService( handler );
				service.execute( MainService.registe , text_user , SHA.SHA1( text_passwd ) );
				bar.show();
			}
			
		});
	}
	
	public void login() {
		Intent intent = new Intent();
		intent.setClass( this , LoginActivity.class );
		startActivity( intent );
		this.finish();
	}
	
	public static class MainHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<RegisteActivity> mOuter;

        public MainHandler(RegisteActivity activity) {
            mOuter = new WeakReference<RegisteActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
        	RegisteActivity outer = mOuter.get();
            if (outer != null) {
            	if( msg.what == Utils.MSG_SERVICE_CALLBACK ) {
            		if( msg.arg1 == Utils.SERVICE_MAIN && msg.arg2 == MainService.registe ) {
            			outer.bar.dismiss();
            			if( msg.obj == null ) {
            				Toast.makeText( outer , "网络链接错误", Toast.LENGTH_SHORT ).show();
            				return;
            			}
            			int data = Integer.valueOf( msg.obj.toString() );
            			if( data > 0 ) {
            				NoticeDialog dialog = NoticeDialog.newInstance( NoticeDialog.REIGSTE_DIALOG );
                        	dialog.show( outer.getFragmentManager() , "registe" );
            			} else if( data == 0 ) {
            				Toast.makeText( outer , "用户名重复，请更换用户名", Toast.LENGTH_SHORT ).show();
            			} else if( data == -1 ) {
            				Toast.makeText( outer , "已达到公测人数上限，请等待正式版", Toast.LENGTH_SHORT ).show();
            			} else {
            				Toast.makeText( outer , "发生未知网络错误", Toast.LENGTH_SHORT ).show();
            			}
            		}
            	}
            }
        }
	}
}
