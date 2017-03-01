package com.xudip.pocketmoney;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AllNotification {

    public static final int resetAllId = 0;



    public void getResetNotification(Context context1){
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context1);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.ic_menu_slideshow);
        notification.setTicker("RESET DATA COMPLETE.");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Wow! Congratulations");
        notification.setContentText("Successfully! Deleted all records.");
        notification.setDefaults(Notification.DEFAULT_SOUND);
        long[] vibrate = { 0, 100, 200, 300 };
        notification.setVibrate(vibrate);

        Intent intent = new Intent(context1, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context1, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) context1.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(resetAllId,notification.build());
    }

}
