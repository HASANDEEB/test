package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import costumize.ArabTextView;
import entities.base;
import entities.doctor;
import entities.insurance_company;

/**
 * Created by Rock on 5/22/2016.
 */
public class doctorContacts extends Activity {


    private ProgressDialog prgDialog;
    LinearLayout company;
    doctor doc=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_doctor_contact);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        company=(LinearLayout)findViewById(R.id.company);



        if(getIntent().hasExtra("doctor"))
        {
            doc=(doctor)getIntent().getSerializableExtra("doctor");

        }


        ((TextView)findViewById(R.id.d_univ)).setText((TextUtils.isEmpty(doc.getUniversity().trim()))?"لا يوجد":doc.getUniversity());
        ((TextView)findViewById(R.id.d_phone)).setText((TextUtils.isEmpty(doc.getPhone().trim()))?"لا يوجد":doc.getPhone());
        ((TextView)findViewById(R.id.d_mob)).setText((TextUtils.isEmpty(doc.getMobile().trim()))?"لا يوجد":doc.getMobile());
        ((TextView)findViewById(R.id.d_email)).setText((TextUtils.isEmpty(doc.getEmail().trim()))?"لا يوجد":doc.getEmail());
        ((TextView)findViewById(R.id.d_info)).setText((TextUtils.isEmpty(doc.getAdditional_info().trim()))?"لا يوجد":doc.getAdditional_info());


        ((Button)findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                }
        );



getCompany(0);





    }


    void getCompany(final int count){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("ID", doc.getID() + "");
        client.post(base.BASE_URL + "getCompany", req, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("link");


                    if(array.length()==0)
                    {
                        ArabTextView tv=new ArabTextView(getApplicationContext());
                        tv.setText("لم يتم الاشتراك بأي شركة تأمين");
                        tv.setTextSize(12);
                        tv.setTextColor(getResources().getColor(R.color.mydarkgray));
                        company.addView(tv);

                    }
                    else
                    {



                        for (int i = 0; i < array.length(); i++) {
                        JSONObject w = array.getJSONObject(i);

                            ArabTextView tv=new ArabTextView(getApplicationContext());
                            tv.setText(w.getString("name"));
                            tv.setTextSize(12);
                            tv.setTextColor(getResources().getColor(R.color.mydarkgray));
                            company.addView(tv);
                        }
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }
                prgDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5)
                {
                    getCompany(count+1);
                }
                else {
                    prgDialog.hide();
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    class doctor_company{

        int ID;
        String name;

        public doctor_company(int ID, String name) {
            this.ID = ID;
            this.name = name;
        }


        @Override
        public String toString() {
            return name ;
        }
    }



    @Override
    protected void onPause() {
        super.onPause();

    }
}