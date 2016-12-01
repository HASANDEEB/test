package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import entities.clinic;
import entities.doctor;
import extra.TextInit;

/**
 * Created by Rock on 5/22/2016.
 */
public class doctorRegisterSubInfo extends Activity {



    private ProgressDialog prgDialog;
    private doctor doc;
    private clinic cl;
    EditText fname,lname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_doctor_shortinfo);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        fname=(EditText)findViewById(R.id.doc_f_name);
        lname=(EditText)findViewById(R.id.doc_l_name);


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

                    cont();


                    }
                }
        );

    }





    private void cont(){

        fname.setError(null);

        lname.setError(null);



        // Store values at the time of the login attempt.
        String fname_ = fname.getText().toString();
        String lname_ = lname.getText().toString();




        boolean cancel = false;
        View focusView = null;


            if (TextUtils.isEmpty(fname_)) {
                fname.setError(getString(R.string.error_field_required));
                focusView = fname;
                cancel = true;
            }

            if (TextUtils.isEmpty(lname_)) {
                lname.setError(getString(R.string.error_field_required));
                focusView = lname;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt signup and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {


                doc.setFname(TextInit.check(fname_));
                doc.setLname(TextInit.check(lname_));


                if(doc.getType().equals("0")) {
                    Intent it = new Intent(doctorRegisterSubInfo.this, Speciality.class);
                    it.putExtra("doctor", doc);
                    startActivity(it);
                    finish();
                }
                else {
                    Intent it = new Intent(doctorRegisterSubInfo.this, clinicRegisteration.class);
                    it.putExtra("doctor", doc);
                    startActivity(it);
                    finish();
                }


        }



    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}