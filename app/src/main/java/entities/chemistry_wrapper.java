/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;


import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class chemistry_wrapper implements Serializable {


    int ID;
    String c_date;
    String name;
    String location;
    String description;
    String area_ID;
    String type;
    String s_time;
    String s_shift_time;
    String e_time;
    String e_shift_time;
    String loc;



    public chemistry_wrapper() {


    }




    public static chemistry_wrapper readSingleOBJ(JSONObject jsonObject) {
        try {

            chemistry_wrapper obj=new chemistry_wrapper();
            obj.setID(jsonObject.getInt("ID"));
            obj.setLocation(jsonObject.getString("location"));
            obj.setC_date(jsonObject.getString("c_date"));
            obj.setArea_ID(jsonObject.getString("area_ID"));
            obj.setDescription(jsonObject.getString("description"));
            obj.setName(jsonObject.getString("name"));
            obj.setType(jsonObject.getString("type"));
            obj.setS_time(jsonObject.getString("s_time"));
            obj.setE_time(jsonObject.getString("e_time"));
            obj.setS_shift_time(jsonObject.getString("s_shift_time"));
            obj.setE_shift_time(jsonObject.getString("e_shift_time"));
            obj.setLoc(jsonObject.getString("loc"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<chemistry_wrapper> readOBJArray(String result) {
        try {
            ArrayList<chemistry_wrapper> all=new ArrayList<chemistry_wrapper>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<chemistry_wrapper>();
    }


    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getS_shift_time() {
        return s_shift_time;
    }

    public void setS_shift_time(String s_shift_time) {
        this.s_shift_time = s_shift_time;
    }

    public String getE_shift_time() {
        return e_shift_time;
    }

    public void setE_shift_time(String e_shift_time) {
        this.e_shift_time = e_shift_time;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getC_date() {
        return c_date;
    }

    public void setC_date(String c_date) {
        this.c_date = c_date;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea_ID() {
        return area_ID;
    }

    public void setArea_ID(String area_ID) {
        this.area_ID = area_ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getS_time() {
        return s_time;
    }

    public void setS_time(String s_time) {
        this.s_time = s_time;
    }

    public String getE_time() {
        return e_time;
    }

    public void setE_time(String e_time) {
        this.e_time = e_time;
    }
}
