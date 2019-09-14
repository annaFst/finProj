package com.example.bt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.bt.activities.EventsActivity;

import java.io.Serializable;

public class UpdateReceiver extends BroadcastReceiver  {


        public static final String CHANNEL_3_ID = "channel3";

        @Override
        public void onReceive(Context context, Intent intent) {
            int alarmId = intent.getIntExtra("index",0);
            String title = intent.getStringExtra("title");

            NotificationManager myNotifi = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel3 = new NotificationChannel(CHANNEL_3_ID,"channel1",NotificationManager.IMPORTANCE_HIGH);
                channel3.setDescription("This is channel 3");
                myNotifi.createNotificationChannel(channel3);
            }


            Intent goToMain  = new Intent(context, EventsActivity.class);
            goToMain.putExtra("Event",title);
            goToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent contentIntend = PendingIntent.getActivity(context,alarmId, goToMain,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(context,CHANNEL_3_ID);
            notification.setContentIntent(contentIntend);
            notification.setSmallIcon(android.R.drawable.ic_dialog_info);
            notification.setContentTitle(title);
            notification.setContentText("Your event was reset you can use it again");
            notification.setAutoCancel(true);
            notification.setWhen(System.currentTimeMillis());
            notification.setPriority(Notification.PRIORITY_MAX);
            notification.setCategory(Notification.CATEGORY_EVENT);

            myNotifi.notify(alarmId,notification.build());
    }
}
