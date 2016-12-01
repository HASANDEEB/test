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

public class user extends base {


    int ID;
    String username;
    String  password;
    String  token;
    String  mobile_serialnumber;
    String status;
    String authorized;
    String register_date;
    String facebookID="";
    String accessToken="";


    public user() {


    }

    public user(String username, String password, String token, String mobile_serialnumber, String status, String authorized, String register_date) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.mobile_serialnumber = mobile_serialnumber;
        this.status = status;
        this.authorized = authorized;
        this.register_date = register_date;
    }

    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("username", username);
            jsonRequest.put("password", password);
            jsonRequest.put("token", token);
            jsonRequest.put("mobile_serialnumber", mobile_serialnumber);
            jsonRequest.put("status", status);
            jsonRequest.put("authorized", authorized);
            jsonRequest.put("register_date", register_date);
            jsonRequest.put("facebookID", facebookID);



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
            jsonRequest.put("username", username);
            jsonRequest.put("password", password);
            jsonRequest.put("token", token);
            jsonRequest.put("mobile_serialnumber", mobile_serialnumber);
            jsonRequest.put("status", status);
            jsonRequest.put("authorized", authorized);
            jsonRequest.put("facebookID", facebookID);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static user readSingleOBJ(JSONObject jsonObject) {
        try {

            user obj=new user();
            obj.setID(jsonObject.getInt("ID"));
            obj.setUsername(jsonObject.getString("username"));
            obj.setPassword(jsonObject.getString("password"));
            obj.setToken(jsonObject.getString("token"));
            obj.setMobile_serialnumber(jsonObject.getString("mobile_serialnumber"));
            obj.setStatus(jsonObject.getString("status"));
            obj.setAuthorized(jsonObject.getString("authorized"));
            obj.setRegister_date(jsonObject.has("register_date") ? jsonObject.getString("register_date") : "");
            obj.setFacebookID((jsonObject.has("facebookID"))?jsonObject.getString("facebookID"):"");
            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<user> readOBJArray(String result) {
        try {
            ArrayList<user> all=new ArrayList<user>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<user>();
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile_serialnumber() {
        return mobile_serialnumber;
    }

    public void setMobile_serialnumber(String mobile_serialnumber) {
        this.mobile_serialnumber = mobile_serialnumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthorized() {
        return authorized;
    }

    public void setAuthorized(String authorized) {
        this.authorized = authorized;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

