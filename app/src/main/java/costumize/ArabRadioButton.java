package costumize;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RadioButton;

/**
 * Created by Rock on 2/19/2016.
 */
public class ArabRadioButton extends RadioButton {
    public ArabRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface arabFont = Typeface.createFromAsset(context.getAssets() , "fonts/DroidKufi-Regular.ttf");
        this.setTypeface(arabFont);
    }
}
