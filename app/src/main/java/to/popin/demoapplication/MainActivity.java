package to.popin.demoapplication;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import to.popin.androidsdk.Popin;
import to.popin.androidsdk.PopinConfig;
import to.popin.androidsdk.listeners.PopinEventsListener;
import to.popin.androidsdk.listeners.PopinInitListener;
import to.popin.androidsdk.models.Product;


public class MainActivity extends AppCompatActivity {

    private TextView tvEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    101
            );
        }

        Map<String, String> meta = new HashMap<>();
        meta.put("businessUnit", "BUY");
        meta.put("journey", "VIDEO_TEST_DRIVE");
        meta.put("tenantId", "INDIA_VIDEO_PLATFORM");
        meta.put("orderId", "DA7OYG");
        meta.put("vehicleId", "19165419981");

        Product product = new Product("19165419981", "2014 Maruti Alto K10", "https://media.cars24.com/hello-ar/dev/uploads/no_bg/2a569f64-091d-11ef-bd33-02ede2007fbe/66348e7ea700c2a08dae1c10/1a471506-1017-4aef-b7f8-a72d04d585db/slot/11007286706-a3642ec2b7b841e69a47e257ea8a5324-Exterior-6.png", "https://stage-catalog-india-website.qac24svc.dev/buy-used-maruti-alto-k10-2014-cars-gurgaon-19165419981", "HR26**2266", "Petrol | Manual | 40562km | u20b97.5 lakh");

        PopinConfig config = new PopinConfig.Builder()
                .userName("user_demo_app")
                .contactInfo("9876543217")
                .sandboxMode(true)
                .debugMode(true)
                .hideDisconnectButton(false)
                .hideScreenShareButton(true)
                .hideFlipCameraButton(true)
                .hideMuteVideoButton(false)
                .hideMuteAudioButton(false)
                .hideBackButton(false)
                .callerId("sdk_call_id")
                .product(product)
                .meta(meta)
                .persistenceMode(false)
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
                        Log.e("INIT_FAILED", ">" + reason);
                    }
                })
                .build();
        Popin.init(this,config);
        Button buttonCall = findViewById(R.id.buttonCall);
        buttonCall.setOnClickListener(view -> {
            PopinConfig newConfig = Popin.getInstance().getConfig();
            Map<String, String> meta2 = new HashMap<>();
            meta.put("item", "set_before_call");
            newConfig.setCallerId("new_caller_id");
            newConfig.setMeta(meta2);
            startCall();
        });

        findViewById(R.id.btn_test_crash).setOnClickListener(view -> {
            throw new RuntimeException("Test Crash"); // Force a crash
        });

        tvEvents = findViewById(R.id.tvEvents);

        findViewById(R.id.btnClearLogs).setOnClickListener(v -> tvEvents.setText(""));

    }

    private void logEvent(String message) {
        runOnUiThread(() -> {
            String time = new java.text.SimpleDateFormat("HH:mm:ss",
                    java.util.Locale.getDefault()).format(new java.util.Date());
            tvEvents.append("[" + time + "] " + message + "\n");
        });
    }

    private void startCall() {
        Popin.getInstance().startCall(new PopinEventsListener() {
            @Override
            public void onPermissionGiven() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3p: PERMISSION GIVEN", Toast.LENGTH_SHORT).show());
                logEvent("Permission given");
            }

            @Override
            public void onPermissionDenied() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3p: PERMISSION DENIED", Toast.LENGTH_SHORT).show());
                logEvent("Permission denied");
            }

            @Override
            public void onCallStart() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: CALL_START", Toast.LENGTH_SHORT).show());
                logEvent("Call started");
            }

            @Override
            public void onCallCancel() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3p: CALL_CANCEL", Toast.LENGTH_SHORT).show());
                logEvent("Call abandoned");
            }

            @Override
            public void onQueuePositionChanged(int position) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: QUEUE POSITION >" + position, Toast.LENGTH_SHORT).show());
                logEvent("Queue position: " + position);
            }

            @Override
            public void onCallMissed() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: CALL_MISSED", Toast.LENGTH_SHORT).show());
                logEvent("Call missed");
            }

            @Override
            public void onCallNetworkFailure() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: CALL_NETWORK_FAILURE", Toast.LENGTH_SHORT).show());
                logEvent("Network failure");
            }

            @Override
            public void onCallConnected() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: CALL_CONNECTED", Toast.LENGTH_SHORT).show());
                logEvent("Call connected");
            }

            @Override
            public void onCallFailed() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: CALL_FAILED", Toast.LENGTH_SHORT).show());
                logEvent("Call failed");
            }

            @Override
            public void onCallEnd() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: CALL_END", Toast.LENGTH_SHORT).show());
                logEvent("Call ended");
            }
        });
    }
}
