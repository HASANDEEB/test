package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import costumize.ArabTextView;
import entities.base;
import entities.city;
import entities.hospital;
import entities.hospital_wrapper;
import extra.MyArrayAdapter;
import extra.custom_HospitalAdapter;


public class Hospitals extends Activity {
    Spinner citys;
    ArrayList<city> list_city=new ArrayList<city>();
    ArrayList<hospital_wrapper> list_hospital=new ArrayList<hospital_wrapper>();
    ArrayList<hospital_wrapper> list_all_hospital=new ArrayList<hospital_wrapper>();
    ArrayAdapter<city> adapter_city;
    custom_HospitalAdapter adapter_hospital;

    private ProgressDialog prgDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hospital);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        citys=(Spinner)findViewById(R.id.clinic_city);

        adapter_city = new MyArrayAdapter<city>(this,R.layout.spinner_item, list_city);
        citys.setAdapter(adapter_city);
        adapter_hospital = new custom_HospitalAdapter(this, list_hospital);
        ((ListView)findViewById(R.id.hospitals)).setAdapter(adapter_hospital);



        getHospitals(0);



        ((FloatingActionButton)findViewById(R.id.fab_back)).setOnClickListener(
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


                list_hospital.clear();
                for(hospital_wrapper h : list_all_hospital){

                    if(h.getHos().getCity_ID().equals(list_city.get(position).getID()+"")){
                        list_hospital.add(h);
                    }

                }
                getHospital();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ((FloatingActionButton)findViewById(R.id.fab)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(list_hospital.size()==0)
                        {
                            Toast.makeText(Hospitals.this,"لا يوجد نتائج لعرضها", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            if(isGooglePlayServicesAvailable()) {
                                Intent it = new Intent(Hospitals.this, HospitalMap.class);
                                it.putExtra("hospital", list_hospital);
                                startActivity(it);
                            }
                            else
                            {
                                Snackbar.make(v, "من فضلك قم بتحديث خدمات   Google Play",
                                        Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );


    }


    void getHospital(){



        if(list_hospital.size()==0){

            findViewById(R.id.hospitals).setVisibility(View.GONE);
            findViewById(R.id.noresult).setVisibility(View.VISIBLE);

        }
        else
        {
            findViewById(R.id.hospitals).setVisibility(View.VISIBLE);
            findViewById(R.id.noresult).setVisibility(View.GONE);
        }
        adapter_hospital.notifyDataSetChanged();


    }




    private void getHospitals(final int count) {

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();


        client.post(base.BASE_URL + "get_hospital", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {


                    JSONObject obj = new JSONObject(response);

                    ArrayList<city> resAR = city.readOBJArray(obj.getJSONArray("cities").toString());
                    list_city.clear();
                    for (city help : resAR) {
                        list_city.add(help);
                        //
                    }

                    list_all_hospital = hospital_wrapper.readOBJArray(obj.getJSONArray("hospitals").toString());

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
                    getHospitals(count + 1);
                else {
                    prgDialog.hide();
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }


            }
        });



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