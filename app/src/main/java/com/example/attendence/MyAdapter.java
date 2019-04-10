package com.example.attendence;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    ArrayList<Addition> movielist;
    Context context;
    public MyAdapter(ArrayList<Addition> names, AddSubject addSubject) {
        this.movielist=names;
        context=addSubject;
    }

    public MyAdapter(ArrayList<String> list, View.OnClickListener onClickListener) {
    }

    public MyAdapter(ArrayList<Addition> movielist) {
   this.movielist=movielist;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.page_view,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        Addition addition=movielist.get(i);
        myHolder.title.setText(addition.getTitle());
        myHolder.heading.setText(addition.getHeading());
        myHolder.year.setText(addition.getYear());

    }

    @Override
    public int getItemCount() {
        return movielist.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView title,heading,year;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            heading=itemView.findViewById(R.id.heading);
            year=itemView.findViewById(R.id.year);
        }
    }
}
