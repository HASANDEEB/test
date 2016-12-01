/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;


import android.widget.Toast;

import com.apps.scit.tabibihon.clinicRegisteration;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class doctor extends base {


    int ID;
    String username;
    String  password;
    String  token;
    String  fname;
    String mname;
    String lname;
    String university;
    String additional_info;
    String email;
    String phone;
    String mobile;
    String authorized;
    String register_date;
    String specialization_ID;
    String clinic_ID;
    String image;
    String type;
    String visits;
   String accessToken="";
   String facebookID="";

    public doctor() {
        accessToken="";

    }

    public doctor( String username, String password, String token, String fname, String mname,
                  String lname, String university, String additional_info, String email, String phone,
                  String mobile, String authorized, String register_date, String specialization_ID, String clinic_ID,String image) {

        this.username = username;
        this.password = password;
        this.token = token;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.university = university;
        this.additional_info = additional_info;
        this.email = email;
        this.phone = phone;
        this.mobile = mobile;
        this.authorized = authorized;
        this.register_date = register_date;
        this.specialization_ID = specialization_ID;
        this.clinic_ID = clinic_ID;
        this.image = image;
    }

    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("username", username);
            jsonRequest.put("password", password);
            jsonRequest.put("token", token);
            jsonRequest.put("fname", fname);
            jsonRequest.put("mname", mname);
            jsonRequest.put("lname", lname);
            jsonRequest.put("university", university);
            jsonRequest.put("additional_info", additional_info);
            jsonRequest.put("email", email);
            jsonRequest.put("phone", phone);
            jsonRequest.put("mobile", mobile);
            jsonRequest.put("authorized", authorized);
            jsonRequest.put("register_date", register_date);
            jsonRequest.put("specialization_ID", (specialization_ID.equals("null"))?"":specialization_ID);
            jsonRequest.put("clinic_ID", clinic_ID);
            jsonRequest.put("image", image);
            jsonRequest.put("type", type);
            jsonRequest.put("facebookID", facebookID);



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
            jsonRequest.put("username", username);
            jsonRequest.put("password", password);
            jsonRequest.put("token", token);
            jsonRequest.put("fname", fname);
            jsonRequest.put("mname", mname);
            jsonRequest.put("lname", lname);
            jsonRequest.put("university", university);
            jsonRequest.put("additional_info", additional_info);
            jsonRequest.put("email", email);
            jsonRequest.put("phone", phone);
            jsonRequest.put("mobile", mobile);
            jsonRequest.put("authorized", authorized);
            jsonRequest.put("register_date", register_date);
            jsonRequest.put("specialization_ID", specialization_ID);
            jsonRequest.put("clinic_ID", clinic_ID);
            jsonRequest.put("image", image);
            jsonRequest.put("type", type);
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
            jsonRequest.put("fname", fname);
            jsonRequest.put("mname", mname);
            jsonRequest.put("lname", lname);
            jsonRequest.put("university", university);
            jsonRequest.put("additional_info", additional_info);
            jsonRequest.put("email", email);
            jsonRequest.put("phone", phone);
            jsonRequest.put("mobile", mobile);
            jsonRequest.put("authorized", authorized);
            jsonRequest.put("register_date", register_date);
            jsonRequest.put("specialization_ID", specialization_ID);
            jsonRequest.put("clinic_ID", clinic_ID);
            jsonRequest.put("image", image);
            jsonRequest.put("type", type);
            jsonRequest.put("facebookID", facebookID);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static doctor readSingleOBJ(JSONObject jsonObject) {
        try {

            doctor obj=new doctor();
            obj.setID(jsonObject.getInt("ID"));
            obj.setUsername(jsonObject.getString("username"));
            obj.setPassword(jsonObject.getString("password"));
            obj.setToken(jsonObject.getString("token"));
            obj.setFname(jsonObject.getString("fname"));
            obj.setMname(jsonObject.getString("mname"));
            obj.setLname(jsonObject.getString("lname"));
            obj.setUniversity(jsonObject.getString("university"));
            obj.setAdditional_info(jsonObject.getString("additional_info"));
            obj.setEmail(jsonObject.getString("email"));
            obj.setPhone(jsonObject.getString("phone"));
            obj.setMobile(jsonObject.getString("mobile"));
            obj.setAuthorized(jsonObject.getString("authorized"));
            obj.setRegister_date(jsonObject.has("register_date") ? jsonObject.getString("register_date") : "");
            obj.setSpecialization_ID(jsonObject.has("specialization_ID") ? jsonObject.getString("specialization_ID") : "");
            obj.setImage(jsonObject.getString("image"));
            obj.setClinic_ID(jsonObject.getString("clinic_ID"));
            obj.setType(jsonObject.getString("type"));
            obj.setVisits((jsonObject.has("visits"))?jsonObject.getString("visits"):"0");
            obj.setFacebookID((jsonObject.has("facebookID"))?jsonObject.getString("facebookID"):"");
            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<doctor> readOBJArray(String result) {
        try {
            ArrayList<doctor> all=new ArrayList<doctor>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<doctor>();
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSpecialization_ID() {
        return specialization_ID;
    }

    public void setSpecialization_ID(String specialization_ID) {
        this.specialization_ID = specialization_ID;
    }

    public String getClinic_ID() {
        return clinic_ID;
    }

    public void setClinic_ID(String clinic_ID) {
        this.clinic_ID = clinic_ID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVisits() {
        return visits;
    }

    public void setVisits(String visits) {
        this.visits = visits;
    }

    @Override
    public String toString() {
        return "doctor{" +
                "ID=" + ID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", fname='" + fname + '\'' +
                ", mname='" + mname + '\'' +
                ", lname='" + lname + '\'' +
                ", university='" + university + '\'' +
                ", additional_info='" + additional_info + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", mobile='" + mobile + '\'' +
                ", authorized='" + authorized + '\'' +
                ", register_date='" + register_date + '\'' +
                ", specialization_ID='" + specialization_ID + '\'' +
                ", clinic_ID='" + clinic_ID + '\'' +
                ", image='" + image + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
