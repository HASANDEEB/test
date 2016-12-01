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

public class voting extends base {


    int ID;
    String content;
    int number ;





    public voting() {


    }


    @Override
    public String toString() {
        return
                content ;
    }


    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("content", content);
            jsonRequest.put("number", number);






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
            jsonRequest.put("content", content);
            jsonRequest.put("number", number);


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static voting readSingleOBJ(JSONObject jsonObject) {
        try {

            voting obj=new voting();
            obj.setID(jsonObject.getInt("ID"));
            obj.setContent(jsonObject.getString("content"));
            obj.setNumber(jsonObject.getInt("number"));


            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<voting> readOBJArray(String result) {
        try {
            ArrayList<voting> all=new ArrayList<voting>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<voting>();
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
