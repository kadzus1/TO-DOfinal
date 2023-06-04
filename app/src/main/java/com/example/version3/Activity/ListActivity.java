package com.example.version3.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.version3.Adapter.DaoAdapter;
import com.example.version3.R;
import com.example.version3.ViewModel.WorkViewModel;
import com.example.version3.model.Work;

import java.util.List;

public class ListActivity extends AppCompatActivity {
    private WorkViewModel viewModel;
    private DaoAdapter daoAdapter;
    private RecyclerView recyclerView;
    private LiveData<List<Work>> listOfWork;
    private LiveData<List<Work>> filteredWork;
    private ProgressBar progressBar;

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Pasek postępów
        progressBar = findViewById(R.id.progress_bar);

        //Tworzenie modelu viewModel, który odpowiada za zarządzanie danymi związanymi z zadaniami
        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(WorkViewModel.class);

        //Komponent, który wyświetla listę zadań
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //Obiekty LiveData, wyświetlające wszystkie zadania lub te przefiltrowane, po zmianach lista odświeża się
        listOfWork = viewModel.getAllWork();
        filteredWork = viewModel.getFilteredWork();

        listOfWork.observe(this, new Observer<List<Work>>() {
            @Override
            //Pobranie listy wszystkich zadań i obserwowanie ich zmian, następnie aktualizacja widoku
            public void onChanged(List<Work> work) {
                if (work.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    findViewById(R.id.app_list_nothing).setVisibility(View.VISIBLE);
                } else {
                    daoAdapter = new DaoAdapter(work);
                    recyclerView.setAdapter(daoAdapter);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    findViewById(R.id.app_list_nothing).setVisibility(View.GONE);

                    daoAdapter.setOnItemClickListener(new DaoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Work work, int position) {
                            deleteWork(work, position);
                        }
                    });
                }
            }
        });

        filteredWork.observe(this, new Observer<List<Work>>() {
            @Override
            //Pobranie filtrowanej listy zadań i obserwowanie ich zmian, następnie aktualizacja widoku
            public void onChanged(List<Work> work) {
                if (work.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    findViewById(R.id.app_list_nothing).setVisibility(View.VISIBLE);
                } else {
                    daoAdapter = new DaoAdapter(work);
                    recyclerView.setAdapter(daoAdapter);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    findViewById(R.id.app_list_nothing).setVisibility(View.GONE);

                    daoAdapter.setOnItemClickListener(new DaoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Work work, int position) {
                            deleteWork(work, position);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Metoda aktualizująca dane zadania
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("updatedWorkItem")) {
                Work updatedWork = data.getParcelableExtra("updatedWorkItem");
                viewModel.updateWork(updatedWork);
            }
        }
    }




    //Usunięcie zadania
    private void deleteWork(Work work, int position) {
        //Służy do budowania dialogów z użytkownikiem
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.del_confirmation));
        builder.setMessage(getString(R.string.del_confirmation_text));

        builder.setPositiveButton(getString(R.string.positiveButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Po potwierdzeniu dialogu zostaje wywołana metoda w viewModel, zadanie zostaje usunięte,
                //zostaje wyświetlone powiadomienie o usunięciu zadania, lista odświeża się.
                viewModel.deleteWork(work);
                daoAdapter.notifyItemRemoved(position);
                daoAdapter.notifyItemRangeChanged(position, daoAdapter.getItemCount());
            }
        });
        builder.setNegativeButton(getString(R.string.negativeButton), null);
        builder.show();
    }


    //Obsługuje opcje filtrowania
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }


    //Reaguje na wybrane pozycje z menu, jak powrót do poprzedniej aktywności czy filtrowanie
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuCategory:
                showCategoryDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Okno dialogowe z wyświetleniem dostępnych kategorii
    private void showCategoryDialog() {
        //Pobiera tablicę łańcuchów znaków z zasobu arrays.xml
        String[] categories = getResources().getStringArray(R.array.category_array);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choose_category));
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedCategory = categories[which];
                viewModel.filterByCategory(selectedCategory);
            }
        });
        builder.show();
    }


}
