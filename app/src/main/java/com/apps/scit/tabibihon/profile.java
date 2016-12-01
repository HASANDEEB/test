package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

import entities.base;
import entities.doctor;
import entities.doctor_list;
import entities.user;


public class profile extends Activity {
    doctor doc = null;
    user us = null;
    private ProgressDialog prgDialog;
    doctor current = null;
    TextView name, speciality, adress;
    String location;
    int exist=0;
    ImageView ivImage;
    RelativeLayout wait_info;
    ProgressBar photoWait;
    LinearLayout profi;
    int doctorType=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        if (getIntent().hasExtra("doctor")) {

            doc = (doctor) getIntent().getSerializableExtra("doctor");


        } else if (getIntent().hasExtra("user")) {
            us = (user) getIntent().getSerializableExtra("user");

        }
        else{}

        int id = (int) getIntent().getSerializableExtra("id");
        if(getIntent().hasExtra("type"))
        doctorType=Integer.parseInt(getIntent().getStringExtra("type"));
        ivImage = (ImageView)findViewById(R.id.ivImage);
        name = (TextView) findViewById(R.id.prof_name);
        speciality = (TextView) findViewById(R.id.prof_speciality);
        adress = (TextView) findViewById(R.id.prof_address);
        wait_info=(RelativeLayout)findViewById(R.id.wait_layout);
        photoWait=(ProgressBar)findViewById(R.id.photo_sec);
        profi=(LinearLayout)findViewById(R.id.profile_sec);

        ((ImageView)findViewById(R.id.authorized)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] pos = new int[2];

                v.getLocationInWindow(pos);

