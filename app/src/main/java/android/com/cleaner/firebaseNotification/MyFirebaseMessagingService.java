package android.com.cleaner.firebaseNotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.com.cleaner.R;
import android.com.cleaner.activities.ActivityCleanerProfile;
import android.com.cleaner.applicationUtils.App;
import android.com.cleaner.firebaseNotification.notificationModels.ProfileInformation;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Objects;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String key, value;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            sendNotificationFromHere(remoteMessage.getData().get("provider_id"), remoteMessage.getData().get("title"));
            ((App) getApplicationContext()).getRxBus().send(new ProfileInformation(remoteMessage.getData().get("provider_name"), remoteMessage.getData().get("Profile_Pic"), remoteMessage.getData().get("Bio"), remoteMessage.getData().get("StartTime"), remoteMessage.getData().get("EndTime")));
        } else if (remoteMessage.getNotification() != null) {
            sendNotificationFromHere(remoteMessage.getData().get("provider_id"),remoteMessage.getData().get("title"));
        }
    }
    private void sendNotificationFromHere(String providerId, String title){
        // Create Notification
        Intent intent = new Intent(this,ActivityCleanerProfile.class);
        intent.putExtra("provider_id", providerId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent, PendingIntent.FLAG_ONE_SHOT);
//        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
//            key = entry.getKey();
//            value = entry.getValue();
//            System.out.println("MyFirebaseMessagingService.onMessageReceived " + key + " " + value);
//        }
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title) // Message
                .setContentText("Customer") // remoteMessage.getNotification().getBody()
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).notify(m, notificationBuilder.build());
    }
}
