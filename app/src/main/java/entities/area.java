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

public class area extends base {


    int ID;
    String name;
    String location;
    String city_ID;




    public area() {


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
            jsonRequest.put("location", location);
            jsonRequest.put("city_ID", city_ID);





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
            jsonRequest.put("location", location);
            jsonRequest.put("city_ID", city_ID);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static area readSingleOBJ(JSONObject jsonObject) {
        try {

            area obj=new area();
            obj.setID(jsonObject.getInt("ID"));
            obj.setName(jsonObject.getString("name"));
            obj.setLocation(jsonObject.getString("location"));
            obj.setCity_ID(jsonObject.getString("city_ID"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<area> readOBJArray(String result) {
        try {
            ArrayList<area> all=new ArrayList<area>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<area>();
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity_ID() {
        return city_ID;
    }

    public void setCity_ID(String city_ID) {
        this.city_ID = city_ID;
    }
}