                String content = getResources().getString(R.string.auth);
                Toast t = Toast.makeText(profile.this, content, Toast.LENGTH_SHORT);
                t.setGravity(Gravity.TOP | Gravity.LEFT, pos[0] - ((content.length() / 2) * 12), pos[1] - 128);
                t.show();
            }
        });

        do_search(id,0);




        ((Button) findViewById(R.id.contact)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (current != null) {
                            String phoneNumber = current.getUsername();
                            Intent i = new
                                    Intent(android.content.Intent.ACTION_DIAL,
                                    Uri.parse("tel:" + phoneNumber));
                            startActivity(i);
                        }


                    }
                }
        );






        ((Button)findViewById(R.id.place)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (current != null) {
                            if (!TextUtils.isEmpty(location)) {
                                Intent ii = new Intent(profile.this, MapsActivity.class);
                                ii.putExtra("location", location);
                                ii.putExtra("type", current.getType());
                                startActivity(ii);
                            } else {
                                Toast.makeText(getApplicationContext(), "لم يتم تحديد الموقع", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "لم يتم تحميل البيانات بشكل صحيح ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        if(us==null&&doc==null)
            ((Button)findViewById(R.id.advice)).setVisibility(View.GONE);

        ((Button)findViewById(R.id.advice)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ii = new Intent(profile.this, doctorAdvice.class);
                        if (us!= null)
                            ii.putExtra("user", us);
                        else
                        if (doc!= null)
                            ii.putExtra("doctor", doc);
                        ii.putExtra("target", current);

                        startActivity(ii);
                    }
                }
        );
        ((Button)findViewById(R.id.b_contact_info)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ii = new Intent(profile.this, doctorContacts.class);
                        ii.putExtra("doctor", current);
                        startActivity(ii);
                    }
                }
        );
        ((Button)findViewById(R.id.b_clinic_info)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ii = new Intent(profile.this, doctorClinic.class);
                        ii.putExtra("id", current);
                        startActivity(ii);
                    }
                }
        );


        ((FloatingActionButton)findViewById(R.id.fab)).setVisibility(View.GONE);



    }






    void do_search(final int id,final int count){



        wait_info.setVisibility(View.VISIBLE);
        profi.setVisibility(View.GONE);

        RequestParams request=new RequestParams();
        String url="";
        if(us!=null&&doctorType==0){
            request.put("user_ID",us.getID()+"");
            request.put("doctor_ID",id+"");
            url="userDoctorProfile";
        }
        else
        {
            request.put("ID",id+"");
            if(doctorType==0)
            url="search/doctor";
            else
                url="search/pharmacy";

        }


        String tok="";
        if(doc!=null)tok=doc.getAccessToken();
        if(us!=null)tok=us.getAccessToken();

        request.put("token",tok);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(base.BASE_URL + url , request, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {


                    JSONArray array = new JSONArray(response);
                    JSONObject obj=array.getJSONObject(0);

                    current=doctor.readSingleOBJ(obj);

                  if(current.getType().equals("0")) {
                      name.setText("د. " + current.getFname() + " " + current.getMname() + " " + current.getLname());
                      speciality.setText("الاختصاص : " + obj.getString("speciality"));
                  }
                    else {
                      name.setText("ص. " + current.getFname() + " " + current.getMname() + " " + current.getLname());
                      speciality.setText("صيدلية  : " + obj.getString("clinic"));
                  }
                    String add = obj.getString("country") +" "+obj.getString("city") +" "+ obj.getString("area")  +((!TextUtils.isEmpty(obj.getString("street")))?"\n"+ "شارع " + obj.getString("street"):"");
                    adress.setText(add);

                    location=obj.getString("location");
                    if(current.getAuthorized().equals("1"))
                        ((ImageView)findViewById(R.id.authorized)).setVisibility(View.VISIBLE);

                    if(!TextUtils.isEmpty(current.getImage())) {

                        ivImage.setVisibility(View.INVISIBLE);
                        photoWait.setVisibility(View.VISIBLE);

                        if(current.getImage().startsWith("https"))
                            new DownloadImageTask((ImageView) ivImage).execute(current.getImage());
                            else
                        new DownloadImageTask((ImageView) ivImage).execute(base.BASE_URL + "256/" + current.getImage());
                    }

                    if(current.getType().equals("1")){

                        ((Button) findViewById(R.id.place)).setText("موقع الصيدلية على الخريطة");
                        ((Button) findViewById(R.id.contact)).setText("اتصال بالصيدلاني");
                        ((Button) findViewById(R.id.b_clinic_info)).setText("معلومات الصيدلية");
//                        ((Button) findViewById(R.id.advice)).setVisibility(View.GONE);

                    }




                    if(us!=null&&doctorType==0) {

                        if(obj.has("checked"))
                        {

                            if(obj.getInt("checked")==-1)
                            {
                                ((FloatingActionButton) findViewById(R.id.fab)).setImageResource(android.R.drawable.ic_input_add);
                                exist=-1;
                            }
                            else{
                                ((FloatingActionButton) findViewById(R.id.fab)).setImageResource(android.R.drawable.ic_input_delete);
                                exist=obj.getInt("checked");


                            }

                            ((FloatingActionButton)findViewById(R.id.fab)).setVisibility(View.VISIBLE);
                            ((FloatingActionButton)findViewById(R.id.fab)).setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (exist != 0) {

                                                if (exist > 0) {
                                                    removeFromList(current.getID());

                                                } else {
                                                    addToList();
                                                }


                                            }


                                        }
                                    }
                            );

                        }


                    }

                    addVisits(current.getID()+"");

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
                    wait_info.setVisibility(View.GONE);
                    profi.setVisibility(View.VISIBLE);
                }

                wait_info.setVisibility(View.GONE);
                profi.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5)
                {
                    do_search(id,count+1);
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                 //   wait_info.setVisibility(View.GONE);
                 //   profi.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    void addToList(){
        prgDialog.show();

        doctor_list dl=new doctor_list();
        dl.setDoctor_ID(current.getID()+"");
        dl.setUser_ID(us.getID() + "");

        RequestParams request= dl.getEntity();
        request.put("token",us.getAccessToken());
        request.put("table", "doctor_list");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(base.BASE_URL + "add" , request, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    JSONObject obj=new JSONObject(response);
                            ((FloatingActionButton) findViewById(R.id.fab)).setImageResource(android.R.drawable.ic_input_delete);
                    exist=obj.getInt("id");
                    Toast.makeText(getApplicationContext(), "تمث عملية اضافة الطبيب الى القائمة بنجاح", Toast.LENGTH_SHORT).show();



                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
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

    void removeFromList(int id){
        prgDialog.show();
        RequestParams request=new RequestParams();
        request.put("token",us.getAccessToken());
        request.put("table", "doctor_list");
        request.put("ID", exist+"");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(base.BASE_URL + "delete" , request, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {

                    ((FloatingActionButton)findViewById(R.id.fab)).setImageResource(android.R.drawable.ic_input_add);
                    exist=-1;
                    Toast.makeText(getApplicationContext(), "تمث عملية ازالة  الطبيب من القائمة  بنجاح", Toast.LENGTH_SHORT).show();



                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
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




    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }


        protected void onPostExecute(Bitmap result) {

            bmImage.setImageBitmap(result);
            bmImage.setVisibility(View.VISIBLE);
            photoWait.setVisibility(View.INVISIBLE);

        }}


    private void addVisits(String id){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("ID",id);



        client.post(base.BASE_URL + "editProfileVisits", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();



            }
        });
    }

}