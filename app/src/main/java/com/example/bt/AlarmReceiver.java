package com.example.bt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String CHANNEL_1_ID = "channel1";

    @Override
    public void onReceive(Context context, Intent intent) {
        //int notificationId = intent.getIntExtra("notificationId",0);
        //String title = intent.getStringExtra("title");
        NotificationManager myNotifi = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID,"channel1",NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is channel 1");
            myNotifi.createNotificationChannel(channel1);
        }


        Intent goToMain  = new Intent(context,MainActivity.class);
        goToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntend = PendingIntent.getActivity(context,100, goToMain,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context,CHANNEL_1_ID);
        notification.setContentIntent(contentIntend);
        notification.setSmallIcon(android.R.drawable.ic_dialog_info);
        notification.setContentTitle("It's time!");
        notification.setContentText("EVENT");
        notification.setAutoCancel(true);
        //notification.setWhen(System.currentTimeMillis());
        notification.setPriority(Notification.PRIORITY_MAX);
        notification.setDefaults(Notification.DEFAULT_ALL);

        myNotifi.notify(100,notification.build());

    }

}
