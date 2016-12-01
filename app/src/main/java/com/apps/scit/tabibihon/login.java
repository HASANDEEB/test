package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
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

import java.util.ArrayList;

import entities.base;
import entities.city;
import entities.doctor;
import entities.user;
import extra.MyArrayAdapter;


public class login extends Activity {

    EditText usernames,passwords;
    ProgressDialog prgDialog;
    Spinner  spinner_type;
    ArrayList<String> list_type=new ArrayList<String>();
    ArrayAdapter<String> adapter_type;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView btnLogin;
    private ProgressDialog progressDialog;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(login.this);
        progressDialog.setMessage("Loading...");

        list_type.add("مستخدم");
        list_type.add("طبيب");
        list_type.add("صيدلاني");

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        spinner_type=(Spinner)findViewById(R.id.user_type);

        usernames=(EditText)findViewById(R.id.username);
        passwords=(EditText)findViewById(R.id.password);


        adapter_type = new MyArrayAdapter<String>(this,R.layout.spinner_item, list_type);
        spinner_type.setAdapter(adapter_type);

        if(getIntent().hasExtra("auto")) {

            checklog_in(0);

        }
        else{
            ((ScrollView)findViewById(R.id.login_form)).setVisibility(View.VISIBLE);

        }







        ((Button) findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            onLogin();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        ((Button) findViewById(R.id.b_visitor)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            save("","","visitor");
                            Intent it = new Intent(login.this, MainActivity.class);
                            startActivity(it);
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        ((Button) findViewById(R.id.b_register)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                          Intent it=new Intent(login.this,signup.class);
                            startActivity(it);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );



    }

    private void onLogin() {

        usernames.setError(null);
        passwords.setError(null);

        // Store values at the time of the login attempt.
        String username = usernames.getText().toString();
        String password = passwords.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            passwords.setError(getString(R.string.error_field_required));
            focusView = passwords;
            cancel = true;
        }

        // Check for a valid mobile address.
        if (TextUtils.isEmpty(username)) {
            usernames.setError(getString(R.string.error_field_required));
            focusView = usernames;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            prgDialog.show();

            String ty="";
                if(spinner_type.getSelectedItemPosition()==0)
                    ty="user";
            else
                ty="doctor";

            log_in(username, password, ty,0);



        }

    }

