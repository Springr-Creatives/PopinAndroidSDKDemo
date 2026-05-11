package to.popin.demoapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AgentLoginActivity extends AppCompatActivity {

    public static final String AGENT_PREFS = "popin_agent_prefs";
    public static final String KEY_EMAIL = "agent_email";
    public static final String KEY_PIN = "agent_pin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Auto-redirect if already logged in
        SharedPreferences prefs = getSharedPreferences(AGENT_PREFS, MODE_PRIVATE);
        String savedEmail = prefs.getString(KEY_EMAIL, null);
        String savedPin = prefs.getString(KEY_PIN, null);
        if (savedEmail != null && savedPin != null) {
            startActivity(new Intent(this, AgentMainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_agent_login);

        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etPin = findViewById(R.id.etPin);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String pin = etPin.getText() != null ? etPin.getText().toString().trim() : "";

            if (email.isEmpty()) {
                etEmail.setError("Email is required");
                return;
            }
            if (pin.isEmpty()) {
                etPin.setError("Pin is required");
                return;
            }

            prefs.edit()
                    .putString(KEY_EMAIL, email)
                    .putString(KEY_PIN, pin)
                    .apply();

            startActivity(new Intent(this, AgentMainActivity.class));
            finish();
        });
    }
}
