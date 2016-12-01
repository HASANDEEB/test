package entities;

import android.graphics.Bitmap;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rock on 5/31/2016.
 */
public class result implements Serializable
    {
        public int id;
        public String doc , spec , city,region,area,location,image;
        public  Bitmap btm=null;
        public boolean downloading=false;
        public  String authorized;
        public String type;
    public result(int id,String doc, String spec, String city, String region,String area,String location,String img,String type) {
        this.id = id;
        this.doc = doc;
        this.spec = spec;
        this.city = city;
        this.region = region;
        this.area=area;
        this.location=location;
        this.image=img;
        this.type=type;
    }


        public static ArrayList<result> readOBJArray(String result) {
            try {
                ArrayList<result> all=new ArrayList<result>();
                JSONArray array = new JSONArray(result);

                for (int i = 0; i < array.length(); i++) {

                    result r = new result(
                            array.getJSONObject(i).getInt("id"),
                            array.getJSONObject(i).getString("name"),
                            (array.getJSONObject(i).has("speciality"))? array.getJSONObject(i).getString("speciality")
                                    :array.getJSONObject(i).getString("clinic"),
                            array.getJSONObject(i).getString("city"),
                            array.getJSONObject(i).getString("country"),
                            array.getJSONObject(i).getString("area"),
                            array.getJSONObject(i).getString("location"),
                            array.getJSONObject(i).getString("image"),
                            array.getJSONObject(i).getString("type")
                    );
                    r.authorized = array.getJSONObject(i).getString("authorized");
                    all.add(r);
                }
                return all;
            } catch (Exception e) {

            }
            return new ArrayList<result>();
        }
}
