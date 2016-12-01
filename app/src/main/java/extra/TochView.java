package extra;

import com.apps.scit.tabibihon.R;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class TochView implements OnTouchListener {
	ImageView IM;
	
	public TochView(ImageView imageButton) {
		IM = imageButton;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			IM.setBackgroundResource(R.drawable.ic_menu_viewred);//*
			break;
		case MotionEvent.ACTION_MOVE:
			IM.setBackgroundResource(R.drawable.ic_menu_viewred);//*
			break;

		default:
			IM.setBackgroundResource(0);
			break;
		}
		return false;
	}

}
