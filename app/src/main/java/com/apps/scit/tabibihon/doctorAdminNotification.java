package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import costumize.ArabTextView;
import entities.admin_notification;
import entities.area;
import entities.base;
import entities.chemistry_wrapper;
import entities.city;
import extra.MyArrayAdapter;
import extra.custom_adminNotificationeListAdapter;


public class doctorAdminNotification extends Activity {

    ListView notification_list;
    ArrayList<admin_notification> list=new ArrayList<admin_notification>();
    custom_adminNotificationeListAdapter adapter_notification;

    private ProgressDialog prgDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_notification);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        notification_list=(ListView)findViewById(R.id.note_list);
        adapter_notification = new custom_adminNotificationeListAdapter(this, list);
        notification_list.setAdapter(adapter_notification);

        if(getIntent().hasExtra("id"))
            getData(0,getIntent().getIntExtra("id",0)+"");
        else
        finish();



        ((Button)findViewById(R.id.b_back)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                }
        );




    }





    private void getData(final int count, final String id) {

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("doctor_ID",id);

        client.post(base.BASE_URL+"get_doctor_notification", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {


                    ArrayList<admin_notification> resNote = admin_notification.readOBJArray(response);
                    list.clear();
                    for (admin_notification note : resNote) {
                        list.add(note);
                        //
                    }

                    if(list.size()==0)
                        findViewById(R.id.no_result).setVisibility(View.VISIBLE);

                    adapter_notification.notifyDataSetChanged();


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();

                }

                prgDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5)
                    getData(count+1,id);
                else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    prgDialog.hide();
                }

            }
        });



    }

}