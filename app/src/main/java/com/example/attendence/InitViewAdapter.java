package com.example.attendence;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class InitViewAdapter extends RecyclerView.Adapter<InitViewAdapter.Myholder> {
  // Boolean list;
  List<Student> list;
    Context context;
    public InitViewAdapter(Context context, List<Student> status) {
        this.context=context;
        list=status;
    }

    @NonNull
    @Override
    public InitViewAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attend_list,viewGroup,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InitViewAdapter.Myholder myholder, int i) {
    Student student=list.get(i);
    if (student.status)
     myholder.getlist.setText("P");
    else
        myholder.getlist.setText("A");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView getlist;
        public Myholder(@NonNull View itemView)
        {
            super(itemView);
          getlist=itemView.findViewById(R.id.txt_getlist);

        }

    }
}
