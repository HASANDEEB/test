package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entities.base;
import entities.doctor;
import entities.user;
import extra.MyArrayAdapter;
import extra.TextInit;

/**
 * Created by Rock on 5/22/2016.
 */
public class facebookRegisteration extends Activity {

    EditText university,info,email,phone,mobile;
    User user=null;
    entities.user us=null;
    doctor doc=null;
    ArrayList<String> list_type=new ArrayList<String>();
    ArrayAdapter<String> adapter_type;
    private Spinner spinner_type;
    private DisplayImageOptions options;
    private ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_doctor_fb_cont);


        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        if(ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().destroy();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.w_user)
                .showImageOnFail(R.drawable.w_user)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(60))
                .build();

        list_type.add("مستخدم");
        list_type.add("طبيب");
        list_type.add("صيدلاني");



        university=(EditText)findViewById(R.id.university);
        info=(EditText)findViewById(R.id.additional_info);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);
        mobile=(EditText)findViewById(R.id.mobile);
        spinner_type=(Spinner)findViewById(R.id.user_type);

        adapter_type = new MyArrayAdapter<String>(this,R.layout.spinner_item, list_type);
        spinner_type.setAdapter(adapter_type);



        if(getIntent().hasExtra("user")){

        user=(User)getIntent().getSerializableExtra("user");

        }
        else
            finish();


        ((TextView)findViewById(R.id.title)).setText("اهلا "+user.name);

        email.setText(user.email);
        ImageLoader.getInstance().displayImage("https://graph.facebook.com/" + user.facebookID + "/picture?type=large",
                (ImageView)findViewById(R.id.user_photo),options);

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)findViewById(R.id.type_form).setVisibility(View.GONE);
                else
                    findViewById(R.id.type_form).setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ((Button)findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(spinner_type.getSelectedItemPosition()!=0)
                        continue_register();
                        else
                            continue_register2(0);
                    }
                }
        );




    }

    private void continue_register() {



        university.setError(null);
        info.setError(null);
        email.setError(null);
        phone.setError(null);
        mobile.setError(null);



        String uni = university.getText().toString();
        String inf = info.getText().toString();
        String em = email.getText().toString();
        String ph = phone.getText().toString();
        String mob = mobile.getText().toString();



        boolean cancel = false;
        View focusView = null;

            // Check for a valid password, if the user entered one.


            if (TextUtils.isEmpty(mob)) {
                mobile.setError(getString(R.string.error_field_required));
                focusView = mobile;
                cancel = true;
            }

        if (!mob.matches("\\d{10}")) {
            mobile.setError(getString(R.string.invailed_phone));
            focusView = mobile;
            cancel = true;
        }

            if (cancel) {
                // There was an error; don't attempt signup and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {

                user.role="doctor";
                //PrefUtils.setCurrentUser(user, this);
                doc=new doctor( mob,  mob, "", "",  "",
                        "",  "", "", "", "",mob, "0", new SimpleDateFormat().format(new Date()),null,null,"");

                doc.setFname(user.fname);
                doc.setLname(user.lname);
                doc.setFacebookID(user.facebookID);
                doc.setImage("https://graph.facebook.com/" + user.facebookID + "/picture?type=large");
                doc.setUniversity(TextInit.check(uni));
                doc.setAdditional_info(TextInit.check(inf));
                doc.setEmail(em);
                doc.setMobile(mob);
                doc.setPhone(ph);
                doc.setType((spinner_type.getSelectedItemPosition() == 1) ? "0" : "1");

                Intent it =null;
                if(doc.getType().equals("0")) {
                    it= new Intent(this, Speciality.class);
                    it.putExtra("user", user);
                }
                else{
                    it= new Intent(this, clinicRegisteration.class);
                }
                it.putExtra("doctor", doc);
                it.putExtra("user", user);

                startActivity(it);
                finish();

            }




    }



    private void continue_register2(final int count) {


        user signed = new user(user.facebookID, user.facebookID, "", "", "active", "0", new SimpleDateFormat().format(new Date()));
        signed.setFacebookID(user.facebookID);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req= (signed).getEntity();
        client.post(base.BASE_URL + "signupUser",req, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {


                try {
                    JSONObject arg0 = new JSONObject(response);

                    user u=entities.user.readSingleOBJ(arg0.getJSONObject("user"));
                    u.setAccessToken(arg0.getString("token"));
                    user.role="user";
                    PrefUtils.setCurrentUser(user, facebookRegisteration.this);


                    Intent it=new Intent(facebookRegisteration.this,MainActivity.class);
                    it.putExtra("user",u);
                    startActivity(it);
                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    showMsg(e.getMessage());
                }

                prgDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5)
                {
                    continue_register2(count+1);
                }else {
                    prgDialog.hide();
                    showMsg(getResources().getString(R.string.error));
                }

            }
        });

    }  void showMsg(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }




}