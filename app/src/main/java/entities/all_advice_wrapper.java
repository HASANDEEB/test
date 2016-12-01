package entities;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AL_DEEB on 11/06/2016.
 */
public class all_advice_wrapper implements Serializable{
    advice adv;
    String  doc;
    String  img;
    String  like;
    String  checked;
    String  doc_spec;
    String  doc_type;
    int  doc_ID;


    public all_advice_wrapper(advice adv, String doc, String img, String like,String checked,int doc_ID,String doc_spec,String doc_type)  {
        this.adv = adv;
        this.doc = doc;
        this.img = img;
        this.img = img;
        this.like = like;
        this.checked = checked;
        this.doc_spec = doc_spec;
        this.doc_ID = doc_ID;
        this.doc_type = doc_type;
    }


    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public advice getAdv() {
        return adv;
    }

    public void setAdv(advice adv) {
        this.adv = adv;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getDoc_spec() {
        return doc_spec;
    }

    public void setDoc_spec(String doc_spec) {
        this.doc_spec = doc_spec;
    }

    public int getDoc_ID() {
        return doc_ID;
    }

    public void setDoc_ID(int doc_ID) {
        this.doc_ID = doc_ID;
    }

    public static ArrayList<all_advice_wrapper> readOBJArray(String result) {
        try {
            ArrayList<all_advice_wrapper> all=new ArrayList<all_advice_wrapper>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {

                advice adv=advice.readSingleOBJ(array.getJSONObject(i));
                all.add(new all_advice_wrapper(adv, array.getJSONObject(i).getString("doctor"),
                        array.getJSONObject(i).getString("image"), array.getJSONObject(i).getString("likes")
                        , array.getJSONObject(i).getString("checked"), array.getJSONObject(i).getInt("doc_ID")
                        , array.getJSONObject(i).getString("doc_spec") ,
                        (array.getJSONObject(i).has("doc_type"))?array.getJSONObject(i).getString("doc_type"):"0"));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<all_advice_wrapper>();
    }

}
