/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;


import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class notification extends base {


    int ID;
    String user_ID;
    String  advice_ID;
    int flag;


    public notification() {


    }



    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("user_ID", user_ID);
            jsonRequest.put("advice_ID", advice_ID);
            jsonRequest.put("flag", flag);

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
            jsonRequest.put("flag", flag);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static notification readSingleOBJ(JSONObject jsonObject) {
        try {

            notification obj=new notification();
            obj.setID(jsonObject.getInt("ID"));
            obj.setUser_ID(jsonObject.getString("user_ID"));
            obj.setAdvice_ID(jsonObject.getString("advice_ID"));
            obj.setFlag(jsonObject.getInt("flag"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<notification> readOBJArray(String result) {
        try {
            ArrayList<notification> all=new ArrayList<notification>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<notification>();
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
