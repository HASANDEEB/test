package extra;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import costumize.ArabCheckedTextView;
import costumize.ArabTextView;

/**
 * Created by AL_deeb on 07/03/2016.
 */

    public class MyArrayAdapter<T> extends ArrayAdapter {



        public MyArrayAdapter(Context context, int textViewResourceId,ArrayList<T> list) {
            super(context, textViewResourceId,list);

        }

        public ArabCheckedTextView getView(int position, View convertView, ViewGroup parent) {
            ArabCheckedTextView v = (ArabCheckedTextView) super.getView(position, convertView, parent);
            Typeface arabFont = Typeface.createFromAsset(getContext().getAssets() , "fonts/DroidKufi-Regular.ttf");
            v.setTypeface(arabFont);

            return v;
        }

        public ArabCheckedTextView getDropDownView(int position, View convertView, ViewGroup parent) {
            ArabCheckedTextView v = (ArabCheckedTextView) super.getView(position, convertView, parent);
            Typeface arabFont = Typeface.createFromAsset(getContext().getAssets() , "fonts/DroidKufi-Regular.ttf");
            v.setTypeface(arabFont);

            return v;
        }

    }


