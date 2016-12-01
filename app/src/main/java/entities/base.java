package entities;


import com.loopj.android.http.RequestParams;



import java.io.Serializable;

public abstract class base implements Serializable {
    String baseSTR = "";

    public static final String BASE_URL = "http://192.168.56.1:3002/";
    //public static final String BASE_URL = "http://192.168.43.45:3002/";
   //public static final String BASE_URL = "http://192.168.1.107:3002/";
    //public static final String BASE_URL = "http://tabebakhoon.com:3007/";
    public abstract RequestParams getEntity();
    public abstract RequestParams getFullEntity();


    public String getBaseSTR() {
        return baseSTR;
    }
}
