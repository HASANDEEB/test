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

public class save_advice extends base {


    int ID;
    String user_ID;
    String  advice_ID;


    public save_advice() {


    }



    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("user_ID", user_ID);
            jsonRequest.put("advice_ID", advice_ID);

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
            jsonRequest.put("advice_ID", advice_ID);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static save_advice readSingleOBJ(JSONObject jsonObject) {
        try {

            save_advice obj=new save_advice();
            obj.setID(jsonObject.getInt("ID"));
            obj.setUser_ID(jsonObject.getString("user_ID"));
            obj.setAdvice_ID(jsonObject.getString("advice_ID"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<save_advice> readOBJArray(String result) {
        try {
            ArrayList<save_advice> all=new ArrayList<save_advice>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<save_advice>();
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

    public String getAdvice_ID() {
        return advice_ID;
    }

    public void setAdvice_ID(String advice_ID) {
        this.advice_ID = advice_ID;
    }
}
