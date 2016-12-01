package com.apps.scit.tabibihon;


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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import extra.DirectionsJSONParser;

public class MapsActivity extends Activity implements LocationListener{
    LatLng Point = new LatLng(21 , 57);
    private GoogleMap map;
    String location="lat/lng: (21.8938073,30.1382496)";
    Marker TP2=null;
    boolean press=false;
    LocationManager locationManager;
    String bestProvider,type="1";
    private static final int GPS_REQUEST = 187;


    @Override
    public void onBackPressed() {
        try {
            locationManager.removeUpdates(this);
        }catch (Exception c){}
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(getIntent().hasExtra("location")) {
            location = getIntent().getStringExtra("location");
            type=getIntent().getStringExtra("type");
        }
        else
            finish();


        if(isGooglePlayServicesAvailable()) {

            if (getIntent().hasExtra("me")) {
                ((FloatingActionButton) findViewById(R.id.fab_direction)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.result)).setVisibility(View.GONE);
            }

            try {
                location = location.substring(location.indexOf("(") + 1, location.length() - 1);
                String[] loc = location.split(",");
                Point = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));

                if (map == null) {
                    map = ((MapFragment) getFragmentManager().
                            findFragmentById(R.id.map)).getMap();
                }
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                String tit="";
                if(type.equals("0"))
                    tit="موقع الطبيب";
                else
                if(type.equals("1"))
                    tit="موقع الصيدلية";
                else
                    tit="موقع المشفى";
                Marker TP = map.addMarker(new MarkerOptions().
                        position(Point).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(tit));


                map.moveCamera(CameraUpdateFactory.newLatLngZoom(Point, 15.0f));


                //map.setMyLocationEnabled(true);
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


            } catch (Exception e) {
                e.printStackTrace();
            }


            ((FloatingActionButton) findViewById(R.id.fab_direction)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bestProvider = LocationManager.NETWORK_PROVIDER;
                    try {
                        if (!press) {
                            if (chekLocation()) {
                                ((TextView) findViewById(R.id.result)).setText("يتم الان رسم المسار ... الرجاء الانتظار ...");


                                locationManager.requestLocationUpdates(bestProvider, 60000, 0, MapsActivity.this);
                                press = true;
                            }

                        }
                    } catch (Exception c) {
                    }


                }
            });

        }



    }




    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "لا يمكن رسم المسار", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }
            duration=duration.replace("hours","ساعة");
            duration=duration.replace("hour","ساعة");
            duration=duration.replace("mins","دقيقة");
            duration=duration.replace("min","دقيقة");
            duration=duration.replace("days","يوم");
            duration=duration.replace("day","يوم");

            ((TextView)findViewById(R.id.result)).setText( "المدة : "+duration+" , "+  "المسافة : "+distance );

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }




    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        //Toast.makeText(MapsActivity.this,latLng.toString(),Toast.LENGTH_SHORT).show();
        if(TP2!=null)
        {
         TP2.setPosition(latLng);
        }
        else {
            TP2 = map.addMarker(new MarkerOptions().
                    position(latLng
                    ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("موقعك"));
        }

        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
       // map.animateCamera(CameraUpdateFactory.zoomTo(15));
       // locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);

        LatLng dest = Point;
        LatLng origin =latLng ;
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

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

                finish();
            }
            else
            {
                ((TextView) findViewById(R.id.result)).setText("يتم الان رسم المسار ... الرجاء الانتظار ...");


                locationManager.requestLocationUpdates(bestProvider, 60000, 0, MapsActivity.this);
                press = true;

            }
        }
        if(requestCode==0) {
            if (ConnectionResult.SUCCESS != GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
                finish();
            }
        }

    }

}