    private void log_in(final String username, final String password, final String table,final int count) {

         AsyncHttpClient client = new AsyncHttpClient();

        RequestParams req=new RequestParams();
        req.put("username",username);
        req.put("password", password);
        req.put("type", spinner_type.getSelectedItemPosition()+"");


            req.put("table", table);

        client.post(base.BASE_URL + "login", req, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Toast.makeText(login.this, response, Toast.LENGTH_LONG).show();
                //section performed when the request has successfully sent

                try {
                    JSONObject arg0 = new JSONObject(response);

                    save(username, password, table);
                    if (spinner_type.getSelectedItemPosition()==0) {
                        user u = entities.user.readSingleOBJ(arg0.getJSONObject("user"));
                        u.setAccessToken(arg0.getString("token"));


                        Intent it = new Intent(login.this, MainActivity.class);
                        it.putExtra("user", u);
                        startActivity(it);
                        finish();
                    } else {
                        doctor u = doctor.readSingleOBJ(arg0.getJSONObject("user"));
                        u.setAccessToken(arg0.getString("token"));


                        Intent it = new Intent(login.this, my_profile.class);
                        it.putExtra("doctor", u);
                        startActivity(it);
                        finish();
                    }
                    prgDialog.hide();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                //section performed when the request has unsuccessfully sent


                try {
                    JSONObject obj=new JSONObject(content);
                    if(obj.has("message")) {
                        Toast.makeText(login.this, R.string.fail_login, Toast.LENGTH_LONG).show();
                        prgDialog.hide();
                    }
                    else {
                        if(count!=5)
                        {
                            log_in(username,password,table,count+1);
                        }
                        else {
                            prgDialog.hide();
                            Toast.makeText(login.this, R.string.error, Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (Exception r){
                    if(count!=5)
                    {
                        log_in(username,password,table,count+1);
                    }
                    else {
                        prgDialog.hide();
                        Toast.makeText(login.this, R.string.error, Toast.LENGTH_LONG).show();
                    }
                }


            }
            });
        }







    public boolean save(String username,String password,String role){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            // run your one time code
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("account", true);
            editor.putString("UserName", username);
            editor.putString("PassWord", password);
            editor.putString("Role", role);
            editor.commit();
            return  false;

    }



    private void checklog_in(final int count) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("account", false)) {


            if(prefs.getString("Role","").equals("visitor"))
            {
                Intent it = new Intent(login.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        else {
                AsyncHttpClient client = new AsyncHttpClient();
                //    prgDialog.show();
                RequestParams req = new RequestParams();
                req.put("username", prefs.getString("UserName", ""));
                req.put("password", prefs.getString("PassWord", ""));

                req.put("table", prefs.getString("Role", ""));

                client.post(base.BASE_URL + "login", req, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(final String response) {
                        //Toast.makeText(login.this, response, Toast.LENGTH_LONG).show();
                        //section performed when the request has successfully sent

                        //Toast.makeText(login.this, response, Toast.LENGTH_LONG).show();
                        //    prgDialog.hide();
                        try {
                            JSONObject arg0 = new JSONObject(response);


                            if (prefs.getString("Role", "").equals("user")) {
                                user u = entities.user.readSingleOBJ(arg0.getJSONObject("user"));
                                u.setAccessToken(arg0.getString("token"));


                                Intent it = new Intent(login.this, MainActivity.class);
                                it.putExtra("user", u);
                                startActivity(it);
                                finish();
                            } else if (prefs.getString("Role", "").equals("doctor")) {
                                doctor u = doctor.readSingleOBJ(arg0.getJSONObject("user"));
                                u.setAccessToken(arg0.getString("token"));


                                Intent it = new Intent(login.this, my_profile.class);
                                it.putExtra("doctor", u);
                                startActivity(it);
                                finish();
                            } else {
                                ((ScrollView) findViewById(R.id.login_form)).setVisibility(View.VISIBLE);
                            }

                        } catch (final JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            ((ScrollView) findViewById(R.id.login_form)).setVisibility(View.VISIBLE);
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        //section performed when the request has unsuccessfully sent

                        //  Toast.makeText(login.this, content, Toast.LENGTH_LONG).show();
                        //    prgDialog.hide();

                        try {
                            JSONObject obj = new JSONObject(content);
                            if (obj.has("message")) {
                                Toast.makeText(login.this, R.string.fail_login, Toast.LENGTH_LONG).show();
                                ((ScrollView) findViewById(R.id.login_form)).setVisibility(View.VISIBLE);
                            } else {
                                if (count != 5) {
                                    checklog_in(count + 1);
                                } else {
                                    prgDialog.hide();
                                    Toast.makeText(login.this,"هناك مشكلة في الاتصال بشبكة الانترنيت ... الرجاء المحاولة  لاحقاً.", Toast.LENGTH_LONG).show();

                                    finish();
//                                    ((ScrollView) findViewById(R.id.login_form)).setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (Exception r) {
                            if (count != 5) {
                                checklog_in(count + 1);
                            } else {
                                prgDialog.hide();
//                                Toast.makeText(login.this, R.string.error, Toast.LENGTH_LONG).show();
//                                ((ScrollView) findViewById(R.id.login_form)).setVisibility(View.VISIBLE);
                                Toast.makeText(login.this,"هناك مشكلة في الاتصال بشبكة الانترنيت ... الرجاء المحاولة  لاحقاً.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }


                    }
                });
            }
        }
        else
        if(PrefUtils.getCurrentUser(login.this) != null){


            checkFBlogin(0);
        }
        else
        {

            ((ScrollView)findViewById(R.id.login_form)).setVisibility(View.VISIBLE);
        }

    }

    void checkFBlogin(final int count){

        final User   us=PrefUtils.getCurrentUser(login.this);
        AsyncHttpClient client = new AsyncHttpClient();
        //    prgDialog.show();
        RequestParams req = new RequestParams();
        req.put("table", us.role);
        req.put("facebookID", us.facebookID);

        client.post(base.BASE_URL + "loginFB", req, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(final String response) {
                //Toast.makeText(login.this, response, Toast.LENGTH_LONG).show();
                //section performed when the request has successfully sent


                //    prgDialog.hide();
                try {
                    JSONObject arg0 = new JSONObject(response);


                    if (us.role.equals("user")) {
                        user u = entities.user.readSingleOBJ(arg0.getJSONObject("user"));
                        u.setAccessToken(arg0.getString("token"));


                        Intent it = new Intent(login.this, MainActivity.class);
                        it.putExtra("user", u);
                        startActivity(it);
                        finish();
                    } else if (us.role.equals("doctor")) {
                        doctor u = doctor.readSingleOBJ(arg0.getJSONObject("user"));
                        u.setAccessToken(arg0.getString("token"));


                        Intent it = new Intent(login.this, my_profile.class);
                        it.putExtra("doctor", u);
                        startActivity(it);
                        finish();
                    } else {
                        ((ScrollView) findViewById(R.id.login_form)).setVisibility(View.VISIBLE);
                    }

                } catch (final JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    ((ScrollView) findViewById(R.id.login_form)).setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {


                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.has("message")) {
                        Toast.makeText(login.this, R.string.fail_login, Toast.LENGTH_LONG).show();
                        ((ScrollView) findViewById(R.id.login_form)).setVisibility(View.VISIBLE);
                    }
                    else {
                        if (count != 5) {
                            checkFBlogin(count + 1);
                        } else {

                            Toast.makeText(login.this,"هناك مشكلة في الاتصال بشبكة الانترنيت ... الرجاء المحاولة  لاحقاً.", Toast.LENGTH_LONG).show();
                              finish();

                        }
                    }
                } catch (Exception r) {
                    if (count != 5) {
                        checkFBlogin(count + 1);
                    } else {


                        Toast.makeText(login.this,"هناك مشكلة في الاتصال بشبكة الانترنيت ... الرجاء المحاولة  لاحقاً.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }



            }
        });

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

                if (PrefUtils.getCurrentUser(login.this) != null)
                    checkFBlogin(0);
                else {
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

            //progressDialog.dismiss();

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
                              //  Toast.makeText(login.this, response.toString(), Toast.LENGTH_LONG).show();
                                user = new User();
                                user.facebookID = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.fname = object.getString("first_name").toString();
                                user.name = object.getString("name").toString();
                                user.lname = object.getString("last_name").toString();
                                user.gender = object.getString("gender").toString();
//                                user.role=(spinner_type.getSelectedItemPosition()==0)?"user":"doctor";
                                checkIfLoginBefore(user,0);

                            }catch (Exception e){
                                e.printStackTrace();
                            }






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
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
            Toast.makeText(login.this,"يرجى اعادة محاولة التسجيل مرة اخرى",Toast.LENGTH_SHORT).show();
        }
    };




    void checkIfLoginBefore(final User u,final int count){

        AsyncHttpClient client = new AsyncHttpClient();
        //    prgDialog.show();
        RequestParams req = new RequestParams();
        req.put("facebookID", u.facebookID);

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
                            user u = entities.user.readSingleOBJ(arg0.getJSONObject("user"));
                            u.setAccessToken(arg0.getString("token"));


                            Intent it = new Intent(login.this, MainActivity.class);
                            it.putExtra("user", u);
                            startActivity(it);
                            finish();
                        } else if (arg0.getString("role").equals("doctor")) {
                            doctor u = doctor.readSingleOBJ(arg0.getJSONObject("user"));
                            u.setAccessToken(arg0.getString("token"));


                            Intent it = new Intent(login.this, my_profile.class);
                            it.putExtra("doctor", u);
                            startActivity(it);
                            finish();
                        } else {
                            Intent intent = new Intent(login.this, facebookRegisteration.class);
                            intent.putExtra("user", u);

                            startActivity(intent);
                            finish();
                        }
                    }
                    else
                    {
                        Intent intent=new Intent(login.this,facebookRegisteration.class);
                        intent.putExtra("user",u);

                        startActivity(intent);
                        finish();
                    }

                } catch (final JSONException e) {

                    Intent intent=new Intent(login.this,facebookRegisteration.class);
                    intent.putExtra("user",u);

                    startActivity(intent);
                    finish();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                checkIfLoginBefore(u,count+1);

            }
        });


    }

}
