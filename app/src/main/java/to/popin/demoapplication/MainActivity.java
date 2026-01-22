package to.popin.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import to.popin.androidsdk.Popin;
import to.popin.androidsdk.PopinEventsListener;
import to.popin.androidsdk.PopinInitListener;
import to.popin.demoapplication.popin.PopinConnectingDialog;


public class MainActivity extends AppCompatActivity {
    private boolean callCancelled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Popin.init(MainActivity.this, "vijith_kk", "9876543210", true, new PopinInitListener() {
            @Override
            public void onInitComplete() {

            }

            @Override
            public void onInitFailed(String s) {

            }
        });
        Button buttonCall = findViewById(R.id.buttonCall);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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

                    }

                    @Override
                    public void onCallConnected() {
                        callCancelled = true;

                    }

                    @Override
                    public void onCallFailed() {
                        callCancelled = true;

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
