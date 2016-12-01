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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entities.base;
import entities.doctor;
import entities.result;
import entities.search_param;
import entities.user;
import extra.DirectionsJSONParser;

public class searchMapsActivity extends Activity implements LocationListener{
    LatLng Point = new LatLng(21 , 57);
    private GoogleMap map;
    doctor doc=null;
    user us=null;
    boolean distanceSearch=false;
    int distance=-1;
    String location="lat/lng: (21.8938073,30.1382496)";
    String mylocation=null;
    HashMap<String,result> doctor_maps=new HashMap<String,result>();
    private ProgressDialog prgDialog;
    public static RequestParams requset_search;
    LocationManager locationManager;
    String bestProvider;
    private static final int GPS_REQUEST = 187;
    @Override
    public void onBackPressed() {

        finish();
    }

    ArrayList<result> doctors=new ArrayList<result>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);


        isGooglePlayServicesAvailable();


        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        search_param sea =(search_param)getIntent().getSerializableExtra("search");

        requset_search=new RequestParams();

        requset_search.put("token", sea.token);
        requset_search.put("specialization_ID", sea.spec);
        requset_search.put("name", sea.name);
        requset_search.put("area_ID", sea.ar);
        requset_search.put("city_ID", sea.ci);
        requset_search.put("type", sea.type);


        if(getIntent().hasExtra("doctor"))
        {
            doc = (doctor) getIntent().getSerializableExtra("doctor");
        }
        else
            us = (user) getIntent().getSerializableExtra("user");

/////////////////////////////////////
        try {


            if (map == null) {
                map = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


          //  final String[] tempmarker = {null};
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                   // if(marker.getId().equals( tempmarker[0])) {
                        result r = doctor_maps.get(marker.getId());

                        Intent ii;
                        if (doc != null) {
                            if (doc.getID() == r.id) {
                                ii = new Intent(searchMapsActivity.this, my_profile.class);
                                ii.putExtra("search", "1");
                            } else {
                                ii = new Intent(searchMapsActivity.this, profile.class);
                                ii.putExtra("id", r.id);
                            }
                            ii.putExtra("doctor", doc);
                            ii.putExtra("type", r.type);
                        } else
                        {
                            ii = new Intent(searchMapsActivity.this, profile.class);
                            if (us != null)
                            ii.putExtra("user", us);
                            ii.putExtra("id", r.id);
                            ii.putExtra("type", r.type);
                        }

                        startActivity(ii);

                    return true;
                }
            });
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.8938073, 37.1382496), 7.0f));


        //////////////////////////////////////
        if(getIntent().hasExtra("distance"))
        {
            distanceSearch=true;
            distance=getIntent().getIntExtra("distance",1);


            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            bestProvider = LocationManager.NETWORK_PROVIDER;
            prgDialog.show();
            if (chekLocation()) {


                Location loca = locationManager.getLastKnownLocation(bestProvider);
                mylocation= new LatLng(loca.getLatitude(), loca.getLongitude()).toString();
                locationManager.requestSingleUpdate(bestProvider,this,null);

            }
        }


            if(!distanceSearch) {

                do_search(requset_search,0);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }






    }



    void do_search(final RequestParams request,final int count) {

        prgDialog.show();

        String url="";
        if(distanceSearch) {
            request.put("distance", distance + "");
            if(mylocation==null) {
                Toast.makeText(this, "لا يمكن جلب معلومات الموقع ", Toast.LENGTH_SHORT).show();
                return;
            }

            request.put("location",mylocation);
            url = "search/distance";
        }
        else
        {
            url="search/map";
        }



        AsyncHttpClient client = new AsyncHttpClient();
        client.post(base.BASE_URL + url, request, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {

                doctors = result.readOBJArray(response);

                getSearch();


                prgDialog.hide();

            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5){
                 do_search(request,count+1);
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();

                    prgDialog.hide();
                }


            }
        });
    }

    private void getSearch() {



        Paint paint=new Paint();
        paint.setColor(getResources().getColor(R.color.myblue));


        for (result r : doctors) {
            if(!r.location.trim().isEmpty()) {
                try{
                location = r.location;
                location = location.substring(location.indexOf("(") + 1, location.length() - 1);
                String[] loc = location.split(",");
                Point = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));



                Marker m=  map.addMarker(new MarkerOptions().
                        position(Point)

                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.bluebox, r.doc))));

                m.showInfoWindow();
                doctor_maps.put(m.getId(),r);
            }catch (Exception cc){}
            }

        }
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

    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        //Toast.makeText(this,"location change"+latLng.toString(),Toast.LENGTH_LONG).show();

        mylocation=latLng.toString();
      //  map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        do_search(requset_search,0);

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
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


    boolean chekLocation()
    {


        if( !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)&&
                !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("تحديد الموقع غير مفعل");  // GPS not found
            builder.setMessage("هل ترغب في تفعيله"); // Want to enable?
            builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),GPS_REQUEST);
                }
            });
            builder.setNegativeButton("لا", null);
            builder.create().show();

            return false;
        }
        else
            return true;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == GPS_REQUEST) {
                if( !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    prgDialog.hide();
                    finish();
                }
                else
                locationManager.requestSingleUpdate(bestProvider,this,null);
            }

        if(requestCode==0) {
            if (ConnectionResult.SUCCESS != GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
                finish();
            }
        }

    }

}
