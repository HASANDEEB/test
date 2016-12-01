package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.PublicKey;

import entities.base;
import entities.doctor;
import entities.user;


public class splash extends Activity {

    private ProgressBar prgbar;
    boolean f=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        prgbar=(ProgressBar)findViewById(R.id.progressBar2);

        try {
            checkVersion(0);
        } catch (PackageManager.NameNotFoundException e) {
            continu();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    public boolean check(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
            return  false;
        }
        else{
            return  true;
        }

    }


    public void checkVersion(final int count) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        int verCode = pInfo.versionCode;


        prgbar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("version",verCode+"");

        client.post(base.BASE_URL + "check_version",req, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {

                try {
                    final JSONObject arg0 = new JSONObject(response);

                        if(arg0.get("statue").equals("200"))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);
                            builder.setTitle("تحديث التطبيق");  // GPS not found
                            builder.setMessage("هناك تحديث جديد متوفر للتطبيق . هل ترغب في التحديث"); // Want to enable?
                            builder.setPositiveButton("تحديث", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    f = true;
                                    try {
                                        downloadApp(arg0.getString("url"));
                                    } catch (JSONException e) {
                                        continu();
                                    }

                                }
                            });
                            builder.setNegativeButton("متابعة", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    f = true;
                                    continu();

                                }
                            });


                            builder.setOnCancelListener(
                                    new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            //When you touch outside of dialog bounds,
                                            //the dialog gets canceled and this method executes.
                                            continu();
                                        }
                                    }
                            );

                            builder.create().show();



                        }
                    else
                        {
                            continu();


                        }


                } catch (JSONException e) {
                    continu();
                }
                prgbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=2){
                    try {
                        checkVersion(count+1);
                    } catch (PackageManager.NameNotFoundException e) {
                        continu();
                    }
                }else {
                    continu();
                }

            }
        });

    }


    public void continu(){

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {

                    if(!check()) {
                        Intent ii = new Intent(splash.this, Welcome.class);
                        startActivity(ii);
                    }
                    else
                    {
                        Intent ii = new Intent(splash.this, login.class);
                        ii.putExtra("auto","auto");
                        startActivity(ii);
                    }
                    finish();


                }
            }
        };
        timer.start();


    }



    void downloadApp(String url){

        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String FileName = "Tabibakhoon.apk";
        destination += FileName;
        final Uri uri = Uri.parse("file://" + destination);

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists())
            //file.delete() - test this, I think sometimes it doesnt work
            file.delete();

        //get url of app on server


        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("تحديث جديد لتطبيق طبيبك هون");
        request.setTitle(this.getString(R.string.app_name));

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(uri, //"application/vnd.android.package-archive");
                        manager.getMimeTypeForDownloadedFile(downloadId));
                startActivity(install);

                unregisterReceiver(this);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        };
        //register receiver for when .apk download is compete
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        continu();

    }


}
