package costumize;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Rock on 2/18/2016.
 */
public class ArabTextView extends TextView {
    public ArabTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface arabFont = Typeface.createFromAsset(context.getAssets() , "fonts/DroidKufi-Regular.ttf");
        this.setTypeface(arabFont);
    }
    public ArabTextView(Context context) {
        super(context);
        Typeface arabFont = Typeface.createFromAsset(context.getAssets() , "fonts/DroidKufi-Regular.ttf");
        this.setTypeface(arabFont);
    }
}
