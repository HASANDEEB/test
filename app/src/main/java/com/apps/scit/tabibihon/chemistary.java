package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import costumize.ArabTextView;
import entities.area;
import entities.base;
import entities.chemistry_wrapper;
import entities.city;
import entities.doctor;
import extra.ExpandableListAdapter;
import extra.MyArrayAdapter;
import extra.custom_ChemestaryAdapter;


public class chemistary extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<chemistry_wrapper>> listDataChild;



    Spinner countries,citys,areas;
    ArrayList<city> list_city=new ArrayList<city>();
    ArrayAdapter<city> adapter_city;




    ArrayList<entities.area> list_area=new ArrayList<entities.area>();
    ArrayList<Integer> list_area_ID=new ArrayList<Integer>();
    ArrayList<chemistry_wrapper> all_list_chemistary=new ArrayList<chemistry_wrapper>();
    ArrayList<chemistry_wrapper> list_chemistary=new ArrayList<chemistry_wrapper>();
    ArrayList<entities.area> all_area=new ArrayList<entities.area>();
    ArrayAdapter<entities.area> adapter_area;
    private ProgressDialog prgDialog;
    custom_ChemestaryAdapter adapter_chimistary;
    //LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_chemistary);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        citys=(Spinner)findViewById(R.id.clinic_city);
        areas=(Spinner)findViewById(R.id.clinic_area);
        adapter_city = new MyArrayAdapter<city>(this,R.layout.spinner_item, list_city);
        citys.setAdapter(adapter_city);
        adapter_area = new MyArrayAdapter<entities.area>(this,R.layout.spinner_item, list_area);
        areas.setAdapter(adapter_area);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<chemistry_wrapper>>();
        listDataHeader.add("0");
        listDataHeader.add("1");

        listDataChild.put("0", new ArrayList<chemistry_wrapper>());
        listDataChild.put("1", new ArrayList<chemistry_wrapper>());

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        //adapter_chimistary=new custom_ChemestaryAdapter(this,list_chemistary);
       // ((ListView)findViewById(R.id.chemistary)).setAdapter(adapter_chimistary);


        getData(0);



        ((Button)findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                }
        );


        citys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String city = list_city.get(position).getID() + "";
                list_area.clear();
                list_area_ID.clear();
                area all=new area();
                all.setID(0);
                all.setName("كل المناطق");
                list_area.add(all);
                for (area ar : all_area) {
                    if (ar.getCity_ID().equals(city)) {
                        list_area.add(ar);
                        list_area_ID.add(ar.getID());
                    }
                    //
                }
                adapter_area = new MyArrayAdapter<entities.area>(chemistary.this,R.layout.spinner_item, list_area);
                areas.setAdapter(adapter_area);

                adapter_area.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        areas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                getChemestary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }


    void getChemestary(){
        if (list_area.get(areas.getSelectedItemPosition()).getID()==-1)
        {
            Toast.makeText(this,"من فضلك اختر منطقة",Toast.LENGTH_SHORT).show();
            return;
        }


        String ar=list_area.get(areas.getSelectedItemPosition()).getID()+"";

        //list_chemistary.clear();
        listDataChild.get("0").clear();
        listDataChild.get("1").clear();


        for(chemistry_wrapper ch : all_list_chemistary){
            if(ar.equals("0")){
                if(list_area_ID.contains(Integer.parseInt(ch.getArea_ID()))) {
                    //list_chemistary.add(ch);
                    listDataChild.get(ch.getType()).add(ch);
                }
            }
            else
            if(ch.getArea_ID().equals(ar))
                listDataChild.get(ch.getType()).add(ch);
                //list_chemistary.add(ch);
        }


      //  if(list_chemistary.size()==0){
        if(listDataChild.get("0").size()==0&&listDataChild.get("1").size()==0){

            findViewById(R.id.lvExp).setVisibility(View.GONE);
            findViewById(R.id.noresult).setVisibility(View.VISIBLE);

        }
        else
        {
            findViewById(R.id.lvExp).setVisibility(View.VISIBLE);
            findViewById(R.id.noresult).setVisibility(View.GONE);
        }
       // adapter_chimistary.notifyDataSetChanged();
        listAdapter.notifyDataSetChanged();




    }





    private void getData(final int count) {

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();


        client.post(base.BASE_URL+"get_chemistary", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    JSONObject obj=new JSONObject(response);
                    ArrayList<city> resAR = city.readOBJArray(obj.getJSONArray("city").toString());
                    list_city.clear();
                    for (city help : resAR) {
                        list_city.add(help);
                        //
                    }

                    ArrayList<area> resARea = area.readOBJArray(obj.getJSONArray("area").toString());
                    all_area.clear();
                    for (area help : resARea) {
                        all_area.add(help);
                        //
                    }
                    ArrayList<chemistry_wrapper> resARch = chemistry_wrapper.readOBJArray(obj.getJSONArray("chemistry").toString());
                    all_list_chemistary.clear();
                    for (chemistry_wrapper help : resARch) {
                        all_list_chemistary.add(help);
                        //
                    }

                    adapter_city.notifyDataSetChanged();


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();

                }

                prgDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5)
                    getData(count+1);
                else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    prgDialog.hide();
                }

            }
        });



    }

}