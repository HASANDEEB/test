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

public class city extends base {


    int ID;
    String name;
    String timezone;
    String country_ID;




    public city() {


    }


    @Override
    public String toString() {
        return
                name ;
    }


    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("name", name);
            jsonRequest.put("timezone", timezone);
            jsonRequest.put("country_ID", country_ID);





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
            jsonRequest.put("name", name);
            jsonRequest.put("timezone", timezone);
            jsonRequest.put("country_ID", country_ID);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static city readSingleOBJ(JSONObject jsonObject) {
        try {

            city obj=new city();
            obj.setID(jsonObject.getInt("ID"));
            obj.setName(jsonObject.getString("name"));
            obj.setTimezone(jsonObject.getString("timezone"));
            obj.setCountry_ID(jsonObject.getString("country_ID"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<city> readOBJArray(String result) {
        try {
            ArrayList<city> all=new ArrayList<city>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<city>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCountry_ID() {
        return country_ID;
    }

    public void setCountry_ID(String country_ID) {
        this.country_ID = country_ID;
    }
}
