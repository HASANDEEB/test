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

public class doctor_list_wrapper implements Serializable {


    String doc;
    doctor_list doc_li;
    String img;

    public doctor_list_wrapper(String doc, doctor_list doc_li,String img) {
        this.doc = doc;
        this.doc_li = doc_li;
        this.img=img;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public doctor_list getDoc_li() {
        return doc_li;
    }

    public void setDoc_li(doctor_list doc_li) {
        this.doc_li = doc_li;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public static ArrayList<doctor_list_wrapper> readOBJArray(String result) {
        try {
            ArrayList<doctor_list_wrapper> all=new ArrayList<doctor_list_wrapper>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {

                doctor_list dl=doctor_list.readSingleOBJ(array.getJSONObject(i));
                all.add(new doctor_list_wrapper(array.getJSONObject(i).getString("name"),dl,array.getJSONObject(i).getString("image")));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<doctor_list_wrapper>();
    }

}
