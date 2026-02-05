package to.popin.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessaging;

import to.popin.androidsdk.Popin;
import to.popin.androidsdk.PopinEventsListener;
import to.popin.demoapplication.popin.PopinConnectingDialog;


public class MainActivity extends AppCompatActivity {
    private boolean callCancelled = false;

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

        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(token -> {
                Log.d("FCM_TOKEN", token);
        });

        Popin.init(MainActivity.this, "vijith_kk", "9876543210");
        Button buttonCall = findViewById(R.id.buttonCall);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopinConnectingDialog cdd = new PopinConnectingDialog(MainActivity.this);
                cdd.show();

                cdd.setOnDismissListener(dialogInterface -> {
                    if (!callCancelled) {
                    Popin.getInstance().cancelCall();
                    }
                });

                callCancelled = false;

                Popin.getInstance().startCall(new PopinEventsListener() {

                    @Override
                    public void onCallStart() {
                    }

                    @Override
                    public void onQueuePositionChanged(int i) {

                    }

                    @Override
                    public void onAllExpertsBusy() {
                        callCancelled = true;
                        cdd.dismiss();
                    }

                    @Override
                    public void onCallConnected() {
                        callCancelled = true;
                        cdd.dismiss();
                    }

                    @Override
                    public void onCallFailed() {
                        callCancelled = true;
                        cdd.dismiss();
                    }

                    @Override
                    public void onCallDisconnected() {

                    }


                });
            }
        });
        Button buttonReset = findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog.showNameAndMobileDialog(MainActivity.this, (name, mobile) -> {
                    Popin.init(MainActivity.this, name, mobile);
                });
            }
        });

    }


}
