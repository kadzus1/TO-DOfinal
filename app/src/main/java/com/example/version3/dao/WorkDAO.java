package com.example.version3.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.version3.model.Work;

import java.util.List;

@Dao
public interface WorkDAO {

    //Wstawia obiekt 'Work' do bazy danych
    @Insert
    void insert(Work work);

    //Pobiera wszystkie obiekty 'Work' z bazy danych jako listę
    //Zwraca wynik w postaci 'LiveData', co umożliwia obserwowanie zmian danych
    @Query("SELECT * FROM work ORDER BY title")
    LiveData<List<Work>> getAllWork();

    //Pobiera obiekt 'Work' o określonym id z bazy danych
    @Query("SELECT * FROM work WHERE id=:workId")
    LiveData<Work> getWorkById(int workId);

    //Usuwa obiekt 'Work' z bazy danych
    @Delete
    void delete(Work work);

    //Aktualizuje obiekt 'Work' z bazy danych
    @Update
    void update(Work work);

    //Pobiera obiekty 'Work' z określona kategorią z bazy danych
    @Query("SELECT * FROM work WHERE category=:category")
    LiveData<List<Work>> getWorkByCategory(String category);


}
