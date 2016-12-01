package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entities.base;
import entities.doctor;
import entities.user;
import extra.MyArrayAdapter;
import extra.TextValidator;
import extra.checkPassword;

/**
 * Created by Rock on 5/26/2016.
 */
public class signup extends Activity {

    EditText username,password,repassword;
    Spinner spinner_type;
    ArrayList<String> list_type=new ArrayList<String>();
    ArrayAdapter<String> adapter_type;
    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);


        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        list_type.add("مستخدم");
        list_type.add("طبيب");
        list_type.add("صيدلاني");
        spinner_type=(Spinner)findViewById(R.id.user_type);

         username=(EditText)findViewById(R.id.username);
         password=(EditText)findViewById(R.id.password);
         repassword=(EditText)findViewById(R.id.re_password);

        password.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });


        adapter_type = new MyArrayAdapter<String>(this,R.layout.spinner_item, list_type);
        spinner_type.setAdapter(adapter_type);

        password.addTextChangedListener(new TextValidator(password) {

            @Override
            public void validate(TextView textView, String text) {

                textView.setError(checkPassword.check(text, signup.this));

            }
        });






        ((Button) findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            signup();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        ((Button) findViewById(R.id.b_register)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            Intent it = new Intent(signup.this, login.class);
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


    public void signup() throws UnsupportedEncodingException, JSONException {


        username.setError(null);
        password.setError(null);
        repassword.setError(null);

        // Store values at the time of the login attempt.

        String password_ = password.getText().toString();
        String repassword_ = repassword.getText().toString();
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

            if (TextUtils.isEmpty(repassword_)) {
                repassword.setError(getString(R.string.error_field_required));
                focusView = repassword;
                cancel = true;
            }

            if (!password_.equals(repassword_)) {
                repassword.setError("كلمة المرور غير متطابقة");
                focusView = repassword;
                cancel = true;
            }




            if (cancel) {
                // There was an error; don't attempt signup and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                prgDialog.show();

                SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");

                base signed=null;
                if(spinner_type.getSelectedItemPosition()==0) {
                    signed = new user(username_, password_, "", "", "active", "0", df.format(new Date()));
                }
                else
                if(spinner_type.getSelectedItemPosition()==2) {
                    signed=new doctor( username_,  password_, "", "",  "",
                            "",  "", "", "", "",username_, "0", df.format(new Date()),null,null,"");
                    ((doctor)signed).setType("1");
                }
                else
                    {
                    signed=new doctor( username_,  password_, "", "",  "",
                             "",  "", "", "", "",username_, "0", df.format(new Date()),null,null,"");
                        ((doctor)signed).setType("0");
                    }

                    check(signed, username_,0);


            }


    }



    private void check(final base signed,  final String username_, final int count) throws UnsupportedEncodingException, JSONException {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams request=new RequestParams();
        request.put("username", username_);
        if(signed instanceof user)
        request.put("table", "user");
        else
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
                            createAcount(signed,0);

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
                if (count != 5) {
                    try {
                        check(signed, username_, count + 1);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
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

    void createAcount(final base signed,final int count){
// "application/json; charset=UTF-8",
        if(signed instanceof user) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams req= ((user)signed).getEntity();

            client.post(base.BASE_URL + "signupUser",req, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {


                    try {
                        JSONObject arg0 = new JSONObject(response);

                        user u=user.readSingleOBJ(arg0.getJSONObject("user"));
                        u.setAccessToken(arg0.getString("token"));

                        save(((user) signed).getUsername(),((user) signed).getPassword(),"user");

                        Intent it=new Intent(signup.this,MainActivity.class);
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
                        createAcount(signed,count+1);
                    }
                    prgDialog.hide();
                    showMsg(getResources().getString(R.string.error));

                }
            });
        }
        else
        {


            Intent it=new Intent(signup.this,doctorRegisterSubInfo.class);
            it.putExtra("doctor",signed);
            startActivity(it);
            finish();


        }

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


}