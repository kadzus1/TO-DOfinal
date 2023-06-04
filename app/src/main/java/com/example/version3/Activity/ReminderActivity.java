package com.example.version3.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.version3.R;
import com.example.version3.model.Work;

public class ReminderActivity extends AppCompatActivity {
    private Work work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Deklaruje zmienną instancji do przechowywania informacji o zadaniu
        work = getIntent().getParcelableExtra("work");

        Button setReminderButton = findViewById(R.id.button_set_reminder);
        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReminder();
                finish();
            }
        });
    }

    private void setReminder() {
        if (work != null) {
            // Utwórz intencję dla AlarmReceiver
            Intent intent = new Intent(this, NotificationReceiver.class);
            intent.putExtra("title", work.getTitle());
            intent.putExtra("description", work.getDescription());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            // Ustaw alarm na podaną datę i godzinę
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, work.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
