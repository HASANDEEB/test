package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import costumize.ArabCheckBox;
import entities.area;
import entities.base;
import entities.city;
import entities.country;
import entities.doctor;
import entities.result;
import entities.search_param;
import entities.specialization;
import entities.user;
import extra.TextInit;


public class search extends Activity {

    doctor doc=null;
    user us=null;

    private Spinner speciality_spinner,city_spinner,type_spinner,area_spinner;
    EditText name_text;
    boolean alldoctor=true,mapSearch=false,distanceSearch=false;
    LinearLayout  searchByNmae;
    ArabCheckBox chBox,mapBox;


    ArrayList<result>doctors=new ArrayList<result>();


    ArrayList<specialization> list_speciality=new ArrayList<specialization>();
    ArrayAdapter<specialization> adapter_speciality;

    ArrayList<city> list_city=new ArrayList<city>();
    ArrayAdapter<city> adapter_city;

    ArrayList<String> list_type=new ArrayList<String>();
    ArrayAdapter<String> adapter_type;


    ArrayList<country> list_country=new ArrayList<country>();
    ArrayAdapter<country> adapter_country;

    ArrayList<area> list_area=new ArrayList<area>();
    ArrayList<area> all_area=new ArrayList<area>();
    ArrayAdapter<area> adapter_area;

    private ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        if(getIntent().hasExtra("doctor"))
        {

            doc=(doctor)getIntent().getSerializableExtra("doctor");


        }
        else
        if(getIntent().hasExtra("user")){
            us=(user)getIntent().getSerializableExtra("user");

        }


        list_type.add("طبيب");
        list_type.add("صيدلاني");

        speciality_spinner=(Spinner)findViewById(R.id.speciality_spinner);
        city_spinner=(Spinner)findViewById(R.id.city_spinner);
        // country_spinner=(Spinner)findViewById(R.id.region_spinner);
        area_spinner=(Spinner)findViewById(R.id.area_spinner);
        type_spinner=(Spinner)findViewById(R.id.type_spinner);




        name_text=(EditText)findViewById(R.id.search_name);
        //distance_text=(EditText)findViewById(R.id.search_distance);
        chBox=(ArabCheckBox)findViewById(R.id.all_doctor_check);
        mapBox=(ArabCheckBox)findViewById(R.id.map_check);
        searchByNmae=(LinearLayout)findViewById(R.id.name_check);
        adapter_speciality = new ArrayAdapter<specialization>(this,R.layout.spinner_item, list_speciality);
        speciality_spinner.setAdapter(adapter_speciality);

        adapter_city = new ArrayAdapter<city>(this,R.layout.spinner_item, list_city);
        city_spinner.setAdapter(adapter_city);


        adapter_area = new ArrayAdapter<area>(this,R.layout.spinner_item, list_area);
        area_spinner.setAdapter(adapter_area);
        adapter_type = new ArrayAdapter<String>(this,R.layout.spinner_item, list_type);
        type_spinner.setAdapter(adapter_type);


