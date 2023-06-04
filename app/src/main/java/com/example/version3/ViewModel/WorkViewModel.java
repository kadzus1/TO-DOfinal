package com.example.version3.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.version3.Repo.WorkRepo;
import com.example.version3.model.Work;

import java.util.ArrayList;
import java.util.List;

public class WorkViewModel extends AndroidViewModel {
    private WorkRepo workRepository;
    private LiveData<List<Work>> allWork;
    private MutableLiveData<List<Work>> filteredWork;


    public WorkViewModel(Application application) {
        super(application);
        //Tworzy instancję repozytorium
        workRepository = new WorkRepo(application);
        //Pobiera listę wszystkich zadań
        allWork = workRepository.getAllWork();
        //tworzy instancję 'MutableLiveData' dla filtrowanych zadań
        filteredWork = new MutableLiveData<>();
    }

    //Zawiera listę filtrowanych zadań
    public LiveData<List<Work>> getFilteredWork() {
        return filteredWork;
    }


    //Filtruje listę wszystkich zadań, na podstawie określonej kategorii
    //Ustawia wartość filtrowanej listy
    //Metoda iteruje po wszytskich zadaniach i dodaje do listy filtrowanej tylko te zadania, które należą do określonych kategorii
    public void filterByCategory(String category) {
        List<Work> allWorkList = allWork.getValue();
        if (allWorkList != null) {
            List<Work> filteredList = new ArrayList<>();
            for (Work work : allWorkList) {
                if (work.getCategory().equals(category)) {
                    filteredList.add(work);
                }
            }
            filteredWork.setValue(filteredList);
        }
    }


    //Wywołuje metodę z repozytorium w celu zaktualizowania zadania
    public void updateWork(Work work) {
        workRepository.updateWork(work);
    }


    //Wywołuje metodę z repozytorium w celu usunięcia zadania
    public void deleteWork(Work work) {
        workRepository.deleteWork(work);
    }

    //Lista zawiera wszystkie zadania
    public LiveData<List<Work>> getAllWork() {
        return allWork;
    }
}
