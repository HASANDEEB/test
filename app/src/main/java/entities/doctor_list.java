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

public class doctor_list extends base {


    int ID;
    String user_ID;
    String  doctor_ID;


    public doctor_list() {


    }



    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("user_ID", user_ID);
            jsonRequest.put("doctor_ID", doctor_ID);

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
            jsonRequest.put("user_ID", user_ID);
            jsonRequest.put("doctor_ID", doctor_ID);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static doctor_list readSingleOBJ(JSONObject jsonObject) {
        try {

            doctor_list obj=new doctor_list();
            obj.setID(jsonObject.getInt("ID"));
            obj.setUser_ID(jsonObject.getString("user_ID"));
            obj.setDoctor_ID(jsonObject.getString("doctor_ID"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<doctor_list> readOBJArray(String result) {
        try {
            ArrayList<doctor_list> all=new ArrayList<doctor_list>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<doctor_list>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getDoctor_ID() {
        return doctor_ID;
    }

    public void setDoctor_ID(String doctor_ID) {
        this.doctor_ID = doctor_ID;
    }
}
