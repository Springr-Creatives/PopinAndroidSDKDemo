package to.popin.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import to.popin.androidsdk.Popin;
import to.popin.androidsdk.PopinCreateScheduleListener;
import to.popin.androidsdk.PopinEventsListener;
import to.popin.androidsdk.PopinScheduleListener;
import to.popin.androidsdk.models.ScheduleSlotsModel;
import to.popin.demoapplication.popin.PopinConnectingDialog;
import to.popin.demoapplication.popin.PopinScheduleDialog;

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
                Popin.getInstance().startConnection(new PopinEventsListener() {

                    @Override
                    public void onCallStart() {
                       cdd.dismiss();

                    }

                    @Override
                    public void onAllExpertsBusy() {
                        cdd.dismiss();
                        showSchedule();
                    }

                    @Override
                    public void onCallConnected() {

                    }

                    @Override
                    public void onCallDisconnected() {
                        cdd.dismiss();
                    }


                });
            }
        });
    }

    private void showSchedule() {

        Popin.getInstance().getAvailableScheduleSlots(new PopinScheduleListener() {
            @Override
            public void onAvailableScheduleLoaded(List<ScheduleSlotsModel.ScheduleSlot> scheduleSlots) {
                PopinScheduleDialog cdd = new PopinScheduleDialog(MainActivity.this, scheduleSlots, scheduletext ->
                        Popin.getInstance().createSchedule(scheduletext, new PopinCreateScheduleListener() {
                            @Override
                            public void onScheduleCreated() {
                                Toast.makeText(MainActivity.this,"Your call has been scheduled", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onScheduleLoadError() {

                            }
                        }));

                cdd.show();
            }

            @Override
            public void onScheduleLoadError() {

            }
        });
    }

}