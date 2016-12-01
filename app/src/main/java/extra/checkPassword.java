package extra;

import android.content.Context;

import com.apps.scit.tabibihon.R;

/**
 * Created by AL_deeb on 06/28/2016.
 */
public class checkPassword {


    public static String check(String text,Context context){
        if(getRating(text)==0.0)
          return   context.getResources().getString(R.string.v_weak);
        else if(getRating(text)==1.0)
            return   context.getResources().getString(R.string.weak);
        else if(getRating(text)==2.0)
            return   context.getResources().getString(R.string.mid);
        else if(getRating(text)==3.0)

            return   context.getResources().getString(R.string.strong);
        else if(getRating(text)==4.0)
            return   context.getResources().getString(R.string.v_strong);

    return "";
    }


    private static float getRating(String password) throws IllegalArgumentException {
        if (password == null) {throw new IllegalArgumentException();}
        int passwordStrength = 0;
        if (password.length() > 5) {passwordStrength++;} // minimal pw length of 6
        if (password.toLowerCase()!= password) {passwordStrength++;} // lower and upper case
        if (password.length() > 8) {passwordStrength++;} // good pw length of 9+
        int numDigits= getNumberDigits(password);
        if (numDigits > 0 && numDigits != password.length()) {passwordStrength++;} // contains digits and non-digits
        return (float)passwordStrength;
    }

    public  static int getNumberDigits(String inString){
        if (isEmpty(inString)) {
            return 0;
        }
        int numDigits= 0;
        int length= inString.length();
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(inString.charAt(i))) {
                numDigits++;
            }
        }
        return numDigits;
    }


    public static boolean isEmpty(String inString) {
        return inString == null || inString.length() == 0;
    }

}
