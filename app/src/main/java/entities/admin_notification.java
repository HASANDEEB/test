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

public class admin_notification extends base {


    int ID;
    String notification;
    String n_time;
    String flag;




    public admin_notification() {


    }

    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {

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

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static admin_notification readSingleOBJ(JSONObject jsonObject) {
        try {

            admin_notification obj=new admin_notification();
            obj.setID(jsonObject.getInt("ID"));
            obj.setNotification(jsonObject.getString("notification"));
            obj.setN_time(jsonObject.getString("n_time"));
            obj.setFlag(jsonObject.getString("flag"));
           ;

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<admin_notification> readOBJArray(String result) {
        try {
            ArrayList<admin_notification> all=new ArrayList<admin_notification>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<admin_notification>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getN_time() {
        return n_time;
    }

    public void setN_time(String n_time) {
        this.n_time = n_time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
