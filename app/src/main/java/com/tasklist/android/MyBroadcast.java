package com.tasklist.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class MyBroadcast extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String task= " ";

        task = intent.getExtras().getString("taskName");

        String[] randomTaskNotification = new String[5];
        randomTaskNotification[0] = "Hey! Have you completed "+task+"?";
        randomTaskNotification[1] = "Just a Reminder for Your "+task;
        randomTaskNotification[2] = "Did You forget about "+task+" ?";
        randomTaskNotification[3] = "Something You Missed to-do "+task;
        randomTaskNotification[4] = "Pending Task here "+task+" ⏰";

        Random random = new Random();
        int randomIndex = random.nextInt(randomTaskNotification.length);

        NotificationManagerCompat notificationManagerCompat;
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("toDo","myNotification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"toDo")
                .setContentTitle("Task Reminder")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setColor(Color.parseColor("#8E2DE2"))
                .setContentText(randomTaskNotification[randomIndex]);

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,notification);

        Toast.makeText(context, randomTaskNotification[randomIndex], Toast.LENGTH_SHORT).show();
    }
}
