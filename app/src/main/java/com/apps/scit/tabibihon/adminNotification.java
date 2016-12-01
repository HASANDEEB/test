package com.apps.scit.tabibihon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class adminNotification extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin_notification);



        if(getIntent().hasExtra("notif"))
        ((TextView)findViewById(R.id.notif)).setText(getIntent().getStringExtra("notif"));
        else
        finish();


        ((Button)findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                }
        );



    }


    @Override
    protected void onPause() {
        super.onPause();

    }
}