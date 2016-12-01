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

public class work_time extends base {


    int ID;
    String start_time;
    String end_time;
    String description;
    String shift_time;
    String e_shift_time;
    String clinic_ID;




    public work_time() {


    }

    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("start_time", start_time);
            jsonRequest.put("end_time", end_time);
            jsonRequest.put("description", description);
            jsonRequest.put("shift_time", shift_time);
            jsonRequest.put("e_shift_time", e_shift_time);
            jsonRequest.put("clinic_ID", clinic_ID);





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
            jsonRequest.put("start_time", start_time);
            jsonRequest.put("end_time", end_time);
            jsonRequest.put("description", description);
            jsonRequest.put("shift_time", shift_time);
            jsonRequest.put("e_shift_time", e_shift_time);
            jsonRequest.put("clinic_ID", clinic_ID);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static work_time readSingleOBJ(JSONObject jsonObject) {
        try {

            work_time obj=new work_time();
            obj.setID(jsonObject.getInt("ID"));
            obj.setStart_time(jsonObject.getString("start_time"));
            obj.setEnd_time(jsonObject.getString("end_time"));
            obj.setDescription(jsonObject.getString("description"));
            obj.setS_shift_time(jsonObject.getString("shift_time"));
            obj.setE_shift_time(jsonObject.getString("e_shift_time"));
            obj.setClinic_ID(jsonObject.getString("clinic_ID"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<work_time> readOBJArray(String result) {
        try {
            ArrayList<work_time> all=new ArrayList<work_time>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<work_time>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClinic_ID() {
        return clinic_ID;
    }

    public void setClinic_ID(String clinic_ID) {
        this.clinic_ID = clinic_ID;
    }

    public String getS_shift_time() {
        return shift_time;
    }

    public void setS_shift_time(String s_shift_time) {
        this.shift_time = s_shift_time;
    }

    public String getE_shift_time() {
        return e_shift_time;
    }

    public void setE_shift_time(String e_shift_time) {
        this.e_shift_time = e_shift_time;
    }
}
