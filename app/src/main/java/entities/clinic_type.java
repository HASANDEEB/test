/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;


import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class clinic_type extends base {


    int ID;
    String type;


    @Override
    public String toString() {
        return
                type ;
    }

    public clinic_type() {


    }



    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("type", type);




        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }

    @Override
    public RequestParams getFullEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("ID", ID);
            jsonRequest.put("type", type);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static clinic_type readSingleOBJ(JSONObject jsonObject) {
        try {

            clinic_type obj=new clinic_type();
            obj.setID(jsonObject.getInt("ID"));
            obj.setType(jsonObject.getString("type"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<clinic_type> readOBJArray(String result) {
        try {
            ArrayList<clinic_type> all=new ArrayList<clinic_type>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<clinic_type>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
