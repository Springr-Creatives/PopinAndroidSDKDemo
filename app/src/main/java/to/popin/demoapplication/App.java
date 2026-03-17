package to.popin.demoapplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import to.popin.androidsdk.Popin;
import to.popin.androidsdk.PopinConfig;
import to.popin.androidsdk.listeners.PopinInitListener;
import to.popin.androidsdk.models.Product;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (!isLoggedIn(this)) {
            return;
        }

        initPopin(this);
    }

    public static void initPopin(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
        String userName = prefs.getString(LoginActivity.KEY_USER_NAME, null);
        String contactInfo = prefs.getString(LoginActivity.KEY_CONTACT_INFO, null);
        String identifier = prefs.getString(LoginActivity.KEY_IDENTIFIER, null);

        PopinConfig.Builder builder = new PopinConfig.Builder()
                .userName(userName)
                .contactInfo(contactInfo);
        if (identifier != null) {
            builder.identifier(identifier);
        }

        PopinConfig config = builder
                .sandboxMode(false)
                .debugMode(true)
                .hideDisconnectButton(false)
                .hideScreenShareButton(true)
                .hideFlipCameraButton(true)
                .hideMuteVideoButton(false)
                .hideMuteAudioButton(false)
                .hideBackButton(false)
                .callerId("sdk_caller_id")
                .persistenceMode(true)
                .enableIncomingCalls(true)
                .expertDesignation("Car expert")
                .secondaryProductText("Car details")
                .initListener(new PopinInitListener() {
                    @Override
                    public void onInitComplete(int userId) {
                        Log.d("POPIN", "SDK initialized, userId: " + userId);
                    }

                    @Override
                    public void onInitFailed(String reason) {
                        Log.e("POPIN", "Init failed: " + reason);
                    }
                })
                .build();
        Popin.init(context, config);
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
        String userName = prefs.getString(LoginActivity.KEY_USER_NAME, null);
        String contactInfo = prefs.getString(LoginActivity.KEY_CONTACT_INFO, null);
        return userName != null && contactInfo != null;
    }
}
