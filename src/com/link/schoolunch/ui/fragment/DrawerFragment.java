package com.link.schoolunch.ui.fragment;

import com.link.schoolunch.MainActivity;
import com.link.schoolunch.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Maple on 14-3-13.
 */
public class DrawerFragment extends Fragment {

	private TextView username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawer,container,false);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        username = (TextView) this.getActivity().findViewById( R.id.tv_user_name );
        username.setText( MainActivity.app.getUser().user );
        //initAll();
    }

    class OnClickListeners implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            System.out.println(view.getTag());
        }
    }
}
