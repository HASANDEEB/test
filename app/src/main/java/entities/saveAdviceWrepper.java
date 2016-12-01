package entities;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by AL_DEEB on 12/06/2016.
 */
public class saveAdviceWrepper {

    int  sa_id;
    String doc;
    advice adv;

    public saveAdviceWrepper(int sa_id, String doc, advice adv) {
        this.sa_id = sa_id;
        this.doc = doc;
        this.adv = adv;
    }


    public int getSa_id() {
        return sa_id;
    }

    public void setSa_id(int sa_id) {
        this.sa_id = sa_id;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public advice getAdv() {
        return adv;
    }

    public void setAdv(advice adv) {
        this.adv = adv;
    }

    public static ArrayList<saveAdviceWrepper> readOBJArray(String result) {
        try {
            ArrayList<saveAdviceWrepper> all=new ArrayList<saveAdviceWrepper>();
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {


                advice adv=advice.readSingleOBJ(array.getJSONObject(i));
                all.add(new saveAdviceWrepper(array.getJSONObject(i).getInt("sa_id"),
                                              array.getJSONObject(i).getString("name"),adv));
            }
            return all;
        } catch (Exception e) {

        }
        return new ArrayList<saveAdviceWrepper>();
    }

}
