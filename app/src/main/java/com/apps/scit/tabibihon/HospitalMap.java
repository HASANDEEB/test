package com.apps.scit.tabibihon;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;

import entities.base;
import entities.doctor;
import entities.hospital_wrapper;
import entities.result;
import entities.search_param;
import entities.user;

public class HospitalMap extends Activity {
    LatLng Point = new LatLng(21 , 57);
    private GoogleMap map;
    doctor doc=null;
    user us=null;
    boolean distanceSearch=false;
    double distance=-1;
    String location="lat/lng: (21.8938073,30.1382496)";
    String mylocation=null;
    HashMap<String,hospital_wrapper> hospital_maps=new HashMap<String,hospital_wrapper>();
    private ProgressDialog prgDialog;
    public static RequestParams requset_search;
    LocationManager locationManager;
    String bestProvider;
    private static final int GPS_REQUEST = 187;
    @Override
    public void onBackPressed() {

        finish();
    }

    ArrayList<hospital_wrapper> hospitals=new ArrayList<hospital_wrapper>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_hsopital_map);


        isGooglePlayServicesAvailable();


        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);



        if(getIntent().hasExtra("hospital"))
        {
            hospitals = (ArrayList<hospital_wrapper>) getIntent().getSerializableExtra("hospital");
        }
        else
            finish();

/////////////////////////////////////
        try {


            if (map == null) {
                map = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);



            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.8938073, 37.1382496), 7.0f));

            ((TextView)findViewById(R.id.result)).setText("المشافي الموجودة في مدينة "+hospitals.get(0).getCity());

            getSearch();



        } catch (Exception e) {
           // Toast.makeText(this,hospitals.size()+hospitals.toString(),Toast.LENGTH_LONG).show();
        }






    }





    private void getSearch() {

        prgDialog.show();

        Paint paint=new Paint();
        paint.setColor(getResources().getColor(R.color.myblue));


        for (hospital_wrapper r : hospitals) {
            if(!r.getHos().getLocation().trim().isEmpty()) {
                try {
                    location = r.getHos().getLocation();
                    location = location.substring(location.indexOf("(") + 1, location.length() - 1);
                    String[] loc = location.split(",");
                    Point = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));


                    Marker m = map.addMarker(new MarkerOptions().
                            position(Point)

                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.bluebox, r.getHos().getName()))));

                    // m.showInfoWindow();
                    hospital_maps.put(m.getId(), r);
                }catch (Exception cc){}
            }

        }

        prgDialog.hide();
    }


    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);


        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/DroidKufi-Regular.ttf");

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.myred));
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(this, 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(this, 6));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
      //  int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;
        int yPos = (int) ( -2* ((paint.descent() + paint.ascent()))) ;

        canvas.drawText(text, xPos, yPos, paint);

        return  bm;
    }



    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }



    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            //GooglePlayServicesUtil.showErrorNotification(status, getApplicationContext());
            return false;
        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode==0) {
                if (ConnectionResult.SUCCESS != GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
                    finish();
                }
            }
    }


}
