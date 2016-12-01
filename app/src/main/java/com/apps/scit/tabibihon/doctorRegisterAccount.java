package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import entities.base;
import entities.doctor;
import extra.TextValidator;
import extra.checkPassword;

/**
 * Created by Rock on 5/22/2016.
 */
public class doctorRegisterAccount extends Activity {



    private ProgressDialog prgDialog;
    private EditText username,password;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_doctor_acount);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);

        password.addTextChangedListener(new TextValidator(password) {

            @Override
            public void validate(TextView textView, String text) {

                textView.setError(checkPassword.check(text, doctorRegisterAccount.this));

            }
        });

        type=getIntent().getStringExtra("type");

        ((Button)findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Onsignup();
                    }
                }
        );

    }


    public void Onsignup() {



        username.setError(null);
        password.setError(null);



        String password_ = password.getText().toString();

        String username_ = username.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid mobile address.
        if (TextUtils.isEmpty(username_)) {
            username.setError(getString(R.string.error_field_required));
            focusView = username;
            cancel = true;
        }
        if (!username_.matches("\\d{10}")) {
            username.setError(getString(R.string.invailed_phone));
            focusView = username;
            cancel = true;
        }

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password_)) {
                password.setError(getString(R.string.error_field_required));
                focusView = password;
                cancel = true;
            }




            if (cancel) {
                // There was an error; don't attempt signup and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                prgDialog.show();

                SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd hh:mm");


                 doctor   signed=new doctor( username_,  password_, "", "",  "",
                            "",  "", "", "", "","", "0", df.format(new Date()),null, null,"");
                signed.setType(type);


                try {
                    check(signed, username_,0);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


    }


    private void check(final doctor signed, final String username_,final int count) throws UnsupportedEncodingException, JSONException {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams request=new RequestParams();
        request.put("username", username_);

            request.put("table","doctor");
        client.post(base.BASE_URL + "check_username", request, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {

                JSONObject res;
                try {
                    res = new JSONObject(response);

                    if (res.has("exist") && res.getInt("exist") == 1) {
                        username.setError("اسم المستخدم محجوز مسبقاً");
                        username.requestFocus();
                        prgDialog.hide();
                    } else {
                        try {

                            Intent it=new Intent(doctorRegisterAccount.this,doctorRegisterSubInfo.class);
                            it.putExtra("doctor",signed);
                            startActivity(it);
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5)
                {
                    try {
                        check(signed,username_,count+1);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    prgDialog.hide();
                    showMsg(getResources().getString(R.string.error));
                }

            }
        });
    }

    void showMsg(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

    }



    @Override
    protected void onPause() {
        super.onPause();

    }
}