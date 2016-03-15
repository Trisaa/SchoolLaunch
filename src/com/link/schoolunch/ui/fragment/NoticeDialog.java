package com.link.schoolunch.ui.fragment;

import com.link.schoolunch.LoginActivity;
import com.link.schoolunch.MainActivity;
import com.link.schoolunch.R;
import com.link.schoolunch.RegisteActivity;
import com.link.schoolunch.util.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class NoticeDialog extends DialogFragment {

	public final static int ALTER_DIALOG = 1;
	public final static int CANCEL_ORDER = 2;
	public final static int REIGSTE_DIALOG = 3;
	public final static int NEW_VERSION = 4;
	
	public static NoticeDialog newInstance(int title ) {
		NoticeDialog myDialogFragment = new NoticeDialog();
		Bundle bundle = new Bundle();
		bundle.putInt("title", title);
		myDialogFragment.setArguments(bundle);
		return myDialogFragment;
	}
	
	public NoticeDialog() {
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		int args = getArguments().getInt("title");
		//根据传进来的参数选择创建哪种Dialog
		switch (args) {
			case ALTER_DIALOG:{
				return new AlertDialog.Builder(getActivity())
			  	.setIcon(R.drawable.ic_launcher)
			  	.setTitle("是否切换用户？")
			  	.setPositiveButton("是",
			  	new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int whichButton) {
				    	MainActivity.app.clear();
				    	Intent intent = new Intent();
				    	intent.setClass( getActivity() , LoginActivity.class );
				    	getActivity().startActivity(intent);
				    	getActivity().finish();
				    }
			  	})
			  	.setNegativeButton("否", new DialogInterface.OnClickListener() {
			  		public void onClick(DialogInterface dialog, int whichButton) {
			  			
			  		}
			  	})
			  	.create();
			}	
			case CANCEL_ORDER:{
				return new AlertDialog.Builder(getActivity())
			  	.setIcon(R.drawable.ic_launcher)
			  	.setTitle("确定要取消订单吗？")
			  	.setPositiveButton("是",
			  	new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int whichButton) {
				    	MyOrderFragment fragment = ( MyOrderFragment )getActivity().
				    			getFragmentManager().findFragmentByTag( Utils.TAG_MYORDER_FRAGMENT );
				    	fragment.cancelOrderByPosition();
				    }
			  	})
			  	.setNegativeButton("否", new DialogInterface.OnClickListener() {
			  		public void onClick(DialogInterface dialog, int whichButton) {
			  			
			  		}
			  	})
			  	.create();
			}
			case REIGSTE_DIALOG:{
				return new AlertDialog.Builder(getActivity())
			  	.setIcon(R.drawable.ic_launcher)
			  	.setTitle("注册成功！")
			  	.setPositiveButton("登录",
			  	new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int whichButton) {
				    	RegisteActivity activity = (RegisteActivity)getActivity();
				    	activity.login();
				    }
			  	})
			  	.create();
			}
			case NEW_VERSION:{
				return new AlertDialog.Builder(getActivity())
			  	.setIcon(R.drawable.ic_launcher)
			  	.setTitle("发现新版本，是否下载？")
			  	.setMessage( Utils.NEW_VERSION.text )
			  	.setPositiveButton("是",
			  	new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int whichButton) {
				    	Uri uri = Uri.parse( Utils.NEW_VERSION.link );  
				    	Intent it = new Intent(Intent.ACTION_VIEW, uri);  
				    	startActivity(it);
				    }
			  	})
			  	.setNegativeButton("否", new DialogInterface.OnClickListener() {
			  		public void onClick(DialogInterface dialog, int whichButton) {
			  			
			  		}
			  	})
			  	.create();
			}
		}
		return null;
	}

}
