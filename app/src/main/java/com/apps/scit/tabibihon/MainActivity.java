package com.apps.scit.tabibihon;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import entities.base;
import entities.doctor;
import entities.user;
import extra.animateTouch;

public class MainActivity extends AppCompatActivity {
    public static BroadcastReceiver mRegistrationBroadcastReceiver;
    boolean type;
  private  static   doctor doc=null;
    private  static   user us=null;
    private  boolean exit=false;





    void Process(){
        if(doc!=null) {
            doctorFragment FRAG = new doctorFragment();
            FRAG.root = this;
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fadin, R.anim.fadout, R.anim.fadin, R.anim.fadout)
                    .replace(R.id.container, FRAG)
                    .commit();
        }
        else
        if(us!=null)
        {
            userFragment FRAG = new userFragment();
            FRAG.root = this;
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fadin, R.anim.fadout, R.anim.fadin, R.anim.fadout)
                    .replace(R.id.container, FRAG)
                    .commit();
        }
        else
        {
            visitorFragment FRAG = new visitorFragment();
            FRAG.root = this;
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fadin, R.anim.fadout, R.anim.fadin, R.anim.fadout)
                    .replace(R.id.container, FRAG)
                    .commit();
        }

    }

   static FloatingActionButton home,notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        home=((FloatingActionButton)findViewById(R.id.home_fab));
        notif=((FloatingActionButton)findViewById(R.id.fab_notific));

        if(getIntent().hasExtra("doctor"))
        {

            doc=(doctor)getIntent().getSerializableExtra("doctor");


            GCMRegistrationIntentService.role="doctor";
            GCMRegistrationIntentService.profile=0;
        }

        else
        if(getIntent().hasExtra("user")){
            us=(user)getIntent().getSerializableExtra("user");


            GCMRegistrationIntentService.role="user";
            GCMRegistrationIntentService.profile=0;
        }
        else
        {
            GCMRegistrationIntentService.role="visitor";
            GCMRegistrationIntentService.profile=-1;
        }
        GCMPushReceiverService.profile=false;


        if(doc!=null||us!=null) {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //Check type of intent filter

                    if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                        //Registration success
                        String token = intent.getStringExtra("token");
                        // Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();

                    } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                        //Registration error
                        Toast.makeText(getApplicationContext(), R.string.gcm_fail, Toast.LENGTH_LONG).show();
                    } else {
                        //Tobe define
                    }
                }
            };

            //Check status of Google play service in device
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if (ConnectionResult.SUCCESS != resultCode) {
                //Check type of error
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    // Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                    //So notification
                    GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
                } else {
                    //Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
                }
            } else {
                //Start service
                Intent itent = new Intent(this, GCMRegistrationIntentService.class);
                startService(itent);
            }
        }


        



        
        if (savedInstanceState == null) {

            Process();
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(doc!=null||us!=null) {
            //  Log.w("MainActivity", "onResume");
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            exit = true;
            Toast.makeText(this, getString(R.string.confirm_exit), Toast.LENGTH_SHORT).show();
            findViewById(R.id.container).postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);
        }
    }








    public static class doctorFragment extends Fragment {
        MainActivity root;




        public MainActivity getRoot() {
            return root;
        }

        public void setRoot(MainActivity root) {
            this.root = root;
        }

        public doctorFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_doctor_main
                  , container, false);




            ((ImageView)rootView.findViewById(R.id.website)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.tabebakhoon.com"));
                    startActivity(browserIntent);
                }
            });

            ((ImageView)rootView.findViewById(R.id.facebook)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/tabebakhoon"));
                    startActivity(browserIntent);
                }
            });
            ((ImageView)rootView.findViewById(R.id.scit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.scitco.sy"));
                    startActivity(browserIntent);
                }
            });

            ((ImageView)rootView.findViewById(R.id.nacabab)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.med-homs.com"));
                    startActivity(browserIntent);
                }
            });
            ((ImageView)rootView.findViewById(R.id.nacababph)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.sph-tartous.com"));
                    startActivity(browserIntent);
                }
            });

            if(doc.getType().equals("0")) {
                ((TextView) rootView.findViewById(R.id.welcom)).setText(
                        "أهلا وسهلاً بك دكتور/ة " + doc.getFname() + " "
                                + "سيجعل  التطبيق عملية التواصل معك من قبل المرضى  اكثر سهولة اضافة الى امكانية ايجاد موقع العيادة والبحث عنها  و العديد من الميزات الاخرى "
                );

                ((ImageView) rootView.findViewById(R.id.profile_but)).setBackgroundResource(R.drawable.profile);
            }
            else{
                ((TextView) rootView.findViewById(R.id.welcom)).setText(
                        "أهلا وسهلاً بك " + doc.getFname() + " "
                                + "سيجعل التطبيق عملية الوصول الى صيدليتك اكثر سهولة اضافة الى امكانية ايجاد موقع الصيدلية والبحث عنها على الخريطة و العديد من الميزات الاخرى"
                );
                ((ImageView) rootView.findViewById(R.id.profile_but)).setBackgroundResource(R.drawable.ph_profile);
            }

            ((LinearLayout) rootView.findViewById(R.id.l3)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), my_profile.class);
                            ii.putExtra("doctor", doc);
                            ii.putExtra("first","true");
                            startActivity(ii);
                        }
                    }
            );


            ((LinearLayout)rootView.findViewById(R.id.l2)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), search.class);
                            ii.putExtra("doctor", doc);
                            startActivity(ii);
                        }
                    }
            );
            ((LinearLayout)rootView.findViewById(R.id.l4)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent ii = new Intent(getContext(), Hospitals.class);
                            startActivity(ii);

                        }
                    }
            );
            ((LinearLayout)rootView.findViewById(R.id.l1)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent ii = new Intent(getContext(), chemistary.class);
                            startActivity(ii);

                        }
                    }
            );

            ((LinearLayout) rootView.findViewById(R.id.l6)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), AdviceActivity.class);
                            ii.putExtra("user",doc);
                            startActivity(ii);
                        }
                    }
            );


            ((Button)rootView.findViewById(R.id.vote)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), Voting.class);
                            ii.putExtra("doctor", doc);
                            startActivity(ii);
                        }
                    }
            );


            View vv = rootView.findViewById(R.id.search_but);
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator alpha = ObjectAnimator.ofFloat(vv, View.ALPHA, 0f,1f);
            ObjectAnimator scale = ObjectAnimator.ofFloat(vv, View.SCALE_X, 0f,1f);
            ObjectAnimator scaley = ObjectAnimator.ofFloat(vv, View.SCALE_Y, 0f, 1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();

            View vv2 = rootView.findViewById(R.id.profile_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv2, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv2, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv2, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();

            View vv4 = rootView.findViewById(R.id.chim_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv4, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv4, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv4, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();
             View vv3 = rootView.findViewById(R.id.hos_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv3, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv3, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv3, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();

            View vv6 = rootView.findViewById(R.id.chim_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv6, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv6, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv6, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();






            vv4.setOnTouchListener(new animateTouch());
            vv3.setOnTouchListener(new animateTouch());
            vv2.setOnTouchListener(new animateTouch());
            vv.setOnTouchListener(new animateTouch());
            vv6.setOnTouchListener(new animateTouch());

            home.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
            //notif.setVisibility(View.GONE);
            notif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it=new Intent(getContext(),doctorAdminNotification.class);
                    it.putExtra("id",doc.getID());
                    startActivity(it);
                }
            });
            ((Button)rootView.findViewById(R.id.invite)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final CharSequence[] items = {"عبر رسالة نصية", "عبر واتس اب",
                                    "إلغاء"};
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                            builder.setTitle("دعوة صديق");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    boolean result = Utility.checkPermission(getContext());
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

            return rootView;
        }

        public void sendSimpleMsg(){
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body","استفد من جميع خدمات تطبيق طبيبك هون . يمكنك الان تحميله من هنا : "+ ":http://tabebakhoon.com/download/?ref="+doc.getID()+"&type=sms");
            sendIntent.setType("vnd.android-dir/mms-sms");
            startActivity(sendIntent);

        }
        public void sendWhatsapppMsg(){
            PackageManager pm=getContext().getPackageManager();
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
                Toast.makeText(getContext(), "وتس اب غير موجود على جهازك", Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }

    public static class userFragment extends Fragment {
        MainActivity root;
        boolean first=true;



        public MainActivity getRoot() {
            return root;
        }

        public void setRoot(MainActivity root) {
            this.root = root;
        }

        public userFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate( R.layout.fragment_user_main, container, false);


            ((ImageView)rootView.findViewById(R.id.website)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.tabebakhoon.com"));
                    startActivity(browserIntent);
                }
            });

            ((ImageView)rootView.findViewById(R.id.facebook)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/tabebakhoon"));
                    startActivity(browserIntent);
                }
            });

            ((ImageView)rootView.findViewById(R.id.scit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.scitco.sy"));
                    startActivity(browserIntent);
                }
            });

            ((ImageView)rootView.findViewById(R.id.nacabab)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.med-homs.com"));
                    startActivity(browserIntent);
                }
            });
            ((ImageView)rootView.findViewById(R.id.nacababph)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.sph-tartous.com"));
                    startActivity(browserIntent);
                }
            });


                    ((LinearLayout) rootView.findViewById(R.id.l2)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), search.class);
                            ii.putExtra("user", us);
                            startActivity(ii);
                        }
                    }
            );

            ((Button)rootView.findViewById(R.id.vote)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), Voting.class);
                            ii.putExtra("user", us);
                            startActivity(ii);
                        }
                    }
            );

            ((LinearLayout)rootView.findViewById(R.id.l3)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), userDoctorList.class);
                            ii.putExtra("user", us);
                            startActivity(ii);
                        }
                    }
            );

            ((LinearLayout)rootView.findViewById(R.id.l1)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), userAdvices.class);
                            ii.putExtra("user", us);
                            startActivity(ii);
                        }
                    }
            );
            ((LinearLayout)rootView.findViewById(R.id.l4)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), chemistary.class);
                            startActivity(ii);
                        }
                    }
            );
            ((LinearLayout)rootView.findViewById(R.id.l5)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), Hospitals.class);
                            startActivity(ii);
                        }
                    }
            );

            ((LinearLayout) rootView.findViewById(R.id.l6)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), AdviceActivity.class);
                            ii.putExtra("user",us);
                            startActivity(ii);
                        }
                    }
            );

            ((Button)rootView.findViewById(R.id.invite)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final CharSequence[] items = {"عبر رسالة نصية", "عبر واتس اب",
                                    "إلغاء" };
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                            builder.setTitle("دعوة صديق");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    boolean result = Utility.checkPermission(getContext());
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


            View vv = rootView.findViewById(R.id.search_but);
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator alpha = ObjectAnimator.ofFloat(vv, View.ALPHA, 0f,1f);
            ObjectAnimator scale = ObjectAnimator.ofFloat(vv, View.SCALE_X, 0f,1f);
            ObjectAnimator scaley = ObjectAnimator.ofFloat(vv, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();

            View vv2 = rootView.findViewById(R.id.dlist_but);
             set = new AnimatorSet();
             alpha = ObjectAnimator.ofFloat(vv2, View.ALPHA, 0f,1f);
             scale = ObjectAnimator.ofFloat(vv2, View.SCALE_X, 0f,1f);
             scaley = ObjectAnimator.ofFloat(vv2, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();

            View vv3 = rootView.findViewById(R.id.adv_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv3, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv3, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv3, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();


            View vv4 = rootView.findViewById(R.id.chim_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv4, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv4, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv4, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();

            View vv5 = rootView.findViewById(R.id.hos_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv5, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv5, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv5, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();

            View vv6 = rootView.findViewById(R.id.chim_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv6, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv6, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv6, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();




            vv5.setOnTouchListener(new animateTouch());
            vv4.setOnTouchListener(new animateTouch());
            vv3.setOnTouchListener(new animateTouch());
            vv2.setOnTouchListener(new animateTouch());
            vv.setOnTouchListener(new animateTouch());
            vv6.setOnTouchListener(new animateTouch());




            home.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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



           notif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ii = new Intent(getContext(), notificationResults.class);
                    ii.putExtra("user", us);
                    startActivity(ii);

                }
            });
            checkNotification();
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            if(!first)
            checkNotification();
            else
                first=false;
        }


        public void sendSimpleMsg(){
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body","استفد من جميع خدمات تطبيق طبيبك هون . يمكنك الان تحميله من هنا : "+ ":http://tabebakhoon.com/download/?ref="+us.getID()+"&type=sms");
            sendIntent.setType("vnd.android-dir/mms-sms");
            startActivity(sendIntent);

        }
        public void sendWhatsapppMsg(){
            PackageManager pm=getContext().getPackageManager();
            try {

                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text ="استفد من جميع خدمات تطبيق طبيبك هون . يمكنك الان تحميله من هنا : "+ ":http://tabebakhoon.com/download/?ref="+us.getID()+"&type=wapp";

                PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                //Check if package exists or not. If not then code
                //in catch block will be called
                waIntent.setPackage("com.whatsapp");

                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(waIntent, "شارك طبيبك هون مع :"));

            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(getContext(), "وتس اب غير موجود على جهازك", Toast.LENGTH_SHORT)
                        .show();
            }

        }



        public void checkNotification(){

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams req=new RequestParams();
            req.put("user_ID", us.getID()+"");
            req.put("token",us.getAccessToken());

            client.post(base.BASE_URL + "getUnreadNotification", req, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {


                    try {
                        JSONObject obj = new JSONObject(response);
                        if(obj.has("count"))
                        {
                            if(obj.getInt("count")>0)
                           //     notif.setBackgroundColor(Color.rgb(255,0,0));
                            notif.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.myoldred)));
                            else
                                notif.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.myblue)));

                        }



                    } catch (Exception e) {
                       // Toast.makeText(getContext(), R.string.fail, Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    try {
                        JSONObject obj = new JSONObject(content);
                        if(obj.has("message"))
                        Toast.makeText(getContext(),obj.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    }

                }
            });





        }




    }


    public static class visitorFragment extends Fragment {
        MainActivity root;
        boolean first=true;
        private static final int REQUEST_NOTI=1 ,REQUEST_SEARCH=2,REQUEST_DOC_FAV=3 ,REQUEST_ADV_FAV=4,REQUEST_VOTE=5 ;


        public MainActivity getRoot() {
            return root;
        }

        public void setRoot(MainActivity root) {
            this.root = root;
        }

        public visitorFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate( R.layout.fragment_visitor_main, container, false);


            notif.setVisibility(View.GONE);

            ((ImageView)rootView.findViewById(R.id.website)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.tabebakhoon.com"));
                    startActivity(browserIntent);
                }
            });

            ((ImageView)rootView.findViewById(R.id.facebook)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/tabebakhoon"));
                    startActivity(browserIntent);
                }
            });

            ((ImageView)rootView.findViewById(R.id.scit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.scitco.sy"));
                    startActivity(browserIntent);
                }
            });

            ((ImageView)rootView.findViewById(R.id.nacabab)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.med-homs.com"));
                    startActivity(browserIntent);
                }
            });

            ((ImageView)rootView.findViewById(R.id.nacababph)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.sph-tartous.com"));
                    startActivity(browserIntent);
                }
            });

            ((LinearLayout) rootView.findViewById(R.id.l2)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), search.class);
                            ii.putExtra("user", us);
                            startActivity(ii);
                        }
                    }
            );

            ((LinearLayout) rootView.findViewById(R.id.l6)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), AdviceActivity.class);
                            startActivity(ii);
                        }
                    }
            );



            ((Button)rootView.findViewById(R.id.invite)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final CharSequence[] items = {"عبر رسالة نصية", "عبر واتس اب",
                                    "إلغاء" };
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                            builder.setTitle("دعوة صديق");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    boolean result = Utility.checkPermission(getContext());
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




            ((LinearLayout)rootView.findViewById(R.id.l4)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), chemistary.class);
                            startActivity(ii);
                        }
                    }
            );
            ((LinearLayout)rootView.findViewById(R.id.l3)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), Hospitals.class);
                            startActivity(ii);
                        }
                    }
            );


            ((LinearLayout)rootView.findViewById(R.id.l5)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ii = new Intent(getContext(), userType.class);
                            startActivity(ii);
                            ((Activity)getContext()).finish();
                        }
                    }
            );

            View vv = rootView.findViewById(R.id.search_but);
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator alpha = ObjectAnimator.ofFloat(vv, View.ALPHA, 0f,1f);
            ObjectAnimator scale = ObjectAnimator.ofFloat(vv, View.SCALE_X, 0f,1f);
            ObjectAnimator scaley = ObjectAnimator.ofFloat(vv, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();

            View vv2 = rootView.findViewById(R.id.reg_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv2, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv2, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv2, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();


            View vv3 = rootView.findViewById(R.id.hos_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv3, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv3, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv3, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();


            View vv4 = rootView.findViewById(R.id.chim_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv4, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv4, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv4, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();
            View vv6 = rootView.findViewById(R.id.chim_but);
            set = new AnimatorSet();
            alpha = ObjectAnimator.ofFloat(vv6, View.ALPHA, 0f,1f);
            scale = ObjectAnimator.ofFloat(vv6, View.SCALE_X, 0f,1f);
            scaley = ObjectAnimator.ofFloat(vv6, View.SCALE_Y, 0f,1f);
            set.play(alpha).with(scale).with(scaley);
            set.setDuration(1500);
            set.start();





            vv.setOnTouchListener(new animateTouch());
            vv2.setOnTouchListener(new animateTouch());
            vv3.setOnTouchListener(new animateTouch());
            vv4.setOnTouchListener(new animateTouch());
            vv6.setOnTouchListener(new animateTouch());



            home.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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





            return rootView;
        }


        public void sendSimpleMsg(){
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body","استفد من جميع خدمات تطبيق طبيبك هون . يمكنك الان تحميله من هنا : "+ ":http://tabebakhoon.com/download/?ref=visitor&type=sms");
            sendIntent.setType("vnd.android-dir/mms-sms");
            startActivity(sendIntent);

        }
        public void sendWhatsapppMsg(){
            PackageManager pm=getContext().getPackageManager();
            try {

                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text ="استفد من جميع خدمات تطبيق طبيبك هون . يمكنك الان تحميله من هنا : "+ ":http://tabebakhoon.com/download/?ref=visitor&type=wapp";

                PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                //Check if package exists or not. If not then code
                //in catch block will be called
                waIntent.setPackage("com.whatsapp");

                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(waIntent, "شارك طبيبك هون مع :"));

            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(getContext(), "وتس اب غير موجود على جهازك", Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }

    public static void updateToken(String token){

        RequestParams req= new RequestParams();
        if(us!=null){
            req.put("ID",us.getID()+"");
            req.put("table","user");
            req.put("token",us.getAccessToken());

        }
        else if(doc!=null){

            req.put("ID",doc.getID()+"");
            req.put("table","doctor");
            req.put("token",doc.getAccessToken());

        }
        else return;
        req.put("mytoken",token);

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

}
