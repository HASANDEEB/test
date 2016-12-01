package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import entities.base;
import entities.clinic;
import entities.doctor;
import entities.service;

/**
 * Created by Rock on 5/22/2016.
 */
public class clinic_service_info extends Activity {


    ArrayList<service> list_service=new ArrayList<service>();
    ArrayList<service> sel_service=new ArrayList<service>();
    ArrayAdapter<service> adapter_service;
    private ProgressDialog prgDialog;
    private doctor doc;
    private clinic cl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_service);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);



        if(getIntent().hasExtra("doctor")){

            doc=(doctor)getIntent().getSerializableExtra("doctor");

        }
        else{
            finish();
        }

        final ListView list=(ListView)findViewById(R.id.service_list);
        adapter_service = new ArrayAdapter<service>(this,android.R.layout.simple_list_item_multiple_choice, list_service);
        list.setAdapter(adapter_service);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getService();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(sel_service.contains(list_service.get(position)))
                {
                    sel_service.remove(list_service.get(position));
                }
                else
                    sel_service.add(list_service.get(position));
            }
        });

        ((Button)findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent it=new Intent(clinic_service_info.this,Speciality.class);
                        it.putExtra("doctor",doc);
                        it.putExtra("service", sel_service);
                        startActivity(it);
                        finish();


                    }
                }
        );

    }



    private void getService() {

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();


        client.post(base.BASE_URL + "getService", new RequestParams(), new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                prgDialog.hide();

                try {
                    ArrayList<service> resAR = service.readOBJArray(response);
                    list_service.clear();
                    for (service help : resAR) {
                        list_service.add(help);
                        //
                    }
                    adapter_service.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }


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