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
import to.popin.androidsdk.agent.PopinAgent;
import to.popin.androidsdk.listeners.PopinCallAcceptedListener;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Try customer SDK first
        if (Popin.onFcmMessageReceived(remoteMessage.getData(), i -> Log.e("CALL","ACCEPTED_LISTENER_FCM"))) {
            return;
        }
        // Try agent SDK
        PopinAgent.onFcmMessageReceived(remoteMessage.getData());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Popin.setFcmToken(getApplicationContext(), token);
        PopinAgent.setFcmToken(getApplicationContext(), token);
        Log.d("FCM_TOKEN", token);
    }
}
