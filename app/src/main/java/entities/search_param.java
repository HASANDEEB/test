package entities;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AL_DEEB on 11/06/2016.
 */
public class search_param implements Serializable{
 public  String token,spec,name,ar,ci,type;


 @Override
 public String toString() {
  return "search_param{" +
          "token='" + token + '\'' +
          ", spec='" + spec + '\'' +
          ", name='" + name + '\'' +
          ", ar='" + ar + '\'' +
          ", ci='" + ci + '\'' +
          '}';
 }
}
