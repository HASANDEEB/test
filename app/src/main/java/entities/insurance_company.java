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

public class insurance_company extends base {


    int ID;
    String name;
    String type;





    public insurance_company() {


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
            jsonRequest.put("type", type);






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
            jsonRequest.put("type", type);


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static insurance_company readSingleOBJ(JSONObject jsonObject) {
        try {

            insurance_company obj=new insurance_company();
            obj.setID(jsonObject.getInt("ID"));
            obj.setName(jsonObject.getString("name"));
            obj.setType(jsonObject.getString("type"));


            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<insurance_company> readOBJArray(String result) {
        try {
            ArrayList<insurance_company> all=new ArrayList<insurance_company>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<insurance_company>();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
