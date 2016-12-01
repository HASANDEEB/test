package costumize;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Rock on 2/19/2016.
 */
public class ArabButton extends Button {
    public ArabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface arabFont = Typeface.createFromAsset(context.getAssets() , "fonts/DroidKufi-Regular.ttf");
        this.setTypeface(arabFont);
    }
    public ArabButton(Context context) {
        super(context);
        Typeface arabFont = Typeface.createFromAsset(context.getAssets() , "fonts/DroidKufi-Regular.ttf");
        this.setTypeface(arabFont);
    }
}
