/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;


import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class hospital_wrapper implements Serializable {


  hospital hos;
    String city;





    public hospital_wrapper() {


    }




    public static hospital_wrapper readSingleOBJ(JSONObject jsonObject) {
        try {

            hospital_wrapper obj=new hospital_wrapper();
            obj.setHos(hospital.readSingleOBJ(jsonObject));
            obj.setCity(jsonObject.getString("city"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<hospital_wrapper> readOBJArray(String result) {
        try {
            ArrayList<hospital_wrapper> all=new ArrayList<hospital_wrapper>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<hospital_wrapper>();
    }

    public hospital getHos() {
        return hos;
    }

    public void setHos(hospital hos) {
        this.hos = hos;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
