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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class advice extends base {


    int ID;
    String content;
    String a_date;
    String doctor_ID;




    public advice() {


    }

    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("content", content);
           // jsonRequest.put("a_date", a_date);
            jsonRequest.put("doctor_ID", doctor_ID);





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
            jsonRequest.put("content", content);
           /// jsonRequest.put("a_date", a_date);
            jsonRequest.put("doctor_ID", doctor_ID);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static advice readSingleOBJ(JSONObject jsonObject) {
        try {

            advice obj=new advice();
            obj.setID(jsonObject.getInt("ID"));
            obj.setContent(jsonObject.getString("content"));
           // Timestamp timestamp =  Timestamp.valueOf(jsonObject.getString("a_date"));
           // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            //obj.setA_date(simpleDateFormat.format(timestamp));
            obj.setA_date(jsonObject.getString("a_date").substring(0,10));
            obj.setDoctor_ID(jsonObject.getString("doctor_ID"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<advice> readOBJArray(String result) {
        try {
            ArrayList<advice> all=new ArrayList<advice>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<advice>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getA_date() {
        return a_date;
    }

    public void setA_date(String a_date) {
        this.a_date = a_date;
    }

    public String getDoctor_ID() {
        return doctor_ID;
    }

    public void setDoctor_ID(String doctor_ID) {
        this.doctor_ID = doctor_ID;
    }
}
