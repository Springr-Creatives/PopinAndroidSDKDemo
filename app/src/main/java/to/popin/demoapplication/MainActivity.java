package to.popin.demoapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import to.popin.androidsdk.Popin;
import to.popin.androidsdk.PopinConfig;
import to.popin.androidsdk.listeners.PopinEventsListener;


public class MainActivity extends AppCompatActivity {

    private TextView tvEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!App.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    101
            );
        }

        SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);

        Button buttonCall = findViewById(R.id.buttonCall);
        buttonCall.setOnClickListener(view -> {
            PopinConfig newConfig = Popin.getInstance().getConfig();
            Map<String, String> meta2 = new HashMap<>();
            meta2.put("item", "set_before_call");
            newConfig.setCallerId("new_caller_id");
            newConfig.setMeta(meta2);
            showUrlDialog();
        });

        // Logout button — deinit SDK, clear SharedPrefs, go to LoginActivity
        findViewById(R.id.buttonReset).setOnClickListener(view -> {
            Popin.deinit();
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        findViewById(R.id.btn_test_crash).setOnClickListener(view -> {
            throw new RuntimeException("Test Crash"); // Force a crash
        });

        tvEvents = findViewById(R.id.tvEvents);

        findViewById(R.id.btnClearLogs).setOnClickListener(v -> tvEvents.setText(""));

    }

    private void logEvent(String tag, String message) {
        runOnUiThread(() -> {
            String time = new java.text.SimpleDateFormat("HH:mm:ss",
                    java.util.Locale.getDefault()).format(new java.util.Date());
            tvEvents.append("[" + time + "] " + message + "\n");
        });
    }

    private void showUrlDialog() {
        EditText input = new EditText(this);
        input.setHint("Enter URL");
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        LinearLayout container = new LinearLayout(this);
        container.setPadding(50, 30, 50, 0);
        container.addView(input);

        new AlertDialog.Builder(this)
                .setTitle("Enter URL")
                .setMessage("Enter a URL or skip to start without one")
                .setView(container)
                .setPositiveButton("OK", (dialog, which) -> {
                    String url = input.getText().toString().trim();
                    if (url.isEmpty()) {
                        startCall();
                    } else {
                        startCall(url);
                    }
                })
                .setNegativeButton("Skip", (dialog, which) -> startCall())
                .show();
    }

    private void startCall(String url) {
        Popin.getInstance().startCall(url, getEventsListener());
    }

    private void startCall() {
        Popin.getInstance().startCall(getEventsListener());
    }

    private PopinEventsListener getEventsListener() {
        return new PopinEventsListener() {
            @Override
            public void onPermissionGiven() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3p: PERMISSION GIVEN", Toast.LENGTH_SHORT).show());
                logEvent("EVENT", "Permission given");
            }

            @Override
            public void onPermissionDenied() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3p: PERMISSION DENIED", Toast.LENGTH_SHORT).show());
                logEvent("EVENT", "Permission denied");
            }

            @Override
            public void onCallStart(int callType) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: CALL_START | type=" + callType, Toast.LENGTH_SHORT).show());
                logEvent("EVENT", "Call started | type=" + callType);
            }

            @Override
            public void onCallCancel() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3p: CALL_CANCEL", Toast.LENGTH_SHORT).show());
                logEvent("EVENT", "Call abandoned");
            }

            @Override
            public void onQueuePositionChanged(int position) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: QUEUE POSITION >" + position, Toast.LENGTH_SHORT).show());
                logEvent("EVENT", "Queue position: " + position);
            }

            @Override
            public void onCallMissed() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: CALL_MISSED", Toast.LENGTH_SHORT).show());
                logEvent("EVENT", "Call missed");
            }

            @Override
            public void onCallConnected() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: CALL_CONNECTED", Toast.LENGTH_SHORT).show());
                logEvent("EVENT", "Call connected");
            }

            @Override
            public void onCallFailed() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: CALL_FAILED", Toast.LENGTH_SHORT).show());
                logEvent("EVENT", "Call failed");
            }

            @Override
            public void onCallEnd() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "3P: CALL_END", Toast.LENGTH_SHORT).show());
                logEvent("EVENT", "Call ended");
            }

            @Override
            public void onCallNetworkFailure(String participant) {
                logEvent("EVENT", "onCallNetworkFailure | participant=" + participant);
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        "NETWORK FAILURE: " + participant, Toast.LENGTH_SHORT).show());
            }
        };
    }
}
