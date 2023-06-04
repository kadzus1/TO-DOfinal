package com.example.version3.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.version3.R;
import com.example.version3.ViewModel.WorkViewModel;


public class MainActivity extends AppCompatActivity {
    private WorkViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja przycisków
        Button myTaskButton = findViewById(R.id.my_task_button);
        Button addButton = findViewById(R.id.add_task_button);

        // Utworzenie nowego obiektu i przypisanie go do zmiennej viewModel
       viewModel = new WorkViewModel(getApplication());

        // Listener dla kliknięcia przycisku
        myTaskButton.setOnClickListener(view -> {
            // Tworzenie nowej aktywności ListActivity
            Intent intent = new Intent(MainActivity.this, com.example.version3.Activity.ListActivity.class);
            startActivity(intent); // Uruchomienie
        });

        // Listener dla kliknięcia przycisku
        addButton.setOnClickListener(view -> {
            // Tworzenie nowej aktywności AddActivity
            Intent intent2 = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent2); // Uruchomienie
        });
    }
}