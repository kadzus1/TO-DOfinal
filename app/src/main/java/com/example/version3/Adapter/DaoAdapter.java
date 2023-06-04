package com.example.version3.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.version3.Activity.MainActivity;
import com.example.version3.Activity.WorkDetailsActivity;
import com.example.version3.R;
import com.example.version3.model.Work;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DaoAdapter extends RecyclerView.Adapter<DaoAdapter.MyViewHolder> {
    private List<Work> listOfWork;
    private MainActivity context;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(Work workItem, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public DaoAdapter(List<Work> listOfWork) {
        this.listOfWork = listOfWork;
    }



   //Tworzy obiekt 'MyViewHolder', który przechowuje referencje do widoków wewnątrz elementu listy
   @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(row);
        viewHolder.delete_button = row.findViewById(R.id.delete_button);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Work currentWork = listOfWork.get(position);
        holder.title_task_textview.setText(listOfWork.get(position).getTitle());
        holder.description_task_textview.setText(listOfWork.get(position).getDescription());
        holder.category_task_textview.setText(listOfWork.get(position).getCategory());

    // Przypisanie wartości do pól dotyczących daty
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(listOfWork.get(position).getTimeInMillis());
        holder.day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        holder.date.setText(new SimpleDateFormat("MMM").format(calendar.getTime()));
        holder.month.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        holder.year.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        String list = listOfWork.get(position).getTitle();

        //Ustawia możliwość kliknięcia przycisku usuwania zadania
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int clickedPosition = holder.getAdapterPosition();
                    listener.onItemClick(currentWork, clickedPosition);
                }
            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), WorkDetailsActivity.class);
                intent.putExtra("work", listOfWork.get(position));

                holder.itemView.getContext().startActivity(intent);
            }

        });
    }

    //Zwraca liczbę elementów na liście 'listOfWork'
    @Override
    public int getItemCount() {
        return listOfWork.size();
    }

    public List<Work> getWorkList() {
        return listOfWork;
    }


    public List<Work> setWorkList(List<Work> workList) {
        return workList;

    }


    //Przechowuje referencje do widoków wewnatrz elementu listy
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title_task_textview;
        public TextView description_task_textview;
        public TextView category_task_textview;
        public TextView delete_button;
        public TextView day;
        public TextView date;
        public TextView month;
        public TextView year;
        private List<Work> workList;


        public MyViewHolder(View view) {
            super(view);
            title_task_textview = view.findViewById(R.id.title_task_textview);
            description_task_textview = view.findViewById(R.id.description_task_textview);
            category_task_textview = view.findViewById(R.id.category_task_textview);
            day = view.findViewById(R.id.day);
            date = view.findViewById(R.id.date);
            month = view.findViewById(R.id.month);
            year = view.findViewById(R.id.year);
        }
    }

}
