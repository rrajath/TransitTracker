package com.rrajath.transittracker.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.rrajath.transittracker.R;
import com.rrajath.transittracker.ui.activity.PermissionsActivity;

public class PermissionUtils {
    public static void showPermissionsNotification(Context context) {
        Intent intent = new Intent(context, PermissionsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Transit Tracker Permissions Request")
                .setContentText("Requesting location permissions")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        int notificationId = 1;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
