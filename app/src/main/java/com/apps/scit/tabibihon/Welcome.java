package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.io.File;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import entities.base;
import entities.doctor;
import entities.user;


/**
 * Created by Rock on 5/22/2016.
 */
public class Welcome   extends Activity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView btnLogin;
    private ProgressDialog progressDialog;
    User userfb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
       // FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.fragment_welcome);

        progressDialog = new ProgressDialog(Welcome.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        ((Button)findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ii = new Intent(Welcome.this, userType.class);

                        startActivity(ii);
                        finish();
                    }
                }
        );

        ((Button) findViewById(R.id.b_register)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent it = new Intent(Welcome.this, login.class);
                        startActivity(it);
                        finish();

                    }
                }
        );





    }


    @Override
    protected void onPause() {
        super.onPause();

    }



    @Override
    protected void onResume() {
        super.onResume();


        callbackManager = CallbackManager.Factory.create();

        loginButton= (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog.show();

                if (PrefUtils.getCurrentUser(Welcome.this) != null) {
                    final User us = PrefUtils.getCurrentUser(Welcome.this);
                    checkIfLoginBefore(us.facebookID,0);
                } else {
                    loginButton.performClick();

                    loginButton.setPressed(true);

                    loginButton.invalidate();

                    loginButton.registerCallback(callbackManager, mCallBack);

                    loginButton.setPressed(false);

                    loginButton.invalidate();
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

           // progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            //Log.e("response: ", response + "");
                            try {
                                //Toast.makeText(Welcome.this, response.toString(), Toast.LENGTH_LONG).show();
                                userfb = new User();
                                userfb.facebookID = object.getString("id").toString();
                                userfb.email = object.getString("email").toString();
                                userfb.fname = object.getString("first_name").toString();
                                userfb.name = object.getString("name").toString();
                                userfb.lname = object.getString("last_name").toString();
                                userfb.gender = object.getString("gender").toString();


                            }catch (Exception e){
                                e.printStackTrace();
                            }

                           // Toast.makeText(Welcome.this,userfb.toString(),Toast.LENGTH_LONG).show();
                            checkIfLoginBefore(userfb.facebookID,0);




                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,gender,email,last_name,first_name,name");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
           // Toast.makeText(Welcome.this,"يرجى اعادة محاولة التسجيل مرة اخرى",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
            Toast.makeText(Welcome.this,"يرجى اعادة محاولة التسجيل مرة اخرى",Toast.LENGTH_SHORT).show();
        }
    };



    void checkIfLoginBefore(final String us,final int count){
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams req = new RequestParams();
        req.put("facebookID", us);

        client.post(base.BASE_URL + "checkloginFB", req, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(final String response) {
                //Toast.makeText(login.this, response, Toast.LENGTH_LONG).show();
                //section performed when the request has successfully sent


                //    prgDialog.hide();
                try {
                    JSONObject arg0 = new JSONObject(response);


                    if(!arg0.has("statue")) {
                        if (arg0.getString("role").equals("user")) {
                            entities.user u = entities.user.readSingleOBJ(arg0.getJSONObject("user"));
                            u.setAccessToken(arg0.getString("token"));


                            Intent it = new Intent(Welcome.this, MainActivity.class);
                            it.putExtra("user", u);
                            startActivity(it);
                            finish();
                        } else if (arg0.getString("role").equals("doctor")) {
                            doctor u = doctor.readSingleOBJ(arg0.getJSONObject("user"));
                            u.setAccessToken(arg0.getString("token"));


                            Intent it = new Intent(Welcome.this, my_profile.class);
                            it.putExtra("doctor", u);
                            startActivity(it);
                            finish();
                        } else {
                            Intent intent = new Intent(Welcome.this, facebookRegisteration.class);
                            intent.putExtra("user", userfb);

                            startActivity(intent);
                            finish();
                        }
                    }
                    else{
                        Intent intent=new Intent(Welcome.this,facebookRegisteration.class);
                        intent.putExtra("user",userfb);

                        startActivity(intent);
                        finish();
                    }
                } catch (final JSONException e) {

                    Intent intent=new Intent(Welcome.this,facebookRegisteration.class);
                    intent.putExtra("user",userfb);

                    startActivity(intent);
                    finish();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5) {
                    checkIfLoginBefore(us, count + 1);
                }
                    else
                    progressDialog.dismiss();


            }
        });


    }


}