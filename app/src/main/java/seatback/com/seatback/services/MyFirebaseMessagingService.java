package seatback.com.seatback.services;

import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.FirebaseMessagingService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import seatback.com.seatback.R;

/**
 * Created by naji on 9/18/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a notification payload.

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject jObject = new JSONObject(remoteMessage.getData());

                Intent broadcast = new Intent();
                broadcast.putExtra("title", jObject.getString("title"));
                broadcast.putExtra("body", jObject.getString("body"));
                broadcast.putExtra("posture", jObject.getString("posture"));
                broadcast.setAction("OPEN_NEW_ACTIVITY");
                sendBroadcast(broadcast);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Message Notification Data: " + remoteMessage.getData());
        }

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}