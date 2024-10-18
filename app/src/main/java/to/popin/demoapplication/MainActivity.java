package to.popin.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import to.popin.androidsdk.Popin;
import to.popin.androidsdk.PopinEventsListener;
import to.popin.demoapplication.popin.PopinConnectingDialog;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonCall = findViewById(R.id.buttonCall);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopinConnectingDialog cdd=new PopinConnectingDialog(MainActivity.this);
                cdd.show();

                Popin.init(MainActivity.this);
                Popin.getInstance().startCall(new PopinEventsListener() {

                    @Override
                    public void onCallStart() {
                       cdd.dismiss();

                    }

                    @Override
                    public void onQueuePositionChanged(int i) {

                    }

                    @Override
                    public void onAllExpertsBusy() {
                        cdd.dismiss();
                    }

                    @Override
                    public void onCallConnected() {

                    }

                    @Override
                    public void onCallFailed() {
                        cdd.dismiss();
                    }

                    @Override
                    public void onCallDisconnected() {
                        cdd.dismiss();
                    }


                });
            }
        });
    }



}