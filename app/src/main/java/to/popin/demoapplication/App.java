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

         Product product = new Product("19165419981",
                 "2014 Maruti Alto K10",
                 "https://media.cars24.com/hello-ar/dev/uploads/no_bg/2a569f64-091d-11ef-bd33-02ede2007fbe/66348e7ea700c2a08dae1c10/1a471506-1017-4aef-b7f8-a72d04d585db/slot/11007286706-a3642ec2b7b841e69a47e257ea8a5324-Exterior-6.png",
                 "https://stage-catalog-india-website.qac24svc.dev/buy-used-maruti-alto-k10-2014-cars-gurgaon-19165419981",
                 "HR26**2266",
                 "Petrol | Manual | 40562km | u20b97.5 lakh");

        PopinConfig config = builder
                .sandboxMode(true)
                .debugMode(true)
                .hideDisconnectButton(false)
                .hideScreenShareButton(true)
                .hideFlipCameraButton(true)
                .hideMuteVideoButton(false)
                .hideMuteAudioButton(false)
                .hideBackButton(false)
                .product(product)
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
