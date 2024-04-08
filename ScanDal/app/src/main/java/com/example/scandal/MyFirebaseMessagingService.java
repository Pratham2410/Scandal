package com.example.scandal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * This service handles Firebase Cloud Messaging (FCM) messages.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * Called when a message is received from Firebase Cloud Messaging (FCM).
     *
     * @param remoteMessage The remote message received from FCM.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendNotification(remoteMessage.getFrom(), remoteMessage.getNotification().getBody());
        sendNotification(remoteMessage.getNotification().getBody());
    }

    /**
     * Sends a notification with the specified title and body.
     *
     * @param from The sender of the message.
     * @param body The body of the message.
     */
    private void sendNotification(String from, String body) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyFirebaseMessagingService.this.getApplicationContext(), body, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sends a notification with the specified message body.
     *
     * @param messageBody The body of the message.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "My channel ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_notification)
                        .setContentTitle("My new notification")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
