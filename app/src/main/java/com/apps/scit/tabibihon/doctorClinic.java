package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import costumize.ArabTextView;
import entities.advice_wrapper;
import entities.base;
import entities.doctor;
import entities.work_time;
import extra.custom_AdviceListAdapter;


public class doctorClinic extends Activity {


    private ProgressDialog prgDialog;
    LinearLayout company,hospital;
    TextView enable;
    doctor  doc=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctor_clinic);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        company=(LinearLayout)findViewById(R.id.works);
        hospital=(LinearLayout)findViewById(R.id.hospital_ly);
        enable=(TextView)findViewById(R.id.status);

        if(getIntent().hasExtra("id"))
        {
            doc=(doctor)getIntent().getSerializableExtra("id");

        }
        else
        {
            finish();
        }

        if(doc.getType().equals("1")) {
            ((TextView) findViewById(R.id.title)).setText("معلومات الصيدلية");
            ((TextView) findViewById(R.id.statustxt)).setText("حالة الصيدلية");
            hospital.setVisibility(View.GONE);
        }

        getClinic(doc.getClinic_ID(),0);

        ((Button)findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                }
        );




    }



    void getClinic(final String id,final int count){

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("ID", id);



        client.post(base.BASE_URL + "get_clinic", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {

                    JSONObject obj = new JSONObject(response);
                    ((TextView) findViewById(R.id.c_city)).setText((TextUtils.isEmpty(obj.getString("city"))) ? "لا يوجد" : obj.getString("city"));
                    ((TextView) findViewById(R.id.c_area)).setText((TextUtils.isEmpty(obj.getString("area"))) ? "لا يوجد" : obj.getString("area"));
                    ((TextView) findViewById(R.id.c_street)).setText((TextUtils.isEmpty(obj.getString("street"))) ? "لا يوجد" : obj.getString("street"));
                    ((TextView) findViewById(R.id.c_loc)).setText((TextUtils.isEmpty(obj.getString("location"))) ? "لا يوجد" : obj.getString("location"));
                    ((TextView) findViewById(R.id.c_phone)).setText((TextUtils.isEmpty(obj.getString("phone"))) ? "لا يوجد" : obj.getString("phone"));
                    ((TextView) findViewById(R.id.c_mobile)).setText((TextUtils.isEmpty(obj.getString("mobile"))) ? "لا يوجد" : obj.getString("mobile"));
                    ((TextView) findViewById(R.id.c_website)).setText((TextUtils.isEmpty(obj.getString("website"))) ? "لا يوجد" : obj.getString("website"));
                    ((TextView) findViewById(R.id.c_email)).setText((TextUtils.isEmpty(obj.getString("email"))) ? "لا يوجد" : obj.getString("email"));
                    ((TextView) findViewById(R.id.c_info)).setText((TextUtils.isEmpty(obj.getString("info"))) ? "لا يوجد" : obj.getString("info"));

                    if (obj.getString("enabled").equals("1")) {

                        enable.setText("متواجد حاليا");
                        //  enable.setTextColor(Color.argb(190, 21, 20, 255));
                    } else {

                        enable.setText("غير متواجد حاليا");
                        //    enable.setTextColor(Color.argb(7, 190, 39, 255));
                    }


                    ArrayList<work_time> works = work_time.readOBJArray(obj.getJSONArray("work").toString());

                    if (works.size() == 0) {
                        ArabTextView tv = new ArabTextView(getApplicationContext());
                        tv.setText("لم يتم تحديد اوقات الدوام");
                        tv.setTextSize(12);
                        tv.setTextColor(getResources().getColor(R.color.mydarkgray));
                        company.addView(tv);

                    } else {


                        for (work_time dl : works) {

                            String s=" من الساعة "+
                                    dl.getStart_time()+" "+(dl.getS_shift_time().equals("0")?"صباحاً":"مساءً")+
                                    " الى الساعة "+
                                    dl.getEnd_time()+" "+(dl.getE_shift_time().equals("0")?"صباحاً":"مساءً");


                            ArabTextView tv = new ArabTextView(getApplicationContext());
                            tv.setText(s);
                            tv.setTextSize(12);
                            tv.setTextColor(getResources().getColor(R.color.mydarkgray));
                            company.addView(tv);
                        }
                    }

                    ////////////////////////////////////////
                    JSONArray hospitals = obj.getJSONArray("hospital");

                    if (hospitals.length() == 0) {
                        ArabTextView tv = new ArabTextView(getApplicationContext());
                        tv.setText("لم يحدد الطبيب اي مشفى يعمل بها");
                        tv.setTextSize(12);
                        tv.setTextColor(getResources().getColor(R.color.mydarkgray));
                        hospital.addView(tv);

                    } else {


                        for (int i = 0; i < hospitals.length(); i++) {
                            {
                                String s = " مشفى" + hospitals.getJSONObject(i).getString("name");


                                ArabTextView tv = new ArabTextView(getApplicationContext());
                                tv.setText(s);
                                tv.setTextSize(12);
                                tv.setTextColor(getResources().getColor(R.color.mydarkgray));
                                hospital.addView(tv);
                            }
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.fail), Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(getApplicationContext(), e.get, Toast.LENGTH_SHORT).show();
                }

                prgDialog.hide();
            }

                @Override
                public void onFailure ( int statusCode, Throwable error,
                        String content){
                    if (count != 5) {
                        getClinic(id, count + 1);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        prgDialog.hide();

                    }

                }
            }

            );

        }



    @Override
    protected void onPause() {
        super.onPause();

    }
}