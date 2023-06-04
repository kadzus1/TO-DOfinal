package com.example.version3.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.version3.R;
import com.example.version3.model.Work;

public class NotificationReceiver extends BroadcastReceiver {

    //Metoda wywoływana po otrzymaniu powiadomienia
    @Override
    public void onReceive(Context context, Intent intent) {
        // Pobiera dane powiadomienia
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        //Parcelable umożliwia przekazywanie danych pomiędzy różnymi komponentami
        Work work = intent.getParcelableExtra("work");

        // Wywołuje metodę do wyświetlania powiadomienia
        showNotification(context, title, description, work);
    }


    private void showNotification(Context context, String title, String description, Work work) {
        int rnds = (int) (Math.random() * 1000);

        // Tworzenie intentu dla ListActivity
        Intent intent = new Intent(context, ListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Dodaj flagę FLAG_ACTIVITY_NEW_TASK, aby uruchomić aktywność z poza stosu zadań

        // Tworzenie PendingIntent na podstawie intentu
        PendingIntent pendingIntent = PendingIntent.getActivity(context, rnds, intent, PendingIntent.FLAG_IMMUTABLE);

        // Tworzenie obiektu NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.task)
                .setContentTitle("Twoje zadanie: " + title + " wymaga akcji!")
                .setContentText(description)
                .setContentIntent(pendingIntent) // Ustawienie PendingIntent jako akcji po kliknięciu powiadomienia
                .setAutoCancel(true);

        // Wysyłanie powiadomienia
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "TO-DO_noti", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, builder.build());
    }


}

