package com.link.schoolunch.ui.fragment;

import com.link.schoolunch.LoginActivity;
import com.link.schoolunch.R;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

public class IntroduceFragment extends Fragment implements OnTouchListener , OnGestureListener {

	private ViewFlipper flipper;
	private GestureDetector detector;
	
	private Animation left_in_animation;
	private Animation left_out_animation;
	private Animation right_in_animation;
	private Animation right_out_animation;
	
	private Button button;
	private int count = 1;
	
	public IntroduceFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.introduce_fragment, container, false);
	}
	
	@Override
    public void onResume() {
        super.onResume();
        
        left_in_animation = AnimationUtils.loadAnimation(  this.getActivity(),  R.anim.push_left_in );
		left_out_animation = AnimationUtils.loadAnimation(  this.getActivity(),  R.anim.push_left_out );
		right_in_animation = AnimationUtils.loadAnimation(  this.getActivity(),  R.anim.push_right_in );
		right_out_animation = AnimationUtils.loadAnimation(  this.getActivity(),  R.anim.push_right_out );
		
        flipper = (ViewFlipper)this.getActivity().findViewById( R.id.view_flipper );
        detector = new GestureDetector( this.getActivity() , this );
        
        flipper.setOnTouchListener( this );
		flipper.setLongClickable(true);
		
		button = (Button)this.getActivity().findViewById( R.id.btn_enter );
		button.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				((LoginActivity)IntroduceFragment.this.getActivity() ).login();
			}
		});
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		do {
			if (e1.getX() - e2.getX() > 5) {  
				if( count == 3 )
					break;
	        	flipper.setInAnimation( left_in_animation );  
	        	flipper.setOutAnimation( left_out_animation );   
	            flipper.showNext();
	            count ++;
	            break; 
	        } else if (e1.getX() - e2.getX() < -5) {  
	        	if( count == 1 )
					break;
	        	flipper.setInAnimation(right_in_animation);  
	        	flipper.setOutAnimation(right_out_animation);  
	        	flipper.showPrevious();  
	        	count --;
	            break;
	        }
			return false;
			
		} while( false );
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if ( v.getId() == R.id.view_flipper ) { 
			Log.i("test", "onTouch");
            return this.detector.onTouchEvent(event);
        }  
		return false;
	}

}
