package com.example.zenstudy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String event = intent.getStringExtra("event");
        String start_time = intent.getStringExtra("startTIME");
        String end_time = intent.getStringExtra("endTIME");
        String first_date = intent.getStringExtra("firstDATE");
        String second_date = intent.getStringExtra("secondDATE");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notificationReminder")
                .setSmallIcon(R.mipmap.pandaboo_transparent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(event)
                .setContentText(start_time + " to " + end_time + ", " + first_date + " to " + second_date)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200,builder.build());
    }
}
