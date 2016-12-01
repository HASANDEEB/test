package com.apps.scit.tabibihon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Rock on 5/22/2016.
 */
public class userType extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_user_type2);

        doctor_click cl1=new doctor_click();
        pharmacy_click cl2=new pharmacy_click();
        user_click cl3=new user_click();
        visitor_click cl4=new visitor_click();

        findViewById(R.id.b_doc).setOnClickListener(cl1);
        findViewById(R.id.b_ph).setOnClickListener(cl2);
        findViewById(R.id.b_us).setOnClickListener(cl3);
        findViewById(R.id.b_visitor).setOnClickListener(cl4);
        findViewById(R.id.b_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userType.this,login.class));
                finish();
            }
        });



    }


    @Override
    protected void onPause() {
        super.onPause();

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


        class doctor_click implements View.OnClickListener {


            @Override
            public void onClick(View v) {
                Intent ii = new Intent(userType.this, doctorRegisterAccount.class);
                ii.putExtra("type","0");
                startActivity(ii);
                finish();

            }

    }
    class user_click implements View.OnClickListener {


            @Override
            public void onClick(View v) {
                Intent ii = new Intent(userType.this, userRegisterAccount.class);

                startActivity(ii);
                finish();

            }

    }
    class pharmacy_click implements View.OnClickListener {


            @Override
            public void onClick(View v) {
                Intent ii = new Intent(userType.this, doctorRegisterAccount.class);
                ii.putExtra("type","1");
                startActivity(ii);
                finish();

            }

    }
    class visitor_click implements View.OnClickListener {


            @Override
            public void onClick(View v) {
                try {
                    Intent it = new Intent(userType.this, MainActivity.class);
                    save("","","visitor");
                    startActivity(it);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

    }

}