        getCity(0);



        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1)
                {
                    findViewById(R.id.spec).setVisibility(View.GONE);
                }
                else
                {
                    findViewById(R.id.spec).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String city = list_city.get(position).getID() + "";
                list_area.clear();
                area all = new area();
                all.setID(0);
                all.setCity_ID("0");
                all.setName("الكل");
                list_area.add(all);
                for (area ar : all_area) {
                    if (city.equals("0") || ar.getCity_ID().equals(city))
                        list_area.add(ar);
                    //
                }
                adapter_area.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        ((Button)findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search();
                    }
                }
        );


        ((SeekBar)findViewById(R.id.dis)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView)findViewById(R.id.dis_val)).setText(":  "+progress+"كم");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    private void search(){

        String token="";
//        if(doc==null)
//            token=us.getAccessToken();
//        else
//            token=doc.getAccessToken();
        name_text.setError(null);

        // Store values at the time of the login attempt.

        String name = name_text.getText().toString();
        //String distance = distance_text.getText().toString();
        double distance_=-1.0;
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(name)&&!alldoctor) {
            name_text.setError(getString(R.string.error_field_required));
            focusView = name_text;
            cancel = true;
        }




        if(area_spinner.getSelectedItemPosition()==-1){
            Toast.makeText(getApplicationContext(), "من فضلك اختر المنطقة", Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        if(city_spinner.getSelectedItemPosition()==-1){
            Toast.makeText(getApplicationContext(), "من فضلك اختر المدينة", Toast.LENGTH_SHORT).show();
            cancel = true;
        }
        if(!cancel) {

            if(alldoctor)
                name="";
            else name= TextInit.check(name);

            RequestParams req = new RequestParams();
            req.put("token", token);
            req.put("type", type_spinner.getSelectedItemPosition()+"");
            req.put("specialization_ID", (type_spinner.getSelectedItemPosition()==0)?list_speciality.get(speciality_spinner.getSelectedItemPosition()).getID() + "":"-1");
            req.put("name", name);
            req.put("area_ID", list_area.get(area_spinner.getSelectedItemPosition()).getID() + "");
            req.put("city_ID", list_city.get(city_spinner.getSelectedItemPosition()).getID() + "");

            search_param sreq = new search_param();
            sreq.token=token;
            sreq.spec= (type_spinner.getSelectedItemPosition()==0)?list_speciality.get(speciality_spinner.getSelectedItemPosition()).getID() + "":"-1";
            sreq.name= name;
            sreq.ar=list_area.get(area_spinner.getSelectedItemPosition()).getID() + "";
            sreq.ci= list_city.get(city_spinner.getSelectedItemPosition()).getID() + "";
            sreq.type=type_spinner.getSelectedItemPosition()+"";
            if(mapSearch)
            {
                if(isGooglePlayServicesAvailable()) {
                    Intent ii = new Intent(search.this, searchMapsActivity.class);
                    if (us != null)
                        ii.putExtra("user", us);
                    else if (doc != null)
                        ii.putExtra("doctor", doc);

                    ii.putExtra("search", sreq);
                    if (distanceSearch)
                        ii.putExtra("distance", ((SeekBar) findViewById(R.id.dis)).getProgress());

                    startActivity(ii);
                }
                else
                {
                    Snackbar.make(findViewById(R.id.b_continue), "من فضلك قم بتحديث خدمات   Google Play",
                            Snackbar.LENGTH_LONG).show();
                }

            }else {
                do_search(req, sreq,0);
            }

        }


    }


    void do_search(final RequestParams request, final search_param sreq,final int count){

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(base.BASE_URL + "search/count", request, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {

                    JSONObject obj = new JSONObject(response);

                    if (obj.has("count"))
                        finish_(obj.getInt("count"), sreq);


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.fail, Toast.LENGTH_SHORT).show();
                }

                prgDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5){
                    do_search(request,sreq,count+1);
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    prgDialog.hide();
                }

            }
        });





    }


    private void finish_(int num,search_param sreq) {

        Intent ii = new Intent(search.this, searchResults.class);
        ii.putExtra("doctors", doctors);
        if(us!=null)
            ii.putExtra("user",us);
        else
        if(doc!=null)
            ii.putExtra("doctor", doc);

        ii.putExtra("search",sreq);
        ii.putExtra("count", num);
        startActivity(ii);

    }




    private void getCity(final int count) {

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();


        client.post(base.BASE_URL + "getSearchOption", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    JSONObject obj = new JSONObject(response);

                    ArrayList<city> resAR = city.readOBJArray(obj.getJSONArray("city").toString());

                    list_city.clear();
                    city all = new city();
                    all.setID(0);
                    all.setName("الكل");
                    list_city.add(all);
                    for (city help : resAR) {
                        list_city.add(help);
                        //
                    }
                    ArrayList<area> resAR2 = area.readOBJArray(obj.getJSONArray("area").toString());
                    list_area.clear();
                    all_area.clear();

                    area all2 = new area();
                    all2.setID(0);
                    all2.setCity_ID("0");
                    all2.setName("الكل");
                    list_area.add(all2);
                    //  all_area.add(all);
                    for (area help : resAR2) {
                        list_area.add(help);
                        all_area.add(help);
                        //
                    }
                    ArrayList<specialization> resAR3 = specialization.readOBJArray(obj.getJSONArray("specialization").toString());
                    list_speciality.clear();
                    specialization all3 = new specialization();
                    all3.setSpecialization("الكل");
                    all3.setID(0);
                    list_speciality.add(all3);
                    for (specialization help : resAR3) {
                        list_speciality.add(help);


                        //
                    }
                    adapter_speciality.notifyDataSetChanged();

                    adapter_area.notifyDataSetChanged();

                    adapter_city.notifyDataSetChanged();


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();

                }
                prgDialog.hide();

            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if (count != 5)
                    getCity(count + 1);
                else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    prgDialog.hide();
                }

            }
        });



    }

    public  void allDocSearch(View v){

        if(alldoctor){
            alldoctor=false;
            chBox.setChecked(true);
            searchByNmae.setVisibility(View.VISIBLE);
        }
        else {
            alldoctor=true;
            chBox.setChecked(false);
            searchByNmae.setVisibility(View.GONE);

        }


    }

    public  void docSearchmap(View v){

        if(mapSearch){
            mapSearch=false;
            distanceSearch=false;
            findViewById(R.id.search_doc_dis).setVisibility(View.GONE);
            ((CheckBox)findViewById(R.id.distance_check)).setChecked(false);
            mapBox.setChecked(false);
            findViewById(R.id.distance_search).setVisibility(View.GONE);
        }
        else {
            mapSearch=true;
            mapBox.setChecked(true);
            findViewById(R.id.distance_search).setVisibility(View.VISIBLE);

        }


    }

    public  void docSearchdis(View v){

        if(distanceSearch){
            distanceSearch=false;
            ((CheckBox)findViewById(R.id.distance_check)).setChecked(false);
            findViewById(R.id.search_doc_dis).setVisibility(View.GONE);
            findViewById(R.id.dis_val).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.dis_val)).setText("");
        }
        else {
            distanceSearch=true;
            ((CheckBox)findViewById(R.id.distance_check)).setChecked(true);
            findViewById(R.id.search_doc_dis).setVisibility(View.VISIBLE);
            findViewById(R.id.dis_val).setVisibility(View.VISIBLE);

        }


    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            return false;
        }
    }

}