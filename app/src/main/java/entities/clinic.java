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

public class clinic extends base {


    int ID;
    String name;
    String  area;
    String  street;
    String  description;
    String location;
    String phone;
    String mobile;
    String email;
    String website;
    String type_id;
    String area_ID;


    public clinic() {

    }

    public clinic( String name, String area, String street, String description, String location, String phone, String mobile, String email, String website, String type_id, String area_ID) {

        this.name = name;
        this.area = area;
        this.street = street;
        this.description = description;
        this.location = location;
        this.phone = phone;
        this.mobile = mobile;
        this.email = email;
        this.website = website;
        this.type_id = type_id;
        this.area_ID = area_ID;
    }

    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("name", name);
            jsonRequest.put("area", area);
            jsonRequest.put("street", street);
            jsonRequest.put("description", description);
            jsonRequest.put("location", location);
            jsonRequest.put("phone", phone);
            jsonRequest.put("mobile", mobile);
            jsonRequest.put("email", email);
            jsonRequest.put("website", website);
            jsonRequest.put("type_id", type_id);
            jsonRequest.put("area_ID", area_ID);



        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }

    public JSONObject getJsonEntity() {
        JSONObject jsonRequest = null;
        try {
            jsonRequest = new JSONObject();
            jsonRequest.put("name", name);
            jsonRequest.put("area", area);
            jsonRequest.put("street", street);
            jsonRequest.put("description", description);
            jsonRequest.put("location", location);
            jsonRequest.put("phone", phone);
            jsonRequest.put("mobile", mobile);
            jsonRequest.put("email", email);
            jsonRequest.put("website", website);
            jsonRequest.put("type_id", type_id);
            jsonRequest.put("area_ID", area_ID);



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
            jsonRequest.put("area", area);
            jsonRequest.put("street", street);
            jsonRequest.put("description", description);
            jsonRequest.put("location", location);
            jsonRequest.put("phone", phone);
            jsonRequest.put("mobile", mobile);
            jsonRequest.put("email", email);
            jsonRequest.put("website", website);
            jsonRequest.put("type_id", type_id);
            jsonRequest.put("area_ID", area_ID);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static clinic readSingleOBJ(JSONObject jsonObject) {
        try {

            clinic obj=new clinic();
            obj.setID(jsonObject.getInt("ID"));
            obj.setName(jsonObject.getString("name"));
            obj.setArea(jsonObject.getString("area"));
            obj.setStreet(jsonObject.getString("street"));
            obj.setDescription(jsonObject.getString("description"));
            obj.setLocation(jsonObject.getString("location"));
            obj.setPhone(jsonObject.getString("phone"));
            obj.setMobile(jsonObject.getString("mobile"));
            obj.setEmail(jsonObject.getString("email"));
            obj.setWebsite(jsonObject.getString("website"));
            obj.setType_id(jsonObject.getString("type_id"));
            obj.setArea_ID(jsonObject.getString("area_ID"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<clinic> readOBJArray(String result) {
        try {
            ArrayList<clinic> all=new ArrayList<clinic>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<clinic>();
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getArea_ID() {
        return area_ID;
    }

    public void setArea_ID(String area_ID) {
        this.area_ID = area_ID;
    }
}
