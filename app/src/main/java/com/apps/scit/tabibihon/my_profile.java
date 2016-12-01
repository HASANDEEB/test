package com.apps.scit.tabibihon;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;

import android.text.TextUtils;
import android.util.Log;

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.Future;

import entities.area;
import entities.base;
import entities.doctor;
import entities.user;


public class my_profile extends Activity {
  private static  doctor doc=null;
    area ar=null;
    private static user us=null;
    private ProgressDialog prgDialog;
    doctor current=null;
    TextView name,speciality,adress,clinic_state,fav_num;
    String location="";
    boolean first=true;
    private static final int PLACE_PICKER_REQUEST = 187;
    Button blocation;
    RelativeLayout wait_info;
    ProgressBar photoWait;
    LinearLayout profi;
    ImageView ivImage;
    String userChoosenTask="";
    private static final int SELECT_FILE=1 ,REQUEST_CAMERA=2 ;
    public static final int OPEN_EDIT_ACTIVITY = 123456;
    private DisplayImageOptions options;
    private  boolean exit=false;
    private  boolean loaded=false;
    Switch enable;
    boolean first_time=false,finish_uploade=true;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_profile);

        if(ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().destroy();

            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.profile_icon4)
                .showImageOnFail(R.drawable.profile_icon4)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0))
                .build();

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        if(getIntent().hasExtra("doctor"))
        {

            doc=(doctor)getIntent().getSerializableExtra("doctor");
            GCMRegistrationIntentService.role="doctor";

        }

        else finish();
        int id=doc.getID();

        if(!getIntent().hasExtra("first")){

        first_time=false;
            GCMPushReceiverService.profile=true;
            GCMRegistrationIntentService.profile=1;
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //Check type of intent filter
                    if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                        //Registration success
                        String token = intent.getStringExtra("token");
                      //  Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();
                        //  ((EditText)findViewById(R.id.editText)).setText("GCM token:" + token);
                    } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                        //Registration error
                        Toast.makeText(getApplicationContext(), R.string.gcm_fail, Toast.LENGTH_LONG).show();
                    } else {
                        //Tobe define
                    }
                }
            };

            //Check status of Google play service in device
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if(ConnectionResult.SUCCESS != resultCode) {
                //Check type of error
                if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                   // Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                    //So notification
                    GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
                } else {
                   // Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
                }
            } else {
                //Start service
                Intent itent = new Intent(this, GCMRegistrationIntentService.class);
                startService(itent);
            }



        }
        else
        {
            first_time=true;
        }

        if(getIntent().hasExtra("search"))
            first_time=true;

        if(first_time){
            ((FloatingActionButton)findViewById(R.id.home_fab)).setVisibility(View.GONE);
            ((FloatingActionButton)findViewById(R.id.fab)).setVisibility(View.GONE);
        }


        name=(TextView)findViewById(R.id.prof_name);
        fav_num=(TextView)findViewById(R.id.favorites);
        speciality=(TextView)findViewById(R.id.prof_speciality);
        adress=(TextView)findViewById(R.id.prof_address);
        clinic_state=(TextView)findViewById(R.id.clinic_state);
        blocation=((Button)findViewById(R.id.place));
        wait_info=(RelativeLayout)findViewById(R.id.wait_layout);
        photoWait=(ProgressBar)findViewById(R.id.photo_sec);
        profi=(LinearLayout)findViewById(R.id.profile_sec);
        enable=(Switch)findViewById(R.id.switch1);
        ////////////////////////
        // web site navication
        ////////////////////////
        ((ImageView)findViewById(R.id.website)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.tabebakhoon.com"));
                startActivity(browserIntent);
            }
        });

        ((ImageView)findViewById(R.id.facebook)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/tabebakhoon"));
                startActivity(browserIntent);
            }
        });

        ((ImageView)findViewById(R.id.scit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.scitco.sy"));
                startActivity(browserIntent);
            }
        });

        ((ImageView)findViewById(R.id.nacabab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.sph-tartous.com"));
                startActivity(browserIntent);
            }
        });

        ////////////////////////
        ////////////////////////


        if(doc.getAuthorized().equals("1"))
            ((ImageView)findViewById(R.id.authorized)).setVisibility(View.VISIBLE);


        ((ImageView)findViewById(R.id.authorized)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] pos = new int[2];

                v.getLocationInWindow(pos);


                String content=getResources().getString(R.string.auth);
                Toast t = Toast.makeText(my_profile.this, content, Toast.LENGTH_SHORT);
                t.setGravity(Gravity.TOP | Gravity.LEFT, pos[0] - ((content.length() / 2) * 12), pos[1] - 128);
                t.show();
            }
        });


        ((FloatingActionButton)findViewById(R.id.pharmacy_fab)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final CharSequence[] items = { "الصيدليات المناوبة","المشافي",
                                "إلغاء"};
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(my_profile.this);
                        //builder.setTitle("دعوة صديق");

                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                boolean result = Utility.checkPermission(my_profile.this);
                                if (item == 1) {
                                    if (result) {
                                        Intent ii = new Intent(my_profile.this, Hospitals.class);
                                        startActivity(ii);
                                    }
                                } else if (item == 0) {
                                    if (result) {
                                        Intent ii = new Intent(my_profile.this, chemistary.class);
                                        startActivity(ii);
                                    }
                                } else if (items[item].equals("إلغاء")) {
                                    dialog.dismiss();
                                }
                            }
                        });
                        builder.show();


                    }
                }
        );


        ((TextView)findViewById(R.id.visits)).setText("عدد الزيارات :" + doc.getVisits());

          do_search(id,0);





        blocation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(isGooglePlayServicesAvailable()) {

                            if (!TextUtils.isEmpty(location)) {
                                Intent ii = new Intent(my_profile.this, MapsActivity.class);
                                ii.putExtra("location", location);
                                ii.putExtra("me", "1");
                                ii.putExtra("type", doc.getType());
                                startActivity(ii);
                            } else {


                                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                                try {
                                    startActivityForResult(builder.build(my_profile.this), PLACE_PICKER_REQUEST);
                                } catch (GooglePlayServicesRepairableException e) {
                                    Snackbar.make(v, "من فضلك قم بتحديث خدمات   Google Play",
                                            Snackbar.LENGTH_LONG).show();
                                    e.printStackTrace();
                                } catch (GooglePlayServicesNotAvailableException e) {
                                    Snackbar.make(v, "من فضلك قم بتحديث خدمات   Google Play",
                                            Snackbar.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }


                            }
                        }
                        else
                        {
                            Snackbar.make(v, "من فضلك قم بتحديث خدمات   Google Play",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
        );


//        if(doc.getType().equals("1"))
//            ((Button)findViewById(R.id.advice)).setVisibility(View.GONE);

        ((Button)findViewById(R.id.advice)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ii = new Intent(my_profile.this, doctorAdvice.class);
                        if (doc == null)
                            ii.putExtra("user", us);
                        else
                            ii.putExtra("doctor", doc);
                        ii.putExtra("target", doc);

                        startActivity(ii);
                    }
                }
        );


        ivImage = (ImageView)findViewById(R.id.ivImage);


        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    if (!loaded)
                        selectImage();
                    else {
                        changeImage();

                    }
                } catch (Exception vv) {
                }
            }
        });

        ((Button)findViewById(R.id.photo_upload)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (finish_uploade) {
                        if (!loaded)
                            selectImage();
                        else {
                            changeImage();
                        }
                    } else {
                        Toast.makeText(my_profile.this, "يرجى انتظار انتهاء تحميل الصورة", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception vv) {
                }
            }
        });



        ((FloatingActionButton)findViewById(R.id.fab)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Add_adviceDialog();

                        Intent ii = new Intent(my_profile.this, search.class);
                        ii.putExtra("doctor", doc);
                        startActivity(ii);


                    }
                }
        );



        ((FloatingActionButton)findViewById(R.id.home_fab)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(my_profile.this);
                        builder.setTitle("اغلاق التطبيق");  // GPS not found
                        builder.setMessage("هل انت متأكد انك تريد اغلاق التطبيق"); // Want to enable?
                        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                android.os.Process.killProcess(android.os.Process.myPid());

                            }
                        });
                        builder.setNegativeButton("لا", null);
                        builder.create().show();
                    }
                }
        );


        ((FloatingActionButton)findViewById(R.id.edit_fab)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(my_profile.this, edit.class);
                        it.putExtra("doctor", doc);
                        it.putExtra("area", ar);
                        startActivityForResult(it, OPEN_EDIT_ACTIVITY);

                    }
                }
        );

        ((Button)findViewById(R.id.invite)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CharSequence[] items = {"عبر رسالة نصية", "عبر واتس اب",
                                "إلغاء"};
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(my_profile.this);
                        builder.setTitle("دعوة صديق");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                boolean result = Utility.checkPermission(my_profile.this);
                                if (item == 0) {
                                    if (result)
                                        sendSimpleMsg();
                                } else if (item == 1) {
                                    if (result)
                                        sendWhatsapppMsg();
                                } else if (items[item].equals("إلغاء")) {
                                    dialog.dismiss();
                                }
                            }
                        });
                        builder.show();

                    }
                }
        );


        ((Button)findViewById(R.id.doctor_advice)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ii = new Intent(my_profile.this, AdviceActivity.class);
                        ii.putExtra("user", doc);
                        startActivity(ii);
                    }
                }
        );

        ((Button)findViewById(R.id.admin_notif)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(my_profile.this,doctorAdminNotification.class);
                it.putExtra("id",doc.getID());
                startActivity(it);
            }
        });


        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enable.isChecked()) {

                    editEnabled("0",0);
                } else {

                    editEnabled("1",0);
                }
            }
        });



        if(doc.getImage().startsWith("https"))
            loadImage( doc.getImage());
        else
       loadImage(base.BASE_URL +"256/"+ doc.getImage());
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!first_time) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        }
    }

    @Override
    public void onBackPressed() {
        if(!first_time) {
            if (exit) {
                finish();
            } else {
                exit = true;
                Toast.makeText(this, getString(R.string.confirm_exit), Toast.LENGTH_SHORT).show();
                findViewById(R.id.home_fab).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 2000);
            }
        }
		else{
		finish();
		}
    }




    void loadImage(String url){
        if(!TextUtils.isEmpty(doc.getImage())) {
            ivImage.setVisibility(View.INVISIBLE);
            photoWait.setVisibility(View.VISIBLE);


            ImageLoader.getInstance().displayImage(url, ivImage, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ((ImageView) view).setImageBitmap(bitmap);
                    ivImage.setVisibility(View.VISIBLE);
                    photoWait.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });


        }


    }

    void upload(final File f)
    {
        ivImage.setVisibility(View.INVISIBLE);
        photoWait.setVisibility(View.VISIBLE);
        finish_uploade=false;

        Future uploading = Ion.with(my_profile.this)
                .load(base.BASE_URL + "upload_photo")
                .setMultipartFile("image", f)
                .setMultipartParameter("ID", doc.getID() + "")
                .setMultipartParameter("token", doc.getAccessToken())
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        try {
                            JSONObject obj=new JSONObject(result.getResult());

                            doc.setImage(obj.getString("image"));
                            loaded=true;

                        } catch (JSONException e1) {
                            Toast.makeText(my_profile.this,getResources().getString(R.string.upload_fail),Toast.LENGTH_SHORT).show();
                        }

                        finish_uploade=true;

                        loadImage("file:///" + f.getPath());



                    }
                });


    }





    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "اختر صورة"), SELECT_FILE);
    }



    private void changeImage() {
        final CharSequence[] items = { "تغيير الصورة", "تدوير الصورة",
                "إلغاء" };
        AlertDialog.Builder builder = new AlertDialog.Builder(my_profile.this);
        builder.setTitle("الصورة الشخصية");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(my_profile.this);
                if (item == 0) {
                    if (result)
                        selectImage();
                } else if (item == 1) {
                    if (result)
                        rotateImage();
                } else if (items[item].equals("إلغاء")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void rotateImage() {
        final CharSequence[] items = { "تدوير 90 درجة الى اليمين", "تدوير 180 درجة", "تدوير 90 درجة الى اليسار",
                "إلغاء" };
        AlertDialog.Builder builder = new AlertDialog.Builder(my_profile.this);
        builder.setTitle(" تدوير الصورة");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(my_profile.this);
                if (item == 0) {
                    if (result)
                        rotate("90");
                } else if (item == 1) {
                    if (result)
                        rotate("180");
                } else if (item == 2) {
                    if (result)
                        rotate("270");
                } else if (items[item].equals("إلغاء")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    private void rotate(String angle){

        RequestParams request=new RequestParams();
        request.put("angle", angle);
        request.put("image", doc.getImage());


        AsyncHttpClient client = new AsyncHttpClient();
        client.post(base.BASE_URL + "rotate_photo", request, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {


                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("message").equals("success")) {
                        ImageLoader.getInstance().clearDiskCache();
                        ImageLoader.getInstance().clearMemoryCache();
                        loadImage(base.BASE_URL + "256/" + doc.getImage());
                    } else {
                        Toast.makeText(getApplicationContext(), "حدثت مشكلة في عملية تعديل الصورة ", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();


            }
        });


    }


 private void selectImage() {
        final CharSequence[] items = { "التقاط من الكاميرا", "صورة من الاستديو",
                "إلغاء" };
        AlertDialog.Builder builder = new AlertDialog.Builder(my_profile.this);
        builder.setTitle("الصورة الشخصية");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(my_profile.this);
                if (items[item].equals("التقاط من الكاميرا")) {
                    userChoosenTask = "التقاط من الكاميرا";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("صورة من الاستديو")) {
                    userChoosenTask = "صورة من الاستديو";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("إلغاء")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {

             try {

                 onSelectFromGalleryResult(data);
             }
             catch (Exception ex){
                 Toast.makeText(my_profile.this,ex.getMessage(),Toast.LENGTH_LONG).show();
             }
            }
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, my_profile.this);
               updateLocation(place.getLatLng().toString(),0);


            }
        }
        if (requestCode == OPEN_EDIT_ACTIVITY) {
          do_search(doc.getID(),0);
        }

    }



    void updateLocation(final String latLng,final int count){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req= new RequestParams();
        req.put("ID",doc.getClinic_ID()+"");
        req.put("location", latLng);
        req.put("token", doc.getAccessToken());
        client.post(base.BASE_URL + "update_location", req, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {

                try {
                    JSONObject arg0 = new JSONObject(response);

                    if (arg0.has("statue")) {

                        location = latLng;
                        blocation.setText("عرض الموقع على الخريطة");
                    } else
                        Toast.makeText(my_profile.this, "حصل خطأ في عملية التعديل", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                prgDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5){
                    updateLocation(latLng, count+1);
                }else {
                    prgDialog.hide();
                    Toast.makeText(my_profile.this, R.string.error, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        String path ="";

        path=getPath(my_profile.this,data.getData());
        //Toast.makeText(my_profile.this,data.getData().getAuthority(),Toast.LENGTH_LONG).show();

        if(path==null||path.isEmpty())
            Toast.makeText(my_profile.this, "لا يمكن تحميل الصورة", Toast.LENGTH_SHORT).show();
        else {
            //Toast.makeText(my_profile.this,path, Toast.LENGTH_SHORT).show();
            ivImage.setImageURI(data.getData());
            upload(new File(path));
        }


    }


     void showmsg(String s)
    {

        Toast.makeText(my_profile.this,s, Toast.LENGTH_SHORT).show();
    }

/////////////

@TargetApi(Build.VERSION_CODES.KITKAT)
public  String getPath(final Context context, final Uri uri) {
try {


    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
            else
            {
                return  System.getenv("SECONDARY_STORAGE")+"/"+split[1];
            }

            // TODO handle non-primary volumes
        }
        // DownloadsProvider
        else if (isDownloadsDocument(uri)) {

            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

            return getDataColumn(context, contentUri, null, null);
        }
        // MediaProvider
        else if (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    split[1]
            };

            return getDataColumn(context, contentUri, selection, selectionArgs);
        }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {
        return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
        return uri.getPath();
    }

    return null;
}
catch (Exception rr){return null;}
}

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }






    /////

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);


        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri tempUri = getImageUri(getApplicationContext(), thumbnail);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File finalFile = new File(getRealPathFromURI(tempUri));

        ivImage.setImageURI(data.getData());
        upload(finalFile);

        ivImage.setImageBitmap(thumbnail);
    }




    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }







    void do_search(final int id,final int count){

        wait_info.setVisibility(View.VISIBLE);
        profi.setVisibility(View.GONE);

        RequestParams request=new RequestParams();
        request.put("ID",id+"");

        request.put("token", (doc == null) ? us.getAccessToken() : doc.getAccessToken());


        String url="search/doctor";
        if(doc.getType().equals("1")) {
            url = "search/pharmacy";
            fav_num.setVisibility(View.GONE);
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(base.BASE_URL + url, request, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {


                    JSONArray array = new JSONArray(response);
                    JSONObject obj = array.getJSONObject(0);

                    String t=doc.getAccessToken();
                    current = doctor.readSingleOBJ(obj);
                    doc=current;
                    doc.setAccessToken(t);
                    ar=new area();
                    ar.setID(obj.getInt("area_ID"));
                    ar.setCity_ID(obj.getString("city_ID"));


                    if(obj.getString("enabled").equals("0"))
                    {
                        enable.setChecked(false);
                        clinic_state.setText("غير متواجد حالياً");
                    }
                    else
                    {
                        enable.setChecked(true);
                        clinic_state.setText(" متواجد حالياً");

                    }
                    int inUserFavourite=Integer.parseInt(obj.getString("number"));
                    fav_num.setText("أنت في مفضلة "+inUserFavourite+" شخص");


                    if(doc.getType().equals("0")) {
                        speciality.setText("الاختصاص : " + obj.getString("speciality"));
                        name.setText("د. "+ current.getFname() + " " + current.getMname() + " " + current.getLname());
                    }
                    else{
                        speciality.setText("صيدلية : " + obj.getString("clinic"));
                        name.setText("ص. "+ current.getFname() + " " + current.getMname() + " " + current.getLname());
                    }
                    String add = obj.getString("country") +" "+obj.getString("city") +" "+ obj.getString("area") + ((!TextUtils.isEmpty(obj.getString("street")))?"\n" + "شارع " + obj.getString("street"):"");
                    adress.setText(add);
                    location = obj.getString("location");
                    if (!TextUtils.isEmpty(location)) {
                        if(doc.getType().equals("0"))
                        blocation.setText(" موقع العيادة على الخريطة");
                        else
                            blocation.setText(" موقع الصيدلية على الخريطة");

                    }
                    else
                    {

                        if(doc.getType().equals("0"))
                            blocation.setText("تحديد موقع العيادة على الخريطة");
                        else
                            blocation.setText("تحديد موقع الصيدلية على الخريطة");
                    }
                    if(doc.getImage().trim().equals("")){
                        loaded=false;
                    }
                    else
                    {
                        loaded=true;
                    }

                    findViewById(R.id.statistic).setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
                }

                wait_info.setVisibility(View.GONE);
                profi.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5 )
                do_search(id,count+1);
                else {
//                wait_info.setVisibility(View.GONE);
//                profi.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),R.string.error, Toast.LENGTH_SHORT).show();

                }

            }
        });


    }




    public static void updateToken(String token){

        RequestParams req= new RequestParams();
        if(doc!=null){
            req.put("ID",doc.getID()+"");
            req.put("table","doctor");
            req.put("token",doc.getAccessToken());

        }
        else{
            req.put("ID",us.getID()+"");
            req.put("table","user");
            req.put("token",us.getAccessToken());

        }
        req.put("mytoken", token);

        AsyncHttpClient client = new AsyncHttpClient();



        client.post(base.BASE_URL + "editToken",req, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {


            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {

                //  Toast.makeText(getContext(), R.string.error,Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void editEnabled(final String enables,final int count) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("ID", doc.getClinic_ID());
        req.put("token",doc.getAccessToken());
        req.put("enabled",enables);




        client.post(base.BASE_URL + "editClinicEnabled", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {

                if(enables.equals("0")) {
                    clinic_state.setText("غير متواجد حاليا");
                    enable.setChecked(false);
                }
                else {
                    clinic_state.setText("متواجد حاليا");
                    enable.setChecked(true);
                }

                prgDialog.hide();

            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5)
                {
                    editEnabled(enables,count+1);
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    prgDialog.hide();
                    if (enables.equals("1")) {
                        enable.setChecked(false);
                        clinic_state.setText("غير متواجد حاليا");

                    } else {
                        enable.setChecked(true);
                        clinic_state.setText("متواجد حاليا");
                    }
                }

            }
        });



    }




    public void sendSimpleMsg(){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", "استفد من جميع خدمات تطبيق طبيبك هون . يمكنك الان تحميله من هنا : " + ":http://tabebakhoon.com/download/?ref=" + doc.getID() + "&type=sms");
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);

    }
    public void sendWhatsapppMsg(){
        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text ="استفد من جميع خدمات تطبيق طبيبك هون . يمكنك الان تحميله من هنا : "+ ":http://tabebakhoon.com/download/?ref="+doc.getID()+"&type=wapp";

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "شارك طبيبك هون مع :"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "وتس اب غير موجود على جهازك", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            return false;
        }
    }

}