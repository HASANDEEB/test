package entities;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AL_DEEB on 15/06/2016.
 */
public class notification_wrapper implements Serializable {

    int id;
    advice adv;
    String doc;
    int flag;
    int saved;


    public notification_wrapper(int id, advice adv, String doc,int flag,int saved) {
        this.id = id;
        this.adv = adv;
        this.doc = doc;
        this.flag = flag;
        this.saved = saved;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getSaved() {
        return saved;
    }

    public void setSaved(int saved) {
        this.saved = saved;
    }

    public static ArrayList<notification_wrapper> readOBJArray(String result) {
        try {
            ArrayList<notification_wrapper> all=new ArrayList<notification_wrapper>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {

                advice adv=advice.readSingleOBJ(array.getJSONObject(i));

                notification_wrapper ntw=new notification_wrapper(array.getJSONObject(i).getInt("notifi_ID"),adv,
                        array.getJSONObject(i).getString("name"),array.getJSONObject(i).getInt("flag"),array.getJSONObject(i).getInt("checked"));

                all.add(ntw);
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<notification_wrapper>();
    }

}
