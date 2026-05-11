package to.popin.demoapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import to.popin.androidsdk.agent.PopinAgent;
import to.popin.androidsdk.agent.PopinAgentConfig;
import to.popin.androidsdk.agent.listeners.PopinAgentCallListener;
import to.popin.androidsdk.agent.listeners.PopinAgentEventsListener;
import to.popin.androidsdk.agent.listeners.PopinAgentInitListener;
import to.popin.androidsdk.agent.listeners.PopinAgentMessageListener;
import to.popin.androidsdk.agent.models.AgentMessage;
import to.popin.androidsdk.agent.models.MessageUser;

public class AgentMainActivity extends AppCompatActivity {

    private TextView tvEvents;
    private TextView tvAgentName;
    private TextInputEditText etUserId;
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(AgentLoginActivity.AGENT_PREFS, MODE_PRIVATE);
        String email = prefs.getString(AgentLoginActivity.KEY_EMAIL, null);
        String pin = prefs.getString(AgentLoginActivity.KEY_PIN, null);

        if (email == null || pin == null) {
            startActivity(new Intent(this, AgentLoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_agent_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    101
            );
        }

        tvEvents = findViewById(R.id.tvEvents);
        tvAgentName = findViewById(R.id.tvAgentName);
        etUserId = findViewById(R.id.etUserId);

        findViewById(R.id.btnClearLogs).setOnClickListener(v -> tvEvents.setText(""));

        findViewById(R.id.btnCallUser).setOnClickListener(v -> callUser());
        findViewById(R.id.btnMessageUsers).setOnClickListener(v -> loadMessageUsers());
        findViewById(R.id.btnSendMessage).setOnClickListener(v -> sendMessage());

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            PopinAgent.deinit();
            prefs.edit().clear().apply();
            startActivity(new Intent(this, ChooserActivity.class));
            finish();
        });

        initAgent(email, pin);
    }

    private void logEvent(String message) {
        runOnUiThread(() -> {
            String time = sdf.format(new Date());
            tvEvents.append("[" + time + "] " + message + "\n");
        });
    }

    private void initAgent(String email, String pin) {
        logEvent("Initializing agent: " + email);

        PopinAgentConfig config = new PopinAgentConfig.Builder()
                .email(email)
                .pin(pin)
                .sandboxMode(true)
                .debugMode(true)
                .initListener(new PopinAgentInitListener() {
                    @Override
                    public void onInitComplete(int agentId, String agentName) {
                        logEvent("Init success | id=" + agentId + " name=" + agentName);
                        runOnUiThread(() -> tvAgentName.setText("Agent: " + agentName + " (ID: " + agentId + ")"));
                        setupEventsListener();
                    }

                    @Override
                    public void onInitFailed(String reason) {
                        logEvent("Init failed: " + reason);
                        runOnUiThread(() ->
                                Toast.makeText(AgentMainActivity.this, "Login failed: " + reason, Toast.LENGTH_LONG).show()
                        );
                    }
                })
                .build();

        PopinAgent.init(this, config);
    }

    private void setupEventsListener() {
        PopinAgent.getInstance().setEventsListener(new PopinAgentEventsListener() {
            @Override
            public void onCallAccepted(int callId) {
                logEvent("Call accepted | callId=" + callId);
            }

            @Override
            public void onCallConnected() {
                logEvent("Call connected");
            }

            @Override
            public void onCallEnded() {
                logEvent("Call ended");
            }

            @Override
            public void onCallMissed() {
                logEvent("Call missed");
            }

            @Override
            public void onCallFailed() {
                logEvent("Call failed");
            }

            @Override
            public void onCallNetworkFailure(String participant) {
                logEvent("Network failure: " + participant);
            }

            @Override
            public void onMessageReceived(int userId, String text) {
                logEvent("Message from user " + userId + ": " + text);
            }

            @Override
            public void onPusherConnectionChanged(boolean connected) {
                logEvent("Pusher: " + (connected ? "connected" : "disconnected"));
            }
        });
    }

    private void callUser() {
        String userIdStr = etUserId.getText() != null ? etUserId.getText().toString().trim() : "";
        if (userIdStr.isEmpty()) {
            Toast.makeText(this, "Enter a User ID", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
            return;
        }

        logEvent("Calling user " + userId + "...");
        PopinAgent.getInstance().callUser(userId, new PopinAgentCallListener() {
            @Override
            public void onCallAccepted(int callId) {
                logEvent("Call accepted by user | callId=" + callId);
            }

            @Override
            public void onCallRejected() {
                logEvent("Call rejected by user");
            }

            @Override
            public void onCallFailed(String reason) {
                logEvent("Call failed: " + reason);
            }
        });
    }

    private void loadMessageUsers() {
        logEvent("Loading message users...");
        PopinAgent.getInstance().getMessageUsers(new PopinAgentMessageListener() {
            @Override
            public void onMessageUsersLoaded(List<MessageUser> users) {
                logEvent("Loaded " + users.size() + " users");
                for (MessageUser user : users) {
                    logEvent("  - " + user.name + " (id=" + user.userId + ")");
                }
            }

            @Override
            public void onMessageFailed(String reason) {
                logEvent("Load users failed: " + reason);
            }
        });
    }

    private void sendMessage() {
        String userIdStr = etUserId.getText() != null ? etUserId.getText().toString().trim() : "";
        if (userIdStr.isEmpty()) {
            Toast.makeText(this, "Enter a User ID", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
            return;
        }

        logEvent("Sending message to user " + userId + "...");
        PopinAgent.getInstance().sendMessage(userId, "Hello from agent SDK demo!", "", new PopinAgentMessageListener() {
            @Override
            public void onMessageSent() {
                logEvent("Message sent to user " + userId);
            }

            @Override
            public void onMessageFailed(String reason) {
                logEvent("Send failed: " + reason);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PopinAgent.deinit();
    }
}
