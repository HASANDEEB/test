package costumize;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Switch;
import android.widget.ToggleButton;

/**
 * Created by Rock on 2/19/2016.
 */
public class ArabToggleButton extends ToggleButton {
    public ArabToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface arabFont = Typeface.createFromAsset(context.getAssets() , "fonts/DroidKufi-Regular.ttf");
        this.setTypeface(arabFont);
    }

}
