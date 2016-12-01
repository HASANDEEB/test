package costumize;

/**
 * Created by AL_deeb on 09/19/2016.
 */
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.NumberPicker;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)//For backward-compability
public class myNumberPicker extends NumberPicker {

    public myNumberPicker(Context context) {
        super(context);
    }

    public myNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttributeSet(attrs);
    }

    public myNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        processAttributeSet(attrs);
    }
    private void processAttributeSet(AttributeSet attrs) {
        //This method reads the parameters given in the xml file and sets the properties according to it
        this.setMinValue(attrs.getAttributeIntValue(null, "min", 0));
        this.setMaxValue(attrs.getAttributeIntValue(null, "max", 0));
    }

    @Override
    public void setDisplayedValues(String[] displayedValues) {
        String[]display=new String[getMaxValue()+1];
        if(getMaxValue()==11) {
            for (int i = 0; i <= getMaxValue(); i++) {
                if (i < 9)
                    display[i] = "0"+(i+1)  ;
                else
                    display[i] = (i+1) + "";
            }
        }
        else
        if(getMaxValue()==1) {
            display[0]="صباحاً";
            display[1]="مساءً";
        }
        else{
            for (int i = 0; i <= getMaxValue(); i++) {
                if (i < 10)
                    display[i] = "0"+i ;
                else
                    display[i] = i + "";
            }
        }




        super.setDisplayedValues(display);
    }


}