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

public class clinic_service extends base {


    int ID;
    double price;
    String clinic_id;
    String service_id;




    public clinic_service() {


    }



    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("price", price);
            jsonRequest.put("clinic_id", clinic_id);
            jsonRequest.put("service_id", service_id);




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
            jsonRequest.put("price", price);
            jsonRequest.put("clinic_id", clinic_id);
            jsonRequest.put("service_id", service_id);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static clinic_service readSingleOBJ(JSONObject jsonObject) {
        try {

            clinic_service obj=new clinic_service();
            obj.setID(jsonObject.getInt("ID"));
            obj.setPrice(jsonObject.getDouble("price"));
            obj.setClinic_id(jsonObject.getString("clinic_id"));
            obj.setService_id(jsonObject.getString("service_id"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<clinic_service> readOBJArray(String result) {
        try {
            ArrayList<clinic_service> all=new ArrayList<clinic_service>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<clinic_service>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(String clinic_id) {
        this.clinic_id = clinic_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }
}
