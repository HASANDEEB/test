package com.apps.scit.tabibihon;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import entities.all_advice_wrapper;
import entities.base;
import entities.doctor;
import entities.user;
import extra.custom_AllAdviceListAdapter;

public class AdviceActivity extends Activity {

    
    private final static String TAG = MainActivity.class.getSimpleName();

    private int pageCount = 0;

    private custom_AllAdviceListAdapter adapter;
    private ListView listView;
    private ProgressDialog dialog;
    private ArrayList<all_advice_wrapper> advices;
    private boolean load=false,finish=false;
    private Snackbar snl;
    private base us;
    private int type=0;
    private ProgressDialog prgDialog;
    public static CallbackManager callbackManager;
    public static  ShareDialog shareDialog;

    Button all_but,doc_but,ph_but;
    String doc_type="2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        callbackManager = CallbackManager.Factory.create();
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        all_but=(Button)findViewById(R.id.all_but);
        doc_but=(Button)findViewById(R.id.doc_but);
        ph_but=(Button)findViewById(R.id.ph_but);

        all_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!doc_type.equals("2")) {
                    all_but.setBackgroundResource((R.color.myred));
                    doc_but.setBackgroundResource((R.color.mylightred));
                    ph_but.setBackgroundResource((R.color.mylightred));
                    all_but.setTextColor(getResources().getColor(R.color.white));
                    doc_but.setTextColor(getResources().getColor(R.color.myblue));
                    ph_but.setTextColor(getResources().getColor(R.color.myblue));
                    doc_type = "2";
                    prgDialog.show();
                    advices.clear();
                    pageCount = 0;
                    getData(0, 0);
                }
            }
        });
        doc_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!doc_type.equals("0")) {
                    all_but.setBackgroundResource(R.color.mylightred);
                    doc_but.setBackgroundResource(R.color.myred);
                    ph_but.setBackgroundResource((R.color.mylightred));
                    all_but.setTextColor(getResources().getColor(R.color.myblue));
                    doc_but.setTextColor(getResources().getColor(R.color.white));
                    ph_but.setTextColor(getResources().getColor(R.color.myblue));
                    doc_type = "0";
                    prgDialog.show();
                    advices.clear();
                    pageCount = 0;
                    getData(0, 0);
                }
            }
        });
        ph_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!doc_type.equals("1")) {
                    all_but.setBackgroundResource((R.color.mylightred));
                    doc_but.setBackgroundResource((R.color.mylightred));
                    ph_but.setBackgroundResource((R.color.myred));

                    all_but.setTextColor(getResources().getColor(R.color.myblue));
                    doc_but.setTextColor(getResources().getColor(R.color.myblue));
                    ph_but.setTextColor(getResources().getColor(R.color.white));
                    doc_type = "1";
                    prgDialog.show();
                    advices.clear();
                    pageCount = 0;
                    getData(0, 0);
                }
            }
        });




        listView = (ListView) findViewById(R.id.list);
        if(getIntent().hasExtra("user"))
            us=(base)getIntent().getSerializableExtra("user");
        else
            us=null;

        setListViewAdapter();




        findViewById(R.id.rd_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type == 1) {
                    prgDialog.show();
                    advices.clear();
                    type = 0;
                    pageCount = 0;
                    getData(0, 0);
                }


            }
        });
        findViewById(R.id.rd_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(type==0) {
                    prgDialog.show();
                    advices.clear();
                    type = 1;
                    pageCount=0;
                    getData(0,0);
                }


            }
        });


        snl=Snackbar.make(listView, "تحميل   النصائح...",
                Snackbar.LENGTH_INDEFINITE);
        prgDialog.show();
        getData(0, 0);
        listView.setOnScrollListener(onScrollListener());

         shareDialog = new ShareDialog(AdviceActivity.this);

        shareDialog.registerCallback(AdviceActivity.callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                // This doesn't work
                Toast.makeText(AdviceActivity.this, "تمت مشاركة النصيحة بنجاح", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(AdviceActivity.this, "لا يمكنك مشاركة النصيحة", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void setListViewAdapter() {
        advices = new ArrayList<all_advice_wrapper>();
        adapter = new custom_AllAdviceListAdapter(this, R.layout.all_advice_item, advices,us);
        listView.setAdapter(adapter);
    }

    // calling asynctask to get json data from internet
    private void getData(final int current ,final int count) {
        snl.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("current", current+"");
        if(us!=null && us instanceof user)
        req.put("user_ID",((user)us).getID()+"");
        if(us!=null && us instanceof doctor)
            req.put("doctor_ID", ((doctor) us).getID() + "");

        req.put("type",type+"");
        req.put("doc_type",doc_type);
        client.post(base.BASE_URL + "getAllAdvice", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                ArrayList<all_advice_wrapper>all=all_advice_wrapper.readOBJArray(response);

                if(all.size()==0)
                    finish=true;

                for (all_advice_wrapper a : all){
                    advices.add(a);
                }
                load=false;
                pageCount++;
                adapter.notifyDataSetChanged();
                if(prgDialog.isShowing())
                prgDialog.hide();
                snl.dismiss();


            }
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if (count != 5)
                    getData(current,count + 1);
                else {
                    //Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    prgDialog.hide();
                }

            }
        });


    }

    private OnScrollListener onScrollListener() {
        return new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = listView.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count - threshold ) {
                        Log.i(TAG, "loading more data");
                        // Execute LoadMoreDataTask AsyncTask
                        if(!load&&!finish) {
                            load=true;

                            snl=Snackbar.make(view, "تحميل المزيد من النصائح...",
                                    Snackbar.LENGTH_INDEFINITE);

                            getData(pageCount * 10, 0);
                        }
                        else
                            if(finish)
                                snl=Snackbar.make(view, "لا يوجد المزيد من النصائح",
                                        Snackbar.LENGTH_SHORT);
                        snl.show();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }

        };
    }



}