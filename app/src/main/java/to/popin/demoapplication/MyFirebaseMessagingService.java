package to.popin.demoapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import to.popin.androidsdk.Popin;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (Popin.onFcmMessageReceived(remoteMessage.getData())) {
            return;
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Popin.setFcmToken(getApplicationContext(), token);
        Log.d("FCM_TOKEN", token);
    }


}
