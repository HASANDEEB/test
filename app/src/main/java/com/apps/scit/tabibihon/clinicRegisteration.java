package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import entities.area;
import entities.base;
import entities.city;
import entities.clinic;
import entities.country;
import entities.doctor;
import extra.MyArrayAdapter;
import extra.TextInit;


public class clinicRegisteration extends Activity {



    Spinner countries,citys,areas;
    EditText street;

    doctor doc=null;
    clinic cl=null;

    ArrayList<country> list_country=new ArrayList<country>();
    ArrayAdapter<country> adapter_country;

    ArrayList<city> list_city=new ArrayList<city>();
    ArrayAdapter<city> adapter_city;

    ArrayList<entities.area> list_area=new ArrayList<entities.area>();
    ArrayList<entities.area> all_area=new ArrayList<entities.area>();
    ArrayAdapter<entities.area> adapter_area;
    String password="";
    User user=null;




    private ProgressDialog prgDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_clinic_info);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);



            cl=new clinic("","","","","","","","","",null,"");

        citys=(Spinner)findViewById(R.id.clinic_city);
        //countries=(Spinner)findViewById(R.id.clinic_country);
        areas=(Spinner)findViewById(R.id.clinic_area);
        street=(EditText)findViewById(R.id.c_street);

        adapter_city = new MyArrayAdapter<city>(this,R.layout.spinner_item, list_city);

        citys.setAdapter(adapter_city);

        adapter_area = new MyArrayAdapter<entities.area>(this,R.layout.spinner_item, list_area);
        areas.setAdapter(adapter_area);


        getCity(0);





        if(getIntent().hasExtra("doctor")){

            doc=(doctor)getIntent().getSerializableExtra("doctor");


        } if(getIntent().hasExtra("user")){


            user=(User)getIntent().getSerializableExtra("user");


        }


        if(doc.getType().equals("1")) {
            ((TextView) findViewById(R.id.title)).setText("عنوان الصيدلية");
            ((TextView) findViewById(R.id.c_title_txt)).setText("اسم الصيدلية");
        }

        password=doc.getPassword();



        citys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String city=list_city.get(position).getID()+"";
                list_area.clear();
                for (area ar : all_area) {
                    if(ar.getCity_ID().equals(city))
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
                        continue_register();
                    }
                }
        );

    }




    private void continue_register() {

        String c_name=((TextView) findViewById(R.id.c_title)).getText().toString();
        ((TextView) findViewById(R.id.c_title)).setError(null);

                if(areas.getSelectedItemPosition()==-1){
                    Toast.makeText(this," من فضلك اختر المنطقة ",Toast.LENGTH_SHORT).show();
                }

                else {
                    if(doc.getType().equals("1")&&TextUtils.isEmpty(c_name))
                    {

                            ((TextView) findViewById(R.id.c_title)).setError(getResources().getString(R.string.error_field_required));
                            ((TextView) findViewById(R.id.c_title)).requestFocus();


                    }else {


                        cl.setArea_ID(list_area.get(areas.getSelectedItemPosition()).getID() + "");
                        cl.setStreet(TextInit.check(street.getText().toString()));
                        cl.setName(TextInit.check(c_name));

                        prgDialog.show();
                        createAccount();
                    }

                }

    }


    private void getArea(final int count) {

        AsyncHttpClient client = new AsyncHttpClient();


        RequestParams req=new RequestParams();

        client.post(base.BASE_URL + "getAllArea", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    ArrayList<area> resAR = area.readOBJArray(response);
                    all_area.clear();
                    for (area help : resAR) {
                        all_area.add(help);
                        //
                    }
                    adapter_city.notifyDataSetChanged();
                   // adapter_area.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
                prgDialog.hide();

            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=2) {
                    getArea(count+1);
                }else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    prgDialog.hide();
                }
            }
        });



    }

    private void getCity(final int count) {

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();


        client.post(base.BASE_URL+"getCity", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    ArrayList<city> resAR = city.readOBJArray(response);
                    list_city.clear();
                    for (city help : resAR) {
                        list_city.add(help);
                        //
                    }
                    getArea(0);



                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    prgDialog.show();
                }


            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {

                if(count!=2){
                    getCity(count+1);
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }

            }
        });



    }










    void createAccount(){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req= new RequestParams();

        req.put("doctor", doc.getJsonEntity().toString());
        req.put("clinic", cl.getJsonEntity().toString());
        client.post(base.BASE_URL + "signupDoctor", req, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {


                try {
                    JSONObject arg0 = new JSONObject(response);

                    try {
                        doc = doctor.readSingleOBJ(arg0.getJSONObject("user"));
                    }catch (Exception dd){
                       // Toast.makeText(clinicRegisteration.this,dd.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    doc.setAccessToken(arg0.getString("token"));

                    if(TextUtils.isEmpty(doc.getFacebookID()))
                    {

                        if(PrefUtils.getCurrentUser(clinicRegisteration.this) != null)
                            PrefUtils.clearCurrentUser(clinicRegisteration.this);
                        save(doc.getUsername(), password, "doctor");


                    }
                    else{
                        PrefUtils.setCurrentUser(user,clinicRegisteration.this);
                    }

                    finish_();


                } catch (JSONException e) {
                    e.printStackTrace();
                    prgDialog.hide();
                }

            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                prgDialog.hide();
                showMsg(getResources().getString(R.string.error));

            }
        });



    }







    void finish_() {

        Intent it = new Intent(clinicRegisteration.this, MainActivity.class);
        it.putExtra("doctor", doc);


        startActivity(it);

        finish();

    }

    void showMsg(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    public boolean save(String username,String password,String role){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("account", true);
            editor.putString("UserName", username);
            editor.putString("PassWord", password);
            editor.putString("Role", role);
            editor.commit();
            return  false;

    }




}