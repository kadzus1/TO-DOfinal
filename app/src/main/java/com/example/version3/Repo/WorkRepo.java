package com.example.version3.Repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.version3.dao.WorkDAO;
import com.example.version3.database.DatabaseHelper;
import com.example.version3.model.Work;

import java.util.List;

public class WorkRepo {
    private WorkDAO workDao;

    public WorkRepo(Application application) {
        //Inicjalizuje repozytorium, tworząc instancję bazy danych
        DatabaseHelper database = DatabaseHelper.getDatabase(application);
        //Pobiera DAO z bazy danych
        workDao = database.workDao();
    }

    //Asynchronicznie aktualizuje zadanie w bazie, wykorzystuje 'AsyncTask' do wykonywania operacji aktualizacji w tle
    public void updateWork(Work work) {
        new UpdateWorkAsyncTask(workDao).execute(work);
    }

    private static class UpdateWorkAsyncTask extends AsyncTask<Work, Void, Void> {
        private final WorkDAO workDao;

        private UpdateWorkAsyncTask(WorkDAO workDao) {
            this.workDao = workDao;
        }

        @Override
        protected Void doInBackground(Work... works) {
            workDao.update(works[0]);
            return null;
        }
    }

    //Obiekt typu 'LiveData', zwracający listę wszystkich zadań
    public LiveData<List<Work>> getAllWork() {
        return workDao.getAllWork();
    }


    //Asynchronicznie usuwa zadanie z bazy
    public void deleteWork(Work work) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                workDao.delete(work);
            }
        });
        //Wykorzystuje wątek 'Thread', aby wykonać operację usuwania w tle
        thread.start();
    }


}
