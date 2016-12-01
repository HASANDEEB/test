package com.apps.scit.tabibihon;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.apps.scit.tabibihon.MainActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by AL_deeb on 07/14/2016.
 */
public class GCMRegistrationIntentService extends IntentService {
    public static int profile=-1;
    public static String role="";
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    public static final String TAG = "GCMTOKEN";
    public static final String ROLE = "GCMROLE";
    public GCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    private void registerGCM() {
        SharedPreferences sharedPreferences = getSharedPreferences("GCM", Context.MODE_PRIVATE);//Define shared reference file name
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Intent registrationComplete = null;
        String token = null;
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken("125341980305", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            //Log.w("GCMRegIntentService", "token:" + token);

            //notify to UI that registration complete success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);

            String oldToken = sharedPreferences.getString(TAG, "");//Return "" when error or key not exists
            String oldRole = sharedPreferences.getString(ROLE, "");//Return "" when error or key not exists
            //Only request to save token when token is new
            if(!"".equals(token) && (!oldToken.equals(token)||!role.equals(oldRole))) {
                if(profile==0)
                MainActivity.updateToken(token);
                else
                if(profile==1)
                    my_profile.updateToken(token);
                //Save new token to shared reference
                editor.putString(TAG, token);
                editor.putString(ROLE, role);
                editor.commit();
            } else {
                Log.w("GCMRegistrationService", "Old token");
            }


        } catch (Exception e) {
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        //Send broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
