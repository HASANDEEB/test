package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import entities.base;
import entities.doctor;
import entities.specialization;
import extra.MyArrayAdapter;

/**
 * Created by Rock on 5/22/2016.
 */
public class Speciality extends Activity {
    private doctor doc;
   // ArrayList<service> service;
    Spinner special;
    ArrayList<specialization> list=new ArrayList<specialization>();
    ArrayAdapter<specialization> adapter;
    private ProgressDialog prgDialog;
    User user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_specialize);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        special=(Spinner)findViewById(R.id.ageSpinner);

        //adapter = new ArrayAdapter<specialization>(this,android.R.layout.simple_spinner_dropdown_item, list);
        adapter = new MyArrayAdapter<specialization>(this,R.layout.spinner_item, list);
        special.setAdapter(adapter);
        prgDialog.show();
        getSpeciality();

        if(getIntent().hasExtra("doctor")){

            doc=(doctor)getIntent().getSerializableExtra("doctor");
            user=(User)getIntent().getSerializableExtra("user");
           // service=(ArrayList<service>)getIntent().getSerializableExtra("service");
        }
        else{
            finish();
        }

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

        if(special.getSelectedItemPosition()==-1)
        {
            Toast.makeText(this, "من فضلك اختر الاختصاص من القائمة", Toast.LENGTH_SHORT).show();

        }
        else{
            doc.setSpecialization_ID(list.get(special.getSelectedItemPosition()).getID()+"");

            Intent it=new Intent(this,clinicRegisteration.class);
            it.putExtra("doctor",doc);
            it.putExtra("user",user);
           // it.putExtra("service",service);
            startActivity(it);
            finish();
        }



    }


    private void getSpeciality() {

        AsyncHttpClient client = new AsyncHttpClient();


        client.post(base.BASE_URL+"getSpecialization", new RequestParams(), new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    ArrayList<specialization> resAR = specialization.readOBJArray(response);
                    list.clear();
                    for (specialization help : resAR) {
                        list.add(help);
                        //
                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }

                prgDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                prgDialog.hide();

            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}