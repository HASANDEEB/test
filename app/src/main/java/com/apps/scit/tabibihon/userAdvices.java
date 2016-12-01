package com.apps.scit.tabibihon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import entities.base;
import entities.saveAdviceWrepper;
import entities.user;
import extra.custom_save_adviceListAdapter;

/**
 * Created by Rock on 5/26/2016.
 */
public class userAdvices extends Activity {

    private ListView gridView;
    private custom_save_adviceListAdapter gridAdapter;

    ArrayList<saveAdviceWrepper>advices=new ArrayList<saveAdviceWrepper>();
    boolean first=true;

    user us=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.actvity_user_advice);




        if(getIntent().hasExtra("user")){
            us=(user)getIntent().getSerializableExtra("user");

        }
        else finish();



        gridView = (ListView) findViewById(R.id.advice_list);
        gridAdapter = new custom_save_adviceListAdapter(userAdvices.this,advices,  us);
        gridView.setAdapter(gridAdapter);

        getAdvices(0);

    }


    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(!first)
            getAdvices(0);
        else
            first=true;

    }

    private void getAdvices(final int count) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("user_ID", us.getID()+"");
        req.put("token",us.getAccessToken());

        client.post(base.BASE_URL + "get_user_saved_advice", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    ArrayList<saveAdviceWrepper> resAR = saveAdviceWrepper.readOBJArray(response);
                    advices.clear();
                    for (saveAdviceWrepper help : resAR) {
                        advices.add(help);
                    }
                    gridAdapter.notifyDataSetChanged();

                    if(advices.size()==0){
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
                    getAdvices(count+1);
                }else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();

                    findViewById(R.id.loadingPanel_stats).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.statuso)).setVisibility(View.GONE);
                }

            }
        });



    }





}