package com.example.version3.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.version3.R;
import com.example.version3.dao.WorkDAO;
import com.example.version3.database.DatabaseHelper;
import com.example.version3.model.Work;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private WorkDAO workDao;
    private EditText edittextTitle;
    private EditText edittextDescription;
    private Spinner spinnerCategory;
    private TextView editTextDate;
    private TextView textViewDate;
    private TextView textViewTime;
    private TextView editTextTime;
    private TextView textViewDay;
    private TextView textViewMonth;
    private TextView textViewYear;
    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Linia tworzy obiekt "DatabaseHelper". Metoda zwraca instancję, która jest używana do zarządania bazą danych
        DatabaseHelper database = DatabaseHelper.getDatabase(this);

        //Pobiera DAO dla tabeli Work, które udostępnia metody do wykonywania operacji na tabeli
        workDao = database.workDao();

        //Inicjalizacja i konfiguracja widoków
        Button buttonAdd = findViewById(R.id.button_add);
        edittextTitle = findViewById(R.id.edit_text_title);
        edittextDescription = findViewById(R.id.edit_text_description);
        spinnerCategory = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        editTextDate = findViewById(R.id.edit_text_date);
        textViewDate = findViewById(R.id.text_view_date);
        editTextTime = findViewById(R.id.edit_text_time);
        textViewTime = findViewById(R.id.text_view_time);
        textViewDay = findViewById(R.id.day);
        textViewMonth = findViewById(R.id.month);
        textViewYear = findViewById(R.id.year);

        selectedDateTime = Calendar.getInstance();



        //Nasłuchiwanie zdarzenia, wywołanie metody, która pokazuje kalendarz
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });



        //Nasłuchiwanie zdarzenia, wywołanie metody, która pokazuje zegar
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });




        //Nasłuchiwanie zdarzenia na przycisku dodawania zadania
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Pobranie wartości wprowadzonych przez użytkownika w pola tekstowe, konwertowanie na łańcuch znaków
                String title = edittextTitle.getText().toString().trim();
                String description = edittextDescription.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString().trim();

                if (title.isEmpty() || description.isEmpty()) {
                    //Tworzy i wyświetla powiadomienia
                    Toast.makeText(AddActivity.this, "Wprowadź tytuł i opis", Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedDateTime.set(Calendar.SECOND, 0);
                selectedDateTime.set(Calendar.MILLISECOND, 0);

                //Tworzenie nowego obiektu work, na podstawie wprowadzonych danych
                Work work = new Work(title, description, category, selectedDateTime.getTimeInMillis());
                //Tworzenie obiektu, który asynchronicznie (odbywa się w tle) wstawia obiekt do bazy danych
                InsertWorkTask insertWorkTask = new InsertWorkTask();
                insertWorkTask.execute(work); //uruchomienie
            }
        });
    }



    //Okno dialogowe wyboru daty
    private void showDatePicker() {
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDateTime.set(Calendar.YEAR, year);
                selectedDateTime.set(Calendar.MONTH, monthOfYear);
                selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String date = dateFormat.format(selectedDateTime.getTime());
                editTextDate.setText(date);

                textViewDay.setText(String.valueOf(dayOfMonth));
                textViewMonth.setText(String.valueOf(monthOfYear + 1));
                textViewYear.setText(String.valueOf(year));
            }
        }, year, month, day);

        datePickerDialog.show();
    }



    //Okno dialogowe wyboru czasu
    private void showTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute);

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String time = timeFormat.format(selectedDateTime.getTime());
                editTextTime.setText(time);

                textViewTime.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }



    //Ta klasa jest podklasą AsyncTask, która asynchronicznie wykonuje zadania
    //Metoda wywołuje wstawienie nowy element na obieckie workDAO do tablicy 'works'
    private class InsertWorkTask extends AsyncTask<Work, Void, Void> {
        @Override
        protected Void doInBackground(Work... works) {
            workDao.insert(works[0]);
            return null;
        }


        //Po wykonaniu zadania w tle, kończy aktywność
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            createAutoNotification();
            finish();
        }


        //Metoda, tworząca automatyczne powiadomienia
        private void createAutoNotification() {
            String title = edittextTitle.getText().toString().trim();
            String description = edittextDescription.getText().toString().trim();
            long timeInMillis = selectedDateTime.getTimeInMillis();

            //Tworzony jest nowy obiekt klasy Intent, który uruchamia komponet AlarmReciver
            Intent notificationIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
            //Do powiadomienia dodawane są dodatkowe informacje
            notificationIntent.putExtra("title", title);
            notificationIntent.putExtra("description", description);

            //Tworzony jest obiekt, który zostanie wykonany w przyszłości - uruchomi BroadcastReciver w danym momenci, FLAG_IMMUTABLE oznacza, że nie można go zmienić
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            // Utworzenie instancji AlarmManager, która pozwala na planowanie i wykonanie operacji w określonych czasach lub interwałach
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            // Ustawienie powiadomienia na określonym czasie
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            }
        }
    }


    //Obsługuje menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
