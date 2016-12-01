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

public class country extends base {


    int ID;
    String name;
    String 	capital;
    String currency;
    String language;
    String timezone;


    @Override
    public String toString() {
        return name;
    }

    public country() {


    }

    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("name", name);
            jsonRequest.put("capital", capital);
            jsonRequest.put("currency", currency);
            jsonRequest.put("language", language);
            jsonRequest.put("timezone", timezone);




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
            jsonRequest.put("capital", capital);
            jsonRequest.put("currency", currency);
            jsonRequest.put("language", language);
            jsonRequest.put("timezone", timezone);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static country readSingleOBJ(JSONObject jsonObject) {
        try {

            country obj=new country();
            obj.setID(jsonObject.getInt("ID"));
            obj.setName(jsonObject.getString("name"));
            obj.setCapital(jsonObject.getString("capital"));
            obj.setCurrency(jsonObject.getString("currency"));
            obj.setLanguage(jsonObject.getString("language"));
            obj.setTimezone(jsonObject.getString("timezone"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<country> readOBJArray(String result) {
        try {
            ArrayList<country> all=new ArrayList<country>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<country>();
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

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
