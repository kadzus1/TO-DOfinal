package com.example.version3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.version3.dao.WorkDAO;
import com.example.version3.model.Work;


@Database(entities = {Work.class}, version = 5)
public abstract class DatabaseHelper extends RoomDatabase {

    //Abstrakcyjna meotda, zwracjąca obiekt interfesju 'WorkDAO'
    //Pozwala na wykonywanie operacji na bazie danych - operacje CRUD
    public abstract WorkDAO workDao();

    public static Context context;

    private static volatile DatabaseHelper INSTANCE;

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Room.databaseBuilder(context, DatabaseHelper.class, "task_database")
                    .addMigrations(MIGRATION_4_5)
                    .build();
        }
    };


    //Wykorzystuje wzorzec Singleton, aby zapewnić, że istnieje tylko jedna instancja bazy danych w aplikacji
    public static DatabaseHelper getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    DatabaseHelper.class,
                                    "task_database")
                            .build();
                }
            }
        }
        return INSTANCE;

    }



}
