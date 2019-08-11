package com.example.noteapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String ID = "package.ID";
    public static final String NOTIFICATION_ID = "package.NOTIFICATION_ID";
    public static final String TITLE = "package.TITLE";
    public static final String DESC = "package.DESC";
    public static final String TIME = "package.TIME";
    public static final String PRIORITY = "package.PRIORITY";
    public static final String NOTIFY = "package.NOTIFY";
    public static final String NOTIFY_DATE = "package.NOTIFY_DATE";
    public static final String NOTIFY_TIME = "package.NOTIFY_TIME";

    String[] priority_arr = new String[]{"High", "Normal", "Low"};
    EditText editTextTitle;
    EditText editTextdesc;
    NumberPicker numberPickerPriority;
    Switch notify_switch;
    String timeStamp = null;
    long notification_id;
    LinearLayout dateTimeContainer;
    TextView textDate, textTime;

    boolean isTime, isDate, notify;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private int aYear, aMonth, aDay, aHour, aMinute;

    String notify_date, notify_time;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextdesc = findViewById(R.id.edit_text_desc);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        notify_switch = findViewById(R.id.notification_switch);
        dateTimeContainer = findViewById(R.id.date_time_container);
        textDate = findViewById(R.id.date);
        textTime = findViewById(R.id.time);

        numberPickerPriority.setMinValue(0);
        numberPickerPriority.setMaxValue(2);
        numberPickerPriority.setDisplayedValues(priority_arr);

        note = getIntent().getParcelableExtra("key");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);

        Intent intent = getIntent();
        if (intent.hasExtra(ID)) {
            setTitle("Edit Note");
            notification_id = intent.getLongExtra(NOTIFICATION_ID, 0);
            editTextTitle.setText(intent.getStringExtra(TITLE));
            editTextdesc.setText(intent.getStringExtra(DESC));
            numberPickerPriority.setValue(intent.getIntExtra(PRIORITY, 1));
            notify = intent.getBooleanExtra(NOTIFY, false);
            notify_date = intent.getStringExtra(NOTIFY_DATE);
            notify_time = intent.getStringExtra(NOTIFY_TIME);
            timeStamp = intent.getStringExtra(TIME);

            if (notify_time == null || notify_date == null) {
                notify_time = "Set Time";
                notify_date = "Set Date";
            }
            textTime.setText(notify_time);
            textDate.setText(notify_date);
        } else {
            setTitle("Add Note");
            notification_id = System.currentTimeMillis();
        }

        Log.d("NotifyUser_", "onCreate: " + notification_id);


        notify_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (notify_switch.isChecked()) {
                dateTimeContainer.setVisibility(View.VISIBLE);
                notify = true;
                textDate.setText(notify_date);
                textTime.setText(notify_time);
            } else {
                notify_date = "Set Date";
                notify_time = "Set Time";
                dateTimeContainer.setVisibility(View.GONE);
                notify = false;
            }
        });

        textDate.setOnClickListener(v -> {
            setDate();

        });

        textTime.setOnClickListener(v -> {
            setTime();
        });

        if (notify) {
            notify_switch.setChecked(true);
            dateTimeContainer.setVisibility(View.VISIBLE);
        }

    }

    private void setTime() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) ->
                {
                    aHour = hourOfDay;
                    aMinute = minute;
                    notify_time = hourOfDay + ":" + minute;
                    textTime.setText(notify_time);
                    isTime = true;
                    if (isDate && isTime)
                        setNotification();
                }, mHour, mMinute, false);
        timePickerDialog.show();


    }

    private void setDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) ->
                {
                    aYear = year;
                    aMonth = monthOfYear;
                    aDay = dayOfMonth;
                    notify_date = dayOfMonth + " . " + (monthOfYear + 1) + " . " + year;
                    textDate.setText(notify_date);
                    isDate = true;
                    if (isDate && isTime)
                        setNotification();
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void setNotification() {
        Calendar calendar_n = Calendar.getInstance();
        calendar_n.set(aYear, aMonth, aDay, aHour, aMinute, 0);
        Log.d("NotifyUser_", "year: " + aYear + " month: " + aMonth + aDay + aHour + aMinute);

        long time_m = calendar_n.getTimeInMillis();

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotifyUser.class);
        intent.putExtra(DESC, getIntent().getStringExtra(TITLE));
        intent.putExtra(NOTIFICATION_ID, notification_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) notification_id, intent, PendingIntent.FLAG_ONE_SHOT);
        manager.setExact(AlarmManager.RTC_WAKEUP, time_m, pendingIntent);

        Toast.makeText(this, "Notification Enabled", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.save_note:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {

        String title = editTextTitle.getText().toString();
        String desc = editTextdesc.getText().toString();
        if (timeStamp == null)
            timeStamp = String.valueOf((System.currentTimeMillis()));
        String priority = priority_arr[numberPickerPriority.getValue()];


        if (title.trim().isEmpty() || desc.trim().isEmpty()) {
            Toast.makeText(this, "You cannot leave any field empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(NOTIFICATION_ID, notification_id);
        intent.putExtra(TITLE, title);
        intent.putExtra(DESC, desc);
        intent.putExtra(TIME, timeStamp);
        intent.putExtra(PRIORITY, priority);
        intent.putExtra(NOTIFY, notify);
        intent.putExtra(NOTIFY_DATE, notify_date);
        intent.putExtra(NOTIFY_TIME, notify_time);

        int id = getIntent().getIntExtra(ID, -1);
        if (id != -1) {
            intent.putExtra(ID, id);
        }

        setResult(RESULT_OK, intent);
        finish();

    }

}
