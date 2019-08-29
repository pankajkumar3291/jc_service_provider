package android.com.cleaner.firebaseNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.com.cleaner.R;
import android.com.cleaner.activities.ActivityCleanerProfile;
import android.com.cleaner.applicationUtils.App;
import android.com.cleaner.firebaseNotification.notificationModels.NotificationPayload;
import android.com.cleaner.firebaseNotification.notificationModels.ProfileInformation;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String key, value;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){

        if (remoteMessage.getData().size() > 0){
//            sendNotificationFromHere(remoteMessage.getData().get("provider_id"), remoteMessage.getData().get("title"));
            ((App) getApplicationContext()).getRxBus().send(new ProfileInformation(remoteMessage.getData().get("provider_name"), remoteMessage.getData().get("Profile_Pic"), remoteMessage.getData().get("Bio"), remoteMessage.getData().get("StartTime"), remoteMessage.getData().get("EndTime")));
            startInForeground(remoteMessage.getData());
        } else if (remoteMessage.getNotification() != null){
            startInForeground(remoteMessage.getData());
//          sendNotificationFromHere(remoteMessage.getData().get("provider_id"),remoteMessage.getData().get("title"));
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


    private void startInForeground(Map<String, String> data){
        String title = "";
        Intent intent = new Intent(this,ActivityCleanerProfile.class);
        intent.putExtra("provider_id", data.get("provider_id"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent;
        if (data.get("payload")!=null)
        {
            Gson gson=new GsonBuilder().create();
           NotificationPayload list= gson.fromJson(data.get("payload"),NotificationPayload.class);
           title=list.getTitle();
            contentIntent= PendingIntent.getActivity(
                    getApplicationContext(),
                    0,
                    new Intent(), // add this
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        else
        {
            contentIntent= PendingIntent.getActivity(
                    getApplicationContext(),
                    0,
                    intent, // add this
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        //Intent notificationIntent = new Intent(this, WorkoutActivity.class);
        //PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AppConstants.NOTIFICATION_CHANNEL_ID_CURRENT_LOCATION)
                .setSmallIcon(R.drawable.notification_icon)
                //data.get("title")
                .setContentTitle(data.get("payload")!=null?title:data.get("title"))
                //data.get("Customer")
                .setContentText(data.get("payload")!=null?data.get("notificationType"):data.get("Customer"))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                //.setContentText("HELLO")
                //.setTicker("TICKER")
                .setContentIntent(contentIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(AppConstants.NOTIFICATION_CHANNEL_ID_CURRENT_LOCATION, AppConstants.NOTIFICATION_CHANNEL_NAME_CURRENT_LOCATION, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(AppConstants.NOTIFICATION_CHANNEL_DESC_CURRENT_LOCATION);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        mNotificationManager.notify(1, builder.build());
    }
}
