package com.example.version3.Activity;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.version3.R;
import com.example.version3.model.Work;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class WorkDetailsActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView categoryTextView;

    private Button setReminderButton;
    private Work work;

    private ImageView alertImageView;

    public static final String EXTRA_WORK = "work";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details_activity);

        titleTextView = findViewById(R.id.task_title_textview);
        descriptionTextView = findViewById(R.id.task_description_textview);
        categoryTextView = findViewById(R.id.task_category_textview);
        setReminderButton = findViewById(R.id.button_set_reminder);
        alertImageView = findViewById(R.id.alert_imageview);
        alertImageView.setVisibility(View.INVISIBLE);


        // Pobiera obiekt 'Work' z Intentu i ustawia szczegóły zadania na odpowiednich widokach
        Bundle bundle = getIntent().getExtras();
        Long time = null;
        if (bundle != null) {
            // Odczytaj obiekt Work z Bundle
            work = bundle.getParcelable(EXTRA_WORK);

            if (work != null) {
                titleTextView.setText(work.getTitle());
                descriptionTextView.setText(work.getDescription());
                categoryTextView.setText(work.getCategory());
                time = work.getTimeInMillis();
            }
        }

        //Po klinięciu przycisku wyświetla kalendarz i zegar
        setReminderButton.setOnClickListener(v -> {
            showDateTimePicker();
        });
        long currentTimeInMillis = System.currentTimeMillis();

        if (time <= currentTimeInMillis) {
            showAlertImage();
        }

    }


    //Ustawia obrazek alertu, po wykonaniu zadania
    private void showAlertImage() {
        alertImageView.setVisibility(View.VISIBLE);
        alertImageView.setImageResource(R.drawable.done);
    }

    private void showDateTimePicker() {
        Calendar currentDate = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                currentDate.set(Calendar.YEAR, year);
                currentDate.set(Calendar.MONTH, monthOfYear);
                currentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        currentDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentDate.set(Calendar.MINUTE, minute);
                        currentDate.set(Calendar.SECOND, 0);

                        createAutoNotification(currentDate.getTimeInMillis());
                    }
                };

                new TimePickerDialog(WorkDetailsActivity.this, timeListener, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
            }
        };

        new DatePickerDialog(WorkDetailsActivity.this, dateListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
    }


    //Tworzy powiadomienie przy użyciu 'AlarmManager' i 'PendingIntent'
    //Generuje losowy id dla obiektu 'PendingIntent', tworzy intencję dla klasy 'NotificationReciver'
    //Ustawia dane, tworzy obiekt 'PendingIntent'.
    //Na koniec korzysta z obiektu 'AlarmManager' do zaplanowania wyświetlenia powiadomienia o określonym czasie
    private void createAutoNotification(long timeInMillis) {
        int rnds = (int) (Math.random() * 1000);

        String title = work.getTitle();
        String description = descriptionTextView.getText().toString().trim();

    // Tworzenie intencji dla powiadomienia
        Intent notificationIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("description", description);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), rnds, notificationIntent, PendingIntent.FLAG_IMMUTABLE);


        // Utworzenie instancji AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Ustawienie powiadomienia na określonym czasie
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
