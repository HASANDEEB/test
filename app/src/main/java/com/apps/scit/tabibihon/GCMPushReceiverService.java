package com.apps.scit.tabibihon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GcmListenerService;


public class GCMPushReceiverService extends GcmListenerService {

    public static boolean profile=false;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        sendNotification(message);
    }
    private void sendNotification(String message) {


        final int MY_NOTIFICATION_ID=1;
        NotificationManager notificationManager;
        Notification myNotification;


        Context context=this;
        Intent myIntent = new Intent(getApplicationContext(), adminNotification.class);
        myIntent.putExtra("notif",message);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                1,
                myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        myNotification = new NotificationCompat.Builder(context)
                .setContentTitle("اشعار الادارة")
                .setContentText(message)
                .setTicker("اشعار جديد")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo)
                .setVibrate(new long[]{500,500,500})
                .build();


        notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, myNotification);

    }
}
