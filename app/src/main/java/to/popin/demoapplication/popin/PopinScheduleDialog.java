package to.popin.demoapplication.popin;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


import to.popin.androidsdk.models.ScheduleSlotsModel;
import to.popin.demoapplication.R;


public class PopinScheduleDialog extends Dialog {

    private List<ScheduleSlotsModel.ScheduleSlot> scheduleSlots;
    private String[] dateList, timeList;
    private Spinner spinnerDate, spinnerTime;
    private Button buttonClose, buttonSchedule;
    private Context context;
    private String scheduleText = "";
    private PopinScheduleListener popinScheduleListener;

    public PopinScheduleDialog(@NonNull Context context, List<ScheduleSlotsModel.ScheduleSlot> _scheduleSlots, PopinScheduleListener popinScheduleListener) {
        super(context);
        this.context = context;
        this.scheduleSlots = _scheduleSlots;
        this.popinScheduleListener = popinScheduleListener;
    }

    public String getScheduleText() {
        return scheduleText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_schedule_confirm);
        spinnerDate = findViewById(R.id.spinnerDate);
        spinnerTime = findViewById(R.id.spinnerTime);
        buttonClose = findViewById(R.id.buttonClose);
        buttonSchedule = findViewById(R.id.buttonSchedule);
        ArrayList<String> dateArrayList = new ArrayList<String>();
        for (ScheduleSlotsModel.ScheduleSlot scheduleSlot : scheduleSlots) {
            dateArrayList.add(scheduleSlot.date);
        }

        dateList = new String[dateArrayList.size()];
        dateList = dateArrayList.toArray(dateList);

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, dateList);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(dateAdapter);
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.WHITE);
                setTimeSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (dateArrayList.size() > 0) {
            spinnerDate.setSelection(0, true);
        }
        buttonClose.setOnClickListener(v -> dismiss());
        buttonSchedule.setOnClickListener(v -> {
            popinScheduleListener.onScheduleClick(scheduleText);
            dismiss();
        });
    }

    private void setTimeSpinner(int position) {
        List<String> times = scheduleSlots.get(position).timeSlots;
        // Log.e("TIME_S>" , ">" + position +">" + scheduleSlots.size());
        String[] timeList = new String[times.size()];
        timeList = times.toArray(timeList);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, timeList);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.WHITE);
                scheduleText = spinnerDate.getSelectedItem().toString() + " " + spinnerTime.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (times.size() > 0) {
            spinnerTime.setSelection(0, true);
        }
    }

    public interface PopinScheduleListener {
        void onScheduleClick(String scheduletext);
    }
}
