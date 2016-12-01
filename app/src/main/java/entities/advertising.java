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

public class advertising extends base {


    int ID;
    String content;
    String from_date;
    String to_date;
    String owner;




    public advertising() {


    }

    @Override
    public RequestParams getEntity() {
        RequestParams jsonRequest = null;
        try {
            jsonRequest = new RequestParams();
            jsonRequest.put("content", content);
            jsonRequest.put("from_date", from_date);
            jsonRequest.put("to_date", to_date);
            jsonRequest.put("owner", owner);





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
            jsonRequest.put("from_date", from_date);
            jsonRequest.put("to_date", to_date);
            jsonRequest.put("owner", owner);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  jsonRequest;
    }


    public static advertising readSingleOBJ(JSONObject jsonObject) {
        try {

            advertising obj=new advertising();
            obj.setID(jsonObject.getInt("ID"));
            obj.setContent(jsonObject.getString("content"));
            obj.setFrom_date(jsonObject.getString("from_date"));
            obj.setTo_date(jsonObject.getString("to_date"));
            obj.setOwner(jsonObject.getString("owner"));

            return obj;
        } catch (Exception e) {

        }

        return null;
    }


    public static ArrayList<advertising> readOBJArray(String result) {
        try {
            ArrayList<advertising> all=new ArrayList<advertising>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                all.add(readSingleOBJ(array.getJSONObject(i)));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<advertising>();
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

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
