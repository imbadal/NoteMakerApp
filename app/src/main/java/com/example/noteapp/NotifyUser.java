package com.example.noteapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class NotifyUser extends BroadcastReceiver {
    private static final String TAG = "NotifyUser_";

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context, intent.getStringExtra(AddEditNoteActivity.DESC), intent.getLongExtra(AddEditNoteActivity.NOTIFICATION_ID,1));
        Log.d(TAG, "onReceive: Notified id:  "+intent.getLongExtra(AddEditNoteActivity.NOTIFICATION_ID,1));

    }

    private void showNotification(Context context, String title, long notificationid) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setContentTitle("Notes Remainder")
                .setContentText(title)
                .setSmallIcon(R.mipmap.ic_launcher_round);
        notificationManager.notify((int) notificationid, builder.build());

    }


}
