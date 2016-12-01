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

public class specialization extends base {


    int ID;
    String specialization;


    @Override
    public String toString() {
        return
               specialization ;
    }

    public specialization() {
    }
    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("specialization", specialization);

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
            jsonRequest.put("specialization", specialization);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static specialization readSingleOBJ(JSONObject jsonObject) {
        try {

            specialization obj=new specialization();
            obj.setID(jsonObject.getInt("ID"));
            obj.setSpecialization(jsonObject.getString("specialization"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<specialization> readOBJArray(String result) {
        try {
            ArrayList<specialization> all=new ArrayList<specialization>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<specialization>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
