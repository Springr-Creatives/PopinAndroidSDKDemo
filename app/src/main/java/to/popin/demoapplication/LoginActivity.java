package to.popin.demoapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "popin_prefs";
    public static final String KEY_USER_NAME = "popin_user_name";
    public static final String KEY_CONTACT_INFO = "popin_contact_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If already logged in, go straight to MainActivity
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.getString(KEY_USER_NAME, null) != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        TextInputEditText etName = findViewById(R.id.etName);
        TextInputEditText etPhone = findViewById(R.id.etPhone);
        Button btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";

            if (name.isEmpty()) {
                etName.setError("Name is required");
                return;
            }
            if (phone.isEmpty()) {
                etPhone.setError("Phone number is required");
                return;
            }

            // Save to SharedPreferences
            prefs.edit()
                    .putString(KEY_USER_NAME, name)
                    .putString(KEY_CONTACT_INFO, phone)
                    .apply();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
