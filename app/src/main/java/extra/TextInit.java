package extra;

import android.text.TextUtils;

/**
 * Created by AL_deeb on 07/26/2016.
 */
public class TextInit {

    public static String check(String s){

        String result="";
        boolean f=true;
        for(int i=0;i<s.length();i++){

            if(s.charAt(i)==' '){
                if(!f) {
                    result += s.charAt(i);
                    f = true;
                }


            }
            else
            {
                f=false;
                result += s.charAt(i);
            }

        }
        if(f&& !TextUtils.isEmpty(s)&&!TextUtils.isEmpty(result))
            result=result.substring(0,result.length()-1);

        return result;
    }

}
