package com.apps.scit.tabibihon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import entities.doctor;
import extra.TextInit;

/**
 * Created by Rock on 5/22/2016.
 */
public class doctorRegisterInfo extends Activity {

    EditText university,info,email,phone,mobile;
    doctor doc=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_doctor_info);


        university=(EditText)findViewById(R.id.university);
        info=(EditText)findViewById(R.id.additional_info);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);
        mobile=(EditText)findViewById(R.id.mobile);


        if(getIntent().hasExtra("doctor")){

            doc=(doctor)getIntent().getSerializableExtra("doctor");

        }
        else{
            finish();
        }


        ((Button)findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     continue_register();
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


            if (cancel) {
                // There was an error; don't attempt signup and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {

                doc.setUniversity(TextInit.check(uni));
                doc.setAdditional_info(TextInit.check(inf));
                doc.setEmail(em);
                doc.setMobile(mob);
                doc.setPhone(ph);

             Intent it=new Intent(this,Speciality.class);
                it.putExtra("doctor",doc);
                startActivity(it);
                finish();

            }




    }


    @Override
    protected void onPause() {
        super.onPause();

    }
}