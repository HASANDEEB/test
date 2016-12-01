package com.apps.scit.tabibihon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import entities.clinic;
import entities.doctor;
import entities.service;

public class ChooseLocation extends Activity {

    private static final int PLACE_PICKER_REQUEST = 187;
    private Button bLocation;
    private Place place;
    private doctor doc;
    ArrayList<service> service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);
        ((Button)findViewById(R.id.b_continue)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(place==null){
                            Toast.makeText(getApplicationContext(), "من فضلك حدد  موقع العيادة", Toast.LENGTH_LONG).show();
                        }
                        else {
                            clinic cl=new clinic();
                            cl.setLocation(place.getLatLng().toString());

                            Intent it=new Intent(getApplicationContext(),clinicRegisteration.class);
                            it.putExtra("doctor", doc);
                            it.putExtra("clinic", cl);
                            it.putExtra("service", service);
                            startActivity(it);
                            finish();
                        }
                    }
                }
        );


        if(getIntent().hasExtra("doctor")&&getIntent().hasExtra("service")){

            doc=(doctor)getIntent().getSerializableExtra("doctor");
            service=(ArrayList<service>)getIntent().getSerializableExtra("service");
        }
        else{
            finish();
        }


        bLocation = (Button) findViewById(R.id.p_pick);
        bLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(ChooseLocation.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Snackbar.make(v, "ERROR: Google Play services are not available!!",
                            Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    Snackbar.make(v, "ERROR: Google Play services are not available!!",
                            Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(data, this);
                bLocation.setText(place.getName());
                String toastMsg = String.format("المكان: %s", place.getName());
            //    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
