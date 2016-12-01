package extra;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.apps.scit.tabibihon.R;


public class animateTouch implements OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		AnimatorSet sets = new AnimatorSet();
		ObjectAnimator scale=null,scaley=null;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				scale= ObjectAnimator.ofFloat(v, View.SCALE_X, 1f, 0.8f,1f);
				scaley = ObjectAnimator.ofFloat(v, View.SCALE_Y, 1f, 0.8f,1f);

				break;
			}
		}

		sets.play(scale).with(scaley);
		sets.setDuration(500);
		sets.start();
		return false;


	}

}
