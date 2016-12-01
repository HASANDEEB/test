package com.apps.scit.tabibihon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import entities.base;
import entities.notification_wrapper;
import entities.user;
import extra.custom_notificationListAdapter;

/**
 * Created by Rock on 5/26/2016.
 */
public class notificationResults extends Activity {

    private ListView gridView;
    private custom_notificationListAdapter gridAdapter;

    ArrayList<notification_wrapper>notific=new ArrayList<notification_wrapper>();


    user us=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.actvity_notification);




        if(getIntent().hasExtra("user")){
            us=(user)getIntent().getSerializableExtra("user");

        }
        else finish();

        gridView = (ListView) findViewById(R.id.notification_list);


        getNotifi(0);


    }





    @Override
    protected void onPause() {
        super.onPause();

    }


    private void getNotifi(final int count) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("user_ID", us.getID()+"");

        req.put("token",us.getAccessToken());



        client.post(base.BASE_URL + "getNotification", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    ArrayList<notification_wrapper> resAR = notification_wrapper.readOBJArray(response);
                    notific.clear();
                    for (notification_wrapper help : resAR) {
                        notific.add(help);

                    }
                    gridAdapter = new custom_notificationListAdapter(notificationResults.this, notific, us);
                    gridView.setAdapter(gridAdapter);

                    if(notific.size()==0){
                        findViewById(R.id.search_no_result).setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.fail, Toast.LENGTH_SHORT).show();
                }

                findViewById(R.id.loadingPanel_stats).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.statuso)).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {

                if(count!=5)
                {
                    getNotifi(count+1);
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();

                    findViewById(R.id.loadingPanel_stats).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.statuso)).setVisibility(View.GONE);
                }

            }
        });



    }





}