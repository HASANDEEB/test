package extra;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import com.apps.scit.tabibihon.R;


public class TochLoc implements OnTouchListener {
	ImageView IM;

	public TochLoc(ImageView imageButton) {
		IM = imageButton;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			IM.setBackgroundResource(R.drawable.ic_menu_compassred);//*
			break;
		case MotionEvent.ACTION_MOVE:
			IM.setBackgroundResource(R.drawable.ic_menu_compassred);//*
			break;

		default:
			IM.setBackgroundResource(0);
			break;
		}
		return false;
	}

}
