package com.example.alanrgan.illinihub;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationManager {
  private static String DEFAULT_CHANNEL = "default";
  private Context mContext;
  private android.app.NotificationManager notificationManager;

  public NotificationManager(Context context) {
    mContext = context;
    createNotificationChannel();
  }

  public void showNotification(String title, String content, Intent intent) {
    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, DEFAULT_CHANNEL)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true);

    notificationManager.notify(0, mBuilder.build());
  }

  // From https://developer.android.com/training/notify-user/build-notification
  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = DEFAULT_CHANNEL;
      String description = "IlliniHub Channel";
      int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(DEFAULT_CHANNEL, name, importance);
      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      notificationManager = mContext.getSystemService(android.app.NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }
}
