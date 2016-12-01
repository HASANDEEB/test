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

public class hospital extends base {


    int ID;
    String name;
    String phone;
    String mobile;
    String description;
    String type;
    String location;
    String city_ID;


    @Override
    public String toString() {
        return  name ;

    }

    public hospital() {


    }

    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("name", name);
            jsonRequest.put("phone", phone);
            jsonRequest.put("mobile", mobile);
            jsonRequest.put("description", description);
            jsonRequest.put("type", type);
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
            jsonRequest.put("ID", ID+"");
            jsonRequest.put("name", name);
            jsonRequest.put("phone", phone);
            jsonRequest.put("mobile", mobile);
            jsonRequest.put("description", description);
            jsonRequest.put("type", type);
            jsonRequest.put("location", location);
            jsonRequest.put("city_ID", city_ID);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static hospital readSingleOBJ(JSONObject jsonObject) {
        try {

            hospital obj=new hospital();
            obj.setID(jsonObject.getInt("ID"));
            obj.setName(jsonObject.getString("name"));
            obj.setPhone(jsonObject.getString("phone"));
            obj.setMobile(jsonObject.getString("mobile"));
            obj.setDescription(jsonObject.getString("description"));
            obj.setType(jsonObject.getString("type"));
            obj.setLocation(jsonObject.getString("location"));
            obj.setCity_ID(jsonObject.getString("city_ID"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<hospital> readOBJArray(String result) {
        try {
            ArrayList<hospital> all=new ArrayList<hospital>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<hospital>();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity_ID() {
        return city_ID;
    }

    public void setCity_ID(String city_ID) {
        this.city_ID = city_ID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
