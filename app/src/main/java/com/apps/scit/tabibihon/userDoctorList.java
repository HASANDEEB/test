package com.apps.scit.tabibihon;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import entities.doctor_list_wrapper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import entities.base;
import entities.user;
import extra.custom_doctorListAdapter;


public class userDoctorList extends Activity {

    private ListView gridView;
    private custom_doctorListAdapter gridAdapter;

    ArrayList<doctor_list_wrapper>doctors=new ArrayList<doctor_list_wrapper>();
    boolean first=true;

    user us=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.actvity_user_doctorlist);




        if(getIntent().hasExtra("user")){
            us=(user)getIntent().getSerializableExtra("user");

        }
        else finish();



        gridView = (ListView) findViewById(R.id.doctor_list);
        gridAdapter = new custom_doctorListAdapter(userDoctorList.this,doctors,  us);
        gridView.setAdapter(gridAdapter);

        getDoctors(0);

    }


    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(!first)
            getDoctors(0);
        else
            first=true;

    }

    private void getDoctors(final int count) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("user_ID", us.getID()+"");
        req.put("token",us.getAccessToken());

        client.post(base.BASE_URL + "get_user_doctor_list", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    ArrayList<doctor_list_wrapper> resAR = doctor_list_wrapper.readOBJArray(response);
                    doctors.clear();
                    for (doctor_list_wrapper help : resAR) {
                        doctors.add(help);

                    }

                    if(doctors.size()==0){
                        findViewById(R.id.search_no_result).setVisibility(View.VISIBLE);
                    }


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.fail, Toast.LENGTH_SHORT).show();
                }

                findViewById(R.id.loadingPanel_stats).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.statuso)).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5){
                    getDoctors(count+1);
                }else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();

                    findViewById(R.id.loadingPanel_stats).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.statuso)).setVisibility(View.GONE);
                }
            }
        });



    }





}