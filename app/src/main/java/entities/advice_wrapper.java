package entities;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AL_DEEB on 11/06/2016.
 */
public class advice_wrapper implements Serializable{
    advice adv;
    int updated;

    public advice_wrapper(advice adv, int updated)  {
        this.adv = adv;
        this.updated = updated;
    }


    public advice getAdv() {
        return adv;
    }

    public void setAdv(advice adv) {
        this.adv = adv;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }


    public static ArrayList<advice_wrapper> readOBJArray(String result) {
        try {
            ArrayList<advice_wrapper> all=new ArrayList<advice_wrapper>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {

                advice adv=advice.readSingleOBJ(array.getJSONObject(i));
                all.add(new advice_wrapper(adv, array.getJSONObject(i).getInt("checked")));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<advice_wrapper>();
    }

}
