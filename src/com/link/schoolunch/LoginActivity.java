package com.link.schoolunch;

import com.link.schoolunch.ui.fragment.IntroduceFragment;
import com.link.schoolunch.ui.fragment.LoginFragment;
import com.link.schoolunch.util.Utils;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.WindowManager;

public class LoginActivity extends Activity {

	private FragmentManager manager;
	private LoginFragment login_fragment = new LoginFragment();
	private IntroduceFragment introduce_fragment = new IntroduceFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_login);

		manager = this.getFragmentManager();
		
		Utils.init();
		if( Utils.FIRST_LOGIN ) {
			manager.beginTransaction().add( R.id.fragment_login , introduce_fragment ).commit();
		} else {
			manager.beginTransaction().add( R.id.fragment_login , login_fragment ).commit();
		}
	}
	
	public void login() {
		Utils.setFirstLogin( false );
		manager.beginTransaction().replace( R.id.fragment_login , login_fragment ).commit();
	}
}
