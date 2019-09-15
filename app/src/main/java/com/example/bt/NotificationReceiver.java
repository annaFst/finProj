package com.example.bt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.bt.activities.EventsActivity;
import com.example.bt.app.CurrentUserAccount;
import com.example.bt.models.Event;
import com.example.bt.models.Item;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onReceive(Context context, Intent intent) {

        int alarmId = intent.getIntExtra("index", 0);
        String title = intent.getStringExtra("title");

       /*
        String eventId = intent.getExtras().getString("eventId");
        Event currEvent = CurrentUserAccount.getInstance().GetEventIfPresent(eventId);

        if (currEvent.getIsRepeat()){
           String type = currEvent.getRepeatType();
           for (Item item : currEvent.getItems()){
               item.setTaken(false);
           }
        }
        else{
            currEvent.setActive(false);

        }
        CurrentUserAccount.getInstance().GetEventRepository().update(currEvent);
*/
        NotificationManager myNotifi = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "channel2", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("This is channel 2");
            myNotifi.createNotificationChannel(channel2);
        }

        Log.d("In Notification:  ", "onReceive: ");
        Intent goToMain = new Intent(context, EventsActivity.class);
        goToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntend = PendingIntent.getActivity(context, alarmId, goToMain, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_2_ID);
        notification.setContentIntent(contentIntend);
        notification.setSmallIcon(android.R.drawable.sym_def_app_icon);
        notification.setContentTitle(title);
        notification.setContentText(title);
        notification.setAutoCancel(true);
        notification.setWhen(System.currentTimeMillis());
        notification.setPriority(Notification.PRIORITY_MAX);
        notification.setCategory(Notification.CATEGORY_EVENT);

        myNotifi.notify(alarmId, notification.build());
    }
}

